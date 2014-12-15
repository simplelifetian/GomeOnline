/**
 * 
 */
package com.gome.haoyuangong.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
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

import com.gome.haoyuangong.MyApplication;
import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.bean.Stock;
import com.gome.haoyuangong.db.QuoteDic;
import com.gome.haoyuangong.fragments.MessageListFragment;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.tougu.SysMessageItemResult;
import com.gome.haoyuangong.net.url.NetUrlTougu;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.DateUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.xlistview.XListView;
import com.gome.haoyuangong.views.xlistview.XListView.IXListViewListener;

/**
 * 
 */
public class MessageListActivity extends BaseActivity {

	private static final String TAG = MessageListActivity.class.getName();
	
	private static final String SAVED_LASTID = "_SAVED_LASTID";
	
	public static final String BUNDLE_PARAM_TITLE = "BUNDLE_PARAM_TITLE";
	public static final String BUNDLE_PARAM_USERID = "BUNDLE_PARAM_USERID";
	public static final String BUNDLE_PARAM_MTYPE = "BUNDLE_PARAM_MTYPE";
	
	private static final int PULL_REFRESH = 1;
	private static final int LOAD_MORE = 2;
	private static final int FIRST_LOAD = 3;

	private XListView myListView;

	private List<SysMessageItemResult.MsgBean> msgListData = new ArrayList<SysMessageItemResult.MsgBean>();

	private AttentionAdapter myAdapter;

	private int currPage = 0;
	private int pageSize = 20;
	private String userid;
	private int mtype;
	
	private int lastMaxId = -1;
	
	private FrameLayout nodataLayout;
	private TextView nodataTip;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String title = getIntent().getStringExtra(BUNDLE_PARAM_TITLE);
		userid = getIntent().getStringExtra(BUNDLE_PARAM_USERID);
		mtype = getIntent().getIntExtra(BUNDLE_PARAM_MTYPE,-1);
		if(StringUtils.isEmpty(userid) || mtype == -1){
			Toast.makeText(this, "无效用户或类型", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		setContentView(R.layout.simple_refresh_listview_layout);
		setTitle("股价预警");
		if(StringUtils.isEmpty(title)){
			
		}else{
			setTitle(title);
		}
		lastMaxId = getLastMaxId(UserInfo.getInstance().getUserId()+SAVED_LASTID);
		myListView = (XListView) findViewById(R.id.listView);
		myListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				getLiveList(PULL_REFRESH);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				getLiveList(LOAD_MORE);
			}

		});
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
				if(MessageListFragment.MSG_TYPE_STOCK_YJ == mtype){
					if(id >= 0 && id < msgListData.size()){
						
					}
				}
			}
		});
		myListView.setDivider(null);
		myAdapter = new AttentionAdapter(this);
		myListView.setAdapter(myAdapter);
		loadVg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getLiveList(FIRST_LOAD);
			}
		});
		
		nodataLayout = (FrameLayout)findViewById(R.id.nodata_layout);
		nodataTip = (TextView)findViewById(R.id.notdata_tip);
		getLiveList(FIRST_LOAD);
	}
	
	@Override
	public void onLoad(){
		
	}

	class AttentionAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public AttentionAdapter(Context context) {
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
			SysMessageItemResult.MsgBean itemData = msgListData.get(position);
			
			
			if(lastMaxId != -1 && lastMaxId < itemData.getDataid()){
				viewHolder.yjNew.setVisibility(View.VISIBLE);
			}else{
				viewHolder.yjNew.setVisibility(View.GONE);
			}
			viewHolder.yjTime.setText(DateUtils.getTimeAgoString(itemData.getCtime(), "MM-dd HH:mm"));
			
			SpannableStringBuilder downContent = new SpannableStringBuilder();
//			downContent.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.font_4c86c6)), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			if(MessageListFragment.MSG_TYPE_STOCK_YJ == mtype && !StringUtils.isEmpty(itemData.getStockname())){
				downContent.append("["+itemData.getStockname()+"]");
				downContent.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.font_4c86c6)), 0, downContent.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			downContent.append(Html.fromHtml(itemData.getSummary()));
			
			URLSpan[] urls = downContent.getSpans(0, downContent.length(), URLSpan.class);
			if(urls != null && urls.length > 0){
				SpannableStringBuilder style = new SpannableStringBuilder(downContent);    
	            style.clearSpans();// should clear old spans 
				for(URLSpan span : urls){
					 MyURLSpan myURLSpan = new MyURLSpan(MessageListActivity.this,span.getURL());  
					 style.setSpan(myURLSpan, downContent.getSpanStart(span),    
							 downContent.getSpanEnd(span), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);  
				}
				viewHolder.yjContent.setText(style);
			}else{
				viewHolder.yjContent.setText(downContent);
			}
			if(MessageListFragment.MSG_TYPE_STOCK_YJ != mtype){
				viewHolder.yjContent.setMovementMethod(LinkMovementMethod.getInstance());
			}
