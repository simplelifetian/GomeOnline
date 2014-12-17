package com.gome.haoyuangong.fragments;

import java.util.List;

import com.gome.haoyuangong.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

public class DynamicFragment extends BaseFragment {
	private EditText mEtSearch;
	private ImageView mIvDelete;
	private ImageView mIvSearch;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View parent = super.onCreateView(inflater, container, savedInstanceState);
		View child = inflater.inflate(R.layout.dynamic_fragment, container, false);
		content.addView(child);
		initView(parent);
		return parent;
	}
	
	private void initView(View v) {
		hideTitle();
		mEtSearch = (EditText) v.findViewById(R.id.et_search);
		mIvDelete = (ImageView) v.findViewById(R.id.iv_delete);
		mIvSearch = (ImageView) v.findViewById(R.id.iv_search);
		
	}
	
}
