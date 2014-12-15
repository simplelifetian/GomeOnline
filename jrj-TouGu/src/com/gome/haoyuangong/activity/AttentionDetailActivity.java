/**
 * 
 */
package com.gome.haoyuangong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.ShareConstants;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.bean.Stock;
import com.gome.haoyuangong.db.QuoteDic;
import com.gome.haoyuangong.dialog.BottomShareDialog;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.tougu.CommentListBean;
import com.gome.haoyuangong.net.result.tougu.OpinionDetailBean;
import com.gome.haoyuangong.net.result.tougu.OptionLimitResult;
import com.gome.haoyuangong.net.result.tougu.PraiseResultBean;
import com.gome.haoyuangong.net.url.NetUrlComment;
import com.gome.haoyuangong.net.url.NetUrlTougu;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.DateUtils;
import com.gome.haoyuangong.utils.HtmlUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.xlistview.XListView;
import com.gome.haoyuangong.views.xlistview.XListView.IXListViewListener;
import com.jrj.sharesdk.CallbackListener;
import com.jrj.sharesdk.PType;
import com.jrj.sharesdk.ShareManager;
import com.jrj.sharesdk.msg.MsgImageText;

/**
 * 
 */
public class AttentionDetailActivity extends BaseActivity {

	private static final String TAG = AttentionDetailActivity.class.getName();
	
	public static final String BUNDLE_PARAM_DETAIL_URL = "detail_url";
	public static final String BUNDLE_PARAM_TITLE = "param_title";
	public static final String BUNDLE_PARAM_ID = "param_id";
//	public static final String BUNDLE_PARAM_COMMENT_C = "param_comments";
	public static final String BUNDLE_PARAM_PRAISE_C = "param_praise";
	
	public static final String BUNDLE_PARAM_TOUGUID = "BUNDLE_PARAM_TOUGUID";
	public static final String BUNDLE_PARAM_TOUGUNAME = "BUNDLE_PARAM_TOUGUNAME";
	public static final String BUNDLE_PARAM_OPTITLE = "BUNDLE_PARAM_OPTITLE";
	public static final String BUNDLE_PARAM_LIMIT = "BUNDLE_PARAM_LIMIT";
	
	public static final int COMMENT_REQUEST_CODE = 1011;
	
	public static final int PRAISE_REQUEST_CODE = 1012;
	
	private static final int PULL_REFRESH = 1;
	private static final int LOAD_MORE = 2;

	private XListView myListView;

	private List<CommentListBean.CommentData[]> commentData = new ArrayList<CommentListBean.CommentData[]>();

	private AttentionAdapter myAdapter;

	private ImageView share;
	private LinearLayout bottomLayout;
//	private LinearLayout zan;
	private TextView tvComment;
	private TextView tvZan;
	private TextView etComment;
	
	private AttentionViewHolder aViewHolder;
	
	private ImageLoader imageLoader;
	
	private String detailUrl;
	private int praiseCount;
	private long opinionId = -1;
	private int currPage = 0;
	private int pageSize = 20;
	
	private String touguId;
	private String touguName;
	private String opTitle;
	private int limit = 1;
	
	private OpinionDetailBean.Data itemData;
	private View juBao;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attention_detail_layout);
		setTitle("关注详情");
		opinionId = getIntent().getIntExtra(BUNDLE_PARAM_ID, -1);
		detailUrl = getIntent().getStringExtra(BUNDLE_PARAM_DETAIL_URL);
		praiseCount = getIntent().getIntExtra(BUNDLE_PARAM_PRAISE_C, 0);
		touguId = getIntent().getStringExtra(BUNDLE_PARAM_TOUGUID);
		touguName = getIntent().getStringExtra(BUNDLE_PARAM_TOUGUNAME);
		opTitle = getIntent().getStringExtra(BUNDLE_PARAM_OPTITLE);
		limit = getIntent().getIntExtra(BUNDLE_PARAM_LIMIT, 1);
		if( -1 == opinionId || StringUtils.isEmpty(detailUrl)){
			Toast.makeText(this, "错误的观点标识", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		if( StringUtils.isEmpty(touguId)||StringUtils.isEmpty(touguName)){
			Toast.makeText(this, "错误的投顾标识", Toast.LENGTH_SHORT).show();
		}
		String titleStr = getIntent().getStringExtra(BUNDLE_PARAM_TITLE);
		if(!StringUtils.isEmpty(titleStr)){
			setTitle(titleStr);
		}
		myListView = (XListView) findViewById(R.id.listView);
		myListView.setPullRefreshEnable(false);
		myListView.setDivider(null);
		myListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				getCommentList("201410221",opinionId,currPage+1, pageSize,LOAD_MORE);
			}

		});
