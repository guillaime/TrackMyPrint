package org.fontys.trackmyprint.database.entities;

import com.google.firebase.database.Exclude;

import org.fontys.trackmyprint.utils.Throw;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guido on 15-Dec-16.
 */
public final class Employee extends Entity
{
	private String phaseId;
	private final String name;
	private final ReentrantLock lock;

	private Employee()
	{
		super(EntityType.EMPLOYEE);

		this.phaseId = null;
		this.name = null;
		this.lock = new ReentrantLock();
	}

	public Employee(String id, String phaseId, String name)
			throws
			IllegalArgumentException
	{
		super(EntityType.EMPLOYEE, id);

		Throw.ifNull(IllegalArgumentException.class, name, "name");

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

	public String getPhaseId()
	{
		return this.phaseId;
	}

	public void setPhaseId(String phaseId)
	{
		this.lock.lock();
		try
		{
			this.phaseId = phaseId;
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
