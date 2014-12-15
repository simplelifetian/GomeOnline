package com.gome.haoyuangong.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.R;

public class AdviserAccreditationActivity extends BaseActivity {
	public static int TIPTYPE;
	private int needback= 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		View view = LayoutInflater.from(this).inflate(R.layout.adviser_accreditation_tip, null);
		needback = getIntent().getIntExtra("needback", 0);
		final TextView phoneView = ((TextView)view.findViewById(R.id.textViewName));
		phoneView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String number = phoneView.getText().toString();  
                //用intent启动拨打电话  
                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));  
                startActivity(intent); 
			}
		});
		if (view != null){
			TextView textView = (TextView)view.findViewById(R.id.tipView);
			ImageView imageView = (ImageView)view.findViewById(R.id.imageViewHead);
			if (TIPTYPE == 1){
				textView.setText(R.string.adviser_accreditation_submit);
				titleLeft1.setBackgroundResource(R.drawable.title_finish);
				imageView.setImageDrawable(getResources().getDrawable(R.drawable.adviser_accred_tip3));
			}
			else if (TIPTYPE == 2){
				textView.setText(R.string.adviser_accreditation_ing);
				imageView.setImageDrawable(getResources().getDrawable(R.drawable.adviser_accred_tip1));
			}
			else if (TIPTYPE == 3){
				textView.setText(R.string.adviser_accreditation_fail);
				imageView.setImageDrawable(getResources().getDrawable(R.drawable.adviser_accred_tip2));
				textView.setText("您的申请未通过审核，"+getIntent().getStringExtra("failereason")+".");
				view.findViewById(R.id.reAccreditate).setVisibility(View.VISIBLE);
				view.findViewById(R.id.reAccreditate).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
//						setResult(RESULT_OK);
						Intent intent = new Intent();
						intent.putExtra(InvestmentAdvisorCertificationActivity.PARAM_REACCEDIATION, 1);
						intent.setClass(AdviserAccreditationActivity.this, InvestmentAdvisorCertificationActivity.class);
						startActivity(intent);
						finish();
					}
				});
			}
			else if (TIPTYPE == 4){
				textView.setText("恭喜您，我们通过了您的投资顾问注册申请，您可以在我们的证券通栏目下开展您的投顾服务。");
				imageView.setImageDrawable(getResources().getDrawable(R.drawable.adviser_accred_tip1));
				view.findViewById(R.id.reAccreditate).setVisibility(View.VISIBLE);
				((TextView)view.findViewById(R.id.textViewContent)).setText("切换到投顾身份");
				view.findViewById(R.id.reAccreditate).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						UserInfo.getInstance().setIsAdviser(1);
						Intent intent = new Intent();
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.setClass(AdviserAccreditationActivity.this, MainActivity.class);
						startActivity(intent);
					}
				});
			}
			content.removeAllViews();
			content.addView(view);
		}
		setTitle("投资顾问认证");
	}
	@Override
	public void onClick(View v){
//		super.onClick(v);
		if (needback != 0){
			super.onClick(v);
			return;
		}
		switch(v.getId()){
			case R.id.title_left1:		
				back();
				break;
		}
	}
	private void back(){
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		
		intent.setClass(AdviserAccreditationActivity.this, MySelfInfoActivity.class);
		startActivity(intent);
		finish();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
			if (needback == 0)
				back();
			else
				finish();
		}
		return true;
	}
}
