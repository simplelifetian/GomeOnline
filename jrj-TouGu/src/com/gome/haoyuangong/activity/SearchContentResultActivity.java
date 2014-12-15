package com.gome.haoyuangong.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.search.SearchContentBean;
import com.gome.haoyuangong.net.result.search.SearchContentData;
import com.gome.haoyuangong.net.result.tougu.HotOpinionListBean;
import com.gome.haoyuangong.net.result.tougu.OpinionListBean;
import com.gome.haoyuangong.net.url.NetUrlTougu;
import com.gome.haoyuangong.net.url.SearchContentAndCongenialUrl;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.DateUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.xlistview.XListView;
import com.gome.haoyuangong.views.xlistview.XListView.IXListViewListener;

public class SearchContentResultActivity extends BaseActivity {

	private static final String TAG = SearchContentResultActivity.class
			.getName();

	private static final int PULL_REFRESH = 1;
	private static final int LOAD_MORE = 2;
	private static final int FIRST_LOAD = 3;

	private XListView myListView;
	
	private ListView mListView;

	private List<SearchContentData> mSearchContentListData = new ArrayList<SearchContentData>();

	private List<HotOpinionListBean.OpinionItem> dataList = new ArrayList<HotOpinionListBean.OpinionItem>();
	
	private SearchContentAdapter myAdapter;
	
	private MyListAdapter myAdapter1;

	// RequestQueue mQueue;

	private int currPage = 0;
	private int pageSize = 12;

	private ImageLoader imageLoader;

