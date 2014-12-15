package com.gome.haoyuangong.net.result.ask;

import java.util.ArrayList;
import java.util.List;

import android.text.SpannableString;

import com.gome.haoyuangong.net.result.BaseResultWeb;
import com.gome.haoyuangong.net.result.ask.AskItemAnswerResult.AskItemAnswer;
import com.gome.haoyuangong.net.result.tougu.TouguUserBean;

public class AskDetailResult extends BaseResultWeb{
	private AskDetail data=new AskDetail();
	
	public AskDetail getData() {
		return data;
	}

	public void setData(AskDetail data) {
		this.data = data;
	}

	public class AskDetail{
		private int askId;
		private String ausername;
		private String auserId;
		private String content;
		private long ctime;
		private List<AskDetailAnswer> answers = new ArrayList<AskDetailResult.AskDetailAnswer>();
		transient private SpannableString contentSpanStr;
		
		public SpannableString getContentSpanStr() {
			return contentSpanStr;
		}
		public void setContentSpanStr(SpannableString contentSpanStr) {
			this.contentSpanStr = contentSpanStr;
		}
		public String getAusername() {
			return ausername;
		}
		public void setAusername(String ausername) {
			this.ausername = ausername;
		}
		public List<AskDetailAnswer> getAnswers() {
			return answers;
		}
		public void setAnswers(List<AskDetailAnswer> answers) {
			this.answers = answers;
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
		
	}
	
	public class AskDetailAnswer{
		private int answerId;
		private String content;
		private long ctime;
		private int evaluate=-1;
		private String evaContent;
		private String voiceAmr;
		private int voicelength;
		private boolean includeImage;
		private TouguUserBean adviserUser=new TouguUserBean();
		private AgainAskVo againAskVo;
		transient private SpannableString contentSpanStr;
		transient private SpannableString evaContentSpanStr;
		
		public SpannableString getEvaContentSpanStr() {
			return evaContentSpanStr;
		}
		public void setEvaContentSpanStr(SpannableString evaContentSpanStr) {
			this.evaContentSpanStr = evaContentSpanStr;
		}
		public SpannableString getContentSpanStr() {
			return contentSpanStr;
		}
		public void setContentSpanStr(SpannableString contentSpanStr) {
			this.contentSpanStr = contentSpanStr;
		}
		public boolean isIncludeImage() {
			return includeImage;
		}
		public void setIncludeImage(boolean includeImage) {
			this.includeImage = includeImage;
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
		public String getVoiceAmr() {
			return voiceAmr;
		}
		public void setVoiceAmr(String voiceAmr) {
			this.voiceAmr = voiceAmr;
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
		public long getCtime() {
			return ctime;
		}
		public void setCtime(long ctime) {
			this.ctime = ctime;
		}
		public int getEvaluate() {
			return evaluate;
		}
		public void setEvaluate(int evaluate) {
			this.evaluate = evaluate;
		}
		public TouguUserBean getAdviserUser() {
			return adviserUser;
		}
		public void setAdviserUser(TouguUserBean adviserUser) {
			this.adviserUser = adviserUser;
		}
		public AgainAskVo getAgainAskVo() {
			return againAskVo;
		}
		public void setAgainAskVo(AgainAskVo againAskVo) {
			this.againAskVo = againAskVo;
		}
		public boolean isHaveEvaluate(){
			return evaluate>0;
		}
		
	}
	public class AgainAskVo{
		private String againanswerContent="";
		private String againanswerVoiceAmr;
		private int againanswerVoicelength;
		private long againanswerCtime;
		private String content;
		private long ctime;
		private int hasAgainanswer;
		private int id;
		transient private SpannableString contentSpanStr;
		transient private SpannableString againanswerContentSpanStr;
		
		public SpannableString getAgainanswerContentSpanStr() {
			return againanswerContentSpanStr;
		}
		public void setAgainanswerContentSpanStr(SpannableString againanswerContentSpanStr) {
			this.againanswerContentSpanStr = againanswerContentSpanStr;
		}
		public SpannableString getContentSpanStr() {
			return contentSpanStr;
		}
		public void setContentSpanStr(SpannableString contentSpanStr) {
			this.contentSpanStr = contentSpanStr;
		}
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
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		
	}
}
