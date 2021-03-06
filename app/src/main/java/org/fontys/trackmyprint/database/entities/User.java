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
public final class User extends Entity
{
	private final List<String> orderIds;
	private final ReentrantLock lock;

	private User()
	{
		super(EntityType.USER);

		this.orderIds = null;
		this.lock = new ReentrantLock();
	}

	public User(String id, List<String> orderIds)
			throws
			IllegalArgumentException
	{
		super(EntityType.USER, id);

		this.orderIds = new ArrayList<>();
		this.lock = new ReentrantLock();

		if(orderIds != null)
		{
			for(String orderId : orderIds)
			{
				if(!this.orderIds.contains(orderId))
				{
					this.orderIds.add(orderId);
				}
			}
		}
	}

	public User(String id)
		throws
		IllegalArgumentException
	{
		this(id, null);
	}

	public boolean addOrderId(String orderId)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, orderId, "orderId");

		this.lock.lock();
		try
		{
			if(this.orderIds.contains(orderId))
			{
				return false;
			}

			this.orderIds.add(orderId);

			return true;
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public boolean removeOrderId(String orderId)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, orderId, "orderId");

		this.lock.lock();
		try
		{
			return this.orderIds.remove(orderId);
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
			return this.orderIds.contains(orderId);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public List<String> getOrderIds()
	{
		this.lock.lock();
		try
		{
			return Collections.unmodifiableList(this.orderIds);
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
