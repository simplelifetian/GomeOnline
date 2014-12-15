package com.gome.haoyuangong.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.activity.AttentionDetailActivity;
import com.gome.haoyuangong.activity.ItemDetailActivity;

public class Invest_OrperateLog_Fragment extends ListViewFragment {
	LayoutInflater inflater;
	ViewGroup container;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.inflater = inflater;
		this.container = container;
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		addItems();
		reFresh();
	}
	
	/**
	 * 测试数据
	 */
	private void addItems(){
		for(int i=0;i<10;i++){
			View view = inflater.inflate(R.layout.invest_operate_log, container,false);
			LinearLayout layout = new LinearLayout(getContext());
			layout.addView(view);
			if (view == null)
				return;
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent aIntent = new Intent(getActivity(), ItemDetailActivity.class);
					aIntent.putExtra("title", "日志详情");
					aIntent.putExtra("layoutid", R.layout.invest_operate_log);
					startActivity(aIntent);
				}
			});
//			_items.add(opinionItem);
			addItem(layout);
		}
	}
}
