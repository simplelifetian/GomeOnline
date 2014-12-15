package com.gome.haoyuangong.net.volley;

import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gome.haoyuangong.LogDataUtils;
import com.gome.haoyuangong.LogUpdate;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.NetConfig;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.utils.StringUtils;
import com.google.gson.Gson;
//import com.jrj.stock.trade.utils.JSONUtils;

public class JsonRequest<T> extends VolleyRequest<T>{
	private Class<T> jsonClass;
	
	public JsonRequest(int method, String url, RequestHandlerListener listener,Class<T> jsonClass) {
		super(method, url, listener);
		this.jsonClass = jsonClass;
	}
	public JsonRequest(int method,String url,Map<String, String> params,RequestHandlerListener listener,Class<T> jsonClass) {
		super(method, url,params, listener);
		this.jsonClass = jsonClass;
	}
	public JsonRequest(int method,String url,Map<String, String> params,RequestHandlerListener listener,Class<T> jsonClass,boolean isJsonPost) {
		super(method, url,params, listener,isJsonPost);
		this.jsonClass = jsonClass;
	}
	Request jsonObjectRequest ;
	protected void init() {
		initErrorListener();
		if(method==Method.POST&&params!=null&&params.size()>0&&isJsonPost){
			initListener2();
			initJson();
			return;
		}
		initListener();
		Logger.info("Request", url);
		jsonObjectRequest = new com.android.volley.toolbox.StringRequest(method, url, listener,errorListener
		){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				if(params!=null&&params.entrySet().size()>0){
					Logger.info("params", params.toString());
					return validFormat(params);
				}
				return super.getParams();
			}
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> rt = NetConfig.getHeaders(isNormalPost);
				addCustomHeader(rt);
				Logger.info("headers", rt.toString());
				return rt;
			}
//			@Override
			public RetryPolicy getRetryPolicy() {
				RetryPolicy retryPolicy = new DefaultRetryPolicy(NetConfig.getConnectionTimeout(), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
				return retryPolicy;
			}
		};
	}
	
	private void initJson(){
		JSONObject json = new JSONObject(params);
		jsonObjectRequest = new com.android.volley.toolbox.JsonObjectRequest(method, url, json, listener2, errorListener);
	}
	private Listener<String> listener;
	private  ErrorListener errorListener;
	private Listener<JSONObject> listener2;
	private void initListener(){
		listener = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Logger.error("onResponse", response);
				try {
					Gson json = new Gson();
					T object = json.fromJson(response, jsonClass);
					if(object == null){
						throw new Exception();
					}else{
						onSuccess(response,object);
						onEnd();
					}
					
				} catch (Exception e) {
					Logger.error("onResponse", "JSON错误 ", e);
					onFailure(0,"json解析错误");
					onEnd();
				}
			}
		};
	}
	private void initErrorListener(){
		errorListener=new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Logger.info("onErrorResponse", getErrorStr(error)+" --> "+getUrl());
				if (error instanceof TimeoutError) {
					LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_TIMEOUT,getUrl(), String.valueOf(-1), "连接超时");
				}else if ((error instanceof ServerError) || (error instanceof AuthFailureError)) {
					LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_TIMEOUT,getUrl(), String.valueOf(-1), "网络连接异常，请稍后再试");
				}
				
				onFailure(0,getErrorStr(error));
				onEnd();
			}
		};
	}
	
	private void initListener2(){
		listener2 = new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if(response == null || StringUtils.isBlank(response.toString())){
						throw new Exception();
					}else{
						Gson json = new Gson();
						T object = json.fromJson(response.toString(), jsonClass);
						onSuccess(response.toString(),object);
						onEnd();
					}
				} catch (Exception e) {
					e.printStackTrace();
					onFailure(0,"json解析错误");
					onEnd();
				}
			}
		};
	}
	
	public void cancel(){
		super.cancel();
		jsonObjectRequest.cancel();
	}
	@Override
	public Object getTargetRequest() {
		return jsonObjectRequest;
	}
	@Override
	public void setTag(Object obj) {
		jsonObjectRequest.setTag(obj);
	}
	
	public void addCustomHeader(Map<String,String> header){
		
	}
	
}
