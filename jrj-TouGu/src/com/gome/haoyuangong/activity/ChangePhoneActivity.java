package com.gome.haoyuangong.activity;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.layout.self.BarItem;
import com.gome.haoyuangong.layout.self.Function;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


public class ChangePhoneActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		content.removeAllViews();
		content.addView(getView());
		
		setTitle("更换手机号");
	}
	
	private LinearLayout getView(){
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 100));
		
		TextView tv = new TextView(this);
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(getResources().getColor(R.color.font_727272));
		tv.setText("更换手机后，下次登录可使用新手机登录");
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 44));
		p.setMargins(0, Function.getFitPx(this, 40), 0, 0);
		layout.addView(tv,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 90));
		tv = new TextView(this);
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(getResources().getColor(R.color.font_727272));
		tv.setText("当前手机号："+getIntent().getStringExtra("name"));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 44));
		layout.addView(tv,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 140));
		final BarItem item = new BarItem(this,true);
		item.setRightArrowVisible(View.INVISIBLE);
		item.setHeadImageVisible(false);
		item.setTitleHint("请输入手机号");
		item.setTitleFontColor(getResources().getColor(R.color.font_cccccc));
		item.addRightImage(R.drawable.delete_item_icon, 30, true,new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				item.setTitle("");
			}
		});
		p.setMargins(0, Function.getFitPx(this, 20), 0, 1);
		layout.addView(item,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 140));
		LinearLayout checkLayout = new LinearLayout(this);
		checkLayout.setBackgroundColor(Color.WHITE);
		checkLayout.setOrientation(LinearLayout.HORIZONTAL);
		p.setMargins(0, 0, 0, Function.getFitPx(this, 120));
		layout.addView(checkLayout,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		tv = new TextView(this);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setText("验证码：");
		tv.setTextColor(getResources().getColor(R.color.font_cccccc));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 44));
//		tv.setBackgroundColor(getResources().getColor(R.color.font_4c87c6));
		p.setMargins(Function.getFitPx(this, 40), 0, 0, 0);
		checkLayout.addView(tv,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
		tv = new EditText(this);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setTextColor(getResources().getColor(R.color.font_cccccc));
		tv.setPadding(0, 0, 0, 0);
		tv.setBackgroundColor(Color.WHITE);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 44));
		p.setMargins(5, 0, 0, 0);
		checkLayout.addView(tv,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		tv = new TextView(this);
		tv.setPadding(5, 0, 5, 0);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setText("发送验证码");
		tv.setTextColor(Color.WHITE);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 44));
		tv.setBackgroundColor(getResources().getColor(R.color.font_4c87c6));
		p.setMargins(0, Function.px2sp(this, 40), Function.px2sp(this, 40), Function.px2sp(this, 40));
		checkLayout.addView(tv,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 140));
		tv = new TextView(this);
		tv.setGravity(Gravity.CENTER);
		tv.setText("提交");
		tv.setTextColor(getResources().getColor(R.color.font_de3031));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 44));
		tv.setBackgroundColor(Color.WHITE);
		layout.addView(tv,p);
		
		return layout;
	}

}
