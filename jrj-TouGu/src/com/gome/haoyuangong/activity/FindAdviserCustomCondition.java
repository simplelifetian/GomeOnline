package com.gome.haoyuangong.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

import com.gome.haoyuangong.views.RectangleSelectOption;

public class FindAdviserCustomCondition extends BaseActivity {
	RectangleSelectOption companyView;
	int ORGNIZATIONTYPE = 1001;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		content.removeAllViews();	
		content.addView(getView());
		setTitle("自定义找投顾");
	}
	
	private ScrollView getView(){
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		RectangleSelectOption rectangleSelectOption = new RectangleSelectOption(this);
		List<String> list = new ArrayList<String>();
		list.add("A股");
		list.add("美股");
		list.add("港股");
		list.add("基金");
		list.add("贵金属");
		list.add("其他领域");
		rectangleSelectOption.setOptionItems(list);
		rectangleSelectOption.setTitle("投顾擅长领域");
		layout.addView(rectangleSelectOption,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		rectangleSelectOption = new RectangleSelectOption(this);
		list.clear();
		list.add("技术面");
		list.add("基本面");
		list.add("长线交易");
		list.add("短线交易");
		list.add("波段操作");
		list.add("个股分析");
		list.add("基金理财");
		list.add("信托");
		rectangleSelectOption.setOptionItems(list);
		rectangleSelectOption.setTitle("投顾能力领域");
		layout.addView(rectangleSelectOption,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		companyView = new RectangleSelectOption(this);
		companyView.setAutoArrange(true);
		companyView.setCanSelectItem(false);
		list.clear();
		list.add("选择证券公司");
		companyView.setOptionItems(list);
		companyView.setTitle("证券公司");
		companyView.setReadOnly(0, true);
		layout.addView(companyView,p);
		companyView.getItem(0).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("viewtype", "pinyin");
				intent.putExtra("multisel", 1);
				intent.setClass(FindAdviserCustomCondition.this, SelectOrgnizationActivity.class);
				startActivityForResult(intent, ORGNIZATIONTYPE);
			}
		});
		ScrollView scrollView = new ScrollView(getContext());
		scrollView.addView(layout);
		return scrollView;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null)
			return;
		if (requestCode == ORGNIZATIONTYPE){
			String[] arr = data.getStringExtra("returnvalue").split(",");
			for(String s:arr){
				companyView.addItem(s,true);
			}
		}
	}
}
