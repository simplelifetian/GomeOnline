package com.gome.haoyuangong.net.result.congenia;

import java.util.ArrayList;

import com.gome.haoyuangong.net.result.BaseResultWeb;


public class CongenialResult  extends BaseResultWeb{
	
	
	private Data data = new Data();
	

	
	public static class Data{
		private ArrayList<Item>  list =new ArrayList<Item>();

		public ArrayList<Item> getList() {
			return list;
		}

		public void setList(ArrayList<Item> list) {
			this.list = list;
		}
	}

	
	public static class Item{
		private int id;
		private String loginId;
		private String userName;
		private String headImage;
		private String position;
		private String company;
		private String investDirection;
		private int rankFlag;
		private int rankCount;
		private int status;
		private int experienceScope;
		private int verify;
		private int useSatisfaction;
		private int answerNumber;
		private int fansNum;
		private int growupVal;
		private int type;
		private String typeDesc;
		private int attentionFlag;
		
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
		public int getVerify() {
			return verify;
		}
		public void setVerify(int verify) {
			this.verify = verify;
		}
		public int getGrowupVal() {
			return growupVal;
		}
		public void setGrowupVal(int growupVal) {
			this.growupVal = growupVal;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
		public int getExperienceScope() {
			return experienceScope;
		}
		public void setExperienceScope(int experienceScope) {
			this.experienceScope = experienceScope;
		}
		public String getCompany() {
			return company;
		}
		public void setCompany(String company) {
			this.company = company;
		}
		public String getInvestDirection() {
			return investDirection;
		}
		public void setInvestDirection(String investDirection) {
			this.investDirection = investDirection;
		}
		public int getUseSatisfaction() {
			return useSatisfaction;
		}
		public void setUseSatisfaction(int useSatisfaction) {
			this.useSatisfaction = useSatisfaction;
		}
		public int getFansNum() {
			return fansNum;
		}
		public void setFansNum(int fansNum) {
			this.fansNum = fansNum;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getLoginId() {
			return loginId;
		}
		public void setLoginId(String loginId) {
			this.loginId = loginId;
		}
		public int getRankFlag() {
			return rankFlag;
		}
		public void setRankFlag(int rankFlag) {
			this.rankFlag = rankFlag;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public int getAttentionFlag() {
			return attentionFlag;
		}
		public void setAttentionFlag(int attentionFlag) {
			this.attentionFlag = attentionFlag;
		}
		public int getRankCount() {
			return rankCount;
		}
		public void setRankCount(int rankCount) {
			this.rankCount = rankCount;
		}
		public int getAnswerNumber() {
			return answerNumber;
		}
		public void setAnswerNumber(int answerNumber) {
			this.answerNumber = answerNumber;
		}
		public String getTypeDesc() {
			return typeDesc;
		}
		public void setTypeDesc(String typeDesc) {
			this.typeDesc = typeDesc;
		}
	}


	public Data getData() {
		return data;
	}



	public void setData(Data data) {
		this.data = data;
	}
	
}
