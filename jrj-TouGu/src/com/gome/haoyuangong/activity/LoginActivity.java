/**
 * 
 */
package com.gome.haoyuangong.activity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.AppInfo;
import com.gome.haoyuangong.LogDataUtils;
import com.gome.haoyuangong.LogUpdate;
import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.NetConfig;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.BaseResultWeb;
import com.gome.haoyuangong.net.result.XinGeBaseResult;
import com.gome.haoyuangong.net.result.tougu.LoginResultBean;
import com.gome.haoyuangong.net.result.tougu.UserIdentifiedResult;
import com.gome.haoyuangong.net.url.NetUrlLoginAndRegist;
import com.gome.haoyuangong.net.url.NetUrlTougu;
import com.gome.haoyuangong.net.url.XinGeURL;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.service.NRShelper;
import com.gome.haoyuangong.utils.StringUtils;

/**
 *
 */
public class LoginActivity extends BaseActivity{
	
	private static final String TAG = LoginActivity.class.getName();
	public static final int LOGINED_RESPONSE_CODE = 1021;
	public static final int UPDATE_RESPONSE_CODE = 1022;
	
	public static final String BUNDLE_PARAM_TARGET_ACTIVITY  = "BUNDLE_PARAM_TARGET_ACTIVITY";
	public static final String BUNDLE_PARAM_TARGET_ACTIVITY_TYPE  = "BUNDLE_PARAM_TARGET_ACTIVITY_TYPE";
	public static final String BUNDLE_PARAM_LOGIN_TYPE = "BUNDLE_PARAM_LOGIN_TYPE";
	public static final String BUNDLE_PARAM_LOGIN_NAME = "BUNDLE_PARAM_LOGIN_NAME";
	public static final String BUNDLE_PARAM_LOGIN_PASSWD = "BUNDLE_PARAM_LOGIN_PASSWD";
	
	public static final String LOGINED_ACTION = "com.jrj.tougu.logined";
	
	public static final int LOGIN_TYPE_NORMAL = 0;
	public static final int LOGIN_TYPE_AUTO = 1;
	
	private Button login;
	
	private Button regist;
	
	private EditText loginName;
	
	private EditText passwd;
	
	private TextView findPass;
	
	private LoginResultBean loginBean;
	
	private String targetActivity;
	private int targetActivityType;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(UserInfo.getInstance().isLogin()){
			finish();
			return;
		}
		int loginType = getIntent().getIntExtra(BUNDLE_PARAM_LOGIN_TYPE, 0);
		targetActivity = getIntent().getStringExtra(BUNDLE_PARAM_TARGET_ACTIVITY);
		targetActivityType = getIntent().getIntExtra(BUNDLE_PARAM_TARGET_ACTIVITY_TYPE, 0);
		switch(loginType){
			case LOGIN_TYPE_AUTO:{
				initAutoLogin();
				break;
			}
			case LOGIN_TYPE_NORMAL:
			default:
				normalLogin();
				break;
		}
		
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void initAutoLogin(){
		final String loginName = getIntent().getStringExtra(BUNDLE_PARAM_LOGIN_NAME);
		final String passWord = getIntent().getStringExtra(BUNDLE_PARAM_LOGIN_PASSWD);
		if(StringUtils.isEmpty(loginName) || StringUtils.isEmpty(passWord)){
			Intent intent = getIntent();
			intent.setClass(this, LoginActivity.class);
			intent.putExtra(BUNDLE_PARAM_LOGIN_TYPE, LOGIN_TYPE_NORMAL);
			startActivity(intent);
			finish();
			return;
		}
		setTheme(android.R.style.Theme_Translucent);
		FrameLayout view = new FrameLayout(this);
		view.setBackgroundColor(getResources().getColor(R.color.half_transparent));
		setContentView(view);
		doLogin(loginName,passWord);
	}
	
