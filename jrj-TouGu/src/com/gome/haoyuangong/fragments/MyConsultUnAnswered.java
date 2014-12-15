package com.gome.haoyuangong.fragments;

import java.util.ArrayList;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.activity.AskDetailActivity;
import com.gome.haoyuangong.activity.AttentionDetailActivity;
import com.gome.haoyuangong.activity.BaseActivity;
import com.gome.haoyuangong.activity.MyAttentionsActivity;
import com.gome.haoyuangong.activity.ReplyActivity;
import com.gome.haoyuangong.activity.ReplyMediaRecordActivity;
import com.gome.haoyuangong.activity.ViewInvesterInfoActivity;
import com.gome.haoyuangong.activity.ViewSignUserInfoActivity;
import com.gome.haoyuangong.layout.self.ActivityChange;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.layout.self.ItemWithInfo;
import com.gome.haoyuangong.layout.self.SelfView;
import com.gome.haoyuangong.layout.self.SelfView.UserType;
import com.gome.haoyuangong.layout.self.data.Answered_Adviser;
import com.gome.haoyuangong.layout.self.data.Answered_User;
import com.gome.haoyuangong.layout.self.data.SignedList;
import com.gome.haoyuangong.layout.self.data.Answered_Adviser.AnsweredItem;
import com.gome.haoyuangong.layout.self.data.Answered_Adviser.Data;
import com.gome.haoyuangong.layout.self.data.AttentionList.AttentionItem;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.url.NetUrlMyInfo;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.presenter.IAskListPresenter;
import com.gome.haoyuangong.utils.DateUtils;
import com.gome.haoyuangong.views.TagTextView;

public class MyConsultUnAnswered extends ListViewFragment {	
	LayoutInflater inflater;
	ViewGroup container;
	ImageLoader imageLoader;
	int recid=Integer.MAX_VALUE;
	String direction="f";
	int requestCount=Integer.MAX_VALUE;
	String userId;
	UserType userType=UserType.utNone;
	ArrayList<AnsweredItem> adviserItems;
	ArrayList<Answered_User.AnsweredItem> userAnsweredItems;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.inflater = inflater;
		this.container = container;		
//		adviserItems = new ArrayList<Answered_Adviser.AnsweredItem>();
//		userAnsweredItems = new ArrayList<Answered_User.AnsweredItem>();
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null){
			userId = args.getString("viewid");
			userType = UserType.values()[args.getInt("usertype", -1)];
		}
		setDividerHeight(0);
		imageLoader = new ImageLoader(getContext());
		
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		requestData(true);		
	}
	private String getUserId(){
		if (userType == UserType.utUserViewAdviser)
			return userId;
		else {
			return UserInfo.getInstance().getUserId();
		}
	}
	protected void requestData(boolean pull){
		if (userType == UserType.utUserViewAdviser || (UserInfo.getInstance().isLogin() && UserInfo.getInstance().isTougu()))
			requestAdviserData();
		else if (UserInfo.getInstance().isLogin() && !UserInfo.getInstance().isTougu())
			requestUserData();
	}
	private void requestUserData(){
		String url = null;
		if (recid == Integer.MAX_VALUE)
			url = String.format(NetUrlMyInfo.USER_ANSWER, getUserId(),2,requestCount,direction,null);
		else
			url = String.format(NetUrlMyInfo.USER_ANSWER, getUserId(),2,requestCount,direction,String.valueOf(recid));
		JsonRequest<Answered_User> request = new JsonRequest<Answered_User>(Method.GET, url,
				new RequestHandlerListener<Answered_User>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						if (showloading)
							showLoading(request);
						// showDialog(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						if (showloading)
							hideLoading(request);
						stopRefresh();
						stopLoadMore();
					}

					@Override
					public void onSuccess(String id, Answered_User data) {
						// TODO Auto-generated method stub
						stopLoadMore();
						stopRefresh();
						if (data.getData().getList()==null || data.getData().getList().size() == 0){							
							setPullLoadEnable(false);
							showEmptyView();
							setEmptyText("暂无提问，您可以向所有投顾提问哦");
							return;
						}
						showDataView();
//						if (recid == Integer.MAX_VALUE)
							clear();
						fillUserData(data.getData());
						setPullLoadEnable(false);
						userAnsweredItems = data.getData().getList();
						recid = data.getData().getList().get(data.getData().getList().size()-1).getAskId();
						reFresh();
					}
				}, Answered_User.class);
			((BaseActivity)getActivity()).send(request);
	}
	private void fillUserData(com.gome.haoyuangong.layout.self.data.Answered_User.Data data){
		
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		p.setMargins(0, 0, 0, 1);
		for(int i=0;i<data.getList().size();i++){	
			LinearLayout layout = new LinearLayout(getContext());
			layout.setBackgroundColor(0xffe9e9e9);
			layout.setOrientation(LinearLayout.VERTICAL);
			final Answered_User.AnsweredItem item = data.getList().get(i);
			View view = inflater.inflate(R.layout.unanwser_user, container,false);
			((TextView)view.findViewById(R.id.textViewName)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ActivityChange.ToAdviserHome(getActivity(), item.getAnswerUsername(), 
							item.getAnswserUserId());
				}
			});
			if (item.getLastedAnswer() != null){
				if (item.getLastedAnswer().getAdviserUser() != null){
					((TextView)view.findViewById(R.id.evaluateTextView)).setText("我追问");
					((TextView)view.findViewById(R.id.textViewName)).setText(item.getLastedAnswer().getAdviserUser().getUserName()+":");	
					if (item.getLastedAnswer().getAgainAskVo() == null){
						Logger.info("user-unanswer", "item.getLastedAnswer().getAgainAskVo() == null");
						((TextView)view.findViewById(R.id.textViewContent)).setText("");
					}
					else
						((TextView)view.findViewById(R.id.textViewContent)).setText(item.getLastedAnswer().getAgainAskVo().getContent());
				}
			}
			else {
				((TextView)view.findViewById(R.id.textViewName)).setText(item.getAnswerUsername()+":");
				((TextView)view.findViewById(R.id.textViewContent)).setText(Html.fromHtml(item.getContent()));
			}
			((TextView)view.findViewById(R.id.textCompany)).setText(DateUtils.getTimeAgoString(item.getCtime(), "MM-dd HH:mm"));
			
			layout.addView(view,p);	
			layout.setTag(item);
			layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					Intent aIntent = new Intent(getActivity(), com.jrj.tougu.activity.AskDetailActivity_.class);
