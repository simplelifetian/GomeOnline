/**
 * 
 */
package com.gome.haoyuangong.activity;

import com.gome.haoyuangong.R;

import android.os.Bundle;


/**
 * 
 */
public class CommentActionActivity extends BaseActivity {

	private static final String TAG = CommentActionActivity.class.getName();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_activity);
		setTitle("评论");
	}
	
}
