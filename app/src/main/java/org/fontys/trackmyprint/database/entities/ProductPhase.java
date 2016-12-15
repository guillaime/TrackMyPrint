package org.fontys.trackmyprint.database.entities;

import org.fontys.trackmyprint.utils.Throw;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guido on 15-Dec-16.
 */
public final class ProductPhase extends Observable implements Observer
{
	public enum ProductPhaseStatus
	{
		NONE(-1);

		private final int id;

		ProductPhaseStatus(int id)
		{
			this.id = id;
		}

		public static ProductPhaseStatus findById(int id)
		{
			for(ProductPhaseStatus productPhaseStatus : ProductPhaseStatus.values())
			{
				if(productPhaseStatus.id == id)
				{
					return productPhaseStatus;
				}
			}

			return NONE;
		}

		public int getId()
		{
			return this.id;
		}
	}

	private final String productPhaseId;
	private Calendar startDate;
	private Calendar endDate;
	private ProductPhaseStatus productPhaseStatus;
	private final Employee employee;
	private final Phase phase;
	private final ReentrantLock lock;
	private final int hashCode;

	public ProductPhase(String productPhaseId, Calendar startDate, Calendar endDate, ProductPhaseStatus productPhaseStatus, Employee employee, Phase phase)
		throws
		IllegalArgumentException
	{
		Throw.IfNull(IllegalArgumentException.class, productPhaseId, "productPhaseId");
		Throw.IfNull(IllegalArgumentException.class, employee, "employee");
		Throw.IfNull(IllegalArgumentException.class, phase, "phase");

		this.productPhaseId = productPhaseId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.productPhaseStatus = productPhaseStatus;
		this.employee = employee;
		this.phase = phase;
		this.lock = new ReentrantLock();

		this.employee.addObserver(this);
		this.phase.addObserver(this);

		this.hashCode = 31 * this.productPhaseId.hashCode();
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

	public String getProductPhaseId()
	{
		return this.productPhaseId;
	}

	public Calendar getStartDate()
	{
		this.lock.lock();
		try
		{
			return this.startDate;
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public void setStartDate(Calendar set)
	{
		this.lock.lock();
		try
		{
			if(this.startDate == set)
			{
				return;
			}

			this.startDate = set;

			notifyObservers();
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public Calendar getEndDate()
	{
		this.lock.lock();
		try
		{
			return this.endDate;
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public void setEndDate(Calendar set)
	{
		this.lock.lock();
		try
		{
			if(this.endDate == set)
			{
				return;
			}

			this.endDate = set;

			notifyObservers();
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public ProductPhaseStatus getProductPhaseStatus()
	{
		this.lock.lock();
		try
		{
			return this.productPhaseStatus;
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public void setProductPhaseStatus(ProductPhaseStatus set)
	{
		this.lock.lock();
		try
		{
			if(this.productPhaseStatus == set)
			{
				return;
			}

			this.productPhaseStatus = set;

			notifyObservers();
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public Employee getEmployee()
	{
		return this.employee;
	}

	public Phase getPhase()
	{
		return this.phase;
	}

	public ReentrantLock getLock()
	{
		return this.lock;
	}
}
