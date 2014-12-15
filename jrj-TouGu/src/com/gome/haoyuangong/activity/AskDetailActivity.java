package com.gome.haoyuangong.activity;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.adapter.MyBaseAdapter;
import com.gome.haoyuangong.adapter.MyBaseAdapter.ViewHolder;
import com.gome.haoyuangong.layout.self.ActivityChange;
import com.gome.haoyuangong.mediarecorder.SoundRecorder;
import com.gome.haoyuangong.net.result.ask.AskDetailResult.AgainAskVo;
import com.gome.haoyuangong.net.result.ask.AskDetailResult.AskDetail;
import com.gome.haoyuangong.net.result.ask.AskDetailResult.AskDetailAnswer;
import com.gome.haoyuangong.net.result.ask.AskItemAnswerResult.AskItemAnswer;
import com.gome.haoyuangong.net.result.ask.AskItemAnswerResult.LastedAnswer;
import com.gome.haoyuangong.net.result.tougu.TouguUserBean;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.presenter.IAskListPresenter;
import com.gome.haoyuangong.utils.DateUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.ProgressView;
import com.gome.haoyuangong.R;

@EActivity(R.layout.activity_ask_detail)
public class AskDetailActivity extends BaseActivity {
	public static final String BUNDLE_Id = "id";
	public static final String BUNDLE_AUTO_ANSWER = "is_auto_answer";
	@ViewById(R.id.ask_detail_list)
	ListView mList;
	@ViewById(R.id.ask_tv_answer)
	TextView mAnswer;
	@ViewById(R.id.ask_empty)
	View empty;
	@ViewById(R.id.empty_img)
	ImageView emptyIv;
	@ViewById(R.id.empty_txt)
	TextView emptyTv;
	@Extra(BUNDLE_Id)
	int id;
	@Extra(BUNDLE_AUTO_ANSWER)
	boolean isAutoAnswer = false;
	int maxTime = (int) (SoundRecorder.MAX_SOUND_SIZE / 1000);
	private ImageLoader mImageLoader;
	private AskDetail mAskDetail;
	private MyAdapter mMyAdapter;
	private View headerView;
	private int answerType;
	private IAskListPresenter mIAskListPresenter = new IAskListPresenter(this) {
		public void onDetail(AskDetail data) {
			AskDetailActivity.this.mAskDetail = data;
			fillData(data);
		};

		public void onPalyFinish() {
			if (mMyAdapter != null) {
				mMyAdapter.notifyDataSetChanged();
			}
		};

		public void onRefresh(int askId, int answerStatus) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mIAskListPresenter.requestAskDetail(false, id);
				}
			}, 500);
		};
	};

	@AfterViews
	void init() {
		setTitle("回答详情");
		mImageLoader = new ImageLoader(this);
		mIAskListPresenter.initVoice();

	}

	Handler handler = new Handler();

	@Override
	protected void onLoad() {
		super.onLoad();
		mIAskListPresenter.requestAskDetail(true, id);
	}

	@Click(R.id.ask_tv_answer)
	void answerClick() {
		if (answerType == IAskListPresenter.ASK_ANSWER_A) {
			ReplyMediaRecordActivity.goToAnswerReply(this, mAskDetail.getAskId());
		} else if (answerType == IAskListPresenter.ASK_ANSWER_RA) {
			ReplyMediaRecordActivity.goToReAnswer(this, mIAskListPresenter.askIdAnswer);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mIAskListPresenter.registReceiverRefresh();
	}
	@Override
	public void onPause() {
		super.onPause();
		if(mIAskListPresenter!=null){
			mIAskListPresenter.stopAll();
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mIAskListPresenter.unregistReceiverRefresh();
	}

	private boolean isSelfQa = false;

	private void fillData(AskDetail data) {
		mAskDetail = data;
		UserInfo info = UserInfo.getInstance();
		if (info.isLogin()) {
			if (info.getUserId().equals(data.getAuserId())) {
				isSelfQa = true;
			}
		}
//		data.setContent(data.getContent()+"「sdsaf(600000)」");
		data.setContentSpanStr(mIAskListPresenter.handleAskStr(data.getContent()));//处理股票
		List<AskDetailAnswer> answers = data.getAnswers();
		if (answers.size() <= 0) {
			empty.setVisibility(View.VISIBLE);
			mList.setVisibility(View.GONE);
			emptyTv.setText("暂没有投顾回答该问题");
			TextView name = (TextView) findViewById(R.id.ask_item_tv_name);
			TextView time = (TextView) findViewById(R.id.ask_item_tv_time);
			TextView qa = (TextView) findViewById(R.id.ask_item_tv_qa);
			TextView anCount = (TextView) findViewById(R.id.ask_item_tv_answercount);
			name.setText(data.getAusername());
			time.setText(getTime(data.getCtime()));
			qa.setMovementMethod(LinkMovementMethod.getInstance());
			qa.setText(data.getContentSpanStr());
			anCount.setText("0人回答");
		} else {
			empty.setVisibility(View.GONE);
			mList.setVisibility(View.VISIBLE);
			if (headerView == null) {
				headerView = LayoutInflater.from(this).inflate(R.layout.item_ask_detail_qa, null);
				mList.addHeaderView(headerView);
			} else {
				// mList.removeHeaderView(headerView);
			}
			for(AskDetailAnswer answer:answers){
//				answer.setContent(answer.getContent()+"「sdsaf(600000)」");
				answer.setContentSpanStr(mIAskListPresenter.handleAskStr(answer.getContent()));
//				answer.setEvaContentSpanStr(mIAskListPresenter.handleAskStr("评价："+answer.getEvaContent()));
				AgainAskVo aAsk = answer.getAgainAskVo();
				if(aAsk!=null){
					aAsk.setContentSpanStr(mIAskListPresenter.handleAskStr(data.getAusername()+"追问："+(aAsk.getContent()!=null?aAsk.getContent():"")));
					if (aAsk.getHasAgainanswer() > 0) {
						aAsk.setAgainanswerContentSpanStr(mIAskListPresenter.handleAskStr(answer.getAdviserUser().getUserName()+"回答："+(aAsk.getAgainanswerContent()!=null?aAsk.getAgainanswerContent():"")));
					}
				}
			}
			TextView name = (TextView) headerView.findViewById(R.id.ask_item_tv_name);
			TextView time = (TextView) headerView.findViewById(R.id.ask_item_tv_time);
			TextView qa = (TextView) headerView.findViewById(R.id.ask_item_tv_qa);
			TextView anCount = (TextView) headerView.findViewById(R.id.ask_item_tv_answercount);
			name.setText(data.getAusername());
			time.setText(getTime(data.getCtime()));
			qa.setMovementMethod(LinkMovementMethod.getInstance());
			qa.setText(data.getContentSpanStr());
			anCount.setText(answers.size() + "人回答");
			mMyAdapter = new MyAdapter(this, answers);
			mList.setAdapter(mMyAdapter);
		}
		answerType = mIAskListPresenter.isAskDetailCanAnswer(data);
		if (answerType == IAskListPresenter.ASK_ANSWER_A || answerType == IAskListPresenter.ASK_ANSWER_RA) {
			mAnswer.setVisibility(View.VISIBLE);
		} else {
			mAnswer.setVisibility(View.GONE);
		}
	}

	private void setQaContent(TextView qaTv, String name, SpannableString content) {
//		if (StringUtils.isBlank(name)) {
//			return;
//		}
//		SpannableString sp = new SpannableString(name + content);
		ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.font_4c86c6));
		content.setSpan(span, 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		qaTv.setMovementMethod(LinkMovementMethod.getInstance());
		qaTv.setText(content);
	}
	private void setEvaContent(TextView evaTv, String content) {
		SpannableString sp = new SpannableString("评价：" + content);
		ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.font_b2b2b2));
		sp.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		evaTv.setText(sp);
	}

	private class MyAdapter extends MyBaseAdapter<AskDetailAnswer> {

		public MyAdapter(Context context, List<AskDetailAnswer> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = ViewHolder.getInstance(context, convertView, parent, R.layout.item_ask_detail_answer);
			if (convertView == null) {
				convertView = holder.getView();
				convertView.setTag(holder);
			}
			final AskDetailAnswer bean = (AskDetailAnswer) getItem(position);
			final TouguUserBean tougu = bean.getAdviserUser();

			ImageView head = holder.getView(R.id.headpic);
			TextView name = holder.getView(R.id.ask_item_tv_name);
			TextView time = holder.getView(R.id.ask_item_tv_time);
			TextView answer = holder.getView(R.id.ask_item_tv_answer);
			TextView qa = holder.getView(R.id.ask_item_tv_qa);
			TextView qaAnswer = holder.getView(R.id.ask_item_tv_qa_an);
			View answerLy = holder.getView(R.id.ask_item_ly_again);
			View ratingLy = holder.getView(R.id.ask_item_ly_rate);
			RatingBar ratingBar = holder.getView(R.id.ask_item_rating);
			TextView evaContent = holder.getView(R.id.ask_item_tv_rate_content);
			TextView askBtn = holder.getView(R.id.ask_item_tv_askagain);
			TextView commentBtn = holder.getView(R.id.ask_item_tv_comment);
			View answerV = holder.getView(R.id.ask_item_answer_voice);
			View qaAnswerV = holder.getView(R.id.ask_item_qa_an_voice);
			View btnLy = holder.getView(R.id.ask_item_ly_btn);
			View vImg = holder.getView(R.id.imageView2);
			View vWeb = holder.getView(R.id.ask_item_ly_web);
			View vWebTv = holder.getView(R.id.ask_item_tv_web);
			TextView company = holder.getView(R.id.ask_item_tv_company);

			OnClickListener listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.ask_item_tv_askagain:
						if (tougu != null && bean != null) {
							ReplyMediaRecordActivity.goToReAsk(AskDetailActivity.this, mAskDetail.getAskId(), bean.getAnswerId(), tougu.getUserId());
						}
						break;
					case R.id.ask_item_tv_comment:
						if (bean != null) {
							ReplyActivity.goToEvaluate(AskDetailActivity.this, bean.getAnswerId());
						}
						break;
					case R.id.headpic:
						if (tougu != null) {
							ActivityChange.ToAdviserHome(context,  tougu.getUserName(),tougu.getUserId());
						}
						break;
					case R.id.ask_item_tv_web:
					{
						Intent intent = new Intent(AskDetailActivity.this, AskDetailWebActivity.class);
						intent.putExtra(AskDetailWebActivity.BUNDLE_ID, mAskDetail.getAskId());
						startActivity(intent);
					}
						break;
					}
				}
			};

			head.setOnClickListener(listener);
			askBtn.setOnClickListener(listener);
			commentBtn.setOnClickListener(listener);
			vWebTv.setOnClickListener(listener);
			commentBtn.setEnabled(false);
			askBtn.setEnabled(false);
			time.setText(getTime(bean.getCtime()));
			if (!StringUtils.isEmpty(bean.getVoiceAmr())) {
				answer.setVisibility(View.GONE);
				answerV.setVisibility(View.VISIBLE);
				setVoiceLayout(answerV, bean.getVoiceAmr(), bean.getVoicelength());
			} else {
				answer.setVisibility(View.VISIBLE);
				answerV.setVisibility(View.GONE);
				answer.setMovementMethod(LinkMovementMethod.getInstance());
				answer.setText(bean.getContentSpanStr());
			}
			if(bean.isIncludeImage()){
				vWeb.setVisibility(View.VISIBLE);
			}else{
				vWeb.setVisibility(View.GONE);
			}
			vImg.setVisibility(View.GONE);
			if (tougu != null) {
				name.setText(tougu.getUserName());
				vImg.setVisibility(View.VISIBLE);
				company.setText(tougu.getTypeDesc() + " " + (StringUtils.isEmpty(tougu.getCompany())?"":tougu.getCompany()));
				mImageLoader.downLoadImage(tougu.getHeadImage(), head);
				AgainAskVo aAsk = bean.getAgainAskVo();
				if (aAsk != null) {

//					if (isSelfQa || (!StringUtils.isBlank(tougu.getUserId()) && tougu.getUserId().equals(UserInfo.getInstance().getUserId()))) {
						answerLy.setVisibility(View.VISIBLE);
						setQaContent(qa, mAskDetail.getAusername(), aAsk.getContentSpanStr());
						if (aAsk.getHasAgainanswer() > 0) {
							if (!StringUtils.isEmpty(aAsk.getAgainanswerVoiceAmr())) {
//								qaAnswer.setVisibility(View.GONE);
								qaAnswerV.setVisibility(View.VISIBLE);
								setVoiceLayout(qaAnswerV,aAsk.getAgainanswerVoiceAmr(), aAsk.getAgainanswerVoicelength());
							} else {
								qaAnswerV.setVisibility(View.GONE);
								answer.setText(bean.getContent());
							}
							qaAnswer.setVisibility(View.VISIBLE);
//							String tempS = aAsk.getAgainanswerContent();
//							if(tempS==null){
//								tempS="";
//							}
							setQaContent(qaAnswer, tougu.getUserName(),  aAsk.getAgainanswerContentSpanStr());
						} else {
							qaAnswer.setVisibility(View.GONE);
						}
//					} else {
//						answerLy.setVisibility(View.GONE);
//					}
				} else {
					if (isSelfQa && !bean.isHaveEvaluate()) {
						askBtn.setEnabled(true);
					}
					answerLy.setVisibility(View.GONE);
				}
			}
			if (bean.getEvaluate() > 0) {
				ratingLy.setVisibility(View.VISIBLE);
				ratingBar.setProgress(bean.getEvaluate());
				if (StringUtils.isBlank(bean.getEvaContent())) {
					evaContent.setVisibility(View.GONE);
				} else {
					evaContent.setVisibility(View.VISIBLE);
//					evaContent.setText(bean.getEvaContent());
					setEvaContent(evaContent,bean.getEvaContent());
				}
			} else {
				if (isSelfQa) {
					commentBtn.setEnabled(true);
				}
				ratingLy.setVisibility(View.GONE);
			}

			if (commentBtn.isEnabled() || askBtn.isEnabled()) {
				btnLy.setVisibility(View.VISIBLE);
			}
			return convertView;
		}

	}

	private String getTime(long time) {
		return DateUtils.getTimeAgoString(time, "MM-dd HH:mm");
	}

	private void setVoiceLayout(View v, final String url, int time) {
		ViewHolder holder = ViewHolder.getInstance(v);
		View answerMediaLy = holder.getView(R.id.ask_item_media_ly);
		View answerMediaSpace = holder.getView(R.id.ask_item_media_space);
		TextView mediaTime = holder.getView(R.id.ask_item_media_time);
		LayoutParams params = (LayoutParams) answerMediaLy.getLayoutParams();
		LayoutParams params1 = (LayoutParams) answerMediaSpace.getLayoutParams();
		params.weight = time;
		params1.weight = maxTime - time;
		answerMediaLy.setLayoutParams(params);
		answerMediaSpace.setLayoutParams(params1);
		mediaTime.setText(getString(R.string.timer_format, time));
		final ImageView mediaVUMeter = holder.getView(R.id.ask_item_media_vum_right);
		final AnimationDrawable anim = (AnimationDrawable) mediaVUMeter.getBackground();

		ProgressView mProgressView = (ProgressView) mediaVUMeter.getTag();
		if (mProgressView == null) {
			mProgressView = new com.gome.haoyuangong.views.ProgressView(getContext(), mediaVUMeter);
			mediaVUMeter.setTag(mProgressView);
		}
		if (mIAskListPresenter.isPlaying(url)) {
			anim.start();
		} else {
			anim.stop();
			anim.selectDrawable(0);
		}
		final ProgressView PV = mProgressView;
		answerMediaLy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mIAskListPresenter.mediaPlayClick(url, anim, PV);
			}
		});

	}

	public static void gotoAskDetailAutoAnswer(Context ctx, int askId) {
		Intent intent = new Intent(ctx, AskDetailActivity.class);
		intent.putExtra(BUNDLE_Id, askId);
		intent.putExtra(BUNDLE_AUTO_ANSWER, true);
		ctx.startActivity(intent);
	}
}
