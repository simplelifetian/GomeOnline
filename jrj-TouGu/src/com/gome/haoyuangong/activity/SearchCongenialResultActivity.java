package com.gome.haoyuangong.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.adapter.MyBaseAdapter;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.TouguBaseResult;
import com.gome.haoyuangong.net.result.congenia.CongenialResult;
import com.gome.haoyuangong.net.result.search.SearchCongeniaBean;
import com.gome.haoyuangong.net.result.search.SearchCongeniaData;
import com.gome.haoyuangong.net.url.NetUrlMyInfo;
import com.gome.haoyuangong.net.url.SearchContentAndCongenialUrl;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.views.xlistview.XListView;
import com.gome.haoyuangong.views.xlistview.XListView.IXListViewListener;

public class SearchCongenialResultActivity extends BaseActivity {

	private static final String TAG = SearchCongenialResultActivity.class
			.getName();

	private static final int PULL_REFRESH = 1;
	private static final int LOAD_MORE = 2;
	private static final int FIRST_LOAD = 3;

	private XListView myListView;

	private ListView mListView;

	private String userId = null;

	private List<SearchCongeniaData> mSearchCongeniaListData = new ArrayList<SearchCongeniaData>();
	List<CongenialResult.Item> congenialListData = new ArrayList<CongenialResult.Item>();
	MyCongenialAdapter adapter;
	private SearchCongeniaAdapter myAdapter;

	private int currPage = 0;
	private int pageSize = 12;

	private ImageLoader imageLoader;

