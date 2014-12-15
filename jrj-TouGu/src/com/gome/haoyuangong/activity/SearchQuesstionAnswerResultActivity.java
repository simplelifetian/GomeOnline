package com.gome.haoyuangong.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.adapter.MyBaseAdapter;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.ask.AskItemAskResult;
import com.gome.haoyuangong.net.result.ask.AskItemAskResult.AskItemAsk;
import com.gome.haoyuangong.net.result.search.SearchQuesstionAnswerBean;
import com.gome.haoyuangong.net.result.search.SearchQuesstionAnswerData;
import com.gome.haoyuangong.net.url.AskUrl;
import com.gome.haoyuangong.net.url.SearchContentAndCongenialUrl;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.DateUtils;
import com.gome.haoyuangong.views.xlistview.XListView;
import com.gome.haoyuangong.views.xlistview.XListView.IXListViewListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchQuesstionAnswerResultActivity extends BaseActivity {

	private static final String TAG = SearchContentResultActivity.class
			.getName();

	private static final int PULL_REFRESH = 1;
	private static final int LOAD_MORE = 2;
	private static final int FIRST_LOAD = 3;

	private XListView myListView;

	private ListView mListView;

	private List<SearchQuesstionAnswerData> mSearchQuesstionAnswerListData = new ArrayList<SearchQuesstionAnswerData>();

	private List<AskItemAsk> dataAsk = new ArrayList<AskItemAsk>();

	private SearchContentAdapter myAdapter;

	MyAskAdapter2 mAdapter;

	// RequestQueue mQueue;

	private int currPage = 0;
	private int pageSize = 12;

	private ImageLoader imageLoader;

	String keyword;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_refresh_listview_search_layout);
		setTitle("搜索问题");
		initRecommendList();
		myListView = (XListView) findViewById(R.id.listView);
		myListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				getSearchContentList(keyword, 0, "up", 20,PULL_REFRESH);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				getSearchContentList(keyword,
						mSearchQuesstionAnswerListData.size(), "down", 20,LOAD_MORE);
			}

		});
		myListView.setDivider(null);
		myAdapter = new SearchContentAdapter(this,mSearchQuesstionAnswerListData);
		myListView.setAdapter(myAdapter);
		
		myListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				// TODO Auto-generated method stub
