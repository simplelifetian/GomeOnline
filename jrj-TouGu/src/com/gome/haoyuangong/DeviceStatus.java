/**
 * 
 */
package com.gome.haoyuangong;

import com.gome.haoyuangong.log.Logger;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.telephony.TelephonyManager;


/**
 * @author tongzhui.peng 2013-9-24 下午4:31:16
 */
public class DeviceStatus {

	private static final String TAG = DeviceStatus.class.getName();

	private static DeviceStatus INSTANCE;

	private static final Object LOCK = new Object();
	
	private Context context;

	private DeviceStatus(Context context) {
		this.context = context;
		init();
	}

	public static DeviceStatus getInstance(Context context) {
		if (null != INSTANCE) {
			return INSTANCE;
		}
		if(context==null){
			return null;
		}
		synchronized (LOCK) {
			if (null == INSTANCE) {
				INSTANCE = new DeviceStatus(context);
			}
		}
		return INSTANCE;
	}

	private void init() {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		this.devid = tm.getDeviceId();
		this.paltid = "android";
		this.model = android.os.Build.MODEL;
		this.localizedModel = android.os.Build.MODEL;
		this.systemName = "android";
		this.systemVersion = android.os.Build.VERSION.RELEASE;
//		this.productid = Setting.PRODUCT_ID;
			

		PackageManager manager = context.getPackageManager();
		try {
			ApplicationInfo ai = manager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;
			Integer pid = bundle.getInt("PRODUCTID");
			this.productid = pid.toString();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Logger.error(TAG, "获取产品id异常",e);
		}
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			this.appver = info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Logger.error(TAG, "获取程序版本错误！",e);
		}

	}

	private String devid;

	private String paltid;

	private String appver;

	private String model;

	private String localizedModel;

	private String systemName;

	private String systemVersion;

	private String productid;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getDevid() {
		return devid;
	}

	public void setDevid(String devid) {
		this.devid = devid;
	}

	public String getPaltid() {
		return paltid;
	}

	public void setPaltid(String paltid) {
		this.paltid = paltid;
	}

	public String getAppver() {
		return appver;
	}

	public void setAppver(String appver) {
		this.appver = appver;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getLocalizedModel() {
		return localizedModel;
	}

	public void setLocalizedModel(String localizedModel) {
		this.localizedModel = localizedModel;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public static final String DEVID = "devid";

	public static final String PALTID = "paltid";

	public static final String APPVER = "appver";

	public static final String MODEL = "model";

	public static final String LOCALIZEDMODEL = "localizedModel";

	public static final String SYSTEMNAME = "systemName";

	public static final String SYSTEMVERSION = "systemVersion";

	public static final String PRODUCTID = "productid";
}
