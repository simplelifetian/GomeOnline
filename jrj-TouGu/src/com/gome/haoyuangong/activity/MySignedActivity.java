package com.gome.haoyuangong.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.layout.self.ActivityChange;
import com.gome.haoyuangong.layout.self.BarItem;
import com.gome.haoyuangong.layout.self.GroupMemberBean;
import com.gome.haoyuangong.layout.self.PersonalInfoItem;
import com.gome.haoyuangong.layout.self.SelfView.UserType;
import com.gome.haoyuangong.layout.self.data.InvestAdviserInfo;
import com.gome.haoyuangong.layout.self.data.SignedList;
import com.gome.haoyuangong.layout.self.data.SignedList.Data;
import com.gome.haoyuangong.layout.self.data.SignedList.SignedItem;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.url.NetUrlMyInfo;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;

public class MySignedActivity extends ListViewActivity {
	ImageLoader imageLoader;
	List<String> items;
	String pageID = "0";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setDividerHeight(0);
		imageLoader = new ImageLoader(this);
		items = new ArrayList<String>();
		requestData(false);		
		setTitle("签约用户");
		setPullLoadEnable(false);
	}

	protected void requestData(final boolean pull) {
		String url = "";
		if (getIntent().getIntExtra("usertype", -1) == UserType.utUserViewAdviser.ordinal())
			url = String.format(NetUrlMyInfo.SIGNERLISTURL, getIntent().getStringExtra("viewid"),pageID,"",10000000);
		else
			url = String.format(NetUrlMyInfo.SIGNERLISTURL, UserInfo.getInstance().getUserId(),pageID,"",10000000);
		JsonRequest<SignedList> request = new JsonRequest<SignedList>(Method.GET, url,
				new RequestHandlerListener<SignedList>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						// showDialog(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						// hideDialog(request);
					}

					@Override
					public void onSuccess(String id, SignedList data) {
						// TODO Auto-generated method stub
						try{
							stopLoadMore();
							if (pull)
								stopRefresh();
							if (data.getData().getList()==null || data.getData().getList().size() == 0){
								showEmptyView();
								setEmptyText("暂无签约用户");
							}
							clear();
							fillData(data.getData());
//							setPinYinDatas(items);
							reFresh();
						}
						catch(Exception e){
							
						}
						
					}
				}, SignedList.class);
			send(request);
	}
	
	private void fillData(Data data) {
//		items.clear();
		for(int i=0;i<data.getList().size();i++){
			final SignedItem signedItem = data.getList().get(i);
			final PersonalInfoItem item = new PersonalInfoItem(this);	
			item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (signedItem.getIsAdviser() == 0){
						ActivityChange.ToUserHome(getContext(), signedItem.getUserName(), signedItem.getUserId(), "");
					}
					else {
						ActivityChange.ToAdviserHome(getContext(), signedItem.getUserName(), signedItem.getUserId());
					}
//					Intent intent = new Intent();
//					intent.putExtra("title", item.getName());
//					intent.putExtra("id", item.getKey());
//					intent.setClass(MySignedActivity.this,ViewSignUserInfoActivity.class);
//					startActivity(intent);
				}
			});
			item.setNameFontColor(this.getResources().getColor(R.color.font_727272));
			item.setHeadPicSize(120, 120);
			item.setName(data.getList().get(i).getUserName());
			item.setKey(data.getList().get(i).getUserId());
			imageLoader.downLoadImage(data.getList().get(i).getHeadImage(), item.getHeadPic());
			item.doLayout();
			addItem(item);
//			items.add(data.getList().get(i).getUserName());
		}
	}
	@Override
	public void OnStartRefresh() {
		// TODO Auto-generated method stub
		super.OnStartRefresh();
		requestData(true);
	}
}
