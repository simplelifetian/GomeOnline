package com.gome.haoyuangong.layout.self;

import java.util.HashMap;
import java.util.Map;



import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.views.SwipeRefreshLayout;
//import com.jrj.stock.trade.OnRefreshListener;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

	public class SelfView extends LinearLayout {

	public static interface IItemClicked{
		public void OnItemClicked(BusinessType businessType);
	}
	public static String USERID;
	public static String USERNAME;
	private IItemClicked itemClicked;
	private String invester;
	UserType userType;
	SelfHead selfHead;
	SelfFoot selfFoot;
	SocialBar socialBar;
	ServiceBar serviceBar;
	RelativeLayout mainLayout;
	private TextView signValueText;
	FragmentActivity activity;
	public SelfView(Context context) {
		super(context);		
		if (UserInfo.getInstance().isLogin() && UserInfo.getInstance().isTougu())
			userType = UserType.utInvester;
		else
//		else if (UserInfo.getInstance().isLogin() && !UserInfo.getInstance().isTougu())
			userType = UserType.utUser;
//		userType = UserType.utInvester;
		// TODO Auto-generated constructor stub
		this.setOrientation(VERTICAL);
//		layout();
	}
	
	public void layout(){
		if (userType == UserType.utInvester || userType == UserType.utUserViewAdviser)
			doLayout();
		else
			doUserLayout();
	}
	public void setActivity(FragmentActivity activity) {
		this.activity = activity;
	}

	public SelfHead getSelfHead() {
		return selfHead;
	}

	public SelfFoot getSelfFoot() {
		return selfFoot;
	}

	public SocialBar getSocialBar() {
		return socialBar;
	}

	public ServiceBar getServiceBar() {
		return serviceBar;
	}

	public String getInvester() {
		return invester;
	}

	public RelativeLayout getMainLayout() {
		return mainLayout;
	}

	public void setInvester(String invester) {
		this.invester = invester;
		if (selfHead != null)
			selfHead.setInvesterName(invester);
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public void setItemClicked(IItemClicked iItemClicked){
		itemClicked = iItemClicked;
	}
	public void setFootVisible(boolean visible){
		if (selfFoot == null)
			return;
		if (visible)
			selfFoot.setVisibility(VISIBLE);
		else
			selfFoot.setVisibility(INVISIBLE);
	}
	private void doItemClicked(BusinessType businessType){
		if (itemClicked != null){
			itemClicked.OnItemClicked(businessType);
		}
	}
	private void init(){
		removeAllViews();
		selfHead = new SelfHead(getContext());
		selfFoot = new SelfFoot(getContext());
		socialBar = new SocialBar(getContext());
		serviceBar = new ServiceBar(getContext());
		mainLayout = new RelativeLayout(getContext());	
		signValueText = new TextView(getContext());
//		signValueText.setText("18人");
		signValueText.setGravity(Gravity.CENTER_VERTICAL|Gravity.TOP);
		signValueText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(getContext(), 40));
		signValueText.setTextColor(Color.WHITE);
		signValueText.setTag(0);
	}
	public void setSignNum(int num){
		if (userType == UserType.utUser)
			signValueText.setText("关注  "+String.valueOf(num));
		else
			signValueText.setText(String.valueOf(num)+"人");
		signValueText.setTag(num);
	}
	public void doUserLayout(){
		init();
		//主layout
		mainLayout = new RelativeLayout(getContext());	
		LinearLayout.LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);	
//		addView(mainLayout,p);
		
		LinearLayout infoLayout = new LinearLayout(getContext());
		infoLayout.setOrientation(VERTICAL);
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		mainLayout.addView(infoLayout,rp);
		
		RelativeLayout headLayout = new RelativeLayout(getContext());		
		p = new LayoutParams(LayoutParams.MATCH_PARENT,selfHead.getItemHeight()+Function.getFitPx(getContext(), 40));	
		infoLayout.addView(headLayout,p);
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,selfHead.getItemHeight());
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		headLayout.addView(selfHead,rp);
		RelativeLayout imageLayout = new RelativeLayout(getContext());
