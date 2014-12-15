package com.gome.haoyuangong.layout.self.data;

import android.R.integer;

import com.gome.haoyuangong.layout.self.data.InvestAdviserInfo.Item;
import com.gome.haoyuangong.net.result.TouguBaseResult;
/**
 * 投顾个人管理
 * @author menghui
 *
 */
public class InvestAdviserManage extends TouguBaseResult {
	Data data;
	
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	public class Data{
		int signFlag;
		int investNum;
		int focusNum;
		int isVerify;
		AnswerItem answerJson;
		LiveItem liveJson;
		UserItem user;
		OpinionItem viewMap;		
		
		public int getSignFlag() {
			return signFlag;
		}
		public void setSignFlag(int signFlag) {
			this.signFlag = signFlag;
		}
		public int getInvestNum() {
			return investNum;
		}
		public void setInvestNum(int investNum) {
			this.investNum = investNum;
		}
		public int getFocusNum() {
			return focusNum;
		}
		public void setFocusNum(int focusNum) {
			this.focusNum = focusNum;
		}
		public int getIsVerify() {
			return isVerify;
		}
		public void setIsVerify(int isVerify) {
			this.isVerify = isVerify;
		}
		public AnswerItem getAnswerJson() {
			return answerJson;
		}
		public void setAnswerJson(AnswerItem answerJson) {
			this.answerJson = answerJson;
		}
		public LiveItem getLiveJson() {
			return liveJson;
		}
		public void setLiveJson(LiveItem liveJson) {
			this.liveJson = liveJson;
		}
		public UserItem getUser() {
			return user;
		}
		public void setUser(UserItem user) {
			this.user = user;
		}
		public OpinionItem getViewMap() {
			return viewMap;
		}
		public void setViewMap(OpinionItem viewMap) {
			this.viewMap = viewMap;
		}
		
	}	
	public class AnswerItem{
		int answerd;
		int noanswer;
		public int getAnswerd() {
			return answerd;
		}
		public void setAnswerd(int answerd) {
			this.answerd = answerd;
		}
		public int getNoanswer() {
			return noanswer;
		}
		public void setNoanswer(int noanswer) {
			this.noanswer = noanswer;
		}		
	}
	public class UserItem{
		String userId;
		String userName;
		String headImage;
		int type;
		String company;
		int signNum;
		int fansNum;
		float useSatisfaction;
		String investDirection;
		int experienceScope;
		String certificationNum;
		String intro;
		String typeDesc;
		
		public String getTypeDesc() {
			return typeDesc;
		}
		public void setTypeDesc(String typeDesc) {
			this.typeDesc = typeDesc;
		}
		public String getInvestDirection() {
			return investDirection;
		}
		public void setInvestDirection(String investDirection) {
			this.investDirection = investDirection;
		}
		public int getExperienceScope() {
			return experienceScope;
		}
		public void setExperienceScope(int experienceScope) {
			this.experienceScope = experienceScope;
		}
		public String getCertificationNum() {
			return certificationNum;
		}
		public void setCertificationNum(String certificationNum) {
			this.certificationNum = certificationNum;
		}
		public String getIntro() {
			return intro;
		}
		public void setIntro(String intro) {
			this.intro = intro;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getHeadImage() {
			return headImage;
		}
		public void setHeadImage(String headImage) {
			this.headImage = headImage;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getCompany() {
			return company;
		}
		public void setCompany(String company) {
			this.company = company;
		}
		public int getSignNum() {
			return signNum;
		}
		public void setSignNum(int signNum) {
			this.signNum = signNum;
		}
		public int getFansNum() {
			return fansNum;
		}
		public void setFansNum(int fansNum) {
			this.fansNum = fansNum;
		}
		public float getUseSatisfaction() {
			return useSatisfaction;
		}
		public void setUseSatisfaction(float useSatisfaction) {
			this.useSatisfaction = useSatisfaction;
		}		
	}
	public class LiveItem{
		String userid;
		int uv_show;
		int uv_total;
		int zhibo_isopen;
		public String getUserid() {
			return userid;
		}
		public void setUserid(String userid) {
			this.userid = userid;
		}
		public int getUv_show() {
			return uv_show;
		}
		public void setUv_show(int uv_show) {
			this.uv_show = uv_show;
		}
		public int getUv_total() {
			return uv_total;
		}
		public void setUv_total(int uv_total) {
			this.uv_total = uv_total;
		}
		public int getZhibo_isopen() {
			return zhibo_isopen;
		}
		public void setZhibo_isopen(int zhibo_isopen) {
			this.zhibo_isopen = zhibo_isopen;
		}
		
	}
	public class OpinionItem{
		int likeTotal;
		int publishTotal;
		int commentTotal;
		int readTotal;
		public int getLikeTotal() {
			return likeTotal;
		}
		public void setLikeTotal(int likeTotal) {
			this.likeTotal = likeTotal;
		}
		public int getPublishTotal() {
			return publishTotal;
		}
		public void setPublishTotal(int publishTotal) {
			this.publishTotal = publishTotal;
		}
		public int getCommentTotal() {
			return commentTotal;
		}
		public void setCommentTotal(int commentTotal) {
			this.commentTotal = commentTotal;
		}
		public int getReadTotal() {
			return readTotal;
		}
		public void setReadTotal(int readTotal) {
			this.readTotal = readTotal;
		}
		
	}
}
