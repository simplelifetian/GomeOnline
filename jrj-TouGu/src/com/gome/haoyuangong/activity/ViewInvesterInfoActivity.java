package com.gome.haoyuangong.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.fragments.SelfFragment;
import com.gome.haoyuangong.fragments.SelfFragment.IOnSetTitleListener;
import com.gome.haoyuangong.layout.self.SelfView.UserType;

public class ViewInvesterInfoActivity extends BaseActivity {
	public static final String ATENTION_ACTION_NAME = "atention";
	public static String ITEM_KEY = "itemkey";
	public static String OPTION_STATE = "optionstate";
	public static int ATENTION=1;
	public static int UNATENTION=2;
	private String USERID = "";	
	private String USERNAME = "";	
	boolean first=true;
	SelfFragment f;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		FragmentManager fragmentManager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction t = fragmentManager.beginTransaction();
		USERNAME = getIntent().getStringExtra("USERNAME");
		USERID = getIntent().getStringExtra("USERID");
//		SelfFragment.userType = UserType.utUserViewAdviser;
		f = new SelfFragment();	
		f.setItemkey(getIntent().getIntExtra(ITEM_KEY, -1));
		f.setUserId(USERID);
		f.setUserName(USERNAME);
		f.setUserType(UserType.utUserViewAdviser);
		t.add(R.id.content,f);
		t.commit();
		
		
//		f.setUserType(UserType.utUserViewAdviser);	
//		SelfFragment.USERID = USERID;
//		SelfFragment.USERNAME = USERNAME;
		if (USERNAME != null)
			setTitle(USERNAME);
		f.setOnSetTitleListener(new IOnSetTitleListener() {
			
			@Override
			public void OnSetTitle(String title) {
				// TODO Auto-generated method stub
				setTitle(title);
			}
		});		
//		setTitle("个人信息");		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		
		super.onStart();
//		if (!first)
//			return;
////		setTitle(USERNAME);		
//		f.setUserType(UserType.utUserViewAdviser);	
////		f.requestData();
//		first = false;
	}


}
