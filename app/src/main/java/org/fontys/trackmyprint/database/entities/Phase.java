package org.fontys.trackmyprint.database.entities;

import com.google.firebase.database.Exclude;

import org.fontys.trackmyprint.utils.Throw;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guido on 15-Dec-16.
 */
public final class Phase extends Entity
{
	private final String name;
	private final ReentrantLock lock;

	public Phase()
	{
		super(EntityType.PHASE);

		this.name = null;
		this.lock = new ReentrantLock();
	}

	public Phase(String id, String name)
			throws
			IllegalArgumentException
	{
		super(EntityType.PHASE, id);

		Throw.ifNull(IllegalArgumentException.class, name, "name");

		this.name = name;
		this.lock = new ReentrantLock();
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
