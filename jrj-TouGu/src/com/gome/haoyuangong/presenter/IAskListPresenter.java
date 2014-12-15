package com.gome.haoyuangong.presenter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.gome.haoyuangong.BaseViewImpl;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.mediarecorder.SoundPlayer;
import com.gome.haoyuangong.mediarecorder.SoundPlayer.OnSoundPlayListner;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.ask.AskDetailResult;
import com.gome.haoyuangong.net.result.ask.AskItemAnswerResult;
import com.gome.haoyuangong.net.result.ask.AskItemAskResult;
import com.gome.haoyuangong.net.result.ask.AskDetailResult.AskDetail;
import com.gome.haoyuangong.net.result.ask.AskDetailResult.AskDetailAnswer;
import com.gome.haoyuangong.net.result.ask.AskItemAnswerResult.AskItemAnswer;
import com.gome.haoyuangong.net.result.ask.AskItemAskResult.AskItemAsk;
import com.gome.haoyuangong.net.url.AskUrl;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.FileUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.ProgressView;

public class IAskListPresenter extends IBasePresenter {
	public static final String ACTION_ASK_REFRESH = "ask_refresh";
	static public int pageNums = 10;

	public IAskListPresenter(BaseViewImpl vImpl) {
		super(vImpl);
		mAskBroadcastReceiver = new AskBroadcastReceiver();
	}

	public void requestAskDetail(final boolean isShowLoading, int id) {
		String url = AskUrl.ASK_DETAIL + id;
		JsonRequest<AskDetailResult> request = new JsonRequest<AskDetailResult>(Method.GET, url, new RequestHandlerListener<AskDetailResult>(getContext()) {

			@Override
			public void onSuccess(String id, AskDetailResult data) {
				onDetail(data.getData());
			}

			@Override
			public void onStart(Request request) {
				super.onStart(request);
				if (isShowLoading) {
					showLoading(request);
				}
			}

			@Override
			public void onEnd(Request request) {
				super.onEnd(request);
				if (isShowLoading) {
					hideLoading(request);
				}
			}
		}, AskDetailResult.class);
		send(request);
	}

	public Request getLastAnswerRequest(int ids) {

		String url = String.format(AskUrl.LAST_ANSWER, pageNums, "f");
		if (ids > 0) {
			url = url + "&aid=" + ids;
		}
		JsonRequest<AskItemAnswerResult> request = new JsonRequest<AskItemAnswerResult>(Method.GET, url, null, AskItemAnswerResult.class);
		return request;
	}

	public Request getNewAskRequest(int ids) {
		UserInfo info = UserInfo.getInstance();
		String url = String.format(AskUrl.NEW_ASK, pageNums, "f");
		if (ids > 0) {
			url = url + "&aid=" + ids;
		}
		if (info.isLogin() && info.isTougu()) {
			url = url + "&" + "uid=" + info.getUserId();
		}
		JsonRequest<AskItemAskResult> request = new JsonRequest<AskItemAskResult>(Method.GET, url, null, AskItemAskResult.class);
		return request;
	}

	public String findVoiceFromSdcard(String url) {
		String path = FileUtils.getVoiceSdcardPath(url);
		if (!StringUtils.isEmpty(path)) {
			File f = new File(path);
			if (f.exists()) {
				return path;
			}
		}
		return null;
	}

	public void initVoice() {
		mSoundPlayer = new SoundPlayer();
		mSoundPlayer.setListener(new OnSoundPlayListner() {
			@Override
			public void onFinish() {
				urlM = null;
				onPalyFinish();
			}

			@Override
			public void onError() {
				urlM = null;
				onPalyFinish();
			}

			@Override
			public void onPaly() {
				onPalyFinish();
			}
		});
	}

	private SoundPlayer mSoundPlayer;
	private String urlM;
	private Map<String, ProgressView> mDownloadViews = new HashMap<String, ProgressView>();
	private IFileDownloadPresenter mIFileDownloadPresenter = new IFileDownloadPresenter(mVImpl) {
		public void onSuccessed(String url, String path) {
			if (StringUtils.isEmpty(urlM)) {
				urlM = url;
				mSoundPlayer.startPlay(path);
			}
		};

		public void onStart(String url) {
			ProgressView v = mDownloadViews.get(url);
			if (v != null) {
				v.show();
			}
		};

		public void onEnd(String url) {
			ProgressView v = mDownloadViews.get(url);
			if (v != null) {
				v.hide();
				mDownloadViews.remove(url);
			}
		};
	};

