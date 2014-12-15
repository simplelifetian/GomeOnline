package com.gome.haoyuangong.presenter;

import android.content.Intent;

import com.gome.haoyuangong.BaseViewImpl;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.activity.LoginActivity;

public class ITradePresenter extends IBasePresenter {
	
	public ITradePresenter(BaseViewImpl vImpl) {
		super(vImpl);
	}

	public void gotoTrade(){
//		Intent i = new Intent(getContext(), TradeActivity.class);
//		if(UserInfo.getInstance().isLogin()){
//		}else{
//			i.setClass(getContext(), LoginActivity.class);
//			i.putExtra(LoginActivity.BUNDLE_PARAM_TARGET_ACTIVITY, TradeActivity.class.getName());
//		}
//		getContext().startActivity(i);
	}
	

}
