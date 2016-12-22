package org.fontys.trackmyprint.database.entities;

import com.google.firebase.database.Exclude;

import org.fontys.trackmyprint.utils.Throw;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guido on 15-Dec-16.
 */
public final class Employee extends Entity
{
	private final String employeeId;
	private String phaseId;
	private final String name;
	private final ReentrantLock lock;

	private Employee()
	{
		super(EntityType.EMPLOYEE);

		this.employeeId = null;
		this.phaseId = null;
		this.name = null;
		this.lock = new ReentrantLock();
	}

	public Employee(String employeeId, String phaseId, String name)
			throws
			IllegalArgumentException
	{
		super(EntityType.EMPLOYEE);

		Throw.ifNull(IllegalArgumentException.class, employeeId, "employeeId");
		Throw.ifNull(IllegalArgumentException.class, name, "name");

		this.employeeId = employeeId;
		this.phaseId = phaseId;
		this.name = name;
		this.lock = new ReentrantLock();
	}

	public Employee(String employeeId, String name)
		throws
		IllegalArgumentException
	{
		this(employeeId, null, name);
	}

	@Override
	public int hashCode()
	{
		return 31 * this.employeeId.hashCode();
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
		return this.employeeId;
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