//					aIntent.putExtra(AskDetailActivity.BUNDLE_Id, item.getAskId());
//					startActivity(aIntent);
				}
			});
			addItem(layout);
		}
		
	}
	private void requestAdviserData() {
		String url = null;
		if (recid == Integer.MAX_VALUE)
			url= String.format(NetUrlMyInfo.ANSWERDED_ADIVISTER, getUserId(),2,requestCount,direction,"");
		else
			url = String.format(NetUrlMyInfo.ANSWERDED_ADIVISTER, getUserId(),2,requestCount,direction,"aid="+String.valueOf(recid));
		JsonRequest<Answered_Adviser> request = new JsonRequest<Answered_Adviser>(Method.GET, url,
				new RequestHandlerListener<Answered_Adviser>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						if (showloading)
							showLoading(request);
					}

					@Override
					public void onEnd(Request request) {
						if (showloading)
							hideLoading(request);
						stopRefresh();
						stopLoadMore();
					}

					@Override
					public void onSuccess(String id, Answered_Adviser data) {
						// TODO Auto-generated method stub
						stopLoadMore();
						stopRefresh();
						if (data.getData().getList()==null || data.getData().getList().size() == 0){							
							setPullLoadEnable(false);
							showEmptyView();
							setEmptyText("暂无未回答的问题");
							return;
						}
						else if (data.getData().getList().size() < requestCount){
							setPullLoadEnable(false);
						}
						else {
							setPullLoadEnable(true);
						}
						showDataView();
//						if (recid == Integer.MAX_VALUE)
							clear();					
						adviserItems = data.getData().getList();
						fillAdviserData(data.getData());
						recid = data.getData().getList().get(data.getData().getList().size()-1).getAskId();
						reFresh();
					}
				}, Answered_Adviser.class);
			((BaseActivity)getActivity()).send(request);
	}
	private void fillAdviserData(Data data){
		
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		p.setMargins(0, 0, 0, 1);
		for(int i=0;i<data.getList().size();i++){	
			LinearLayout layout = new LinearLayout(getContext());
			layout.setBackgroundColor(0xffe9e9e9);
			layout.setOrientation(LinearLayout.VERTICAL);
			final AnsweredItem item = data.getList().get(i);			
			View view = inflater.inflate(R.layout.unanswer_adviser, container,false);
//			ImageView fView = (ImageView)view.findViewById(R.id.imageView2);
//			if (fView != null){
//				if (item.getAskUserType() == 1)
//					fView.setVisibility(View.INVISIBLE);
//				else
//					fView.setVisibility(View.VISIBLE);
//			}
			TextView nameView = (TextView)view.findViewById(R.id.textViewName);
			nameView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (item.getAskUserType() == 0){
						ActivityChange.ToUserHome(getActivity(), item.getAusername(), 
								item.getAuserId(),"");
					}
					else {
						ActivityChange.ToAdviserHome(getActivity(), item.getAusername(), 
								item.getAuserId());
					}
				}
			});
			nameView.setText(item.getAusername());
			TextView dateView = (TextView)view.findViewById(R.id.textCompany);
			dateView.setText(DateUtils.format(item.getCtime(), "MM-dd HH:mm"));
			ImageView imageView = (ImageView)view.findViewById(R.id.imageViewHead);
			imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (item.getAskUserType() == 0){
						ActivityChange.ToUserHome(getActivity(), item.getAusername(), 
								item.getAuserId(),"");
					}
					else {
						ActivityChange.ToAdviserHome(getActivity(), item.getAusername(), 
								item.getAuserId());
					}
				}
			});
			imageLoader.downLoadImage(item.getHeadImages(), imageView);
			imageView.setTag(item);
			imageView.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (v instanceof ImageView){
						AnsweredItem answeredItem = (AnsweredItem)v.getTag();
						Intent intent = new Intent();
						intent.putExtra("title", answeredItem.getAusername());
						intent.putExtra("id", answeredItem.getAuserId());						
						intent.setClass(getActivity(), ViewSignUserInfoActivity.class);
						startActivity(intent);
					}
				}
			});
