package com.gome.haoyuangong.net.result.found;

import android.text.SpannableString;

public class AnswerData {

	private int answerTimes ;
	private int askId;
	private String auserId;
	private String ausername;
	private String content;
	private long ctime;
	private String headImages;
	private LastAnswer lastedAnswer;
	transient private SpannableString contentSpanStr;
	
	public SpannableString getContentSpanStr() {
		return contentSpanStr;
	}
	public void setContentSpanStr(SpannableString contentSpanStr) {
		this.contentSpanStr = contentSpanStr;
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
	public String getHeadImages() {
		return headImages;
	}
	public void setHeadImages(String headImages) {
		this.headImages = headImages;
	}
	public LastAnswer getLastedAnswer() {
		return lastedAnswer;
	}
	public void setLastedAnswer(LastAnswer lastedAnswer) {
		this.lastedAnswer = lastedAnswer;
	}
	
	
}
