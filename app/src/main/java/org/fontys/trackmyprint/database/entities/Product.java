package org.fontys.trackmyprint.database.entities;

import org.fontys.trackmyprint.utils.Throw;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guido on 15-Dec-16.
 */
public final class Product extends Entity implements Observer
{
	public enum ChangeType
	{
		NONE(-1),
		PRODUCT_PHASE_ADDED(0),
		PRODUCT_PHASE_REMOVED(1);

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

	private final String productId;
	private final String name;
	private final String image;
	private final String description;
	private final int amount;
	private final Order order;
	private final Map<String, ProductPhase> productPhases;
	private final ReentrantLock lock;
	private final int hashCode;

	public Product(String productId, String name, String image, String description, int amount, Order order, Map<String, ProductPhase> productPhases)
			throws
			IllegalArgumentException
	{
		super(EntityType.PRODUCT);

		Throw.ifNull(IllegalArgumentException.class, productId, "productId");
		Throw.ifNull(IllegalArgumentException.class, name, "name");
		Throw.ifNull(IllegalArgumentException.class, image, "image");
		Throw.ifNull(IllegalArgumentException.class, description, "description");
		Throw.ifNull(IllegalArgumentException.class, order, "order");
		Throw.ifOutOfRangeInMin(IllegalArgumentException.class, amount, "amount", 0);

		this.productId = productId;
		this.name = name;
		this.image = image;
		this.description = description;
		this.amount = amount;
		this.order = order;
		this.productPhases = new HashMap<>();
		this.lock = new ReentrantLock();

		this.order.addObserver(this);

		if(productPhases != null)
		{
			for(ProductPhase productPhase : productPhases.values())
			{
				if(this.productPhases.put(productPhase.getId(), productPhase) == null)
				{
					productPhase.addObserver(this);
				}
			}
		}

		this.hashCode = 31 * this.productId.hashCode();
	}

	public Product(String productId, String name, String image, String description, int amount, Order order)
		throws
		IllegalArgumentException
	{
		this(productId, name, image, description, amount, order, null);
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

	public boolean addProductPhase(ProductPhase productPhase)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, productPhase, "productPhase");

		this.lock.lock();
		try
		{
			if(this.productPhases.containsKey(productPhase.getId()))
			{
				return false;
			}

			this.productPhases.put(productPhase.getId(), productPhase);
			productPhase.addObserver(this);

			notifyObservers(new EntityChanged(this, ChangeType.PRODUCT_PHASE_ADDED.getId(), productPhase));

			return true;
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public boolean removeProductPhase(ProductPhase productPhase)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, productPhase, "productPhase");

		this.lock.lock();
		try
		{
			ProductPhase removedProductPhase = this.productPhases.remove(productPhase.getId());
			if(removedProductPhase == null)
			{
				return false;
			}

			removedProductPhase.deleteObserver(this);

			notifyObservers(new EntityChanged(this, ChangeType.PRODUCT_PHASE_REMOVED.getId(), removedProductPhase));

			return true;
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public boolean hasProductPhase(String productPhaseId)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, productPhaseId, "productPhaseId");

		this.lock.lock();
		try
		{
			return this.productPhases.containsKey(productPhaseId);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public boolean hasProductPhase(ProductPhase productPhase)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, productPhase, "productPhase");

		this.lock.lock();
		try
		{
			return hasProductPhase(productPhase.getId());
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public String getId()
	{
		return this.productId;
	}

	public String getName()
	{
		return this.name;
	}

	public String getImage()
	{
		return this.image;
	}

	public String getDescription()
	{
		return this.description;
	}

	public int getAmount()
	{
		return this.amount;
	}

	public Order getOrder()
	{
		return this.order;
	}

	public Map<String, ProductPhase> getProductPhases()
	{
		this.lock.lock();
		try
		{
			return Collections.unmodifiableMap(this.productPhases);
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