	public void mediaPlayClick(String url, final AnimationDrawable anim, ProgressView v) {
		String path = findVoiceFromSdcard(url);
		if (!StringUtils.isEmpty(urlM)) {
			if (!this.urlM.equals(url)) {
				mSoundPlayer.stopPlay();
			} else {
				return;
			}
		}
		if (!StringUtils.isEmpty(path)) {
			this.urlM = url;
			anim.start();
			mSoundPlayer.startPlay(path);
		} else {
			mDownloadViews.put(url, v);
			mIFileDownloadPresenter.downloadVoice(url);
		}
	}

	public void stopAll() {
		mSoundPlayer.stopPlay();
	}

	public boolean isPlaying(String url) {
		return (!StringUtils.isEmpty(urlM) && urlM.equals(url));
	}

	public void registReceiverRefresh() {
		getContext().registerReceiver(mAskBroadcastReceiver, new IntentFilter(ACTION_ASK_REFRESH));
	}

	public void unregistReceiverRefresh() {
		if (mAskBroadcastReceiver != null) {
			getContext().unregisterReceiver(mAskBroadcastReceiver);
		}
	}

	private AskBroadcastReceiver mAskBroadcastReceiver;

	private class AskBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (ACTION_ASK_REFRESH.equals(intent.getAction())) {
				int status = intent.getIntExtra(BUNDLE_STATUS, ANSWER_STATUS_NOTANSWER);
				int askid = intent.getIntExtra(BUNDLE_ASKID, 0);
				onRefresh(askid, status);
			}
		}
	}

	public static final int ASK_ANSWER_NO = 0;// 不能回答
	public static final int ASK_ANSWER_A = 1;// 可以回答(第一次)
	public static final int ASK_ANSWER_RA = 2;// 可以回答追问
	public int askIdAnswer;

	public int isAskDetailCanAnswer(AskDetail detail) {
		UserInfo info = UserInfo.getInstance();
		int answerType = ASK_ANSWER_A;
		if (info.isTougu()) {
			if (!detail.getAuserId().equals(info.getUserId())) {
				for (AskDetailAnswer answer : detail.getAnswers()) {
					if (answer.getAdviserUser() != null) {
						if (answer.getAdviserUser() != null && answer.getAdviserUser().getUserId().equals(info.getUserId())) {
							if (answer.isHaveEvaluate()) {
								answerType = ASK_ANSWER_NO;
								break;
							}
							if (answer.getAgainAskVo() == null) {
								answerType = ASK_ANSWER_NO;
							} else {
								if (answer.getAgainAskVo().getHasAgainanswer() > 0) {
									answerType = ASK_ANSWER_NO;
								} else {

									answerType = ASK_ANSWER_RA;
									askIdAnswer = answer.getAgainAskVo().getId();
								}
							}
							break;
						}
					}
				}
			} else {
				answerType = ASK_ANSWER_NO;
			}
		} else {
			answerType = ASK_ANSWER_NO;
		}
		return answerType;
	}

	public static final String BUNDLE_STATUS = "answer_status";
	public static final String BUNDLE_ASKID = "ask_id";
	public static final String BUNDLE_ANSWERID = "answer_id";
	public static final String BUNDLE_EVALUATE_RATING = "evaluate_rating";
	public static final String BUNDLE_EVALUATE_CONTENT = "evaluate_content";
	public static final int ANSWER_STATUS_NOTANSWER = 1;
	public static final int ANSWER_STATUS_ANSWERED = 2;
	public static final int ANSWER_STATU_REASK = 3;
	public static final int ANSWER_STATU_REANSWERED = 4;
	public static final int ANSWER_STATU_EVALUATED = 5;

	public void onRefresh(int askId, int answerStatus) {

	}

	public void onPalyFinish() {

	}

	public void onDetail(AskDetail data) {

	}

	private String regexstrImg = "<img[^>]*>";
	private String regexstrEceptStock = "<(?!a href).*?>";
	private String regexstrAll = "<[^>]*>";
	private String regexstrStock = "\\$<a href=\"(.+,(\\d{6})\\..+)\">(.+)\\$";
	private String[] specialStrsFrom = new String[] { "&nbsp", "&amp", "&lt", "&gt", "&quot", "&qpos" };
	private String[] specialStrsTo = new String[] { " ", "&", "<", ">", "\"", "\'" };
	private String regexstrStockClient = "「(.+\\((.+)\\))」";
	private SpanClickListener listener;

	public SpannableString handleAskStr(String content) {
		if (StringUtils.isBlank(content)) {
			return new SpannableString("");
		}else{
			return new SpannableString(content.replaceAll("「|」", ""));
		}
//		Pattern pattern = Pattern.compile(regexstrStockClient);
//		Matcher matcher = pattern.matcher(content);
//		String stockcode = "";
//		String stockC = "";
//		StringBuffer buffer = new StringBuffer();
//		List<StockSpanBean> stocks = new ArrayList<IAskListPresenter.StockSpanBean>();
//		while (matcher.find()) {
//			stockC = matcher.group(1);
//			stockcode = matcher.group(2);
//			matcher.appendReplacement(buffer, stockC);
//			StockSpanBean bean = new StockSpanBean();
//			bean.stockcode = stockcode;
//			bean.index = buffer.lastIndexOf(stockC);
//			bean.count = stockC.length();
//			stocks.add(bean);
//		}
//		matcher.appendTail(buffer);
//		return getSpannableStr(buffer.toString(), stocks);
	}
	private Set<String> stockInstertSet=new LinkedHashSet<String>();
	public String handleInsertStock(String str){
		return str;
//		if(StringUtils.isEmpty(str))return str;
//		String regStr="";
//		for(String s : stockInstertSet){
//			regStr=s.replaceAll("\\(", "\\\\(");
//			regStr=regStr.replaceAll("\\)", "\\\\)");
//			str = str.replaceAll(regStr, "「"+s+"」");
//		}
//		return str;
	}
	public void addInsertStock(String str){
//		stockInstertSet.add(str);
	}

	public SpannableString handleAskStr1(String content) {
		// content =
		// "<p>$<a href=\"http://stock.jrj.com.cn/share,000001.shtml\" class=\"link\" target=\"_blank\">平安银行(000001)</a>$ &nbsp;买吗？</p>";
		if (StringUtils.isBlank(content)) {
			return new SpannableString(content);
		}
		Pattern pattern = Pattern.compile(regexstrImg);
		Matcher matcher = pattern.matcher(content);
		content = matcher.replaceAll("[图片]");
		pattern = Pattern.compile(regexstrEceptStock);
		matcher = pattern.matcher(content);
		content = matcher.replaceAll("");
		// content.replaceAll(specialStrsFrom, replacement)
		pattern = Pattern.compile(regexstrStock);
		matcher = pattern.matcher(content);
		String stockcode = "";
		String stockC = "";
		StringBuffer buffer = new StringBuffer();
		List<StockSpanBean> stocks = new ArrayList<IAskListPresenter.StockSpanBean>();
		while (matcher.find()) {
			stockcode = matcher.group(2);
			stockC = matcher.group(3);
			matcher.appendReplacement(buffer, stockC);
			StockSpanBean bean = new StockSpanBean();
			bean.stockcode = stockcode;
			bean.index = buffer.lastIndexOf(stockC);
			bean.count = stockC.length();
			stocks.add(bean);
		}
		matcher.appendTail(buffer);
		return getSpannableStr(buffer.toString(), stocks);
	}

	private SpannableString getSpannableStr(String content, List<StockSpanBean> stocks) {
		SpannableString sp = new SpannableString(content);
		for (StockSpanBean bean : stocks) {
			MyURLSpan ms = new MyURLSpan(bean.stockcode);
			sp.setSpan(ms, bean.index, bean.index + bean.count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return sp;
	}

	public class StockSpanBean {
		String stockcode;
		int index;
		int count;
	}

	public class MyURLSpan extends ClickableSpan {
		private String mUrl;

		MyURLSpan(String url) {
			mUrl = url;
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.bgColor = 0;
			ds.setColor(0xffde3031);
			ds.setUnderlineText(false); // 去掉下划线
		}

		@Override
		public void onClick(View widget) {
			// TODO Auto-generated method stub
			if (listener != null) {
				listener.onClick(mUrl);
			}
			// System.out.println(mUrl);
			// Selection.setSelection((Spannable)MultiClickTextView.this.getText(),
			// 0);
		}
	}

	public interface SpanClickListener {
		/**
		 * 响应点击事件
		 * 
		 * @param indexSpan
		 *          span编号 从0开始
		 */
		public void onClick(String indexSpan);
	}

}
