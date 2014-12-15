package com.gome.haoyuangong;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.gome.haoyuangong.activity.BaseActivity;

public class Environment {
	public static Environment instance;
	private BaseActivity _activity;
	
	private String productId;
	private String channelId;
	private String versionId;
	private String deviceId;
	private String os;
	private String brand;
	private String pixel;
	private String netWork;	
	
	
	public String getProductId() {
		return "111";
	}
	public String getChannelId() {
		return channelId;
	}
	public String getVersionId() {
		return versionId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String value) {
		os = value;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String value){
		brand = value;
	}
	public String getPixel() {
		return pixel;
	}
	public String getNetWork() {
		return netWork;
	}
	public void setNetWork(String value) {
		netWork = value;
	}
	private Environment(BaseActivity activity){
		_activity = activity;
	}
	public static Environment getInstance(){		
		return instance;
	}
	private void setVersion(){
        PackageInfo pkg;
        try {
            pkg = _activity.getPackageManager().getPackageInfo(_activity.getPackageName(), 0);
            versionId = pkg.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            versionId = "";
        } 
     }
	private void setDisplay(){
		DisplayMetrics dm = new DisplayMetrics();
		_activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		pixel = dm.heightPixels+"*"+dm.widthPixels;
	}
	private void setDeviceId(){
		TelephonyManager telephonyManager= (TelephonyManager) _activity.getSystemService(Context.TELEPHONY_SERVICE);
		deviceId = telephonyManager.getDeviceId();
	}
	public static void init(BaseActivity activity){
		instance = new Environment(activity);
		instance.setVersion();
		instance.setDeviceId();
		instance.setDisplay();
		instance.setBrand(android.os.Build.MODEL);
		instance.setOs("android-"+android.os.Build.VERSION.RELEASE);
		instance.setNetWork(AppInfo.isWifi(activity)?"WIFI":"2G/3G/4G");
	}
}
