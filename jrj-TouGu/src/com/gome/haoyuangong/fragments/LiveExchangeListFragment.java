/**
 * 
 */
package com.gome.haoyuangong.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.activity.AttentionDetailActivity;
import com.gome.haoyuangong.activity.LoginActivity;
import com.gome.haoyuangong.activity.ViewInvesterInfoActivity;
import com.gome.haoyuangong.activity.ViewSignUserInfoActivity;
import com.gome.haoyuangong.bean.Stock;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.live.ListObjectItem;
import com.gome.haoyuangong.net.result.live.PostReplyResult;
import com.gome.haoyuangong.net.url.NetUrlLive;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.net.volley.StringRequest;
import com.gome.haoyuangong.utils.DateUtils;
import com.gome.haoyuangong.utils.HtmlUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.utils.HtmlUtils.EmoBean;
import com.gome.haoyuangong.views.EmotionsLayout;

/**
 * 
 */
public class LiveExchangeListFragment extends XListFragment{

	private static final String TAG = LiveExchangeListFragment.class.getName();
	public static final String BUNDLE_PARAM_ROOMID = "room_id";
	public static final String BUNDLE_PARAM_NAME = "user_name";
	
	public static final int STOCK_REQUEST_CODE = 1001;

	private MyListAdapter myAdapter;

	private List<ListObjectItem> dataList = new ArrayList<ListObjectItem>();

