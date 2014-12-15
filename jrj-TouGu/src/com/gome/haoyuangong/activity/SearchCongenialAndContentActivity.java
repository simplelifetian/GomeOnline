package com.gome.haoyuangong.activity;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.fragments.SearchHistoryRecordFragment;
import com.gome.haoyuangong.fragments.SearchHistoryRecordFragment.OnRecentSearchHistoryRecordClickedListener;
import com.gome.haoyuangong.keyboard.ChangeCodeLayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SearchCongenialAndContentActivity extends BaseActivity implements
		OnRecentSearchHistoryRecordClickedListener {

	
	Fragment mSearchHistoryRecordFragment;
//	Fragment mCongenialSearchResultFragment;
	Fragment mContent;
	FragmentManager mFragmentManager;
	EditText mSearchInput;
	volatile Object mObject = new Object();
	private boolean mOnSearch = false;
	private boolean mOnSearchResult = false;
	private boolean mFirstSearch = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_fragment_search_congenial);
		mSearchHistoryRecordFragment = new SearchHistoryRecordFragment();
		mFragmentManager=getSupportFragmentManager();
		FragmentTransaction mfragmentTransaction = getSupportFragmentManager().beginTransaction();
		mfragmentTransaction.replace(R.id.content1, mSearchHistoryRecordFragment).commit();
		init();
	}

	private void init() {
		setTitle("搜索");
		final TextView clearView = (TextView)findViewById(R.id.clearEditTextTv);
		clearView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSearchInput.setText("");
			}
		});
//		titleRight2.setBackgroundResource(R.drawable.find_adviser);
//		titleRight2.setOnClickListener(this);
		mSearchInput = (EditText) findViewById(R.id.SearchInput);
		mSearchInput.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence input, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if(input.length() > 0){
					clearView.setVisibility(View.VISIBLE);
				}else{
					clearView.setVisibility(View.GONE);
				}
				if (input != null && (input.toString().length() > 1)) {
					mOnSearch = true;
				}
				if(mOnSearchResult){
//					FragmentTransaction mfragmentTransaction = getSupportFragmentManager().beginTransaction();
//					mfragmentTransaction.remove(mCongenialSearchResultFragment);
//					mfragmentTransaction.add(R.id.content1,mSearchHistoryRecordFragment);
//					mfragmentTransaction.commit();
					
//					switchContent(mCongenialSearchResultFragment,mSearchHistoryRecordFragment);
				}
			}

		});
//		mSearchInput
//				.setOnEditorActionListener(new EditText.OnEditorActionListener() {
//					@Override
//					public boolean onEditorAction(TextView v, int actionId,
//							KeyEvent event) {
//						// TODO Auto-generated method stub
//						// synchronized(mObject){
//						if (actionId == EditorInfo.IME_ACTION_SEARCH
//								&& mOnSearch && mFirstSearch) {
//							FragmentTransaction mfragmentTransaction = getSupportFragmentManager().beginTransaction();
//							mfragmentTransaction
//									.remove(mSearchHistoryRecordFragment);
//							mfragmentTransaction.add(R.id.content1,
//									mCongenialSearchResultFragment);
//							mfragmentTransaction.commit();
//							mOnSearch = false;
//							mOnSearchResult=true;
//							mFirstSearch=false;
//							return true;
//						}else if(actionId == EditorInfo.IME_ACTION_SEARCH
//								&& mOnSearch){
////							switchContent(mSearchHistoryRecordFragment,mCongenialSearchResultFragment);
//						}
//						// }
//
//						return false;
//					}
//				});
//		Button searchButton = (Button) findViewById(R.id.SearchButton);
//		searchButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//			}
//
//		});
		
		mSearchInput
		.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
//				if(event !=null && event.getKeyCode()==KeyEvent.KEYCODE_ENTER){
//					return true;
//				}
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					hideSoftInput();
				}

				return false;
			}
		});
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_right2:
			startActivity(new Intent(this,
					FindAdviserCustomCondition.class));

			break;
		}
	}
	public void switchContent(Fragment from, Fragment to) {
        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction = mFragmentManager.beginTransaction().setCustomAnimations(
                    android.R.anim.fade_in, android.R.anim.fade_out);
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.content1, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

	@Override
	public void onRecordItemClicked(String serarchKey) {
		// TODO Auto-generated method stub
		mSearchInput.setText(serarchKey);
	}

}
