package com.gome.haoyuangong.dao;

import com.gome.haoyuangong.BuildConfig;
import com.gome.haoyuangong.db.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import android.content.Context;
import android.util.Log;


/**
 * <h1>
 * 
 * @author
 */
public class BaseDao {
	
	public static final int ALL = -1;
	
	protected DatabaseHelper databaseHelper = null;

	protected DatabaseHelper getHelper(Context context) {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(context,
					DatabaseHelper.class);
		}
		return databaseHelper;
	}
	
	public void close() {
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
		if(BuildConfig.DEBUG) Log.d("", "BaseDao close");
	}
}