//		imageLayout.setBackgroundColor(Color.RED);
		rp = new RelativeLayout.LayoutParams(Function.getFitPx(getContext(), 228),Function.getFitPx(getContext(), 90));	
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rp.setMargins(0, Function.getFitPx(getContext(), 80), 0, 0);
		headLayout.addView(imageLayout,rp);
		ImageView img = new ImageView(getContext());
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doItemClicked(BusinessType.btViewUserAttenions);
			}
		});
		img.setScaleType(ScaleType.CENTER_INSIDE);
		img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.user_attention));
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		imageLayout.addView(img,rp);
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		rp.setMargins(Function.getFitPx(getContext(), 20), 0, 0, 0);
		imageLayout.addView(signValueText,rp);
		signValueText.setTextColor(getContext().getResources().getColor(R.color.font_4c87c6));
		signValueText.setGravity(Gravity.CENTER_VERTICAL);
		
		serviceBar = new ServiceBar(getContext());
		p = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);		
		p.setMargins(0, Function.getFitPx(getContext(), 40), 0, 0);
		serviceBar.setLayoutParams(p);
		infoLayout.addView(serviceBar,p);

		
		ScrollView scrollView = new ScrollView(getContext());
		scrollView.setBackgroundColor(0xfff5f5f5);
		p = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		scrollView.addView(mainLayout,p);

		SwipeRefreshLayout s = new SwipeRefreshLayout(getContext());
		s.setTag(UserInfo.getInstance().getUserId());
		s.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				doItemClicked(BusinessType.btRefresh);
			}
		});
		s.addView(scrollView);
		p = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,1);	
		addView(s,p);
	}
	public void doLayout(){
		init();
		//主layout
		mainLayout = new RelativeLayout(getContext());	
		LinearLayout.LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);	
//		addView(mainLayout,p);
		
		LinearLayout infoLayout = new LinearLayout(getContext());
		infoLayout.setId(1001);
		infoLayout.setOrientation(VERTICAL);
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		mainLayout.addView(infoLayout,rp);
		
		RelativeLayout headLayout = new RelativeLayout(getContext());		
		p = new LayoutParams(LayoutParams.MATCH_PARENT,selfHead.getItemHeight()+Function.getFitPx(getContext(), 40));	
		infoLayout.addView(headLayout,p);
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,selfHead.getItemHeight());
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		headLayout.addView(selfHead,rp);
		rp = new RelativeLayout.LayoutParams(Function.getFitPx(getContext(), 120),Function.getFitPx(getContext(), 157));
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rp.setMargins(0, Function.getFitPx(getContext(), 24), Function.getFitPx(getContext(), 110), 0);
		if (userType != UserType.utUserViewAdviser){
			RelativeLayout imageLayout = new RelativeLayout(getContext());
			imageLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					doItemClicked(BusinessType.btViewSign);
				}
			});
			headLayout.addView(imageLayout,rp);
			rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			ImageView image = new ImageView(getContext());
			image.setScaleType(ScaleType.CENTER_INSIDE);
			image.setImageDrawable(getContext().getResources().getDrawable(R.drawable.android_attention_back));
			imageLayout.addView(image,rp);
		
		//签约		
			LinearLayout asignLayout = new LinearLayout(getContext());
			asignLayout.setOrientation(VERTICAL);
			asignLayout.setGravity(Gravity.CENTER);
			rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			imageLayout.addView(asignLayout,p);
			TextView tv = new TextView(getContext());
			tv.setText("签约");
			tv.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(getContext(), 40));
			tv.setTextColor(Color.WHITE);
			p = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,1);	
			asignLayout.addView(tv,p);
			asignLayout.addView(signValueText,p);		
		}
		
		socialBar = new SocialBar(getContext());
		LinearLayout socialLayout = new LinearLayout(getContext());
		socialLayout.setBackgroundColor(Color.WHITE);
		p = new LayoutParams(LayoutParams.MATCH_PARENT,socialBar.getItemHeight());	
		infoLayout.addView(socialLayout,p);
		p = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);		
		p.setMargins(0, Function.getFitPx(getContext(), 15), 0, Function.getFitPx(getContext(), 15));
		socialBar.setLayoutParams(p);
		socialLayout.addView(socialBar,p);
		
		serviceBar = new ServiceBar(getContext());
		p = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);	
		p.setMargins(0, Function.getFitPx(getContext(), 40), 0, 0);
		serviceBar.setLayoutParams(p);
		infoLayout.addView(serviceBar,p);
		
