package com.gome.haoyuangong.fragments;

import java.util.ArrayList;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.BaseViewImpl;
import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.activity.AskDetailActivity;
import com.gome.haoyuangong.activity.BaseActivity;
import com.gome.haoyuangong.activity.ReplyMediaRecordActivity;
import com.gome.haoyuangong.layout.self.ActivityChange;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.layout.self.SelfView.UserType;
import com.gome.haoyuangong.layout.self.data.Answered_Adviser;
import com.gome.haoyuangong.layout.self.data.Answered_User;
import com.gome.haoyuangong.layout.self.data.Answered_Adviser.AnsweredItem;
import com.gome.haoyuangong.layout.self.data.Answered_Adviser.Data;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.mediarecorder.SoundRecorder;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.url.NetUrlMyInfo;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.presenter.IAskListPresenter;
import com.gome.haoyuangong.utils.DateUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.ProgressView;
import com.gome.haoyuangong.views.TagTextView;

public class MyConsultAnswered extends ListViewFragment {
	MyAdapter adapter;
	LayoutInflater inflater;
	ViewGroup container;
	ImageLoader imageLoader;
	int recid=0;
	String direction="f";
	int requestCount=20;
	String userId,userName;
	UserType userType=UserType.utNone;
	ArrayList<AnsweredItem> answeredItems;
	ArrayList<Answered_User.AnsweredItem> userAnsweredItems;
	int firstRecordId = Integer.MIN_VALUE;
	int maxTime = (int) (SoundRecorder.MAX_SOUND_SIZE / 1000);
	private AskDetailEx mIAskListPresenter = new AskDetailEx(this) {

		public void onPalyFinish() {
			adapter.notifyDataSetChanged();
		};
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.inflater = inflater;
		this.container = container;	
		answeredItems = new ArrayList<Answered_Adviser.AnsweredItem>();
		userAnsweredItems = new ArrayList<Answered_User.AnsweredItem>();
		imageLoader = new ImageLoader(getContext());
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		mIAskListPresenter.initVoice();
		adapter = new MyAdapter();
		setAdapter(adapter);
		requestData(false);
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		setDividerHeight(0);
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null){
			userId = args.getString("viewid");			
			userType = UserType.values()[args.getInt("usertype", -1)];
			if (userType != UserType.utUserViewAdviser)
				userName = "我";
			else
				userName = args.getString("viewname");
		}
		else{
			userName = "我";
		}
	}
	
	protected void requestData(final boolean pull) {
//		requestUserData();		
		if (userType == UserType.utUserViewAdviser || (UserInfo.getInstance().isLogin() && UserInfo.getInstance().isTougu()))
			requestAdviserData(pull);
		else if (UserInfo.getInstance().isLogin() && !UserInfo.getInstance().isTougu())
			requestUserData(pull);
	}
	private String getUserId(){
		if (userType == UserType.utUserViewAdviser)
			return userId;
		else {
			return UserInfo.getInstance().getUserId();
		}
	}
	private void requestAdviserData(final boolean pull) {
		String url="";
		if (recid == 0)
			url = String.format(NetUrlMyInfo.ANSWERDED_ADIVISTER, getUserId(),1,requestCount,direction,null);
		else{
			if (direction.equals("f"))
				url = String.format(NetUrlMyInfo.ANSWERDED_ADIVISTER, getUserId(),1,requestCount,direction,"aid="+String.valueOf(recid));
			else
				url = String.format(NetUrlMyInfo.ANSWERDED_ADIVISTER, getUserId(),1,requestCount,direction,"aid="+String.valueOf(firstRecordId));
		}
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
						super.onEnd(request);
						if (showloading)
							hideLoading(request);
						stopRefresh();
						stopLoadMore();
					}

					@Override
					public void onSuccess(String id, Answered_Adviser data) {
						// TODO Auto-generated method stub
						
						stopLoadMore();
						if (pull)
							stopRefresh();
						if (data.getData().getList()==null || data.getData().getList().size() == 0){							
							setPullLoadEnable(false);
							if (recid == 0){								
								showEmptyView();
								setEmptyText("暂无已回答的问题，您可以到最新提问列表中回答提问哦");
							}
							return;
						}
						else if (data.getData().getList().size() < requestCount)
							setPullLoadEnable(false);
						else {
							setPullLoadEnable(true);
						}		
//						showDataView();
						if (recid == 0){
							clear();
							answeredItems.clear();
						}
						if (firstRecordId <data.getData().getList().get(0).getAskId()){
							firstRecordId = data.getData().getList().get(0).getAskId();
						}
//						fillAdviserData(data.getData());
						if (direction.equals("f"))
							answeredItems.addAll(data.getData().getList());
						else
							answeredItems = data.getData().getList();
						recid = data.getData().getList().get(data.getData().getList().size()-1).getAskId();
						adapter.notifyDataSetChanged();	
					}
				}, Answered_Adviser.class);
			((BaseActivity)getActivity()).send(request);
	}
	private void setVoiceLayout(View v, final String url, int time) {
		View answerMediaLy = v.findViewById(R.id.ask_item_media_ly);
		View answerMediaSpace = v.findViewById(R.id.ask_item_media_space);
		TextView mediaTime = (TextView)v.findViewById(R.id.ask_item_media_time);
		LayoutParams params = (LayoutParams) answerMediaLy.getLayoutParams();
		LayoutParams params1 = (LayoutParams) answerMediaSpace.getLayoutParams();
		params.weight = time;
		params1.weight = maxTime - time;
		answerMediaLy.setLayoutParams(params);
		answerMediaSpace.setLayoutParams(params1);
		mediaTime.setText(getString(R.string.timer_format, time));
		final ImageView mediaVUMeter = (ImageView)v.findViewById(R.id.ask_item_media_vum_right);
		final AnimationDrawable anim = (AnimationDrawable) mediaVUMeter.getBackground();		
		ProgressView mProgressView = (ProgressView) mediaVUMeter.getTag();
		if (mProgressView == null) {
			mProgressView = new com.gome.haoyuangong.views.ProgressView(getContext(), mediaVUMeter);
			mediaVUMeter.setTag(mProgressView);
		}
		if (mIAskListPresenter.isPlaying(url)) {
			anim.start();
		} else {
			anim.stop();
			anim.selectDrawable(0);
		}
		final ProgressView PV = mProgressView;
		answerMediaLy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				mIAskListPresenter.mediaPlayClick(url, anim, PV);
				mIAskListPresenter.setTag(anim);
			}
		});

	}
	private void requestUserData(final boolean pull){
		String url = null;
		if (recid == Integer.MAX_VALUE)
			url = String.format(NetUrlMyInfo.USER_ANSWER, getUserId(),1,requestCount,direction,null);
		else{
			if (direction.equals("f"))
				url = String.format(NetUrlMyInfo.USER_ANSWER, getUserId(),1,requestCount,direction,recid==0?"":"aid="+String.valueOf(recid));
			else
				url = String.format(NetUrlMyInfo.USER_ANSWER, getUserId(),1,requestCount,direction,"aid="+String.valueOf(firstRecordId));
		}
		JsonRequest<Answered_User> request = new JsonRequest<Answered_User>(Method.GET, url,
				new RequestHandlerListener<Answered_User>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						if (showloading)
							showLoading(request);
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
						if (pull)
							stopRefresh();
						if (data.getData().getList()==null || data.getData().getList().size() == 0){							
							setPullLoadEnable(false);
							if (recid == 0){
								showEmptyView();
								setEmptyText("暂无已回答的问题");
							}
							return;
						}
						else if (data.getData().getList().size() < requestCount)
							setPullLoadEnable(false);
						else {
							setPullLoadEnable(true);
						}
						showDataView();
						if (recid == 0){
							clear();
							userAnsweredItems.clear();
						}
						if (firstRecordId <data.getData().getList().get(0).getAskId()){
							firstRecordId = data.getData().getList().get(0).getAskId();
						}
//						fillUserData(data.getData());
						if (direction.equals("f"))
							userAnsweredItems.addAll(data.getData().getList());
						else
							userAnsweredItems = data.getData().getList();
						recid = data.getData().getList().get(data.getData().getList().size()-1).getAskId();
//						reFresh();
						adapter.notifyDataSetChanged();
					}
				}, Answered_User.class);
			((BaseActivity)getActivity()).send(request);
	}
	@Override
	public void OnStartLoadMore() {
		// TODO Auto-generated method stub
		super.OnStartLoadMore();
		direction = "f";
		requestCount = 20;
		showloading = false;
		requestData(false);
	}
	@Override
	public void OnStartRefresh() {
		// TODO Auto-generated method stub
		super.OnStartRefresh();
//		recid = Integer.MAX_VALUE;
		direction = "f";
		requestCount = 20;
		recid = 0;
		showloading = false;
//		if (answeredItems != null)
//			answeredItems.clear();
//		if (userAnsweredItems != null)
//			userAnsweredItems.clear();
		requestData(true);
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mIAskListPresenter.stopAll();
	}
	private int getItemIndex(int id,int type){
		View v=null;
		int index = -1;
		if (userType == UserType.utUser){
			for(int i=0;i<userAnsweredItems.size();i++){	
				if (type == IAskListPresenter.ANSWER_STATU_EVALUATED){
					if (userAnsweredItems.get(i).getLastedAnswer().getAnswerId() != id)
						continue;
				}
				else{
					if (userAnsweredItems.get(i).getAskId() != id)
						continue;
				}
				index = i;
				break;
			}
		}
		return index;
	}
	
	  private BroadcastReceiver mReceiver = new BroadcastReceiver() {  
		  
	        @Override  
	        public void onReceive(Context context, Intent intent) {  
	            if (IAskListPresenter.ACTION_ASK_REFRESH.equals(intent.getAction()))  {
	            	int type = intent.getIntExtra(IAskListPresenter.BUNDLE_STATUS, 0);	            	
	            	if (type == IAskListPresenter.ANSWER_STATU_EVALUATED){
	            		int id = intent.getIntExtra(IAskListPresenter.BUNDLE_ANSWERID, 0);	            	
		            	int rating = intent.getIntExtra(IAskListPresenter.BUNDLE_EVALUATE_RATING, 0);
		            	String content = intent.getStringExtra(IAskListPresenter.BUNDLE_EVALUATE_CONTENT);
		            	int index = getItemIndex(id, type);
		            	if (index >= 0){
		            		userAnsweredItems.get(index).getLastedAnswer().setEvaluate(rating);
		            		userAnsweredItems.get(index).getLastedAnswer().setEvaContent(content);
		            		adapter.notifyDataSetChanged();
		            	}
	            	}
	            	else if (type == IAskListPresenter.ANSWER_STATU_REASK){
	            		int id = intent.getIntExtra(IAskListPresenter.BUNDLE_ASKID, 0);	
	            		if (id == 0)
	            			return;
	            		int index = getItemIndex(id, type);
	            		if (index >= 0){
	            			userAnsweredItems.remove(index);
	            			adapter.notifyDataSetChanged();
	            		}
	            	}
	            }
	        }  
	    }; 
	   
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
	    
	    private class AskDetailEx extends IAskListPresenter{
	    	Object tag;
			public AskDetailEx(BaseViewImpl vImpl) {
				super(vImpl);
				// TODO Auto-generated constructor stub
			}
			public Object getTag() {
				return tag;
			}
			public void setTag(Object tag) {
				this.tag = tag;
			}	
	    }
	    
	    private class MyAdapter extends BaseAdapter{

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				int count = 0;
				if (userType == UserType.utUserViewAdviser || (UserInfo.getInstance().isLogin() && UserInfo.getInstance().isTougu()))
					count = answeredItems.size();
				else if (UserInfo.getInstance().isLogin() && !UserInfo.getInstance().isTougu())
					count = userAnsweredItems.size();
				return count;
			}

			@Override
			public Object getItem(int position) {
				if (userType == UserType.utUserViewAdviser || (UserInfo.getInstance().isLogin() && UserInfo.getInstance().isTougu()))
					return answeredItems.get(position);
				else if (UserInfo.getInstance().isLogin() && !UserInfo.getInstance().isTougu())
					return userAnsweredItems.get(position);
				return null;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (userType == UserType.utUserViewAdviser || (UserInfo.getInstance().isLogin() && UserInfo.getInstance().isTougu()))
					convertView = getAdviserView(position, convertView, parent);
				else
					convertView = getUserView(position, convertView, parent);
				return convertView;
			}
			
			private View getAdviserView(int position, View convertView, ViewGroup parent){
				final ViewHolderAdviser holder;
				if (convertView == null){
					convertView = inflater.inflate(R.layout.answer_adviser, parent, false);
					holder = new ViewHolderAdviser();					
					holder.mainLayout = (LinearLayout)convertView.findViewById(R.id.mainlayout);
					holder.headerView = (ImageView)convertView.findViewById(R.id.imageViewHead);
					holder.nameView = (TextView)convertView.findViewById(R.id.textViewName);
					holder.textView1 = (TextView)convertView.findViewById(R.id.textView2);
					holder.dateView = (TextView)convertView.findViewById(R.id.textCompany);
					holder.view1 = (TextView)convertView.findViewById(R.id.textViewContent);
					holder.view2 = (TextView)convertView.findViewById(R.id.againAnswerView);
					holder.view3 = (TextView)convertView.findViewById(R.id.srcaskText);
					holder.view4 = (TextView)convertView.findViewById(R.id.againAskView);
					holder.countView1 = (TextView)convertView.findViewById(R.id.textView3);
					holder.countView2 = (TextView)convertView.findViewById(R.id.textView4);
					holder.evaluateLayout = (LinearLayout)convertView.findViewById(R.id.evaluatelayout);
					holder.ratingBar = (RatingBar)convertView.findViewById(R.id.ratingBar);
					holder.evaluateView = (TextView)convertView.findViewById(R.id.evaluateTextView);
					holder.layout3 = (RelativeLayout)convertView.findViewById(R.id.asklayout);
					holder.voiceView1 = convertView.findViewById(R.id.ask_adviser_media1);
					holder.voiceView2 = convertView.findViewById(R.id.ask_adviser_media2);
					convertView.setTag(holder);
				}
				else {
					holder = (ViewHolderAdviser)convertView.getTag();
				}
				
				final AnsweredItem item = answeredItems.get(position);
				if (item.getLastedAnswer() == null){
					Logger.info("getAdviserView", "getLastedAnswer为空:askId="+String.valueOf(item.getAskId()));
				}
				holder.askId = item.getAskId();
				if (item.getLastedAnswer() != null && item.getLastedAnswer().getEvaluate() > 0){
					holder.evaluateLayout.setVisibility(View.VISIBLE);
					holder.ratingBar.setRating(item.getLastedAnswer().getEvaluate());
					if (TextUtils.isEmpty(item.getLastedAnswer().getEvaContent()))
						holder.evaluateView.setText("评价：暂无评价");
					else
						holder.evaluateView.setText("评价："+item.getLastedAnswer().getEvaContent());
				}
				else {						
					holder.evaluateLayout.setVisibility(View.GONE);
				}
				ImageView imageView = holder.headerView;
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
				String askerName = item.getAusername();
//				holder.countView2.setText(String.valueOf(item.getAnswerTimes())+"人回答");
				holder.nameView.setOnClickListener(new OnClickListener() {
					
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
				holder.countView2.setVisibility(View.GONE);
				holder.countView1.setVisibility(View.GONE);
				holder.voiceView1.setVisibility(View.GONE);
				holder.voiceView2.setVisibility(View.GONE);
				//有追问				
				if (item.getLastedAnswer().getAgainAskVo() != null){
					holder.countView2.setVisibility(View.GONE);
					holder.countView1.setVisibility(View.VISIBLE);
					holder.countView1.setText(String.valueOf(item.getAnswerTimes())+"人回答");
					holder.nameView.setText(askerName);
					holder.textView1.setText("追问");
					holder.view1.setText(item.getLastedAnswer().getAgainAskVo().getContent());
					String str = DateUtils.getTimeAgoString(item.getLastedAnswer().getAgainAskVo().getAgainanswerCtime(),"MM-dd HH:mm"); 
					holder.dateView.setText(str);
					
					str = userName+"回答"+askerName+"追问："+item.getLastedAnswer().getAgainAskVo().getAgainanswerContent();
					SpannableStringBuilder builder = new SpannableStringBuilder(str);
					ForegroundColorSpan span = new ForegroundColorSpan(getContext().getResources().getColor(R.color.font_4c87c6));
					builder.setSpan(span, userName.length()+2, userName.length()+2+askerName.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
					holder.view4.setText(builder);
					
					str = askerName+"问："+item.getLastedAnswer().getContent();
					builder = new SpannableStringBuilder(str);
					span = new ForegroundColorSpan(getContext().getResources().getColor(R.color.font_4c87c6));
					builder.setSpan(span, 0, askerName.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
					holder.view3.setText(builder);
					
//					str = userName+"回答"+askerName+"追问："+item.getLastedAnswer().getAgainAskVo().getAgainanswerContent();
//					builder = new SpannableStringBuilder(str);
//					span = new ForegroundColorSpan(getContext().getResources().getColor(R.color.font_4c87c6));
//					builder.setSpan(span, userName.length()+2, userName.length()+2+askerName.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//					holder.view4.setText(builder);
					//有追问语音
					if (item.getLastedAnswer().getAgainAskVo().getAgainanswerVoicelength() > 0){
						holder.voiceView2.setVisibility(View.VISIBLE);
						setVoiceLayout(holder.voiceView2, item.getLastedAnswer().getAgainAskVo().getAgainanswerVoiceAmr(), item.getLastedAnswer().getAgainAskVo().getAgainanswerVoicelength());
					}
					holder.view4.setVisibility(View.VISIBLE);
					holder.layout3.setVisibility(View.VISIBLE);
				}
				else {
					holder.countView2.setVisibility(View.VISIBLE);
					holder.view4.setVisibility(View.GONE);
					holder.layout3.setVisibility(View.GONE);	
				}
				if (item.getLastedAnswer() != null){
					//有语音
					if (item.getLastedAnswer().getVoicelength() > 0){
						holder.voiceView1.setVisibility(View.VISIBLE);
						setVoiceLayout(holder.voiceView1, item.getLastedAnswer().getVoiceAmr(), item.getLastedAnswer().getVoicelength());
					}
//					holder.countView2.setVisibility(View.VISIBLE);
//					holder.countView2.setText(String.valueOf(item.getAnswerTimes())+"人回答");
					holder.nameView.setText(askerName);
					if (item.getLastedAnswer().getAgainAskVo() != null)
						holder.view3.setText(item.getContent());
					else
						holder.view1.setText(item.getContent());
					String str = DateUtils.getTimeAgoString(item.getCtime(),"MM-dd HH:mm"); 
					holder.dateView.setText(str);
					
					str = userName+"回答"+askerName+"："+item.getLastedAnswer().getContent();
					SpannableStringBuilder builder = new SpannableStringBuilder(str);
					ForegroundColorSpan span = new ForegroundColorSpan(getContext().getResources().getColor(R.color.font_4c87c6));
					builder.setSpan(span, userName.length()+2, userName.length()+2+askerName.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
					holder.view2.setText(builder);

//					holder.view4.setVisibility(View.GONE);
//					holder.layout3.setVisibility(View.GONE);	
				}
				
				holder.mainLayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
//						Intent aIntent = new Intent(getActivity(), com.jrj.tougu.activity.AskDetailActivity_.class);
//						aIntent.putExtra(AskDetailActivity.BUNDLE_Id, holder.askId);
//						startActivity(aIntent);
					}
				});
				return convertView;
			}
			private View getUserView(final int position, View convertView, ViewGroup parent){
				final ViewHolderUser holder;
				if (convertView == null){
					convertView = inflater.inflate(R.layout.answer_user, parent, false);
					holder = new ViewHolderUser();					
					holder.mainLayout = (LinearLayout)convertView.findViewById(R.id.mainlayout);
					holder.headerView = (ImageView)convertView.findViewById(R.id.imageViewHead);
					holder.companyView =(TextView)convertView.findViewById(R.id.textCompany);
					holder.nameView = (TextView)convertView.findViewById(R.id.textViewName);
					holder.iconView = (ImageView)convertView.findViewById(R.id.imageView4);
					holder.dateView = (TextView)convertView.findViewById(R.id.textViewDate);
					holder.view1 = (TextView)convertView.findViewById(R.id.textViewContent);
					holder.view2 = (TextView)convertView.findViewById(R.id.askTextView);					
					holder.view3 = (TextView)convertView.findViewById(R.id.againAnswerView);
					holder.view4 = (TextView)convertView.findViewById(R.id.againAskView);
					holder.evaluateLayout = (LinearLayout)convertView.findViewById(R.id.evaluatelayout);
					holder.ratingBar = (RatingBar)convertView.findViewById(R.id.ratingBar);
					holder.evaluateView = (TextView)convertView.findViewById(R.id.evaluateTextView);
					holder.reAskView = (TextView)convertView.findViewById(R.id.textView1);
					holder.voiceView1 = convertView.findViewById(R.id.ask_adviser_media1);
					holder.voiceView2 = convertView.findViewById(R.id.ask_adviser_media2);
					holder.bottomlayout = (LinearLayout)convertView.findViewById(R.id.bottomlayout);
					holder.layout2 = (LinearLayout)convertView.findViewById(R.id.layout2);
					holder.layout4 = (LinearLayout)convertView.findViewById(R.id.layout4);
					holder.reAskImageView = (ImageView)convertView.findViewById(R.id.imageView2);
					holder.evaluaTextView = (TextView)convertView.findViewById(R.id.textView4);
					convertView.setTag(holder);
				}
				else {
					holder = (ViewHolderUser)convertView.getTag();
				}
				
				String adviserName="";
				final com.gome.haoyuangong.layout.self.data.Answered_User.AnsweredItem item = userAnsweredItems.get(position);
				if (item.getLastedAnswer() == null){
					Logger.info("user-answer", "item.getLastedAnswer() == null");
				}
				holder.askId = item.getAskId();
				View view = inflater.inflate(R.layout.answer_user, container,false);
				
				holder.reAskView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (item.getLastedAnswer().getAdviserUser() == null){
						Toast.makeText(getActivity(), "没有获得回答的投顾信息", Toast.LENGTH_SHORT).show();
						return;
					}					
						ReplyMediaRecordActivity.goToReAsk(getActivity(), item.getAskId(), item.getLastedAnswer().getAnswerId(), item.getLastedAnswer().getAdviserUser().getUserId());
					}
				});
				
				holder.evaluaTextView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ReplyMediaRecordActivity.goToEvaluate(getActivity(), item.getLastedAnswer().getAnswerId());
					}
				});
				ImageView imageView = holder.headerView;
				imageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ActivityChange.ToAdviserHome(getActivity(), item.getLastedAnswer().getAdviserUser().getUserName(), 
								item.getLastedAnswer().getAdviserUser().getUserId());
					}
				});
				String dateString = DateUtils.getTimeAgoString(item.getCtime(),"MM-dd HH:mm");
				holder.nameView.setText(dateString);
				if (item.getLastedAnswer() != null && item.getLastedAnswer().getAdviserUser() != null){
					adviserName = item.getLastedAnswer().getAdviserUser().getUserName();
					holder.nameView.setText(adviserName);
					//显示身份
					holder.companyView.setText(item.getLastedAnswer().getAdviserUser().getTypeDesc()+" "+(StringUtils.isEmpty(item.getLastedAnswer().getAdviserUser().getCompany())?"":item.getLastedAnswer().getAdviserUser().getCompany()));
					imageLoader.downLoadImage(item.getLastedAnswer().getAdviserUser().getHeadImage(), imageView);
				}
				else
					imageLoader.downLoadImage(null, imageView);
							
				holder.view2.setText("我问："+Html.fromHtml(item.getContent()));
				
				if (item.getLastedAnswer() != null){	
					if (item.getLastedAnswer().getEvaluate() > 0){
						holder.evaluateLayout.setVisibility(View.VISIBLE);
						holder.ratingBar.setRating(item.getLastedAnswer().getEvaluate());
						holder.evaluateView.setText("评价："+item.getLastedAnswer().getEvaContent());
						holder.bottomlayout.setVisibility(View.GONE);
					}
					else {						
						holder.evaluateLayout.setVisibility(View.GONE);
					}
					if (item.getLastedAnswer().getAgainAskVo() != null)
					{
						holder.view1.setText(item.getLastedAnswer().getAgainAskVo().getAgainanswerContent());
//						holder.view3.setText(item.getLastedAnswer().getContent());
					}
					else {
						holder.view1.setText(item.getLastedAnswer().getContent());
					}
					//有语音
					if (item.getLastedAnswer().getVoicelength() > 0){
						holder.view1.setVisibility(View.GONE);
						holder.voiceView1.setVisibility(View.VISIBLE);
						setVoiceLayout(holder.voiceView1, item.getLastedAnswer().getVoiceAmr(), item.getLastedAnswer().getVoicelength());
					}
					else
						holder.voiceView1.setVisibility(View.GONE);
					if (item.getLastedAnswer().getAgainAskVo() != null){
						holder.reAskImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_reask_d));
						holder.reAskView.setEnabled(false);
						holder.reAskView.setTextColor(getContext().getResources().getColor(R.color.font_595959));
						String string = "追问"+adviserName+":"+item.getLastedAnswer().getAgainAskVo().getContent();
						SpannableStringBuilder builder = new SpannableStringBuilder(string);
						ForegroundColorSpan span = new ForegroundColorSpan(getContext().getResources().getColor(R.color.font_4c87c6));
						builder.setSpan(span, 2, 2+adviserName.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
						holder.view4.setText(builder);
						String str = DateUtils.getTimeAgoString(item.getLastedAnswer().getAgainAskVo().getCtime(),"MM-dd HH:mm"); 
						holder.dateView.setText(str);
						//有追问语音
						if (item.getLastedAnswer().getAgainAskVo().getAgainanswerVoicelength() > 0){
							holder.voiceView2.setVisibility(View.VISIBLE);
							setVoiceLayout(holder.voiceView2, item.getLastedAnswer().getAgainAskVo().getAgainanswerVoiceAmr(), item.getLastedAnswer().getAgainAskVo().getAgainanswerVoicelength());
						}
						else
							holder.voiceView2.setVisibility(View.GONE);
						if (item.getLastedAnswer().getAgainAskVo().getHasAgainanswer() > 0){
//							string = adviserName+"回答："+item.getLastedAnswer().getAgainAskVo().getAgainanswerContent();
//							builder = new SpannableStringBuilder(string);
//							span = new ForegroundColorSpan(getContext().getResources().getColor(R.color.font_4c87c6));
//							builder.setSpan(span, 0, adviserName.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//							holder.view3.setText(builder);			
							string = adviserName+"回答："+item.getLastedAnswer().getContent();
							builder = new SpannableStringBuilder(string);
							span = new ForegroundColorSpan(getContext().getResources().getColor(R.color.font_4c87c6));
							builder.setSpan(span, 0, adviserName.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
							holder.view3.setText(builder);		
						}
						else{
//							((TextView)view.findViewById(R.id.againAnswerView)).setVisibility(View.GONE);
							((LinearLayout)view.findViewById(R.id.layout2)).setVisibility(View.GONE);
						}						
					}
					else{
						holder.reAskImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_reask_e));
						holder.reAskView.setEnabled(true);
						holder.reAskView.setTextColor(getContext().getResources().getColor(R.color.font_4c87c6));