	private ImageLoader imageLoader;
	private String roomId;
	private String userName;
	private String order = "desc";
	private int lastId = 0;
	private int pageSize = 20;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if(args == null){
			
		}else{
			roomId = args.getString(BUNDLE_PARAM_ROOMID);
			userName = args.getString(BUNDLE_PARAM_NAME);
		}
	}
	
	public void init(View v) {
		super.init(v);
		addBottomView();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		imageLoader = new ImageLoader(mActivity);
		myAdapter = new MyListAdapter();
		mList.setAdapter(myAdapter);
	}


	class MyListAdapter extends BaseAdapter {

		private LayoutInflater layoutInflater;

		public MyListAdapter() {
			layoutInflater = LayoutInflater.from(mActivity);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			ViewHolder aViewHolder;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.live_exchange_item, parent, false);
				aViewHolder = new ViewHolder();
				aViewHolder.userIcon = (ImageView) convertView.findViewById(R.id.user_icon);
				aViewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
				aViewHolder.time = (TextView) convertView.findViewById(R.id.a_time);
				aViewHolder.replyTv = (TextView) convertView.findViewById(R.id.reply_tv);
				aViewHolder.exchangeContentUp = (TextView) convertView.findViewById(R.id.exchange_content_up);
				aViewHolder.exchangeContentDown = (TextView) convertView.findViewById(R.id.exchange_content_down);
				aViewHolder.loWraperReply = (LinearLayout)convertView.findViewById(R.id.lo_wraper_reply);
				convertView.setTag(aViewHolder);
			} else {
				aViewHolder = (ViewHolder) convertView.getTag();
			}
			
			final ListObjectItem exchangeItem = dataList.get(position);
			String upNameStr = exchangeItem.getmSso_userName();
			String upNameVStr = upNameStr;
			if(1 == exchangeItem.getVerify()){
				upNameVStr += " V";
			}
			SpannableStringBuilder upName = new SpannableStringBuilder(upNameVStr);
			if(upNameStr.length() > 0){
				upName.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.font_4c86c6)), 0, upNameStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if(1 == exchangeItem.getVerify()){
				ImageSpan imageSpan = new ImageSpan(getContext(), R.drawable.icon_certification);
				upName.setSpan(imageSpan, upNameVStr.length() - 1, upNameVStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			aViewHolder.userName.setText(upName);
			
			aViewHolder.time.setText(DateUtils.getTimeAgoString(exchangeItem.getPubDate(), "yyyy-MM-dd HH:mm:ss"));
			aViewHolder.exchangeContentUp.setText(exchangeItem.getmContentSSB());
			
			aViewHolder.replyTv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					replayUserid = exchangeItem.getmSso_userid();
					replayId = exchangeItem.getmRoomID();
					etContent.setText("");
					etContent.setHint("回复"+exchangeItem.getmSso_userName());
					showSoftInput(etContent);
				}
			});
			
			if(StringUtils.isEmpty(exchangeItem.getHeadPic())){
				aViewHolder.userIcon.setImageResource(R.drawable.icon_head_default);
			}else{
				imageLoader.downLoadImage(exchangeItem.getHeadPic(), aViewHolder.userIcon);
			}
			
			aViewHolder.userIcon.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(exchangeItem.getIsAdviser() == 1 || exchangeItem.getIsAdviser() == 2){
						Intent intent = new Intent(mActivity, ViewInvesterInfoActivity.class);
						intent.putExtra("USERNAME", exchangeItem.getmSso_userName());
						intent.putExtra("USERID", exchangeItem.getmSso_userid());
						startActivity(intent);
					}else{
						Intent intent = new Intent();
						intent.putExtra("title", exchangeItem.getmSso_userName());
						intent.putExtra("id", exchangeItem.getmSso_userid());
						intent.putExtra("headImage", exchangeItem.getHeadPic());
						intent.setClass(mActivity, ViewSignUserInfoActivity.class);
						startActivity(intent);
					}
				}
			});
			
			if(!StringUtils.isEmpty(exchangeItem.getmReplyName())){
				aViewHolder.loWraperReply.setVisibility(View.VISIBLE);
				String downNameStr = exchangeItem.getmReplyName();
				String nDownNameStr = downNameStr+": ";
				SpannableStringBuilder downContent = new SpannableStringBuilder(nDownNameStr);
				downContent.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.font_4c86c6)), 0, nDownNameStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

				downContent.append(exchangeItem.getmReplyContentSSB());
				aViewHolder.exchangeContentDown.setText(downContent);
			}else{
				aViewHolder.loWraperReply.setVisibility(View.GONE);
			}
			
			return convertView;
		}

		class ViewHolder {
			ImageView userIcon;
			TextView userName;
			TextView time;
			TextView replyTv;
			
			TextView exchangeContentUp;
			TextView exchangeContentDown;
			
			LinearLayout loWraperReply;
		}
		
	}

	@Override
	protected <T> Request<T> getRequest() {
		// TODO Auto-generated method stub
		String url = String.format(NetUrlLive.EXCHANGE_LIST, roomId, pageSize, lastId , order);
		Logger.error(TAG, url);
		Request request = new StringRequest(Method.GET, url, null);
		return request;
	}

	@Override
	protected void onRefreshPrepear() {
		// TODO Auto-generated method stub
		lastId = 0;
	}

	@Override
	protected void onLoadMorePrepear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onReceive(boolean isLoadMore, String id, Object data) {
		// TODO Auto-generated method stub
		saveRefreshTime(NetUrlLive.EXCHANGE_LIST);
		mList.setRefreshTime(getRefreshTime(NetUrlLive.EXCHANGE_LIST));
		
		String result = (String)data;
		if(!StringUtils.isEmpty(result)){
			List<ListObjectItem> talkList= parserTalkData(result,isLoadMore);
			if(!isLoadMore){
				dataList.clear();
			}
			
			dataList.addAll(talkList);
			myAdapter.notifyDataSetChanged();
			if(!talkList.isEmpty()){
				ListObjectItem item = talkList.get(talkList.size() - 1);
				lastId = Integer.parseInt(item.getmRoomID());
			}
			if(talkList.size() < pageSize){
				mList.setPullLoadEnable(false);
			}else{
				mList.setPullLoadEnable(true);
			}
		}else{
			Toast.makeText(mActivity, "获取数据失败", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	  private List<ListObjectItem> parserTalkData(String str,boolean isMore){
		  
		  List<ListObjectItem> dataList = new ArrayList<ListObjectItem>();
		    try {
//		      noURLTempStr = "";
//		      ssoidVec.removeAllElements();
		      JSONObject jsonData = new JSONObject(str); 
		      JSONArray a_jsonObjectInfos = jsonData.optJSONArray("data"); 
		      ListObjectItem loi = null;
//		      long times1 = System.currentTimeMillis();
		      if(a_jsonObjectInfos != null&&a_jsonObjectInfos.length()>0){
		        int getNetInfoCount = a_jsonObjectInfos.length();
		        for (int i = 0; i < a_jsonObjectInfos.length(); i++) {
		          loi = new ListObjectItem();
		          JSONObject jsonO = a_jsonObjectInfos.optJSONObject(i);
		          if(jsonO == null){
		        	  continue;
		          }
		          String content = StringUtils.decodeUnicode(jsonO.optString("content"));
		          content = content.toLowerCase();
		          content = StringUtils.dealColorTag(content);
//		          loi.setImageAndTextFlag(haveImageTag(content));
		          loi.setmContentSSB(HtmlUtils.parseImgTag(mActivity,getScreenW(),content,loi));
		          loi.setmContent(content);
		          loi.setPubDate(jsonO.optString("time"));
		          loi.setmRoomID(jsonO.optString("id"));
		          loi.setmSso_userName(jsonO.optString("userName"));
		          String ssoid = jsonO.optString("uid");
		          loi.setmSso_userid(ssoid);
		          loi.setVerify(jsonO.optInt("verify"));
		          loi.setHeadPic(jsonO.optString("headImage"));
		          loi.setIsAdviser(jsonO.optInt("isAdviser"));
//		          String tUrl = roomIocnURLHashTable.get(ssoid);
//		          if(tUrl==null){
//		            noURLTempStr = noURLTempStr+ssoid+",";
//		            ssoidVec.add(ssoid);
//		          }else{
//		            loi.setLinkPic(JRJ_GlobalData.ROOM_IMAGE_BASE_URL+tUrl);
//		          }
		          loi.setmRoomName(jsonO.optString("uname"));
		          JSONObject jsonR = jsonO.optJSONObject("quote");
		          if(jsonR!=null){
		            loi.setmReplyName(jsonR.optString("userName"));
		            String contentT = jsonR.optString("content");
//		            loi.setReplyImageAndTextFlag(haveImageTag(contentT));
		            loi.setmReplyContentSSB(HtmlUtils.parseImgTag(mActivity,getScreenW(),contentT,loi));
		            loi.setmReplyContent(contentT);
		            loi.setmReplyTime(jsonR.optString("time"));
		          }
		          dataList.add(loi);
		        }
		      }
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		    return dataList;
		  }
	  
	  class TempClass{
			private String data;
			private String delIDs;
			public String getData() {
				return data;
			}
			public void setData(String data) {
				this.data = data;
			}
			public String getDelIDs() {
				return delIDs;
			}
			public void setDelIDs(String delIDs) {
				this.delIDs = delIDs;
			}
		}
	  
	  private ImageView imageView1;
	  private ImageView imageView2;
	  private EditText etContent;
	  private TextView send;
	  private EmotionsLayout emotionLayout;
	  
	  private String replayUserid;
	  private String replayId;
		private static final int INPUT_TYPE_KEYBOARD = 1;
		private static final int INPUT_TYPE_BIAOQING = 2;
		private int currentInputType = INPUT_TYPE_KEYBOARD;
	  
	  private void addBottomView(){
		  View bottom = LayoutInflater.from(getContext()).inflate(R.layout.simple_reply_layout, null);
		  
		  LinearLayout.LayoutParams layoutParam = (LinearLayout.LayoutParams)mList.getLayoutParams();
		  layoutParam.weight = 1;
		  mList.setLayoutParams(layoutParam);
		  
		  LinearLayout parent = (LinearLayout)mList.getParent();
		  parent.addView(bottom);
		  
		  imageView1 = (ImageView) bottom.findViewById(R.id.imageView_1);
		  imageView1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (currentInputType == INPUT_TYPE_KEYBOARD) {
						uiHandler.sendEmptyMessage(INPUT_TYPE_BIAOQING);
					} else {
						uiHandler.sendEmptyMessage(INPUT_TYPE_KEYBOARD);
					}

				}
			});
		  uiHandler.sendEmptyMessage(INPUT_TYPE_KEYBOARD);
		  imageView2 = (ImageView) bottom.findViewById(R.id.imageView_2);
		  imageView2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					
				}
			});
		  etContent = (EditText) bottom.findViewById(R.id.et_reply);
		  etContent.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (currentInputType == INPUT_TYPE_KEYBOARD) {
						
					} else {
						uiHandler.sendEmptyMessage(INPUT_TYPE_KEYBOARD);
					}
				}
			});
		  send = (TextView)bottom.findViewById(R.id.bt_action);
		  send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				postReply();
			}
		});
		  
		  emotionLayout = (EmotionsLayout) bottom.findViewById(R.id.emotion_layout);
		  emotionLayout.setVisibility(View.GONE);
		  emotionLayout.setOnItemClickListener(new EmotionsLayout.OnItemClickListener() {
				
				@Override
				public void onEmotionClick(String emoName) {
					// TODO Auto-generated method stub
					Logger.error(TAG, "emo name is : "+emoName);
					EmoBean emoBean = HtmlUtils.getEmotionResId(emoName);
					if(emoBean != null){
						ImageSpan imageSpan = new ImageSpan(getContext(),HtmlUtils.getEmotionResId(emoName).getResId());
						String repText = emoBean.geteName();
						SpannableString spannableString = new SpannableString(repText);
						spannableString.setSpan(imageSpan, 0, repText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						// 将随机获得的图像追加到EditText控件的最后
//						etContent.append(spannableString);
						int index = etContent.getSelectionStart();
						Editable editable = etContent.getText();
						editable.insert(index, spannableString);
					}
				}
			});
		  
	  }
		public Handler uiHandler = new Handler(){
			
			@Override
	        public void handleMessage(Message msg) {
				switch(msg.what){
				case INPUT_TYPE_KEYBOARD:
					setInputStatus(INPUT_TYPE_KEYBOARD);
					currentInputType = INPUT_TYPE_KEYBOARD;
					break;
				case INPUT_TYPE_BIAOQING:
					setInputStatus(INPUT_TYPE_BIAOQING);
					currentInputType = INPUT_TYPE_BIAOQING;
					break;
				}
			}
		};
	  /**
		 * 
		 * @param status
		 * 1 键盘
		 * 2 表情
		 */
		private void setInputStatus(int status){
			
			switch(status){
				case 1:{
					showSoftInput(etContent);
					emotionLayout.setVisibility(View.GONE);
					imageView1.setImageResource(R.drawable.icon_biaoqing);
					break;
				}case 2:{
					hideSoftInput();
					imageView1.setImageResource(R.drawable.icon_keyboard);
					new Handler().postDelayed(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							emotionLayout.setVisibility(View.VISIBLE);
						}
						
					}, 100);
					break;
				}
			}
		}
	  
		@Override
		public void onActivityResult(int requestCode, int responseCode, Intent data) {
			if (STOCK_REQUEST_CODE == requestCode) {
				if (data != null) {
					
				}
				return;
			}
			super.onActivityResult(requestCode, responseCode, data);
		}
		
		/**
		 * 
		 * @param uname 用户名
		 * @param passwd 密码
		 * @param rid 房间号
		 * @param fid  来源, 可选值有 1:pc, 3:ipad, 4:iphone 5:android 
		 * @param content 内容 
		 * @param replayUserid 引用的互动内容发布者的sso_userid。有值时为回复他人互动
		 * @param replayId   引用的互动内容id。有值时为回复他人互动 
		 */
		private void postReply(){
			
			if (!UserInfo.getInstance().isLogin()) {
				Intent intent = new Intent(mActivity, LoginActivity.class);
				startActivity(intent);
				return;
			}
			
			if(StringUtils.isEmpty(roomId)){
				Toast.makeText(mActivity, "直播室参数错误", Toast.LENGTH_SHORT).show();
				return;
			}
			
			String sendContent = HtmlUtils.getSenderText(etContent.getText());
			if(StringUtils.isEmpty(sendContent)){
				Toast.makeText(mActivity, "输入内容", Toast.LENGTH_SHORT).show();
				return;
			}
			
			String url = NetUrlLive.POST_REPLY;
			Log.e(TAG, url);
			Map<String,String> params = new HashMap<String,String>();
			params.put("u", UserInfo.getInstance().getUserName());
//			params.put("p", "passwd");
			params.put("rid", roomId);
			params.put("fid", "5");
			params.put("content", sendContent);
			if(StringUtils.isEmpty(replayUserid) || StringUtils.isEmpty(replayId)){
				
			}else{
				params.put("replay_userid", replayUserid);
				params.put("replay_talk_id", ""+replayId);
			}
			
			Logger.error(TAG, params.toString());
			
			JsonRequest request = new JsonRequest<PostReplyResult>(Method.POST, url,params,
					new RequestHandlerListener<PostReplyResult>(getContext()) {

						@Override
						public void onStart(Request request) {
							super.onStart(request);
//							showDialog(request);
						}

						@Override
						public void onEnd(Request request) {
							super.onEnd(request);
//							hideDialog(request);
						}

						@Override
						public void onSuccess(String id, PostReplyResult data) {
							// TODO Auto-generated method stub
							if(data.getStatus() == 1){
								Toast.makeText(mActivity, "发送成功", Toast.LENGTH_SHORT).show();
								etContent.setText(null);
								onRefresh();
								mList.smoothScrollToPosition(0);
							}else{
								Toast.makeText(mActivity, data.getInfo(), Toast.LENGTH_SHORT).show();
							}
//							Toast.makeText(AttentionDetailActivity.this, "赞成功", Toast.LENGTH_SHORT).show();
							
						}
					}, PostReplyResult.class);

			send(request);
			
		}
}