//		if (userType == UserType.utUserViewAdviser && !UserInfo.getInstance().getUserId().equals(USERID)){
//			rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
//			rp.addRule(RelativeLayout.BELOW,infoLayout.getId());
//			rp.setMargins(0, Function.getFitPx(getContext(), 40), 0, 0);
//			mainLayout.addView(selfFoot,rp);
//		}
		
		ScrollView scrollView = new ScrollView(getContext());
		scrollView.setBackgroundColor(0xfff5f5f5);
		p = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		scrollView.addView(mainLayout,p);

		SwipeRefreshLayout s = new SwipeRefreshLayout(getContext());
		if (userType == UserType.utUserViewAdviser)
			s.setTag(USERID);
		else
			s.setTag(UserInfo.getInstance().getUserId());
		s.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				doItemClicked(BusinessType.btRefresh);
			}
		});
		s.addView(scrollView);
		p = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,1);	
		addView(s,p);
		
		if (userType == UserType.utUserViewAdviser && !UserInfo.getInstance().getUserId().equals(USERID)){
			p = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);	
			addView(selfFoot,p);
		}
	}
	
	private class UserItem extends PersonalInfoItem{
		TextView infoView;
		public UserItem(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			infoView = new TextView(getContext());
			infoView.setTextColor(context.getResources().getColor(R.color.font_595959));
			infoView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(getContext(), 44));	
			this.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getContext(), "关注", Toast.LENGTH_SHORT).show();
				}
			});
		}
		public void doLayout(){
			super.doLayout();
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1);	
			p.setMargins(Function.getFitPx(getContext(), 30), 0, 0, 1);
			_infoLayout.addView(infoView,p);		
		}
		protected void setInfoText(String info){
			infoView.setText(info);
		}
	}
	public class SelfHead extends RelativeLayout {
		private ImageView headPic;
		private TextView nameText;
		private ImageView headIcon;
		private ImageView levelPic;
		private TextView identityText;
		private TextView companyText;
		private TextView signValueText;
		private int headHeight;
		LinearLayout starLayout;
		public SelfHead(Context context) {
			super(context);
			this.setBackgroundColor(Color.WHITE);
			headHeight = Function.getFitPx(context, 300);
			initComponent(context);
			doLayout();
			// TODO Auto-generated constructor stub
		}
		
		private int getItemHeight(){
			return headHeight;
		}
		
		private void initComponent(Context context){
			starLayout = new LinearLayout(getContext());
			starLayout.setOrientation(HORIZONTAL);
			headPic = new ImageView(context);
			headPic.setScaleType(ScaleType.FIT_START);
			headPic.setBackgroundResource(R.drawable.icon_head_default);
			headIcon = new ImageView(context);
			headIcon.setScaleType(ScaleType.CENTER_INSIDE);
			headIcon.setBackgroundResource(R.drawable.icon_v);
			headIcon.setVisibility(INVISIBLE);
			nameText = new TextView(context);
			nameText.setText(invester);
			nameText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(getContext(), 44));
			nameText.setTextColor(context.getResources().getColor(R.color.font_4c86c6));			
			nameText.setGravity(Gravity.CENTER_VERTICAL);
			levelPic = new ImageView(context);
			levelPic.setScaleType(ScaleType.CENTER_INSIDE);
			identityText = new TextView(context);
			identityText.setMaxLines(1);
//			identityText.setText("投资顾问");
			identityText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(getContext(), 40));
			identityText.setTextColor(context.getResources().getColor(R.color.font_727272));
			identityText.setGravity(Gravity.CENTER_VERTICAL);
			companyText = new TextView(context);
			companyText.setPadding(5, 0, 0, 0);
//			companyText.setText("国信证券");
			companyText.setMaxLines(1);
			companyText.setGravity(Gravity.CENTER_VERTICAL);
			companyText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(getContext(), 40));
			companyText.setTextColor(context.getResources().getColor(R.color.font_595959));
			signValueText = new TextView(context);
