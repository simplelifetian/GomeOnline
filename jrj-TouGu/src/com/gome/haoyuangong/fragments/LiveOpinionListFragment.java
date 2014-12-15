/**
 * 
 */
package com.gome.haoyuangong.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.gome.haoyuangong.R;
import com.gome.haoyuangong.activity.LiveRoomActivity.OnNewCountListener;
import com.gome.haoyuangong.dialog.BottomShareDialog;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.live.LiveOpinionBean;
import com.gome.haoyuangong.net.result.live.NewLiveCountResult;
import com.gome.haoyuangong.net.url.NetUrlLive;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.HtmlUtils;
import com.gome.haoyuangong.utils.JSONUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.xlistview.XListView;
import com.gome.haoyuangong.views.xlistview.XListView.IXListViewListener;

/**
 * 
 */
public class LiveOpinionListFragment extends BaseFragment implements IXListViewListener {

	private static final String TAG = LiveOpinionListFragment.class.getName();
	public static final String BUNDLE_PARAM_ROOMID = "room_id";

	private static final int LIST_TYPE_REFRESH=1;
	private static final int LIST_TYPE_LOADMORE=2;

	private MyListAdapter myAdapter;
	protected XListView mList;

	private List<LiveOpinionBean.LiveOpinionItem> dataList = new ArrayList<LiveOpinionBean.LiveOpinionItem>();
	
	private ImageLoader imageLoader;
	private String roomId;
	private String order = "desc";
	private int lastLoadMoreId = 0;
	private int pageSize = 20;
	
	private RequestQueue mQueue;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if(args == null){
			
		}else{
			roomId = args.getString(BUNDLE_PARAM_ROOMID);
		}
		mQueue = Volley.newRequestQueue(mActivity); 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup vg = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.simple_fragment_list, null);
		setContent(v);
		init(v);
		return vg;
	}
	
	public void init(View v) {
		hideTitle();
		mList = (XListView) v.findViewById(R.id.list);
		mList.setXListViewListener(this);
		mList.setOnScrollListener(new OnScrollListener() {
			
			private int firstVisibleItem = -1;
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				Logger.error(TAG, "onScrollStateChanged : scrollState="+scrollState);
				if(scrollState == SCROLL_STATE_IDLE){
					if(firstVisibleItem > 1){
						stopRefreshing();
					}else{
						startRefreshing(false);
					}
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
//				Logger.error(TAG, "onScroll : firstVisibleItem ="+firstVisibleItem);
				this.firstVisibleItem = firstVisibleItem;
			}
		});
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		imageLoader = new ImageLoader(mActivity);
		myAdapter = new MyListAdapter();
		mList.setAdapter(myAdapter);
		
		startRefreshing(true);
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
				convertView = layoutInflater.inflate(R.layout.live_opinion_item, parent, false);
				aViewHolder = new ViewHolder();
				aViewHolder.opinionContent = (TextView)convertView.findViewById(R.id.op_content);
				aViewHolder.opTime = (TextView)convertView.findViewById(R.id.op_time);
				aViewHolder.opShare = (TextView)convertView.findViewById(R.id.share_tv);
				aViewHolder.opImage = (ImageView)convertView.findViewById(R.id.op_image);
				convertView.setTag(aViewHolder);
			} else {
				aViewHolder = (ViewHolder) convertView.getTag();
			}
			
			LiveOpinionBean.LiveOpinionItem item = dataList.get(position);
			
			if(StringUtils.isEmpty(item.getLinkPic())){
				aViewHolder.opImage.setVisibility(View.GONE);
			}else{
				aViewHolder.opImage.setVisibility(View.VISIBLE);
				imageLoader.downLoadImage(item.getLinkPic(), aViewHolder.opImage);
			}
			
			aViewHolder.opinionContent.setText(item.getmContentSSB());
			aViewHolder.opTime.setText(item.getTime());
//			aViewHolder.opShare.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					openShareDialog();
//				}
//			});
			
			return convertView;
		}

		class ViewHolder {
			TextView opinionContent;
			TextView opTime;
			TextView opShare;
			ImageView opImage;
		}
		
	}
	
	private void openShareDialog(){
		BottomShareDialog dialog = new BottomShareDialog(mActivity);
		dialog.setOnConfirmListener(new BottomShareDialog.OnConfirmListener() {
			@Override
			public void onConfirm(int index) {
				
			}
		});
		dialog.show();
	}
	
	
