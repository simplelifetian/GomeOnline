package com.gome.haoyuangong.net.result.tougu;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.gome.haoyuangong.net.result.BaseResult;

public class LoginResultBean extends BaseResult {

	private String userName;
	private String mobile;
	private String passportId;
	private String isNeedComplete;
	private String loginToken;
	private String accessToken;
	private long timeout;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPassportId() {
		return passportId;
	}
	public void setPassportId(String passportId) {
		this.passportId = passportId;
	}
	public String getIsNeedComplete() {
		return isNeedComplete;
	}
	public void setIsNeedComplete(String isNeedComplete) {
		this.isNeedComplete = isNeedComplete;
	}
	public String getLoginToken() {
		return loginToken;
	}
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public long getTimeout() {
		return timeout;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
	public static void writeLoginResult(Context context,LoginResultBean loginBean){
		SharedPreferences.Editor editor = context.getSharedPreferences("logined_result_bean", Activity.MODE_PRIVATE).edit();
		editor.putString("userName", loginBean.getUserName());
		editor.putString("mobile", loginBean.getMobile());
		editor.putString("passportId", loginBean.getPassportId());
		editor.putString("isNeedComplete", loginBean.getIsNeedComplete());
		editor.putString("loginToken", loginBean.getLoginToken());
		editor.putString("accessToken", loginBean.getAccessToken());
		editor.putLong("timeout", loginBean.getTimeout());
		editor.commit();
	}
	
	public static LoginResultBean readLoginResult(Context context){
		SharedPreferences shared = context.getSharedPreferences("logined_result_bean", Activity.MODE_PRIVATE);
		LoginResultBean loginBean = new LoginResultBean();
		loginBean.setUserName(shared.getString("userName", null));
		loginBean.setMobile(shared.getString("mobile", null));
		loginBean.setPassportId(shared.getString("passportId", null));
		loginBean.setIsNeedComplete(shared.getString("isNeedComplete", null));
		loginBean.setLoginToken(shared.getString("loginToken", null));
		loginBean.setAccessToken(shared.getString("accessToken", null));
		loginBean.setTimeout(shared.getLong("timeout", 0));
		return loginBean;
	}
	
	public static void clear(Context context){
		SharedPreferences.Editor editor = context.getSharedPreferences("logined_result_bean", Activity.MODE_PRIVATE).edit();
		editor.clear().commit();
	}
	
}