//			signValueText.setText("18");
			signValueText.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
			signValueText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(getContext(), 40));
			signValueText.setTextColor(Color.RED);
		}
		
		private void doLayout(){
			this.removeAllViews();
			LinearLayout.LayoutParams lp = null;
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			p.setMargins(Function.getFitPx(getContext(), 40), 0, Function.getFitPx(getContext(), 40), 0);
			RelativeLayout pLayout = new RelativeLayout(getContext());
			pLayout.setBackgroundColor(getContext().getResources().getColor(R.color.list_divider_color));
			addView(pLayout,p);
			
			p = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			RelativeLayout layout = new RelativeLayout(getContext());
			layout.setBackgroundColor(Color.WHITE);
			if (userType != UserType.utUser)
				p.setMargins(0, 0, 0, 1);
			pLayout.addView(layout,p);

			p = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			p.addRule(RelativeLayout.LEFT_OF,divideLayout.getId());
			RelativeLayout leftLayout = new RelativeLayout(getContext());
			leftLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					doItemClicked(BusinessType.btViewMySelfInfo);
				}
			});
			layout.addView(leftLayout,p);
			
			//头像
			LinearLayout headLayout = new LinearLayout(getContext());
			headLayout.setGravity(Gravity.CENTER);
			headLayout.setId(1003);
			int picH = Function.getFitPx(getContext(), 220);
			p = new RelativeLayout.LayoutParams(picH,LayoutParams.MATCH_PARENT);
			p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			leftLayout.addView(headLayout,p);
			lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,picH);
			headLayout.addView(headPic,lp);
			//姓名
			p = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, headHeight/3);
			p.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			p.addRule(RelativeLayout.RIGHT_OF,headLayout.getId());
			p.setMargins(Function.getFitPx(getContext(), 20), 0, 0, 0);
			LinearLayout nameLayout = new LinearLayout(getContext());
			nameLayout.setGravity(Gravity.CENTER_VERTICAL);
			nameLayout.setId(1004);
			leftLayout.addView(nameLayout,p);
			lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
			lp.setMargins(0, 5, 0, 0);
			nameLayout.addView(nameText,lp);
			LinearLayout iconLayout = new LinearLayout(getContext());
			lp = new LinearLayout.LayoutParams(Function.getFitPx(getContext(), 40),Function.getFitPx(getContext(), 40));
			lp.setMargins(0, 5, 0, 0);
			nameLayout.addView(iconLayout,lp);
			lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			iconLayout.addView(headIcon,lp);
			//身份及公司
			p = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, headHeight/3);
//			p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			p.addRule(BELOW,nameLayout.getId());
			p.addRule(RelativeLayout.RIGHT_OF,headLayout.getId());
			p.setMargins(Function.getFitPx(getContext(), 20), Function.getFitPx(getContext(), 20), 0, 0);
			LinearLayout identityLayout = new LinearLayout(getContext());
			identityLayout.setOrientation(HORIZONTAL);
			leftLayout.addView(identityLayout,p);
			lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
			identityLayout.addView(identityText,lp);
			lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
			lp.setMargins(0, 0, 0, 0);
			identityLayout.addView(companyText,lp);
			//star
			p = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, headHeight/3);
			p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			p.addRule(RelativeLayout.RIGHT_OF,headLayout.getId());
			p.setMargins(Function.getFitPx(getContext(), 20), 0, 0, 0);
			leftLayout.addView(starLayout,p);
//			ImageView image = new ImageView(getContext());
//			image.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_star));
//			image.setScaleType(ScaleType.CENTER_INSIDE);
//			lp = new LinearLayout.LayoutParams(headHeight/3-5,headHeight/3-5);
//			starLayout.addView(image,lp);
		}
		public void setInvesterName(String name){
			nameText.setText(name);
		}
		public void setStar(int count){
			
		}
		public ImageView getHeadPicImageView(){
			return headPic;
		}
		public void setCompany(String company){
			companyText.setText(company);
		}
		public void setIdentity(String indentity) {
			identityText.setText(indentity);
		}
		public void showHeadIcon(boolean show){
			if (show){
				headIcon.setVisibility(VISIBLE);
			}
			else {
				headIcon.setVisibility(INVISIBLE);
			}
		}		
	}
	public class SelfFoot extends LinearLayout {
		private SelfFootItem signItem;
		private SelfFootItem attenItem;
		public SelfFoot(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.setOrientation(HORIZONTAL);
			signItem = new SelfFootItem(getContext());
			attenItem = new SelfFootItem(getContext());
			this.setBackgroundColor(context.getResources().getColor(R.color.divider));
			doLayout();
		}
		private void doLayout(){
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,Function.getFitPx(getContext(), 150),1);
			SelfFootItem item = new SelfFootItem(getContext());
			item.setTitle("咨询");
			item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					doItemClicked(BusinessType.btAsk);
				}
			});
			item.setImageID(R.drawable.icon_self_ask);
			p.setMargins(0, 1, 1, 0);
			addView(item,p);
			
			signItem.setTitle("签约");
			signItem.setImageID(R.drawable.icon_self_sign);
			signItem.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					doItemClicked(BusinessType.btSign);
					
