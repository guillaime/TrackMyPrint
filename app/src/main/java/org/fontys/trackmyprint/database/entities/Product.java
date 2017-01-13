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
public final class Product extends Entity
{
	private final String name;
	private final String image;
	private final String description;
	private final int amount;
	private final String orderId;
	private final List<String>  productPhaseIds;
	private final ReentrantLock lock;

	public Product()
	{
		super(EntityType.PRODUCT);

		this.name = null;
		this.image = null;
		this.description = null;
		this.amount = 0;
		this.orderId = null;
		this.productPhaseIds = null;
		this.lock = new ReentrantLock();
	}

	public Product(String id, String name, String image, String description, int amount, String orderId, List<String> productPhaseIds)
			throws
			IllegalArgumentException
	{
		super(EntityType.PRODUCT, id);

		Throw.ifNull(IllegalArgumentException.class, name, "name");
		Throw.ifNull(IllegalArgumentException.class, image, "image");
		Throw.ifNull(IllegalArgumentException.class, description, "description");
		Throw.ifNull(IllegalArgumentException.class, orderId, "orderId");
		Throw.ifOutOfRangeInMin(IllegalArgumentException.class, amount, "amount", 0);

		this.name = name;
		this.image = image;
		this.description = description;
		this.amount = amount;
		this.orderId = orderId;
		this.productPhaseIds = new ArrayList<>();
		this.lock = new ReentrantLock();

		if(productPhaseIds != null)
		{
			for(String productPhaseId : productPhaseIds)
			{
				if(!this.productPhaseIds.contains(productPhaseId))
				{
					this.productPhaseIds.add(productPhaseId);
				}
			}
		}
	}

	public Product(String id, String name, String image, String description, int amount, String orderId)
		throws
		IllegalArgumentException
	{
		this(id, name, image, description, amount, orderId, null);
	}

	public boolean addProductPhaseId(String productPhaseId)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, productPhaseId, "productPhaseId");

		this.lock.lock();
		try
		{
			if(this.productPhaseIds.contains(productPhaseId))
			{
				return false;
			}

			this.productPhaseIds.add(productPhaseId);

			return true;
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public boolean removeProductPhaseId(String productPhaseId)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, productPhaseId, "productPhaseId");

		this.lock.lock();
		try
		{
			this.productPhaseIds.remove(productPhaseId);

			return true;
		}
		finally
		{
			this.lock.unlock();
		}
	}

	public boolean hasProductPhase(String productPhaseId)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, productPhaseId, "productPhaseId");

		this.lock.lock();
		try
		{
			return this.productPhaseIds.contains(productPhaseId);
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

	public String getImage()
	{
		return this.image;
	}

	public String getDescription()
	{
		return this.description;
	}

	public int getAmount()
	{
		return this.amount;
	}

	public String getOrderId()
	{
		return this.orderId;
	}

	public List<String> getProductPhaseIds()
	{
		this.lock.lock();
		try
		{
			return Collections.unmodifiableList(this.productPhaseIds);
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
