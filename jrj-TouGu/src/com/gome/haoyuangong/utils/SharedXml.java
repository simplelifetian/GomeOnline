package com.gome.haoyuangong.utils;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * <h1>提供操作SharedPreferences写xml文件的常用方法</h1>
 * @author 
 */
public class SharedXml {
	public static final String XML_ACCOUNT = "MyPrefsFileAcc";
	
	/** if the file exist, the activity will finish itself and application return to login */
	public static final String FILE_FLAG = "MyPrefs";
	public static final String FILE_ACCOUNT_DELETE_FLAG = "MyPrefs_delete";
	
	/* XML_ACCOUNT */
	public static final String AUTO_LOGIN = "auto_login";// removed at 2013-8-27
	
	public static SharedPreferences getSharedPreferences(Context context, String fileName) {
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return prefer;
	}
	
	public static Editor getEditor(Context context, String fileName) {
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return prefer.edit();
	}
	
	public static String getString(Context context, String fileName, String name, String defaultValue) {
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return prefer.getString(name, defaultValue);
	}
	
	public static void putString(Context context, String fileName, String name, String value) {
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		prefer.edit().putString(name, value).commit();
	}
	
	public static boolean getBoolean(Context context, String fileName, String name, boolean defaultValue) {
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return prefer.getBoolean(name, defaultValue);
	}

	public static void putBoolean(Context context, String fileName, String name, boolean value) {
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		prefer.edit().putBoolean(name, value).commit();
	}
	
	public static long getLong(Context context, String fileName, String name, long defaultValue) {
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return prefer.getLong(name, 0);
	}

	public static void putLong(Context context, String fileName, String name, long value) {
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		prefer.edit().putLong(name, value).commit();
	}
	
	public static int getInt(Context context, String fileName, String name, int defaultValue) {
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return prefer.getInt(name, defaultValue);
	}
	
	public static void putInt(Context context, String fileName, String name, int value) {
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		prefer.edit().putInt(name, value).commit();
	}
	
	public static void remove(Context context, String fileName, String name) {
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		prefer.edit().remove(name).commit();
	}
	
	public static String getString(SharedPreferences prefer, String name, String defaultString) {
		return prefer.getString(name, defaultString);
	}
	
	public static void setFlag(Context context) {
		File flagFile = new File(Env.getAppDataDirectory(context), FILE_FLAG);
		if(!flagFile.exists()) {
			try {
				flagFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void clearFlag(Context context) {
		File flagFile = new File(Env.getAppDataDirectory(context), FILE_FLAG);
		if(flagFile.exists()) {
			Log.d("", "flagFile.delete()="+flagFile.delete());
		}
	}
	
	public static boolean existFlag(Context context) {
		File flagFile = new File(Env.getAppDataDirectory(context), FILE_FLAG);
		return flagFile.exists();
	}
	
	public static void setAccountsDeletedFlag(Context context) {
		File flagFile = new File(Env.getAppDataDirectory(context), FILE_ACCOUNT_DELETE_FLAG);
		if(!flagFile.exists()) {
			try {
				flagFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void clearAccountsDeletedFlag(Context context) {
		File flagFile = new File(Env.getAppDataDirectory(context), FILE_ACCOUNT_DELETE_FLAG);
		if(flagFile.exists()) {
			Log.d("", "clearAccountsDeletedFlag.delete()="+flagFile.delete());
		}
	}
	
	public static boolean existAccountsDeletedFlag(Context context) {
		File flagFile = new File(Env.getAppDataDirectory(context), FILE_ACCOUNT_DELETE_FLAG);
		return flagFile.exists();
	}
}