//			downContent.get
//			downContent.setSpan(new ClickableSpan() {
//
//				@Override
//				public void onClick(View widget) {
//					WebViewActivity.gotoWebViewActivity(MessageListActivity.this, "用户协议", NetUrlLoginAndRegist.USER_AGREEMENT);
//				}
//
//				@Override
//				public void updateDrawState(TextPaint ds) {
//					ds.setColor(getResources().getColor(R.color.font_4c86c6));
//					ds.setUnderlineText(true); // 下划线
//				}
//			}, 7, downContent.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			viewHolder.yjContent.setText(downContent);
			
			return convertView;
		}

		class ViewHolder {
			ImageView yjNew;
			TextView yjTime;
			TextView yjContent;
		}
	}
	
	private static class MyURLSpan extends ClickableSpan {    
	    
        private String mUrl;  
        private Context context;
    
        MyURLSpan(Context context,String url) { 
        	this.context = context;
        	this.mUrl = url;    
        }    
    
        @Override    
        public void onClick(View widget) {    
        	WebViewActivity.gotoWebViewActivity(context, "系统消息", this.mUrl);
        }    
    }  

	private void getLiveList(final int requestType) {

		int id = 0;
		if(requestType == LOAD_MORE){
			if(!msgListData.isEmpty()){
				SysMessageItemResult.MsgBean msgbean = msgListData.get(msgListData.size() - 1);
				id = msgbean.getDataid();
			}
		}
		String url = null;
		if(MessageListFragment.MSG_TYPE_SYSTEM == mtype){
			url = NetUrlTougu.MESSAGE_LIST.replace("_userid", userid).replace("_mtype", ""+mtype+",12,14").replace("_id", ""+id).replace("_ps", ""+pageSize).replace("_b", "f");
		}else{
			url = NetUrlTougu.MESSAGE_LIST.replace("_userid", userid).replace("_mtype", ""+mtype).replace("_id", ""+id).replace("_ps", ""+pageSize).replace("_b", "f");
		}
		if(url == null){
			return;
		}
		
		Log.e(TAG, url);
		JsonRequest<SysMessageItemResult> request = new JsonRequest<SysMessageItemResult>(Method.GET, url,
				new RequestHandlerListener<SysMessageItemResult>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						if(requestType == FIRST_LOAD){
							showLoading(request);
						}
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						if(requestType == FIRST_LOAD){
							hideLoading(request);
						}
						if(requestType == PULL_REFRESH){
							myListView.stopRefresh();
						}
						if(requestType == LOAD_MORE){
							myListView.stopLoadMore();
						}
						
					}

					@Override
					public void onSuccess(String id, SysMessageItemResult data) {
						// TODO Auto-generated method stub
						if(data == null){
							Toast.makeText(MessageListActivity.this, "请求消息列表失败", Toast.LENGTH_SHORT).show();
						}else{
							saveRefreshTime(mtype+"_"+MessageListFragment.MSG_TYPE_SYSTEM);
							myListView.setRefreshTime(getRefreshTime(mtype+"_"+MessageListFragment.MSG_TYPE_SYSTEM));
							
							if(requestType == PULL_REFRESH){
								msgListData.clear();
							}
							msgListData.addAll(data.getData().getList());
							myAdapter.notifyDataSetChanged();
							if(data.getData().getList().size() < pageSize){
								myListView.setPullLoadEnable(false);
							}else{
								myListView.setPullLoadEnable(true);
							}
							if(!msgListData.isEmpty()){
//								SysMessageItemResult.MsgBean first = msgListData.get(0);
								int lastMax = lastMaxId;
								boolean hasnew = false;
								for(SysMessageItemResult.MsgBean first : msgListData){
									if(first != null && first.getDataid() > lastMax){ 
										lastMax = first.getDataid();
										hasnew = true;
									}
								}
								if(hasnew){
									MyApplication.putMsgMaxId(mtype, lastMax);
									saveLastMaxId(UserInfo.getInstance().getUserId()+SAVED_LASTID,lastMax);
								}
							}
							
							if(msgListData.size() == 0){
								myListView.setVisibility(View.GONE);
								nodataLayout.setVisibility(View.VISIBLE);
								
								switch(mtype){
								case MessageListFragment.MSG_TYPE_SYSTEM:
									nodataTip.setText("暂无系统通知");
									break;
								case MessageListFragment.MSG_TYPE_STOCK_YJ:
									nodataTip.setText("暂无股价预警消息\n您可以在自选股中添加预警哦");
									break;
								}
								
							}
							
						}
					}
				}, SysMessageItemResult.class);

		send(request);

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
	
	private void saveLastMaxId(String key,int laxMaxId){
		SharedPreferences.Editor editor = getContext().getSharedPreferences(key, Activity.MODE_PRIVATE).edit();
		editor.putInt("list_"+mtype, laxMaxId);
		editor.commit();
	}
	
	private int getLastMaxId(String key){
		SharedPreferences reader = getContext().getSharedPreferences(key, Activity.MODE_PRIVATE);
		return reader.getInt("list_"+mtype, -1);
	}
}
