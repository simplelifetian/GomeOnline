package com.gome.haoyuangong.layout.self.data;

import java.util.ArrayList;

import com.gome.haoyuangong.net.result.TouguBaseResult;

public class InvestAdviserListInfo extends TouguBaseResult {
	private ArrayList<Item> data = new ArrayList<Item>();

	public ArrayList<Item> getList() {
		return data;
	}

	public void setList(ArrayList<Item> list) {
		this.data = data;
	}

	public class Item{
		private String userId;
		private String userName;
		private String headImage;
		private int verify;
		private int growupVal;
		private int type;
		private String position;
		private int experienceScope;
		private String company;
		private String investDirection;
		private int useSatisfaction;
		private int fansNum;
		private int replyNum;
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
		public int getReplyNum() {
			return replyNum;
		}
		public void setReplyNum(int replyNum) {
			this.replyNum = replyNum;
		}
		
	}
}
