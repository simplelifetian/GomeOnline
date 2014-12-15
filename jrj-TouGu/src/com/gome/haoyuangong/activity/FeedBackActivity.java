package com.gome.haoyuangong.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.DeviceStatus;
import com.gome.haoyuangong.LogUpdate;
import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.layout.self.BarItem;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.net.NetConfig;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.TouguBaseResult;
import com.gome.haoyuangong.net.result.XinGeBaseResult;
import com.gome.haoyuangong.net.url.XinGeURL;
import com.gome.haoyuangong.net.volley.JsonRequest;

public class FeedBackActivity extends BaseActivity {
	TextView contentView;
	TextView contactView;
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_feedback);
		contentView = (TextView)findViewById(R.id.feedback_content);
		contactView = (TextView)findViewById(R.id.feedback_contact);
		TextView tv = (TextView)findViewById(R.id.feedback_submit);
		if (tv != null){
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (TextUtils.isEmpty(contentView.getText())){
						Toast.makeText(FeedBackActivity.this, "请您填写宝贵意见", Toast.LENGTH_SHORT).show();
						return;
					}
					submit();
				}
			});
		}
		setTitle("用户反馈");
		titleRight2.setText("提交");
	}
	@Override
	public void onClick(View v){
		super.onClick(v);
		switch(v.getId()){
			case R.id.title_right2:			
				if (TextUtils.isEmpty(contentView.getText())){
					Toast.makeText(FeedBackActivity.this, "请您填写宝贵意见", Toast.LENGTH_SHORT).show();
					return;
				}
				submit();
				break;
		}
	}
	private void submit() {
		String postUrl = LogUpdate.getPostUrl(this, "http://sjcms.jrj.com.cn/api/feedback");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("feedback", contentView.getText().toString());
		if (!TextUtils.isEmpty(contactView.getText().toString()))
			params.put("contact", contactView.getText().toString());
		JsonRequest<TouguBaseResult> request = new JsonRequest<TouguBaseResult>(
				Method.POST, postUrl, params,
//				Method.POST, "http://172.16.20.80/api/feedback", params,
				new RequestHandlerListener<TouguBaseResult>(getContext()) {

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
					public void onSuccess(String id, TouguBaseResult data) {
						// TODO Auto-generated method stub
						if (data.getRetCode() == 0){
							Toast.makeText(FeedBackActivity.this, "反馈提交成功", Toast.LENGTH_SHORT).show();
							finish();
						}
						}
					}, TouguBaseResult.class,true);
		send(request);
	}
	private String getVersion(){
        PackageInfo pkg;
        try {
            pkg = getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
            String versionName = pkg.versionName; 
            return versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } 
     }
	private String getDeviceId(){
		TelephonyManager telephonyManager= (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}
}
