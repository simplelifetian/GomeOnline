package com.gome.haoyuangong.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.gome.haoyuangong.layout.self.BarItem;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.layout.self.ItemWithInfo;
import com.gome.haoyuangong.layout.self.PersonalInfoItem;
import com.gome.haoyuangong.layout.self.data.Answered_Adviser;
import com.gome.haoyuangong.layout.self.data.UserInfo;
import com.gome.haoyuangong.layout.self.data.UserInfo.Item;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.url.NetUrlMyInfo;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;

public class ViewSignUserInfoActivity extends BaseActivity {
	Intent intent;
	int EDITBZTYPEID = 1;
	String id;
	SignUserView child;
	ImageLoader imageLoader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imageLoader = new ImageLoader(this);
		intent = getIntent();
		id = intent.getStringExtra("id");
		content.removeAllViews();
		child = new SignUserView(this);
		content.addView(child);
		setTitle(intent.getStringExtra("title"));
		child.getInfoItem().setName(intent.getStringExtra("title"));
//		child.getInfoItem().setInfoText(intent.getStringExtra("city"));
		imageLoader.downLoadImage(intent.getStringExtra("headImage"),child.getInfoItem().getHeadPic());
		requestData();
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	    super.onActivityResult(requestCode, resultCode, data);  
	      if (data == null)
	    	  return;
	    if (requestCode == EDITBZTYPEID){
	    	child.setBzText(data.getStringExtra("data"));;
	    }
	}  
	
	private void requestData() {
		String url = String.format(NetUrlMyInfo.USERINFOURL, id);
		JsonRequest<UserInfo> request = new JsonRequest<UserInfo>(Method.GET, url,
				new RequestHandlerListener<UserInfo>(getContext()) {

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
					public void onSuccess(String id, UserInfo data) {
						// TODO Auto-generated method stub
						fillData(data.getData());
					}
				}, UserInfo.class);
			send(request);
	}
	private void fillData(Item data) {
//		if (TextUtils.isEmpty(data.getUserName()))
//			child.getInfoItem().setName(intent.getStringExtra("title"));
//		else
//			child.getInfoItem().setName(data.getUserName());
		child.getInfoItem().setInfoText(data.getProvince()+" "+data.getCity());
//		imageLoader.downLoadImage(data.getHeadImage(), child.getInfoItem().getHeadPic());
	}
	
	
	private class SignUserView extends LinearLayout{
		BarItem bzItem;
		ItemWithInfo infoItem;
		public SignUserView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.setOrientation(VERTICAL);		
			infoItem = new ItemWithInfo(getContext());
			infoItem.setHeadPicSize(220, 220);
//			infoItem.setHeadPic(R.drawable.icon111);
			bzItem = new BarItem(getContext());
			bzItem.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putExtra("bz", bzItem.getStateText());
					intent.putExtra("typeid", EDITBZTYPEID);
					intent.setClass(ViewSignUserInfoActivity.this, EditInfoActivity.class);
//					startActivity(intent);
					startActivityForResult(intent,EDITBZTYPEID);
				}
			});
			doLayout();
		}
		
		private void doLayout(){
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//			infoItem.setName(intent.getStringExtra("title"));
//			infoItem.setInfoText(intent.getStringExtra("city"));
			infoItem.doLayout();
			p.setMargins(0, Function.getFitPx(getContext(), 40), 0, 1);
			addView(infoItem,p);
			
				
//			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, bzItem.getItemHeight());
//			bzItem.setTitle("备注");
//			bzItem.setTitleFontColor(getContext().getResources().getColor(R.color.font_727272));
//			bzItem.setStateText("  中信证券  资产20万");
//			bzItem.setStateFontColor(getContext().getResources().getColor(R.color.font_595959));
//			p.setMargins(0, Function.getFitPx(getContext(), 40), 0, 1);
//			addView(bzItem,p);
		}
		protected void setBzText(String bz){
			bzItem.setStateText(bz);
		}
		protected ItemWithInfo getInfoItem(){
			return infoItem;
		}
	}

}
