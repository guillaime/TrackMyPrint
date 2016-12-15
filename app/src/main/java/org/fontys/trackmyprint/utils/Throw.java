package org.fontys.trackmyprint.utils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by guido on 15-Dec-16.
 */

public final class Throw
{
	private Throw()
	{

	}

	public static <T extends Exception> void IfNull(Class<T> tClass, Object value, String valueName)
			throws
			RuntimeException,
			T
	{
		if(value == null)
		{
			throw Create(tClass, buildIfNullMessage(valueName));
		}
	}

	public static <T extends Exception, T2 extends Comparable<T2>> void IfOutOfRangeInMin(Class<T> tClass, T2 value, String valueName, T2 minValue)
			throws
			RuntimeException,
			T
	{
		if(value.compareTo(minValue) <= 0)
		{
			throw Create(tClass, buildIfOutOfRangeInMinMessage(valueName, minValue.toString()));
		}
	}

	private static <T extends Exception> T Create(Class<T> tClass, String message)
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
}
