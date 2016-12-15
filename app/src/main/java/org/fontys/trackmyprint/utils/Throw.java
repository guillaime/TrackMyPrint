package org.fontys.trackmyprint.utils;

/**
 * Created by guido on 15-Dec-16.
 */

public final class Throw
{
	private Throw()
	{

	}

	public static <T extends Exception> void always(Class<T> tClass, String message)
			throws
			RuntimeException,
			T
	{
		throw create(tClass, message);
	}

	public static <T extends Exception> void when(Class<T> tClass, boolean condition, String message)
			throws
			RuntimeException,
			T
	{
		if(condition)
		{
			throw create(tClass, message);
		}
	}

	public static <T extends Exception> void ifNull(Class<T> tClass, Object value, String valueName)
			throws
			RuntimeException,
			T
	{
		if(value == null)
		{
			throw create(tClass, buildIfNullMessage(valueName));
		}
	}

	public static <T extends Exception, T2 extends Comparable<T2>> void ifOutOfRangeInMin(Class<T> tClass, T2 value, String valueName, T2 minValue)
			throws
			RuntimeException,
			T
	{
		if(value.compareTo(minValue) <= 0)
		{
			throw create(tClass, buildIfOutOfRangeInMinMessage(valueName, minValue.toString()));
		}
	}

	public static <T extends Exception, T2> void ifNotInstanceOf(Class<T> tClass, Object value, String valueName, Class<T2> t2Class)
			throws
			RuntimeException,
			T
	{
		if(!t2Class.isInstance(value))
		{
			throw create(tClass, buildIfNotInstanceOf(valueName, t2Class.toString()));
		}
	}

	private static <T extends Exception> T create(Class<T> tClass, String message)
			throws
			RuntimeException
	{
		try
		{
			return tClass.getDeclaredConstructor(String.class).newInstance(message);
		}
		catch(Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}

	private static String buildIfNullMessage(String valueName)
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Argument '").append(valueName).append("' may not be null");

		return stringBuilder.toString();
	}

	private static String buildIfOutOfRangeInMinMessage(String valueName, String minValue)
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Argument '").append(valueName).append("' may not be <= ").append(minValue);

		return stringBuilder.toString();
	}

	private static String buildIfNotInstanceOf(String valueName, String desiredType)
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Argument '").append(valueName).append("' is not of type '").append(desiredType).append('\'');

		return stringBuilder.toString();
	}
}
