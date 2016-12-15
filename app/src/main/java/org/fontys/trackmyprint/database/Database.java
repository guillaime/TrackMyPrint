package org.fontys.trackmyprint.database;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guido on 15-Dec-16.
 */
public final class Database
{
	private static final ReentrantLock INSTANCE_LOCK;
	private static Database INSTANCE;

	static
	{
		INSTANCE_LOCK = new ReentrantLock();
		INSTANCE = null;
	}

	private final Object lock;
	private boolean closed;

	private Database()
	{
		this.lock = new Object();
		this.closed = false;
	}

	public static void InitializeInstance()
			throws
			IllegalStateException
	{
		INSTANCE_LOCK.lock();
		try
		{
			getInstance().initialize();
		}
		finally
		{
			INSTANCE_LOCK.unlock();
		}
	}

	public static void DestroyInstance()
	{
		INSTANCE_LOCK.lock();
		try
		{
			getInstance().deInitialize();
		}
		finally
		{
			INSTANCE_LOCK.unlock();
		}
	}

	public static Database getInstance()
	{
		INSTANCE_LOCK.lock();
		try
		{
			if(INSTANCE == null)
			{
				INSTANCE = new Database();
			}

			return INSTANCE;
		}
		finally
		{
			INSTANCE_LOCK.unlock();
		}
	}

	private void initialize()
			throws
			IllegalStateException
	{
		synchronized(this.lock)
		{
			throwIfClosed();

			// Do initialize
		}
	}

	private void deInitialize()
	{
		synchronized(this.lock)
		{
			if(this.closed)
			{
				return;
			}

			// Do close

			this.closed = true;
		}
	}

	private void throwIfClosed()
			throws
			IllegalStateException
	{
		if(this.closed)
		{
			throw new IllegalStateException("Database is closed");
		}
	}
}
