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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.fragments.MessageListFragment;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.live.LiveListBean;
import com.gome.haoyuangong.net.result.live.MyLiveListBean;
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
public class MyLiveListActivity extends BaseActivity {

	private static final String TAG = MyLiveListActivity.class.getName();
	public static final String BUNDLE_PARAM_USERID = "BUNDLE_PARAM_USERID";
	
	private static final int PULL_REFRESH = 1;
	private static final int LOAD_MORE = 2;
	private static final int FIRST_LOAD = 3;
	
	private XListView myListView;

	private List<MyLiveListBean.MyLiveData> liveListData = new ArrayList<MyLiveListBean.MyLiveData>();

	private AttentionAdapter myAdapter;

	private ImageLoader imageLoader;
	
	private String userId ;
	
	private FrameLayout nodataLayout;
	private TextView nodataTip;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_refresh_listview_layout);
		setTitle("关注直播室");
		myListView = (XListView) findViewById(R.id.listView);
		myListView.setPullLoadEnable(false);
//		myListView.setPullRefreshEnable(false);
		myListView.setDivider(null);
		myAdapter = new AttentionAdapter(this);
		myListView.setAdapter(myAdapter);
		myListView.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				getLiveList(PULL_REFRESH);
			}
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				
			}
		});
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(id >=0 && id < liveListData.size()){
					MyLiveListBean.MyLiveData room = liveListData.get((int)id);
					if(room != null && room.getDataid() > 0){
						Intent intent = new Intent(MyLiveListActivity.this,LiveRoomActivity.class);
						intent.putExtra(LiveRoomActivity.BUNDLE_PARAM_NAME, room.getSenderinfo().getUserName());
						intent.putExtra(LiveRoomActivity.BUNDLE_PARAM_ROOMID, ""+room.getSenderinfo().getUserId());
						startActivity(intent);
					}else{
						Toast.makeText(MyLiveListActivity.this, "无此直播室", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

//		mQueue = Volley.newRequestQueue(getApplicationContext());
		imageLoader = new ImageLoader(MyLiveListActivity.this);
		nodataLayout = (FrameLayout)findViewById(R.id.nodata_layout);
		nodataTip = (TextView)findViewById(R.id.notdata_tip);
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
				viewHolder.userName.setTextColor(getResources().getColor(R.color.font_595959));
				viewHolder.userRole = (TextView) convertView.findViewById(R.id.user_role);
				viewHolder.userCompany = (TextView) convertView.findViewById(R.id.user_company);
				viewHolder.userStatus = (TextView) convertView.findViewById(R.id.user_status);
				viewHolder.userStatus.setVisibility(View.VISIBLE);
				viewHolder.time = (TextView) convertView.findViewById(R.id.time);
				viewHolder.liveContent = (TextView) convertView.findViewById(R.id.live_content);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			MyLiveListBean.MyLiveData itemData = liveListData.get(position);
			viewHolder.userName.setText(itemData.getSenderinfo().getUserName()+"直播室");
			viewHolder.userRole.setText(itemData.getSenderinfo().getTypeDesc());
//			if(itemData.getStatus() == 0){
//				viewHolder.userStatus.setText("【暂停直播】");
//				viewHolder.userStatus.setTextColor(getResources().getColor(R.color.font_595959));
//			}else if(itemData.getStatus() == 1){
//				viewHolder.userStatus.setText("【正在直播】");
//				viewHolder.userStatus.setTextColor(getResources().getColor(R.color.font_c30114));
//			}else{
//				viewHolder.userStatus.setText("【直播关闭】");
//				viewHolder.userStatus.setTextColor(getResources().getColor(R.color.font_595959));
//			}
			
			viewHolder.userCompany.setText(itemData.getSenderinfo().getCompany());
			viewHolder.time.setText(DateUtils.getTimeAgoString(itemData.getCtime(), "MM-dd HH:mm"));
			viewHolder.liveContent.setText(itemData.getSummary());
			
			imageLoader.downLoadImage(itemData.getSenderinfo().getHeadImage(), viewHolder.userIcon);

			return convertView;
		}

		class ViewHolder {
			ImageView userIcon;
			TextView userName;
			TextView userRole;
			TextView userCompany;
			TextView userStatus;
			TextView time;
			TextView liveContent;
		}
	}
	
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		userId = getIntent().getStringExtra(BUNDLE_PARAM_USERID);
		if(StringUtils.isEmpty(userId)){
			Toast.makeText(this, "无效用户身份", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		getLiveList(FIRST_LOAD);
	}

	private void getLiveList(final int requestType) {

		String url = NetUrlTougu.MY_LIVE_LIST.replace("_userid", userId);
		Log.e(TAG, url);
		JsonRequest<MyLiveListBean> request = new JsonRequest<MyLiveListBean>(Method.GET, url,
				new RequestHandlerListener<MyLiveListBean>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
//						 showDialog(request);
						if(FIRST_LOAD == requestType){
							showLoading(request);
						}
						
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						// hideDialog(request);
						if(FIRST_LOAD == requestType){
							hideLoading(request);
						}
						if(requestType == PULL_REFRESH){
							myListView.stopRefresh();
						}else{
							myListView.stopLoadMore();
						}
					}

					@Override
					public void onSuccess(String id, MyLiveListBean data) {
						// TODO Auto-generated method stub
						if(data == null){
							Toast.makeText(MyLiveListActivity.this, "请求直播室列表失败", Toast.LENGTH_SHORT).show();
						}else{
							
							saveRefreshTime(NetUrlTougu.MY_LIVE_LIST);
							myListView.setRefreshTime(getRefreshTime(NetUrlTougu.MY_LIVE_LIST));
							
							liveListData.clear();
							liveListData.addAll(data.getData().getList());
							myAdapter.notifyDataSetChanged();
							
							if(liveListData.size() == 0){
								myListView.setVisibility(View.GONE);
								nodataLayout.setVisibility(View.VISIBLE);
								nodataTip.setText("暂无直播室消息");
								
							}
							
						}
						
					}
				}, MyLiveListBean.class);

		send(request);

	}
}
