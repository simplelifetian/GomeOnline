/**
 * 
 */
package com.gome.haoyuangong.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.MyApplication;
import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.activity.AttentionDetailActivity;
import com.gome.haoyuangong.activity.MessageListActivity;
import com.gome.haoyuangong.bean.Stock;
import com.gome.haoyuangong.db.QuoteDic;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.tougu.HotOpinionListBean;
import com.gome.haoyuangong.net.result.tougu.MessageItemResult;
import com.gome.haoyuangong.net.result.tougu.SysMessageItemResult;
import com.gome.haoyuangong.net.url.NetUrlTougu;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.DateUtils;
import com.gome.haoyuangong.utils.StringUtils;

/**
 * 
 */
public class CommonMessageListFragment extends XListFragment {

	private static final String TAG = CommonMessageListFragment.class.getName();

	public static final String BUNDLE_PARAM_USERID = "BUNDLE_PARAM_USERID";
	public static final String BUNDLE_PARAM_MTYPE = "BUNDLE_PARAM_MTYPE";
	
	private static final String SAVED_LASTID = "_SAVED_LASTID";

	private MessageAdapter myAdapter;

	private List<MessageItemResult.MsgBean> msgListData = new ArrayList<MessageItemResult.MsgBean>();

	// private int currPage = 0;
	private int pageSize = 20;
	private String direction;
	private String directionDown = "down";
	private String directionUp = "up";
	private long pointId = 0;

	private String fromName;
	private String opinionUrl;
	
	private String userid;
	private int mtype;
	
