package com.gome.haoyuangong.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.activity.AskDetailActivity;
import com.gome.haoyuangong.activity.AttentionDetailActivity;
import com.gome.haoyuangong.activity.HotOpinionsActivity;
import com.gome.haoyuangong.activity.LiveListActivity;
import com.gome.haoyuangong.activity.LoginActivity;
import com.gome.haoyuangong.activity.OpenConsultingActivity;
import com.gome.haoyuangong.activity.SearchCongenialAndContentActivity;
import com.gome.haoyuangong.activity.ViewInvesterInfoActivity;
import com.gome.haoyuangong.activity.WriteOpinionActivity;
import com.gome.haoyuangong.adapter.MyBaseAdapter;
import com.gome.haoyuangong.mediarecorder.SoundRecorder;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.ask.AskItemAnswerResult.AskItemAnswer;
import com.gome.haoyuangong.net.result.ask.AskItemAnswerResult.LastedAnswer;
import com.gome.haoyuangong.net.result.found.AdviserUserData;
import com.gome.haoyuangong.net.result.found.AnswerData;
import com.gome.haoyuangong.net.result.found.FoundBean;
import com.gome.haoyuangong.net.result.found.HotPointsData;
import com.gome.haoyuangong.net.result.found.LastAnswer;
import com.gome.haoyuangong.net.result.found.RecommendsData;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.presenter.IAskListPresenter;
import com.gome.haoyuangong.utils.DateUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.ProgressView;
import com.gome.haoyuangong.views.SwipeRefreshLayout;
import com.gome.haoyuangong.views.SwipeRefreshLayout.OnRefreshListener;

public class FoundFragments extends BaseFragment implements OnRefreshListener {

	private static final String TAG = FoundFragments.class.getName();

	public static final int CONTENT_ATTENTION = 0;
	public static final int CONTENT_MESSAGE = 1;
	public static final int CONTENT_OPINION = 2;
	public static final int CONTENT_INFO = 3;
	
	private SwipeRefreshLayout mSwipeContainer;

	private ScrollView mScrollView;

	@SuppressWarnings("rawtypes")
	private FoundListAdapter myAdapter;

	@SuppressWarnings("rawtypes")
	private FoundHotIdeaListAdapter myAdapter1;

	private ListView listView;

	private ListView listView1;

	private List<HotPointsData> dataList = new ArrayList<HotPointsData>();

	private List<AnswerData> dataList1 = new ArrayList<AnswerData>();

	/** 滑动控件 */
	private ViewPager viewPager;
	/** 引导进度条 */
	private LinearLayout layout;

	private Display display;

	/** 存放引导进度条需要的View */
	// private ArrayList<View> pageViews;
	/** 存放引导进度条中需要的ImageView */
	private ImageView[] imageViews;

	private FindPageAdapter findPageAdapter;

	List<RecommendsData> recommendsDatalist = new ArrayList<RecommendsData>();

	private ImageView findPoint;

	private static final float SCALE = 0.33f;

	ImageLoader mImageLoader;
	private int maxTime;

	private IAskListPresenter mIAskListPresenter = new IAskListPresenter(this) {
		public void onPalyFinish() {
			myAdapter1.notifyDataSetChanged();
		};

		public void onRefresh() {
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageLoader = new ImageLoader(getActivity());
		maxTime = (int) (SoundRecorder.MAX_SOUND_SIZE / 1000);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View parent = super.onCreateView(inflater, container,
				savedInstanceState);
		View child = inflater.inflate(R.layout.found_listview_layout, null,
				false);
		content.addView(child);
		initChildTitle();
		findView(parent);
		titleWhole.setVisibility(View.VISIBLE);
		return parent;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (UserInfo.getInstance().isLogin()
				&& UserInfo.getInstance().isTougu()) {
			titleRight1.setBackgroundResource(R.drawable.top_search_icon);
			titleRight1.setOnClickListener(this);
			titleRight2.setBackgroundResource(R.drawable.icon_write_opinion);
			titleRight2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getContext(),
							WriteOpinionActivity.class);
					startActivity(intent);
				}
			});

		} else {
			titleRight1.setBackgroundResource(0);
			titleRight1.setOnClickListener(null);
			titleRight2.setBackgroundResource(R.drawable.top_search_icon);
			titleRight2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(mActivity, SearchCongenialAndContentActivity.class));
				}
			});
