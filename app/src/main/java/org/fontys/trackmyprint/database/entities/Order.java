package org.fontys.trackmyprint.database.entities;

import org.fontys.trackmyprint.utils.Throw;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guido on 15-Dec-16.
 */
public final class Order extends Entity implements Observer
{
	public enum ChangeType
	{
		NONE(-1),
		PRODUCT_ADDED(0),
		PRODUCT_REMOVED(1),
		ORDER_STATUS_CHANGED(2);

		private final int id;

		ChangeType(int id)
		{
			this.id = id;
		}

		public static ChangeType findById(int id)
		{
			for(ChangeType changeType : ChangeType.values())
			{
				if(changeType.id == id)
				{
					return changeType;
				}
			}

			return NONE;
		}

		public int getId()
		{
			return this.id;
		}
	}

	public enum OrderStatus
	{
		NONE(-1);

		private final int id;

		OrderStatus(int id)
		{
			this.id = id;
		}

		public static OrderStatus findById(int id)
		{
			for(OrderStatus status : OrderStatus.values())
			{
				if(status.id == id)
				{
					return status;
				}
			}

			return NONE;
		}

		public int getId()
		{
			return this.id;
		}
	}

	private final String orderId;
	private final User user;
	private OrderStatus orderStatus;
	private final Calendar orderDate;
	private final Map<String, Product> products;
	private final ReentrantLock lock;
	private final int hashCode;

	public Order(String orderId, User user, OrderStatus orderStatus, Calendar orderDate, Map<String, Product> products)
		throws
		IllegalArgumentException
	{
		super(EntityType.ORDER);

		Throw.ifNull(IllegalArgumentException.class, orderId, "orderId");
		Throw.ifNull(IllegalArgumentException.class, user, "user");
		Throw.ifNull(IllegalArgumentException.class, orderDate, "orderDate");

		this.orderId = orderId;
		this.user = user;
		this.orderStatus = orderStatus;
		this.orderDate = orderDate;
		this.products = new HashMap<>();
		this.lock = new ReentrantLock();

		this.user.addObserver(this);

		if(products != null)
		{
			for(Product product : products.values())
			{
				if(this.products.put(product.getId(), product) ==  null)
				{
					product.addObserver(this);
				}
			}
		}

		this.hashCode = 31 * this.orderId.hashCode();
	}

	public Order(String orderId, User user, OrderStatus orderStatus, Calendar orderDate)
			throws
			IllegalArgumentException
	{
		this(orderId, user, orderStatus, orderDate, null);
	}

	public Order(String orderId, User user, Calendar orderDate, Map<String, Product> products)
			throws
			IllegalArgumentException
	{
		this(orderId, user, OrderStatus.NONE, orderDate, products);
	}

	public Order(String orderId, User user, Calendar orderDate)
			throws
			IllegalArgumentException
	{
		this(orderId, user, OrderStatus.NONE, orderDate, null);
	}

	@Override
	public int hashCode()
	{
		return this.hashCode;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
		{
			return false;
		}

		return (hashCode() == obj.hashCode());
	}

	@Override
	public void update(Observable o, Object arg)
	{
		this.lock.lock();
		try
		{
			notifyObservers(arg);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public boolean addProduct(Product product)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, product, "product");

		this.lock.lock();
		try
		{
			if(this.products.containsKey(product.getId()))
			{
				return false;
			}

			this.products.put(product.getId(), product);
			product.addObserver(this);

			notifyObservers(new EntityChanged(this, ChangeType.PRODUCT_ADDED.getId(), product));

			return true;
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public boolean removeProduct(Product product)
		throws
		IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, product, "product");

		this.lock.lock();
		try
		{
			Product removedProduct = this.products.remove(product.getId());
			if(removedProduct == null)
			{
				return false;
			}

			removedProduct.deleteObserver(this);

			notifyObservers(new EntityChanged(this, ChangeType.PRODUCT_REMOVED.getId(), removedProduct));

			return true;
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public boolean hasProduct(String productId)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, productId, "productId");

		this.lock.lock();
		try
		{
			return this.products.containsKey(productId);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public boolean hasProduct(Product product)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, product, "product");

		this.lock.lock();
		try
		{
			return hasProduct(product.getId());
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public String getId()
	{
		return this.orderId;
	}

	public User getUser()
	{
		return this.user;
	}

	public OrderStatus getOrderStatus()
	{
		this.lock.lock();
		try
		{
			return this.orderStatus;
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public void setOrderStatus(OrderStatus set)
	{
		this.lock.lock();
		try
		{
			if(this.orderStatus != set)
			{
				return;
			}

			this.orderStatus = set;

			notifyObservers(new EntityChanged(this, ChangeType.ORDER_STATUS_CHANGED.getId()));
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public Calendar getOrderDate()
	{
		return this.orderDate;
	}

	public Map<String, Product> getProducts()
	{
		this.lock.lock();
		try
		{
			return Collections.unmodifiableMap(this.products);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public ReentrantLock getLock()
	{
		return this.lock;
	}
}
