package com.gome.haoyuangong.fragments;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.activity.BaseActivity;
import com.gome.haoyuangong.activity.EditBriefActivity;
import com.gome.haoyuangong.activity.GroupMessageActivity;
import com.gome.haoyuangong.activity.InvestGroupActivity;
import com.gome.haoyuangong.activity.InvestOpinionActivity;
import com.gome.haoyuangong.activity.LiveRoomActivity;
import com.gome.haoyuangong.activity.LoginActivity;
import com.gome.haoyuangong.activity.MyAttentionsActivity;
import com.gome.haoyuangong.activity.MyConsultActivity;
import com.gome.haoyuangong.activity.MyFansActivity;
import com.gome.haoyuangong.activity.MySelfInfoActivity;
import com.gome.haoyuangong.activity.MySignedActivity;
import com.gome.haoyuangong.activity.OpenConsultingActivity;
import com.gome.haoyuangong.activity.ProtocolActivity;
import com.gome.haoyuangong.activity.SetupActivity;
import com.gome.haoyuangong.activity.ViewInvesterInfoActivity;
import com.gome.haoyuangong.activity.WebViewActivity;
import com.gome.haoyuangong.dialog.DialogManage;
import com.gome.haoyuangong.layout.self.Accrediation;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.layout.self.SelfInfo;
import com.gome.haoyuangong.layout.self.SelfView;
import com.gome.haoyuangong.layout.self.Accrediation.AccrediationType;
import com.gome.haoyuangong.layout.self.SelfView.BusinessType;
import com.gome.haoyuangong.layout.self.SelfView.IItemClicked;
import com.gome.haoyuangong.layout.self.SelfView.SocialBarItem;
import com.gome.haoyuangong.layout.self.SelfView.SocialType;
import com.gome.haoyuangong.layout.self.SelfView.UserType;
import com.gome.haoyuangong.layout.self.data.InvestAdviserInfo;
import com.gome.haoyuangong.layout.self.data.InvestAdviserManage;
import com.gome.haoyuangong.layout.self.data.UserManageBean;
import com.gome.haoyuangong.layout.self.data.InvestAdviserManage.Data;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.BaseSignResult;
import com.gome.haoyuangong.net.result.TouguBaseResult;
import com.gome.haoyuangong.net.url.NetUrlMyInfo;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.presenter.ITradePresenter;
import com.gome.haoyuangong.views.SwipeRefreshLayout;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class SelfFragment extends BaseFragment{
	public static interface IOnSetTitleListener{
		public void OnSetTitle(String title);
	}
	IOnSetTitleListener onSetTitleListener;
//	public static String USERID = "";	
//	public static String USERNAME = "";	
	public SelfView.UserType userType = UserType.utNone;
	SelfView selfView;
	BaseActivity _activity;
	ImageLoader imageLoader;
	boolean signed;
	boolean attended;
	String userId="";
	String userName = "";
	Object infoItem;
	boolean operating = false;
	int itemkey=-1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		ViewGroup vg = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
		init();
		return vg;
//		return inflater.inflate(R.layout.fragment_5, null);		
	}	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		_activity = (BaseActivity) activity;
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_right2:
			Intent intent = new Intent(mActivity, SetupActivity.class);
			startActivity(intent);
			break;
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == BusinessType.btSign.ordinal()){
//			doSign();
		}
		else if (requestCode == BusinessType.btAttention.ordinal()){
//			doAttention();
		}
	}
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();		
	}
	
	public void setItemkey(int itemkey) {
		this.itemkey = itemkey;
	}
	public SelfView.UserType getUserType() {
		return userType;
	}
	public void setOnSetTitleListener(IOnSetTitleListener onSetTitleListener) {
		this.onSetTitleListener = onSetTitleListener;
	}
	private void init(){
		if (userType == userType.utNone){
			if (UserInfo.getInstance().isLogin() && UserInfo.getInstance().isTougu())
				userType = UserType.utInvester;
			else
				userType = UserType.utUser;
		}
		imageLoader = new ImageLoader(getContext());
		selfView = new SelfView(getContext());		
		selfView.setActivity(this.getActivity());
		if (TextUtils.isEmpty(UserInfo.getInstance().getUserName()))
			setTitle("游客");
		else
			setTitle("我的");
		selfView.setUserType(userType);
		selfView.layout();
//		setTitle(UserInfo.getInstance().getUserName());
		selfView.setItemClicked(new IItemClicked() {
			
			@Override
			public void OnItemClicked(BusinessType businessType) {
				Intent intent = new Intent();
				// TODO Auto-generated method stub
				if (selfView.getUserType() == UserType.utUserViewAdviser)
					intent.putExtra("name", userName);
				else
					intent.putExtra("name", UserInfo.getInstance().getUserName());
				intent.putExtra("viewid", userId);
				intent.putExtra("usertype", selfView.getUserType().ordinal());
				if (businessType == BusinessType.btViewSign){
//					intent.putExtra("viewtype", "pinyin");
					intent.setClass(getActivity(),MySignedActivity.class);
				}
				else if (businessType == BusinessType.btViewMySelfInfo){
					if (selfView.getUserType() == UserType.utUserViewAdviser)
						return;
					if (selfView.getUserType() != UserType.utUserViewAdviser && infoItem!= null && UserInfo.getInstance().isLogin() && !UserInfo.getInstance().isTougu()){
						UserManageBean.Item item = (UserManageBean.Item)infoItem;
						intent.putExtra("headimage", item.getHeadImage());
						intent.putExtra("address", item.getProvince());
					}
					SelfInfo.userType = selfView.getUserType();
//					if (userType == UserType.utUserViewAdviser)
//						intent.putExtra("viewid", userId);
					intent.setClass(getActivity(),MySelfInfoActivity.class);					
				}
				else if (businessType == BusinessType.btViewFans){
					if (!UserInfo.getInstance().isLogin()){
						intent.setClass(getActivity(), LoginActivity.class);
						startActivityForResult(intent, BusinessType.btViewFans.ordinal());	
						return;
					}
					intent.setClass(getActivity(),MyFansActivity.class);
				}
				
				else if (businessType == BusinessType.btViewAttenions)
					intent.setClass(getActivity(),MyAttentionsActivity.class);
				
				else if (businessType == BusinessType.btViewUserAttenions)
					intent.setClass(getActivity(),MyAttentionsActivity.class);
				
				else if (businessType == BusinessType.btBrief){
					intent.putExtra(EditBriefActivity.TITLE, "简介");
					intent.putExtra(EditBriefActivity.BRIEFCONTENT, selfView.getServiceBar().getItem(BusinessType.btBrief).getInfoText());
					intent.setClass(getActivity(), EditBriefActivity.class);
				}
				
				else if (businessType == BusinessType.btInvestGroup)
					intent.setClass(getActivity(),InvestGroupActivity.class);
				
				else if (businessType == BusinessType.btInvestOpinion)
					intent.setClass(getActivity(),InvestOpinionActivity.class);
				
				else if (businessType == BusinessType.btViewConsult)
					intent.setClass(getActivity(),MyConsultActivity.class);
				else if (businessType == BusinessType.btGroupMessage)
					intent.setClass(getActivity(),GroupMessageActivity.class);
				else if (businessType == BusinessType.btTrade){
					ITradePresenter itp = new ITradePresenter(SelfFragment.this);
					itp.gotoTrade();
					return;
				}
				else if (businessType == BusinessType.btLive){
					if (selfView.getServiceBar().getItem(BusinessType.btLive) != null){
						InvestAdviserManage.Data item = (InvestAdviserManage.Data)selfView.getTag();
						if (item==null || item.getLiveJson().getZhibo_isopen() == -1){
							if (selfView.getUserType() == UserType.utUserViewAdviser)
								DialogManage.showSingleButtonDialog(getContext(), "今天投顾有点忙，未开启直播哦", "知道了");
							else
								DialogManage.showSingleButtonDialog(getContext(), "开始直播功能正在开发，您可以到\"爱投顾\"网页端开启直播哦", "知道了");
//							Function.showToask(getContext(), "该投顾未开启直播室！");
							return;
						}
					}
					if (selfView.getUserType() == UserType.utUserViewAdviser){
						intent.putExtra(LiveRoomActivity.BUNDLE_PARAM_ROOMID, userId);
						intent.putExtra(LiveRoomActivity.BUNDLE_PARAM_NAME, userName);
					}
					else{
						intent.putExtra(LiveRoomActivity.BUNDLE_PARAM_ROOMID, UserInfo.getInstance().getUserId());
						intent.putExtra(LiveRoomActivity.BUNDLE_PARAM_NAME, UserInfo.getInstance().getUserName());
					}
					intent.setClass(getActivity(),LiveRoomActivity.class);
				}
				else if (businessType == BusinessType.btMyAdviser){
					if (infoItem == null)
						return;
					if (TextUtils.isEmpty(((UserManageBean.Item)infoItem).getAdviserId())){
						doNoSignedAdviser();
						return;
					}
					intent.putExtra("USERID", ((UserManageBean.Item)infoItem).getAdviserId());
					intent.setClass(getActivity(),ViewInvesterInfoActivity.class);
					
				}
				else if (businessType == BusinessType.btSign){
					sign();					
//					doSign();
					return;
				}
				else if (businessType == BusinessType.btAttention){
					attention();
//					doAttention();
					return;
				}
				else if (businessType == BusinessType.btAsk){
					if (!UserInfo.getInstance().isLogin()){		
						intent.setClass(getActivity(), LoginActivity.class);
						startActivityForResult(intent, BusinessType.btSign.ordinal());	
						return;
					}
					else {
						if (selfView.getUserType() == UserType.utUserViewAdviser){
							intent.setClass(getActivity(),OpenConsultingActivity.class);
							intent.putExtra(OpenConsultingActivity.BUNDLE_TYPE, OpenConsultingActivity.SPECIAL_CONSULTING);
							intent.putExtra(OpenConsultingActivity.BUNDLE_PARAM_NAME, userName);
							intent.putExtra(OpenConsultingActivity.BUNDLE_PARAM_ID, userId);
						}
					}
					
				}
				else if (businessType == BusinessType.btRefresh){
					requestData();
					return;
				}
				
				startActivity(intent);
			}
		});
		setContent(selfView);
		initTitle();
		requestData();
	}
	private void doNoSignedAdviser() {
		DialogManage.showTwoButtonDialog(getContext(), "您尚未签约投顾", "取消", "去签约", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				startActivity(new Intent(
//				mActivity,
//				com.jrj.tougu.activity.FindCongenialListActivity_.class));
				dialog.dismiss();
			}
		});
//		final Dialog dialog = new Dialog(getContext());
//		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		dialog.setContentView(R.layout.dialog_nosigned);
//		Window dialogWindow = dialog.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.width = (int) (selfView.getWidth()*0.9);
//        dialogWindow.setAttributes(lp);		
//
//        TextView tvOk = (TextView)dialog.findViewById(R.id.unregist_btnok);
//        tvOk.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				startActivity(new Intent(
//						mActivity,
//						com.jrj.tougu.activity.FindCongenialListActivity_.class));
//				dialog.dismiss();
//			}
//		});
//        tvOk = (TextView)dialog.findViewById(R.id.unregist_btncancel);
//        tvOk.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				dialog.dismiss();
//			}
//		});
//        
//        dialog.show();
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setUserId(String userId) {
		this.userId = userId;
		selfView.USERID = userId;
	}
	private String getUserId(){
		if (selfView.getUserType() == UserType.utUserViewAdviser)
			return userId;
		else
			return UserInfo.getInstance().getUserId();
	}
	
	private Accrediation.IAccrediatedListener accrediatedListener = new Accrediation.IAccrediatedListener() {
		
		@Override
		public void OnAccrediated(int returnCode) {
			// TODO Auto-generated method stub
			operating = false;
			//签约成功
			if (returnCode == 1){
				signed = true;
				selfView.getSelfFoot().changeSignState(1);
				attended=true;
				SocialBarItem socialBarItem = selfView.getSocialBar().getItem(SocialType.stFans);
				if (socialBarItem != null)
					socialBarItem.setValue(String.valueOf(Integer.parseInt(socialBarItem.getValue())+1));
				Function.showToask(getContext(), "签约成功");
			}
			//取消签约成功
			else if (returnCode == 3){
				signed = false;
				selfView.getSelfFoot().changeSignState(0);
				SocialBarItem socialBarItem = selfView.getSocialBar().getItem(SocialType.stFans);
				if (socialBarItem != null)
					socialBarItem.setValue(String.valueOf(Integer.parseInt(socialBarItem.getValue())-1));
				Function.showToask(getContext(), "解除签约成功");
			}
			//关注成功
			else if (returnCode == 2){
				attended = true;
				selfView.getSelfFoot().changeAttenionState(1);
				Function.showToask(getContext(), "关注成功");
			}
			//取消关注成功
			else if (returnCode == 4){
				attended = false;
				selfView.getSelfFoot().changeAttenionState(1);
				Function.showToask(getContext(), "取消关注成功");
			}
		}
	};
	
	private void sign() {		
		//未登录
		if (!UserInfo.getInstance().isLogin()){		
			Intent intent = new Intent();
			intent.setClass(getActivity(), LoginActivity.class);
			startActivityForResult(intent, BusinessType.btSign.ordinal());
		}
		else {
			if (operating)
				return;
			operating = true;
			int flag = 1;
			if (signed)
				flag = 0;
			Accrediation.USERNAME = userName;
			Accrediation.Accrediated(getActivity(), AccrediationType.atSign, flag, userId, UserInfo.getInstance().getUserId(), accrediatedListener);
		}
	}
	/**
	 * 已存在签约投顾提示
	 */
	private void alertExsistSigned(){
		final Dialog dialog = new Dialog(getContext());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_unbindsign);
		Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (selfView.getWidth()*0.9);
        dialogWindow.setAttributes(lp);		
        TextView tvLink = (TextView)dialog.findViewById(R.id.linktext);
        if (tvLink != null){
        	tvLink.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					WebViewActivity.gotoWebViewActivity(getContext(), "证券通投资协议", "http://itougu.jrj.com.cn/site/adviser_agreement.html");
//					getContext().startActivity(new Intent(getActivity(),ProtocolActivity.class));
				}
			});
        }
        TextView tvOk = (TextView)dialog.findViewById(R.id.unsign_btnok);
        tvOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doSign();
				dialog.dismiss();
			}
		});
        tvOk = (TextView)dialog.findViewById(R.id.unsign_btncancel);
        tvOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getContext(), "签约取消",Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		});
        
        dialog.show();
	}
	/**
	 * 没有签约时提示
	 */
	private void alertNoSigned() {
		final Dialog dialog = new Dialog(getContext());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_unsign);
		Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (selfView.getWidth()*0.9);
        dialogWindow.setAttributes(lp);		
        TextView tvLink = (TextView)dialog.findViewById(R.id.linktext);
        final TextView tvOk = (TextView)dialog.findViewById(R.id.unsign_btnok);
        final CheckBox checkBox = (CheckBox)dialog.findViewById(R.id.checkBox1);
        if (checkBox != null){
        	checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if (!isChecked)
						tvOk.setTextColor(getActivity().getResources().getColor(R.color.font_595959));
					else
						tvOk.setTextColor(getActivity().getResources().getColor(R.color.font_4c87c6));
				}
			});
        }
        if (tvLink != null){
        	tvLink.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					WebViewActivity.gotoWebViewActivity(getContext(), "证券通投资协议", "http://itougu.jrj.com.cn/site/adviser_agreement.html");
//					getContext().startActivity(new Intent(getActivity(),ProtocolActivity.class));
				}
			});
        }
        
        tvOk.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!checkBox.isChecked())
					return;
				doSign();
				dialog.dismiss();
			}
		});
        TextView tvCancel = (TextView)dialog.findViewById(R.id.unsign_btncancel);
        tvCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getContext(), "签约取消",Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		});
        
        dialog.show();
	}
	private void checkSigned(){
		String url = String.format(NetUrlMyInfo.CHECKSIGNED, getUserId());
		JsonRequest<TouguBaseResult> request = new JsonRequest<TouguBaseResult>(Method.GET, url,
				new RequestHandlerListener<TouguBaseResult>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						// showDialog(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						// hideDialog(request);
					}

					@Override
					public void onSuccess(String id, TouguBaseResult data) {
						// TODO Auto-generated method stub
						try{
							if (data.getRetCode() != 0){
								Function.showToask(getActivity(), data.getMsg());								
							}
							else {
								alertNoSigned();
							}
						}
						catch(Exception e){
							
						}
						
					}
				}, TouguBaseResult.class);
			((BaseActivity)getActivity()).send(request);
	}
	private void doSign(){
		String url = "";
		if (signed)
			url = String.format(NetUrlMyInfo.SIGNURL, userId,UserInfo.getInstance().getUserId(),0);
		else
			url = String.format(NetUrlMyInfo.SIGNURL, userId,UserInfo.getInstance().getUserId(),1);
		JsonRequest<TouguBaseResult> request = new JsonRequest<TouguBaseResult>(Method.GET, url,
				new RequestHandlerListener<TouguBaseResult>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						// showDialog(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						// hideDialog(request);
					}

					@SuppressLint("ShowToast") @Override
					public void onSuccess(String id, TouguBaseResult data) {
						// TODO Auto-generated method stub
						try{
							if (data.getRetCode() == 0){
								if (signed)
									selfView.getSelfFoot().changeSignState(0);
								else
									selfView.getSelfFoot().changeSignState(1);
								signed = !signed;
								if (signed)
									attended=true;								
							}
//							Toast.makeText(_activity, data.getMsg(), Toast.LENGTH_SHORT).show();
						}
						catch(Exception e){
							Toast.makeText(_activity, e.toString(), Toast.LENGTH_SHORT).show();
						}
						
					}
				}, TouguBaseResult.class);
		_activity.send(request);
	}
	
	private void attention() {
		//未登录
		if (!UserInfo.getInstance().isLogin()){		
			Intent intent = new Intent();
			intent.setClass(getActivity(), LoginActivity.class);
			startActivityForResult(intent, BusinessType.btAttention.ordinal());
		}
		else{
			doAttention();
		}
	}
	private void doAttention(){
		String url = "";
		if (attended)
			url = String.format(NetUrlMyInfo.ATTENTION, userId,UserInfo.getInstance().getUserId(),0);
		else
			url = String.format(NetUrlMyInfo.ATTENTION, userId,UserInfo.getInstance().getUserId(),1);
		JsonRequest<BaseSignResult> request = new JsonRequest<BaseSignResult>(Method.GET, url,
				new RequestHandlerListener<BaseSignResult>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						 showDialog(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						 hideDialog(request);
					}
					@Override
					public void onFailure(String id, int code, String str,
							Object obj) {
						// TODO Auto-generated method stub
						super.onFailure(id, code, str, obj);
						Toast.makeText(_activity, "操作超时，请稍后再试！", Toast.LENGTH_SHORT).show();		
						
					}
					@SuppressLint("ShowToast") @Override
					public void onSuccess(String id, BaseSignResult data) {
						// TODO Auto-generated method stub
						try{
							Intent intent = new Intent(ViewInvesterInfoActivity.ATENTION_ACTION_NAME);
							intent.putExtra(ViewInvesterInfoActivity.ITEM_KEY, itemkey);
							SocialBarItem socialBarItem = selfView.getSocialBar().getItem(SocialType.stFans);
							//取消成功							
							if (data.getRetCode() == 4){
								selfView.getSelfFoot().changeAttenionState(0);
								attended = !attended;
								if (socialBarItem != null)
									socialBarItem.setValue(String.valueOf(Integer.parseInt(socialBarItem.getValue())-1));
								Function.showToask(getActivity(), "取消关注");
								intent.putExtra(ViewInvesterInfoActivity.OPTION_STATE, ViewInvesterInfoActivity.UNATENTION);
								sendBroadCast(intent);
							}
							else if (data.getRetCode() == 2){
								selfView.getSelfFoot().changeAttenionState(1);
								com.gome.haoyuangong.MyApplication.get().setNewConcent(true);
								attended = !attended;
								if (socialBarItem != null)
									socialBarItem.setValue(String.valueOf(Integer.parseInt(socialBarItem.getValue())+1));
								Function.showToask(getActivity(), "已关注");
								intent.putExtra(ViewInvesterInfoActivity.OPTION_STATE, ViewInvesterInfoActivity.ATENTION);
								sendBroadCast(intent);
							}	
							else{
								if (signed)
									DialogManage.showSingleButtonDialog(getContext(), "已签约的投顾不能取消关注", "知道了");
							}
						}
						catch(Exception e){
							Logger.info("doAttention", e.toString());
						}
						
					}
				}, BaseSignResult.class);
		_activity.send(request);
	}
	
	private void sendBroadCast(Intent intent) {
		getActivity().sendBroadcast(intent);
	}
	private void initTitle(){
		if (userType == UserType.utUserViewAdviser){
			hideTitle();
		}
		titleLeft1.setVisibility(View.GONE);	
		titleRight2.setBackgroundResource(R.drawable.icon_setup);
	}
	public void setUserType(UserType userType){
		this.userType = userType;		
		SelfView.USERID = userId;
		SelfView.USERNAME = userName;
	}
	public void doLayout() {
		selfView.layout();
	}

	public void requestData() {
		String url = "";
		if (userType == UserType.utUserViewAdviser){
			url = String.format(NetUrlMyInfo.INVESTERHOME,userId, UserInfo.getInstance().getUserId());
			requestAdviserData(url);
//			userType = UserType.utNone;
		}
		else{
			if (selfView.getUserType() == UserType.utUser){
				url = String.format(NetUrlMyInfo.USERMANAGEURL, UserInfo.getInstance().getUserId());
				requestUserData(url);
			}
			else{
				url = String.format(NetUrlMyInfo.INVESTERSELFMANAGEURL, UserInfo.getInstance().getUserId());
				requestAdviserData(url);
			}
		}
		
	}
	private void requestAdviserData(String url){
		JsonRequest<InvestAdviserManage> request = new JsonRequest<InvestAdviserManage>(Method.GET, url,
				new RequestHandlerListener<InvestAdviserManage>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						 showDialog(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						View view = selfView.getChildAt(0);
						if (view != null && view instanceof SwipeRefreshLayout){
							((SwipeRefreshLayout)view).stopRefresh();
						}
						 hideDialog(request);
					}

					@Override
					public void onSuccess(String id, InvestAdviserManage data) {
						// TODO Auto-generated method stub
						try{
							fillAdviserData(data);
						}
						catch(Exception e){
							Logger.info("requestAdviserData", e.toString());
//							Toast.makeText(_activity, e.toString(), Toast.LENGTH_SHORT).show();
						}
						
					}
				}, InvestAdviserManage.class);
		_activity.send(request);
	}
	private void requestUserData(String url){
		JsonRequest<UserManageBean> request = new JsonRequest<UserManageBean>(Method.GET, url,
				new RequestHandlerListener<UserManageBean>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						 showDialog(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						View view = selfView.getChildAt(0);
						if (view != null && view instanceof SwipeRefreshLayout){
							((SwipeRefreshLayout)view).stopRefresh();
						}
						 hideDialog(request);
					}

					@Override
					public void onSuccess(String id, UserManageBean data) {
						// TODO Auto-generated method stub
						try{
							fillUserData(data);
						}
						catch(Exception e){
							
						}
						
					}
				}, UserManageBean.class);
		_activity.send(request);
	}
	private void fillAdviserData(InvestAdviserManage data){
		Data item = data.getData();
		infoItem = item;
		selfView.setTag(item);
		if (item.getUser() == null)
		{
			Toast.makeText(_activity, "没有获取到投顾数据", Toast.LENGTH_SHORT).show();
			return;
		}
		userName = item.getUser().getUserName();
		imageLoader.downLoadImage(item.getUser().getHeadImage(), selfView.getSelfHead().getHeadPicImageView());
		selfView.getSelfHead().setInvesterName(item.getUser().getUserName());
		if (this.onSetTitleListener != null){
			this.onSetTitleListener.OnSetTitle(item.getUser().getUserName());
		}
		if (item.getUser().getType() == 2)
			selfView.getSelfHead().setCompany(item.getUser().getCompany());
		else{
			if (selfView.getServiceBar().getItem(BusinessType.btCertification) != null)
				selfView.getServiceBar().getItem(BusinessType.btCertification).setVisibility(View.GONE);
		}
		selfView.getSelfHead().setIdentity(item.getUser().getTypeDesc());
		if (item.getIsVerify() == 1)
			selfView.getSelfHead().showHeadIcon(true);
		else
			selfView.getSelfHead().showHeadIcon(false);
		selfView.setSignNum(item.getUser().getSignNum());
		
		if (selfView.getSocialBar().getItem(SocialType.stSatisfy) != null)
			selfView.getSocialBar().getItem(SocialType.stSatisfy).setValue(String.valueOf(item.getUser().getUseSatisfaction())+"%");
		if (selfView.getSocialBar().getItem(SocialType.stFans) != null)
			selfView.getSocialBar().getItem(SocialType.stFans).setValue(String.valueOf(item.getUser().getFansNum()));
		if (selfView.getSocialBar().getItem(SocialType.stAttention) != null)
			selfView.getSocialBar().getItem(SocialType.stAttention).setValue(String.valueOf(item.getFocusNum()));
		if (selfView.getSocialBar().getItem(SocialType.stAgree) != null)
			selfView.getSocialBar().getItem(SocialType.stAgree).setValue(String.valueOf(item.getViewMap().getLikeTotal()));
		
		if (selfView.getServiceBar().getItem(BusinessType.btInvestGroup) != null)
			selfView.getServiceBar().getItem(BusinessType.btInvestGroup).setInfoText(String.valueOf(item.getInvestNum()));
		if (selfView.getServiceBar().getItem(BusinessType.btInvestOpinion) != null)
			selfView.getServiceBar().getItem(BusinessType.btInvestOpinion).setInfoText(String.valueOf(item.getViewMap().getPublishTotal()));
		if (selfView.getServiceBar().getItem(BusinessType.btLive) != null){
			if (item.getLiveJson().getZhibo_isopen() == 1){
				selfView.getServiceBar().getItem(BusinessType.btLive).setStateText(" [正在直播]");
				selfView.getServiceBar().getItem(BusinessType.btLive).setStateFontColor(getContext().getResources().getColor(R.color.font_dd3030));
//				selfView.getServiceBar().getItem(BusinessType.btLive).setInfoText("人气："+String.valueOf(item.getLiveJson().getUv_show()));
			}
			else if (item.getLiveJson().getZhibo_isopen() == 0){
				selfView.getServiceBar().getItem(BusinessType.btLive).setStateText(" [直播暂停]");
				selfView.getServiceBar().getItem(BusinessType.btLive).setStateFontColor(getContext().getResources().getColor(R.color.font_dd3030));
			}
			else {
				selfView.getServiceBar().getItem(BusinessType.btLive).setStateText(" [直播关闭]");
				selfView.getServiceBar().getItem(BusinessType.btLive).setStateFontColor(getContext().getResources().getColor(R.color.font_cccccc));
			}
		}
		if (selfView.getServiceBar().getItem(BusinessType.btViewConsult) != null){
			int i = item.getAnswerJson().getAnswerd();
			int j = item.getAnswerJson().getNoanswer();
			if (selfView.getUserType() == UserType.utUserViewAdviser)
				selfView.getServiceBar().getItem(BusinessType.btViewConsult).setInfoText(String.format("%d", i));
			else{
				String str = String.format("%d/%d", j,i);
				SpannableStringBuilder builder = new SpannableStringBuilder(str);
				ForegroundColorSpan span = new ForegroundColorSpan(getContext().getResources().getColor(R.color.font_dd3030));
				builder.setSpan(span, 0, String.valueOf(j).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				selfView.getServiceBar().getItem(BusinessType.btViewConsult).setInfoText(builder);
			}
		}
		//个人看投顾
		if (selfView.getUserType() == UserType.utUserViewAdviser){
			SelfView.USERID = item.getUser().getUserId();
			SelfView.USERNAME = item.getUser().getUserName();
			if (item.getSignFlag() == 5){
				selfView.getSelfFoot().changeSignState(1);
				signed = true;
				attended = true;
			}
			else if (item.getSignFlag() == 4){
				selfView.getSelfFoot().changeAttenionState(1);
				signed = false;
				attended = true;
			}
			if (selfView.getServiceBar().getItem(BusinessType.btExpert) != null)
				selfView.getServiceBar().getItem(BusinessType.btExpert).setInfoText(item.getUser().getInvestDirection().replace(","," "));
			if (selfView.getServiceBar().getItem(BusinessType.btWorkLimit) != null){
				if (item.getUser().getExperienceScope()==1)
					selfView.getServiceBar().getItem(BusinessType.btWorkLimit).setInfoText("1年以下");
				else if (item.getUser().getExperienceScope() == 2)
					selfView.getServiceBar().getItem(BusinessType.btWorkLimit).setInfoText("1-3年");
				else if (item.getUser().getExperienceScope() == 2)
					selfView.getServiceBar().getItem(BusinessType.btWorkLimit).setInfoText("3-5年");
				else
					selfView.getServiceBar().getItem(BusinessType.btWorkLimit).setInfoText("5年以上");
			}
			if (selfView.getServiceBar().getItem(BusinessType.btCertification) != null)
				selfView.getServiceBar().getItem(BusinessType.btCertification).setInfoText(item.getUser().getCertificationNum());
			if (selfView.getServiceBar().getItem(BusinessType.btBrief) != null)
				selfView.getServiceBar().getItem(BusinessType.btBrief).setInfoText(item.getUser().getIntro());
		}
	}
	private void fillUserData(UserManageBean data){
		UserManageBean.Item item = data.getData();
		infoItem = item;
		imageLoader.downLoadImage(item.getHeadImage(), selfView.getSelfHead().getHeadPicImageView());
		selfView.getSelfHead().setIdentity("");
		selfView.getSelfHead().setInvesterName(item.getUserName());
//		((BaseActivity)getActivity()).setTitle(item.getUserName());
		selfView.getSelfHead().setCompany(item.getProvince()+" "+item.getCity());
		selfView.setSignNum(item.getAttentionNum());

		if (selfView.getServiceBar().getItem(BusinessType.btViewConsult) != null)
			selfView.getServiceBar().getItem(BusinessType.btViewConsult).setInfoText(String.valueOf(item.getConsultNum()));
	}

}