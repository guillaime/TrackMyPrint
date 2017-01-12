package org.fontys.trackmyprint.database.entities;

import com.google.firebase.database.Exclude;

import org.fontys.trackmyprint.utils.Throw;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guido on 15-Dec-16.
 */
public final class Phase extends Entity
{
	private final String id;
	private final String name;
	private final ReentrantLock lock;

	public Phase()
	{
		super(EntityType.PHASE);

		this.id = null;
		this.name = null;
		this.lock = new ReentrantLock();
	}

	public Phase(String id, String name)
			throws
			IllegalArgumentException
	{
		super(EntityType.PHASE);

		Throw.ifNull(IllegalArgumentException.class, id, "id");
		Throw.ifNull(IllegalArgumentException.class, name, "name");

		this.id = id;
		this.name = name;
		this.lock = new ReentrantLock();
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

	@Override
	public String getId()
	{
		return this.id;
	}

	public String getName()
	{
		return this.name;
	}

	@Exclude
	public ReentrantLock getLock()
	{
		return this.lock;
	}
}
