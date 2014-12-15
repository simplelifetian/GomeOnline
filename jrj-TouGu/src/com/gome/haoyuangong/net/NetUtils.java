package com.gome.haoyuangong.net;

import java.util.Map;

public class NetUtils {
	public static String getFormatPostUrl(String relateUrl) {
		StringBuilder sb = new StringBuilder();
//		sb.append(HTTP_PROTOCOL).append("://").append(HOST).append(":").append(PORT).append(relateUrl);
		return sb.toString();
	}
	
	public static String getFormatGetUrl(String relateUrl,Map<String,Object> params) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
//		sb.append(HTTP_PROTOCOL).append("://").append(HOST).append(":").append(PORT).append(relateUrl);
		if(params!= null && !params.isEmpty()){
			sb.append("?");
			for(Map.Entry<String, Object> e : params.entrySet()){
				if(!first){
					sb.append("&");
				}else{
					first = false;
				}
				sb.append(e.getKey()).append("=").append(e.getValue().toString());
			}
		}
		return sb.toString();
	}
}
