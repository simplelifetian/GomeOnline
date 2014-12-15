
package com.gome.haoyuangong.update;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.Serializable;

import com.gome.haoyuangong.MyApplication;

public class AppInfo implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = -4229095029328770749L;

    public Version version;
    public String osVersion;
    public String deviceName;
    public String mobileNumber;
    public String netStatus;
    public String imei;
    public String packageName;

    public AppInfo() {
        
    }
    
    public AppInfo(PackageInfo info) {
        fromPackageInfo(info);
    }
    
    public AppInfo(Context context) {
       fromLocal(context); 
    }
    
    public void fromLocal(Context context) {
        version = getVersion(context);
        imei = getImei(context);
        deviceName = Build.MODEL;
        osVersion = Build.VERSION.RELEASE;
        packageName = context.getPackageName();
    }

    public static String getImei(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        String imei = tm.getDeviceId();
        if (TextUtils.isEmpty(imei)) {
            return "";
        }
        return imei;
    }
    
    public static boolean isIMEIEven() {
        String imei = AppInfo.getImei(MyApplication.get());
        if (!TextUtils.isEmpty(imei)) {
            imei = imei.trim();
            try {
                int imeiInt = (int)imei.charAt(imei.length()-1);
                return (imeiInt % 2 == 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    /**
     * @return
     */
    public static int getInviteABTestIndex() {
        String imei = AppInfo.getImei(MyApplication.get());
        if (!TextUtils.isEmpty(imei)) {
            imei = imei.trim();
            try {
                return imei.charAt(imei.length()-1) % 3;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    
    public static String getInviteMessage() {
//        switch (getInviteABTestIndex()) {
//            case 0:
//                return AppContext.getContext().getString(com.snda.youni.R.string.invitation_message_default_1);
//            case 1:
//            case 2:
//                return AppContext.getContext().getString(com.snda.youni.R.string.invitation_message_default_0);
//        }
//        return AppContext.getContext().getString(com.snda.youni.R.string.invitation_message_default_1);
    	return "188888";
//        if (isIMEIEven()) {
//            return AppContext.getContext().getString(com.snda.youni.R.string.invitation_message_default_0);
//        } else {
//            return AppContext.getContext().getString(com.snda.youni.R.string.invitation_message_default_1);
//        }
    }

    private Version getVersion(Context context) {
        Version version = new Version();
        try {
            version.name = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
            version.code = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            version.name = "unknown";
            version.code = -1;
        }
        return version;
    }

    public class Version implements Serializable {

        /**
		 * 
		 */
        private static final long serialVersionUID = -8278309986269004124L;

        public int code;
        public String name;
    }

    public void fromPackageInfo(PackageInfo info) {
        version = new Version();
        version.code = info.versionCode;
        version.name = info.versionName;
        packageName = info.packageName;
    }
    
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            return  0;
        }
    }
    
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            return  "0";
        }
    }
}