//		View head = LayoutInflater.from(this).inflate(R.layout.opinion_listitem_dongtai, null);
//		aViewHolder = new AttentionViewHolder();
//		aViewHolder.userIcon = (ImageView) head.findViewById(R.id.user_icon);
//		aViewHolder.userName = (TextView) head.findViewById(R.id.user_name);
//		aViewHolder.userRole = (TextView) head.findViewById(R.id.user_role);
//		aViewHolder.userCompany = (TextView) head.findViewById(R.id.user_company);
//		aViewHolder.time = (TextView) head.findViewById(R.id.time);
//		aViewHolder.opinionTitle = (TextView) head.findViewById(R.id.opinion_title);
//		aViewHolder.opinionContent = (TextView) head.findViewById(R.id.opinion_content);
//		aViewHolder.support = (TextView) head.findViewById(R.id.support);
//		aViewHolder.imageLimit = (ImageView) head.findViewById(R.id.image_limit);
//		aViewHolder.ivCertif = (ImageView) head.findViewById(R.id.iv_certif);
//		
//		head.findViewById(R.id.band_bg).setVisibility(View.VISIBLE);
//		myListView.addHeaderView(head);
		
		WebView webView = getHeadView();
		myListView.addHeaderView(webView);
		
		juBao = getJuBaoView();
		juBao.setVisibility(View.GONE);
		myListView.addHeaderView(juBao);
		
		myAdapter = new AttentionAdapter(this);
		myListView.setAdapter(myAdapter);
		
//		myListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				// TODO Auto-generated method stub
//				if (id >= 0 && id < commentData.size()) {
//					CommentListBean.CommentData[] d = commentData.get((int)id);
//					if(d != null){
//						CommentListBean.CommentData data = d.length == 2 ? d[1]:d[0];
//						
//						Intent intent = new Intent(AttentionDetailActivity.this, ReplyActivity.class);
//						intent.putExtra(ReplyActivity.BUNDLE_TYPE, ReplyActivity.TYPE_COMMENT);
//						intent.putExtra(ReplyActivity.BUNDLE_PARAM_APPID, "201410221");
//						intent.putExtra(ReplyActivity.BUNDLE_PARAM_APPITEMID, ""+itemData.getId());
//						intent.putExtra(ReplyActivity.BUNDLE_PARAM_SENDERID, UserInfo.getInstance().getUserId());//需替换userid
//						intent.putExtra(ReplyActivity.BUNDLE_PARAM_REPLYROOTID, data.getReplyRootid());
//						intent.putExtra(ReplyActivity.BUNDLE_PARAM_REPLYTOID, data.getReplyToid());
//						startActivity(intent);
//					}else{
//						Toast.makeText(AttentionDetailActivity.this, "无效数据", Toast.LENGTH_SHORT).show();
//					}
//					
//				}
//			}
//		});
		
		share = (ImageView) findViewById(R.id.share_lo);
		share.setOnClickListener(this);
		bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
//		comment.setOnClickListener(this);
//		zan = (LinearLayout) findViewById(R.id.zan_lo);
//		zan.setOnClickListener(this);
		
		etComment = (TextView)findViewById(R.id.tv_reply);
		etComment.setOnClickListener(this);
		
		tvZan = (TextView)findViewById(R.id.tv_zan);
		tvZan.setEnabled(false);
//		tvZan.setOnClickListener(this);
//		tvZan.setText(""+praiseCount);
		
		imageLoader = new ImageLoader(AttentionDetailActivity.this);
		
//		getAttentionDetail(opinionId);
		
//		getCommentList("201410221",opinionId,currPage+1, pageSize,LOAD_MORE);
		
//		webView.loadUrl(detailUrl + "?currUserid="+UserInfo.getInstance().getUserId());
//		getCommentCount(opinionId);
		