	String keyword;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_refresh_listview_search_layout);
		setTitle("搜索观点");
		initRecommendList();
		myListView = (XListView) findViewById(R.id.listView);
		myListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				getSearchContentList(keyword, 0, "up", 10,PULL_REFRESH);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				getSearchContentList(keyword, mSearchContentListData.size(),
						"down", 10,LOAD_MORE);
			}

		});
		myListView.setDivider(null);
		myAdapter = new SearchContentAdapter(this);
		myListView.setAdapter(myAdapter);
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (id >= 0 && id < mSearchContentListData.size()) {
					SearchContentData d = mSearchContentListData.get((int)id);
					if (d != null) {
						Intent aIntent = new Intent(SearchContentResultActivity.this, AttentionDetailActivity.class);
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_TITLE,   "内容详情");
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_ID, d.getId());
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_DETAIL_URL, d.getLinkUrl());
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_PRAISE_C, d.getPraise());
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_TOUGUID, d.getUserId());
						aIntent.putExtra(
								AttentionDetailActivity.BUNDLE_PARAM_OPTITLE,
								d.getTitle());
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_LIMIT, d.getLimits());
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_TOUGUNAME, d.getUserName());
						startActivity(aIntent);
					} else {
						Toast.makeText(SearchContentResultActivity.this, "无效观点", Toast.LENGTH_SHORT).show();
					}

				}
			}
		});

		// mQueue = Volley.newRequestQueue(getApplicationContext());
		imageLoader = new ImageLoader(SearchContentResultActivity.this);

	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub

		try {
			keyword = getIntent().getStringExtra("KEYWORD");
			keyword = URLEncoder.encode(keyword, "UTF-8");
			getSearchContentList(keyword, 0, "down", 10,FIRST_LOAD);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class SearchContentAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public SearchContentAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mSearchContentListData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mSearchContentListData.get(position);
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
				convertView = mInflater.inflate(
						R.layout.opinion_listitem_dongtai, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.userIcon = (ImageView) convertView
						.findViewById(R.id.user_icon);
				viewHolder.userName = (TextView) convertView
						.findViewById(R.id.user_name);
				viewHolder.userRole = (TextView) convertView
						.findViewById(R.id.user_role);
				viewHolder.userCompany = (TextView) convertView
						.findViewById(R.id.user_company);
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.time);
				viewHolder.opinionTitle = (TextView) convertView
						.findViewById(R.id.opinion_title);
				viewHolder.opinionContent = (TextView) convertView
						.findViewById(R.id.opinion_content);
				viewHolder.support = (TextView) convertView
						.findViewById(R.id.support);
				// aViewHolder.comment = (TextView)
				// convertView.findViewById(R.id.comment);
				viewHolder.imageLimit = (ImageView) convertView
						.findViewById(R.id.image_limit);
				viewHolder.ivCertif = (ImageView) convertView
						.findViewById(R.id.iv_certif);
				viewHolder.opImagesLayout = (LinearLayout) convertView.findViewById(R.id.op_images_layout);
				// ViewGroup.LayoutParams vgl =
				// aViewHolder.opImagesLayout.getLayoutParams();
				// vgl.height = aViewHolder.opImagesLayout.getMeasuredWidth() /
				// 3;
				// aViewHolder.opImagesLayout.setLayoutParams(vgl);
				viewHolder.opImage[0] = (ImageView) convertView.findViewById(R.id.op_image_1);
				viewHolder.opImage[1] = (ImageView) convertView.findViewById(R.id.op_image_2);
				viewHolder.opImage[2] = (ImageView) convertView.findViewById(R.id.op_image_3);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			final SearchContentData itemData = mSearchContentListData.get(position);
			viewHolder.userName.setText(itemData.getUserName());
			viewHolder.userRole.setText(itemData.getAdviserTypeDesc());
			viewHolder.userCompany.setText(itemData.getCompany());
			viewHolder.time.setText(DateUtils.getTimeAgoString(itemData.getCtime(), "MM-dd HH:mm"));
			viewHolder.opinionTitle.setText(itemData.getTitle());
			viewHolder.opinionContent.setText(itemData.getSummary());
			viewHolder.support.setText("" + itemData.getPraise());
			if (1 == itemData.getLimits()) {
				viewHolder.imageLimit.setVisibility(View.GONE);
			} else {
				viewHolder.imageLimit.setVisibility(View.VISIBLE);
			}
			if (1 == itemData.getVefify()) {
				viewHolder.ivCertif.setVisibility(View.VISIBLE);
			} else {
				viewHolder.ivCertif.setVisibility(View.GONE);
			}
			if (StringUtils.isEmpty(itemData.getHeadImage())) {
				viewHolder.userIcon.setImageResource(R.drawable.icon_head_default);
			} else {
				imageLoader.downLoadImage(itemData.getHeadImage(), viewHolder.userIcon);
			}
			viewHolder.userIcon.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(SearchContentResultActivity.this,ViewInvesterInfoActivity.class);
					intent.putExtra("USERNAME", itemData.getUserName());
					intent.putExtra("USERID", itemData.getUserId());
					startActivity(intent);
				}
			});
			if (StringUtils.isEmpty(itemData.getThumbnailurl())) {
				viewHolder.opImagesLayout.setVisibility(View.GONE);
				for (ImageView image : viewHolder.opImage) {
					image.setImageBitmap(null);
				}
			} else {
				viewHolder.opImagesLayout.setVisibility(View.VISIBLE);
//				Logger.error(TAG, "download : "+itemData.getImgUrl());
				imageLoader.downLoadImage(itemData.getThumbnailurl(), viewHolder.opImage[0],R.drawable.point_default_icon,R.drawable.point_default_icon);
				viewHolder.opImage[0].setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(SearchContentResultActivity.this,ImageViewerActivity.class);
						intent.putExtra(ImageViewerActivity.BUNDLE_PARAM_FILEPATH, itemData.getImgUrl());
						startActivity(intent);
					}
				});
			}
			return convertView;
		}

		private String getDateStr(long millis) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(millis);
			Formatter ft = new Formatter(Locale.CHINA);
			return ft.format("%1$tY-%1$tm-%1$td %1$tT ", cal).toString();
		}

		class ViewHolder {
			ImageView userIcon;
			TextView userName;
			TextView userRole;
			TextView userCompany;
			TextView time;
			TextView opinionTitle;
			TextView opinionContent;
			TextView support;
			// TextView comment;
			ImageView imageLimit;
			ImageView ivCertif;
			
			LinearLayout opImagesLayout;
			ImageView opImage[] = new ImageView[3];
		}
	}

	private void getSearchContentList(String keyword, final int from,
			final String direction, final int requestsize,final int requestType) {

		String url = String.format(SearchContentAndCongenialUrl.CONTENT_LIST,
				keyword, from, requestsize, direction);

		Log.e(TAG, url);
		JsonRequest<SearchContentBean> request = new JsonRequest<SearchContentBean>(
				Method.GET, url, new RequestHandlerListener<SearchContentBean>(
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
					public void onSuccess(String id, SearchContentBean data) {
						if (data == null) {
//							Toast.makeText(SearchContentResultActivity.this,
//									"请求标题栏失败", Toast.LENGTH_SHORT).show();
							// }
						} else {
							if (data.getRetCode() == 0) {
								int size = data.getData().size();
								if (size > 0) {
									if (from == 0) {
										mSearchContentListData.clear();
									}
									mSearchContentListData.addAll(data
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
											SearchContentResultActivity.this,
											"无此类信息", Toast.LENGTH_SHORT).show();
								}
								if (size > 0 && size < requestsize || size <= 0) {
									myListView.setPullLoadEnable(false);
								}
								if (from == 0 && size == 0) {
									// 发起二次推荐数据请求
									if (UserInfo.getInstance().isLogin()) {
										getRecommendList(UserInfo.getInstance().getUserId());
									}
									
								}
							} else {
//								Toast.makeText(
//										SearchContentResultActivity.this,
//										data.getMsg(), Toast.LENGTH_SHORT)
//										.show();
							}
						}

					}
				}, SearchContentBean.class);

		send(request);

	}

	private void getRecommendList(String userId) {
		String url = NetUrlTougu.DONGTAI_HOT;
		Log.e(TAG, url);
		JsonRequest<HotOpinionListBean> request = new JsonRequest<HotOpinionListBean>(
				Method.GET, url, new RequestHandlerListener<HotOpinionListBean>(
						getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
//						 showDialog(request,"获取推荐内容...");
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
//						 hideDialog(request);
					}

					@Override
					public void onSuccess(String id, HotOpinionListBean data) {
						if (data == null) {
//							Toast.makeText(SearchContentResultActivity.this,
//									"请求标题栏失败", Toast.LENGTH_SHORT).show();
							// }
						} else {
							if (data.getRetCode() == 0) {
								HotOpinionListBean livelistBean = (HotOpinionListBean) data;
								dataList.addAll(livelistBean.getData()
										.getList());
								myListView.setVisibility(View.GONE);
								mListView.setVisibility(View.VISIBLE);
								myAdapter1.notifyDataSetChanged();
								if (livelistBean.getData().getList().size() < pageSize) {
								} else {
								}
							} else {
//								Toast.makeText(
//										SearchContentResultActivity.this,
//										data.getMsg(), Toast.LENGTH_SHORT)
//										.show();
							}
						}

					}
				}, HotOpinionListBean.class);

		send(request);

	}
	
	class MyListAdapter extends BaseAdapter {

		private LayoutInflater layoutInflater;

		public MyListAdapter() {
			layoutInflater = LayoutInflater.from(SearchContentResultActivity.this);
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

			AttentionViewHolder aViewHolder;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.opinion_listitem_dongtai, parent, false);
				aViewHolder = new AttentionViewHolder();
				aViewHolder.userIcon = (ImageView) convertView.findViewById(R.id.user_icon);
				aViewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
				aViewHolder.userRole = (TextView) convertView.findViewById(R.id.user_role);
				aViewHolder.userCompany = (TextView) convertView.findViewById(R.id.user_company);
				aViewHolder.time = (TextView) convertView.findViewById(R.id.time);
				aViewHolder.opinionTitle = (TextView) convertView.findViewById(R.id.opinion_title);
				aViewHolder.opinionContent = (TextView) convertView.findViewById(R.id.opinion_content);
				aViewHolder.support = (TextView) convertView.findViewById(R.id.support);
				aViewHolder.comment = (TextView)convertView.findViewById(R.id.comment);
				aViewHolder.imageLimit = (ImageView) convertView.findViewById(R.id.image_limit);
				aViewHolder.ivCertif = (ImageView) convertView.findViewById(R.id.iv_certif);

				aViewHolder.opImagesLayout = (LinearLayout) convertView.findViewById(R.id.op_images_layout);
//				ViewGroup.LayoutParams vgl = aViewHolder.opImagesLayout.getLayoutParams();
//				vgl.height = aViewHolder.opImagesLayout.getMeasuredWidth() / 3;
//				aViewHolder.opImagesLayout.setLayoutParams(vgl);
				aViewHolder.opImage[0] = (ImageView) convertView.findViewById(R.id.op_image_1);
				aViewHolder.opImage[1] = (ImageView) convertView.findViewById(R.id.op_image_2);
				aViewHolder.opImage[2] = (ImageView) convertView.findViewById(R.id.op_image_3);
				convertView.setTag(aViewHolder);
			} else {
				aViewHolder = (AttentionViewHolder) convertView.getTag();
			}

			final HotOpinionListBean.OpinionItem itemData = dataList.get(position);
			aViewHolder.userName.setText(itemData.getUserName());
			aViewHolder.userRole.setText(itemData.getUserInfo().getTypeDesc());
			aViewHolder.userCompany.setText(itemData.getUserInfo().getCompany());
			aViewHolder.time.setText(DateUtils.getTimeAgoString(itemData.getCtime(), "MM-dd HH:mm"));
			aViewHolder.opinionTitle.setText(itemData.getTitle());
			aViewHolder.opinionContent.setText(itemData.getSummary());
			aViewHolder.comment.setText("" + itemData.getComments());
			aViewHolder.support.setText("" + itemData.getPraise());
			if (1 == itemData.getLimits()) {
				aViewHolder.imageLimit.setVisibility(View.GONE);
			} else {
				aViewHolder.imageLimit.setVisibility(View.VISIBLE);
			}
			if (1 == itemData.getUserInfo().getSignV()) {
				aViewHolder.ivCertif.setVisibility(View.VISIBLE);
			} else {
				aViewHolder.ivCertif.setVisibility(View.GONE);
			}
			if (StringUtils.isEmpty(itemData.getUserInfo().getHeadImage())) {
				aViewHolder.userIcon.setImageResource(R.drawable.icon_head_default);
			} else {
				imageLoader.downLoadImage(itemData.getUserInfo().getHeadImage(), aViewHolder.userIcon);
			}
			
			if (StringUtils.isEmpty(itemData.getThumbnailurl())) {
				aViewHolder.opImagesLayout.setVisibility(View.GONE);
				for (ImageView image : aViewHolder.opImage) {
					image.setImageBitmap(null);
				}
			} else {
				aViewHolder.opImagesLayout.setVisibility(View.VISIBLE);
//				Logger.error(TAG, "download : "+itemData.getImgUrl());
				imageLoader.downLoadImage(itemData.getThumbnailurl(), aViewHolder.opImage[0],R.drawable.point_default_icon,R.drawable.point_default_icon);
				aViewHolder.opImage[0].setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(SearchContentResultActivity.this,ImageViewerActivity.class);
						intent.putExtra(ImageViewerActivity.BUNDLE_PARAM_FILEPATH, itemData.getImgUrl());
						startActivity(intent);
					}
				});
			}
			
			aViewHolder.userIcon.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(SearchContentResultActivity.this, ViewInvesterInfoActivity.class);
					intent.putExtra("USERNAME", itemData.getUserInfo().getUserName());
					intent.putExtra("USERID", itemData.getUserInfo().getUserId());
					startActivity(intent);
				}
			});
