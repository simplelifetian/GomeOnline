/**
 * 
 */
package com.gome.haoyuangong.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.tougu.CheckPhoneNumReslt;
import com.gome.haoyuangong.net.result.tougu.RegistResultBean;
import com.gome.haoyuangong.net.url.NetUrlLoginAndRegist;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.presenter.IRegistPresenter;
import com.gome.haoyuangong.utils.CheckUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.utils.CheckUtils.CheckResponse;

/**
 *
 */
public class FindPass1Activity extends BaseActivity{
	
	private static final String TAG = FindPass1Activity.class.getName();
	public static final String BUNDLE_PARAM_TYPE = "param_type";
	
	public static final int INTERVAL = 60;
	
	public static final int TYPE_FIND_PASS = 1;//找回密码
	public static final int TYPE_NO_USERNAME = 2;//无用户名
	public static final int TYPE_HAS_USERNAME = 3;//有用户名
	
	public static final String BUNDLE_PARAM_PASSPORTID = "passport_id";
	public static final String BUNDLE_PARAM_SPTOKEN = "sp_token";
	
	public static final int REQUEST_UPDATE = 1030;
	public static final int RESPONSE_UPDATE_SUCCESS = 1031;
	
	private Button nextStep;
	
	private EditText phoneNum;
	
	private EditText vCode;
	
	private TextView getVcode;
	
	private TimerTask task;
	private Timer timer = new Timer();  
	private int time = INTERVAL;
	
	private String phoneNumStr;
	private String passportId;
	private String phoneVcode;
	private String spToken;
	
