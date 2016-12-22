package org.fontys.trackmyprint.database;

/**
 * Created by guido on 15-Dec-16.
 */

public class DatabaseException extends Exception
{
	public DatabaseException()
	{
		super();
	}

	public DatabaseException(String message)
	{
		super(message);
	}

	public DatabaseException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public DatabaseException(Throwable cause)
	{
		super(cause);
	}
}
