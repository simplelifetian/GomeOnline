package com.gome.haoyuangong.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.dialog.CustomDialog;
import com.gome.haoyuangong.dialog.DialogManage;
import com.gome.haoyuangong.dialog.CustomDialog.Builder;
import com.gome.haoyuangong.fragments.Invest_CurrentPosition_Fragment;
import com.gome.haoyuangong.layout.self.BarItem;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.layout.self.InvestGroupChart;
import com.gome.haoyuangong.layout.self.InvestGroupData;
import com.gome.haoyuangong.layout.self.SelfView.UserType;
import com.gome.haoyuangong.layout.self.data.InvestGroupList;
import com.gome.haoyuangong.layout.self.data.InvestGroupList.InvestGroupItem;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.url.NetUrlMyInfo;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.DateUtils;

public class InvestGroupActivity extends ListViewActivity {
	private List<InvestGroupData.CategoryData> dataList;
	private int relation = 0;
	int NEWINVESTGROUPTYPEID = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataList = new ArrayList<InvestGroupData.CategoryData>();
		addDatas();
		setTitle("投资组合");
//		titleRight2.setText("创建组合");
//	     titleRight2.setGravity(Gravity.CENTER);
//	     titleRight2.setOnClickListener(this);
	     setPullLoadEnable(false);
//	     setPullRefreshEnable(false);
	     setDividerHeight(0);
	     requestData(true);
	}

	@Override
	public void onClick(View v){
		super.onClick(v);
		switch(v.getId()){
			case R.id.title_right2:			
				Intent intent = new Intent();
				intent.setClass(this, NewInvestGroupActivity.class);
				startActivityForResult(intent,NEWINVESTGROUPTYPEID);
				
//				startActivity(new Intent(this,NewInvestGroupActivity.class));
				break;
		}
	}
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
    super.onActivityResult(requestCode, resultCode, data);  
		 if (data == null)
			return;
		if (requestCode == NEWINVESTGROUPTYPEID){
			View item = newItem(data.getStringExtra("title"));
			addItem(item);
			reFresh();
			Toast.makeText(this, data.getStringExtra("title")+":"+data.getStringExtra("content"), Toast.LENGTH_SHORT).show();
		}
    }
	
	private View newItem(String groupname){
		final View item = LayoutInflater.from(this).inflate(R.layout.invest_group_item, null);
		final TextView tv = (TextView)item.findViewById(R.id.igid_name);
		item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(InvestGroupActivity.this, InvestGroupDetailActivity.class);
				intent.putExtra("name", tv.getText().toString());
				startActivity(intent);
			}
		});
		
		tv.setText(groupname);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		p.setMargins(0, 0, Function.getFitPx(this, 80), 0);
		LinearLayout layout = (LinearLayout)item.findViewById(R.id.chartlayout);
		if (layout != null){
			InvestGroupChart chart = new InvestGroupChart(this);
			chart.setBackgroundColor(Color.WHITE);
			chart.setDataList(dataList);
			layout.addView(chart,p);
		}
		return item;
	}
	
 	private void addDatas(){
		
		dataList.clear();
		dataList.add(new InvestGroupData().new CategoryData());
		dataList.add(new InvestGroupData().new CategoryData());
		for(int i=1;i<11;i++){
			InvestGroupData.CategoryDatas data = new InvestGroupData().new CategoryDatas();
			
			data.setDate(20141016);
			data.setValue(i);
			dataList.get(0).getItems().add(data);
			dataList.get(1).getItems().add(data);
		}		
	}	
 	protected void requestData(boolean pull){
		String url="";
		if (getIntent().getIntExtra("usertype", -1) == UserType.utUserViewAdviser.ordinal())
			url = String.format(NetUrlMyInfo.INVESTGROUPLIST, getIntent().getStringExtra("viewid"));
		else
			url = String.format(NetUrlMyInfo.INVESTGROUPLIST, UserInfo.getInstance().getUserId());
		JsonRequest<InvestGroupList> request = new JsonRequest<InvestGroupList>(Method.GET, url,
				new RequestHandlerListener<InvestGroupList>(getContext()) {

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
					public void onSuccess(String id, InvestGroupList data) {
						// TODO Auto-generated method stub
						try{
							stopRefresh();
							if (data==null || data.getData() == null || data.getData().getList().size() == 0){
								showEmptyView();
								setEmptyText("暂无投资组合");
								return;
							}
							clear();
							showDataView();
							fillData(data.getData().getList());
							relation = data.getData().getRelation();
							reFresh();
						}
						catch(Exception e){
							Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
						}
						
					}
				}, InvestGroupList.class);
		send(request);
	}
	
	private void fillData(List<InvestGroupItem> data) {
		for(int i=0;i<data.size();i++){
			LinearLayout layout = new LinearLayout(this);
			layout.setBackgroundColor(getResources().getColor(R.color.background_f5f5f5));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,Function.getFitPx(this, 141));
			
			final InvestItem item = new InvestItem(this);
			item.setTag(data.get(i));
			item.groupId = data.get(i).getId();
			item.setTitle(data.get(i).getPname());
			if (data.get(i).getLimits() == 2){
				item.setStateText("(私密)");
			}
			item.setStateFontColor(getResources().getColor(R.color.font_b2b2b2));
			item.setInfoText(DateUtils.format(data.get(i).getCtime(), "yyyy-MM-dd"));
			item.setTitleFontSize(52);
			item.setInfoFontSize(36);
			item.setInfoFontColor(getResources().getColor(R.color.font_b2b2b2));
			
			item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					InvestItem investItem = (InvestItem)v;
					InvestGroupItem investGroupItem = (InvestGroupItem)investItem.getTag();
					if (!investGroupItem.getUserid().equals(UserInfo.getInstance().getUserId()) && isSecret(item)){
						//未登录
						if (!UserInfo.getInstance().isLogin()){		
							Intent intent = new Intent();
							intent.setClass(InvestGroupActivity.this, LoginActivity.class);
							startActivityForResult(intent, 0);
							return;
						}
						//非签约关系
						else if (relation != 5){
							DialogManage.showSingleButtonDialog(getContext(), "只有该投顾的签约用户才可查看私密组合详情哦", "知道了");
							return;
						}
					}
					Intent intent = new Intent();
					intent.setClass(InvestGroupActivity.this, InvestGroupDetailActivity.class);					
					Invest_CurrentPosition_Fragment.GROUPID = investItem.groupId;
//					intent.putExtra("id", investItem.groupId);
//					intent.putExtra("name", investItem.getTitle());
					startActivity(intent);
				}
			});
			params.setMargins(0, 0, 0, 1);
			layout.addView(item,params);
			addItem(layout);
		}
	}
	
	private boolean isSecret(InvestItem item){
		return ((InvestGroupItem)item.getTag()).getLimits() == 2;
	}
	@Override
	public void OnStartRefresh() {
		// TODO Auto-generated method stub
		super.OnStartRefresh();
		requestData(true);
	}
	private class InvestItem extends BarItem{

		private int groupId;
		public InvestItem(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		
	}

}