	private void normalLogin(){
		setContentView(R.layout.activity_login);
		setTitle("登录");
		loginName = (EditText)findViewById(R.id.login_name);
		passwd = (EditText)findViewById(R.id.passwd);
		login = (Button)findViewById(R.id.login);
		login.setOnClickListener(this);
		
		regist = (Button)findViewById(R.id.regist);
		regist.setOnClickListener(this);
		
		findPass = (TextView)findViewById(R.id.find_pass);
		findPass.setOnClickListener(this);
		
		titleLeft1.setBackgroundResource(R.drawable.title_finish);
//		titleRight2.setText("注册");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch(v.getId()){
		case R.id.title_right2:
//			Intent intent1 = new Intent(this,Regist1Activity_.class);
//			Intent intent1 = new Intent(this,AskDetailWebActivity.class);
//			startActivity(intent1);
//			ITradePresenter itp = new ITradePresenter(this);
//			itp.gotoTrade();
			break;
		case R.id.regist:
			Intent intent1 = new Intent(this, RegistApplyActivity.class);
			startActivity(intent1);
			break;
		case R.id.find_pass:
			Intent intent2 = new Intent(this,FindPass1Activity.class);
			startActivity(intent2);
			break;
		case R.id.login:
			String loginIdStr = loginName.getText().toString();
			if(StringUtils.isEmpty(loginIdStr)){
				Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
				return;
			}
			
			String passwdStr = passwd.getText().toString();
			if(StringUtils.isEmpty(passwdStr)){
				Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
				return;
			}
			doLogin(loginIdStr, passwdStr);
			break;
		}
	}
	
	private void doLogin(String loginID,String passwd) {

		Map<String,String> params = new HashMap<String,String>();
		params.put("loginID", loginID);
//		params.put("passwd", passwd);
		params.put("passwd", StringUtils.md5(passwd.toLowerCase(Locale.CHINA)));
		params.put("passwd1", StringUtils.md5(passwd));
		params.put("charset", "utf8");
		if(StringUtils.isEmpty(UserInfo.getInstance().getDeivceId())){
			
		}else{
			params.put("deviceId", UserInfo.getInstance().getDeivceId());
		}
		params.put("deviceType", "1");
		
		JsonRequest<LoginResultBean> request = new JsonRequest<LoginResultBean>(Method.POST, NetUrlLoginAndRegist.LOGIN,params,
				new RequestHandlerListener<LoginResultBean>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						 showDialog(request,"正在登录...");
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						 hideDialog(request);
					}

					@Override
					public void onSuccess(String id, LoginResultBean data) {
						// TODO Auto-generated method stub
//						Toast.makeText(ReplyActivity.this, "赞成功", Toast.LENGTH_SHORT).show();
						LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_LOGIN, NetUrlLoginAndRegist.LOGIN,String.valueOf(data.getResultCode()), data.getResultMsg());
						if(data.getResultCode() == 0){
//							Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//							UserInfo.getInstance().setUserId(data.getPassportId());
//							UserInfo.getInstance().setUserName(data.getUserName());
//							UserInfo.getInstance().setLoginToken(data.getLoginToken());
//							UserInfo.getInstance().setAccessToken(data.getAccessToken());
							
							
							loginBean = data;
							LoginResultBean.writeLoginResult(LoginActivity.this, data);
							getUserInfo(loginBean.getPassportId(),loginBean.getAccessToken());
							
							registDevice(data.getPassportId());
						}else{
							Toast.makeText(LoginActivity.this, data.getResultMsg(), Toast.LENGTH_SHORT).show();
						}
					}
					@Override
					public void onFailure(String id, int code, String str,Object obj) {
						
						if(obj != null && obj instanceof LoginResultBean){
							LoginResultBean data = (LoginResultBean)obj;
							LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_LOGIN, NetUrlLoginAndRegist.LOGIN,String.valueOf(data.getResultCode()), data.getResultMsg());
							if(data.getResultCode() == 1){
								Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
							}else if(data.getResultCode() == -1){
								Toast.makeText(LoginActivity.this, "非法参数", Toast.LENGTH_SHORT).show();
							}else if(data.getResultCode() == 2){
								Toast.makeText(LoginActivity.this, "服务内部错误", Toast.LENGTH_SHORT).show();
							}else{
								Toast.makeText(LoginActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
							}
						}else{
							super.onFailure(id, code, str, obj);
						}
						
						
					}
				}, LoginResultBean.class);

		send(request);
	}
	private void registDevice(String userid) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("deviceToken", UserInfo.getInstance().getDeivceId());
		params.put("deviceType", "3");
