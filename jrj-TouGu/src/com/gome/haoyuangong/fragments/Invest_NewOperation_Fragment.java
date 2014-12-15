package com.gome.haoyuangong.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.layout.self.StockItem;

public class Invest_NewOperation_Fragment extends ListViewFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		setPullRefreshEnable(false);
		setPullLoadEnable(false);
		addItems();
		reFresh();
	}
	
	private void addItems(){
		StockItem item = new StockItem(getContext());
		item.setSpace(40, 20, 0, 20);
		item.setBackgroundColor(getContext().getResources().getColor(R.color.background_ECECEC));
		item.doLayout();
		int fontsize = Function.px2sp(getContext(), 40);
		item.getItems().get(0).setText("股票名称");			
		item.getItems().get(0).setTextSize(TypedValue.COMPLEX_UNIT_SP,fontsize);
		item.getItems().get(0).setTextColor(getContext().getResources().getColor(R.color.font_999999));
		item.getItems().get(1).setText("操作");
		item.getItems().get(1).setTextSize(TypedValue.COMPLEX_UNIT_SP,fontsize);
		item.getItems().get(1).setTextColor(getContext().getResources().getColor(R.color.font_999999));
		item.getItems().get(2).setText("成交均价");
		item.getItems().get(2).setTextSize(TypedValue.COMPLEX_UNIT_SP,fontsize);
		item.getItems().get(2).setTextColor(getContext().getResources().getColor(R.color.font_999999));
		item.getItems().get(3).setText("成交日期");
		item.getItems().get(3).setTextSize(TypedValue.COMPLEX_UNIT_SP,fontsize);
		item.getItems().get(3).setTextColor(getContext().getResources().getColor(R.color.font_999999));
		addHeadView(item);
		for(int i=0;i<10;i++){
			item = new StockItem(getContext());
			item.setSpace(40, 30, 0, 20);
			item.doLayout();
			LinearLayout layout = item.getLayout(0);
			layout.setOrientation(LinearLayout.VERTICAL);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
			TextView tv = new TextView(getContext());	
			tv.setText("60000");
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setTextColor(getContext().getResources().getColor(R.color.font_8b8b8b));
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(getContext(), 32));
			p.setMargins(0, Function.getFitPx(getContext(), 10), 0, 0);
			layout.addView(tv,p);
			item.getItems().get(0).setText("平安银行");
			item.getItems().get(1).setText("买入");
			item.getItems().get(2).setText("12.49");
			item.getItems().get(3).setText("2014.10.21");
			addItem(item);
		}
	}
}
