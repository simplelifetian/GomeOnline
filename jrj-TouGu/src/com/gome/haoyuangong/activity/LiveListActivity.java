/**
 * 
 */
package com.gome.haoyuangong.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.live.LiveListBean;
import com.gome.haoyuangong.net.url.NetUrlLive;
import com.gome.haoyuangong.net.url.NetUrlTougu;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.DateUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.xlistview.XListView;
import com.gome.haoyuangong.views.xlistview.XListView.IXListViewListener;

/**
 * 
 */
public class LiveListActivity extends BaseActivity {

	private static final String TAG = LiveListActivity.class.getName();
	
	private static final int PULL_REFRESH = 1;
	private static final int LOAD_MORE = 2;
	private static final int FIRST_LOAD = 3;

	private XListView myListView;

	private List<LiveListBean.LiveData> liveListData = new ArrayList<LiveListBean.LiveData>();

	private AttentionAdapter myAdapter;

//	RequestQueue mQueue;
	
	private int currPage = 0;
	private int pageSize = 20;
	
	private ImageLoader imageLoader;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_refresh_listview_layout);
		setTitle("直播室");
		myListView = (XListView) findViewById(R.id.listView);
//		myListView.setPullRefreshEnable(false);
		myListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				getLiveList(1, pageSize,PULL_REFRESH);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				getLiveList(currPage+1, pageSize,LOAD_MORE);
			}

		});
		myListView.setDivider(null);
		myAdapter = new AttentionAdapter(this);
		myListView.setAdapter(myAdapter);
		
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(id >=0 && id < liveListData.size()){
					LiveListBean.LiveData room = liveListData.get((int)id);
					if(room != null && !StringUtils.isEmpty(room.getRoom_id())){
						Intent intent = new Intent(LiveListActivity.this,LiveRoomActivity.class);
						intent.putExtra(LiveRoomActivity.BUNDLE_PARAM_NAME, room.getUserName());
						intent.putExtra(LiveRoomActivity.BUNDLE_PARAM_ROOMID, room.getRoom_id());
						startActivity(intent);
					}else{
						Toast.makeText(LiveListActivity.this, "无此直播室", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

//		mQueue = Volley.newRequestQueue(getApplicationContext());
		imageLoader = new ImageLoader(LiveListActivity.this);
		loadIv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getLiveList(1, pageSize,FIRST_LOAD);
			}
		});
		getLiveList(1, pageSize,FIRST_LOAD);
	}

	class AttentionAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public AttentionAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return liveListData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return liveListData.get(position);
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
				convertView = mInflater.inflate(R.layout.live_listitem_faxian, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.userIcon = (ImageView) convertView.findViewById(R.id.user_icon);
				viewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
				viewHolder.userRole = (TextView) convertView.findViewById(R.id.user_role);
				viewHolder.userCompany = (TextView) convertView.findViewById(R.id.user_company);
				viewHolder.time = (TextView) convertView.findViewById(R.id.time);
				viewHolder.liveContent = (TextView) convertView.findViewById(R.id.live_content);
				viewHolder.ivCertif = (ImageView) convertView.findViewById(R.id.iv_certif);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final LiveListBean.LiveData itemData = liveListData.get(position);
			viewHolder.userName.setText(itemData.getUserName());
			viewHolder.userRole.setText(itemData.getTypeDesc());
			viewHolder.userCompany.setText(itemData.getCompany());
			viewHolder.time.setText(DateUtils.getTimeAgoString(itemData.getTimeStamp(), "MM-dd HH:mm"));
			viewHolder.liveContent.setText(itemData.getLastContent());
			
			if (1 == itemData.getSignV()) {
				viewHolder.ivCertif.setVisibility(View.VISIBLE);
			} else {
				viewHolder.ivCertif.setVisibility(View.GONE);
			}
			
			imageLoader.downLoadImage(itemData.getHeadPic(), viewHolder.userIcon);
			viewHolder.userIcon.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(LiveListActivity.this,ViewInvesterInfoActivity.class);
					intent.putExtra("USERNAME", itemData.getUserName());
					intent.putExtra("USERID", itemData.getUserid());
					startActivity(intent);
				}
			});

			return convertView;
		}

		class ViewHolder {
			ImageView userIcon;
			TextView userName;
			TextView userRole;
			TextView userCompany;
			TextView time;
			TextView liveContent;
			ImageView ivCertif;
		}
	}

	private void getLiveList(final int p, final int pz,final int requestType) {

		String url = String.format(NetUrlLive.LIVE_LIST, pz, p);
		Log.e(TAG, url);
		JsonRequest<LiveListBean.LiveData[]> request = new JsonRequest<LiveListBean.LiveData[]>(Method.GET, url,
				new RequestHandlerListener<LiveListBean.LiveData[]>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						if(FIRST_LOAD == requestType){
							showLoading(request);
						}
						
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						if(FIRST_LOAD == requestType){
							hideLoading(request);
						}
					}

					@Override
					public void onSuccess(String id, LiveListBean.LiveData[] data) {
						// TODO Auto-generated method stub
						if(data == null){
							Toast.makeText(LiveListActivity.this, "请求直播室列表失败", Toast.LENGTH_SHORT).show();
						}else{
							
							saveRefreshTime(NetUrlLive.LIVE_LIST);
							myListView.setRefreshTime(getRefreshTime(NetUrlLive.LIVE_LIST));
							
							if (p == 1) {
								liveListData.clear();
							}
							liveListData.addAll(Arrays.asList(data));
							myAdapter.notifyDataSetChanged();
							currPage = p;
							if(requestType == 1){
								myListView.stopRefresh();
							}else{
								myListView.stopLoadMore();
							}
							if(data.length < pageSize){
								myListView.setPullLoadEnable(false);
							}else{
								myListView.setPullLoadEnable(true);
							}
						}
						
					}
				}, LiveListBean.LiveData[].class);

		send(request);

	}
}
