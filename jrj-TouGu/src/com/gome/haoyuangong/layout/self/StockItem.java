package com.gome.haoyuangong.layout.self;

import java.util.ArrayList;
import java.util.List;



import com.gome.haoyuangong.R;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StockItem extends LinearLayout {
	private List<TextView> items;
	private List<LinearLayout> layouts;
	private int headerCount = 4;
	private int leftSpace,topSpace,rightSpace,bottomSpace;
	ImageView arrowImg;
	public StockItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		items = new ArrayList<TextView>();
		layouts = new ArrayList<LinearLayout>();
		arrowImg = new ImageView(getContext());
		arrowImg.setScaleType(ScaleType.CENTER_INSIDE);
	}
	public void hideArrow() {
		arrowImg.setVisibility(INVISIBLE);
	}
	public void setHeaderCount(int count){
		headerCount = count;
	}
	public void setSpace(int left,int top,int right,int bottom){
		leftSpace = left;
		topSpace = top;
		rightSpace = right;
		bottomSpace = bottom;
	}
	public List<TextView> getItems() {
		return items;
	}
	public LinearLayout getLayout(int index){
		return layouts.get(index);
	}
	public void doLayout(){
		items.clear();
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout layout = new LinearLayout(getContext());
		layout.setOrientation(HORIZONTAL);		
		addView(layout,p);	
		
//		for(int i=0;i<headerCount;i++){
//			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
//			LinearLayout subLayout = new LinearLayout(getContext());
//			layouts.add(subLayout);
//			p.setMargins(Function.getFitPx(getContext(), leftSpace), Function.getFitPx(getContext(), topSpace), 
//					Function.getFitPx(getContext(), rightSpace), Function.getFitPx(getContext(), bottomSpace));
//			layout.addView(subLayout,p);
//			
//			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
//			TextView tv = new TextView(getContext());	
//			tv.setGravity(Gravity.CENTER_VERTICAL);
//			tv.setTextColor(getContext().getResources().getColor(R.color.font_727272));
//			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(getContext(), 46));
//			items.add(tv);			
//			subLayout.addView(tv,p);
//		}
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
		LinearLayout subLayout = new LinearLayout(getContext());
		layouts.add(subLayout);
		p.setMargins(Function.getFitPx(getContext(), leftSpace), Function.getFitPx(getContext(), topSpace), 
				Function.getFitPx(getContext(), rightSpace), Function.getFitPx(getContext(), bottomSpace));
		layout.addView(subLayout,p);		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
		TextView tv = new TextView(getContext());	
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setTextColor(getContext().getResources().getColor(R.color.font_727272));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(getContext(), 46));
		items.add(tv);			
		subLayout.addView(tv,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
		subLayout = new LinearLayout(getContext());
		layouts.add(subLayout);
		layout.addView(subLayout,p);		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
		tv = new TextView(getContext());	
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setTextColor(getContext().getResources().getColor(R.color.font_8b8b8b));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(getContext(), 46));
		items.add(tv);			
		subLayout.addView(tv,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		subLayout = new LinearLayout(getContext());
		layouts.add(subLayout);
		layout.addView(subLayout,p);		
		p = new LinearLayout.LayoutParams(Function.getFitPx(getContext(), 30), LayoutParams.MATCH_PARENT,1);
		
		p.setMargins(0, 0, Function.getFitPx(getContext(), 81), 0);
		arrowImg.setImageDrawable(getContext().getResources().getDrawable(R.drawable.right_arrow));

		subLayout.addView(arrowImg,p);
		
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
//		subLayout = new LinearLayout(getContext());
//		layouts.add(subLayout);
//		layout.addView(subLayout,p);		
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		tv = new TextView(getContext());	
//		tv.setGravity(Gravity.CENTER_VERTICAL);
//		tv.setTextColor(getContext().getResources().getColor(R.color.font_727272));
//		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(getContext(), 46));
//		items.add(tv);			
//		subLayout.addView(tv,p);
//		
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
//		subLayout = new LinearLayout(getContext());
//		layouts.add(subLayout);
//		layout.addView(subLayout,p);		
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		tv = new TextView(getContext());	
//		tv.setGravity(Gravity.CENTER_VERTICAL);
//		tv.setTextColor(getContext().getResources().getColor(R.color.font_727272));
//		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(getContext(), 46));
//		items.add(tv);			
//		subLayout.addView(tv,p);
	}

}
