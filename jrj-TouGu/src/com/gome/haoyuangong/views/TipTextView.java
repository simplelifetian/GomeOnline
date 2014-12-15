package com.gome.haoyuangong.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class TipTextView extends TextView {
	
	public static final int MIN_SIZE = 8;
	
	public int defaultDipSize;
	
	public TipTextView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public TipTextView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		// TODO Auto-generated constructor stub
		init();
	}

	public TipTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init(){
		defaultDipSize = dipToPixels(MIN_SIZE);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		String text = getText().toString();
		if(text == null || text.length() == 0){
			super.onMeasure(MeasureSpec.makeMeasureSpec(defaultDipSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(defaultDipSize, MeasureSpec.EXACTLY));
		}else if(text.length() == 1){
			int max = getMeasuredWidth() > getMeasuredHeight() ? getMeasuredWidth() : getMeasuredHeight();
			super.onMeasure(MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY));
		}else{
			if(getMeasuredHeight() > getMeasuredWidth()){
				super.onMeasure(MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
			}
		}
	}

	private int dipToPixels(int dip) {
		Resources r = getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
		return (int) px;
	}
}
