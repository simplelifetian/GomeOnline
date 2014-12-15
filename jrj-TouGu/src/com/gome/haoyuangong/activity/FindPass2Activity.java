/**
 * 
 */
package com.gome.haoyuangong.activity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.net.result.tougu.RegistResultBean;
import com.gome.haoyuangong.presenter.IRegistPresenter;
import com.gome.haoyuangong.utils.CheckUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.utils.CheckUtils.CheckResponse;

/**
 *
 */
public class FindPass2Activity extends BaseActivity{
	
	private static final String TAG = FindPass2Activity.class.getName();
	
	public static final String BUNDLE_PARAM_TYPE = "param_type";
	public static final int TYPE_FIND_PASS = 1;//找回密码
	public static final int TYPE_NO_USERNAME_NO_MOBILE = 2;//无用户名
	public static final int TYPE_NO_USERNAME_HAS_MOBILE = 3;//无用户名
	
	public static final String BUNDLE_PARAM_PHONENUM = "phone_num";
	public static final String BUNDLE_PARAM_PASSPORTID = "passport_id";
	public static final String BUNDLE_PARAM_SPTOKEN = "sp_token";
	
	public static final int RESPONSE_UPDATE_SUCCESS = 1031;
	
	private Button finish;
	
	private TextView jrjAccount;
	
	private EditText passwd;
	
	private String phoneNum;
	private String passportId;
	private String spToken;
	
	private int type;
	
	private RelativeLayout passwdLo;
	
	private RelativeLayout unameLo;
	
