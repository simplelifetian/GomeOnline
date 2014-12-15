package com.gome.haoyuangong.utils.next;

import java.util.Locale;

import com.gome.haoyuangong.MyApplication;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

public class DeviceUtil {
//	手机生产商/手机型号/操作系统/固件版本/客户端版本号/客户端版本描述/语言

	public static String mDeviceManufacturer; 
	public static String mDeviceModel;
	public static String mOs;
	public static String mFirmware;
	public static String mVersionString;
	public static String mClientVersionCode;
	public static String mClientVersionDescription;
	public static String mClientLocale;
	
	static{
		fetchDeviceManufacturer();
		fetchDeviceModel();
		fetchOs();
		fetchFirmware();
		fetchVersionString();
		fetchClientVersionCode();
		fetchClientVersionDescription();
		fetchClientLanguage();
	}
	
	public static String getDeviceInfoForUserAgentYN()
	{
		//HTC/g7/Android/ android2.0/25/1.0.9 Build0711A/ zh-CN
		//HTC/HTC Desire/android/10/54/1.1.12 0930A/zh-CN
		StringBuilder str=new StringBuilder();
		str.append(mDeviceManufacturer);
		str.append("/");
		str.append(mDeviceModel);
		str.append("/");
		str.append(mOs);
		str.append("/");
		str.append(mFirmware);
		str.append("/");
		str.append(mClientVersionCode);
		str.append("/");
		str.append(mClientVersionDescription);
		str.append("/");
		str.append(mClientLocale);
		return str.toString();
	}
	
	
	private static void fetchDeviceManufacturer()
	{
		mDeviceManufacturer = android.os.Build.MANUFACTURER;
	}
	
	private static void fetchDeviceModel()
	{
		mDeviceModel = android.os.Build.MODEL;
	}
	
	private static void fetchOs()
	{
		mOs = "android";
	}
	
	private static void fetchFirmware()
	{
		mFirmware = android.os.Build.VERSION.SDK;
	}
	
	private static void fetchVersionString()
	{
		mVersionString=android.os.Build.VERSION.RELEASE;
	}
	private static void fetchClientVersionCode()
	{
		try {
			Context context = MyApplication.get();
			String name = context.getPackageName();
			mClientVersionCode = ""+(context.getPackageManager().getPackageInfo(name, 0).versionCode);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mClientVersionCode="unknown";
		}
	}
	
	private static void fetchClientVersionDescription()
	{
		try {
			Context context = MyApplication.get();
			String name = context.getPackageName();
			mClientVersionDescription = ""+(context.getPackageManager().getPackageInfo(name, 0).versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mClientVersionDescription="unknown";
		}
	}
	
	private static void fetchClientLanguage()
	{
		mClientLocale = Locale.getDefault().getLanguage()+"-"+Locale.getDefault().getCountry();
	}
	
}
