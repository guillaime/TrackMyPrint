package org.fontys.trackmyprint.database.entities;

import org.fontys.trackmyprint.utils.Throw;

import java.util.Observable;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guido on 15-Dec-16.
 */
public final class Phase extends Observable
{
	private final String phaseId;
	private final String name;
	private final ReentrantLock lock;
	private final int hashCode;

	public Phase(String phaseId, String name)
			throws
			IllegalArgumentException
	{
		Throw.IfNull(IllegalArgumentException.class, phaseId, "phaseId");
		Throw.IfNull(IllegalArgumentException.class, name, "name");

		this.phaseId = phaseId;
		this.name = name;
		this.lock = new ReentrantLock();
		this.hashCode = 31 * this.phaseId.hashCode();
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

	public String getPhaseId()
	{
		return this.phaseId;
	}

	public String getName()
	{
		return this.name;
	}

	public ReentrantLock getLock()
	{
		return this.lock;
	}
}