//					final Dialog dialog = new Dialog(getContext());
//					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//					dialog.setContentView(R.layout.dialog_unregist);
//					Window dialogWindow = dialog.getWindow();
//			        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//			        lp.width = (int) (SelfView.this.getWidth()*0.9);
//			        dialogWindow.setAttributes(lp);		
//			        TextView tvLink = (TextView)dialog.findViewById(R.id.linktext);
//			        if (tvLink != null){
//			        	tvLink.setOnClickListener(new OnClickListener() {
//							
//							@Override
//							public void onClick(View v) {
//								// TODO Auto-generated method stub
//								getContext().startActivity(new Intent(activity,ProtocolActivity.class));
//							}
//						});
//			        }
//			        TextView tvOk = (TextView)dialog.findViewById(R.id.unregist_btnok);
//			        tvOk.setOnClickListener(new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							Toast.makeText(getContext(), "签约成功",Toast.LENGTH_SHORT).show();
//							dialog.dismiss();
//						}
//					});
//			        tvOk = (TextView)dialog.findViewById(R.id.unregist_btncancel);
//			        tvOk.setOnClickListener(new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							Toast.makeText(getContext(), "签约取消",Toast.LENGTH_SHORT).show();
//							dialog.dismiss();
//						}
//					});
//			        
//			        dialog.show();
				}
			});
			p.setMargins(0, 1, 1, 0);
			addView(signItem,p);
			
			attenItem = new SelfFootItem(getContext());
			attenItem.setTitle("关注");
			attenItem.setImageID(R.drawable.icon_self_attention);
			attenItem.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					doItemClicked(BusinessType.btAttention);
				}
			});
			addView(attenItem,p);
		}
		public void changeSignState(int flag) {
			if (flag > 0){
				signItem.setTitle("解除签约");
				changeAttenionState(flag);
			}
			else
				signItem.setTitle("签约");
		}
		public void changeAttenionState(int flag) {
			if (flag > 0){
				attenItem.setImageID(R.drawable.icon_self_unattention);
				attenItem.setTitle("已关注");
			}
			else{
				attenItem.setImageID(R.drawable.icon_self_attention);
				attenItem.setTitle("关注");
			}
		}
	}
	private class SelfFootItem extends LinearLayout {
		ImageView image;
		TextView titleText;
		public SelfFootItem(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.setOrientation(HORIZONTAL);
			this.setGravity(Gravity.CENTER);
			this.setBackgroundColor(Color.WHITE);
			image = new ImageView(context);
			image.setScaleType(ScaleType.CENTER_INSIDE);
			titleText = new TextView(context);
			titleText.setTextColor(context.getResources().getColor(R.color.font_4c86c6));
			titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(context, 50));
			titleText.setGravity(Gravity.CENTER_VERTICAL);
			doLayout();
		}
		private void doLayout(){
			int picH = Function.getFitPx(getContext(), 44);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(picH, picH);
			p.setMargins(0, 0, Function.getFitPx(getContext(), 20), 0);
			addView(image,p);
			p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
			addView(titleText,p);
		}
		public void setImageID(int resid){
			image.setImageDrawable(getContext().getResources().getDrawable(resid));
		}
		public void setTitle(String title){
			titleText.setText(title);
		}
	}
	public class SocialBar extends LinearLayout {
		private int barHeight;
		private Map<SocialType,SocialBarItem> items;
		public SocialBar(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			items = new HashMap<SelfView.SocialType, SelfView.SocialBarItem>();
			this.setOrientation(HORIZONTAL);
			this.setBackgroundColor(context.getResources().getColor(R.color.divider_2));
			barHeight = Function.getFitPx(context, 160);
			doLayout();
		}
		
		private void doLayout(){
			items.clear();
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,1);			
			
			SocialBarItem item = new SocialBarItem(getContext());
			item.setName("满意度");		
			item.setImageId(R.drawable.android_icon_love);
			p.setMargins(0, 0, 1, 0);
			addView(item,p);
			items.put(SocialType.stSatisfy, item);
			
			item = new SocialBarItem(getContext());
			item.setBackgroundResource(R.drawable.selector_item_btn);
			item.setName("粉丝");
			item.setImageId(R.drawable.android_icon_people);
			item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					doItemClicked(BusinessType.btViewFans);
				}
			});
			p.setMargins(0, 0, 1, 0);
			addView(item,p);
			items.put(SocialType.stFans, item);
			
			item = new SocialBarItem(getContext());
			item.setBackgroundResource(R.drawable.selector_item_btn);
			item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					doItemClicked(BusinessType.btViewAttenions);
				}
			});
			if (userType == UserType.utInvester){
				item.setName("关注");
//				item.setValue("233");
				item.setImageId(R.drawable.android_anttention);
				p.setMargins(0, 0, 1, 0);
				addView(item,p);
				items.put(SocialType.stAttention, item);
			}
			item = new SocialBarItem(getContext());
			item.setName("赞");