//			imageLoader.downLoadImage(url, imgView);
			TextView contentView = (TextView)view.findViewById(R.id.textViewContent);
			if (item.getLastedAnswer() != null){
				if (item.getLastedAnswer().getAgainAskVo() != null){
					((TextView)view.findViewById(R.id.evaluateTextView)).setText("追问");
					contentView.setText(Html.fromHtml(item.getLastedAnswer().getAgainAskVo().getContent()));
				}
				else
					contentView.setText(Html.fromHtml(item.getContent()));
			}
			else
				contentView.setText(Html.fromHtml(item.getContent()));
			
			TagTextView button = (TagTextView)view.findViewById(R.id.textViewAnswer);
			if (!getUserId().equals(UserInfo.getInstance().getUserId())){
				button.setVisibility(View.GONE);
			}
			button.setTag(item);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AnsweredItem answeredItem = (AnsweredItem)((TagTextView)v).getTag();
					ReplyMediaRecordActivity.goToAnswerReply(getActivity(), answeredItem.getAskId());
				}
			});
			
			layout.addView(view,p);		
			layout.setTag(item);
			layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					Intent aIntent = new Intent(getActivity(), com.jrj.tougu.activity.AskDetailActivity_.class);
//					aIntent.putExtra(AskDetailActivity.BUNDLE_Id, item.getAskId());
//					startActivity(aIntent);
				}
			});
			addItem(layout);
		}
		
	}
	private View getItemView(int id) {
		View v=null;
		int index = -1;
		if (userType == UserType.utUser){
			for(int i=0;i<getCount();i++){
				LinearLayout layout = getItemAt(i);
				if (layout == null)
					continue;
				Answered_User.AnsweredItem item = (Answered_User.AnsweredItem)layout.getTag();
				if (item.getAskId() != id)
					continue;
				index = i;
				break;
			}
		}
		else{
			for(int i=0;i<getCount();i++){
				LinearLayout layout = getItemAt(i);
				if (layout == null)
					continue;
				AnsweredItem item = (AnsweredItem)layout.getTag();
				if (item.getAskId() != id)
					continue;
				index = i;
				break;
			}
		}
		LinearLayout layout = getItemAt(index);
		if (layout == null)
			return null;
		return layout;
	}
	  private BroadcastReceiver mReceiver = new BroadcastReceiver() {  
		  
	        @Override  
	        public void onReceive(Context context, Intent intent) {  
	            if (IAskListPresenter.ACTION_ASK_REFRESH.equals(intent.getAction()))  {
	            	int id = intent.getIntExtra(IAskListPresenter.BUNDLE_ASKID, 0);
	            	int type = intent.getIntExtra(IAskListPresenter.BUNDLE_STATUS, 0);
	            	if (type == IAskListPresenter.ANSWER_STATUS_ANSWERED){
	            		View view = getItemView(id);
	            		if (view == null)
	            			return;
	            		remove(view);
	            		reFresh();
	            	}	            	
	            }
	        }  
	    }; 
	    @Override
		public void OnListViewItemClick(AdapterView<?> parent, View view,
				int position, long id) {
//			// TODO Auto-generated method stub
//			super.OnListViewItemClick(parent, view, position, id);
//			if (view.getTag() == null)
//				return;
//			Intent aIntent = new Intent(getActivity(), com.jrj.tougu.activity.AskDetailActivity_.class);
//			if (userType == UserType.utUser){
//				Answered_User.AnsweredItem item = (Answered_User.AnsweredItem)view.getTag();
//				aIntent.putExtra(AskDetailActivity.BUNDLE_Id, item.getAskId());
//			}
//			else{
//				AnsweredItem item = (AnsweredItem)view.getTag();
//				aIntent.putExtra(AskDetailActivity.BUNDLE_Id, item.getAskId());
//			}
//			startActivity(aIntent);
		}
	@Override
	public void OnStartLoadMore() {
		// TODO Auto-generated method stub
		super.OnStartLoadMore();
		direction = "f";
//		requestCount = 20;
		showloading = false;
		requestData(false);
	}
	@Override
	public void OnStartRefresh() {
		// TODO Auto-generated method stub
		super.OnStartRefresh();
		recid = Integer.MAX_VALUE;
		direction = "f";
		showloading = false;
		if (adviserItems != null)
			adviserItems.clear();
		if (userAnsweredItems != null)
			userAnsweredItems.clear();
//		requestCount = Integer.MAX_VALUE;
		requestData(true);
	}
	public void onResume() {
    	super.onResume();
    	getActivity().registerReceiver(mReceiver, new IntentFilter(IAskListPresenter.ACTION_ASK_REFRESH));  
    };
    @Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	getActivity().unregisterReceiver(mReceiver);
    }
}
