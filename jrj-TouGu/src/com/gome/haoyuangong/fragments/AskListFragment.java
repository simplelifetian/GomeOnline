package com.gome.haoyuangong.fragments;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.RefreshTimeInfo;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.activity.ReplyMediaRecordActivity;
import com.gome.haoyuangong.adapter.MyBaseAdapter;
import com.gome.haoyuangong.layout.self.ActivityChange;
import com.gome.haoyuangong.mediarecorder.SoundRecorder;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.result.ask.AskItemAnswerResult;
import com.gome.haoyuangong.net.result.ask.AskItemAskResult;
import com.gome.haoyuangong.net.result.ask.AskItemAnswerResult.AskItemAnswer;
import com.gome.haoyuangong.net.result.ask.AskItemAnswerResult.LastedAnswer;
import com.gome.haoyuangong.net.result.ask.AskItemAskResult.AskItemAsk;
import com.gome.haoyuangong.net.result.tougu.TouguUserBean;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.presenter.IAskListPresenter;
import com.gome.haoyuangong.utils.DateUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.ProgressView;

@EFragment
public class AskListFragment extends XListFragment {
	public static final int TYPE_NEW_ANSWER = 0;
	public static final int TYPE_NEW_ASK = 1;

	ImageLoader mImageLoader;
	BaseAdapter mAdapter = null;
	private List<AskItemAnswer> dataAnswer;
	private List<AskItemAsk> dataAsk;

	private int aid = 0;
	private int type;
	private int maxTime;
	
