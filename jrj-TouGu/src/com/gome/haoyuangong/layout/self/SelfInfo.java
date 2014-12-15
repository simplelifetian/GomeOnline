package com.gome.haoyuangong.layout.self;

import java.util.HashMap;
import java.util.Map;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.crop.CropHelper;
import com.gome.haoyuangong.layout.self.SelfView.BusinessType;
import com.gome.haoyuangong.layout.self.SelfView.IItemClicked;
import com.gome.haoyuangong.layout.self.SelfView.UserType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelfInfo extends LinearLayout {
	public static SelfView.UserType userType = UserType.utNone;
	public static interface IItemClicked{
		public void OnItemClicked(BusinessType businessType,BarItem item);
	}
	private IItemClicked itemClicked;
	private ItemWithInfo headBar;
	private Map<Integer,BarItem> itemsMap;
	private boolean isUsesr;
	public SelfInfo(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setOrientation(VERTICAL);
//		this.setBackgroundColor(context.getResources().getColor(R.color.divider));
		headBar = new ItemWithInfo(getContext());
		headBar.setHeadPicSize(200, 200);
//		headBar.setHeadPic(R.drawable.icon111);
		headBar.setName("李家智");
		headBar.setInfoText("广东  东莞");		
		itemsMap = new HashMap<Integer,BarItem>();
	}
	public void setItemClicked(IItemClicked iItemClicked){
		itemClicked = iItemClicked;
	}
	public BarItem getItem(BusinessType businessType){
		return itemsMap.get(businessType.ordinal());
	}
	private void doLayout(){
		itemsMap.clear();
		if (getIsUser() && userType != UserType.utUserViewAdviser)
			doUserLayout();
		else
			doInvesterLayout();
	}
	private void doInvesterLayout(){
		this.removeAllViews();
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		headBar.doLayout();
////		p.setMargins(0, 0, 0, 1);
//		addView(headBar,p);
		
		LinearLayout headLayout = new LinearLayout(getContext());
		headLayout.setBackgroundColor(Color.WHITE);;
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(getContext(), 248));
		p.setMargins(0, Function.getFitPx(getContext(), 40), 0, 1);
		addView(headLayout,p);
		
		BarItem item = createBarItem("头像","",BusinessType.btHeadPic);	
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		item.setRightArrowVisible(GONE);
		item.addRightImage(-1, 200, true);
		p.setMargins(0, Function.getFitPx(getContext(), 24), Function.getFitPx(getContext(), 40), Function.getFitPx(getContext(), 24));
		headLayout.addView(item,p);
		
		int h = Function.getFitPx(getContext(), 130);
		item = createBarItem("真实姓名","",BusinessType.btNikeName);	
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, h);
		p.setMargins(0, 0, 0, 1);
		addView(item,p);
		
		item = createBarItem("性别","",BusinessType.btSex);
		item.setRightArrowVisible(View.INVISIBLE);
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, h);
//		p.setMargins(0, 1, 0, 0);
		addView(item,p);
		
		item = createBarItem("所在地","",BusinessType.btAddress);		
		item.setDrawBottomLine(true);	
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, h);
		p.setMargins(0, Function.getFitPx(getContext(), 40), 0, 0);
//		p.setMargins(0, 10, 0, 0);
		addView(item,p);
		
		item = createBarItem("机构名称","",BusinessType.btOrgnization);
		item.setDrawBottomLine(true);	
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, h);
//		p.setMargins(0, 1, 0, 0);
		addView(item,p);
		
//		item = createBarItem("从属营业部","复兴门营业部",BusinessType.btDepartment);
//		item.setDrawBottomLine(true);	
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, h);
////		p.setMargins(0, 1, 0, 0);
//		addView(item,p);
		
		item = createBarItem("所在岗位","",BusinessType.btPosition);
		item.setDrawBottomLine(true);	
		item.setRightArrowVisible(View.INVISIBLE);
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, h);
//		p.setMargins(0, 1, 0, 0);
		addView(item,p);
		
		item = createBarItem("证书编号","",BusinessType.btCertification);
		item.setDrawBottomLine(true);	
		item.setRightArrowVisible(View.INVISIBLE);
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, h);
//		p.setMargins(0, 1, 0, 0);
		addView(item,p);
		
		item = createBarItem("从业年限","",BusinessType.btWorkLimit);
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, h);
		p.setMargins(0, 0, 0, 1);