//				Intent i = new Intent(SearchQuesstionAnswerResultActivity.this,
//						com.jrj.tougu.activity.AskDetailActivity_.class);
//
//				if (id < mSearchQuesstionAnswerListData.size() && id >= 0) {
////					需要后台加问答id
//					i.putExtra(AskDetailActivity.BUNDLE_Id,
//							mSearchQuesstionAnswerListData.get((int) id).getId());
//					startActivity(i);
//				}
			}
			
		});

		imageLoader = new ImageLoader(SearchQuesstionAnswerResultActivity.this);

	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub

		try {
			keyword = getIntent().getStringExtra("KEYWORD");
			keyword = URLEncoder.encode(keyword, "UTF-8");
			getSearchContentList(keyword, 0, "down", 20,FIRST_LOAD);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class SearchContentAdapter extends MyBaseAdapter<SearchQuesstionAnswerData> {
		


		public SearchContentAdapter(Context context, List<SearchQuesstionAnswerData> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = ViewHolder.getInstance(context, convertView,
					parent, R.layout.item_ask_list2);
			if (convertView == null) {
				convertView = holder.getView();
				convertView.setTag(holder);
			}
			SearchQuesstionAnswerData itemData = mSearchQuesstionAnswerListData
					.get(position);
			TextView answerName = holder.getView(R.id.ask_item_tv_name);
			answerName.setText(itemData.getUserName());
			TextView answerTime = holder.getView(R.id.ask_item_tv_time);
			answerTime.setText(DateUtils.getTimeAgoString(Long.valueOf(itemData.getCtime()),
					"MM-dd hh:mm"));
			TextView qa = holder.getView(R.id.ask_item_tv_qa);
			qa.setText(itemData.getContent());
			ImageView icon = holder.getView(R.id.headpic);
			imageLoader.downLoadImage(itemData.getHeadImage(), icon);
			ImageView imgFensi = holder.getView(R.id.ask_item_iv_fensi);
			TextView answerBtn = holder.getView(R.id.ask_item_btn_answer);
			answerBtn.setVisibility(View.GONE);
			imgFensi.setVisibility(View.GONE);
			UserInfo info = UserInfo.getInstance();
			if (info.isLogin()) {
				if (itemData.getRelation() == 1) {
					imgFensi.setImageResource(R.drawable.icon_fensi);
					imgFensi.setVisibility(View.VISIBLE);
				} else {
					imgFensi.setVisibility(View.GONE);
				}
//				if (info.isTougu()) {
//					answerBtn.setVisibility(View.VISIBLE);
//					answerBtn.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
////							ReplyMediaRecordActivity.goToAnswerReply(SearchQuesstionAnswerResultActivity.this, bean.getAskId());
//						}
//					});
//				}
			}
			return convertView;
		}

	}

	private void getSearchContentList(String keyword, final int from,
			final String direction, final int requestsize, final int requestType) {

		String url = String.format(
				SearchContentAndCongenialUrl.QUESSTION_ANSWER_LIST, keyword,
				from, requestsize, direction);
		// String
		// url="http://mapi.itougu.jrj.com.cn/wireless/search/?type=ask&keyword=A%E8%82%A1&size=20&from=0";
		Log.e(TAG, url);
		JsonRequest<SearchQuesstionAnswerBean> request = new JsonRequest<SearchQuesstionAnswerBean>(
				Method.GET, url,
				new RequestHandlerListener<SearchQuesstionAnswerBean>(
						getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						if(FIRST_LOAD == requestType){showLoading(request);}
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						if(FIRST_LOAD == requestType){hideLoading(request);}
					}

					@Override
					public void onSuccess(String id,
							SearchQuesstionAnswerBean data) {
						if (data == null) {
//							Toast.makeText(
//									SearchQuesstionAnswerResultActivity.this,
//									"请求标题栏失败", Toast.LENGTH_SHORT).show();
						} else {
							if (data.getRetCode() == 0) {
								int size = data.getData().size();
								if (size > 0) {
									if (from == 0) {
										mSearchQuesstionAnswerListData.clear();
									}
									mSearchQuesstionAnswerListData.addAll(data
											.getData());
									myAdapter.notifyDataSetChanged();
									myListView.setRefreshTime("1分钟前");
									currPage = from;
									if ("up".equals(direction)) {
										myListView.stopRefresh();
										myListView.setPullLoadEnable(true);
									} else {
										myListView.stopLoadMore();
									}
								} else {
									if ("up".equals(direction)) {
										myListView.stopRefresh();
									} else {
										myListView.stopLoadMore();
									}
									Toast.makeText(
											SearchQuesstionAnswerResultActivity.this,
											"无此类信息", Toast.LENGTH_SHORT).show();
								}
								if (size > 0 && size < requestsize || size <= 0) {
									myListView.setPullLoadEnable(false);
								}
								if (from == 0 && size == 0) {
									// 发起二次推荐数据请求
									getRecommendList("");
								}
							} else {
//								Toast.makeText(
//										SearchQuesstionAnswerResultActivity.this,
//										data.getMsg(), Toast.LENGTH_SHORT)
//										.show();
							}
						}

					}
				}, SearchQuesstionAnswerBean.class);

		send(request);

	}

	private void setListHeader() {
		final View v1 = getLayoutInflater().inflate(
				R.layout.item_search_congenial_list_header, null, false);
		mListView.addHeaderView(v1);
		TextView labelTextView1=(TextView)v1.findViewById(R.id.labelTextView1);
		labelTextView1.setText("暂无相关关键词的问答，您可以尝试其他搜索条件\n精心为您推荐的最新问答");
//		RelativeLayout mRelativeLayout = (RelativeLayout) v1
//				.findViewById(R.id.first);
//		mRelativeLayout.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				mListView.removeHeaderView(v1);
//			}
//
//		});
//		ImageView imageView = (ImageView) v1.findViewById(R.id.delete_item);
//		imageView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				mListView.removeHeaderView(v1);
//			}
//
//		});
	}

	private void initRecommendList() {
		mListView = (ListView) findViewById(R.id.mlistView);
		setListHeader();
		mAdapter = new MyAskAdapter2(SearchQuesstionAnswerResultActivity.this,
				dataAsk);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				Intent i = new Intent(SearchQuesstionAnswerResultActivity.this,
//						com.jrj.tougu.activity.AskDetailActivity_.class);
//
//				if (id < dataAsk.size() && id >= 0) {
//					i.putExtra(AskDetailActivity.BUNDLE_Id,
//							dataAsk.get((int) id).getAskId());
//					startActivity(i);
//				}
			}
		});
	}

	class MyAskAdapter2 extends MyBaseAdapter<AskItemAsk> {

		public MyAskAdapter2(Context context, List<AskItemAsk> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = ViewHolder.getInstance(context, convertView,
					parent, R.layout.item_ask_list2);
			if (convertView == null) {
				convertView = holder.getView();
				convertView.setTag(holder);
			}
			final AskItemAsk bean = (AskItemAsk) getItem(position);
			TextView answerName = holder.getView(R.id.ask_item_tv_name);
			answerName.setText(bean.getAusername());
			TextView answerTime = holder.getView(R.id.ask_item_tv_time);
			answerTime.setText(DateUtils.getTimeAgoString(bean.getCtime(),
					"MM-dd hh:mm"));
			TextView qa = holder.getView(R.id.ask_item_tv_qa);
			qa.setText(bean.getContent());
			ImageView icon = holder.getView(R.id.headpic);
			imageLoader.downLoadImage(bean.getHeadImages(), icon);
			ImageView imgFensi = holder.getView(R.id.ask_item_iv_fensi);
			TextView answerBtn = holder.getView(R.id.ask_item_btn_answer);
			answerBtn.setVisibility(View.GONE);
			imgFensi.setVisibility(View.GONE);
			UserInfo info = UserInfo.getInstance();
			if (info.isLogin()) {
				if (bean.getIsMyFuns() == 1) {
					imgFensi.setImageResource(R.drawable.icon_fensi);
					imgFensi.setVisibility(View.VISIBLE);
				} else {
					imgFensi.setVisibility(View.GONE);
				}
//				if (info.isTougu()) {
//					answerBtn.setVisibility(View.VISIBLE);
//					answerBtn.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							ReplyMediaRecordActivity.goToAnswerReply(SearchQuesstionAnswerResultActivity.this, bean.getAskId());
//						}
//					});
//				}
			}
			return convertView;
		}

	}

	private void getRecommendList(String userId) {
		UserInfo info = UserInfo.getInstance();
		String url = String.format(AskUrl.NEW_ASK, 10, "f");
		// if(ids>0){
		// url=url+"&aid="+ids;
		// }
		if (info.isLogin() && info.isTougu()) {
			url = url + "&" + "uid=" + info.getUserId();
		}
		Log.e(TAG, url);
		JsonRequest<AskItemAskResult> request = new JsonRequest<AskItemAskResult>(
				Method.GET, url, new RequestHandlerListener<AskItemAskResult>(
						getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
//						showDialog(request,"获取推荐内容...");
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
//						 hideDialog(request);
					}

					@Override
					public void onSuccess(String id, AskItemAskResult data) {
						if (data == null) {
//							Toast.makeText(
//									SearchQuesstionAnswerResultActivity.this,
//									"请求标题栏失败", Toast.LENGTH_SHORT).show();
							// }
						} else {
							if (data.getRetCode() == 0) {
								AskItemAskResult livelistBean = (AskItemAskResult) data;
								dataAsk.addAll(livelistBean.getData().getList());
								myListView.setVisibility(View.GONE);
								mListView.setVisibility(View.VISIBLE);
								mAdapter.notifyDataSetChanged();
								if (livelistBean.getData().getList().size() < pageSize) {
								} else {
								}
							} else {
//								Toast.makeText(
//										SearchQuesstionAnswerResultActivity.this,
//										data.getMsg(), Toast.LENGTH_SHORT)
//										.show();
							}
						}

					}
				}, AskItemAskResult.class);

		send(request);

	}

}
