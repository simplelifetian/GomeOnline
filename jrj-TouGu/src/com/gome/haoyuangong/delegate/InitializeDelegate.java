package com.gome.haoyuangong.delegate;

import java.util.HashMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.gome.haoyuangong.activity.MainActivity;
import com.gome.haoyuangong.update.AppInfo;
import com.gome.haoyuangong.update.UpdateInfo;
import com.gome.haoyuangong.update.UpdateManager;
import com.gome.haoyuangong.utils.next.PreferenceKeys;

public class InitializeDelegate {

	public static final int UPDATE_NOTIFY_INTERVAL = 24 * 60 * 60 * 1000;
	public static final long TIMER_TASK_TIME_WEEK = 7 * 24 * 3600 * 1000;
	public static final long WAP_CHECK_INTERVAL = 24 * 60 * 60 * 1000;
	MainActivity mAct;

	public InitializeDelegate(MainActivity act) {
		mAct = act;
	}

	private void startActivity(Intent intent) {
		mAct.startActivity(intent);
	}

	private void finish() {
		mAct.finish();
	}

	public HashMap<String, String> doInitialize() {
		HashMap<String, String> rets = new HashMap<String, String>();
		checkUpdate();

		return rets;
	}

	private void checkUpdate() {
		AppInfo appInfo = new AppInfo(mAct);
		final SharedPreferences mPreferences = PreferenceManager
				.getDefaultSharedPreferences(mAct);
		int lastVersionCode = mPreferences.getInt(
				PreferenceKeys.KEY_LAST_VERSION_CODE, 0);
		int versioncode = mPreferences.getInt(
				UpdateManager.KEY_UPDATE_VERSIONCODE, 0);

		if (mPreferences.getBoolean(
				UpdateManager.KEY_UPDATE_NOTIFICATION_AFTER_MARKET, false)) {
			if (lastVersionCode != 0 && appInfo.version.code == lastVersionCode) {
				if (System.currentTimeMillis()
						- mPreferences.getLong(
								UpdateManager.KEY_UPDATE_CALL_MARKET, 0) > UPDATE_NOTIFY_INTERVAL) {
					if (appInfo.version.code < versioncode) {
						UpdateInfo info = UpdateManager.getInstance()
								.getLocalUpdateInfo();
						UpdateManager.getInstance().showUpdateDescription(info,
								!info.force);
					}
				}
				mPreferences
						.edit()
						.putBoolean(
								UpdateManager.KEY_UPDATE_CALL_MARKET_SUCCESS,
								false)
						.putBoolean(
								UpdateManager.KEY_UPDATE_NOTIFICATION_AFTER_MARKET,
								false).commit();
			} else {
				mPreferences
						.edit()
						.putBoolean(
								UpdateManager.KEY_UPDATE_CALL_MARKET_SUCCESS,
								true).commit();
			}
		}

		if (System.currentTimeMillis()
				- mPreferences.getLong(
						PreferenceKeys.KEY_LAST_GET_ENTERPRISE_LIST, 0) > TIMER_TASK_TIME_WEEK) {
			mPreferences
					.edit()
					.putLong(PreferenceKeys.KEY_LAST_GET_ENTERPRISE_LIST,
							System.currentTimeMillis()).commit();
		}

		if (System.currentTimeMillis()
				- mPreferences.getLong(
						PreferenceKeys.KEY_LAST_GET_EMOTION_LIST, 0) > WAP_CHECK_INTERVAL) {

			mPreferences
					.edit()
					.putLong(PreferenceKeys.KEY_LAST_GET_EMOTION_LIST,
							System.currentTimeMillis()).commit();
		}

		if (appInfo.version.code != lastVersionCode) {
			Editor editor = mPreferences.edit();
			editor.putInt(PreferenceKeys.KEY_LAST_VERSION_CODE,
					appInfo.version.code);
			editor.commit();
		}

		if (mPreferences.getInt(PreferenceKeys.KEY_FLAG_USER_EXTRAINFO_COMMIT,
				0) != appInfo.version.code) {
//			userExtraInfoCommit(C.get());
		}
		UpdateManager.getInstance().autoUpdate();

	}

}