	private int lastMaxId = -1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			userid = args.getString(BUNDLE_PARAM_USERID);
			mtype = args.getInt(BUNDLE_PARAM_MTYPE,-1);
		}
		
		lastMaxId = getLastMaxId(UserInfo.getInstance().getUserId()+"_"+mtype+SAVED_LASTID);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		myAdapter = new MessageAdapter(mActivity);
		mList.setPullRefreshEnable(false);
		mList.setAdapter(myAdapter);
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(MessageListFragment.MSG_TYPE_STOCK_GROUP == mtype){
					if (id >= 0 && id < msgListData.size()) {
						MessageItemResult.MsgBean bean = msgListData.get((int) id);
						if (bean != null) {
							String stockcode = bean.getStockcode();
							Stock ss = getStockFromDb(stockcode);
							if(ss != null){
								
							}
						}
					}
				}
			}
		});

	}

	class MessageAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MessageAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return msgListData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return msgListData.get(position);
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
				convertView = mInflater.inflate(R.layout.yujing_list_item, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.yjNew = (ImageView) convertView.findViewById(R.id.yj_new);
				viewHolder.yjTime = (TextView) convertView.findViewById(R.id.yj_time);
				viewHolder.yjContent = (TextView) convertView.findViewById(R.id.yj_content);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			MessageItemResult.MsgBean itemData = msgListData.get(position);
			
			if(lastMaxId != -1 && lastMaxId < itemData.getDataid()){
				viewHolder.yjNew.setVisibility(View.VISIBLE);
			}else{
				viewHolder.yjNew.setVisibility(View.GONE);
			}
			viewHolder.yjTime.setText(DateUtils.getTimeAgoString(itemData.getCtime(), "MM-dd HH:mm"));
			
//			SpannableStringBuilder downContent = new SpannableStringBuilder("[通信职业]涨跌幅100%，日涨幅超过设定值52%");
//			downContent.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.font_4c86c6)), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			if(MessageListFragment.MSG_TYPE_GROUP_MSG == mtype){
				viewHolder.yjContent.setText(itemData.getSummary());
			}else if(MessageListFragment.MSG_TYPE_STOCK_GROUP == mtype){
				StringBuilder sb = new StringBuilder();
				sb.append(itemData.getSenderinfo().getUserName()).append("变更“").append(itemData.getPname()).append("”投资组合，以最新价").append(itemData.getPrice())
					.append("元");
				if(itemData.getDealtype() == 1){
					sb.append("买入");
				}else{
					sb.append("卖出");
				}
				sb.append(itemData.getStockname()+"("+itemData.getStockcode()+")");
				
				viewHolder.yjContent.setText(sb.toString());
			}else{
				viewHolder.yjContent.setText(null);
			}
			

			return convertView;
		}

		class ViewHolder {
			ImageView yjNew;
			TextView yjTime;
			TextView yjContent;
		}
	}

	@Override
	protected <T> Request<T> getRequest() {
		// TODO Auto-generated method stub
		
		int id = 0;
		if(!msgListData.isEmpty()){
			MessageItemResult.MsgBean msgbean = msgListData.get(msgListData.size() - 1);
			id = msgbean.getMsg().getDataid();
		}
		String url = NetUrlTougu.MESSAGE_LIST.replace("_userid", userid == null?"":userid).replace("_mtype", mtype==-1?"":""+mtype).replace("_id", ""+id).replace("_ps", ""+pageSize).replace("_b", "b");

		Logger.error(TAG, url);
		JsonRequest request = new JsonRequest<MessageItemResult>(Method.GET, url, null, MessageItemResult.class);
		return request;
	}

	@Override
	protected void onRefreshPrepear() {

	}

	@Override
	protected void onLoadMorePrepear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onReceive(boolean isLoadMore, String id, Object data) {
		// TODO Auto-generated method stub
		MessageItemResult dataBean = (MessageItemResult) data;
		msgListData.addAll(dataBean.getData().getList());
		myAdapter.notifyDataSetChanged();
		if (dataBean.getData().getList().size() < pageSize) {
			mList.setPullLoadEnable(false);
		} else {
			mList.setPullLoadEnable(true);
		}
		if(!msgListData.isEmpty()){
//			MessageItemResult.MsgBean first = msgListData.get(0);
			boolean hasnew = false;
			int lastMax = lastMaxId;
			for(MessageItemResult.MsgBean first : msgListData){
				if(first != null && first.getDataid() > lastMax){
					lastMax = first.getDataid();
					hasnew = true;
				}
			}
			if(hasnew){
				MyApplication.putMsgMaxId(mtype, lastMax);
				saveLastMaxId(UserInfo.getInstance().getUserId()+"_"+mtype+SAVED_LASTID,lastMax);
			}
		}
		if(msgListData.size() == 0){
			showEmpty();
		}
		
	}
	
	private void showEmpty(){
		View empty = LayoutInflater.from(getContext()).inflate(R.layout.empty, null);
		
		TextView nodataTip = (TextView)empty.findViewById(R.id.empty_txt);
		switch(mtype){
		case MessageListFragment.MSG_TYPE_GROUP_MSG:
			nodataTip.setText("暂无签约投顾向您发送消息");
			break;
		case MessageListFragment.MSG_TYPE_STOCK_GROUP:
			nodataTip.setText("签约投顾暂无投资组合变更");
			break;
		}
		
		FrameLayout container = new FrameLayout(mActivity);
		LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
		container.setLayoutParams(linearParams);
		
		FrameLayout.LayoutParams fParam = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		fParam.gravity= Gravity.CENTER;
		empty.setLayoutParams(fParam);
		container.addView(empty);
		
		mList.getParent();
		LinearLayout parent = (LinearLayout)mList.getParent();
		parent.removeView(mList);
		parent.addView(container);
	}

	private void saveLastMaxId(String key,int laxMaxId){
		SharedPreferences.Editor editor = getContext().getSharedPreferences(key, Activity.MODE_PRIVATE).edit();
		editor.putInt("list_"+mtype, laxMaxId);
		editor.commit();
	}
	
	private int getLastMaxId(String key){
		SharedPreferences reader = getContext().getSharedPreferences(key, Activity.MODE_PRIVATE);
		return reader.getInt("list_"+mtype, -1);
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
				String path = "/data/data/" + mActivity.getPackageName() + "/" + QuoteDic.DATA_NAME;
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
