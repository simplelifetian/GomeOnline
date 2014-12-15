/**
 * 
 */
package com.gome.haoyuangong.log;

import android.util.Log;

/**
 * 
 */
public class Logger {

	private static final boolean logOpen = true;
	
	public static void verbose(String TAG, String msg) {
		if (logOpen) {
			Log.v(TAG, msg);
		}
	}

	public static void verbose(String TAG, String msg, Throwable t) {
		if (logOpen) {
			Log.v(TAG, msg, t);
		}
	}

	public static void debug(String TAG, String msg) {
		if (logOpen) {
			Log.d(TAG, msg);
		}
	}

	public static void debug(String TAG, String msg, Throwable t) {
		if (logOpen) {
			Log.d(TAG, msg, t);
		}
	}
	
	public static void info(String TAG, String msg) {
		if (logOpen) {
			Log.i(TAG, msg);
		}
	}

	public static void info(String TAG, String msg, Throwable t) {
		if (logOpen) {
			Log.i(TAG, msg, t);
		}
	}
	
	public static void warn(String TAG, String msg) {
		if (logOpen) {
			Log.w(TAG, msg);
		}
	}

	public static void warn(String TAG, String msg, Throwable t) {
		if (logOpen) {
			Log.w(TAG, msg, t);
		}
	}
	
	public static void error(String TAG, String msg) {
		if (logOpen) {
			Log.e(TAG, msg);
		}
	}

	public static void error(String TAG, String msg, Throwable t) {
		if (logOpen) {
			Log.e(TAG, msg, t);
		}
	}
}
