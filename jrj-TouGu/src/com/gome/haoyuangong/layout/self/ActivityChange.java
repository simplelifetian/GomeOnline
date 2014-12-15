package com.gome.haoyuangong.layout.self;

import com.gome.haoyuangong.activity.FindAdviserCustomCondition;
import com.gome.haoyuangong.activity.SelectOrgnizationActivity;
import com.gome.haoyuangong.activity.ViewInvesterInfoActivity;
import com.gome.haoyuangong.activity.ViewSignUserInfoActivity;

import android.R.bool;
import android.content.Context;
import android.content.Intent;

public class ActivityChange {
	public static void ToAdviserHome(Context context,String userName,String userId){
		Intent intent = new Intent();	
		intent.putExtra("USERNAME", userName);
		intent.putExtra("USERID", userId);
		intent.setClass(context, ViewInvesterInfoActivity.class);
		context.startActivity(intent);
	}
	public static void ToUserHome(Context context,String userName,String userId,String headImage){
		Intent intent = new Intent();	
		intent.putExtra("title", userName);
		intent.putExtra("id", userId);
		intent.putExtra("headImage",headImage);
		intent.setClass(context, ViewSignUserInfoActivity.class);
		context.startActivity(intent);
	}
}