//			item.setValue("2333");
			item.setImageId(R.drawable.icon_zan);
			addView(item,p);
			items.put(SocialType.stAgree, item);
		}
		
		public int getItemHeight(){
			return barHeight;
		}
		public SocialBarItem getItem(SocialType socialType){
			return items.get(socialType);
		}
		
	}
	public class SocialBarItem extends LinearLayout {
		private TextView nameText;
		private TextView valueText;
		private ImageView image;
		public SocialBarItem(Context context) {
			super(context);			
			// TODO Auto-generated constructor stub
			this.setOrientation(VERTICAL);
			this.setBackgroundColor(Color.WHITE);
			nameText = new TextView(context);
			nameText.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
			nameText.setTextColor(getContext().getResources().getColor(R.color.font_727272));
			nameText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(getContext(), 40));
			valueText = new TextView(context);
			valueText.setGravity(Gravity.CENTER_HORIZONTAL);
			valueText.setTextColor(getContext().getResources().getColor(R.color.font_fcaf33));
			valueText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(getContext(), 44));
			valueText.setTag(0);
			
			image = new ImageView(context);
			image.setScaleType(ScaleType.FIT_END);
			doLayout();
		}
		
		private void doLayout(){
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,1);
			LinearLayout imageLayout = new LinearLayout(getContext());
			imageLayout.setOrientation(HORIZONTAL);
			imageLayout.setGravity(Gravity.CENTER);
			addView(imageLayout,p);			
			addView(valueText,p);
			int picH = Function.getFitPx(getContext(), 40);
			p = new LinearLayout.LayoutParams(picH,picH);
			p.setMargins(0,0, 10, 0);
			imageLayout.addView(image,p);
			p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);			
			imageLayout.addView(nameText,p);
		}
		
		public void setName(String name){
			nameText.setText(name);
		}
		public void setValue(String value){
			valueText.setText(value);
		}
		public String getValue(){
			return valueText.getText().toString();
		}
		public void setImageId(int resid){
			image.setImageDrawable(getContext().getResources().getDrawable(resid));
		}
	}

	public class ServiceBar extends LinearLayout {
		private Map<BusinessType,BarItem> items;
		public ServiceBar(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			items = new HashMap<SelfView.BusinessType, BarItem>();
			this.setOrientation(VERTICAL);			
			doLayout();
		}
		private void doLayout(){
			items.clear();
			BarItem item = new BarItem(getContext());
			LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
			
			if (userType == UserType.utUserViewAdviser){
				item.setTitle("擅长");
//				item.setInfoText("A股 美股 短线操作");
				item.setDrawBottomLine(true);
				item.setRightArrowVisible(View.INVISIBLE);
				addView(item,p);
				items.put(BusinessType.btExpert, item);
				
				item = new BarItem(getContext());
				p = new LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());			
				item.setTitle("从业年限");
//				item.setInfoText("4年");
				item.setDrawBottomLine(true);
				item.setRightArrowVisible(View.INVISIBLE);
				addView(item,p);
				items.put(BusinessType.btWorkLimit, item);
				
				item = new BarItem(getContext());
				p = new LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());			
				item.setTitle("资格证号");
//				item.setInfoText("34534343434343");
				item.setDrawBottomLine(true);
				item.setRightArrowVisible(View.INVISIBLE);
				addView(item,p);
				items.put(BusinessType.btCertification, item);
				
				item = new BarItem(getContext());
				p = new LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());			
				item.setTitle("简介");
