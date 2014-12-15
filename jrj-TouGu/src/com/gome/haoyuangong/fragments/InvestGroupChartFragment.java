package com.gome.haoyuangong.fragments;

import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.layout.self.InvestGroupChart;

import android.content.pm.LabeledIntent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class InvestGroupChartFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		ViewGroup vg = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
		
		return createView();
	}
	
	private View createView(){
		LinearLayout layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(getContext(), 200));
		InvestGroupChart chart = new InvestGroupChart(getContext());
		chart.setBackgroundColor(Color.WHITE);
		layout.addView(chart,p);
		
		return layout;
	}
}
