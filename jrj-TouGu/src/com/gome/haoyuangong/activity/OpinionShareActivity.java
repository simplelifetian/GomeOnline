package com.gome.haoyuangong.activity;

import com.gome.haoyuangong.R;

import android.os.Bundle;


public class OpinionShareActivity extends BaseActivity {

	private static final String TAG = OpinionShareActivity.class.getName();


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opinion_share_layout);
		setTitle("分享");
	}
}
