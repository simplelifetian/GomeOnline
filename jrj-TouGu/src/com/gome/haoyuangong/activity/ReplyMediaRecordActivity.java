package com.gome.haoyuangong.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.mediarecorder.SoundRecorder;
import com.gome.haoyuangong.mediarecorder.SoundRecorder.OnRecorderListener;
import com.gome.haoyuangong.net.result.BaseResultWeb;
import com.gome.haoyuangong.presenter.IAskListPresenter;
import com.gome.haoyuangong.presenter.IFileDownloadPresenter;
import com.gome.haoyuangong.presenter.IFileUploadPresenter;
import com.gome.haoyuangong.views.CusImage;
import com.gome.haoyuangong.views.VUMeterView;
import com.google.gson.Gson;
import com.gome.haoyuangong.R;

public class ReplyMediaRecordActivity extends ReplyActivity implements OnRecorderListener {
	private static final int STATE_IDLE = 0;
	private static final int STATE_RECORDING = 1;
	private static final int STATE_FINISH = 2;
	private static final int STATE_PLAYING = 3;
	private static final int STATE_PRE_RECORDING = 4;

	private static final int VUM_MAX_NUM = 7;
	private int state;

	VUMeterView mVUMeterViewL;
	VUMeterView mVUMeterViewR;
	CusImage mCusImage;
	private TextView mTvTime;
	private View mVAction;
	private TextView mTvCancel;
	private TextView mTvShow;
	private TextView mMediaTip;
	private SoundRecorder mSoundRecorder;
	private int maxTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSoundRecorder = new SoundRecorder(this, savedInstanceState, getIntent());
		mSoundRecorder.setmOnRecorderListener(this);
		maxTime = (int) (SoundRecorder.MAX_SOUND_SIZE / 1000);
		initView();
		init();
	}

	private void initView() {
		mCusImage = (CusImage) findViewById(R.id.record_btn);
//		mVUMeterViewL = (VUMeterView) findViewById(R.id.record_vum_left);
//		mVUMeterViewR = (VUMeterView) findViewById(R.id.record_vum_right);
		mTvTime = (TextView) findViewById(R.id.record_time);
		mTvCancel = (TextView) findViewById(R.id.record_tv_cancel);
		mTvShow = (TextView) findViewById(R.id.record_tv_send);
		mMediaTip = (TextView) findViewById(R.id.record_tip);
		mVAction = findViewById(R.id.record_ly_action);
	}

	void init() {
		setState(STATE_IDLE);
		mVUMeterViewL.setOrientation(false);
		mSoundRecorder.setVUMMax(VUM_MAX_NUM);
		mCusImage.setOnClickListener(mListener);
		mTvShow.setOnClickListener(mListener);
		mTvCancel.setOnClickListener(mListener);
		mCusImage.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Logger.info("", "开始录音");
				clickMain();
				return true;
			}
		});
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		default:
			if (state == STATE_PRE_RECORDING || state == STATE_RECORDING) {
				Logger.info("", "停止录音");
				mSoundRecorder.stopRecord();
				return true;
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	private OnClickListener mListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.record_btn:
				if (state != STATE_IDLE) {
					clickMain();
				}
				break;
			case R.id.record_tv_cancel:
				clickCancel();
				break;
			case R.id.record_tv_send:
				clickSend();
				break;
			}
		}
	};

	private void clickMain() {
		if (state == STATE_IDLE) {
			mSoundRecorder.startRecording();
			setState(STATE_PRE_RECORDING);
		} else if (state == STATE_RECORDING) {
			mSoundRecorder.stopRecord();
		} else if (state == STATE_FINISH) {
			mSoundRecorder.playRecording();
		} else if (state == STATE_PLAYING) {
			mSoundRecorder.stopPlaying();
		}
	}

	private void clickCancel() {
		mSoundRecorder.stopRecord();
		mSoundRecorder.stopPlaying();
		mSoundRecorder.deleteRecord();
	}

	private void clickSend() {
		doAnswerMedia(mSoundRecorder.getRecordFilePath(), mSoundRecorder.getTimeLength());
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSoundRecorder.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSoundRecorder.onPause();
	}

	public void setState(int state) {
		this.state = state;
		switch (state) {
		case STATE_IDLE:
			mTvTime.setText("");
			mVUMeterViewL.setVisibility(View.GONE);
			mVUMeterViewR.setVisibility(View.GONE);
			mVAction.setVisibility(View.GONE);
			mCusImage.setupprogress(0);
			mCusImage.setPressed(false);
			mCusImage.setBackgroundResource(R.drawable.record_start_button);
			mMediaTip.setVisibility(View.VISIBLE);
			mMediaTip.setText("按住说话");
			break;
		case STATE_PLAYING:
			mCusImage.setupprogress(0);
			mCusImage.setBackgroundResource(R.drawable.recording_bg);
			mMediaTip.setVisibility(View.INVISIBLE);
			break;
		case STATE_FINISH:
			mTvTime.setText(getString(R.string.timer_format, mSoundRecorder.getTimeLength()));
			mVUMeterViewL.setVisibility(View.GONE);
			mVUMeterViewR.setVisibility(View.GONE);
			mVAction.setVisibility(View.VISIBLE);
			mMediaTip.setVisibility(View.VISIBLE);
			mMediaTip.setText("点击试听");
			mCusImage.setupprogress(0);
			mCusImage.setBackgroundResource(R.drawable.record_play);
			break;
		case STATE_RECORDING:
			mVUMeterViewL.setVisibility(View.VISIBLE);
			mVUMeterViewR.setVisibility(View.VISIBLE);
			mVAction.setVisibility(View.GONE);
			mCusImage.setupprogress(0);
			// mCusImage.setBackgroundResource(R.drawable.recording_bg);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSoundRecorder.onDestroy();
	}

	@Override
	public void onStartRecording() {
		setState(STATE_RECORDING);

	}

	@Override
	public void onUpdateVUMetur(int cur, int max) {
		mVUMeterViewL.setCur(cur);
		mVUMeterViewR.setCur(cur);
	}

	@Override
	public void onFinish() {
		long sLeng = mSoundRecorder.getTimeLengthMiss();
		if(sLeng>0&&sLeng<1000){
			Toast toast = getToast("按住时间太短");
			toast.setGravity(Gravity.CENTER, 0, 100);
			toast.show();
			clickCancel();
			return;
		}
		setState(STATE_FINISH);
	}

	@Override
	public void onPlaying() {
		setState(STATE_PLAYING);

	}

	@Override
	public void onPlayProgress(int progress) {
		mCusImage.setupprogress(progress);
	}

	@Override
	public void onError(String error) {
		showToast(error);
	}

	@Override
	public void onUpdateTime(int second) {
		if (state == STATE_RECORDING) {
			mTvTime.setText(getString(R.string.timer_format_record, second, maxTime));
		} else {
			mTvTime.setText(getString(R.string.timer_format, second));
		}
	}

	@Override
	public void onIdle() {
		setState(STATE_IDLE);
	}

	IFileUploadPresenter mIFileUploadPresenterr = new IFileUploadPresenter(this) {
		public void onSuccessed(String jsonData) {
			BaseResultWeb result = null;
			try {
				Gson gson = new Gson();
				result = gson.fromJson(jsonData, BaseResultWeb.class);
			} catch (Exception e) {
			}
			if (result != null && result.getRetCode() == 0) {
				showToast("回答成功,可以去您的的回答记录中查看全部回答内容");
				Intent intent = new Intent(IAskListPresenter.ACTION_ASK_REFRESH);
				intent.putExtra(IAskListPresenter.BUNDLE_ASKID, askId);
				intent.putExtra(IAskListPresenter.BUNDLE_STATUS, IAskListPresenter.ANSWER_STATUS_ANSWERED);
				sendBroadcast(intent);
				finish();
			}
		};
	};

	public void doAnswerMedia(String file, int length) {
		int type = getIntent().getIntExtra(BUNDLE_TYPE, -1);
		if (type == TYPE_ANSWER) {
			int askId = getIntent().getIntExtra(BUNDLE_PARAM_APPITEMID, -1);
			this.askId = askId;
			mIFileUploadPresenterr.uploadAnswerMedia(askId, file, length);
		}else{
			int reAskId = getIntent().getIntExtra(BUNDLE_PARAM_ANSWERTOUSERID, -1);
			this.askId = -1;
			mIFileUploadPresenterr.uploadReAnswerMedia(reAskId, file, length);
		}
	}

	public static void goToAnswerReply(Context ctx, int askId) {
		Intent i = new Intent(ctx, ReplyMediaRecordActivity.class);
		i.putExtra(BUNDLE_TYPE, TYPE_ANSWER);
		i.putExtra(BUNDLE_PARAM_APPITEMID, askId);
		ctx.startActivity(i);
	}

	public static void goToReAsk(Context ctx, int askId, int answerId, String answerUserId) {
		Intent i = new Intent(ctx, ReplyMediaRecordActivity.class);
		i.putExtra(BUNDLE_TYPE, TYPE_REASK);
		i.putExtra(BUNDLE_PARAM_APPITEMID, askId);
		i.putExtra(BUNDLE_PARAM_ANSWERID, answerId);
		i.putExtra(BUNDLE_PARAM_ASKTOUSERID, answerUserId);
		ctx.startActivity(i);
	}

	public static void goToReAnswer(Context ctx, int againAskId) {
		Intent i = new Intent(ctx, ReplyMediaRecordActivity.class);
		i.putExtra(BUNDLE_TYPE, TYPE_AGAINANSWER);
		i.putExtra(BUNDLE_PARAM_ANSWERTOUSERID, againAskId);
		ctx.startActivity(i);
	}
}
