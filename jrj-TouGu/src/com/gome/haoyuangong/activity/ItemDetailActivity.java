package com.gome.haoyuangong.activity;

import com.gome.haoyuangong.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;


public class ItemDetailActivity extends ListViewActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		
		setContentView(intent.getIntExtra("layoutid", R.layout.invest_operate_log));
		if (intent.getStringExtra("title") != null)
			setTitle(intent.getStringExtra("title"));
		
	}
	
	

}
