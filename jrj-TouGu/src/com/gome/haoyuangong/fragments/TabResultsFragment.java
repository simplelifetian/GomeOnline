package com.gome.haoyuangong.fragments;

import com.gome.haoyuangong.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabResultsFragment extends BaseFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View parent = super.onCreateView(inflater, container, savedInstanceState);
		View child = inflater.inflate(R.layout.simple_viewpager_layout, container, false);
		content.addView(child);
		initTitle();
		return parent;
	}
	
	private void initTitle() {
		titleLeft1.setVisibility(View.VISIBLE);
		titleLeft1.setBackgroundResource(R.drawable.actionbar_me);
		titleCenter.setText(getResources().getString(R.string.actionbar_bottom_results));
		titleRight1.setBackgroundResource(R.drawable.top_search_icon);
	}

}
