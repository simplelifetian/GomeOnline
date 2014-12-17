package com.gome.haoyuangong.activity;

import com.gome.haoyuangong.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class RegistApplyActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist_apply);
		setTitle("申请开通账户");
	}

	@Override
	public void startActivity(Intent intent) {
		// TODO Auto-generated method stub
		super.startActivity(intent);
	}
	
}