//		p.setMargins(0, 1, 0, 0);
		addView(item,p);
		
		item = createBarItem("擅长领域","",BusinessType.btExpertArea);
		item.setDrawBottomLine(true);	
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, h);
		p.setMargins(0, Function.getFitPx(getContext(), 40), 0, 0);
		addView(item,p);
		
		item = createBarItem("能力标签","",BusinessType.btSkill);
		item.setTag(BusinessType.btSkill);
		item.setDrawBottomLine(true);	
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, h);
//		p.setMargins(0, 1, 0, 0);
		addView(item,p);
		
		item = createBarItem("简介","",BusinessType.btBrief);
		item.setRightArrowVisible(VISIBLE);
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, h);
//		p.setMargins(0, 1, 0, 0);
		addView(item,p);
	}
	private void doUserLayout(){
		this.removeAllViews();
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout headLayout = new LinearLayout(getContext());
		headLayout.setBackgroundColor(Color.WHITE);;
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(getContext(), 248));
		p.setMargins(0, Function.getFitPx(getContext(), 40), 0, 1);
		addView(headLayout,p);
		
		BarItem item = createBarItem("头像","",BusinessType.btHeadPic);	
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		item.setRightArrowVisible(GONE);
		item.addRightImage(0, 200, true);
		p.setMargins(0, Function.getFitPx(getContext(), 24), Function.getFitPx(getContext(), 40), Function.getFitPx(getContext(), 24));
		headLayout.addView(item,p);
		
		item = createBarItem("用户名","",BusinessType.btNikeName);
//		item.setDrawBottomLine(true);		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
		p.setMargins(0, Function.getFitPx(getContext(), 40), 0, 0);
		addView(item,p);
		
//		item = createBarItem("所在地","",BusinessType.btAddress);
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
//		addView(item,p);
		
		item = createBarItem("","",BusinessType.btApplyAdviser);
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
		item.setInfoText("申请认证投资顾问");		
		item.setDrawBottomLine(false);
		item.setInfoFontColor(getContext().getResources().getColor(R.color.font_4c87c6));
		item.setRightArrowVisible(GONE);
		item.setBackgroundColor(Color.TRANSPARENT);
		addView(item,p);
	}
	private BarItem createBarItem(String title,String info,BusinessType businessType){
		final BarItem item = new BarItem(getContext());	
		item.setTitleFontSize(46);
		item.setInfoFontSize(50);
		item.setTag(businessType);
		item.setRightArrowVisible(View.INVISIBLE);
		item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (itemClicked != null)
					itemClicked.OnItemClicked((BusinessType)item.getTag(), item);
			}
		});
		item.setTitle(title);
		if (info != null)
			item.setInfoText(info);
		itemsMap.put(businessType.ordinal(), item);
		return item;
	}
	private boolean getIsUser(){
		return !UserInfo.getInstance().isTougu();
	}
	public void setIsUser(boolean isuser){
		this.isUsesr = isuser;
		doLayout();
	}

	public static enum BusinessType{
		/**
		 * 无
		 */
		btNone,
		/**
		 * 性别
		 */
		btSex,
		/**
		 * 头像
		 */
		btHeadPic,
		/**
		 * 用户名
		 */
		btNikeName,
		/**
		 * 所在地
		 */
		btAddress,
		/**
		 * 机构名称
		 */
		btOrgnization,
		/**
		 * 所属营业部
		 */
		btDepartment,
		/**
		 * 所在岗位
		 */
		btPosition,
		/**
		 * 从业年限
		 */
		btWorkLimit,
		/**
		 * 证书编号
		 */
		btCertification,
		/**
		 * 擅长领域
		 */
		btExpertArea,
		/**
		 * 能力标签
		 */
		btSkill,
		/**
		 * 简介
		 */
		btBrief,
		/**
		 * 
		 */
		btTrade,
		/**
		 * 
		 */
		btViewInvester,
		/**
		 * 咨询记录
		 */
		btAskRecords,
		/**
		 * 申请投资顾问
		 */
		btApplyAdviser
	}
}
