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
public final class User extends Entity implements Observer
{
	public enum ChangeType
	{
		NONE(-1),
		ORDER_ADDED(0),
		ORDER_REMOVED(1);

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

	private final String userId;
	private final Map<String, Order> orders;
	private final ReentrantLock lock;
	private final int hashCode;

	public User(String userId, Map<String, Order> orders)
			throws
			IllegalArgumentException
	{
		super(EntityType.USER);

		Throw.ifNull(IllegalArgumentException.class, userId, "userId");

		this.userId = userId;
		this.orders = new HashMap<>();
		this.lock = new ReentrantLock();

		if(orders != null)
		{
			for(Order order : orders.values())
			{
				if(this.orders.put(order.getOrderId(), order) == null)
				{
					order.addObserver(this);
				}
			}
		}

		this.hashCode = 31 * this.userId.hashCode();
	}

	public User(String userId)
		throws
		IllegalArgumentException
	{
		this(userId, null);
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

	public boolean addOrder(Order order)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, order, "order");

		this.lock.lock();
		try
		{
			if(this.orders.containsKey(order.getOrderId()))
			{
				return false;
			}

			this.orders.put(order.getOrderId(), order);
			order.addObserver(this);

			notifyObservers(new EntityChanged(this, ChangeType.ORDER_ADDED.getId(), order));

			return true;
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public boolean removeOrder(Order order)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, order, "order");

		this.lock.lock();
		try
		{
			Order removedOrder = this.orders.remove(order.getOrderId());
			if(removedOrder == null)
			{
				return false;
			}

			removedOrder.deleteObserver(this);

			notifyObservers(new EntityChanged(this, ChangeType.ORDER_REMOVED.getId(), removedOrder));

			return true;
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public boolean hasOrder(String orderId)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, orderId, "orderId");

		this.lock.lock();
		try
		{
			return this.orders.containsKey(orderId);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public boolean hasOrder(Order order)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, order, "order");

		this.lock.lock();
		try
		{
			return hasOrder(order.getOrderId());
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public String getUserId()
	{
		return this.userId;
	}

	public Map<String, Order> getOrders()
	{
		this.lock.lock();
		try
		{
			return Collections.unmodifiableMap(this.orders);
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
