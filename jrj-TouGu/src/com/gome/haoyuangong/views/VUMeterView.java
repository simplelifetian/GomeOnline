package com.gome.haoyuangong.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.gome.haoyuangong.AppInfo;
import com.gome.haoyuangong.R;

public class VUMeterView extends LinearLayout {
	private final int DEFAULT_COLOR = 0xffffffff;
	private final int SHOW_COLOR = 0xffde3031;
	private int numViews = 7;
	private int curShowView;
	private MyItemView[] showVUArray;
	private boolean isRight;
	private int mDefaultColor;
	private int mShowColor;
	public VUMeterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public void init( AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.VUMeter);
    this.isRight = a.getBoolean(R.styleable.VUMeter_isOrientationRight, false);
    this.numViews = a.getInt(R.styleable.VUMeter_MaxNum, 6);
    this.mDefaultColor=a.getInt(R.styleable.VUMeter_BackColor, DEFAULT_COLOR);
    this.mShowColor=a.getInt(R.styleable.VUMeter_FrontColor, SHOW_COLOR);
    a.recycle();
    setMax();
	}

	/**
	 * 需要在setMax 之前调用
	 * 
	 * @param isRight
	 */
	public void setOrientation(boolean isRight) {
		this.isRight = isRight;
	}

	public void setMax() {
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 0, 0, 0);
		showVUArray = new MyItemView[numViews];
		for (int i = 0; i < numViews; i++) {
			showVUArray[i] = new MyItemView(getContext());
			showVUArray[i].setBackgroundColor(mDefaultColor);
			if (isRight) {
				showVUArray[i].setItemHeight(i * 2 + 5);
			} else {
				showVUArray[i].setItemHeight(5 + (numViews - i - 1) * 2);
			}
			showVUArray[i].setLayoutParams(params);
			addView(showVUArray[i]);
		}
		requestLayout();
	}

	/**
	 * 
	 * @param cur
	 *          0 ~ max
	 */
	public void setCur(int cur) {
		cur--;
		for (int i = 0; i < numViews; i++) {
			if (isRight) {
				if (i <=cur) {
					showVUArray[i].setBackgroundColor(mShowColor);
				} else {
					showVUArray[i].setBackgroundColor(mDefaultColor);
				}
			}else{
				if (i >=(numViews-cur-1)) {
					showVUArray[i].setBackgroundColor(mShowColor);
				} else {
					showVUArray[i].setBackgroundColor(mDefaultColor);
				}
			}
		}
	}
	public void startPlay(){
		isAuto=true;
		autoIndex=0;
		handler.post(refresh);
	}
	public void stopPlay(){
		isAuto=false;
		handler.removeCallbacks(refresh);
		setCur(0);
	}
	private boolean isAuto=false;
	private int autoIndex=0;
	private Runnable refresh = new Runnable() {
		@Override
		public void run() {
			autoIndex++;
			setCur(autoIndex);
			if(autoIndex>numViews){
				autoIndex=0;
			}
			if(isAuto){
				handler.postDelayed(refresh,200);
			}
		}
	};
	Handler handler = new Handler();
	
	private class MyItemView extends View {
		private int targetH;
		private int targetW;

		public MyItemView(Context context) {
			super(context);
			targetW = getResources().getDimensionPixelSize(R.dimen.space_2);
		}

		public void setItemHeight(int h) {
			targetH = AppInfo.dip2px(getContext(), h);
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			setMeasuredDimension(targetW, targetH);
		}
	}
}
