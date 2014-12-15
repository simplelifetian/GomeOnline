package com.gome.haoyuangong.layout.self.data;

import java.util.ArrayList;

import com.gome.haoyuangong.net.result.TouguBaseResult;

public class InvestAdviserInfo extends TouguBaseResult {
	private Item data = new Item();


	public Item getData() {
		return data;
	}


	public void setData(Item data) {
		this.data = data;
	}


	public class Item{
		private String userName;
		private String headImage;
		private int sex;
		private int isVerify;
		private int type;
		private String position;
		private String province;
		private String company;
		private String certificationNum;
		private int experienceScope;
		private String investDirection;
		private String label;
		private String intro;
		
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}

		public String getHeadImage() {
			return headImage;
		}
		public void setHeadImage(String headImage) {
			this.headImage = headImage;
		}

		public int getSex() {
			return sex;
		}
		public void setSex(int sex) {
			this.sex = sex;
		}
		public int getIsVerify() {
			return isVerify;
		}
		public void setIsVerify(int isVerify) {
			this.isVerify = isVerify;
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
		public String getCompany() {
			return company;
		}
		public void setCompany(String company) {
			this.company = company;
		}
		public String getCertificationNum() {
			return certificationNum;
		}
		public void setCertificationNum(String certificationNum) {
			this.certificationNum = certificationNum;
		}
		public String getInvestDirection() {
			return investDirection;
		}
		public void setInvestDirection(String investDirection) {
			this.investDirection = investDirection;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getIntro() {
			return intro;
		}
		public void setIntro(String intro) {
			this.intro = intro;
		}
		public int getExperienceScope() {
			return experienceScope;
		}
		public void setExperienceScope(int experienceScope) {
			this.experienceScope = experienceScope;
		}

		
	}
}