//		params.put("user_type", "1");
//		params.put("user_id", "141027010023086621");
		if (!TextUtils.isEmpty(userid))
			params.put("user_id", userid);
		params.put("appid", "tougu");
		if (!UserInfo.getInstance().isLogin())
			params.put("user_type", "3");
		else if (UserInfo.getInstance().isTougu())
			params.put("user_type", "1");
		else
			params.put("user_type", "2");

		JsonRequest<XinGeBaseResult> request = new JsonRequest<XinGeBaseResult>(
				Method.POST, XinGeURL.REGISTDEVICE, params,
				new RequestHandlerListener<XinGeBaseResult>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						// showDialog(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						// hideDialog(request);
					}

			@Override
			public void onSuccess(String id, XinGeBaseResult data) {
				// TODO Auto-generated method stub
				// Toast.makeText(ReplyActivity.this, "赞成功", Toast.LENGTH_SHORT).show();
				if (data.getRet() == "1") {
					finish();
				}
//				Toast.makeText(LoginActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
				}
			}, XinGeBaseResult.class);
		send(request);

	}
	private void getUserInfo(final String userid,final String accessToken) {

		String url = String.format(NetUrlTougu.USER_IDENTIFY,userid);//000822010000046691
//		String url = String.format(NetUrlTougu.USER_IDENTIFY,"000822010000046691");
		Logger.error(TAG, "login url : "+url);
		JsonRequest<UserIdentifiedResult> request = new JsonRequest<UserIdentifiedResult>(Method.GET, url,null,
				new RequestHandlerListener<UserIdentifiedResult>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						 showDialog(request,"正在登录...");
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						 hideDialog(request);
					}
					
					@Override
					public void onSuccess(String id, UserIdentifiedResult data) {
						// TODO Auto-generated method stub
//						Toast.makeText(ReplyActivity.this, "赞成功", Toast.LENGTH_SHORT).show();
						LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_USERINFO, NetUrlTougu.USER_IDENTIFY,String.valueOf(data.getRetCode()), data.getMsg());
						if(data.getRetCode() == 0 && loginBean != null){
							Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
							UserInfo.getInstance().setPassportId(loginBean.getPassportId());
							UserInfo.getInstance().setUserId(loginBean.getPassportId());
							UserInfo.getInstance().setLoginName(loginBean.getUserName());
							UserInfo.getInstance().setUserName(data.getData().getUser().getUserName());
							UserInfo.getInstance().setLoginToken(loginBean.getLoginToken());
							UserInfo.getInstance().setAccessToken(loginBean.getAccessToken());
							UserInfo.getInstance().setIsAdviser(data.getData().getIsAdviser());
							UserInfo.getInstance().setMobile(loginBean.getMobile());
							
							UserInfo.getInstance().setTouguUserBean(data.getData().getUser());
							
							UserInfo.saveUserInfo(LoginActivity.this, UserInfo.getInstance());
							AppInfo.jrjUserSSoid = loginBean.getPassportId();
							NRShelper.doSysnMyStock(LoginActivity.this);
							
//							setResult(LOGINED_RESPONSE_CODE);
//							finish();
//							Logger.error(TAG, "access : "+UserInfo.getInstance().getAccessToken());
//							Logger.error(TAG, "logintoken : "+UserInfo.getInstance().getLoginToken());
							
							if(StringUtils.isEmpty(UserInfo.getInstance().getUserName())&&StringUtils.isEmpty(UserInfo.getInstance().getMobile())){
								Intent intent = new Intent(LoginActivity.this,FindPass1Activity.class);
								intent.putExtra(FindPass1Activity.BUNDLE_PARAM_TYPE, FindPass1Activity.TYPE_NO_USERNAME);
								intent.putExtra(FindPass1Activity.BUNDLE_PARAM_PASSPORTID, UserInfo.getInstance().getUserId());
								intent.putExtra(FindPass1Activity.BUNDLE_PARAM_SPTOKEN, UserInfo.getInstance().getAccessToken());
								startActivityForResult(intent,UPDATE_RESPONSE_CODE);
								return;
							}else if(!StringUtils.isEmpty(UserInfo.getInstance().getMobile())&&StringUtils.isEmpty(UserInfo.getInstance().getUserName())){
								Intent intent = new Intent(LoginActivity.this,FindPass2Activity.class);
								intent.putExtra(FindPass2Activity.BUNDLE_PARAM_TYPE, FindPass2Activity.TYPE_NO_USERNAME_HAS_MOBILE);
								intent.putExtra(FindPass2Activity.BUNDLE_PARAM_PHONENUM, UserInfo.getInstance().getMobile());
								intent.putExtra(FindPass2Activity.BUNDLE_PARAM_PASSPORTID, UserInfo.getInstance().getUserId());
								intent.putExtra(FindPass2Activity.BUNDLE_PARAM_SPTOKEN, UserInfo.getInstance().getAccessToken());
								startActivityForResult(intent,UPDATE_RESPONSE_CODE);
								return;
							}else if(StringUtils.isEmpty(UserInfo.getInstance().getMobile())&&!StringUtils.isEmpty(UserInfo.getInstance().getUserName())){
								Intent intent = new Intent(LoginActivity.this,FindPass1Activity.class);
								intent.putExtra(FindPass1Activity.BUNDLE_PARAM_TYPE, FindPass1Activity.TYPE_HAS_USERNAME);
								intent.putExtra(FindPass1Activity.BUNDLE_PARAM_PASSPORTID, UserInfo.getInstance().getUserId());
								intent.putExtra(FindPass1Activity.BUNDLE_PARAM_SPTOKEN, UserInfo.getInstance().getAccessToken());
								startActivityForResult(intent,UPDATE_RESPONSE_CODE);
								return;
							}else{
								if(!StringUtils.isEmpty(targetActivity)){
									if("com.jrj.tougu.activity.OpenConsultingActivity".equals(targetActivity) && UserInfo.getInstance().isTougu()
											&&targetActivityType > 0){
										targetActivity = "com.jrj.tougu.activity.WriteOpinionActivity";
									}
									try {
										Class<?> c = Class.forName(targetActivity);
										if( c != null){
											Intent intent = getIntent();
											intent.setClass(LoginActivity.this, c);
											startActivity(intent);
										}
									} catch (ClassNotFoundException e) {
										// TODO Auto-generated catch block
										Logger.error(TAG, "not activity found",e);
										Toast.makeText(LoginActivity.this, "找不到目标Activity", Toast.LENGTH_SHORT).show();
									}
								}
								setResult(LOGINED_RESPONSE_CODE);
								setLoginedBroadcast();
								finish();
							}
						}else{
							Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
						}
					}
					
					@Override
					public void onFailure(String id, int code, String str,Object obj) {
						
						if(obj != null && obj instanceof BaseResultWeb){
							BaseResultWeb data = (BaseResultWeb)obj;
							LogUpdate.getInstance().addLog(LogDataUtils.FUNCTIONID_LOG_USERINFO, NetUrlTougu.USER_IDENTIFY,String.valueOf(data.getRetCode()), data.getMsg());
						}else{
							super.onFailure(id, code, str, obj);
						}
					}
					
				}, UserIdentifiedResult.class){
			
			@Override
			public void addCustomHeader(Map<String,String> header){
				header.put(NetConfig.PASSPORTID, userid);
				header.put(NetConfig.ACCESSTOKEN, accessToken);
			}
			
		};

		send(request);
	}
	
	@Override
	public void onActivityResult(int requestCode,int responseCode,Intent data){
		if(requestCode == UPDATE_RESPONSE_CODE && (responseCode == FindPass1Activity.RESPONSE_UPDATE_SUCCESS 
				|| responseCode == FindPass2Activity.RESPONSE_UPDATE_SUCCESS)){
			setResult(LOGINED_RESPONSE_CODE);
			setLoginedBroadcast();
			finish();
			return;
		}else if(requestCode == UPDATE_RESPONSE_CODE){
			UserInfo.getInstance().clearUserInfo(this);
			return;
		}
		super.onActivityResult(requestCode, responseCode, data);
	}
	
	private void setLoginedBroadcast(){
		Intent intent = new Intent();  
        intent.setAction(LOGINED_ACTION); 
        sendBroadcast(intent);  
	}
	
}
