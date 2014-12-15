/**
 * 
 */
package com.gome.haoyuangong.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.haoyuangong.MyApplication;
import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.activity.MessageListActivity;
import com.gome.haoyuangong.activity.MyConsultActivity;
import com.gome.haoyuangong.activity.MyLiveListActivity;
import com.gome.haoyuangong.activity.MySignedActivity;
import com.gome.haoyuangong.activity.TouGuMessageListActivity;
import com.gome.haoyuangong.fragments.DongTaiFragment.PageIndicatorTipListener;
import com.gome.haoyuangong.layout.self.SelfView;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.live.NewMessageResultBean;
import com.gome.haoyuangong.net.url.NetUrlTougu;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.xlistview.XListView;
import com.gome.haoyuangong.views.xlistview.XListView.IXListViewListener;

/**
 * 
 */
public class MessageListFragment extends BaseFragment {

	private static final String TAG = DTOpinionListFragment.class.getName();
	
	private static final String SAVE_KEY = "_message_list";
	private static final String USER_FIRST_OPEN = "USER_FIRST_OPEN";

	public static final int CONTENT_ATTENTION = 0;
	public static final int CONTENT_MESSAGE = 1;

	public static final int MSG_TYPE_SYSTEM = 1;
	public static final int MSG_TYPE_STOCK_YJ = 2;
	public static final int MSG_TYPE_ANSWERME = 3;
	public static final int MSG_TYPE_LIVE = 4;
	public static final int MSG_TYPE_SIGN = 5;
	public static final int MSG_TYPE_ASKME = 6;
	public static final int MSG_TYPE_GUANZHU = 7;
	public static final int MSG_TYPE_COMMENT = 8;
	public static final int MSG_TYPE_GROUP_MSG = 9;
	public static final int MSG_TYPE_REANSWER = 10;
	public static final int MSG_TYPE_REASK = 11;
	public static final int MSG_TYPE_STOCK_GROUP = 13;

	private XListView listView;

	private MyListAdapter myAdapter;

	private List<MsgItemBean> dataList = new ArrayList<MsgItemBean>();
	
	private SparseIntArray typeToIndex = new SparseIntArray();

	private MsgItemBean[] dataArray;

	private int displayType = -1;// 0：游客，1：登录用户，2投顾；
	