//		getOptionLimit();
	}
	
	@Override
	public void onLoad(){
		getOptionLimit();
	}
	
	class AttentionViewHolder {
		ImageView userIcon;
		TextView userName;
		TextView userRole;
		TextView userCompany;
		TextView time;
		TextView opinionTitle;
		TextView opinionContent;
		TextView support;
//		TextView comment;
		ImageView imageLimit;
		ImageView ivCertif;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.share_lo:
			if(limit != 1){
				Toast.makeText(AttentionDetailActivity.this, "私密观点不能分享哦~", Toast.LENGTH_SHORT).show();
			}else{
				openShareDialog();
			}
			break;
		case R.id.tv_reply:
			if(UserInfo.getInstance().isLogin()){
				if(opinionId != -1){
					Intent _intent = new Intent(AttentionDetailActivity.this, ReplyActivity.class);
					_intent.putExtra(ReplyActivity.BUNDLE_TYPE, ReplyActivity.TYPE_COMMENT);
					_intent.putExtra(ReplyActivity.BUNDLE_PARAM_APPID, "201410221");
					_intent.putExtra(ReplyActivity.BUNDLE_PARAM_APPITEMID, ""+opinionId);
					_intent.putExtra(ReplyActivity.BUNDLE_PARAM_SENDERID, UserInfo.getInstance().getUserId());
					_intent.putExtra(ReplyActivity.BUNDLE_PARAM_SENDERNAME, UserInfo.getInstance().getUserName());
					_intent.putExtra(ReplyActivity.BUNDLE_PARAM_REPLYROOTID, "");
					_intent.putExtra(ReplyActivity.BUNDLE_PARAM_REPLYTOID, "");
					startActivityForResult(_intent,COMMENT_REQUEST_CODE);
				}else{
					Toast.makeText(AttentionDetailActivity.this, "错误的观点标识", Toast.LENGTH_SHORT).show();
				}
			}else{
				Intent intent = new Intent(AttentionDetailActivity.this, LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.tv_zan:
			if(UserInfo.getInstance().isLogin()){
				doPraise();
			}else{
				Intent intent = new Intent(AttentionDetailActivity.this, LoginActivity.class);
				startActivityForResult(intent,PRAISE_REQUEST_CODE);
			}
			
			break;
		}

	}
	
	private void openShareDialog(){
		BottomShareDialog dialog = new BottomShareDialog(this);
		dialog.setOnConfirmListener(new BottomShareDialog.OnConfirmListener() {
			@Override
			public void onConfirm(int index) {
				switch(index){
					case 0:{
						sendShare(PType.PLATFORM_WX);
						break;
					}
					case 1:{
						sendShare(PType.PLATFORM_WX_friends);
						break;
					}
					case 2:{
						sendShare(PType.PLATFORM_SINA);
						break;
					}
				}
			}
		});
		dialog.show();
	}
	
	private void initShare(){
		
		if(ShareManager.getInstance() == null){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(ShareManager.KEY_APPID_QQ,ShareConstants.QQ_APP_ID);
			map.put(ShareManager.KEY_APPID_WX,ShareConstants.WX_APP_ID);
			
//			map.put(ShareManager.KEY_APPID_SINA,ShareConstants.APP_KEY);
//			map.put(ShareManager.KEY_APPID_CALLBACK,ShareConstants.REDIRECT_URL);
//			map.put(ShareManager.KEY_APPID_SCOPE,ShareConstants.SCOPE);
			ShareManager.init(this,map);
		}
	}
	
	private void sendShare(int pType){
		MsgImageText msg = new MsgImageText();
		String appName = getString(R.string.app_name);
//		String appName = "黄飞鸿";
		msg.appName = appName;
		msg.pType=pType;
		msg.title = opTitle;
		msg.summary = "我正在使用“"+appName+"手机客户端”，和你一起分享"+touguName+"的投资日志《"+opTitle+"》";
		msg.image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		msg.targetUrl = detailUrl;
		if(pType == PType.PLATFORM_SINA){
			Intent intent = new Intent(this,ShareToSinaActivity.class);
			intent.putExtra(ShareToSinaActivity.BUNDLE_PARAM_SHARE_CONTENT, msg);
			startActivity(intent);
		}else{
			initShare();
			msg.share(new CallbackListener() {
				@Override
				public void onSuccess() {
					Toast.makeText(AttentionDetailActivity.this,"分享成功",Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onFailure() {
					Toast.makeText(AttentionDetailActivity.this,"分享失败",Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onCancel() {
//					showToast(ImageEditActivity.this,"取消分享");
				}
			});
		}
	}

	class AttentionAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public AttentionAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return commentData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return commentData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.dongtai_attention_comment_item, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.userIcon = (ImageView) convertView.findViewById(R.id.user_icon);
				viewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
				viewHolder.userRole = (TextView) convertView.findViewById(R.id.user_role);
				viewHolder.userCompany = (TextView) convertView.findViewById(R.id.user_company);
				viewHolder.time = (TextView) convertView.findViewById(R.id.time);
				viewHolder.commentContent = (TextView) convertView.findViewById(R.id.comment_content);
				viewHolder.ivCertif = (ImageView) convertView.findViewById(R.id.iv_certif);
				
				viewHolder.replyToLayout = (LinearLayout) convertView.findViewById(R.id.reply_to_layout);
				viewHolder.replyToContent = (TextView) convertView.findViewById(R.id.reply_to_content);
				
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			final CommentListBean.CommentData[] itemData = commentData.get(position);
			if(itemData != null){
				if(itemData.length == 1){
					viewHolder.replyToLayout.setVisibility(View.GONE);
					viewHolder.userName.setText(itemData[0].getSenderName());
					viewHolder.commentContent.setText(HtmlUtils.parseImgTag(AttentionDetailActivity.this, getScreenW(), itemData[0].getContent(), null));
					viewHolder.time.setText(DateUtils.getTimeAgoString(itemData[0].getCtime(), "MM-dd HH:mm"));
					if(StringUtils.isEmpty(itemData[0].getHeadPic())){
						viewHolder.userIcon.setImageResource(R.drawable.icon_head_default);
					}else{
						imageLoader.downLoadImage(itemData[0].getHeadPic(), viewHolder.userIcon);
					}
					
					viewHolder.userRole.setText(itemData[0].getTypeDesc());
					viewHolder.userCompany.setText(itemData[0].getCompany());
					if (1 == itemData[0].getSignV()) {
						viewHolder.ivCertif.setVisibility(View.VISIBLE);
					} else {
						viewHolder.ivCertif.setVisibility(View.GONE);
					}
					viewHolder.userIcon.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(itemData[0].getIsAdviser() == 1 || itemData[0].getIsAdviser() == 2){
								Intent intent = new Intent(AttentionDetailActivity.this, ViewInvesterInfoActivity.class);
								intent.putExtra("USERNAME", itemData[0].getSenderName());
								intent.putExtra("USERID", itemData[0].getSenderId());
								startActivity(intent);
							}else{
								Intent intent = new Intent();
								intent.putExtra("title", itemData[0].getSenderName());
								intent.putExtra("id", itemData[0].getSenderId());
								intent.putExtra("headImage", itemData[0].getHeadPic());
								intent.setClass(AttentionDetailActivity.this, ViewSignUserInfoActivity.class);
								startActivity(intent);
							}
							
						}
					});
					
				}else if(itemData.length == 2){
					viewHolder.replyToLayout.setVisibility(View.VISIBLE);
					viewHolder.replyToContent.setText(Html.fromHtml("<font color=\"#4c87c6\">"+itemData[0].getSenderName()+"</font> : "+itemData[0].getContent()));
					viewHolder.userName.setText(itemData[1].getSenderName());
					viewHolder.commentContent.setText(itemData[1].getContent());
				}
			}
			
			return convertView;
		}

		class ViewHolder {
			ImageView userIcon;
			TextView userName;
			TextView userRole;
			TextView userCompany;
			TextView time;
			TextView commentContent;
			ImageView ivCertif;
			
			LinearLayout replyToLayout;
			TextView replyToContent;
		}
	}
	
	private void getAttentionDetail(long id) {

		String url = String.format(NetUrlTougu.OPINION_DETAIL, id);
		Log.e(TAG, url);
		JsonRequest<OpinionDetailBean> request = new JsonRequest<OpinionDetailBean>(Method.GET, url,
				new RequestHandlerListener<OpinionDetailBean>(getContext()) {

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
					public void onSuccess(String id, OpinionDetailBean data) {
						// TODO Auto-generated method stub
						fillData(data.getData());
						
					}
				}, OpinionDetailBean.class);

		send(request);

	}
	