//	@Override
//	protected <T> Request<T> getRequest() {
//		// TODO Auto-generated method stub
//		String url = String.format(NetUrlLive.OPINION_LIST, roomId, order, lastId , pageSize);
//		Logger.error(TAG, url);
//		JsonRequest request = new JsonRequest<LiveOpinionBean>(Method.GET, url, null, LiveOpinionBean.class);
//		return request;
//	}
//
//	@Override
//	protected void onRefreshPrepear() {
//		// TODO Auto-generated method stub
//		lastId = 0;
//	}
//
//	@Override
//	protected void onLoadMorePrepear() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	protected void onReceive(boolean isLoadMore, String id, Object data) {
//		// TODO Auto-generated method stub
//		LiveOpinionBean liveOpinionBean = (LiveOpinionBean) data;
//		if (!isLoadMore) {
//			dataList.clear();
//		}
//		for(LiveOpinionBean.LiveOpinionItem dataTtem : liveOpinionBean.getData()){
//			String content = StringUtils.decodeUnicode(dataTtem.getContent());
//	        content = content.toLowerCase();
//	        content = StringUtils.fiterHtmlTag(content, "a");
//	        content = content.replace("<p style=\"color: black;\">", " ");
//	        content = content.replace("<p>", "\n");
//	        content = content.replace("</p>", "");
////	        content = content.replace("&nbsp;", " ");
//	        content = content.replace("strong", "b");
//	        content = StringUtils.dealColorTag(content);
//	        content = content.replace("class=\"user\"", "color=\"blue\"");
////	        loi.setImageAndTextFlag(haveImageTag(content));
//	        dataTtem.setmContentSSB(HtmlUtils.parseImgTag(mActivity,getScreenW(),content,dataTtem));
//		}
//		
//		
//		dataList.addAll(liveOpinionBean.getData());
//		myAdapter.notifyDataSetChanged();
//		if(!liveOpinionBean.getData().isEmpty()){
//			LiveOpinionBean.LiveOpinionItem item = liveOpinionBean.getData().get(liveOpinionBean.getData().size() - 1);
//			lastId = Integer.parseInt(item.getId());
//		}
//		if(liveOpinionBean.getData().size() < pageSize){
//			mList.setPullLoadEnable(false);
//		}else{
//			mList.setPullLoadEnable(true);
//		}
//	}
	
	
	private GetNewOpinionTask getNewOpinionTask;
	private GetNewCountTask getNewCountTask;
	private int lastRefreshId;
	private OnNewCountListener listener;
	
	private static final int REFRESH_INTERVAL = 5000;
	
	private Timer newOptionTimer = new Timer();  
	private Timer newCountTimer = new Timer();  
	
	
	private class GetNewOpinionTask extends TimerTask{

		private com.android.volley.Request<String> runningRequest;
		private boolean isRunning ;
		private boolean requesting = false;
		private int refreshType = 1;//1 强制刷新
		public GetNewOpinionTask(){
//			request = createNewOptions(LIST_TYPE_REFRESH);
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(isRunning ){
				
				if(!requesting){
					com.android.volley.toolbox.StringRequest newRequest = createNewOptionsRequest(LIST_TYPE_REFRESH,refreshType);
					refreshType = 0 ;
					if(newRequest != null){
						runningRequest = mQueue.add(newRequest);
					}
				}
			}
		}
		
		public void pause(){
			isRunning = false;
			if(runningRequest != null){
				runningRequest.cancel();
				runningRequest = null;
			}
			requesting = false;
		}
		
		public void resume(){
			isRunning = true;
		}
		
		private com.android.volley.toolbox.StringRequest createNewOptionsRequest(final int type,final int refreshType){
			if(refreshType == 1){
				lastRefreshId = 0;
			}
			String url = String.format(NetUrlLive.OPINION_LIST, roomId,"new", order, lastRefreshId , pageSize);
			Logger.error(TAG, url);
			
//			JsonRequest<LiveOpinionBean> request = new JsonRequest<LiveOpinionBean>(Method.GET, url,
//					new RequestHandlerListener<LiveOpinionBean>(getContext()) {
//
//						@Override
//						public void onStart(Request request) {
//							super.onStart(request);
//							requesting = true;
////							 showDialog(request);
//						}
//
//						@Override
//						public void onEnd(Request request) {
//							super.onEnd(request);
//							requesting = false;
////							 hideDialog(request);
//						}
//
//						@Override
//						public void onSuccess(String id, LiveOpinionBean data) {
//							// TODO Auto-generated method stub
//							
//						}
//					}, LiveOpinionBean.class);
			requesting = true;
			com.android.volley.toolbox.StringRequest stringRequest = new com.android.volley.toolbox.StringRequest(url,  
		                            new Response.Listener<String>() {  
		                                @Override  
		                                public void onResponse(String response) {
		                                	Logger.error(TAG, response);
		                                	requesting = false;
		                                	if(StringUtils.isEmpty(response)){
		                                		
		                                	}else{
		                                		saveRefreshTime(NetUrlLive.OPINION_LIST);
		                                		mList.setRefreshTime(getRefreshTime(NetUrlLive.OPINION_LIST));
		                                		
		                                		LiveOpinionBean data = JSONUtils.parseObject(response, LiveOpinionBean.class);
		                                		if(data.getRetCode() == 0){
		                                			Message msg = updateUIHandler.obtainMessage(1, data);
			            							msg.arg1 = type;
			            							msg.arg2 = refreshType;
			            							updateUIHandler.sendMessage(msg);
		                                		}else{
		                                			
		                                		}
		                                	}
		                                }  
		                            }, new Response.ErrorListener() {  
		                                @Override  
		                                public void onErrorResponse(VolleyError error) {  
		                                	requesting = false;
		                                    Log.e("TAG", error.getMessage(), error);  
		                                }  
		                            });  
			 
			return stringRequest;
		}
		
	}
	
	private class GetNewCountTask extends TimerTask{

		private com.android.volley.Request<String> runningRequest;
		private boolean isRunning;
		private boolean requesting = false;
		public GetNewCountTask(){
//			request = createNewOptions(LIST_TYPE_REFRESH);
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(isRunning){
				if(!requesting){
					com.android.volley.toolbox.StringRequest newRequest = createNewCountRequest();
					if(newRequest != null){
						runningRequest = mQueue.add(newRequest);
					}
					
				}
			}
		}
		
		public void pause(){
			isRunning = false;
			if(runningRequest != null){
				runningRequest.cancel();
				runningRequest = null;
			}
			requesting = false;
		}
		
		public void resume(){
			isRunning = true;
		}
		
		private com.android.volley.toolbox.StringRequest createNewCountRequest(){
			if(StringUtils.isEmpty(roomId)){
				return null;
			}
			String url = String.format(NetUrlLive.OPINION_NEW_COUNT, roomId,lastRefreshId);
			Log.e(TAG, url);
//			JsonRequest<NewLiveCountResult> request = new JsonRequest<NewLiveCountResult>(Method.GET, url,
//					new RequestHandlerListener<NewLiveCountResult>(getContext()) {
//
//						@Override
//						public void onStart(Request request) {
//							super.onStart(request);
//							requesting = true;
////							 showDialog(request);
//						}
//
//						@Override
//						public void onEnd(Request request) {
//							super.onEnd(request);
//							requesting = false;
////							 hideDialog(request);
//						}
//
//						@Override
//						public void onSuccess(String id, NewLiveCountResult data) {
//							// TODO Auto-generated method stub
//							if(data != null && data.getStatus() == 1){
//								Message msg = updateUIHandler.obtainMessage(2, data);
//								updateUIHandler.sendMessage(msg);
//							}else{
//								
//							}
//						}
//					}, NewLiveCountResult.class);
			requesting = true;
			com.android.volley.toolbox.StringRequest stringRequest = new com.android.volley.toolbox.StringRequest(url,  
                    new Response.Listener<String>() {  
                        @Override  
                        public void onResponse(String response) {
                        	Logger.error(TAG, response);
                        	requesting = false;
                        	if(StringUtils.isEmpty(response)){
                        		
                        	}else{
                        		NewLiveCountResult data = JSONUtils.parseObject(response, NewLiveCountResult.class);
                        		if(data != null && data.getStatus() == 1){
    								Message msg = updateUIHandler.obtainMessage(2, data);
    								updateUIHandler.sendMessage(msg);
    							}else{
    								
    							}
                        	}
                        }  
                    }, new Response.ErrorListener() {  
                        @Override  
                        public void onErrorResponse(VolleyError error) {  
                        	requesting = false;
                        	Logger.error("TAG", error.getMessage(), error);  
                        }  
                    });

			return stringRequest;
		}
		
	}
	
	private void  getLoadMoreOptions(final int type){
		
		if(LIST_TYPE_LOADMORE != type){
			Toast.makeText(mActivity, "错误请求", Toast.LENGTH_SHORT).show();
			return;
		}
		if(!dataList.isEmpty()){
			LiveOpinionBean.LiveOpinionItem item = dataList.get(dataList.size() - 1);
			lastLoadMoreId = Integer.parseInt(item.getId());
		}else{
			lastLoadMoreId = 0;
		}
		String url = String.format(NetUrlLive.OPINION_LIST, roomId,"old", order, lastLoadMoreId , pageSize);
		Logger.error(TAG, url);
		JsonRequest request = new JsonRequest<LiveOpinionBean>(Method.GET, url,
				new RequestHandlerListener<LiveOpinionBean>(getContext()) {

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
					public void onSuccess(String id, LiveOpinionBean data) {
						// TODO Auto-generated method stub
						Message msg = updateUIHandler.obtainMessage(1, data);
						msg.arg1 = type;
						updateUIHandler.sendMessage(msg);
					}
				}, LiveOpinionBean.class);
		send(request);
	}
	

	
	private Handler updateUIHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
				case 1:{//update new opinion
					LiveOpinionBean liveOpinionBean = (LiveOpinionBean)msg.obj;
					for(LiveOpinionBean.LiveOpinionItem dataTtem : liveOpinionBean.getData()){
						String content = StringUtils.decodeUnicode(dataTtem.getContent());
				        content = content.toLowerCase();
				        content = StringUtils.fiterHtmlTag(content, "a");
				        content = content.replace("<p style=\"color: black;\">", " ");
				        content = content.replace("<p>", "\n");
				        content = content.replace("</p>", "");
//				        content = content.replace("&nbsp;", " ");
				        content = content.replace("strong", "b");
				        content = StringUtils.dealColorTag(content);
				        content = content.replace("class=\"user\"", "color=\"blue\"");
//				        loi.setImageAndTextFlag(haveImageTag(content));
				        dataTtem.setmContentSSB(HtmlUtils.parseImgTag(mActivity,getScreenW(),content,dataTtem));
					}
					
					if (msg.arg1 == LIST_TYPE_REFRESH) {
						if(msg.arg2 == 1){
							dataList.clear();
						}
						dataList.addAll(0, liveOpinionBean.getData());
					}else{
						dataList.addAll(liveOpinionBean.getData());
					}
					
					if(!dataList.isEmpty()){
						LiveOpinionBean.LiveOpinionItem first = dataList.get(0);
						lastRefreshId = Integer.parseInt(first.getId());
						LiveOpinionBean.LiveOpinionItem last = dataList.get(dataList.size() - 1);
						lastLoadMoreId = Integer.parseInt(last.getId());
					}
					
					myAdapter.notifyDataSetChanged();
//					if(!liveOpinionBean.getData().isEmpty()){
//						LiveOpinionBean.LiveOpinionItem item = liveOpinionBean.getData().get(liveOpinionBean.getData().size() - 1);
//						lastLoadMoreId = Integer.parseInt(item.getId());
//					}
					if(msg.arg1 == LIST_TYPE_REFRESH){
						mList.stopRefresh();
					}else{
						mList.stopLoadMore();
					}
					if(liveOpinionBean.getData().size() < pageSize){
						mList.setPullLoadEnable(false);
					}else{
						mList.setPullLoadEnable(true);
					}
					
					break;
				}
				case 2:{//update new opinion
					NewLiveCountResult result = (NewLiveCountResult)msg.obj;
					if(result.getData().getCount() > 0){
						//提示新消息
						if(listener != null){
							listener.action(result.getData().getCount());
						}
					}else{
//						Toast.makeText(mActivity, "获取数据失败", Toast.LENGTH_SHORT).show();
					}
					break;
				}
			}
		}
	};

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		lastLoadMoreId = 0;
		startRefreshing(true);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		getLoadMoreOptions(LIST_TYPE_LOADMORE);
	}

	public void startRefreshing(boolean rightNow){
		if(getNewCountTask != null){
			getNewCountTask.pause();
		}
		
		if(rightNow){
			if(getNewOpinionTask != null){
				getNewOpinionTask.pause();
				getNewOpinionTask.cancel();
			}
			
			getNewOpinionTask = new GetNewOpinionTask();
			getNewOpinionTask.resume();
			newOptionTimer.schedule(getNewOpinionTask, 0, REFRESH_INTERVAL);
			mList.smoothScrollToPosition(0);
		}else{
			if(getNewOpinionTask == null){
				getNewOpinionTask = new GetNewOpinionTask();
				getNewOpinionTask.resume();
				newOptionTimer.schedule(getNewOpinionTask, 0, REFRESH_INTERVAL);
			}else{
				getNewOpinionTask.resume();
			}
		}
		
	}
	
	public void stopRefreshing(){
		
		if(getNewOpinionTask != null){
			getNewOpinionTask.pause();
		}
		
		if(getNewCountTask == null){
			getNewCountTask = new GetNewCountTask();
			getNewCountTask.resume();
			newCountTimer.schedule(getNewCountTask, 0, REFRESH_INTERVAL);
		}else{
			getNewCountTask.resume();
		}
		
		
	}
	
	public void setOnNewCountListener(OnNewCountListener listener){
		this.listener = listener;
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(getNewOpinionTask != null){
			getNewOpinionTask.pause();
			getNewOpinionTask.cancel();
		}
		
		if(getNewCountTask != null){
			getNewCountTask.pause();
			getNewCountTask.cancel();
		}
		newCountTimer.cancel();
		newOptionTimer.cancel();
	}
}