//						((TextView)view.findViewById(R.id.againAskView)).setVisibility(View.GONE);
//						((TextView)view.findViewById(R.id.againAnswerView)).setVisibility(View.GONE);
						holder.layout2.setVisibility(View.GONE);
						holder.layout4.setVisibility(View.GONE);
						holder.voiceView2.setVisibility(View.GONE);
						String str = DateUtils.getTimeAgoString(item.getCtime(),"MM-dd HH:mm"); 
						holder.dateView.setText(str);
					}				
				}			
				holder.mainLayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
//						Intent aIntent = new Intent(getActivity(), com.jrj.tougu.activity.AskDetailActivity_.class);
//						aIntent.putExtra(AskDetailActivity.BUNDLE_Id, holder.askId);
//						startActivity(aIntent);
					}
				});
				return convertView;
			}			
	    }
	    private class ViewHolderAdviser{
	    	int askId;
	    	LinearLayout mainLayout;
	    	ImageView headerView;
	    	TextView nameView;
	    	TextView textView1;
	    	TextView dateView;
	    	TextView view1;
	    	TextView view2;
	    	TextView view3;
	    	TextView view4;
	    	TextView countView1;
	    	TextView countView2;
	    	LinearLayout evaluateLayout;
	    	RatingBar ratingBar;
	    	TextView evaluateView;
	    	RelativeLayout layout3;
	    	View voiceView1;
	    	View voiceView2;
	    }
	    private class ViewHolderUser{
	    	int askId;
	    	LinearLayout mainLayout;
	    	TextView companyView;
	    	ImageView headerView;
	    	ImageView iconView;
	    	TextView nameView;
	    	TextView dateView;
	    	TextView reAskView;
	    	ImageView reAskImageView;
	    	TextView evaluaTextView;
	    	TextView view1;
	    	TextView view2;
	    	TextView view3;
	    	TextView view4;
	    	TextView countView;
	    	LinearLayout evaluateLayout;
	    	RatingBar ratingBar;
	    	TextView evaluateView;
	    	View voiceView1;
	    	View voiceView2;
	    	LinearLayout bottomlayout;
	    	LinearLayout layout2;
	    	LinearLayout layout4;
	    }
}

