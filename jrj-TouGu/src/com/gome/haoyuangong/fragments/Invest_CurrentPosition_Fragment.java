package com.gome.haoyuangong.fragments;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.ItemSelect;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.activity.BaseActivity;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.layout.self.StockItem;
import com.gome.haoyuangong.layout.self.data.InvestGroupPosition;
import com.gome.haoyuangong.layout.self.data.InvestGroupPosition.InvestGroupPositionItem;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.url.NetUrlMyInfo;
import com.gome.haoyuangong.net.volley.JsonRequest;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Invest_CurrentPosition_Fragment extends ListViewFragment {
	public static int GROUPID;
	private List<InvestGroupPositionItem> stockItems;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setPullRefreshEnable(false);
		setPullLoadEnable(false);
		requestData(false);
		setDividerHeight(0);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		
	}
	
	protected void requestData(boolean pull){
		String url = String.format(NetUrlMyInfo.INVESTGROUPLISTPOSITION, UserInfo.getInstance().getUserId(),GROUPID);
		JsonRequest<InvestGroupPosition> request = new JsonRequest<InvestGroupPosition>(Method.GET, url,
				new RequestHandlerListener<InvestGroupPosition>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						 showDialog(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						 hideDialog(request);
					}

					@SuppressLint("ShowToast") @Override
					public void onSuccess(String id, InvestGroupPosition data) {
						// TODO Auto-generated method stub
						stopRefresh();
						stopLoadMore();
						try{
							clear();
							fillData(data.getData().getList());
							stockItems = data.getData().getList();
							if (stockItems.size() == 0)
								showEmptyView();
							else
								showDataView();
							reFresh();
						}
						catch(Exception e){
						}
						
					}
				}, InvestGroupPosition.class);
		((BaseActivity)getActivity()).send(request);
	}
	private void fillData(List<InvestGroupPositionItem> data){
		StockItem item = new StockItem(getContext());
		item.setBackgroundColor(0xffb5b5b5);
		item.setSpace(40, 20, 0, 20);
		item.doLayout();
		item.setBackgroundColor(getContext().getResources().getColor(R.color.background_f5f5f5));
		item.hideArrow();
		int fontsize = Function.px2sp(getContext(), 40);
		item.getItems().get(0).setText("股票名称");
		item.getItems().get(0).setTextSize(TypedValue.COMPLEX_UNIT_SP,fontsize);
		item.getItems().get(0).setTextColor(getContext().getResources().getColor(R.color.font_727272));
		item.getItems().get(1).setText("仓位");
		item.getItems().get(1).setTextSize(TypedValue.COMPLEX_UNIT_SP,fontsize);
		item.getItems().get(1).setTextColor(getContext().getResources().getColor(R.color.font_727272));
//		item.getItems().get(2).setText("买入价");
//		item.getItems().get(2).setTextSize(TypedValue.COMPLEX_UNIT_SP,fontsize);
//		item.getItems().get(2).setTextColor(getContext().getResources().getColor(R.color.font_999999));
//		item.getItems().get(3).setText("浮动盈亏");
//		item.getItems().get(3).setTextSize(TypedValue.COMPLEX_UNIT_SP,fontsize);
//		item.getItems().get(3).setTextColor(getContext().getResources().getColor(R.color.font_999999));
		LinearLayout headLayout = new LinearLayout(getContext());
		headLayout.setBackgroundColor(getContext().getResources().getColor(R.color.divider_d0d0d0));
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		p.setMargins(0, 0, 0, 1);
		headLayout.addView(item,p);
		addHeadView(headLayout);
		for(int i=0;i<data.size();i++){
			item = new StockItem(getContext());
			item.setBackgroundColor(Color.WHITE);
			item.setTag(data.get(i));
			item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
				}
			});
			item.setSpace(40, 30, 0, 20);
			item.doLayout();
			LinearLayout layout = item.getLayout(0);
			layout.setOrientation(LinearLayout.VERTICAL);
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
			TextView tv = new TextView(getContext());	
			tv.setText(data.get(i).getStockCode());
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setTextColor(getContext().getResources().getColor(R.color.font_8b8b8b));
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(getContext(), 32));
			p.setMargins(0, Function.getFitPx(getContext(), 10), 0, 0);
			layout.addView(tv,p);
			item.getItems().get(0).setText(data.get(i).getStockName());
			item.getItems().get(1).setText(String.valueOf(data.get(i).getPosition())+"%");
//			item.getItems().get(2).setText("12.49");
//			item.getItems().get(3).setText("+10%");
			LinearLayout itemLayout = new LinearLayout(getContext());
			itemLayout.setBackgroundColor(getContext().getResources().getColor(R.color.background_f5f5f5));
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			p.setMargins(0, 0, 0, 1);
			itemLayout.addView(item,p);
			itemLayout.setBackgroundColor(getContext().getResources().getColor(R.color.divider_d0d0d0));
			addItem(itemLayout);
		}
	}

}
