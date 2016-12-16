package org.fontys.trackmyprint.database.entities;

import com.google.firebase.database.Exclude;

import org.fontys.trackmyprint.utils.Throw;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guido on 15-Dec-16.
 */
public final class Employee extends Entity implements Observer
{
	public enum ChangeType
	{
		NONE(-1),
		PHASE_CHANGED(0);

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

	private final String employeeId;
	private String phaseId;
	private Phase phase;
	private final String name;
	private final ReentrantLock lock;

	private Employee()
	{
		super(EntityType.EMPLOYEE);

		this.employeeId = null;
		this.phaseId = null;
		this.phase = null;
		this.name = null;
		this.lock = new ReentrantLock();
	}

	public Employee(String employeeId, Phase phase, String name)
			throws
			IllegalArgumentException
	{
		super(EntityType.EMPLOYEE);

		Throw.ifNull(IllegalArgumentException.class, employeeId, "employeeId");
		Throw.ifNull(IllegalArgumentException.class, name, "name");

		this.employeeId = employeeId;
		this.phase = phase;
		this.name = name;
		this.lock = new ReentrantLock();

		if(this.phase != null)
		{
			this.phaseId = this.phase.getId();
			this.phase.addObserver(this);
		}
		else
		{
			this.phaseId = null;
		}
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

	@Override
	public String getId()
	{
		return this.employeeId;
	}

	@Exclude
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

	public String getPhaseId()
	{
		return this.phaseId;
	}

	@Exclude
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
				this.phaseId = this.phase.getId();
				this.phase.addObserver(this);
			}
			else
			{
				this.phaseId = null;
			}

			notifyObservers(new EntityChanged(this, ChangeType.PHASE_CHANGED.getId()));
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