	private PageIndicatorTipListener listener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.simple_refresh_listview_layout_withtoph, null);
		findView(v);
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (UserInfo.getInstance().isTougu()) {
			if(displayType != 2){
				dataArray = new MsgItemBean[5];
				dataArray[0] = new MsgItemBean(MSG_TYPE_ASKME, "提问我的",R.drawable.icon_msg_ask);
				dataArray[1] = new MsgItemBean(MSG_TYPE_SIGN, "签约用户",R.drawable.icon_msg_qianyue);
				dataArray[2] = new MsgItemBean(MSG_TYPE_STOCK_YJ, "股价预警",R.drawable.icon_msg_yujing);
				dataArray[3] = new MsgItemBean(MSG_TYPE_LIVE, "直播室",R.drawable.icon_msg_live);
				dataArray[4] = new MsgItemBean(MSG_TYPE_SYSTEM, "系统通知",R.drawable.icon_msg_sys);
				displayType = 2;
				typeToIndex.clear();
				typeToIndex.put(MSG_TYPE_ASKME, 0);
				typeToIndex.put(MSG_TYPE_SIGN, 1);
				typeToIndex.put(MSG_TYPE_STOCK_YJ, 2);
				typeToIndex.put(MSG_TYPE_LIVE, 3);
				typeToIndex.put(MSG_TYPE_SYSTEM, 4);
			}
		} else if (!UserInfo.getInstance().isTougu() && UserInfo.getInstance().isLogin()) {
			if( displayType != 1){
				dataArray = new MsgItemBean[5];
				dataArray[0] = new MsgItemBean(MSG_TYPE_ANSWERME, "回答我的",R.drawable.icon_msg_answer);
				dataArray[1] = new MsgItemBean(MSG_TYPE_GROUP_MSG, "签约投顾",R.drawable.icon_msg_qunfa);
				dataArray[2] = new MsgItemBean(MSG_TYPE_STOCK_YJ, "股价预警",R.drawable.icon_msg_yujing);
				dataArray[3] = new MsgItemBean(MSG_TYPE_LIVE, "直播室",R.drawable.icon_msg_live);
				dataArray[4] = new MsgItemBean(MSG_TYPE_SYSTEM, "系统通知",R.drawable.icon_msg_sys);
				displayType = 1;
				typeToIndex.clear();
				typeToIndex.put(MSG_TYPE_ANSWERME, 0);
				typeToIndex.put(MSG_TYPE_GROUP_MSG, 1);
				typeToIndex.put(MSG_TYPE_STOCK_YJ, 2);
				typeToIndex.put(MSG_TYPE_LIVE, 3);
				typeToIndex.put(MSG_TYPE_SYSTEM, 4);
			}
		} else if(!UserInfo.getInstance().isTougu() && !UserInfo.getInstance().isLogin()){
			if(displayType != 0){
				dataArray = new MsgItemBean[1];
				dataArray[0] = new MsgItemBean(MSG_TYPE_SYSTEM, "系统通知",R.drawable.icon_msg_sys);
				displayType = 0;
				typeToIndex.clear();
				typeToIndex.put(MSG_TYPE_SYSTEM, 0);
			}
		}
		String prefix = UserInfo.getInstance().getUserId();
		readMessageList(prefix+SAVE_KEY);
		
		Map<Integer,Integer> newIds = MyApplication.getAllChanged();
		if(newIds != null){
			for(Map.Entry<Integer, Integer> e : newIds.entrySet()){
				Integer key = e.getKey();
				Integer value = e.getValue();
				if(key != null && value != null){
					int index = typeToIndex.get(key);
					if (index >= 0 && index < dataArray.length && dataArray[index].getNewMaxId() < value) {
						dataArray[index].setNewMaxId(value);
					}
				}
			}
			saveMessageList(prefix+SAVE_KEY);
		}
		
		dataList.clear();
		dataList.addAll(Arrays.asList(dataArray));
		myAdapter.notifyDataSetChanged();
		getNewMessageIds();
	}

	private void findView(View v) {

		listView = (XListView) v.findViewById(R.id.listView);
		listView.setDivider(null);
		myAdapter = new MyListAdapter();
		listView.setAdapter(myAdapter);
		listView.setPullLoadEnable(false);
		// listView.setPullRefreshEnable(false);
		listView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				getNewMessageIds();
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub

			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				if (id >= 0 && id < dataList.size()) {
					MsgItemBean item = dataList.get((int) id);
					if(item.getLastMaxId() != item.getNewMaxId()){
						item.setLastMaxId(item.getNewMaxId());
						String prefix = UserInfo.getInstance().getUserId();
						saveMessageList(prefix+SAVE_KEY);
					}
					
					myAdapter.notifyDataSetChanged();
					int type = item.getType();
					switch (type) {
					case MSG_TYPE_SYSTEM: {
						Intent intent = new Intent(mActivity, MessageListActivity.class);
						intent.putExtra(MessageListActivity.BUNDLE_PARAM_TITLE, "系统通知");
						intent.putExtra(MessageListActivity.BUNDLE_PARAM_USERID, UserInfo.getInstance().getUserId());
						intent.putExtra(MessageListActivity.BUNDLE_PARAM_MTYPE, MSG_TYPE_SYSTEM);
						startActivity(intent);
						break;
					}
					case MSG_TYPE_LIVE: {
						Intent intent = new Intent(mActivity, MyLiveListActivity.class);
						// intent.putExtra(MyLiveListActivity.BUNDLE_PARAM_USERID,
						// UserInfo.getInstance().getUserId());
						intent.putExtra(MyLiveListActivity.BUNDLE_PARAM_USERID, UserInfo.getInstance().getUserId());
						startActivity(intent);
						break;
					}
					case MSG_TYPE_STOCK_YJ: {
						Intent intent = new Intent(mActivity, MessageListActivity.class);
						intent.putExtra(MessageListActivity.BUNDLE_PARAM_TITLE, "股价预警");
						intent.putExtra(MessageListActivity.BUNDLE_PARAM_USERID, UserInfo.getInstance().getUserId());
						intent.putExtra(MessageListActivity.BUNDLE_PARAM_MTYPE, MSG_TYPE_STOCK_YJ);
						startActivity(intent);
						break;
					}
					case MSG_TYPE_GROUP_MSG: {
						Intent intent = new Intent(mActivity, TouGuMessageListActivity.class);
						intent.putExtra(MessageListActivity.BUNDLE_PARAM_USERID, UserInfo.getInstance().getUserId());
						startActivity(intent);
						break;
					}
					case MSG_TYPE_SIGN: {
						Intent intent = new Intent(mActivity, MySignedActivity.class);
						intent.putExtra("name", "我");
						intent.putExtra("viewid", UserInfo.getInstance().getUserId());
						intent.putExtra("usertype",
								UserInfo.getInstance().isTougu() ? SelfView.UserType.utInvester.ordinal()
										: SelfView.UserType.utUser.ordinal());
//						intent.putExtra("viewtype", "pinyin");
						startActivity(intent);
						break;
					}
					case MSG_TYPE_ASKME: {
						Intent intent = new Intent(mActivity, MyConsultActivity.class);
						intent.putExtra("viewid", UserInfo.getInstance().getUserId());
						intent.putExtra("name", UserInfo.getInstance().getUserName());
						intent.putExtra("usertype",
								UserInfo.getInstance().isTougu() ? SelfView.UserType.utInvester.ordinal()
										: SelfView.UserType.utUser.ordinal());
						startActivity(intent);
						break;
					}
					case MSG_TYPE_ANSWERME: {
						Intent intent = new Intent(mActivity, MyConsultActivity.class);
						intent.putExtra("viewid", UserInfo.getInstance().getUserId());
						intent.putExtra("name", UserInfo.getInstance().getUserName());
						intent.putExtra("usertype",
								UserInfo.getInstance().isTougu() ? SelfView.UserType.utInvester.ordinal()
										: SelfView.UserType.utUser.ordinal());
						startActivity(intent);
						break;
					}
					}
				}
			}
		});
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
			MessageViewHolder mViewHolder;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.dongtai_message_item_simple, parent, false);
				mViewHolder = new MessageViewHolder();
				mViewHolder.iconView = (ImageView) convertView.findViewById(R.id.imageView);
				mViewHolder.tipView = (TextView) convertView.findViewById(R.id.tip_tv);
				mViewHolder.title = (TextView) convertView.findViewById(R.id.item_title);
				mViewHolder.bandBg = convertView.findViewById(R.id.band_bg);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (MessageViewHolder) convertView.getTag();
			}

			MsgItemBean item = dataList.get(position);
			mViewHolder.iconView.setBackgroundResource(item.getResId());
			if (item != null) {
				mViewHolder.title.setText(item.getName());
				if (item.getLastMaxId() > 0 && item.getNewMaxId() > item.getLastMaxId()) {
					mViewHolder.tipView.setVisibility(View.VISIBLE);
				} else {
					mViewHolder.tipView.setVisibility(View.GONE);
				}
			}
			return convertView;
		}

		class MessageViewHolder {
			ImageView iconView;
			TextView tipView;
			TextView title;
			View bandBg;
		}

	}

	private void getNewMessageIds() {

		String url = NetUrlTougu.MY_NEW_MESSAGE_LIST.replace("_userid", UserInfo.getInstance().getUserId() == null?"":UserInfo.getInstance().getUserId());
		Logger.error(TAG, url);
		JsonRequest request = new JsonRequest<NewMessageResultBean>(Method.GET, url, null,
				new RequestHandlerListener<NewMessageResultBean>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						// showDialog(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						// hideDialog(request);
						listView.stopRefresh();
					}

					@Override
					public void onSuccess(String id, NewMessageResultBean data) {
						// TODO Auto-generated method stub
						if (data != null) {
							saveRefreshTime(NetUrlTougu.MY_NEW_MESSAGE_LIST);
							listView.setRefreshTime(getRefreshTime(NetUrlTougu.MY_NEW_MESSAGE_LIST));
							
							for (NewMessageResultBean.NewMsgBean bean : data.getData().getList()) {
								int index = typeToIndex.get(bean.getType());
								if (index >= 0 && index < dataArray.length) {
									dataArray[index].setNewMaxId(bean.getMaxID());
									myAdapter.notifyDataSetChanged();
								}
								
								String prefix = UserInfo.getInstance().getUserId();
								saveMessageList(prefix+SAVE_KEY);
							}

						}
						listView.stopRefresh();
					}
				}, NewMessageResultBean.class);

		send(request);
	}

	private class MsgItemBean {
		private int type;
		private String name;
		private int lastMaxId;
		private int newMaxId;
		private int resId;

		public MsgItemBean(int type, String name,int resid) {
			this.type = type;
			this.name = name;
			this.resId = resid;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getLastMaxId() {
			return lastMaxId;
		}

		public void setLastMaxId(int lastMaxId) {
			this.lastMaxId = lastMaxId;
		}

		public int getNewMaxId() {
			return newMaxId;
		}

		public void setNewMaxId(int newMaxId) {
			this.newMaxId = newMaxId;
		}

		public int getResId() {
			return resId;
		}

		public void setResId(int resId) {
			this.resId = resId;
		}
		
	}
	
	private void saveMessageList(String key){
		SharedPreferences.Editor editor = mActivity.getSharedPreferences(key, Activity.MODE_PRIVATE).edit();
		if(dataArray != null){
			StringBuilder types = new StringBuilder();
			for(int i = 0 ; i < dataArray.length ; i++){
				String typeKey = ""+dataArray[i].getType();
				String typeValue = dataArray[i].getLastMaxId()+"_"+dataArray[i].getNewMaxId();
				editor.putString(typeKey, typeValue);
				types.append("_").append(typeKey);
			} 
			if(types.length() > 0){
				editor.putString("types", types.substring(1));
			}
		}
		editor.commit();
		checkHasNew();
	}
	
	private void readMessageList(String key){
		SharedPreferences reader = mActivity.getSharedPreferences(key, Activity.MODE_PRIVATE);
		if(dataArray != null){
			String typesStr = reader.getString("types", null);
			if(!StringUtils.isEmpty(typesStr)){
				String[] types = typesStr.split("_");
				for(String type : types){
					int typeInt = Integer.parseInt(type);
					int index = typeToIndex.get(typeInt);
					if(index < 0 || index >= dataArray.length){
						continue;
					}
					String valueStr = reader.getString(type, null);
					if(!StringUtils.isEmpty(valueStr)){
						String[] values = valueStr.split("_");
						if(values.length == 2){
							dataArray[index].setLastMaxId(Integer.parseInt(values[0]));
							dataArray[index].setNewMaxId(Integer.parseInt(values[1]));
						}
					}
				}
			}
				
		}
		checkHasNew();
	}
	
	public void setPageIndicatorTipListener(PageIndicatorTipListener listener){
		this.listener = listener;
	}
	
	private void checkHasNew(){
		boolean hasNew = false;
		if(dataArray != null){
			for(int i = 0;i < dataArray.length ; i++){
				if(dataArray[i].getLastMaxId() > 0 && dataArray[i].getLastMaxId() < dataArray[i].getNewMaxId()){
					hasNew = true;
					break;
				}
			}
		}
		if(hasNew && listener != null){
			listener.onTipChanged(View.VISIBLE);
		}else if(listener != null){
			listener.onTipChanged(View.GONE);
		}
	}
	
}