	private EditText etUname;
	private String userName;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findpass_2);
		type = getIntent().getIntExtra(BUNDLE_PARAM_TYPE, TYPE_FIND_PASS);
		
		initViews();
		
		switch(type){
			case TYPE_FIND_PASS:{
				initFindPass();
				break;
			}
			case TYPE_NO_USERNAME_NO_MOBILE:{
				initNoUsernameNoMobile();
				break;
			}
			case TYPE_NO_USERNAME_HAS_MOBILE:{
				initNoUsernameHasMobile();
				break;
			}
		}
		
	}
	
	private void initViews(){
		jrjAccount = (TextView)findViewById(R.id.jrj_account);
//		jrjAccount.setText(phoneNum);
		passwd = (EditText)findViewById(R.id.passwd);
		etUname = (EditText)findViewById(R.id.et_uname);
		finish = (Button)findViewById(R.id.finish);
		finish.setOnClickListener(this);
		passwdLo = (RelativeLayout)findViewById(R.id.passwd_lo);
		unameLo = (RelativeLayout)findViewById(R.id.uname_lo);
	}
	
	private void initFindPass(){
		setTitle("密码找回");
		phoneNum = getIntent().getStringExtra(BUNDLE_PARAM_PHONENUM);
		passportId = getIntent().getStringExtra(BUNDLE_PARAM_PASSPORTID);
		spToken = getIntent().getStringExtra(BUNDLE_PARAM_SPTOKEN);
		if(StringUtils.isEmpty(phoneNum)||StringUtils.isEmpty(passportId)||StringUtils.isEmpty(spToken)){
			Toast.makeText(this, "账户错误", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		jrjAccount.setText(StringUtils.maskMobile(phoneNum));
		passwdLo.setVisibility(View.VISIBLE);
		unameLo.setVisibility(View.GONE);
	}
	
	private void initNoUsernameNoMobile(){
		setTitle("完善信息");
		phoneNum = getIntent().getStringExtra(BUNDLE_PARAM_PHONENUM);
		passportId = getIntent().getStringExtra(BUNDLE_PARAM_PASSPORTID);
		spToken = getIntent().getStringExtra(BUNDLE_PARAM_SPTOKEN);
		if(StringUtils.isEmpty(phoneNum)||StringUtils.isEmpty(passportId)||StringUtils.isEmpty(spToken)){
			Toast.makeText(this, "账户错误", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		jrjAccount.setText(StringUtils.maskMobile(phoneNum));
		passwdLo.setVisibility(View.GONE);
		unameLo.setVisibility(View.VISIBLE);
		
	}
	
	private void initNoUsernameHasMobile(){
		setTitle("完善信息");
		phoneNum = getIntent().getStringExtra(BUNDLE_PARAM_PHONENUM);
		passportId = getIntent().getStringExtra(BUNDLE_PARAM_PASSPORTID);
		spToken = getIntent().getStringExtra(BUNDLE_PARAM_SPTOKEN);
		if(StringUtils.isEmpty(phoneNum)||StringUtils.isEmpty(passportId)||StringUtils.isEmpty(spToken)){
			Toast.makeText(this, "账户错误", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		jrjAccount.setText(StringUtils.maskMobile(phoneNum));
		passwdLo.setVisibility(View.GONE);
		unameLo.setVisibility(View.VISIBLE);
	}

	IRegistPresenter rigist = new IRegistPresenter(this) {
		
		@Override
		public void onUpdateOk(String id, RegistResultBean data){
			if(data.getResultCode() == 0){
				switch(type){
					case TYPE_FIND_PASS:{
						Toast.makeText(FindPass2Activity.this, "找回密码成功，请重新登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(FindPass2Activity.this,LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						break;
					}
					case TYPE_NO_USERNAME_NO_MOBILE:{
						Toast.makeText(FindPass2Activity.this, "完善信息成功", Toast.LENGTH_SHORT).show();
						UserInfo.getInstance().setMobile(phoneNum);
						UserInfo.getInstance().setUserName(userName);
						UserInfo.saveUserInfo(FindPass2Activity.this, UserInfo.getInstance());
						setResult(RESPONSE_UPDATE_SUCCESS);
						Intent intent = new Intent(getContext(), StartQuestionnaireActivity.class);
						startActivity(intent);
						finish();
						break;
					}
					case TYPE_NO_USERNAME_HAS_MOBILE:{
						Toast.makeText(FindPass2Activity.this, "完善信息成功", Toast.LENGTH_SHORT).show();
						UserInfo.getInstance().setUserName(userName);
						UserInfo.saveUserInfo(FindPass2Activity.this, UserInfo.getInstance());
						setResult(RESPONSE_UPDATE_SUCCESS);
						Intent intent = new Intent(getContext(), StartQuestionnaireActivity.class);
						startActivity(intent);
						finish();
						break;
					}
				}
				
			}else{
				Toast.makeText(FindPass2Activity.this, data.getResultMsg(), Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch(v.getId()){
		case R.id.finish:
			
			Map<String,String> params = new HashMap<String,String>();
			switch(type){
				case TYPE_FIND_PASS:{
					String passwdStr = passwd.getText().toString();
					if(StringUtils.isEmpty(passwdStr)){
						Toast.makeText(FindPass2Activity.this, "请输入密码", Toast.LENGTH_SHORT).show();
						return;
					}
					CheckResponse checkResponse = CheckUtils.isValidPassword(passwdStr);
					if(!checkResponse.isValid){
						Toast.makeText(FindPass2Activity.this, checkResponse.rtMsg, Toast.LENGTH_SHORT).show();
						return;
					}
//					params.put("password", passwdStr);
					params.put("password", StringUtils.md5(passwdStr.toLowerCase(Locale.CHINA)));
					params.put("password1", StringUtils.md5(passwdStr));
					params.put("mobile", phoneNum);
					params.put("passportId", passportId);
					params.put("bizSource", "android");
					params.put("spToken", spToken);
					break;
				}
				case TYPE_NO_USERNAME_NO_MOBILE:{
					userName = etUname.getText().toString();
					if(StringUtils.isEmpty(userName)){
						Toast.makeText(FindPass2Activity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
						return;
					}
					CheckResponse checkResponse = CheckUtils.CheckUserName(userName);
					if(!checkResponse.isValid){
						Toast.makeText(FindPass2Activity.this, checkResponse.rtMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					params.put("passportName", userName);
					params.put("mobile", phoneNum);
					params.put("passportId", passportId);
					params.put("bizSource", "android");
					params.put("spToken", spToken);
					break;
				}
				case TYPE_NO_USERNAME_HAS_MOBILE:{
					userName = etUname.getText().toString();
					if(StringUtils.isEmpty(userName)){
						Toast.makeText(FindPass2Activity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
						return;
					}
					CheckResponse checkResponse = CheckUtils.CheckUserName(userName);
					if(!checkResponse.isValid){
						Toast.makeText(FindPass2Activity.this, checkResponse.rtMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					params.put("passportName", userName);
					params.put("passportId", passportId);
					params.put("bizSource", "android");
					params.put("spToken", spToken);
					break;
				}
			}
			rigist.updateUserInfo(params);
			break;
		}
		
	}
	
	
	
	
}