//			titleRight2.setBackgroundResource(R.drawable.top_ask_icon);
//			titleRight2.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (UserInfo.getInstance().isLogin()) {
//						Intent intent = new Intent(getContext(),
//								OpenConsultingActivity.class);
//						startActivity(intent);
//					} else {
//						Intent intent = new Intent(getContext(),
//								LoginActivity.class);
//						intent.putExtra(
//								LoginActivity.BUNDLE_PARAM_TARGET_ACTIVITY,
//								"com.jrj.tougu.activity.OpenConsultingActivity");
//						intent.putExtra(
//								LoginActivity.BUNDLE_PARAM_TARGET_ACTIVITY_TYPE,
//								1);
//						startActivity(intent);
//					}
//				}
//			});
		}
	}

	private void initChildTitle() {
		titleLeft1.setVisibility(View.GONE);
		titleCenter.setText("发现");
//		titleRight1.setBackgroundResource(R.drawable.top_search_icon);
//		titleRight1.setOnClickListener(this);
//		titleRight2.setBackgroundResource(R.drawable.icon_write_opinion);
//		titleRight2.setOnClickListener(this);
	}

	@SuppressWarnings("unchecked")
	private void findView(View v) {
		mSwipeContainer=(SwipeRefreshLayout)v.findViewById(R.id.swipeContainer);
		mSwipeContainer.setTag(this.getClass().getName());
		mSwipeContainer.setOnRefreshListener(this);
		mSwipeContainer.setRefreshing(false);
		mScrollView = (ScrollView) v.findViewById(R.id.ScrollViewId);
		layout = (LinearLayout) v.findViewById(R.id.groupView);
		viewPager = (ViewPager) v.findViewById(R.id.viewPages);
		display = mActivity.getWindowManager().getDefaultDisplay();

		
		findPageAdapter = new FindPageAdapter(mActivity, recommendsDatalist);
		viewPager.setAdapter(findPageAdapter);
		viewPager.setOnPageChangeListener(new FindPageChangeListener());

		listView = (ListView) v.findViewById(R.id.found_listview);

		listView1 = (ListView) v.findViewById(R.id.found_listview1);

		myAdapter1 = new FoundHotIdeaListAdapter<AnswerData>(mActivity,
				dataList1);
		listView1.setAdapter(myAdapter1);
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				// TODO Auto-generated method stub
//				Intent i = new Intent(mActivity,
//						com.jrj.tougu.activity.AskDetailActivity_.class);
//				// if (id < dataAnswer.size() && id >= 0) {
//				i.putExtra(AskDetailActivity.BUNDLE_Id, dataList1.get((int) id)
//						.getAskId());
//				startActivity(i);
				// }
			}

		});

		myAdapter = new FoundListAdapter<HotPointsData>(mActivity, dataList);
		listView.setAdapter(myAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (id >= 0 && id < dataList.size()) {
					HotPointsData d = dataList.get((int) id);
					if (d != null) {
						Intent aIntent = new Intent(mActivity,
								AttentionDetailActivity.class);
						aIntent.putExtra(
								AttentionDetailActivity.BUNDLE_PARAM_TITLE,
								"内容详情");
						aIntent.putExtra(
								AttentionDetailActivity.BUNDLE_PARAM_ID,
								Integer.valueOf(d.getId()));
						aIntent.putExtra(
								AttentionDetailActivity.BUNDLE_PARAM_DETAIL_URL,
								d.getUrl());
						aIntent.putExtra(
								AttentionDetailActivity.BUNDLE_PARAM_PRAISE_C,
								2);
						aIntent.putExtra(
								AttentionDetailActivity.BUNDLE_PARAM_TOUGUID,
								d.getUserid());
						aIntent.putExtra(
								AttentionDetailActivity.BUNDLE_PARAM_OPTITLE,
								d.getTitle());
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_TOUGUNAME, d.getTouguname());
//						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_LIMIT, d.getLimits());
						startActivity(aIntent);
					} else {
						Toast.makeText(mActivity, "无效观点", Toast.LENGTH_SHORT)
								.show();
					}

				}
			}
		});

		findPoint = (ImageView) v.findViewById(R.id.find_point_image);
		findPoint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(mActivity, HotOpinionsActivity.class));
			}

		});
		ImageView findLive = (ImageView) v.findViewById(R.id.find_live_image);
		findLive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(mActivity, LiveListActivity.class));
			}

		});
		ImageView findPeople = (ImageView) v
				.findViewById(R.id.find_people_image);
		findPeople.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				startActivity(new Intent(mActivity,
//						com.jrj.tougu.activity.FindCongenialListActivity_.class));
			}

		});

		ImageView findTalk = (ImageView) v.findViewById(R.id.find_talk_image);
		findTalk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				startActivity(new Intent(mActivity,
//						com.jrj.tougu.activity.AskTallActivity_.class));
			}

		});

		TextView pointMore = (TextView) v.findViewById(R.id.point_more);
		pointMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// startActivity(new Intent(mActivity,
				// com.jrj.tougu.activity.StartQuestionnaireActivity.class));
				startActivity(new Intent(mActivity, HotOpinionsActivity.class));
			}

		});

		TextView recommendMore = (TextView) v.findViewById(R.id.recommend_more);
		recommendMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				startActivity(new Intent(mActivity,
//						com.jrj.tougu.activity.FindCongenialListActivity_.class));
			}

		});

		TextView answerMore = (TextView) v.findViewById(R.id.answer_more);
		answerMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				startActivity(new Intent(mActivity,
//						com.jrj.tougu.activity.AskTallActivity_.class));
			}

		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_right1:
			startActivity(new Intent(mActivity,
					SearchCongenialAndContentActivity.class));
			break;
		case R.id.title_right2:
			startActivity(new Intent(mActivity, WriteOpinionActivity.class));
			break;

		default:
			break;
		}
	}

	class FoundListAdapter<T> extends MyBaseAdapter<T> {
		List<T> mlist;

		public FoundListAdapter(Context ctx, List<T> list) {
			super(ctx, list);
			mlist = list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = ViewHolder.getInstance(context, convertView,
					parent, R.layout.found_listview_item_layout);
			if (convertView == null) {
				convertView = holder.getView();
				TextView contentTextView = (TextView) convertView
						.findViewById(R.id.foudHotPointsContentTextview);
				final HotPointsData data = (HotPointsData) mlist.get(position);
				contentTextView.setText(data.getTitle());
				TextView nameTextView = (TextView) convertView
						.findViewById(R.id.foudHotPointsNameTextview);
				nameTextView.setText(data.getTouguname());
				nameTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(mActivity,
								ViewInvesterInfoActivity.class);
						intent.putExtra("USERNAME", data.getTouguname());
						intent.putExtra("USERID", data.getUserid());
						startActivity(intent);
					}

				});
				TextView positionTextView = (TextView) convertView
						.findViewById(R.id.foudHotPointsPositionTextview);
				positionTextView.setText(data.getPosition());

				ImageView headImage = (ImageView) convertView
						.findViewById(R.id.imageH);
				mImageLoader.downLoadImage(data.getImg(), headImage,
						R.drawable.point_default_icon,
						R.drawable.point_default_icon);
				convertView.setTag(holder);
			}

			return convertView;
		}

	}

	private void setQaContent(TextView qaTv, String name, SpannableString content) {
//		SpannableString sp = new SpannableString(name + "：" + content);
		ForegroundColorSpan span = new ForegroundColorSpan(getResources()
				.getColor(R.color.font_4c86c6));
		content.setSpan(span, 0, name.length() + 1,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		qaTv.setMovementMethod(LinkMovementMethod.getInstance());
		qaTv.setText(content);
	}

	class FoundHotIdeaListAdapter<T> extends MyBaseAdapter<T> {
		List<T> mlist;

		public FoundHotIdeaListAdapter(Context ctx, List<T> list) {
			super(ctx, list);
			mlist = list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = ViewHolder.getInstance(context, convertView,
					parent, R.layout.found_hotidea_listview_item_layout);

			if (convertView == null) {
				convertView = holder.getView();
				convertView.setTag(holder);
			}
			AnswerData bean = (AnswerData) dataList1.get(position);
			TextView answerName = holder.getView(R.id.ask_item_tv_name);
			final LastAnswer answer = bean.getLastedAnswer();
			final AdviserUserData touguBean = answer.getAdviserUser();
			if (touguBean != null) {
				answerName.setText(touguBean.getUserName());
				TextView answerTime = holder.getView(R.id.ask_item_tv_time);
				answerTime.setText(DateUtils.getTimeAgoString(bean.getCtime(),
						"MM-dd hh:mm"));
				ImageView talkButton = (ImageView) holder
						.getView(R.id.ask_item_tv_talk_button);
				talkButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						if (UserInfo.getInstance().isLogin()) {
							Intent intent = new Intent(mActivity,OpenConsultingActivity.class);
							intent.putExtra(OpenConsultingActivity.BUNDLE_TYPE,OpenConsultingActivity.SPECIAL_CONSULTING);
							intent.putExtra(OpenConsultingActivity.BUNDLE_PARAM_NAME,touguBean.getUserName());
							intent.putExtra(OpenConsultingActivity.BUNDLE_PARAM_ID,touguBean.getUserId());
							startActivity(intent);
						} else {
							Intent intent = new Intent(getContext(),LoginActivity.class);
							intent.putExtra(LoginActivity.BUNDLE_PARAM_TARGET_ACTIVITY,"com.jrj.tougu.activity.OpenConsultingActivity");
							intent.putExtra(OpenConsultingActivity.BUNDLE_TYPE,OpenConsultingActivity.SPECIAL_CONSULTING);
							intent.putExtra(OpenConsultingActivity.BUNDLE_PARAM_NAME,touguBean.getUserName());
							intent.putExtra(OpenConsultingActivity.BUNDLE_PARAM_ID,touguBean.getUserId());
							startActivity(intent);
						}
					}

				});
				TextView answerCompany = holder
						.getView(R.id.ask_item_tv_company);
				answerCompany.setText(touguBean.getTypeDesc() + " "
						+ (StringUtils.isEmpty(touguBean.getCompany())?"":touguBean.getCompany()));

				ImageView answerV = holder.getView(R.id.ask_item_IvV);
				if (touguBean.getSignV() == 1) {
					answerV.setVisibility(View.VISIBLE);
				} else {
					answerV.setVisibility(View.GONE);
				}
				RatingBar mRating = holder.getView(R.id.rating_star);
				if (touguBean.getGrowupVal() > 0) {
					mRating.setNumStars(touguBean.getGrowupVal());
				}
				ImageView icon = holder.getView(R.id.headpic);
				mImageLoader.downLoadImage(touguBean.getHeadImage(), icon);
			}
			TextView qa = holder.getView(R.id.ask_item_tv_qa);
			setQaContent(qa, bean.getAusername(), bean.getContentSpanStr());
			TextView answerContent = holder.getView(R.id.ask_item_tv_answer);
			TextView answerCount = holder.getView(R.id.ask_item_tv_answercount);
			answerCount.setText(bean.getAnswerTimes() + "人回答");
			if (answer != null) {
				View answerMediaWhole = holder
						.getView(R.id.ask_item_tv_answer_media);
				if (!StringUtils.isBlank(answer.getVoiceAmr())) {
					answerMediaWhole.setVisibility(View.VISIBLE);
					answerContent.setVisibility(View.GONE);
					View answerMediaLy = holder.getView(R.id.ask_item_media_ly);
					View answerMediaSpace = holder
							.getView(R.id.ask_item_media_space);
					TextView mediaTime = holder
							.getView(R.id.ask_item_media_time);
					int time = answer.getVoicelength();
					LayoutParams params = (LayoutParams) answerMediaLy
							.getLayoutParams();
					LayoutParams params1 = (LayoutParams) answerMediaSpace
							.getLayoutParams();
					params.weight = time;
					params1.weight = maxTime - time;
					answerMediaLy.setLayoutParams(params);
					answerMediaSpace.setLayoutParams(params1);
					mediaTime.setText(context.getString(R.string.timer_format,
							time));
					final ImageView mediaVUMeter = holder
							.getView(R.id.ask_item_media_vum_right);
					ProgressView mProgressView = (ProgressView) mediaVUMeter
							.getTag();
					if (mProgressView == null) {
						mProgressView = new com.gome.haoyuangong.views.ProgressView(
								getContext(), mediaVUMeter);
						mediaVUMeter.setTag(mProgressView);
					}

					final AnimationDrawable anim = (AnimationDrawable) mediaVUMeter
							.getBackground();
					if (mIAskListPresenter.isPlaying((answer.getVoiceAmr()))) {
						anim.start();
					} else {
						anim.stop();
						anim.selectDrawable(0);
					}
					final ProgressView PV = mProgressView;
					answerMediaLy.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							mIAskListPresenter.mediaPlayClick(
									answer.getVoiceAmr(), anim, PV);
						}
					});
				} else {
					answerContent.setText(answer.getContentSpanStr());
//					answerContent.setMovementMethod(LinkMovementMethod.getInstance());
					answerMediaWhole.setVisibility(View.GONE);
					answerContent.setVisibility(View.VISIBLE);
				}
			}
			return convertView;

		}

	}

	class FindPageAdapter extends PagerAdapter {
		List<RecommendsData> mlist;
		private LayoutInflater mInflater;

		public FindPageAdapter(Context ctx, List<RecommendsData> list) {
			mInflater = LayoutInflater.from(ctx);
			mlist = list;
			initView();
		}

		@Override
		public int getCount() {
			return mlist.size() / 3;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			View v1 = mInflater.inflate(R.layout.find_congenial_item1, null);
			((ViewPager) arg0).removeView(v1);
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			View v1 = mInflater.inflate(R.layout.find_congenial_item1, null);

			RecommendsData data1 = mlist.get(arg1 * 3);

			final String userName1 = data1.getName();
			final String userId1 = data1.getUserid();
			LinearLayout layout1 = (LinearLayout) v1.findViewById(R.id.layout1);
			layout1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mActivity,
							ViewInvesterInfoActivity.class);
					intent.putExtra("USERNAME", userName1);
					intent.putExtra("USERID", userId1);
					startActivity(intent);
				}

			});
			ImageView im1 = (ImageView) v1.findViewById(R.id.headImageView1);
			mImageLoader.downLoadImage(data1.getImg(), im1);
			TextView na1 = (TextView) v1.findViewById(R.id.nameTextView1);
			na1.setText(data1.getName());
			TextView po1 = (TextView) v1.findViewById(R.id.positionTextView1);
			po1.setText(data1.getPosition());

			RecommendsData data2 = mlist.get(arg1 * 3 + 1);
			final String userName2 = data2.getName();
			final String userId2 = data2.getUserid();
			LinearLayout layout2 = (LinearLayout) v1.findViewById(R.id.layout2);
			layout2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mActivity,
							ViewInvesterInfoActivity.class);
					intent.putExtra("USERNAME", userName2);
					intent.putExtra("USERID", userId2);
					startActivity(intent);
				}

			});

			ImageView im2 = (ImageView) v1.findViewById(R.id.headImageView2);
			mImageLoader.downLoadImage(data2.getImg(), im2);
			TextView na2 = (TextView) v1.findViewById(R.id.nameTextView2);
			na2.setText(data2.getName());
			TextView po2 = (TextView) v1.findViewById(R.id.positionTextView2);
			po2.setText(data2.getPosition());

			RecommendsData data3 = mlist.get(arg1 * 3 + 2);

			final String userName3 = data3.getName();
			final String userId3 = data3.getUserid();
			LinearLayout layout3 = (LinearLayout) v1.findViewById(R.id.layout3);
			layout3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mActivity,
							ViewInvesterInfoActivity.class);
					intent.putExtra("USERNAME", userName3);
					intent.putExtra("USERID", userId3);
					startActivity(intent);
				}

			});

			ImageView im3 = (ImageView) v1.findViewById(R.id.headImageView3);
			mImageLoader.downLoadImage(data3.getImg(), im3);
			TextView na3 = (TextView) v1.findViewById(R.id.nameTextView3);
			na3.setText(data3.getName());
			TextView po3 = (TextView) v1.findViewById(R.id.positionTextView3);
			po3.setText(data3.getPosition());

			((ViewPager) arg0).addView(v1);
			return v1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		void initView() {
			if (getCount() > 1) {
				// 初始化引导进度条
				imageViews = new ImageView[getCount()];
				for (int i = 0; i < getCount(); i++) {
					ImageView imageView = new ImageView(mActivity);
					android.widget.LinearLayout.LayoutParams params = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					DisplayMetrics metrics = new DisplayMetrics();
					mActivity.getWindowManager().getDefaultDisplay()
							.getMetrics(metrics);
					params.leftMargin = (int) (3 * metrics.density + 0.5f);
					params.rightMargin = (int) (3 * metrics.density + 0.5f);
					imageView.setLayoutParams(params);
					imageViews[i] = imageView;

					if (i == 0) {
						imageViews[i]
								.setBackgroundResource(R.drawable.find_page_indicator_focused);
					} else {
						imageViews[i]
								.setBackgroundResource(R.drawable.find_page_indicator);
					}
					layout.addView(imageViews[i]);
				}
			}
		}
	}

	private class FindPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0]
						.setBackgroundResource(R.drawable.find_page_indicator_focused);
				if (arg0 != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.find_page_indicator);
				}
			}
		}
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		getData();
	}

	private void getData() {
		String url = "http://mapi.itougu.jrj.com.cn/wireless/ques/discover";
		JsonRequest<FoundBean> request = new JsonRequest<FoundBean>(Method.GET,
				url, new RequestHandlerListener<FoundBean>(getContext()) {

					@Override
					public void onFailure(String id, int code, String str,
							Object obj) {
						// TODO Auto-generated method stub
						super.onFailure(id, code, str, obj);
					}

					@Override
					public void onStart(Request request) {
						// TODO Auto-generated method stub
						super.onStart(request);
						if(mSwipeContainer.isRefreshing()){
						}else{
							showLoading(request);
						}
						
					}

					@Override
					public void onEnd(Request request) {
						// TODO Auto-generated method stub
						super.onEnd(request);
						
						if(mSwipeContainer.isRefreshing()){
							mSwipeContainer.stopRefresh();
						}else{
							hideLoading(request);
						}
					}

					@Override
					public void onSuccess(String id, FoundBean data) {
						// TODO Auto-generated method stub
						if (data instanceof FoundBean) {
							mScrollView.setVisibility(View.VISIBLE);
							dataList.clear();
							recommendsDatalist.clear();
							dataList1.clear();
							dataList.addAll(((FoundBean) data).getHotpoints());
							recommendsDatalist.addAll(((FoundBean) data)
									.getRecommends());
							ArrayList<AnswerData> list = ((FoundBean) data).getAsklist();
							for(AnswerData answer:list){
							answer.setContentSpanStr(mIAskListPresenter.handleAskStr(answer.getAusername()+"："+answer.getContent()));
							LastAnswer lastAnswer = answer.getLastedAnswer();
							if(lastAnswer!=null){
								lastAnswer.setContentSpanStr(mIAskListPresenter.handleAskStr(lastAnswer.getContent()));
							}
						}
							dataList1.addAll(list);
							myAdapter.notifyDataSetChanged();
							findPageAdapter.notifyDataSetChanged();
							myAdapter1.notifyDataSetChanged();
						}
					}
				}, FoundBean.class);
		send(request);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		getData();
	}

}
