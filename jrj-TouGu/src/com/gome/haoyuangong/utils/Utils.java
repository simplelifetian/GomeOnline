package com.gome.haoyuangong.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;

public class Utils {

	/**
	 * Get String if str is not null. If it is null, return the defaultString.
	 * 
	 * @param str
	 * @param defaultString
	 * @return String
	 */
	public static String getString(String str, String defaultString) {
		return str == null ? defaultString : str;
	}

	/**
	 * parse String to int
	 * 
	 * @param str
	 * @param defaultInt
	 * @return int
	 */
	public static int getInt(String str, int defaultInt) {
		if (str == null) {
			return defaultInt;
		}
		try {
			defaultInt = Integer.parseInt(str);
		} catch (NumberFormatException e) {
		}

		return defaultInt;
	}

	/**
	 * parse String to float
	 * 
	 * @param str
	 * @param defaultInt
	 * @return float
	 */
	public static float getFloat(String str, float defaultInt) {
		if (str == null) {
			return defaultInt;
		}
		try {
			defaultInt = Float.parseFloat(str);
		} catch (NumberFormatException e) {
		}

		return defaultInt;
	}

	/**
	 * get version code of context
	 * 
	 * @param context
	 *            contains the version code
	 * @return int version code of Application
	 */
	public static int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			versionCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * the versionName of this Application.
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		String versionName = "";
		try {
			versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}
	/**
	 * judge this string and returns true if it is int.
	 * 
	 * @param str
	 *            the string to be judged.
	 * @return true if if it is int, false otherwise.
	 */
	public static boolean isInt(String str) {
		if (str == null) {
			return false;
		}
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}
	public static void sendSMS(Context context, String phoneNumber,
			String message) {
		/*
		 * sendReceiver = new SMSReceiver(); IntentFilter sendFilter = new
		 * IntentFilter(ACTION_SMS_SEND); registerReceiver(sendReceiver,
		 * sendFilter);
		 */

		Intent itSend = new Intent("send_sms");
		PendingIntent pi = PendingIntent.getActivity(context, 0, itSend, 0);
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, pi, null);
	}

	public class SMSServiceReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				/* android.content.BroadcastReceiver.getResultCode()���� */
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					break;
				}
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
	}

	/**
	 * judge this string and returns true if it is null, "", or "null".
	 * 
	 * @param str
	 *            the string to be judged.
	 * @return true if if it is null, "", or "null", false otherwise.
	 */
	public static boolean isNull(String str) {
		if (str == null || "null".equalsIgnoreCase(str) || str.equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * free memory of layout, remove views and unbind drawables
	 * 
	 * @param view
	 */
	public static void unbindDrawables(View view) {
		Drawable back = view.getBackground();
		if (back != null) {
			back.setCallback(null);
		}
		if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}
	}

	/**
	 * Get thumbnail of an image. The thumbnail has specific width and height
	 * from the original bitmap.
	 * 
	 * @param imagePath
	 *            where the image is.
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width,
			int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// get image width and height without loading image
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // false, will loading image
		// scale
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int scale = 1;
		if (beWidth < beHeight) {
			scale = beWidth;
		} else {
			scale = beHeight;
		}
		if (scale <= 0) {
			scale = 1;
		}
		options.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// android2.2 , ThumbnailUtils
		Bitmap newBitmap = ThumbnailUtils.extractThumbnail(bitmap, width,
				height);
		return newBitmap;
	}

	/**
	 * send a broadcast.
	 * 
	 * @param context
	 * @param action
	 */
	public static void sendBroadcast(Context context, String action) {
		Intent intent = new Intent();
		intent.setAction(action);
		context.sendBroadcast(intent);
	}

	/**
	 * Validate if the email is valid.
	 * 
	 * @param email
	 * @return true: it is valid. Otherwise, it is invalid.
	 */
	public static boolean isValidEmail(String email) {
		String check = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
				+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
				+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";
		Pattern pattern = Pattern.compile(check);
		Matcher matcher = pattern.matcher(email);

		return matcher.matches();
	}

	public static String[] getDefaultIpPortContext(Context context, int index,
			int key) {
		String ipPortContext = context.getResources().getStringArray(key)[index];
		return ipPortContext.split("/");
	}

	public static void openApp(Context context, String packageName)
			throws NameNotFoundException {
		PackageManager pm = context.getPackageManager();
		PackageInfo pi = context.getPackageManager().getPackageInfo(
				packageName, 0);

		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(pi.packageName);

		List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);

		ResolveInfo ri = apps.iterator().next();
		if (ri != null) {
			String packName = ri.activityInfo.packageName;
			String className = ri.activityInfo.name;

			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);

			ComponentName cn = new ComponentName(packName, className);

			intent.setComponent(cn);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	public static boolean doubleCompare(String smallValue, String bigValue) {
		try {
			double small = Double.parseDouble(smallValue);
			double big = Double.parseDouble(bigValue);
			if (big > small) {
				return true;
			}
			return false;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	/**
	 * finish activities which are in activityList.
	 * 
	 * @param activityList
	 */
	public static void finishActivity(List<Activity> activityList) {
		for (int i = 0; i < activityList.size(); i++) {
			Activity iActivity = activityList.get(i);
			if (iActivity != null) {
				iActivity.finish();
			}
		}
	}

	public static void lostFocusHideSoft(final Context context,
			final EditText editText) {
		if(editText == null) {
			return;
		}
		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Log.d("", "onFocusChange, hasFocus=" + hasFocus);
				if (!hasFocus) {
					InputMethodManager imm = (InputMethodManager) context
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				}
			}
		});
	}
	
	public static void hideSoftInput(EditText editText) {
		InputMethodManager imm = (InputMethodManager) editText.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	/**
	 * 字符串截短
	 * @param str
	 * @param toCount
	 * @return
	 */
	public static String substring(String str, int toCount) {
		if (str == null)
			return "";
		int reInt = 0;
		StringBuilder reStr = new StringBuilder();
		char[] tempChar = str.toCharArray();
		int len = tempChar.length;
		for (int kk = 0; (kk < len && toCount > reInt); kk++) {
			char currentChar = tempChar[kk];
			String s1 = String.valueOf(currentChar);
			byte[] b = s1.getBytes();
			int bLen = b.length;
			if (reInt + bLen > toCount) {
				break;
			}
			reInt += bLen;
			reStr.append(currentChar);
		}
		reStr.trimToSize();
		return reStr.toString();
	}
}
