package com.gome.haoyuangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.layout.self.ActivityChange;
import com.gome.haoyuangong.layout.self.ItemWithInfo;
import com.gome.haoyuangong.layout.self.SelfView.UserType;
import com.gome.haoyuangong.layout.self.data.FansList;
import com.gome.haoyuangong.layout.self.data.AttentionList.AttentionItem;
import com.gome.haoyuangong.layout.self.data.FansList.Data;
import com.gome.haoyuangong.layout.self.data.FansList.FansItem;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.url.NetUrlMyInfo;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.StringUtils;

public class MyFansActivity extends ListViewActivity {
	ImageLoader imageLoader ;
	String pageID="0";
	String direction="f";
	int requestCount=20;
	String firstRecordId = "-1";
	boolean showloading = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		imageLoader = new ImageLoader(this);		
		setTitle(getIntent().getStringExtra("name")+"的粉丝");
		setDividerHeight(0);
	}
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		requestData(false);
	}
	protected void requestData(final boolean pull) {
		String url = "";
		if (getIntent().getIntExtra("usertype", -1) == UserType.utUserViewAdviser.ordinal())
			url = String.format(NetUrlMyInfo.FUNSLISTURL, getIntent().getStringExtra("viewid"),pageID,direction,requestCount);
		else{
			if (direction.equals("f"))	
				url = String.format(NetUrlMyInfo.FUNSLISTURL, UserInfo.getInstance().getUserId(),pageID,direction,requestCount);
			else
				url = String.format(NetUrlMyInfo.FUNSLISTURL, UserInfo.getInstance().getUserId(),firstRecordId,direction,requestCount);
		}
		JsonRequest<FansList> request = new JsonRequest<FansList>(Method.GET, url,
				new RequestHandlerListener<FansList>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						if(showloading)
							showLoading(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						if (showloading)
							hideLoading(request);
						stopRefresh();
						stopLoadMore();
					}

					@Override
					public void onSuccess(String id, FansList data) {
						// TODO Auto-generated method stub
						try{
							if (pull)
								stopRefresh();
							stopLoadMore();
							if (data.getData().getList()==null || data.getData().getList().size() == 0){							
								setPullLoadEnable(false);
								if (pageID.equals("0")){
									showEmptyView();
									if (getIntent().getIntExtra("usertype", -1) == UserType.utUserViewAdviser.ordinal())
										setEmptyText("暂无用户关注该投顾");
									else
										setEmptyText("暂无用户关注您");
								}
								return;
							}
							else if (data.getData().getList().size() < requestCount){
								setPullLoadEnable(false);
							}
							else {
								setPullLoadEnable(true);
							}
							showDataView();
							if (pageID.equals("0"))
								clear();
							fillData(data.getData());	
							String key = data.getData().getList().get(0).getPageId();
							if (firstRecordId.compareTo(key) < 0)
								firstRecordId = data.getData().getList().get(0).getPageId();
							pageID = data.getData().getList().get(data.getData().getList().size()-1).getPageId();
							reFresh();
						}
						catch(Exception e){
							
						}
						
					}
				}, FansList.class);
			send(request);
	}
	
	private void fillData(Data data) {
		for(int i=0;i<data.getList().size();i++){
			final ItemWithInfo item = new ItemWithInfo(this);	
			FansItem fansItem = data.getList().get(i);
			item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();					
					if (((FansItem)item.getTag()).getIsAdviser() == 1){
						ActivityChange.ToAdviserHome(MyFansActivity.this, ((FansItem)item.getTag()).getUserName(), ((FansItem)item.getTag()).getUserId());
					}
					else{
						ActivityChange.ToUserHome(MyFansActivity.this, ((FansItem)item.getTag()).getUserName(), ((FansItem)item.getTag()).getUserId(), ((FansItem)item.getTag()).getHeadImage());
					}
				}
			});
			
			item.setName(fansItem.getUserName());
			if (fansItem.getIsAdviser() == 1){
				item.setInfoText(fansItem.getTypeDesc() +" "+(StringUtils.isEmpty(fansItem.getCompany())?"":fansItem.getCompany()));
				if (data.getList().get(i).getSignV() == 1)
					item.setHeadIcon(R.drawable.icon_v);
			}
			else if (fansItem.getRelationStatus() == 5)
				item.setAttachInfoText("     签约用户");
			
			item.setHeadPicSize(120, 120);
			imageLoader.downLoadImage(fansItem.getHeadImage(), item.getHeadPic());
			item.doLayout();
			item.setTag(fansItem);
//			addItem(item);
			if (direction.equals("f"))
				addItem(item);
			else
				addItem(item,i);
		}
	}
	
	@Override
	public void OnStartLoadMore() {
		// TODO Auto-generated method stub
		super.OnStartLoadMore();
		showloading = false;
		direction = "f";
		requestCount = 20;
		requestData(false);
	}
	@Override
	public void OnStartRefresh() {
		// TODO Auto-generated method stub
		super.OnStartRefresh();
		direction = "f";
		pageID="0";
		requestCount = 20;
		showloading = false;
		requestData(true);
	}
}
