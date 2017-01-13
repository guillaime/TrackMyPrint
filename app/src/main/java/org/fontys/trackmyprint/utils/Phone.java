package org.fontys.trackmyprint.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by guido on 13-Jan-17.
 */
public final class Phone
{
	private Phone()
	{

	}

	public static String getIMEI(Context context)
			throws
			IllegalArgumentException
	{
		Throw.ifNull(IllegalArgumentException.class, context, "context");

		TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

		return telephonyManager.getDeviceId();
	}
}
