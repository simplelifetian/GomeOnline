package com.gome.haoyuangong.presenter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.gome.haoyuangong.BaseViewImpl;
import com.gome.haoyuangong.LogDataUtils;
import com.gome.haoyuangong.LogUpdate;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.BaseResult;
import com.gome.haoyuangong.net.result.tougu.LoginResultBean;
import com.gome.haoyuangong.net.result.tougu.RegistResultBean;
import com.gome.haoyuangong.net.url.NetUrlLoginAndRegist;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.CheckUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.utils.CheckUtils.CheckResponse;

public abstract class IRegistPresenter extends IBasePresenter{

	public IRegistPresenter(BaseViewImpl vImpl) {
		super(vImpl);
	}
	
	public void checkPhoneNum(String number){
		if(StringUtils.isEmpty(number)){
			showToast("请输入手机号");
			return ;
		}
		CheckResponse cr = CheckUtils.isValidPhoneNum(number);
		if(!cr.isValid){
			showToast(cr.rtMsg);
			return ;
		}
		Map<String, String> params =new HashMap<String, String>();
		params.put("mobile", number);
		JsonRequest<BaseResult> request = new JsonRequest<BaseResult>(Method.POST,NetUrlLoginAndRegist.CHECK_PHONE,params, new RequestHandlerListener<BaseResult>(getContext()) {
			
			@Override
			public void onSuccess(String id, BaseResult data) {
				LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_VERIFY_PHONE,NetUrlLoginAndRegist.CHECK_PHONE, String.valueOf(data.getResultCode()), data.getResultMsg());
				onCheckPhone(data.getResultCode()==0);
			}
			@Override
			public void onStart(Request request) {
				super.onStart(request);
				showDialog(request);
			}
			@Override
			public void onEnd(Request request) {
				super.onEnd(request);
				hideDialog(request);
			}
			@Override
			public void onFailure(String id, int code, String str,Object obj) {
				if(obj != null && obj instanceof BaseResult){
					BaseResult data = (BaseResult)obj;
					LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_VERIFY_PHONE,NetUrlLoginAndRegist.CHECK_PHONE, String.valueOf(data.getResultCode()), data.getResultMsg());
				}
				if(code==1){
					showToast("手机号已被注册，请更换手机号或直接登录");
				}else{
					super.onFailure(id, code, str,obj);
				}
			}
		}, BaseResult.class);
		send(request);
	}
	
	public void getVCode(String number){
		Map<String, String> params =new HashMap<String, String>();
		params.put("mobile", number);
		params.put("appParam", "tg");
		JsonRequest<BaseResult> request = new JsonRequest<BaseResult>(Method.POST,NetUrlLoginAndRegist.GET_VCODE,params, new RequestHandlerListener<BaseResult>(getContext()) {
			@Override
			public void onSuccess(String id, BaseResult data) {
				showToast("获取验证码");
				LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_SEND_VCODE, NetUrlLoginAndRegist.GET_VCODE,String.valueOf(data.getResultCode()), data.getResultMsg());
				onGetCodeSuccess();
			}
			@Override
			public void onFailure(String id, int code, String str,Object obj) {
//				super.onFailure(id, code, str, obj);
				if(obj != null && obj instanceof BaseResult){
					BaseResult data = (BaseResult)obj;
					LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_SEND_VCODE, NetUrlLoginAndRegist.GET_VCODE,String.valueOf(data.getResultCode()), data.getResultMsg());
					if(data.getResultCode()==2){
						showToast("验证码获取过于频繁，请稍后再试");
					}else{
						showToast(data.getResultMsg());
					}
				}
				
				onGetCodeFailure();
			}
		}, BaseResult.class);
		send(request);
	}
	public void verifyCode(String vcode,String phone){
		if(StringUtils.isEmpty(vcode)){
			showToast("请输入验证码");
			return ;
		}
		Map<String, String> params =new HashMap<String, String>();
		params.put("code", vcode);
		params.put("mobile", phone);
		params.put("appParam", "tg");
		JsonRequest<BaseResult> request = new JsonRequest<BaseResult>(Method.POST,NetUrlLoginAndRegist.VERIFY_VCODE,params, new RequestHandlerListener<BaseResult>(getContext()) {
			
			@Override
			public void onSuccess(String id, BaseResult data) {
				LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_VERIFY_VCODE,NetUrlLoginAndRegist.VERIFY_VCODE, String.valueOf(data.getResultCode()), data.getResultMsg());
				onVerifyCode(data.getResultCode()==0);
			}
			@Override
			public void onFailure(String id, int code, String str, Object obj) {
//				super.onFailure(id, code, str, obj);
				if(obj != null && obj instanceof BaseResult){
					BaseResult data = (BaseResult)obj;
					LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_VERIFY_VCODE,NetUrlLoginAndRegist.VERIFY_VCODE, String.valueOf(data.getResultCode()), data.getResultMsg());
				}
				if(code==1){
					showToast("验证码有误，请重新输入");
				}else{
					showToast(str);
				}
			}
			@Override
			public void onStart(Request request) {
				super.onStart(request);
				showDialog(request);
			}
			@Override
			public void onEnd(Request request) {
				super.onEnd(request);
				hideDialog(request);
			}
		}, BaseResult.class);
		send(request);
	}
	
	public void regist(final String name,String password,String phone,String headUrl){
		if(StringUtils.isEmpty(name)){
			showToast("请输入昵称");
			return ;
		}
		if(StringUtils.isEmpty(password)){
			showToast("请输入密码");
			return ;
		}
		
		Map<String, String> params =new HashMap<String, String>();
		if(StringUtils.isEmpty(name)){
			
		}else{
			params.put("passportName", name);
		}
//		params.put("password", password);
		params.put("password", StringUtils.md5(password.toLowerCase(Locale.CHINA)));
		params.put("password1", StringUtils.md5(password));
		params.put("mobile", phone);
		if(!StringUtils.isEmpty(headUrl)){
			params.put("imageURL", headUrl);
		}
		params.put("bizSource", "android");
		params.put("charset", "utf-8");
		JsonRequest<RegistResultBean> request = new JsonRequest<RegistResultBean>(Method.POST,NetUrlLoginAndRegist.RIGIST_AND_LOGIN,params, new RequestHandlerListener<RegistResultBean>(getContext()) {
			
			@Override
			public void onSuccess(String id, RegistResultBean data) {
				LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_REGIST,NetUrlLoginAndRegist.RIGIST_AND_LOGIN, String.valueOf(data.getResultCode()), data.getResultMsg());
				if(data.getResultCode()==0){
					onRigistOk(data);
				}else{
					showToast("注册失败，请稍后重试");
				}
			}
			@Override
			public void onFailure(String id, int code, String str, Object obj) {
				if(obj != null && obj instanceof RegistResultBean){
					RegistResultBean data = (RegistResultBean)obj;
					LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_REGIST,NetUrlLoginAndRegist.RIGIST_AND_LOGIN, String.valueOf(data.getResultCode()), data.getResultMsg());
				}
				super.onFailure(id, code, str, obj);
			}
			@Override
			public void onStart(Request request) {
				super.onStart(request);
				showDialog(request);
			}
			@Override
			public void onEnd(Request request) {
				super.onEnd(request);
				hideDialog(request);
			}
		}, RegistResultBean.class);
		send(request);
	}
	
	public void updateHead(String headUrl,String passportId){
		if(StringUtils.isEmpty(headUrl)||StringUtils.isEmpty(passportId)){
			Logger.error("updateHead", "error");
			return ;
		}
		Map<String, String> params =new HashMap<String, String>();
		params.put("filename", headUrl);
		params.put("user_id", passportId);
		JsonRequest<BaseResult> request = new JsonRequest<BaseResult>(Method.POST,NetUrlLoginAndRegist.UPDATE_HEAD,params, new RequestHandlerListener<BaseResult>(getContext()) {
			
			@Override
			public void onSuccess(String id, BaseResult data) {
				if(data.getResultCode()==0){
					onUpdateHead();
				}
				LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_UPDATE_HEAD, NetUrlLoginAndRegist.UPDATE_HEAD,String.valueOf(data.getResultCode()), data.getResultMsg());
			}
			@Override
			public void onFailure(String id, int code, String str, Object obj) {
				if(obj instanceof BaseResult){
					BaseResult result = (BaseResult) obj;
					LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_UPDATE_HEAD,NetUrlLoginAndRegist.UPDATE_HEAD, String.valueOf(result.getResultCode()), result.getResultMsg());
					if(result.getResultCode()==1){
						onUpdateHead();
					}else{
						onUpdateHeadError();
					}
				}else{
					super.onFailure(id, code, str, obj);
					onUpdateHeadError();
				}
				
			}
			@Override
			public void onStart(Request request) {
				super.onStart(request);
				showDialog(request);
			}
			@Override
			public void onEnd(Request request) {
				super.onEnd(request);
				hideDialog(request);
			}
		}, BaseResult.class);
		send(request);
	}
	public void onUpdateHead(){
		
	}
	public void onUpdateHeadError(){
		
	}
	public void onGetCodeSuccess(){
		
	}
	public void onGetCodeFailure(){
		
	}
	public void onCheckPhone(boolean isOk){
		
	}
	public void onVerifyCode(boolean isOk){
		
	}
	public void onRigistOk(RegistResultBean bean){
//		UserInfo user = UserInfo.getInstance();
//		user.setUserId(passportId);
//		user.setUserName(name);
	}
	
	public void updateUserInfo(Map<String, String> params){
		if(params == null){
			return ;
		}
		
		JsonRequest<RegistResultBean> request = new JsonRequest<RegistResultBean>(Method.POST,NetUrlLoginAndRegist.RIGIST,params, new RequestHandlerListener<RegistResultBean>(getContext()) {
			
			@Override
			public void onSuccess(String id, RegistResultBean data) {
				if(data.getResultCode()==0){
					onUpdateOk(id,data);
				}
			}
			@Override
			public void onStart(Request request) {
				super.onStart(request);
				showDialog(request);
			}
			@Override
			public void onEnd(Request request) {
				super.onEnd(request);
				hideDialog(request);
			}
		}, RegistResultBean.class);
		send(request);
	}
	
	public void onUpdateOk(String id, RegistResultBean data){
		
	}
	
}