	String keyword;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_refresh_listview_search_layout);
		setTitle("搜索投顾");

		initRecommendList();
		myListView = (XListView) findViewById(R.id.listView);
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				// TODO Auto-generated method stub
				if (id >= 0 && id < mSearchCongeniaListData.size()) {
					Intent intent = new Intent(
							SearchCongenialResultActivity.this,
							ViewInvesterInfoActivity.class);
					intent.putExtra("USERNAME",
							mSearchCongeniaListData.get((int) id).getUserName());
					intent.putExtra("USERID",
							mSearchCongeniaListData.get((int) id).getUserId());
					startActivity(intent);
				}

			}

		});

		myListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				getSearchContentList(keyword, 0, "up", 20,PULL_REFRESH);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				getSearchContentList(keyword, mSearchCongeniaListData.size(),
						"down", 20,LOAD_MORE);
			}

		});
		myListView.setDivider(null);
		myAdapter = new SearchCongeniaAdapter(this);
		myListView.setAdapter(myAdapter);

		imageLoader = new ImageLoader(SearchCongenialResultActivity.this);

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

	class SearchCongeniaAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public SearchCongeniaAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		private Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					int itemIndex = msg.arg1;
					updateView(1, itemIndex);// 用我们自己写的方法
				}
				// notifyDataSetChanged();不用了
			}

		};

		public void updateView(int status, int itemIndex) {
			// TODO Auto-generated method stub

			// 得到第一个可显示控件的位置，
			int visiblePosition = myListView.getFirstVisiblePosition();
			// 只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
			if (itemIndex - visiblePosition >= 0) {
				// 得到要更新的item的view
				View view = myListView.getChildAt(itemIndex - visiblePosition);
				ViewHolder holder = (ViewHolder) view.getTag();
				ImageView addIcon = holder.addIcon;
				if (status == 1) {
					addIcon.setBackgroundResource(R.drawable.android_icon_add_old);
					// addButton.invalidateDrawable(mActivity.getResources().getDrawable(R.drawable.android_icon_add_old));
				} else {
					addIcon.setBackgroundResource(R.drawable.android_icon_add_new);
				}
				view.postInvalidate();
			}

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mSearchCongeniaListData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mSearchCongeniaListData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_congenial_list,
						parent, false);
				viewHolder = new ViewHolder();
				viewHolder.userIcon = (ImageView) convertView
						.findViewById(R.id.user_icon);
				viewHolder.userName = (TextView) convertView
						.findViewById(R.id.user_name);
				viewHolder.userRole = (TextView) convertView
						.findViewById(R.id.labelTextView1);
				viewHolder.company = (TextView) convertView
						.findViewById(R.id.labelTextView3);
				viewHolder.addIcon = (ImageView) convertView
						.findViewById(R.id.add_icon);
				viewHolder.experienceScope = (TextView) convertView
						.findViewById(R.id.labelTextView2);
				viewHolder.investDirection = (TextView) convertView
						.findViewById(R.id.labelTextView5);
				viewHolder.satisfaction = (TextView) convertView
						.findViewById(R.id.loveTextView);
				viewHolder.replyNum = (TextView) convertView
						.findViewById(R.id.messageTextView);
				viewHolder.fansNum = (TextView) convertView
						.findViewById(R.id.peopleTextView);
				viewHolder.position = (TextView) convertView
						.findViewById(R.id.labelTextView1);
				viewHolder.talkIcon = (ImageView) convertView
						.findViewById(R.id.talk_button);
				// aViewHolder.comment = (TextView)
				// convertView.findViewById(R.id.comment);
				// viewHolder.imageLimit = (ImageView) convertView
				// .findViewById(R.id.image_limit);
				// viewHolder.ivCertif = (ImageView) convertView
				// .findViewById(R.id.iv_certif);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			SearchCongeniaData itemData = mSearchCongeniaListData.get(position);
			final String userName = itemData.getUserName();
			final String userLonginId = itemData.getUserId();
			ImageView talkIcon = viewHolder.talkIcon;
			talkIcon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (UserInfo.getInstance().isLogin()) {
						if (userLonginId == UserInfo.getInstance().getUserId()) {
							showToast("自己不能咨询自己哟！");
						} else {
							Intent intent = new Intent(
									SearchCongenialResultActivity.this,
									OpenConsultingActivity.class);
							intent.putExtra(OpenConsultingActivity.BUNDLE_TYPE,
									OpenConsultingActivity.SPECIAL_CONSULTING);
							intent.putExtra(
									OpenConsultingActivity.BUNDLE_PARAM_NAME,
									userName);
							intent.putExtra(
									OpenConsultingActivity.BUNDLE_PARAM_ID,
									userLonginId);
							startActivity(intent);
						}

					} else {
						Intent intent = new Intent(
								SearchCongenialResultActivity.this,
								LoginActivity.class);
						intent.putExtra(
								LoginActivity.BUNDLE_PARAM_TARGET_ACTIVITY,
								"com.jrj.tougu.activity.OpenConsultingActivity");
						intent.putExtra(OpenConsultingActivity.BUNDLE_TYPE,
								OpenConsultingActivity.SPECIAL_CONSULTING);
						intent.putExtra(
								OpenConsultingActivity.BUNDLE_PARAM_NAME,
								userName);
						intent.putExtra(OpenConsultingActivity.BUNDLE_PARAM_ID,
								userLonginId);
						startActivity(intent);
					}
				}

			});
			ImageView addIcon = viewHolder.addIcon;
			final int attentionFlag = itemData.getRelation();
			if (UserInfo.getInstance().isLogin()) {
				if (userLonginId.equals(UserInfo.getInstance().getUserId())){
					addIcon.setVisibility(View.INVISIBLE);
				}else{
					addIcon.setVisibility(View.VISIBLE);
				}
			}else{
				addIcon.setVisibility(View.VISIBLE);
			}
			if (attentionFlag == 4) {
				addIcon.setBackgroundResource(R.drawable.android_icon_add_old);
			} else {
				addIcon.setBackgroundResource(R.drawable.android_icon_add_new);
			}
			final int itemIndex = position;
			addIcon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					if (!UserInfo.getInstance().isLogin()) {
						Intent intent = new Intent(
								SearchCongenialResultActivity.this,
								LoginActivity.class);
						startActivityForResult(intent, 1);
					} else {
						if (attentionFlag != 4) {
							doAttention(itemIndex, userLonginId);
						}

					}
				}

			});
			ImageView userIcon = viewHolder.userIcon;

			viewHolder.userName.setText(itemData.getUserName());

			viewHolder.position.setText(itemData.getTypeDesc());

			viewHolder.investDirection.setText(itemData.getInvestDirection());

			viewHolder.experienceScope.setText(String.valueOf(itemData
					.getExperienceScope()));
			viewHolder.company.setText(itemData.getCompany());

			// labelTextView4.setText();
			viewHolder.satisfaction.setText(String.valueOf(itemData
					.getUseSatisfaction()) + "%");

			viewHolder.replyNum.setText(String.valueOf(itemData.getReplyNum()));

			viewHolder.fansNum.setText(String.valueOf(itemData.getFansNum()));
			userId = itemData.getUserId();
			imageLoader.downLoadImage(itemData.getHeadImage(), userIcon);
			return convertView;
		}

		private void doAttention(final int itemIndex, String userId) {
			String url = "";
			// if (attended)
			// url = String.format(NetUrlMyInfo.ATTENTION,
			// userId,UserInfo.getInstance().getUserId(),0);
			// else
			url = String.format(NetUrlMyInfo.ATTENTION, userId, UserInfo
					.getInstance().getUserId(), 1);
			JsonRequest<TouguBaseResult> request = new JsonRequest<TouguBaseResult>(
					Method.GET, url,
					new RequestHandlerListener<TouguBaseResult>(getContext()) {

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

						@SuppressLint("ShowToast")
						@Override
						public void onSuccess(String id, TouguBaseResult data) {
							// TODO Auto-generated method stub
//							try {
//								if (data.getRetCode() == 2) {
//									Message msg = Message.obtain();
//									msg.what = 1;
//									msg.arg1 = itemIndex;
//									mHandler.sendMessage(msg);
//									com.jrj.tougu.MyApplication.get().setNewConcent(true);
//								}
////								Toast.makeText(
////										SearchCongenialResultActivity.this,
////										data.getMsg(), Toast.LENGTH_SHORT)
////										.show();
//							} catch (Exception e) {
//								Toast.makeText(
//										SearchCongenialResultActivity.this,
//										e.toString(), Toast.LENGTH_SHORT)
//										.show();
//							}
							Toast.makeText(SearchCongenialResultActivity.this,data.getMsg(), Toast.LENGTH_SHORT).show();

						}
						@Override
						public void onFailure(String id, int code, String str,Object obj) {
							if(obj != null && obj instanceof TouguBaseResult){
								TouguBaseResult data = (TouguBaseResult)obj;
								if(data.getRetCode() == 2){
									SearchCongeniaData itemData = (SearchCongeniaData) getItem(itemIndex);
									itemData.setRelation(4);
									myAdapter.notifyDataSetChanged();
									Toast.makeText(SearchCongenialResultActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
									com.gome.haoyuangong.MyApplication.get().setNewConcent(true);
								}else{
									if(data.getRetCode() == -3){
										SearchCongeniaData itemData = (SearchCongeniaData) getItem(itemIndex);
										itemData.setRelation(4);
										myAdapter.notifyDataSetChanged();
									}
									super.onFailure(id, code, str, obj);
								}
							}else{
								super.onFailure(id, code, str, obj);
							}
						}
					}, TouguBaseResult.class);
			send(request);
		}

	}

	class ViewHolder {
		ImageView talkIcon;
		ImageView userIcon;
		TextView userName;
		ImageView addIcon;
		TextView userRole;
		TextView company;
		TextView position;
		TextView experienceScope;
		TextView fansNum;
		TextView replyNum;
		TextView satisfaction;
		TextView investDirection;
		ImageView imageLimit;
		ImageView ivCertif;
	}

	private void getSearchContentList(String keyword, final int from,
			final String direction, final int requestsize,final int requestType) {
		String url = String.format(SearchContentAndCongenialUrl.CONGENIAL_LIST,
				keyword, from, requestsize, direction);
		Log.e(TAG, url);
		if (UserInfo.getInstance().isLogin()) {
			url = url + "&passportId=" + UserInfo.getInstance().getUserId();
		}
		JsonRequest<SearchCongeniaBean> request = new JsonRequest<SearchCongeniaBean>(
				Method.GET, url,
				new RequestHandlerListener<SearchCongeniaBean>(getContext()) {

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
					public void onSuccess(String id, SearchCongeniaBean data) {
						// TODO Auto-generated method stub

						if (data == null) {
							Toast.makeText(SearchCongenialResultActivity.this,
									"请求标题栏失败", Toast.LENGTH_SHORT).show();
						} else {
							if (data.getRetCode() == 0) {
								int size = data.getData().size();
								if (size > 0) {
									if (from == 0) {
										mSearchCongeniaListData.clear();
									}
									mSearchCongeniaListData.addAll(data
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
											SearchCongenialResultActivity.this,
											"无此类信息", Toast.LENGTH_SHORT).show();
								}
								if (size > 0 && size < requestsize || size <= 0) {
									myListView.setPullLoadEnable(false);
								}
								if (from == 0 && size == 0) {
									// 发起二次推荐数据请求
									getRecommendList("0");
								}
							} else {
//								Toast.makeText(
//										SearchCongenialResultActivity.this,
//										data.getMsg(), Toast.LENGTH_SHORT)
//										.show();
							}
						}

					}
				}, SearchCongeniaBean.class);

		send(request);

	}

	class MyCongenialAdapter extends MyBaseAdapter<CongenialResult.Item> {

		public MyCongenialAdapter(Context context,
				List<CongenialResult.Item> list) {
			super(context, list);
		}

		private Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					int itemIndex = msg.arg1;
					updateView(1, itemIndex);// 用我们自己写的方法
				}
				// notifyDataSetChanged();不用了
			}

		};

		public void updateView(int status, int itemIndex) {
			// TODO Auto-generated method stub

			// 得到第一个可显示控件的位置，
			int visiblePosition = mListView.getFirstVisiblePosition();
			// 只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
			if (itemIndex - visiblePosition >= 0) {
				// 得到要更新的item的view
				View view = mListView.getChildAt(itemIndex - visiblePosition);
				ViewHolder holder = (ViewHolder) view.getTag();
				ImageView addButton = (ImageView) holder.getView(R.id.add_icon);
				if (status == 1) {
					addButton
							.setBackgroundResource(R.drawable.android_icon_add_old);
				} else {
					addButton
							.setBackgroundResource(R.drawable.android_icon_add_new);
				}
				view.postInvalidate();
			}

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = ViewHolder.getInstance(context, convertView,
					parent, R.layout.item_congenial_list);
			if (convertView == null) {
				convertView = holder.getView();
				convertView.setTag(holder);
			}

			CongenialResult.Item data = (CongenialResult.Item) getItem(position);

			ImageView talkButton = (ImageView) holder.getView(R.id.talk_button);
			final String userName = data.getUserName();
			final String userLonginId = data.getLoginId();
			talkButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (UserInfo.getInstance().isLogin()) {
						if (userLonginId == UserInfo.getInstance().getUserId()) {
							showToast("自己不能咨询自己哟！");
						} else {
							Intent intent = new Intent(
									SearchCongenialResultActivity.this,
									OpenConsultingActivity.class);
							intent.putExtra(OpenConsultingActivity.BUNDLE_TYPE,
									OpenConsultingActivity.SPECIAL_CONSULTING);
							intent.putExtra(
									OpenConsultingActivity.BUNDLE_PARAM_NAME,
									userName);
							intent.putExtra(
									OpenConsultingActivity.BUNDLE_PARAM_ID,
									userLonginId);
							startActivity(intent);
						}

					} else {
						Intent intent = new Intent(
								SearchCongenialResultActivity.this,
								LoginActivity.class);
						intent.putExtra(
								LoginActivity.BUNDLE_PARAM_TARGET_ACTIVITY,
								"com.jrj.tougu.activity.OpenConsultingActivity");
						intent.putExtra(OpenConsultingActivity.BUNDLE_TYPE,
								OpenConsultingActivity.SPECIAL_CONSULTING);
						intent.putExtra(
								OpenConsultingActivity.BUNDLE_PARAM_NAME,
								userName);
						intent.putExtra(OpenConsultingActivity.BUNDLE_PARAM_ID,
								userLonginId);
						startActivity(intent);
					}
				}

			});

			ImageView addButton = (ImageView) holder.getView(R.id.add_icon);
			if (UserInfo.getInstance().isLogin()) {
				if (userLonginId.equals(UserInfo.getInstance().getUserId())) {
					addButton.setVisibility(View.INVISIBLE);
				}else{
					addButton.setVisibility(View.VISIBLE);
				}
			}else{
				addButton.setVisibility(View.VISIBLE);
			}
			
			final int attentionFlag=data.getAttentionFlag();
			if (attentionFlag == 1) {
				addButton
						.setBackgroundResource(R.drawable.android_icon_add_old);
			} else {
				addButton
						.setBackgroundResource(R.drawable.android_icon_add_new);
			}
			final int itemIndex = position;
			addButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (!UserInfo.getInstance().isLogin()) {
						Intent intent = new Intent(
								SearchCongenialResultActivity.this,
								LoginActivity.class);
						startActivityForResult(intent, 1);
					} else {
						if(attentionFlag != 1){
							doAttention(itemIndex, userLonginId);
						}
					}
				}

			});
			ImageView userIcon = holder.getView(R.id.user_icon);
			TextView userNameTextView = holder.getView(R.id.user_name);
			userNameTextView.setText(data.getUserName());
			TextView labelTextView1 = holder.getView(R.id.labelTextView1);
