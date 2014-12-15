package com.gome.haoyuangong.net;


import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


//import com.jrj.stock.trade.logs.Logger;

import com.gome.haoyuangong.LogDataUtils;
import com.gome.haoyuangong.LogUpdate;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.AccessTokenFailureResult;
import com.gome.haoyuangong.net.result.BaseResult;
import com.gome.haoyuangong.net.result.BaseResultWeb;
import com.gome.haoyuangong.net.result.TouguBaseResult;
import com.gome.haoyuangong.net.result.tougu.LoginResultBean;
import com.gome.haoyuangong.net.url.NetUrlLoginAndRegist;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.JSONUtils;
import com.gome.haoyuangong.utils.StringUtils;

public abstract class RequestHandlerListener<T> extends Handler implements RequestListener<T>{
	
	private static final String TAG = RequestHandlerListener.class.getName();
	
	public static final int MSG_START=0;
	public static final int MSG_SUCCESS=1;
	public static final int MSG_FAILURE=2;
	public static final int MSG_END=3;
	public static final int MSG_PROGRESS=4;
	
	private boolean isCancel=false;
	private String id;
	private Request  mRequest;
	private Context ctx;
	private boolean isNetFailure=false;
	public RequestHandlerListener(Context ctx){
		this.ctx = ctx;
	}
	@Override
	public void handleMessage(Message msg) {
		if(isCancel)return;
		switch(msg.what){
		case MSG_START:
			if(msg.obj!=null&&msg.obj instanceof Request){
				mRequest = (Request) msg.obj;
				id = mRequest.getId();
				onStart((Request)msg.obj);
			}
			break;
		case MSG_SUCCESS:
			if(msg.obj!=null){
				Object[] o = (Object[])msg.obj;
				handleServiceMsg((String)o[0],o[1]);
			}
			break;
		case MSG_FAILURE:
			if(msg.obj!=null&&msg.obj instanceof String){
				isNetFailure=true;
				onFailure(id,msg.arg1,(String)msg.obj,msg.obj );
			}
			break;
		case MSG_END:
			onEnd(mRequest);
			break;
		case MSG_PROGRESS:
			
			break;
		}
	}
	public void handleOnStartImmediately(Request request){
		mRequest = request;
		id = request.getId();
		onStart(request);
	}
	private void handleErrorMsg(int code, String str){
		if(!isNetFailure && !StringUtils.isEmpty(str)){
			Toast.makeText(ctx, str, Toast.LENGTH_LONG).show();
		}
	}
	private void handleServiceMsg(String response,Object obj){
		if(response instanceof String && !StringUtils.isEmpty(response) && (response.indexOf("accessToken failure") > 0||response.indexOf("AccessToken Expired") > 0) && mRequest.getRequestedTimes() <= 1){
			AccessTokenFailureResult result = JSONUtils.parseObject(response, AccessTokenFailureResult.class);
			if(result != null && result.getRetCode() == -401 ){
				com.gome.haoyuangong.activity.BaseActivity baseActivity = null;
				if(ctx instanceof com.gome.haoyuangong.activity.BaseActivity){
					baseActivity = (com.gome.haoyuangong.activity.BaseActivity)ctx;
				}
				
				if(baseActivity != null){
					onAccessTokenFailure(mRequest,ctx,result.getRetCode(),result.getMsg(),baseActivity);
				}
				
				return;
			}
		}
		if(obj instanceof BaseResult){
			if(((BaseResult)obj).getResultCode()==0){
				onSuccess(id,(T)obj);
			}else{
				onFailure(id,((BaseResult)obj).getResultCode(),((BaseResult)obj).getResultMsg(),obj);
			}
		}
		else if(obj instanceof BaseResultWeb){
			if(((BaseResultWeb)obj).getRetCode()==0){
				onSuccess(id,(T)obj);
			}else{
				onFailure(id,((BaseResultWeb)obj).getRetCode(),((BaseResultWeb)obj).getMsg(),obj);
			}
		}else if(obj instanceof TouguBaseResult){
			if(((TouguBaseResult)obj).getRetCode()==0){
				onSuccess(id,(T)obj);
			}else{
				onFailure(id,((TouguBaseResult)obj).getRetCode(),((TouguBaseResult)obj).getMsg(),obj);
			}
		}
		else{
			onSuccess(id,(T)obj);
		}
	}
	
	@Override
	public void onFailure(String id, int code, String str,Object obj) {
		handleErrorMsg(code,str);
	}
	@Override
	public void onStart(Request request) {
		
	}
	@Override
	public void onEnd(Request  request) {
		
	}
	public void setCancel(boolean isCancel){
		this.isCancel = isCancel;
	}
	@Override
	public void onProgress(String url, int size, int allCount) {
		
	}

//	private static Object lock = new Object();
	private static boolean updatingToken = false;
	private static void onAccessTokenFailure(final Request mRequest,final Context context,final int retCode,final String msg,final com.gome.haoyuangong.activity.BaseActivity baseActivity){
		
//		if(updatingToken){
//			return;
//		}
		
		if(StringUtils.isEmpty(UserInfo.getInstance().getPassportId())
				||StringUtils.isEmpty(UserInfo.getInstance().getLoginToken())){
//			Toast.makeText(context, "未登录用户", Toast.LENGTH_LONG).show();
			return;
		}
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("loginToken", UserInfo.getInstance().getLoginToken());
		params.put("passportId", UserInfo.getInstance().getPassportId());
		params.put("charset", "utf8");
//		if(StringUtils.isEmpty(UserInfo.getInstance().getDeivceId())){
//			
//		}else{
//			params.put("deviceId", UserInfo.getInstance().getDeivceId());
//		}
//		params.put("deviceType", "1");
		
		Log.e(TAG, NetUrlLoginAndRegist.GET_ACCESS_TOKEN);
		Log.e(TAG, params.toString());
		JsonRequest<LoginResultBean> updateAccessTokenRequest = new JsonRequest<LoginResultBean>(Method.POST, NetUrlLoginAndRegist.GET_ACCESS_TOKEN,params,
			new RequestHandlerListener<LoginResultBean>(context) {
	
				@Override
				public void onStart(Request request) {
					super.onStart(request);
					updatingToken = true;
				}
	
				@Override
				public void onEnd(Request request) {
					super.onEnd(request);
					updatingToken = false;
				}
	
				@Override
				public void onSuccess(String id, LoginResultBean data) {
					// TODO Auto-generated method stub
					LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_ACCESSTOKEN, NetUrlLoginAndRegist.GET_ACCESS_TOKEN,String.valueOf(data.getResultCode()), data.getResultMsg());
					if(data.getResultCode() == 0 && !StringUtils.isEmpty(data.getAccessToken())){
						UserInfo.getInstance().setAccessToken(data.getAccessToken());
						UserInfo.saveUserInfo(context, UserInfo.getInstance());
						mRequest.addRequestedTimes();
						baseActivity.send(mRequest);
					}else{
//						Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
					}
				}
				
				@Override
				public void onFailure(String id, int code, String str,Object obj) {
					
					if(obj != null && obj instanceof LoginResultBean){
						LoginResultBean data = (LoginResultBean)obj;
						LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_ACCESSTOKEN,NetUrlLoginAndRegist.GET_ACCESS_TOKEN, String.valueOf(data.getResultCode()), data.getResultMsg());
					}else{
						super.onFailure(id, code, str, obj);
					}
					
				}
				
			}, LoginResultBean.class);
		baseActivity.send(updateAccessTokenRequest);
	}
}
