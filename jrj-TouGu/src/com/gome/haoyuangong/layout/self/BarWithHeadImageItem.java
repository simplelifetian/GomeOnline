package com.gome.haoyuangong.layout.self;


import com.gome.haoyuangong.R;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;

public class BarWithHeadImageItem extends LinearLayout {
	ImageView headImage;
	TextView nameText;
	TextView identityText;
	TextView companyText;
	TextView signedText;
	int itemHeight;
	public BarWithHeadImageItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initComponent(context);
		itemHeight = Function.getFitPx(context, 160);
		doLayout();
	}
	private void initComponent(Context context){
		headImage = new ImageView(context);
		headImage.setScaleType(ScaleType.CENTER_INSIDE);
		nameText = new TextView(context);
		nameText.setTextColor(context.getResources().getColor(R.color.font_4c86c6));
		nameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(context, 30));
		nameText.setGravity(Gravity.CENTER_VERTICAL);
		identityText = new TextView(context);
		identityText.setText("投资顾问");
		identityText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(getContext(), 24));
		identityText.setTextColor(context.getResources().getColor(R.color.font_727272));
		identityText.setGravity(Gravity.CENTER_VERTICAL);
		companyText = new TextView(context);
		companyText.setTextColor(context.getResources().getColor(R.color.font_454545));
		companyText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(context, 24));
		companyText.setGravity(Gravity.CENTER_VERTICAL);
		signedText = new TextView(context);
		signedText.setTextColor(Color.RED);
		signedText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(context, 24));
		signedText.setGravity(Gravity.CENTER_VERTICAL);
	}
	
	private void doLayout(){
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, itemHeight);
		RelativeLayout mainLayout = new RelativeLayout(getContext());			
		addView(mainLayout,rp);
		LinearLayout headLayout = new LinearLayout(getContext());
		headLayout.setId(1001);
		int picH = Function.getFitPx(getContext(), 144);
		rp = new RelativeLayout.LayoutParams(picH, LayoutParams.MATCH_PARENT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		mainLayout.addView(headLayout,rp);
//		//头像
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(picH, picH);
		headLayout.addView(headImage,lp);
		
		LinearLayout infoLayout = new LinearLayout(getContext());
		infoLayout.setOrientation(LinearLayout.VERTICAL);
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rp.addRule(RelativeLayout.RIGHT_OF,headLayout.getId());
		mainLayout.addView(infoLayout,rp);
		LinearLayout topLayout = new LinearLayout(getContext());
		topLayout.setOrientation(LinearLayout.HORIZONTAL);
		lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
		infoLayout.addView(topLayout,lp);
		LinearLayout bottomLayout = new LinearLayout(getContext());
		bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
		lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
		infoLayout.addView(bottomLayout,lp);
		//名字,签约
		lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
		topLayout.addView(nameText,lp);
		topLayout.addView(signedText,lp);
		//身份，公司
		lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		bottomLayout.addView(identityText,lp);
		companyText.setPadding(Function.getFitPx(getContext(), 40), 0, 0, 0);
		bottomLayout.addView(companyText,lp);
	}
	public void setSigned(boolean signed){
		if (signed){
			signedText.setVisibility(VISIBLE);
			signedText.setText("签约用户");
		}
		else
			signedText.setVisibility(INVISIBLE);
	}
	public void setName(String name){
		nameText.setText(name);
	}
	public void setIdentity(String value){
		identityText.setText(value);
	}
	public void setCompany(String value){
		companyText.setText(value);
	}
	public void setHeadImage(int resid){
		headImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_launcher));
	}
	public TextView getNameTextView(){
		return nameText;
	}
	public TextView getIdentityTextView(){
		return identityText;
	}
	public TextView getCompanyTextView(){
		return companyText;
	}
	public TextView getSignedTextView(){
		return signedText;
	}
	public ImageView getHeadImageView(){
		return headImage;
	}

}
