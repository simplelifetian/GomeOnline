package com.gome.haoyuangong.net.volley;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.gome.haoyuangong.LogDataUtils;
import com.gome.haoyuangong.LogUpdate;
import com.gome.haoyuangong.net.NetConfig;
import com.gome.haoyuangong.net.RequestHandlerListener;

public class StringRequest extends VolleyRequest<String>{
	public StringRequest(int method, String url, RequestHandlerListener listener) {
		super(method, url, listener);
	}
	public StringRequest(int method,String url,Map<String, String> params,RequestHandlerListener listener) {
		super(method, url,params, listener);
	}
	com.android.volley.toolbox.StringRequest stringRequest ;
	
	protected void init() {
		stringRequest = new com.android.volley.toolbox.StringRequest(method, url,new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
					onSuccess(response,response);
					onEnd();
			}
		},
		new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (error instanceof TimeoutError) {
					LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_TIMEOUT,getUrl(), String.valueOf(-1), "连接超时");
				}else if ((error instanceof ServerError) || (error instanceof AuthFailureError)) {
					LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_TIMEOUT,getUrl(), String.valueOf(-1), "网络连接异常，请稍后再试");
				}
				onFailure(0,getErrorStr(error));
				onEnd();
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				if(params!=null&&params.entrySet().size()>0){
					return validFormat(params);
				}
				return super.getParams();
			}
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				return NetConfig.getHeaders(isNormalPost);
			}
			@Override
			public RetryPolicy getRetryPolicy() {
				RetryPolicy retryPolicy = new DefaultRetryPolicy(NetConfig.getConnectionTimeout(), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
				return retryPolicy;
			}
		};
	}
	
	public void cancel(){
		super.cancel();
		stringRequest.cancel();
	}
	@Override
	public Object getTargetRequest() {
		return stringRequest;
	}
	@Override
	public void setTag(Object obj) {
		stringRequest.setTag(obj);
	}
	
}
