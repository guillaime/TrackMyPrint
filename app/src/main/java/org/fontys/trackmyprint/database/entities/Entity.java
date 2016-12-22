package org.fontys.trackmyprint.database.entities;

import com.google.firebase.database.Exclude;

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

	protected Entity(EntityType entityType)
	{
		this.entityType = entityType;
	}

	@Exclude
	public EntityType getEntityType()
	{
		return this.entityType;
	}

	public abstract String getId();
}
