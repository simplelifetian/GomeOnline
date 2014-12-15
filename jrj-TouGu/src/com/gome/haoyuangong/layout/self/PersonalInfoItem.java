package com.gome.haoyuangong.layout.self;


import com.gome.haoyuangong.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PersonalInfoItem extends LinearLayout {

	protected LinearLayout _mainLayout;
	private LinearLayout _headPicLayout;
	protected LinearLayout _infoLayout;
	LinearLayout _nameLayout;
	private String key;
	private Object tag;
	private ImageView _headPic;
	private ImageView _headIcon;
	private ImageView _headAttachImage;
	private TextView _nameTextView;
	private int _headPicWidth,_headPicHeight;
	public PersonalInfoItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initUI(context);
//		doLayout();
	}
	private void initUI(Context context){		
		_mainLayout = new LinearLayout(context);
		_mainLayout.setBackgroundColor(Color.WHITE);
		_mainLayout.setOrientation(HORIZONTAL);
		
		_headPicLayout = new LinearLayout(context);
		
		_infoLayout = new LinearLayout(context);
		_infoLayout.setGravity(Gravity.CENTER_VERTICAL);
		_infoLayout.setOrientation(VERTICAL);
		
		_nameLayout = new LinearLayout(getContext());
		
		_headIcon = new ImageView(context);
		_headIcon.setScaleType(ScaleType.CENTER_INSIDE);
		
		_headAttachImage = new ImageView(context);
		_headAttachImage.setScaleType(ScaleType.CENTER_INSIDE);
		
		_headPic = new ImageView(context);
		_headPic.setScaleType(ScaleType.FIT_XY);
		
		_nameTextView = new TextView(context);
		_nameTextView.setGravity(Gravity.CENTER_VERTICAL);
		_nameTextView.setTextColor(context.getResources().getColor(R.color.font_4c87c6));
		_nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(context, 44));
	}
	public void doLayout(){
		_mainLayout.removeAllViews();
		_nameLayout.removeAllViews();
		_headPicLayout.removeAllViews();
		_infoLayout.removeAllViews();
		this.removeAllViews();
		
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);		
		p.setMargins(0, 0, 0, 1);
		addView(_mainLayout,p);
		
		//头像
//		p = new LinearLayout.LayoutParams(_headPicWidth,_headPicHeight);
//		p.setMargins(Function.getFitPx(getContext(), 40), Function.getFitPx(getContext(), 40), 0, Function.getFitPx(getContext(), 40)-1);
//		_mainLayout.addView(_headPicLayout,p);
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		_headPicLayout.addView(_headPic,p);
		p = new LinearLayout.LayoutParams(_headPicWidth,_headPicHeight);
		p.setMargins(Function.getFitPx(getContext(), 40), Function.getFitPx(getContext(), 40), 0, Function.getFitPx(getContext(), 40)-1);
		_mainLayout.addView(_headPicLayout,p);
		RelativeLayout rLayout = new RelativeLayout(getContext());		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		_headPicLayout.addView(rLayout,p);
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		rLayout.addView(_headPic,rp);
		//头像右下角图像
		rp = new RelativeLayout.LayoutParams(Function.getFitPx(getContext(), 30), Function.getFitPx(getContext(), 30));
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rLayout.addView(_headAttachImage,rp);
		
		//个人信息
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		_mainLayout.addView(_infoLayout,p);
		//姓名
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,1);
		p.setMargins(Function.getFitPx(getContext(), 30), 0, 0, 1);
		_infoLayout.addView(_nameLayout,p);
		p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);		
		p.setMargins(0, Function.getFitPx(getContext(), 21), 0, 0);
		_nameLayout.addView(_nameTextView,p);
		p = new LinearLayout.LayoutParams(Function.getFitPx(getContext(), 44),LayoutParams.MATCH_PARENT);		
		p.setMargins(0, Function.getFitPx(getContext(), 21), 0, 0);
		_nameLayout.addView(_headIcon,p);
	}
	
	public Object getTag() {
		return tag;
	}
	public void setTag(Object tag) {
		this.tag = tag;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setHeadPicSize(int width,int height){
		_headPicWidth = Function.getFitPx(getContext(), width);
		_headPicHeight = Function.getFitPx(getContext(), height);
	}
	public void setHeadPic(int resid){
		_headPic.setImageDrawable(getContext().getResources().getDrawable(resid));
	}
	public ImageView getHeadPic() {
		return _headPic;
	}
	public void setHeadIcon(int resid){
		_headIcon.setImageDrawable(getContext().getResources().getDrawable(resid));
	}
	public void setHeadAttachImage(int resid){
		_headAttachImage.setImageDrawable(getContext().getResources().getDrawable(resid));
	}
	public void setName(String name){
		_nameTextView.setText(name);
	}
	public String getName(){
		return _nameTextView.getText().toString();
	}
	public void setNameFontColor(int color){
		_nameTextView.setTextColor(color);
	}
	public void setNameFontSize(int size){
		_nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(getContext(), size));;
	}
}
