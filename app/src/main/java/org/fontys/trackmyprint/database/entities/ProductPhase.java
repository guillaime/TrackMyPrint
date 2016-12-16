package org.fontys.trackmyprint.database.entities;

import org.fontys.trackmyprint.utils.Throw;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guido on 15-Dec-16.
 */
public final class ProductPhase extends Entity implements Observer
{
	public enum ChangeType
	{
		NONE(-1),
		START_DATE_CHANGED(0),
		END_DATE_CHANGED(1),
		PRODUCT_PHASE_STATUS_CHANGED(2);

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
		super(EntityType.PRODUCT_PHASE);

		Throw.ifNull(IllegalArgumentException.class, productPhaseId, "productPhaseId");
		Throw.ifNull(IllegalArgumentException.class, employee, "employee");
		Throw.ifNull(IllegalArgumentException.class, phase, "phase");

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

	public ProductPhase(String productPhaseId, Employee employee, Phase phase)
			throws
			IllegalArgumentException
	{
		this(productPhaseId, null, null, ProductPhaseStatus.NONE, employee, phase);
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

	@Override
	public String getId()
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

			notifyObservers(new EntityChanged(this, ChangeType.START_DATE_CHANGED.getId()));
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

			notifyObservers(new EntityChanged(this, ChangeType.END_DATE_CHANGED.getId()));
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

			notifyObservers(new EntityChanged(this, ChangeType.PRODUCT_PHASE_STATUS_CHANGED.getId()));
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
