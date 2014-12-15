/**
 * 
 */
package com.gome.haoyuangong.utils;

import java.lang.reflect.Type;

import com.gome.haoyuangong.log.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author tongzhui.peng
 * 
 */
public final class JSONUtils {
	
	private static final String TAG = JSONUtils.class.getName();
	
	private static final String DEFAULT_ERROR = "{\"retcode\":-2,\"msg\":\"JSON解析异常!\"}";

	public static final <T> T parseObject(String str, Class<T> clazz) {
		Gson json = new Gson();
		try{
			return json.fromJson(str, clazz);
		}catch(Exception e){
			Logger.error(TAG, "JSON解析异常!",e);
			return json.fromJson(DEFAULT_ERROR, clazz);
		}
	}

	@SuppressWarnings("unchecked")
	public static final <T> T parseObject(String str, Type type) {
		Gson json = new Gson();
		try{
			return (T) json.fromJson(str, type);
		}catch(Exception e){
			Logger.error(TAG, "JSON解析异常!",e);
			return json.fromJson(DEFAULT_ERROR, type);
		}
	}

	public static String toJsonString(Object object) {
		Gson json = new Gson();
		
		try{
			return json.toJson(object);
		}catch(Exception e){
			Logger.error(TAG, "JSON解析异常!",e);
			return DEFAULT_ERROR;
		}
	}
	
	public static String toHtmlJsonString(Object object) {
		
		try{
			 GsonBuilder gb =new GsonBuilder();
			 gb.disableHtmlEscaping();
			 return gb.create().toJson(object);
		}catch(Exception e){
			Logger.error(TAG, "JSON解析异常!",e);
			return DEFAULT_ERROR;
		}
	}

}
