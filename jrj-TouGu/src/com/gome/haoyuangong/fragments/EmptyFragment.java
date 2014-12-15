/**
 * 
 */
package com.gome.haoyuangong.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.activity.LoginActivity;

/**
 * 
 */
public class EmptyFragment extends BaseFragment {

	private static final String TAG = EmptyFragment.class.getName();

	private ImageView imageView1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.empty_fragment_layout, null);
		imageView1 = (ImageView)v.findViewById(R.id.imageView1);
		imageView1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mActivity,LoginActivity.class);
				startActivity(intent);
			}
		});
		return v;
	}
	
	

}