//			labelTextView1.setText(data.getPosition());
			labelTextView1.setText(data.getTypeDesc());
			TextView labelTextView2 = holder.getView(R.id.labelTextView2);

			TextView labelTextView3 = holder.getView(R.id.labelTextView3);
			labelTextView3.setText(data.getCompany());
			
			TextView labelTextView4 = holder.getView(R.id.labelTextView4);
			// labelTextView4.setText();
			TextView labelTextView5 = holder.getView(R.id.labelTextView5);
			labelTextView5.setText(data.getInvestDirection());
			// labelTextView5.setText(String.valueOf(data.getUseSatisfaction())+"%");
			TextView labelTextView6 = holder.getView(R.id.labelTextView6);
			labelTextView6.setText(String.valueOf(data.getAnswerNumber()));
			TextView labelTextView7 = holder.getView(R.id.labelTextView7);
			labelTextView7.setText(String.valueOf(data.getFansNum()));
			userId = String.valueOf(data.getId());
			if (data.getHeadImage() == null) {
//				System.out.println("********** 1" + data.getHeadImage());
			} else
				imageLoader.downLoadImage(data.getHeadImage(), userIcon);
			if (data.getExperienceScope() == 1) {
				labelTextView2.setText("1年以下");
			} else if (data.getExperienceScope() == 2) {
				labelTextView2.setText("1-3年");
			} else if (data.getExperienceScope() == 3) {
				labelTextView2.setText("3-5年");
			} else {
				labelTextView2.setText("5年以上");
			}
			TextView labelTextView8 = holder.getView(R.id.loveTextView);
			labelTextView8.setText(String.valueOf(data.getUseSatisfaction())+"%");
			TextView labelTextView9 = holder.getView(R.id.messageTextView);
			labelTextView9.setText(String.valueOf(data.getAnswerNumber()));
			TextView labelTextView0 = holder.getView(R.id.peopleTextView);
			labelTextView0.setText(String.valueOf(data.getFansNum()));

			return convertView;
		}

		private void doAttention(final int itemIndex, String userId) {
			String url = "";
			// if (attended)
			// url = String.format(NetUrlMyInfo.ATTENTION,
			// userId,UserInfo.getInstance().getUserId(),0);
			// else
			url = String.format(NetUrlMyInfo.ATTENTION, userId, UserInfo
					.getInstance().getUserId(), 1);
			JsonRequest<TouguBaseResult> request = new JsonRequest<TouguBaseResult>(
					Method.GET, url,
					new RequestHandlerListener<TouguBaseResult>(getContext()) {

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

						@SuppressLint("ShowToast")
						@Override
						public void onSuccess(String id, TouguBaseResult data) {
							// TODO Auto-generated method stub
//							try {
//								if (data.getRetCode() == 2) {
//									Message msg = Message.obtain();
//									msg.what = 1;
//									msg.arg1 = itemIndex;
//									mHandler.sendMessage(msg);
//									com.jrj.tougu.MyApplication.get().setNewConcent(true);
//								}
////								Toast.makeText(
////										SearchCongenialResultActivity.this,
////										data.getMsg(), Toast.LENGTH_SHORT)
////										.show();
//							} catch (Exception e) {
//								Toast.makeText(
//										SearchCongenialResultActivity.this,
//										e.toString(), Toast.LENGTH_SHORT)
//										.show();
//							}

						}
						
						@Override
						public void onFailure(String id, int code, String str,Object obj) {
							if(obj != null && obj instanceof TouguBaseResult){
								TouguBaseResult data = (TouguBaseResult)obj;
								if(data.getRetCode() == 2){
									CongenialResult.Item itemData = (CongenialResult.Item) getItem(itemIndex);
									itemData.setAttentionFlag(1);
									adapter.notifyDataSetChanged();
									Toast.makeText(SearchCongenialResultActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
									com.gome.haoyuangong.MyApplication.get().setNewConcent(true);
								}else{
									if(data.getRetCode() == -3){
										CongenialResult.Item itemData = (CongenialResult.Item) getItem(itemIndex);
										itemData.setAttentionFlag(1);
										adapter.notifyDataSetChanged();
									}
									super.onFailure(id, code, str, obj);
								}
							}else{
								super.onFailure(id, code, str, obj);
							}
						}
					}, TouguBaseResult.class);
			send(request);
		}
	}

	private void getRecommendList(String userId) {
		String URL = "http://mapi.itougu.jrj.com.cn/wireless/account/adviserRankingList/%s/?ps=%d&d=%s&id=%d&userId=%s";
		String url;
		if (!UserInfo.getInstance().isLogin()) {
			url = String.format(URL, "1", 10, "down", 0, userId);
		} else {
			url = String.format(URL, "1", 10, "down", 0, UserInfo
					.getInstance().getUserId());
		}
		Log.e(TAG, url);
		JsonRequest<CongenialResult> request = new JsonRequest<CongenialResult>(
				Method.GET, url, new RequestHandlerListener<CongenialResult>(
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
					public void onSuccess(String id, CongenialResult data) {
						if (data == null) {
//							Toast.makeText(SearchCongenialResultActivity.this,
//									"请求标题栏失败", Toast.LENGTH_SHORT).show();
							// }
						} else {
							if (data.getRetCode() == 0) {
								CongenialResult dataBean = (CongenialResult) data;
								congenialListData.addAll(dataBean.getData()
										.getList());
								
								myListView.setVisibility(View.GONE);
								mListView.setVisibility(View.VISIBLE);

								adapter.notifyDataSetChanged();
							} else {
//								Toast.makeText(
//										SearchCongenialResultActivity.this,
//										data.getMsg(), Toast.LENGTH_SHORT)
//										.show();
							}
						}

					}
				}, CongenialResult.class);

		send(request);

	}

	private void setListHeader() {
		final View v1 = getLayoutInflater().inflate(
				R.layout.item_search_congenial_list_header, null, false);
		mListView.addHeaderView(v1);
		TextView labelTextView1 = (TextView) v1
				.findViewById(R.id.labelTextView1);
		labelTextView1.setText("暂无相关关键词的投顾，您可以尝试其他搜索条件\n精心为您推荐的热门投顾");
		// RelativeLayout mRelativeLayout = (RelativeLayout) v1
		// .findViewById(R.id.first);
		// mRelativeLayout.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// mListView.removeHeaderView(v1);
		// }
		//
		// });
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
		adapter = new MyCongenialAdapter(SearchCongenialResultActivity.this,
				congenialListData);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (id >= 0 && id < congenialListData.size()) {
					Intent intent = new Intent(
							SearchCongenialResultActivity.this,
							ViewInvesterInfoActivity.class);
					intent.putExtra("USERNAME", congenialListData.get((int) id)
							.getUserName());
					intent.putExtra("USERID", congenialListData.get((int) id)
							.getLoginId());
					startActivity(intent);
				}

			}
		});
	}

}
