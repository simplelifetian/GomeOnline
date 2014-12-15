package com.gome.haoyuangong.net.volley;

import java.util.Map;

import com.android.volley.VolleyError;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;

public abstract class VolleyRequest<T> extends Request<T>{

	public VolleyRequest(int method, String url, RequestHandlerListener listener) {
		super(method, url, listener);
	}
	public VolleyRequest(int method, String url,Map<String, String> params, RequestHandlerListener listener) {
		super(method, url,params, listener);
	}
	public VolleyRequest(int method, String url,Map<String, String> params, RequestHandlerListener listener,boolean isJsonPost) {
		super(method, url,params, listener,isJsonPost);
	}
	
	protected String getErrorStr(VolleyError error){
		return VolleyErrorHelper.getMessage(error);
	}
	protected Map<String, String> validFormat(Map<String, String> params){
		for(Map.Entry<String, String> entry : params.entrySet()){
			if(entry.getValue()==null){
				params.put(entry.getKey(), "");
			}
		}
		return params;
	}
}
