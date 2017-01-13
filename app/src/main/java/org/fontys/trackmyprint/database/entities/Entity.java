package org.fontys.trackmyprint.database.entities;

import com.google.firebase.database.Exclude;

import org.fontys.trackmyprint.utils.Throw;

/**
 * Created by guido on 15-Dec-16.
 */
public abstract class Entity
{
	public enum EntityType
	{
		EMPLOYEE,
		ORDER,
		PHASE,
		PRODUCT,
		PRODUCT_PHASE,
		USER
	}

	private final EntityType entityType;
	private final String id;

	protected Entity(EntityType entityType, String id)
	{
		Throw.ifNull(IllegalArgumentException.class, id, "id");

		this.entityType = entityType;
		this.id = id;
	}

	protected Entity(EntityType entityType)
	{
		this.entityType = entityType;
		this.id = null;
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

	@Exclude
	public EntityType getEntityType()
	{
		return this.entityType;
	}

	public String getId()
	{
		return this.id;
	}
}
