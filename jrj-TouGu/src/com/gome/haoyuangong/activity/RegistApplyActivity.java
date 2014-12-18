package com.gome.haoyuangong.activity;

import com.gome.haoyuangong.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RegistApplyActivity extends BaseActivity {
	Button btnNextStep;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist_apply);
		setTitle("申请开通账户");
		btnNextStep = (Button) findViewById(R.id.nextstep);
		btnNextStep.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(); 
				intent.setClass(RegistApplyActivity.this, GoodsActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void startActivity(Intent intent) {
		// TODO Auto-generated method stub
		super.startActivity(intent);
	}
	
}