//				item.setInfoText("在参考学习了牛人");
//				item.setRightArrowVisible(View.INVISIBLE);
				p.setMargins(0, 0, 0, Function.getFitPx(getContext(), 40));
				item.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						doItemClicked(BusinessType.btBrief);
					}
				});
				addView(item,p);
				items.put(BusinessType.btBrief, item);
			}
			
			if (userType == UserType.utInvester || userType == UserType.utUserViewAdviser){
				p = new LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());		
				item = new BarItem(getContext());
				item.setTitle("投资组合");
				item.setDrawBottomLine(true);
				item.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						doItemClicked(BusinessType.btInvestGroup);
					}
				});
				addView(item,p);
				items.put(BusinessType.btInvestGroup, item);
			}
			
			if (userType == UserType.utInvester || userType == UserType.utUser){
				item = new BarItem(getContext());
				p = new LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());			
				item.setTitle("开户与交易");
				item.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						doItemClicked(BusinessType.btTrade);
					}
				});
				if (userType == UserType.utUser)
					item.setDrawBottomLine(true);
				addView(item,p);
				items.put(BusinessType.btTrade, item);
			}
			if (userType == UserType.utUser){
				item = new BarItem(getContext());
				p = new LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());			
				item.setTitle("我的投顾");
				p.setMargins(0, 0, 0, Function.getFitPx(getContext(), 40));
				item.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						doItemClicked(BusinessType.btMyAdviser);
					}
				});
				addView(item,p);
				items.put(BusinessType.btMyAdviser, item);
			}
			else{
				item = new BarItem(getContext());
				p = new LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
				item.setTitle("投资观点");
	//			item.setInfoText("28");
				item.setDrawBottomLine(true);
				item.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						doItemClicked(BusinessType.btInvestOpinion);
					}
				});
				if (userType == UserType.utInvester)
					p.setMargins(0, Function.getFitPx(getContext(), 40), 0, 0);
					
				addView(item,p);
				items.put(BusinessType.btInvestOpinion, item);
			}			
			if (userType == UserType.utInvester || userType == UserType.utUserViewAdviser){
				item = new BarItem(getContext());
				p = new LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
				item.setTitle("直播");
//				item.setDrawBottomLine(true);
				item.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						doItemClicked(BusinessType.btLive);
					}
				});
				addView(item,p);
				items.put(BusinessType.btLive, item);
			}
			
			item = new BarItem(getContext());
			p = new LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
			if (userType == UserType.utInvester || userType == UserType.utUserViewAdviser){
				item.setTitle("回答记录");
				p.setMargins(0, Function.getFitPx(getContext(), 40), 0, Function.getFitPx(getContext(), 40));
			}
			else
				item.setTitle("咨询记录");
//			item.setInfoText("45");
//			item.setDrawBottomLine(true);
			item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					doItemClicked(BusinessType.btViewConsult);
				}
			});
			
			addView(item,p);
			items.put(BusinessType.btViewConsult, item);
			
			if (userType == UserType.utInvester){			
			
				item = new BarItem(getContext());
				item.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						doItemClicked(BusinessType.btGroupMessage);
					}
				});
//				p = new LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
//				item.setTitle("群发记录");
//				addView(item,p);
//				items.put(BusinessType.btGroupMessage, item);
			}
		}
		public BarItem getItem(BusinessType businessType){
			return items.get(businessType);
		}
	}

	public static enum BusinessType{
		btNone,
		/**
		 * 个人信息
		 */
		btViewMySelfInfo,
		/**
		 * 查看签约用户
		 */
		btViewSign,
		btSign,
		/**
		 * 查看我的粉丝
		 */
		btViewFans,
		/**
		 * 查看关注我的人
		 */
		btViewAttenions,
		/**
		 * 用户的关注
		 */
		btViewUserAttenions,
		/**
		 * 关注
		 */
		btAttention,
		/**
		 * 投资组合
		 */
		btInvestGroup,
		/**
		 * 投资观点
		 */
		btInvestOpinion,
		/**
		 * 咨询记录
		 */
		btViewConsult,
		/**
		 * 群发记录
		 */
		btGroupMessage,
		btExpert,
		btWorkLimit,
		btCertification,
		btBrief,
		btTrade,
		btLive,
		btMyAdviser,
		btAsk,
		btRefresh
	}
	public static enum UserType{
		utNone,utUserViewAdviser,utViewUser,utInvester,utUser
	}
	public static enum SocialType{
		stSign,stFans,stAttention,stSatisfy,stAgree
	}
}
