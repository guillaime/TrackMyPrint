package org.fontys.trackmyprint.database.entities;

import com.google.firebase.database.Exclude;

import org.fontys.trackmyprint.utils.Throw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guido on 15-Dec-16.
 */
public final class Order extends Entity
{
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

	private final String id;
	private final String userId;
	private OrderStatus orderStatus;
	private final String orderDate;
	private final List<String> productIds;
	private final ReentrantLock lock;

	public Order()
	{
		super(EntityType.ORDER);

		this.id = null;
		this.userId = null;
		this.orderStatus = OrderStatus.NONE;
		this.orderDate = null;
		this.productIds = null;
		this.lock = new ReentrantLock();
	}

	public Order(String id, String userId, OrderStatus orderStatus, String orderDate, List<String> productIds)
		throws
		IllegalArgumentException
	{
		super(EntityType.ORDER);

		Throw.ifNull(IllegalArgumentException.class, id, "id");
		Throw.ifNull(IllegalArgumentException.class, userId, "userId");
		Throw.ifNull(IllegalArgumentException.class, orderDate, "orderDate");

		this.id = id;
		this.userId = userId;
		this.orderStatus = orderStatus;
		this.orderDate = orderDate;
		this.productIds = new ArrayList<>();
		this.lock = new ReentrantLock();

		if(productIds != null)
		{
			for(String productId : productIds)
			{
				if(!this.productIds.contains(productId))
				{
					this.productIds.add(productId);
				}
			}
		}
	}

	public Order(String id, String userId, OrderStatus orderStatus, String orderDate)
			throws
			IllegalArgumentException
	{
		this(id, userId, orderStatus, orderDate, null);
	}

	public Order(String id, String userId, String orderDate, List<String> productIds)
			throws
			IllegalArgumentException
	{
		this(id, userId, OrderStatus.NONE, orderDate, productIds);
	}

	public Order(String id, String userId, String orderDate)
			throws
			IllegalArgumentException
	{
		this(id, userId, OrderStatus.NONE, orderDate, null);
	}

	@Override
	public int hashCode()
	{
		return 31 * this.id.hashCode();
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

	public boolean addProductId(String productId)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, productId, "productId");

		this.lock.lock();
		try
		{
			if(this.productIds.contains(productId))
			{
				return false;
			}

			this.productIds.add(productId);

			return true;
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public boolean removeProductId(String productId)
		throws
		IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, productId, "productId");

		this.lock.lock();
		try
		{
			return this.productIds.remove(productId);
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
			return this.productIds.contains(productId);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public String getId()
	{
		return this.id;
	}

	public String getUserId()
	{
		return this.userId;
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
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public String getOrderDate()
	{
		return this.orderDate;
	}

	public List<String> getProductIds()
	{
		this.lock.lock();
		try
		{
			return Collections.unmodifiableList(this.productIds);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Exclude
	public ReentrantLock getLock()
	{
		return this.lock;
	}
}
