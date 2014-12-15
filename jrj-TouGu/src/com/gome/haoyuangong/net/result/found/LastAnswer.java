package com.gome.haoyuangong.net.result.found;

import android.text.SpannableString;

public class LastAnswer {
	
	private AdviserUserData adviserUser;
	private int anserId;
	private long ctime;
	private String voiceAmr;
	private int voicelength;
	private String content;
	transient private SpannableString contentSpanStr;
	
	public SpannableString getContentSpanStr() {
		return contentSpanStr;
	}
	public void setContentSpanStr(SpannableString contentSpanStr) {
		this.contentSpanStr = contentSpanStr;
	}
	public AdviserUserData getAdviserUser() {
		return adviserUser;
	}
	public void setAdviserUser(AdviserUserData adviserUser) {
		this.adviserUser = adviserUser;
	}
	public int getAnserId() {
		return anserId;
	}
	public void setAnserId(int anserId) {
		this.anserId = anserId;
	}
	public long getCtime() {
		return ctime;
	}
	public void setCtime(long ctime) {
		this.ctime = ctime;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
