package org.fontys.trackmyprint.database.entities;

import com.google.firebase.database.Exclude;

import org.fontys.trackmyprint.utils.Throw;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guido on 15-Dec-16.
 */
public final class ProductPhase extends Entity
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

	private final String id;
	private String startDate;
	private String endDate;
	private ProductPhaseStatus productPhaseStatus;
	private final String employeeId;
	private final String phaseId;
	private final ReentrantLock lock;

	public ProductPhase()
	{
		super(EntityType.PRODUCT_PHASE);

		this.id = null;
		this.startDate = null;
		this.endDate = null;
		this.productPhaseStatus = ProductPhaseStatus.NONE;
		this.employeeId = null;
		this.phaseId = null;
		this.lock = new ReentrantLock();
	}

	public ProductPhase(String id, String startDate, String endDate, ProductPhaseStatus productPhaseStatus, String employeeId, String phaseId)
		throws
		IllegalArgumentException
	{
		super(EntityType.PRODUCT_PHASE);

		Throw.ifNull(IllegalArgumentException.class, id, "id");
		Throw.ifNull(IllegalArgumentException.class, employeeId, "employeeId");
		Throw.ifNull(IllegalArgumentException.class, phaseId, "phaseId");

		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.productPhaseStatus = productPhaseStatus;
		this.employeeId = employeeId;
		this.phaseId = phaseId;
		this.lock = new ReentrantLock();
	}

	public ProductPhase(String id, String employeeId, String phaseId)
			throws
			IllegalArgumentException
	{
		this(id, null, null, ProductPhaseStatus.NONE, employeeId, phaseId);
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

	public String getStartDate()
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

	public void setStartDate(String set)
	{
		this.lock.lock();
		try
		{
			if(this.startDate == set)
			{
				return;
			}

			this.startDate = set;
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public String getEndDate()
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

	public void setEndDate(String set)
	{
		this.lock.lock();
		try
		{
			if(this.endDate == set)
			{
				return;
			}

			this.endDate = set;
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

	public String getPhaseId()
	{
		return this.phaseId;
	}

	@Exclude
	public ReentrantLock getLock()
	{
		return this.lock;
	}
}
