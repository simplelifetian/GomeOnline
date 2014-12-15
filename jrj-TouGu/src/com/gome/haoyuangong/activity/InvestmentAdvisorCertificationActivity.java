package com.gome.haoyuangong.activity;

import java.util.HashMap;
import java.util.Map;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.layout.self.data.InvestAdviserInfo;
import com.gome.haoyuangong.layout.self.data.UserInfoForApply;
import com.gome.haoyuangong.layout.self.data.UserInfoForApply.Item;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.url.NetUrlMyInfo;
import com.gome.haoyuangong.net.volley.JsonRequest;

import android.R.bool;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InvestmentAdvisorCertificationActivity extends BaseActivity{
	public static Map<String, String> ParamsMap = new HashMap<String, String>();
	public static String PARAM_REACCEDIATION = "reaccediation";
	Item item;
	int ORGNIZATIONTYPE = 1001;
	int ADDRESSTYPE = 1002;
	int WORKLIMITTYPE = 1003;
	int workLimitIndex;
	String positionFXS = "投资顾问（分析师）";
	String positionTG = "投资顾问（投资顾问）";
	String city,province;
	int reaccediation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		reaccediation = getIntent().getIntExtra(PARAM_REACCEDIATION, 0);
		setContentView(R.layout.activity_investment_advisor_certification);
		setTitle("投资顾问认证");
		Button btn = (Button)findViewById(R.id.btnnext);
		if (btn != null){
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TextView textView = (TextView)findViewById(R.id.name_text);
					if (TextUtils.isEmpty(textView.getText())){
						Toast.makeText(getContext(), "请填写姓名", Toast.LENGTH_SHORT).show();
						return;
					}
					textView = (TextView)findViewById(R.id.sfz_Text);
					if (TextUtils.isEmpty(textView.getText())){
						Toast.makeText(getContext(), "请填写身份证", Toast.LENGTH_SHORT).show();
						return;
					}
					textView = (TextView)findViewById(R.id.jgmc_Text);
					if (TextUtils.isEmpty(textView.getText())){
						Toast.makeText(getContext(), "请选择机构名称", Toast.LENGTH_SHORT).show();
						return;
					}
					textView = (TextView)findViewById(R.id.address_Text);
					if (TextUtils.isEmpty(textView.getText())){
						Toast.makeText(getContext(), "请选择所在地", Toast.LENGTH_SHORT).show();
						return;
					}
					textView = (TextView)findViewById(R.id.zsbh_Text);
					if (TextUtils.isEmpty(textView.getText())){
						Toast.makeText(getContext(), "请填写证书编号", Toast.LENGTH_SHORT).show();
						return;
					}
					textView = (TextView)findViewById(R.id.yearid);
					if (TextUtils.isEmpty(textView.getText())){
						Toast.makeText(getContext(), "请选择从业年限", Toast.LENGTH_SHORT).show();
						return;
					}
					textView = (TextView)findViewById(R.id.qq_Text);
					if (TextUtils.isEmpty(textView.getText())){
						Toast.makeText(getContext(), "请填写QQ号码", Toast.LENGTH_SHORT).show();
						return;
					}
					putParams(item);
					Intent intent = new Intent();
					intent.setClass(InvestmentAdvisorCertificationActivity.this, AdviserAccreditateActivity.class);
					startActivity(intent);
				}
			});
		}
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.jgmc_layout);
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("viewtype", "pinyin");
				intent.setClass(InvestmentAdvisorCertificationActivity.this, SelectOrgnizationActivity.class);
				startActivityForResult(intent, ORGNIZATIONTYPE);
			}
		});
		layout = (RelativeLayout)findViewById(R.id.address_layout);
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("viewtype", "pinyin");
				intent.putExtra("flag", "1");
				intent.putExtra("title", "选择省份");
				intent.setClass(InvestmentAdvisorCertificationActivity.this, SelectAddressActivity.class);
				startActivityForResult(intent, ADDRESSTYPE);
			}
		});
		layout = (RelativeLayout)findViewById(R.id.year_layout);
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("name", ((TextView)v.findViewById(R.id.yearid)).getText());
				intent.setClass(InvestmentAdvisorCertificationActivity.this, SelectWorkLimitActivity.class);
				startActivityForResult(intent, WORKLIMITTYPE);
			}
		});
		requestData();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null)
			return;
		if (requestCode == ORGNIZATIONTYPE)
			((TextView)findViewById(R.id.jgmc_Text)).setText(data.getStringExtra("returnvalue"));
		else if (requestCode == ADDRESSTYPE){
			((TextView)findViewById(R.id.address_Text)).setText(data.getStringExtra("returnvalue"));
			province = data.getStringExtra("province");
			city = data.getStringExtra("city");
			ParamsMap.put("province", data.getStringExtra("province"));
			ParamsMap.put("city", data.getStringExtra("city"));
		}
		if (requestCode == WORKLIMITTYPE){
			((TextView)findViewById(R.id.yearid)).setText(data.getStringExtra("returnvalue"));
			workLimitIndex = data.getIntExtra("returnindex", 0);
		}
	}
	
	private void requestData() {
		String url = String.format(NetUrlMyInfo.USERINFOFORAPPLY, UserInfo.getInstance().getUserId());
		JsonRequest<UserInfoForApply> request = new JsonRequest<UserInfoForApply>(Method.GET, url,
				new RequestHandlerListener<UserInfoForApply>(getContext()) {

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
					public void onSuccess(String id, UserInfoForApply data) {
						// TODO Auto-generated method stub
						try{
							fillData(data);
						}
						catch(Exception e){
							
						}
						
					}
				}, UserInfoForApply.class);
			send(request);
	}
	
	private void fillData(UserInfoForApply data) {
		item = data.getData();
		TextView textView = (TextView)findViewById(R.id.name_text);
		if (reaccediation > 0 || TextUtils.isEmpty(item.getUserName())){
			textView.setEnabled(true);
		}
		else{
			textView.setEnabled(false);
		}
		textView.setText(item.getUserName());
//		RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
		if (item.getSex() == 2)
			((RadioButton)findViewById(R.id.womanRadio)).setChecked(true);
		else
			((RadioButton)findViewById(R.id.manRadio)).setChecked(true);
		textView = (TextView)findViewById(R.id.sfz_Text);
		if (reaccediation > 0 || TextUtils.isEmpty(item.getIdentityId())){
			textView.setEnabled(true);
		}
		else{
			textView.setEnabled(false);
		}
		textView.setText(item.getIdentityId());
		textView = (TextView)findViewById(R.id.jgmc_Text);
		textView.setText(item.getCompany());
		textView = (TextView)findViewById(R.id.address_Text);
		textView.setText(item.getProvince()+" "+item.getCity());
		textView = (TextView)findViewById(R.id.qq_Text);
		textView.setText(item.getQq());
		
		if (item.getPosition()== positionTG)
			((RadioButton)findViewById(R.id.tzgwRadio)).setChecked(true);
		else
			((RadioButton)findViewById(R.id.fxsRadio)).setChecked(true);
		textView = (TextView)findViewById(R.id.zsbh_Text);
		textView.setText(item.getCertificationNum());
		textView = (TextView)findViewById(R.id.yearid);
		workLimitIndex = item.getExperienceScope();
		if (workLimitIndex == 0)
			textView.setText("");
		else if (workLimitIndex == 1)
			textView.setText("3年以下");
		else if (workLimitIndex == 2)
			textView.setText("3-5年"); 
		else if (workLimitIndex == 3)
			textView.setText("5-10年");
		else if (workLimitIndex == 4)
			textView.setText("10年以上");
	}
	private boolean isNull(Object obj){
		return obj==null ||obj.toString().equals("null");
	}
	private void putParams(Item item){
		if (!isNull(UserInfo.getInstance().getUserId())){
			ParamsMap.put("userId", UserInfo.getInstance().getUserId());
			ParamsMap.put("loginId", UserInfo.getInstance().getUserId());
		}
		if (TextUtils.isEmpty(province))
			ParamsMap.put("province", item.getCity());
		if (TextUtils.isEmpty(city))
			ParamsMap.put("city", item.getProvince());
		ParamsMap.put("userName", ((TextView)findViewById(R.id.name_text)).getText().toString());
		if (!isNull(((TextView)findViewById(R.id.zsbh_Text)).getText().toString()))
			ParamsMap.put("certificationNum", ((TextView)findViewById(R.id.zsbh_Text)).getText().toString());
		if (!isNull(((TextView)findViewById(R.id.jgmc_Text)).getText().toString()))
			ParamsMap.put("company", ((TextView)findViewById(R.id.jgmc_Text)).getText().toString());
//		ParamsMap.put("ctime", String.valueOf(item.getCtime()));
//		ParamsMap.put("experienceScope", "2");
//		ParamsMap.put("id", String.valueOf(item.getId()));
		if (!isNull(((TextView)findViewById(R.id.sfz_Text)).getText().toString()))
			ParamsMap.put("identityId", ((TextView)findViewById(R.id.sfz_Text)).getText().toString());
		if (item!=null && !isNull(item.getIntro()))
			ParamsMap.put("intro", item.getIntro());
		if (item!=null && !isNull(item.getInvestDirection()))
			ParamsMap.put("investDirection", item.getInvestDirection());
		if (item!=null && !isNull(item.getLabel()))
			ParamsMap.put("label", item.getLabel());
		if (item!=null && !isNull(item.getMobile()))
			ParamsMap.put("mobile", item.getMobile());
		ParamsMap.put("qq", ((TextView)findViewById(R.id.qq_Text)).getText().toString());
		if (((RadioButton)findViewById(R.id.manRadio)).isChecked())
			ParamsMap.put("sex", "1");
		else
			ParamsMap.put("sex", "2");
		if (((RadioButton)findViewById(R.id.tzgwRadio)).isChecked())
			ParamsMap.put("position", positionTG);
		else
			ParamsMap.put("position", positionFXS);
		ParamsMap.put("experienceScope", String.valueOf(workLimitIndex));
		ParamsMap.put("passportName", UserInfo.getInstance().getUserName());
		ParamsMap.put("writeNum", "1");
		if (item!=null && !TextUtils.isEmpty(item.getHeadImage()))
			ParamsMap.put("headImage", item.getHeadImage());
		if (item!=null && !TextUtils.isEmpty(item.getIdentityImage()))
			ParamsMap.put("identityImage", item.getIdentityImage());
	}

}
