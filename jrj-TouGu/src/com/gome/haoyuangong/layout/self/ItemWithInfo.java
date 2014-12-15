package com.gome.haoyuangong.layout.self;

import com.gome.haoyuangong.R;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ItemWithInfo extends PersonalInfoItem {
	TextView infoView;
	TextView attachInfoView;
	public ItemWithInfo(Context context) {
		super(context);
		this.setBackgroundColor(0xffd0d0d0);
		// TODO Auto-generated constructor stub
		infoView = new TextView(getContext());
		infoView.setTextColor(context.getResources().getColor(R.color.font_595959));
		infoView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(getContext(), 44));
		
		attachInfoView = new TextView(getContext());
		attachInfoView.setTextColor(context.getResources().getColor(R.color.font_de3031));
		attachInfoView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(getContext(), 40));
		attachInfoView.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
	}
	public void doLayout(){
		super.doLayout();
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1);	
		p.setMargins(Function.getFitPx(getContext(), 30), Function.getFitPx(getContext(), 21), 0, 0);
		_infoLayout.addView(infoView,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);	
		p.setMargins(0, Function.getFitPx(getContext(), 21), Function.getFitPx(getContext(), 30), 0);
		_nameLayout.addView(attachInfoView,p);
	}
	public void setInfoText(String info){
		infoView.setText(info);
	}
	public void setAttachInfoText(String info){
		attachInfoView.setText(info);
	}
}
