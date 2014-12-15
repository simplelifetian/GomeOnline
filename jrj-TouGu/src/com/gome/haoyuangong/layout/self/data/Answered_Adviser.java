package com.gome.haoyuangong.layout.self.data;

import java.util.ArrayList;

import android.R.integer;

import com.gome.haoyuangong.net.result.TouguBaseResult;

public class Answered_Adviser extends TouguBaseResult {
	
	private Data data = new Data();
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data{
		private ArrayList<AnsweredItem> list = new ArrayList<Answered_Adviser.AnsweredItem>();

		public ArrayList<AnsweredItem> getList() {
			return list;
		}

		public void setList(ArrayList<AnsweredItem> list) {
			this.list = list;
		}
		
	}
	/**
	 * 投顾回答
	 * @author menghui
	 *
	 */
	public class AnsweredItem{
		int askId;
		String auserId;
		String ausername;
		String content;
		long ctime;
		int askUserType;
		int answerTimes;
		String headImages;
		LastAnsweredItem lastedAnswer;

		public int getAskUserType() {
			return askUserType;
		}
		public void setAskUserType(int askUserType) {
			this.askUserType = askUserType;
		}
		public String getHeadImages() {
			return headImages;
		}
		public void setHeadImages(String headImages) {
			this.headImages = headImages;
		}
		public int getAnswerTimes() {
			return answerTimes;
		}
		public void setAnswerTimes(int answerTimes) {
			this.answerTimes = answerTimes;
		}
		public int getAskId() {
			return askId;
		}
		public void setAskId(int askId) {
			this.askId = askId;
		}
		public String getAuserId() {
			return auserId;
		}
		public void setAuserId(String auserId) {
			this.auserId = auserId;
		}
		public String getAusername() {
			return ausername==null?"":ausername;
		}
		public void setAusername(String ausername) {
			this.ausername = ausername;
		}
		public String getContent() {
			return content==null?"":content;
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
		public LastAnsweredItem getLastedAnswer() {
			if (lastedAnswer == null)
				lastedAnswer=new LastAnsweredItem();
			return lastedAnswer;
		}
		public void setLastedAnswer(LastAnsweredItem lastedAnswer) {
			this.lastedAnswer = lastedAnswer;
		}
//		public AgainAskItem getAgainAskVo() {
//			return againAskVo;
//		}
//		public void setAgainAskVo(AgainAskItem againAskVo) {
//			this.againAskVo = againAskVo;
//		}
		
		
	}
	/**
	 * 最新一条回答
	 * @author menghui
	 *
	 */
	public class LastAnsweredItem{
		AgainAskItem againAskVo;
		int answerId;
		String content;
		int evaluate;
		String evaContent;
		String userId;
		String username;
		private String voiceAmr;
		private int voicelength;
		public AgainAskItem getAgainAskVo() {
			return againAskVo;
		}
		public void setAgainAskVo(AgainAskItem againAskVo) {
			this.againAskVo = againAskVo;
		}
		
		public String getEvaContent() {
			return evaContent==null?"":evaContent;
		}
		public void setEvaContent(String evaContent) {
			this.evaContent = evaContent;
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
		public int getAnswerId() {
			return answerId;
		}
		public void setAnswerId(int answerId) {
			this.answerId = answerId;
		}
		public String getContent() {
			return content==null?"":content;
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
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		
	}
	/**
	 * 追问
	 * @author menghui
	 *
	 */
	public class AgainAskItem{
		String content;
		long ctime;
		String againanswerContent;
		long againanswerCtime;
		int hasAgainanswer;
		String againanswerVoiceAmr;
		int againanswerVoicelength;
		
		public String getAgainanswerVoiceAmr() {
			return againanswerVoiceAmr;
		}
		public void setAgainanswerVoiceAmr(String againanswerVoiceAmr) {
			this.againanswerVoiceAmr = againanswerVoiceAmr;
		}
		public int getAgainanswerVoicelength() {
			return againanswerVoicelength;
		}
		public void setAgainanswerVoicelength(int againanswerVoicelength) {
			this.againanswerVoicelength = againanswerVoicelength;
		}
		public String getAgainanswerContent() {
			if (againanswerContent == null)
				return "";
			else
				return againanswerContent;
		}
		public void setAgainanswerContent(String againanswerContent) {
			this.againanswerContent = againanswerContent;
		}
		public long getAgainanswerCtime() {
			return againanswerCtime;
		}
		public void setAgainanswerCtime(long againanswerCtime) {
			this.againanswerCtime = againanswerCtime;
		}
		public String getContent() {
			if (content == null)
				return "";
			else
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
		public int getHasAgainanswer() {
			return hasAgainanswer;
		}
		public void setHasAgainanswer(int hasAgainanswer) {
			this.hasAgainanswer = hasAgainanswer;
		}
		
	}
}