	private int type;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findpass_1);
		type = getIntent().getIntExtra(BUNDLE_PARAM_TYPE, TYPE_FIND_PASS);
		initViews();
		switch(type){
			case TYPE_FIND_PASS:{
				initFindPass();
				break;
			}
			case TYPE_NO_USERNAME:{
				initNoUsername();
				break;
			}
			case TYPE_HAS_USERNAME:{
				initHasUsername();
				break;
			}
		}
		
	}
	
	private void initViews(){
		phoneNum = (EditText)findViewById(R.id.phone_num);
		vCode = (EditText)findViewById(R.id.et_vcode);
		getVcode = (TextView)findViewById(R.id.get_vcode);
		getVcode.setOnClickListener(this);
		
		nextStep = (Button)findViewById(R.id.next_step);
		nextStep.setOnClickListener(this);
	}
	
	private void initFindPass(){
		setTitle("找回密码");
		nextStep.setText("下一步");
	}
	
	private void initNoUsername(){
		setTitle("完善信息");
		nextStep.setText("下一步");
		passportId = getIntent().getStringExtra(BUNDLE_PARAM_PASSPORTID);
		spToken = getIntent().getStringExtra(BUNDLE_PARAM_SPTOKEN);
		if(StringUtils.isEmpty(passportId)||StringUtils.isEmpty(spToken)){
			Toast.makeText(this, "账户错误", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
	}
	private void initHasUsername(){
		setTitle("完善信息");
		nextStep.setText("完成");
		passportId = getIntent().getStringExtra(BUNDLE_PARAM_PASSPORTID);
		spToken = getIntent().getStringExtra(BUNDLE_PARAM_SPTOKEN);
		if(StringUtils.isEmpty(passportId)||StringUtils.isEmpty(spToken)){
			Toast.makeText(this, "账户错误", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
	}
	
	IRegistPresenter check = new IRegistPresenter(this) {
		public void onVerifyCode (boolean isOk) {
			if (isOk) {
				switch(type){
					case TYPE_FIND_PASS:{
							Intent intent = new Intent(FindPass1Activity.this,FindPass2Activity.class);
							intent.putExtra(FindPass2Activity.BUNDLE_PARAM_TYPE, FindPass2Activity.TYPE_FIND_PASS);
							intent.putExtra(FindPass2Activity.BUNDLE_PARAM_PHONENUM, phoneNumStr);
							intent.putExtra(FindPass2Activity.BUNDLE_PARAM_PASSPORTID, passportId);
							intent.putExtra(FindPass2Activity.BUNDLE_PARAM_SPTOKEN, phoneVcode);
							startActivity(intent);
							break;
					}
						case TYPE_NO_USERNAME:{
							Intent intent = new Intent(FindPass1Activity.this,FindPass2Activity.class);
							intent.putExtra(FindPass2Activity.BUNDLE_PARAM_TYPE, FindPass2Activity.TYPE_NO_USERNAME_NO_MOBILE);
							intent.putExtra(FindPass2Activity.BUNDLE_PARAM_PHONENUM, phoneNumStr);
							intent.putExtra(FindPass2Activity.BUNDLE_PARAM_PASSPORTID, passportId);
							intent.putExtra(FindPass2Activity.BUNDLE_PARAM_SPTOKEN, spToken);
							startActivityForResult(intent,REQUEST_UPDATE);
							break;
						}
					case TYPE_HAS_USERNAME:{
						Map<String,String> params = new HashMap<String,String>();
						params.put("mobile", phoneNumStr);
						params.put("passportId", passportId);
						params.put("bizSource", "android");
						params.put("spToken", spToken);
						Logger.error(TAG, "sptoken "+spToken);
						check.updateUserInfo(params);
						break;
					}
				}
			}
		}
		public void onGetCodeFailure() {
			getVcode.setEnabled(true);
			getVcode.setText("获取验证码");
			task.cancel();
		};
		
		@Override
		public void onUpdateOk(String id, RegistResultBean data){
			if(data.getResultCode() == 0){
				Toast.makeText(FindPass1Activity.this, "完善信息成功", Toast.LENGTH_SHORT).show();
				UserInfo.getInstance().setMobile(phoneNumStr);
				UserInfo.saveUserInfo(FindPass1Activity.this, UserInfo.getInstance());
				setResult(RESPONSE_UPDATE_SUCCESS);
				Intent intent = new Intent(getContext(), StartQuestionnaireActivity.class);
				startActivity(intent);
				finish();
			}else{
				Toast.makeText(FindPass1Activity.this, data.getResultMsg(), Toast.LENGTH_SHORT).show();
			}
		}
		
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch(v.getId()){
		case R.id.next_step:
			String _phoneNumStr = phoneNum.getText().toString();
			if(StringUtils.isEmpty(_phoneNumStr)){
				Toast.makeText(FindPass1Activity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
				return;
			}
			
			String vcode = vCode.getText().toString();
			if(StringUtils.isEmpty(vcode)){
				Toast.makeText(FindPass1Activity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
				return;
			}
			this.phoneVcode = vcode;
			check.verifyCode(vcode, phoneNumStr);
			break;
		case R.id.get_vcode:
			//验证手机是否已注册
			String phoneNumStr = phoneNum.getText().toString();
			if(StringUtils.isEmpty(phoneNumStr)){
				Toast.makeText(FindPass1Activity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
				return;
			}
			CheckResponse cr = CheckUtils.isValidPhoneNum(phoneNumStr);
			if(!cr.isValid){
				showToast(cr.rtMsg);
				return ;
			}
			checkPhoneNum(phoneNumStr);
			break;
		}
	}
	
	public void checkPhoneNum(final String number){
		Map<String, String> params =new HashMap<String, String>();
		params.put("mobile", number);
		JsonRequest<CheckPhoneNumReslt> request = new JsonRequest<CheckPhoneNumReslt>(Method.POST,NetUrlLoginAndRegist.CHECK_PHONE,params, new RequestHandlerListener<CheckPhoneNumReslt>(getContext()) {
			
			@Override
			public void onSuccess(String id, CheckPhoneNumReslt data) {
				Toast.makeText(FindPass1Activity.this, "无此手机号", Toast.LENGTH_SHORT).show();
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
				if(1 == code){
					time = INTERVAL;
					phoneNumStr = number;
					passportId = ((CheckPhoneNumReslt)obj).getPassportId();
					check.getVCode(phoneNumStr);
					getVcode.setEnabled(false);
					task = new TimerTask() {
						@Override
						public void run() {
							runOnUiThread(new Runnable() { // UI thread
								@Override
								public void run() {
									if (time <= 0) {
										getVcode.setEnabled(true);
										getVcode.setText("获取验证码");
										task.cancel();
									} else {
										getVcode.setText(time+"s");
									}
									time--;
								}
							});
						}
					};
					timer.schedule(task, 0, 1000);
				}else{
					Toast.makeText(FindPass1Activity.this, str, Toast.LENGTH_SHORT).show();
				}
			}
		}, CheckPhoneNumReslt.class);
		send(request);
	}
	
	@Override
	public void onActivityResult(int requestCode,int responseCode,Intent data){
		if(requestCode == REQUEST_UPDATE && (responseCode == FindPass2Activity.RESPONSE_UPDATE_SUCCESS)){
			setResult(RESPONSE_UPDATE_SUCCESS);
			finish();
			return;
		}
		super.onActivityResult(requestCode, responseCode, data);
	}
}
