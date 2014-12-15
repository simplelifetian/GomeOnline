package com.gome.haoyuangong.layout.self;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.SetupData;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.activity.MainActivity;
import com.gome.haoyuangong.layout.self.SelfInfo.BusinessType;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.XinGeBaseResult;
import com.gome.haoyuangong.net.url.XinGeURL;
import com.gome.haoyuangong.net.volley.JsonRequest;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SetupView extends LinearLayout {
	public static interface IItemClicked{
		public void OnItemClicked(BusinessType businessType,BarItem item);
	}	
	private Map<Integer,BarItem> itemsMap;
	private IItemClicked itemClicked;
	public SetupView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setBackgroundColor(context.getResources().getColor(R.color.background_f5f5f5));
		itemsMap = new HashMap<Integer,BarItem>();
		this.setOrientation(VERTICAL);
		doLayout();
	}
	public void setItemClicked(IItemClicked iItemClicked){
		itemClicked = iItemClicked;
	}
	
	private void doItemClicked(BusinessType businessType,BarItem item){
		if (itemClicked != null){
			itemClicked.OnItemClicked(businessType,item);
		}
	}	

	
	private void doLayout(){
		this.removeAllViews();
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
//		BarItem item = createBarItem("绑定其他平台","",BusinessType.btThirdPlatform);				
//		item.addRightImage(R.drawable.icon_share_sina,60,false);
//		item.addRightImage(R.drawable.icon_share_qq,60,false);		
//		item.setDrawBottomLine(true);	
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
//		addView(item,p);
//		
//		item = createBarItem("手机号","3232323232",BusinessType.btPhone);
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
//		addView(item,p);
		
//		item = createBarItem("字号","中",BusinessType.btFontSize);
//		item.setDrawBottomLine(true);	
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
//		p.setMargins(0, Function.getFitPx(getContext(), 40), 0, 0);
//		addView(item,p);
		
		BarItem item = createBarItem("推送设置",null,BusinessType.btPullMessage);
		item.setDrawBottomLine(true);	
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
		p.setMargins(0, 0, 0, 0);
		addView(item,p);
		
		item = createBarItem("行情刷新频率","5秒",BusinessType.btFreshRate);
//		item.setDrawBottomLine(true);	
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
//		p.setMargins(0, 1, 0, 0);
		addView(item,p);
		
//		item = createBarItem("仅wifi网络下下载图片",null,BusinessType.btWifi);
////		item.addRightImage(R.drawable.icon_check_checked,100,false);
////		item.setDrawBottomLine(true);	
//		item.setRightArrowVisible(View.INVISIBLE);
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
//		p.setMargins(0, Function.getFitPx(getContext(), 40), 0, 0);
//		addView(item,p);
		
//		item = createBarItem("清理缓存","54.6k",BusinessType.btClearCache);
//		item.setDrawBottomLine(true);	
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
////		p.setMargins(0, 1, 0, 0);
//		addView(item,p);
		
		item = createBarItem("用户反馈","",BusinessType.btFeedBack);
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
		p.setMargins(0, Function.getFitPx(getContext(), 40), 0, 0);
		item.setDrawBottomLine(true);	
//		p.setMargins(0, 1, 0, 0);
		addView(item,p);
		
		item = createBarItem("版本更新","",BusinessType.btVersion);
		item.setDrawBottomLine(true);	
		item.setRightArrowVisible(View.INVISIBLE);
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
		addView(item,p);
		
		item = createBarItem("关于","",BusinessType.btAbout);
//		item.setDrawBottomLine(true);	
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
//		p.setMargins(0, 1, 0, 0);
		addView(item,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
		LinearLayout exitLayout = new LinearLayout(getContext());		
		exitLayout.setBackgroundColor(getContext().getResources().getColor(R.color.list_divider_color));
		p.setMargins(0, Function.getFitPx(getContext(), 40), 0, 0);
		addView(exitLayout,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		p.setMargins(1, 1, 1, 1);
		TextView tv = new TextView(getContext());
		tv.setBackgroundColor(Color.WHITE);
		tv.setTextColor(getContext().getResources().getColor(R.color.font_dd3030));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(getContext(), 50));
		tv.setGravity(Gravity.CENTER);	
		tv.setText("退出");
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doItemClicked(BusinessType.btLogout,null);	
			}
		});
		exitLayout.addView(tv,p);

	}
	private BarItem createBarItem(String title,String info,BusinessType businessType){
		final BarItem item;		
		if (businessType == BusinessType.btWifi){
			item = new TurnOffItem(getContext());			
		}
		else
			item = new BarItem(getContext());
		item.setTitleFontSize(50);
		item.setTag(businessType);
		item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doItemClicked((BusinessType)item.getTag(),item);				
			}
		});
		item.setTitle(title);
		if (info != null)
			item.setInfoText(info);
		itemsMap.put(businessType.ordinal(), item);
		return item;
	}
	public BarItem getItem(BusinessType businessType){
		return itemsMap.get(businessType.ordinal());
	}

	public static enum BusinessType{
		/**
		 * 第三方平台
		 */
		btThirdPlatform,
		/**
		 * 手机
		 */
		btPhone,
		/**
		 * 字号
		 */
		btFontSize,
		/**
		 * 推送设置
		 */
		btPullMessage,
		/**
		 * 刷新频率
		 */
		btFreshRate,
		/**
		 * wifi下载
		 */
		btWifi,
		/**
		 * 咨询记录
		 */
		btClearCache,
		/**
		 * 用户反馈
		 */
		btFeedBack,
		/**
		 * 版本更新
		 */
		btVersion,
		/**
		 * 关于
		 */
		btAbout,
		btLogout
	}
}