	private IAskListPresenter mIAskListPresenter = new IAskListPresenter(this) {
		public void onPalyFinish() {
			mAdapter.notifyDataSetChanged();
		};

		public void onRefresh(int askId, int answerStatus) {
			updateDate(askId, answerStatus);
		};
	};

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mIAskListPresenter.registReceiverRefresh();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mIAskListPresenter.unregistReceiverRefresh();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(mIAskListPresenter!=null){
			mIAskListPresenter.stopAll();
		}
	}

	@AfterViews
	void viewsInjectComplete() {
		maxTime = (int) (SoundRecorder.MAX_SOUND_SIZE / 1000);
		
		if (type == TYPE_NEW_ANSWER) {
			// mIAskListPresenter.requestLastAnswer(true,curPage);
			mList.setDivider(getResources().getDrawable(R.drawable.divider));
			mList.setDividerHeight(1);
			dataAnswer = new ArrayList<AskItemAnswer>();
			mAdapter = new MyAskAdapter(getActivity(), dataAnswer);
			mList.setRefreshTime(getRefreshTime(RefreshTimeInfo.REFRESH_ASK_ANSWER));
		} else {
			mList.setDivider(getResources().getDrawable(R.drawable.divider));
			mList.setDividerHeight(1);
			// mIAskListPresenter.requestNewAsk(true,curPage);

			dataAsk = new ArrayList<AskItemAsk>();
			mAdapter = new MyAskAdapter2(getActivity(), dataAsk);
			mList.setRefreshTime(getRefreshTime(RefreshTimeInfo.REFRESH_ASK_ASK));
		}
		mList.setAdapter(mAdapter);

		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				Intent i = new Intent(mActivity, com.jrj.tougu.activity.AskDetailActivity_.class);
//				if (type == TYPE_NEW_ANSWER) {
//					if (id < dataAnswer.size() && id >= 0) {
//						i.putExtra(AskDetailActivity.BUNDLE_Id, dataAnswer.get((int) id).getAsk_id());
//						startActivity(i);
//					}
//				} else {
//					if (id < dataAsk.size() && id >= 0) {
//						i.putExtra(AskDetailActivity.BUNDLE_Id, dataAsk.get((int) id).getAskId());
//						startActivity(i);
//					}
//				}
			}
		});
		hideTitle();
		mImageLoader = new ImageLoader(getActivity());
		mIAskListPresenter.initVoice();
		
	}

	private void updateDate(int askId, int answerStatus) {
		if (type == TYPE_NEW_ASK) {
			if (dataAsk != null && dataAsk.size() > 0) {
				for (AskItemAsk item : dataAsk) {
					if (item.getAskId() == askId) {
						item.setStatus(answerStatus);
						mAdapter.notifyDataSetChanged();
						break;
					}
				}
			}
		}
	}

	private void setQaContent(TextView qaTv, String name, SpannableString content) {
//		content =name + "：" + content;  
//		SpannableString sp = new SpannableString(content);
		ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.font_4c86c6));
		content.setSpan(span, 0, name.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		qaTv.setMovementMethod(LinkMovementMethod.getInstance());
		qaTv.setText(content);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_right1:
			break;
		case R.id.title_right2:

			break;
		case R.id.search:
			break;

		default:
			break;
		}
	}

	private void fillAnswerData(List<AskItemAnswer> list) {
		if (aid <= 0) {
			dataAnswer.clear();
			mList.setPullLoadEnable(true);
		}
		if (list.size() > 0) {
			for(AskItemAnswer answer:list){
//				answer.setContent(answer.getContent()+"「sdsaf(600000)」");
				answer.setContentSpanStr(mIAskListPresenter.handleAskStr(answer.getAusername()+"："+(answer.getContent()!=null?answer.getContent():"")));
				LastedAnswer lastAnswer = answer.getLastedAnswer();
				if(lastAnswer!=null){
//					lastAnswer.setContent(lastAnswer.getContent()+"「sdsaf(600000)」");
					lastAnswer.setContentSpanStr(mIAskListPresenter.handleAskStr(lastAnswer.getContent()));
				}
			}
			dataAnswer.addAll(list);
			mAdapter.notifyDataSetChanged();
			aid = list.get(list.size() - 1).getAsk_id();
		} else {
			mList.setPullLoadEnable(false);
		}
		if(type==TYPE_NEW_ANSWER){
			saveRefreshTime(RefreshTimeInfo.REFRESH_ASK_ANSWER);
			mList.setRefreshTime(getRefreshTime(RefreshTimeInfo.REFRESH_ASK_ANSWER));
		}else{
			saveRefreshTime(RefreshTimeInfo.REFRESH_ASK_ASK);
			mList.setRefreshTime(getRefreshTime(RefreshTimeInfo.REFRESH_ASK_ASK));
		}
	}

	private void fillAskData(List<AskItemAsk> list) {
		if (aid <= 0) {
			dataAsk.clear();
			mList.setPullLoadEnable(true);
		}
		if (list.size() > 0) {
			for(AskItemAsk ask:list){
//				ask.setContent(ask.getContent()+"「sdsaf(600000)」");
				ask.setContentSpanStr(mIAskListPresenter.handleAskStr(ask.getContent()));
			}
			dataAsk.addAll(list);
			mAdapter.notifyDataSetChanged();
			aid = list.get(list.size() - 1).getAskId();
		} else {
			mList.setPullLoadEnable(false);
		}
	}

	class MyAskAdapter<T> extends MyBaseAdapter<AskItemAnswer> {

		public MyAskAdapter(Context context, List<AskItemAnswer> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = ViewHolder.getInstance(context, convertView, parent, R.layout.item_ask_list);
			if (convertView == null) {
				convertView = holder.getView();
				convertView.setTag(holder);
			}
			AskItemAnswer bean = (AskItemAnswer) getItem(position);
			TextView answerName = holder.getView(R.id.ask_item_tv_name);
			final LastedAnswer answer = bean.getLastedAnswer();
			final TouguUserBean touguBean = answer.getAdviserUser();
			if (touguBean != null) {
				answerName.setText(touguBean.getUserName());
				TextView answerTime = holder.getView(R.id.ask_item_tv_time);
				answerTime.setText(DateUtils.getTimeAgoString(bean.getCtime(), "MM-dd hh:mm"));
				TextView answerCompany = holder.getView(R.id.ask_item_tv_company);
				answerCompany.setText(touguBean.getTypeDesc() + " " + (StringUtils.isEmpty(touguBean.getCompany())?"":touguBean.getCompany()));

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
				icon.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (touguBean != null) {
							ActivityChange.ToAdviserHome(context,  touguBean.getUserName(), touguBean.getUserId());
						}
					}
				});
				mImageLoader.downLoadImage(touguBean.getHeadImage(), icon);
			}
			TextView qa = holder.getView(R.id.ask_item_tv_qa);
			setQaContent(qa, bean.getAusername(),  bean.getContentSpanStr());
			TextView answerContent = holder.getView(R.id.ask_item_tv_answer);
			TextView answerCount = holder.getView(R.id.ask_item_tv_answercount);
			answerCount.setText(bean.getAnswerTimes() + "人回答");
			if (answer != null) {
				View answerMediaWhole = holder.getView(R.id.ask_item_tv_answer_media);
				if (!StringUtils.isBlank(answer.getVoiceAmr())) {
					answerMediaWhole.setVisibility(View.VISIBLE);
					answerContent.setVisibility(View.GONE);
					View answerMediaLy = holder.getView(R.id.ask_item_media_ly);
					View answerMediaSpace = holder.getView(R.id.ask_item_media_space);
					TextView mediaTime = holder.getView(R.id.ask_item_media_time);
					int time = answer.getVoicelength();
					LayoutParams params = (LayoutParams) answerMediaLy.getLayoutParams();
					LayoutParams params1 = (LayoutParams) answerMediaSpace.getLayoutParams();
					params.weight = time;
					params1.weight = maxTime - time;
					answerMediaLy.setLayoutParams(params);
					answerMediaSpace.setLayoutParams(params1);
					mediaTime.setText(context.getString(R.string.timer_format, time));
					final ImageView mediaVUMeter = holder.getView(R.id.ask_item_media_vum_right);
					ProgressView mProgressView = (ProgressView) mediaVUMeter.getTag();
					if (mProgressView == null) {
						mProgressView = new com.gome.haoyuangong.views.ProgressView(getContext(), mediaVUMeter);
						mediaVUMeter.setTag(mProgressView);
					}

					final AnimationDrawable anim = (AnimationDrawable) mediaVUMeter.getBackground();
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
							mIAskListPresenter.mediaPlayClick(answer.getVoiceAmr(), anim, PV);
						}
					});
				} else {
//					answerContent.setMovementMethod(LinkMovementMethod.getInstance());
					answerContent.setText(answer.getContentSpanStr());
					answerMediaWhole.setVisibility(View.GONE);
					answerContent.setVisibility(View.VISIBLE);
				}
			}
			return convertView;
		}

	}

	class MyAskAdapter2 extends MyBaseAdapter<AskItemAsk> {

		public MyAskAdapter2(Context context, List<AskItemAsk> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = ViewHolder.getInstance(context, convertView, parent, R.layout.item_ask_list2);
			if (convertView == null) {
				convertView = holder.getView();
				convertView.setTag(holder);
			}
			final AskItemAsk bean = (AskItemAsk) getItem(position);
			TextView answerName = holder.getView(R.id.ask_item_tv_name);
			answerName.setText(bean.getAusername());
			TextView answerTime = holder.getView(R.id.ask_item_tv_time);
			answerTime.setText(DateUtils.getTimeAgoString(bean.getCtime(), "MM-dd hh:mm"));
			TextView qa = holder.getView(R.id.ask_item_tv_qa);
			qa.setText(bean.getContentSpanStr());
//			qa.setMovementMethod(LinkMovementMethod.getInstance());
			ImageView icon = holder.getView(R.id.headpic);
			icon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (bean != null) {
						if(bean.getAskUserType()==1){
							ActivityChange.ToAdviserHome(context,  bean.getAusername(), bean.getAuserId());
						}else {
							ActivityChange.ToUserHome(context,bean.getAusername(), bean.getAuserId(), null);
						}
					}
				}
			});
			mImageLoader.downLoadImage(bean.getHeadImages(), icon);
			ImageView imgFensi = holder.getView(R.id.ask_item_iv_fensi);
			TextView answerBtn = holder.getView(R.id.ask_item_btn_answer);
			imgFensi.setVisibility(View.GONE);
			UserInfo info = UserInfo.getInstance();
			answerBtn.setVisibility(View.GONE);
			if (info.isTougu()) {
				if (bean.getIsMyFuns() == 1) {
					imgFensi.setImageResource(R.drawable.icon_fensi);
					imgFensi.setVisibility(View.VISIBLE);
				} else {
					imgFensi.setVisibility(View.GONE);
				}
				if (!info.isMySelf(bean.getAuserId())) {
					if (bean.getStatus() == IAskListPresenter.ANSWER_STATUS_NOTANSWER) {// 1.待回答
																																							// 2.已回答
																																							// 3.追问待回答
						answerBtn.setVisibility(View.VISIBLE);
						answerBtn.setEnabled(true);
						answerBtn.setText("回答");
						answerBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								ReplyMediaRecordActivity.goToAnswerReply(mActivity, bean.getAskId());

							}
						});
					} else {
						answerBtn.setVisibility(View.VISIBLE);
						answerBtn.setEnabled(false);
						answerBtn.setOnClickListener(null);
						answerBtn.setText("已回答");
					}
				}
			}
			return convertView;
		}

	}

	@Override
	protected <T> Request<T> getRequest() {
		if (type == TYPE_NEW_ANSWER) {
			return mIAskListPresenter.getLastAnswerRequest(aid);
		} else {
			return mIAskListPresenter.getNewAskRequest(aid);
		}

	}

	@Override
	protected void onRefreshPrepear() {
		aid = 0;

	}

	@Override
	protected void onLoadMorePrepear() {

	}

	@Override
	protected void onReceive(boolean isLoadMore, String id, Object data) {
		if (data instanceof AskItemAnswerResult) {
			fillAnswerData(((AskItemAnswerResult) data).getData().getList());
		} else if (data instanceof AskItemAskResult) {
			fillAskData(((AskItemAskResult) data).getData().getList());
		}
	}
}
