package com.gome.haoyuangong.net.result.ask;

import java.util.ArrayList;
import java.util.List;

import android.text.SpannableString;

import com.gome.haoyuangong.net.result.BaseResultWeb;
import com.gome.haoyuangong.net.result.tougu.TouguUserBean;

public class AskItemAnswerResult extends BaseResultWeb{
	private DataList data=new DataList();
	
	public DataList getData() {
		return data;
	}

	public void setData(DataList data) {
		this.data = data;
	}
	public class DataList{
		private List<AskItemAnswer> list=new ArrayList<AskItemAnswer>();

		public List<AskItemAnswer> getList() {
			return list;
		}

		public void setList(List<AskItemAnswer> list) {
			this.list = list;
		}
		
	}

	static public class AskItemAnswer{
		private int askId;
		private String auserId;//
		private String ausername;//
		private String content;//
		private long ctime;//
		private String status;// 
		private int answerTimes;
		private LastedAnswer lastedAnswer;//
		transient private SpannableString contentSpanStr;
		
		public SpannableString getContentSpanStr() {
			return contentSpanStr;
		}
		public void setContentSpanStr(SpannableString contentSpanStr) {
			this.contentSpanStr = contentSpanStr;
		}
		public int getAskId() {
			return askId;
		}
		public void setAskId(int askId) {
			this.askId = askId;
		}
		public int getAnswerTimes() {
			return answerTimes;
		}
		public void setAnswerTimes(int answerTimes) {
			this.answerTimes = answerTimes;
		}
		public int getAsk_id() {
			return askId;
		}
		public void setAsk_id(int ask_id) {
			this.askId = ask_id;
		}
		public String getAuserId() {
			return auserId;
		}
		public void setAuserId(String auserId) {
			this.auserId = auserId;
		}
		public String getAusername() {
			return ausername;
		}
		public void setAusername(String ausername) {
			this.ausername = ausername;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public long getCtime() {
			return ctime;
		}
		public void setCtime(long ctime) {
			this.ctime = ctime;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public LastedAnswer getLastedAnswer() {
			return lastedAnswer;
		}
		public void setLastedAnswer(LastedAnswer lastedAnswer) {
			this.lastedAnswer = lastedAnswer;
		}
		
		
	}
	static public class LastedAnswer{
		private int answerId;
		private String content;
		private int evaluate;
		private String voiceAmr;
		private int voicelength;
		private TouguUserBean adviserUser;//
		transient private SpannableString contentSpanStr;
		
		public SpannableString getContentSpanStr() {
			return contentSpanStr;
		}
		public void setContentSpanStr(SpannableString contentSpanStr) {
			this.contentSpanStr = contentSpanStr;
		}
		public String getVoiceAmr() {
			return voiceAmr;
		}
		public void setVoiceAmr(String voiceAmr) {
			this.voiceAmr = voiceAmr;
		}
		public int getVoicelength() {
			return voicelength;
		}
		public void setVoicelength(int voicelength) {
			this.voicelength = voicelength;
		}
		public TouguUserBean getAdviserUser() {
			return adviserUser;
		}
		public void setAdviserUser(TouguUserBean adviserUser) {
			this.adviserUser = adviserUser;
		}
		public int getAnswerId() {
			return answerId;
		}
		public void setAnswerId(int answerId) {
			this.answerId = answerId;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public int getEvaluate() {
			return evaluate;
		}
		public void setEvaluate(int evaluate) {
			this.evaluate = evaluate;
		}
		
	}
}
