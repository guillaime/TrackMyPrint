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
	private final String imageFront;
	private final String imageBack;
	private final String paperSize;
	private final String paperColor;
	private final String marginLeft;
	private final String marginRight;
	private final String marginTop;
	private final String marginBottom;
	private final String description;
	private final int amount;
	private final String orderId;
	private final List<String>  productPhaseIds;
	private final ReentrantLock lock;

	public Product()
	{
		super(EntityType.PRODUCT);

		this.name = null;
		this.imageFront = null;
		this.imageBack = null;
		this.paperSize = null;
		this.paperColor = null;
		this.marginLeft = null;
		this.marginRight = null;
		this.marginTop = null;
		this.marginBottom = null;
		this.description = null;
		this.amount = 0;
		this.orderId = null;
		this.productPhaseIds = null;
		this.lock = new ReentrantLock();
	}

	public Product(String id, String name, String imageFront, String imageBack, String paperSize, String paperColor, String marginLeft, String marginRight, String marginTop, String marginBottom, String description, int amount, String orderId, List<String> productPhaseIds)
			throws
			IllegalArgumentException
	{
		super(EntityType.PRODUCT, id);

		Throw.ifNull(IllegalArgumentException.class, name, "name");
		Throw.ifNull(IllegalArgumentException.class, imageFront, "imageFront");
		Throw.ifNull(IllegalArgumentException.class, imageBack, "imageBack");
		Throw.ifNull(IllegalArgumentException.class, paperSize, "paperSize");
		Throw.ifNull(IllegalArgumentException.class, paperColor, "paperColor");
		Throw.ifNull(IllegalArgumentException.class, marginLeft, "marginLeft");
		Throw.ifNull(IllegalArgumentException.class, marginRight, "marginRight");
		Throw.ifNull(IllegalArgumentException.class, marginTop, "marginTop");
		Throw.ifNull(IllegalArgumentException.class, marginBottom, "marginBottom");
		Throw.ifNull(IllegalArgumentException.class, description, "description");
		Throw.ifNull(IllegalArgumentException.class, orderId, "orderId");
		Throw.ifOutOfRangeInMin(IllegalArgumentException.class, amount, "amount", 0);

		this.name = name;
		this.imageFront = imageFront;
		this.imageBack = imageBack;
		this.paperSize = paperSize;
		this.paperColor = paperColor;
		this.marginLeft = marginLeft;
		this.marginRight = marginRight;
		this.marginTop = marginTop;
		this.marginBottom = marginBottom;
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

	public Product(String id, String name, String imageFront, String imageBack, String paperSize, String marginLeft, String marginRight, String marginTop, String marginBottom, String paperColor, String description, int amount, String orderId)
		throws
		IllegalArgumentException
	{
		this(id, name, imageFront, imageBack, paperSize, paperColor, marginLeft, marginRight, marginTop , marginBottom , description, amount, orderId, null);
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

	public String getImageFront()
	{
		return this.imageFront;
	}

	public String getImageBack()
	{
		return this.imageBack;
	}

	public String getDescription()
	{
		return this.description;
	}

	public String getPaperSize() {
  		return this.paperSize;
	}

	public String getPaperColor() {
		return this.paperColor;
	}

	public int getAmount()
	{
		return this.amount;
	}

	public String getMarginTop()
	{
		return this.marginTop;
	}
	public String getMarginLeft()
	{
		return this.marginLeft;
	}
	public String getMarginRight()
	{
		return this.marginRight;
	}
	public String getMarginBottom()
	{
		return this.marginBottom;
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
