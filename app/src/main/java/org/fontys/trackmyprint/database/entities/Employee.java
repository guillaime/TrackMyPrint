package org.fontys.trackmyprint.database.entities;

import com.google.firebase.database.Exclude;

import org.fontys.trackmyprint.utils.Throw;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guido on 15-Dec-16.
 */
public final class Employee extends Entity
{
	private final String id;
	private String phaseId;
	private Phase phase;

	private final String name;
	private final ReentrantLock lock;

	private Employee()
	{
		super(EntityType.EMPLOYEE);

		this.id = null;
		this.phaseId = null;
		this.name = null;
		this.lock = new ReentrantLock();
	}

	public Employee(String id, String phaseId, String name)
			throws
			IllegalArgumentException
	{
		super(EntityType.EMPLOYEE);

		Throw.ifNull(IllegalArgumentException.class, id, "id");
		Throw.ifNull(IllegalArgumentException.class, name, "name");

		this.id = id;
		this.phaseId = phaseId;
		this.name = name;
		this.lock = new ReentrantLock();
	}

	public Employee(String id, String name)
		throws
		IllegalArgumentException
	{
		this(id, null, name);
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

	public Phase getPhase()
	{
		return this.phase;
	}

	public void setPhase(Phase phase)
	{
		this.lock.lock();
		try
		{
			this.phase = phase;
		}
		finally
		{
			this.lock.unlock();
		}
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