//			convertView.requestLayout();
			
//			imageLoader.downLoadImage(itemData.getPointPic(), aViewHolder.opImage[0]);
			return convertView;
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
			TextView comment;
			ImageView imageLimit;
			ImageView ivCertif;

			LinearLayout opImagesLayout;
			ImageView opImage[] = new ImageView[3];
		}

	}
	private void setListHeader() {
		final View v1 = getLayoutInflater().inflate(
				R.layout.item_search_congenial_list_header, null, false);
		mListView.addHeaderView(v1);
		
		TextView labelTextView1=(TextView)v1.findViewById(R.id.labelTextView1);
		labelTextView1.setText("暂无相关关键词的观点，您可以尝试其他搜索条件\n精心为您推荐的观点");
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

	private void initRecommendList(){
		mListView = (ListView) findViewById(R.id.mlistView);
		setListHeader();
		myAdapter1 = new MyListAdapter();
		mListView.setAdapter(myAdapter1);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (id >= 0 && id < dataList.size()) {
					HotOpinionListBean.OpinionItem d = dataList.get((int) id);
					if (d != null) {
						Intent aIntent = new Intent(SearchContentResultActivity.this, AttentionDetailActivity.class);
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_TITLE,  "观点详情");
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_ID, d.getId());
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_DETAIL_URL,d.getDetailUrl());
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_PRAISE_C, d.getPraise());
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_TOUGUID, d.getUserInfo().getUserId());
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_OPTITLE, d.getTitle());
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_LIMIT, d.getLimits());
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_TOUGUNAME, d.getUserInfo().getUserName());
						startActivity(aIntent);
					} else {
						Toast.makeText(SearchContentResultActivity.this, "无效观点", Toast.LENGTH_SHORT).show();
					}

				}
			}
		});
	}

}
