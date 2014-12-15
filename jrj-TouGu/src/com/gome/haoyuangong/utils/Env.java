package com.gome.haoyuangong.utils;

import java.io.File;




import com.gome.haoyuangong.BuildConfig;

import android.content.Context;
import android.util.Log;


/**
 * Contains some pathes of the client.
 * 
 * @author 
 */
public class Env {

	private final static String TAG = "ENV";

	/**
	 * android data directory in internal storage
	 */
	public static String DATA_FILE = null;

	/**
	 * image directory in sdcard
	 */
	public static String SD_IMG = null;

	/**
	 * the suffix of image name that saved in sdcard
	 */
	public static String SD_IMG_SUFFIX = null;

	private static File cacheDir;

	/**
	 * get the file of directoryName in android data directory, like
	 * "/data/data/../files/directoryName"
	 * 
	 * @param appName
	 * @return File
	 */
	public static File getDataDirectoryPath(Context context,
			String directoryName) {
		if (directoryName == null) {
			return getAppDataDirectory(context);
		}
		File appDir = new File(getAppDataDirectory(context), directoryName);
		if (!appDir.exists()) {
			appDir.mkdirs();// create all directory.
		}
		return appDir;
	}

	/**
	 * get the file of main directory in sdcard
	 * 
	 * @return File
	 */
	public static File getExternalStorageDirectory(Context context) {
		cacheDir = context.getExternalFilesDir(null);
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();// create all directory.
		}
		return cacheDir;
	}

	/**
	 * get the file of data directory, like "/data/data/../files/"
	 * 
	 * @return File
	 */
	public static File getAppDataDirectory(Context context) {
		File file = context.getFilesDir();
		if (!file.exists()) {
			boolean b = file.mkdirs();// create all directory.
			if (!b) {
				if(BuildConfig.DEBUG) Log.e(TAG, "CANNOT CREATE DIRECTORY :" + DATA_FILE);
				return null;
			}
		}
		return file;
	}
}
