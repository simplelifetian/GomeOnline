package com.gome.haoyuangong.views;


import com.gome.haoyuangong.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AnalogClock;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class ProgressView extends LinearLayout {

	private ProgressBar mProgressBar;
	private View mTarget;
	private Context ctx;
	private int style;
	public ProgressView(Context context, View mTarget) {
		super(context);
		this.mTarget = mTarget;
		this.ctx = context;
		this.style = android.R.attr.progressBarStyleSmall;
		init();
	}
	public ProgressView(Context context, View mTarget,int style) {
		super(context);
		this.mTarget = mTarget;
		this.ctx = context;
		this.style = style;
		init();
	}

	private void init() {
		mProgressBar = new ProgressBar(ctx,null,style);
		mProgressBar.setIndeterminateDrawable(getResources().getDrawable(R.anim.frame_loading));
		LayoutParams mLyParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLyParams.gravity = Gravity.CENTER;
		this.addView(mProgressBar, mLyParams);
		this.setGravity(Gravity.CENTER);
		applyTo(mTarget);
	}

	private void applyTo(View target) {

		android.view.ViewGroup.LayoutParams lp = target.getLayoutParams();
		ViewParent parent = target.getParent();
		FrameLayout container = new FrameLayout(ctx);

		// TODO verify that parent is indeed a ViewGroup
		ViewGroup group = (ViewGroup) parent;
		int index = group.indexOfChild(target);

		group.removeView(target);
		group.addView(container, index, lp);

		container.addView(target);

		this.setVisibility(View.GONE);
		container.addView(this,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		group.invalidate();
	}
	
	public void show(){
		this.setVisibility(View.VISIBLE);
	}
	public void hide(){
		this.setVisibility(View.GONE);
	}
}
