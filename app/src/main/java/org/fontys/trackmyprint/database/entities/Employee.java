package org.fontys.trackmyprint.database.entities;

import org.fontys.trackmyprint.utils.Throw;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guido on 15-Dec-16.
 */
public final class Employee extends Observable implements Observer
{
	private final String employeeId;
	private Phase phase;
	private final String name;
	private final ReentrantLock lock;
	private final int hashCode;

	public Employee(String employeeId, Phase phase, String name)
			throws
			IllegalArgumentException
	{
		Throw.IfNull(IllegalArgumentException.class, employeeId, "employeeId");
		Throw.IfNull(IllegalArgumentException.class, name, "name");

		this.employeeId = employeeId;
		this.phase = phase;
		this.name = name;
		this.lock = new ReentrantLock();

		if(this.phase != null)
		{
			this.phase.addObserver(this);
		}

		this.hashCode = 31 * this.employeeId.hashCode();
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
			notifyObservers();
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public String getEmployeeId()
	{
		return this.employeeId;
	}

	public Phase getPhase()
	{
		this.lock.lock();
		try
		{
			return this.phase;
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public void setPhase(Phase set)
	{
		this.lock.lock();
		try
		{
			if(this.phase == set)
			{
				return;
			}

			if(this.phase != null)
			{
				this.phase.deleteObserver(this);
			}

			this.phase = set;

			if(this.phase != null)
			{
				this.phase.addObserver(this);
			}

			notifyObservers();
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

	public ReentrantLock getLock()
	{
		return this.lock;
	}
}
