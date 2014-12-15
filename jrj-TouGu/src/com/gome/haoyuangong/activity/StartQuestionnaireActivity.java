package com.gome.haoyuangong.activity;


import com.gome.haoyuangong.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartQuestionnaireActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_questionnaire);
		setTitle("选投顾");
		init();
	}

	private void init() {
		titleLeft1.setVisibility(View.GONE);
		titleRight1.setText("跳过");
		titleRight1.setVisibility(View.VISIBLE);
		Button btnnext = (Button) findViewById(R.id.btnnext);
		btnnext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(StartQuestionnaireActivity.this,
						QuestionnaireActivity.class),1);
			}

		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_right1:
			finish();
			break;
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		finish();
	}
	
	

}
