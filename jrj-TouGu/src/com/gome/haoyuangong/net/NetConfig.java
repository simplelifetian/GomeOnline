package com.gome.haoyuangong.net;

import java.util.HashMap;
import java.util.Map;

import com.gome.haoyuangong.DeviceStatus;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.utils.StringUtils;

public class NetConfig {
	private static final int connectionTimeout = 60000;
//	private static final int socketTimeout = 15000;
	private static final String HOST = "172.16.2.223";
	
	public static int getConnectionTimeout(){
		return connectionTimeout;
	}
	
	public static final String DEVID = "devid";
	public static final String PALTID = "paltid";
	public static final String APPVER = "appver";
	public static final String MODEL = "model";
	public static final String LOCALIZEDMODEL = "localizedModel";
	public static final String SYSTEMNAME = "systemName";
	public static final String SYSTEMVERSION = "systemVersion";
	public static final String PRODUCTID = "productid";
	
	public static final String PASSPORTID = "passportId";
	public static final String ACCESSTOKEN = "accessToken";
	
	public static Map<String, String> getHeaders(boolean isNormal){
		Map<String, String> headers = new HashMap<String, String>();
		if(isNormal){
//			headers.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
		}else{
			headers.put("Content-Type", "application/json");
		}
		headers.put("Charset", "UTF-8"); 
		DeviceStatus status = DeviceStatus.getInstance(null);
		if(status==null){
			return headers;
		}
		headers.put(DEVID, status.getDevid());
		headers.put(PALTID, status.getPaltid());
		headers.put(APPVER, status.getAppver());
		headers.put(MODEL, status.getModel());
		headers.put(LOCALIZEDMODEL, status.getLocalizedModel());
		headers.put(SYSTEMNAME, status.getSystemName());
		headers.put(SYSTEMVERSION, status.getSystemVersion());
		headers.put(PRODUCTID, status.getProductid());
		
		if(StringUtils.isEmpty(UserInfo.getInstance().getPassportId())||StringUtils.isEmpty(UserInfo.getInstance().getAccessToken())){
			
		}else{
			headers.put(PASSPORTID, UserInfo.getInstance().getPassportId());
			headers.put(ACCESSTOKEN, UserInfo.getInstance().getAccessToken());
		}
		
		return headers;
	}
}
