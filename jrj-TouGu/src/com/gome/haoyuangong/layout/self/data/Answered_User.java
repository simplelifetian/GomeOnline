package com.gome.haoyuangong.layout.self.data;

import java.util.ArrayList;

import com.gome.haoyuangong.net.result.TouguBaseResult;

public class Answered_User extends TouguBaseResult {
	
	private Data data = new Data();
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data{
		private ArrayList<AnsweredItem> list = new ArrayList<AnsweredItem>();

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
		String content;
		long ctime;
		String answserUserId;
		String answerUsername;
		int hasAgainAsk;
		int answerTimes;		
		LastAskItem lastedAnswer;
		AdviserUser adviserUser;
		public AdviserUser getAdviserUser() {
			return adviserUser;
		}
		public void setAdviserUser(AdviserUser adviserUser) {
			this.adviserUser = adviserUser;
		}
		public int getAskId() {
			return askId;
		}
		public void setAskId(int askId) {
			this.askId = askId;
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
		public String getAnswserUserId() {
			return answserUserId;
		}
		public void setAnswserUserId(String answserUserId) {
			this.answserUserId = answserUserId;
		}
		public String getAnswerUsername() {
			return answerUsername == null ? "":answerUsername;
		}
		public void setAnswerUsername(String answerUsername) {
			this.answerUsername = answerUsername;
		}
		public int getHasAgainAsk() {
			return hasAgainAsk;
		}
		public void setHasAgainAsk(int hasAgainAsk) {
			this.hasAgainAsk = hasAgainAsk;
		}
		public int getAnswerTimes() {
			return answerTimes;
		}
		public void setAnswerTimes(int answerTimes) {
			this.answerTimes = answerTimes;
		}		
		public LastAskItem getLastedAnswer() {
//			if (lastedAnswer == null)
//				lastedAnswer = new LastAskItem();
			return lastedAnswer;
		}
		public void setLastedAnswer(LastAskItem lastedAnswer) {
			this.lastedAnswer = lastedAnswer;
		}
		
	}
	/**
	 * 最新一条回答
	 * @author menghui
	 *
	 */
	public class AdviserUser{
		String userId;
		String userName;
		String company;
		String headImage;
		String position;
		int type;
		int signV;
		int relationStatus;
		int growupVal;
		int isAdviser;
		String typeDesc;
		
		public String getTypeDesc() {
			return typeDesc;
		}
		public void setTypeDesc(String typeDesc) {
			this.typeDesc = typeDesc;
		}
		public int getSignV() {
			return signV;
		}
		public void setSignV(int signV) {
			this.signV = signV;
		}
		public int getRelationStatus() {
			return relationStatus;
		}
		public void setRelationStatus(int relationStatus) {
			this.relationStatus = relationStatus;
		}
		public int getGrowupVal() {
			return growupVal;
		}
		public void setGrowupVal(int growupVal) {
			this.growupVal = growupVal;
		}
		public int getIsAdviser() {
			return isAdviser;
		}
		public void setIsAdviser(int isAdviser) {
			this.isAdviser = isAdviser;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getUserName() {
			return userName==null?"":userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getCompany() {
			return company;
		}
		public void setCompany(String company) {
			this.company = company;
		}
		public String getHeadImage() {
			return headImage;
		}
		public void setHeadImage(String headImage) {
			this.headImage = headImage;
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
		
	}
	/**
	 * 追问
	 * @author menghui
	 *
	 */
	public class LastAskItem{
		int answerId;
		String content;
		int evaluate;
		String evaContent;
		String voiceAmr;
		int voicelength;
		AgainAskItem againAskVo;	
		AdviserUser adviserUser;
		public AdviserUser getAdviserUser() {
			return adviserUser;
		}
		public void setAdviserUser(AdviserUser adviserUser) {
			this.adviserUser = adviserUser;
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
		public String getEvaContent() {
			return evaContent;
		}
		public void setEvaContent(String evaContent) {
			this.evaContent = evaContent;
		}
		public AgainAskItem getAgainAskVo() {
//			if (againAskVo == null)
//				againAskVo = new AgainAskItem();
			return againAskVo;
		}
		public void setAgainAskVo(AgainAskItem againAskVo) {
			this.againAskVo = againAskVo;
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
	
	}
	
	public class AgainAskItem{
		String content;
		long ctime;
		int hasAgainanswer;
		String againanswerContent;
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
		public String getAgainanswerContent() {
			if (againanswerContent == null)
				return "";
			else
				return againanswerContent;
		}
		public void setAgainanswerContent(String againanswerContent) {
			this.againanswerContent = againanswerContent;
		}
		
	}
}
