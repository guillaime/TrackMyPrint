package org.fontys.trackmyprint.database.entities;

import org.fontys.trackmyprint.utils.Throw;

/**
 * Created by guido on 15-Dec-16.
 */

public final class EntityChanged
{
	private final Entity entity;
	private final int changeType;
	private final Object arg;

	public EntityChanged(Entity entity, int changeType, Object arg)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, entity, "entity");

		this.entity = entity;
		this.changeType = changeType;
		this.arg = arg;
	}

	public EntityChanged(Entity entity, int changeType)
			throws
			IllegalArgumentException
	{
		this(entity, changeType, null);
	}

	public Entity getEntity()
	{
		return this.entity;
	}

	public int getChangeType()
	{
		return this.changeType;
	}

	public Object getArg()
	{
		return this.arg;
	}
}