//	private void getCommentCount(long itemId) {
//
//		String url = String.format(NetUrlComment.COMMENT_COUNT, "201410221",itemId);
//		Log.e(TAG, url);
//		JsonRequest<CommentCountBean> request = new JsonRequest<CommentCountBean>(Method.GET, url,
//				new RequestHandlerListener<CommentCountBean>(getContext()) {
//
//					@Override
//					public void onStart(Request request) {
//						super.onStart(request);
//						// showDialog(request);
//					}
//
//					@Override
//					public void onEnd(Request request) {
//						super.onEnd(request);
//						// hideDialog(request);
//					}
//
//					@Override
//					public void onSuccess(String id, CommentCountBean data) {
//						// TODO Auto-generated method stub
//						if(data != null){
//							tvComment.setText(""+data.getTotalCount());
//						}
//					}
//				}, CommentCountBean.class);
//
//		send(request);
//
//	}
	
	private void fillData(OpinionDetailBean.Data itemData){
		
		aViewHolder.userName.setText(itemData.getUserinfo().getUsername());
		aViewHolder.userRole.setText(itemData.getUserinfo().getPosition());
		aViewHolder.userCompany.setText(itemData.getUserinfo().getCompany());
		aViewHolder.time.setText(itemData.getCtime());
		aViewHolder.opinionTitle.setText(itemData.getTitle());
		aViewHolder.opinionContent.setText(itemData.getContent());
		aViewHolder.support.setText(""+itemData.getPraise());
		if(1 == itemData.getLimits()){
			aViewHolder.imageLimit.setVisibility(View.GONE);
		}else{
			aViewHolder.imageLimit.setVisibility(View.VISIBLE);
		}
		if(1 == itemData.getUserinfo().getUse_satisfaction()){
			aViewHolder.ivCertif.setVisibility(View.GONE);
		}else{
			aViewHolder.ivCertif.setVisibility(View.VISIBLE);
		}
		imageLoader.downLoadImage(itemData.getUserinfo().getHead_image(), aViewHolder.userIcon);
		myAdapter.notifyDataSetChanged();
		this.itemData = itemData;
//		getCommentList(2,1,currPage+1, pageSize,LOAD_MORE);
	}
	
	private void getCommentList(final String appId,final long opinionId,final int pageId, int pageSize,final int requestType) {

		String url = String.format(NetUrlComment.COMMENT_LIST,appId, opinionId,pageSize, pageId);
		Log.e(TAG, url);
		JsonRequest<CommentListBean> request = new JsonRequest<CommentListBean>(Method.GET, url,
				new RequestHandlerListener<CommentListBean>(getContext()) {

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
					public void onSuccess(String id, CommentListBean data) {
						// TODO Auto-generated method stub
						if (pageId == 1) {
							commentData.clear();
						}
						commentData.addAll(data.getListData());
						myAdapter.notifyDataSetChanged();
						myListView.setRefreshTime("1分钟前");
						currPage = pageId;
						if(requestType == PULL_REFRESH){
							myListView.stopRefresh();
						}else{
							myListView.stopLoadMore();
						}
						if(data.getTotalCount() < data.getPageSize()){
							myListView.setPullLoadEnable(false);
						}
					}
				}, CommentListBean.class);

		send(request);

	}
	
	private OptionLimitResult optionLimitResult;
	
	private void getOptionLimit() {

		String url = String.format(NetUrlTougu.OPTION_LIMIT, opinionId,UserInfo.getInstance().getUserId(),touguId,limit);
		Log.e(TAG, url);
		JsonRequest<OptionLimitResult> request = new JsonRequest<OptionLimitResult>(Method.GET, url,
				new RequestHandlerListener<OptionLimitResult>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						 showLoading(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						hideLoading(request);
					}

					@Override
					public void onSuccess(String id, OptionLimitResult data) {
						// TODO Auto-generated method stub
//						Toast.makeText(AttentionDetailActivity.this, "赞成功", Toast.LENGTH_SHORT).show();
						optionLimitResult = data;
						if(data.getRetCode() == 0){
							
							long praised = data.getData().getLikeTotal();
							tvZan.setText(""+praised);
							if(data.getData().isIfPraise()){
								tvZan.setOnClickListener(AttentionDetailActivity.this);
								tvZan.setEnabled(true);
							}
							if(limit != 1 && !data.getData().isIfSign()){
								bottomLayout.setVisibility(View.GONE);
							}
							
							webView.loadUrl(detailUrl + "?currUserid="+UserInfo.getInstance().getUserId());
						}
					}
				}, OptionLimitResult.class);

		send(request);

	}
	
	private void doPraise() {

		String oldValue = tvZan.getText().toString();
		if(StringUtils.isEmpty(oldValue)){
			tvZan.setText("1");
		}else{
			tvZan.setText(""+(Integer.parseInt(oldValue)+1));
		}
		tvZan.setEnabled(false);
		
		String url = String.format(NetUrlTougu.OPINION_PRAISE, opinionId,UserInfo.getInstance().getUserId(),touguId);
		Log.e(TAG, url);
		Map<String,String> params = new HashMap<String,String>();
		params.put("id", ""+opinionId);
		JsonRequest<PraiseResultBean> request = new JsonRequest<PraiseResultBean>(Method.POST, url,params,
				new RequestHandlerListener<PraiseResultBean>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
//						 showDialog(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
//						 hideDialog(request);
					}

					@Override
					public void onSuccess(String id, PraiseResultBean data) {
						// TODO Auto-generated method stub
//						Logger.error(TAG, "点赞成功");
//						Toast.makeText(AttentionDetailActivity.this, "赞成功", Toast.LENGTH_SHORT).show();
//						String oldValue = tvZan.getText().toString();
//						if(StringUtils.isEmpty(oldValue)){
//							tvZan.setText("1");
//						}else{
//							tvZan.setText(""+(Integer.parseInt(oldValue)+1));
//						}
					}
					
					@Override
					public void onFailure(String id, int code, String str,Object obj) {
						
					}
				}, PraiseResultBean.class);

		send(request);

	}
	
	@Override
	public void onActivityResult(int requestCode, int responseCode, Intent data) {
		if (COMMENT_REQUEST_CODE == requestCode && ReplyActivity.COMMENT_SUCCESS_CODE == responseCode) {
			currPage = 0;
			getCommentList("201410221",opinionId,currPage+1, pageSize,LOAD_MORE);
//			getCommentCount(opinionId);
			return;
		}else if(PRAISE_REQUEST_CODE == requestCode && LoginActivity.LOGINED_RESPONSE_CODE == responseCode){
			finish();
			startActivity(getIntent());
		}
		super.onActivityResult(requestCode, responseCode, data);
	}
	
	private WebView webView;
	private WebView getHeadView(){
		webView = new WebView(this);
		AbsListView.LayoutParams llp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
		webView.setLayoutParams(llp);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		webView.setWebViewClient(webViewClient);
		return webView;
	}
	
	private WebViewClient webViewClient = new WebViewClient() {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Logger.info(TAG, "onPageStarted");
			showLoading(null);
	    }
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			// super.onReceivedError(view, errorCode,
			//
			Logger.info(TAG, "onReceivedError");
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// Debug.e("aaa", "onPageFinished");
			Logger.error(TAG, "onPageFinished");
			
			hideLoading(null);
			if(optionLimitResult != null && (limit == 1 || (limit == 2 && optionLimitResult.getData().isIfSign()))){
				juBao.setVisibility(View.VISIBLE);
				getCommentList("201410221",opinionId,currPage+1, pageSize,LOAD_MORE);
			}
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			
			// TODO Auto-generated method stub
			if(url.endsWith("http://www.userdetail.com/"+touguId)){
				Intent intent = new Intent(AttentionDetailActivity.this,ViewInvesterInfoActivity.class);
				intent.putExtra("USERNAME", touguName);
				intent.putExtra("USERID", touguId);
				startActivity(intent);
			}else if(!StringUtils.isEmpty(url) && StringUtils.containsStringRegex(url,"http://stock.jrj.com.cn/share,\\d{6}.shtml")){
	         		String stockCode = url.substring("http://stock.jrj.com.cn/share,".length(), "http://stock.jrj.com.cn/share,".length()+6);
	         		Stock ss = getStockFromDb(stockCode);
	         		
	         }else if(!StringUtils.isEmpty(url) && url.endsWith("jpg") || url.endsWith("JPG")||url.endsWith("png")||url.endsWith("PNG")){
	        	 Intent intent = new Intent(AttentionDetailActivity.this,ImageViewerActivity.class);
				 intent.putExtra(ImageViewerActivity.BUNDLE_PARAM_FILEPATH, url);
				 startActivity(intent);
	         }
            return true;
		}
		
	};
	
	private View getJuBaoView(){
		
		LinearLayout jubaoLayout = new LinearLayout(this);
//		jubaoLayout.setBackgroundResource(R.drawable.selector_bg_listitem);
		AbsListView.LayoutParams llp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
		jubaoLayout.setLayoutParams(llp);
		jubaoLayout.setBackgroundColor(getResources().getColor(R.color.white));
		jubaoLayout.setGravity(Gravity.RIGHT);
		TextView jubaoTv = new TextView(this);
		jubaoTv.setText("举报");
		jubaoTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		jubaoTv.setTextColor(getResources().getColor(R.color.font_727272));
		jubaoTv.setPadding(dipToPixels(this,13), dipToPixels(this,13), dipToPixels(this,13), dipToPixels(this,13));
		jubaoTv.setBackgroundResource(R.drawable.selector_bg_listitem);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		jubaoTv.setLayoutParams(params);
		jubaoLayout.addView(jubaoTv);
		
		jubaoTv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AttentionDetailActivity.this,JuBaoActivity.class);
				intent.putExtra(JuBaoActivity.BUNDLE_PARAM_VIEWPOINTID, ""+opinionId);
				intent.putExtra(JuBaoActivity.BUNDLE_PARAM_ADVISERID, touguId);
				intent.putExtra(JuBaoActivity.BUNDLE_PARAM_TITLE, opTitle);
				intent.putExtra(JuBaoActivity.BUNDLE_PARAM_CONTENT_TYPE, JuBaoActivity.TYPE_OPINION);
				startActivity(intent);
			}
		});
		return jubaoLayout;
	}
	
	private int dipToPixels(Context context,int dip) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
		return (int) px;
	}
	
	public Stock getStockFromDb(String stockCode) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(stockCode)){
			return null;
		}
		String sql = "SELECT * FROM " + QuoteDic.TABLE_NAME + " WHERE " + QuoteDic.Property_StockCode + " = '" + stockCode + "'";// limit
		SQLiteDatabase dataBase = null;
		Cursor oCursor1 = null;
		try{
			Stock ss = null;
			if (dataBase == null || !dataBase.isOpen()) {
				String path = "/data/data/" + getPackageName() + "/" + QuoteDic.DATA_NAME;
				dataBase = SQLiteDatabase.openOrCreateDatabase(path, null);
			}
			oCursor1 = dataBase.rawQuery(sql, null);
			if(oCursor1.getCount() == 0){
				return null;
			}
			for (oCursor1.moveToFirst(); !oCursor1.isAfterLast(); oCursor1.moveToNext()) {
				Stock _ss = new Stock();
				int market = oCursor1.getColumnIndex(QuoteDic.Property_MarketId);
				if (market > 0) {
					_ss.setMarketID(oCursor1.getString(market));
				}
				int codeID = oCursor1.getColumnIndex(QuoteDic.Property_StockCode);
				if (codeID > 0) {
					_ss.setStockCode(oCursor1.getString(codeID));
				}
				int nameID = oCursor1.getColumnIndex(QuoteDic.Property_StockName);
				if (nameID > 0) {
					_ss.setStockName(oCursor1.getString(nameID));
				}
				int pinyin = oCursor1.getColumnIndex(QuoteDic.Property_StockPY);
				if (pinyin > 0) {
					_ss.setStockPinyin(oCursor1.getString(pinyin));
				}
				int index = oCursor1.getColumnIndex(QuoteDic.Property_STOCKId);
				_ss.setStid(oCursor1.getString(index));
				index = oCursor1.getColumnIndex(QuoteDic.Property_StockType);
				_ss.setType(oCursor1.getString(index));
				if(ss != null && _ss.getType().equals("i")){
					
				}else{
					ss = _ss;
				}
			}
			return ss;
		}finally{
			if(oCursor1 != null){
				oCursor1.close();
			}
			if(dataBase != null){
				dataBase.close();
			}
		}
	}
	
}
