package com.gome.haoyuangong.layout.self.data;

import com.gome.haoyuangong.net.result.TouguBaseResult;

public class UserInfoForApply extends TouguBaseResult {
	private Item data;
	
	public Item getData() {
		return data;
	}

	public void setData(Item data) {
		this.data = data;
	}

	public class Item{
		int id;
		String certificationNum;
		String city;
		String company;
		long ctime;
		int experienceScope;
		String headImage;
		String identityId;
		String identityImage;
		String intro;
		String investDirection;
		String label;
		String loginId;
		String mobile;
		String position;
		String province;
		String qq;
		int sex;
		int status;
		int type;
		String userName;
		long utime;
		int writeNum;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getCertificationNum() {
			return certificationNum;
		}
		public void setCertificationNum(String certificationNum) {
			this.certificationNum = certificationNum;
		}
		public String getCity() {
			return city==null?"":city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getCompany() {
			return company==null?"":company;
		}
		public void setCompany(String company) {
			this.company = company;
		}
		public long getCtime() {
			return ctime;
		}
		public void setCtime(long ctime) {
			this.ctime = ctime;
		}
		public int getExperienceScope() {
			return experienceScope;
		}
		public void setExperienceScope(int experienceScope) {
			this.experienceScope = experienceScope;
		}
		public String getHeadImage() {
			return headImage;
		}
		public void setHeadImage(String headImage) {
			this.headImage = headImage;
		}
		public String getIdentityId() {
			return identityId;
		}
		public void setIdentityId(String identityId) {
			this.identityId = identityId;
		}
		public String getIdentityImage() {
			return identityImage;
		}
		public void setIdentityImage(String identityImage) {
			this.identityImage = identityImage;
		}
		public String getIntro() {
			return intro;
		}
		public void setIntro(String intro) {
			this.intro = intro;
		}
		public String getInvestDirection() {
			return investDirection==null?"":investDirection;
		}
		public void setInvestDirection(String investDirection) {
			this.investDirection = investDirection;
		}
		public String getLabel() {
			return label==null?"":label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getLoginId() {
			return loginId;
		}
		public void setLoginId(String loginId) {
			this.loginId = loginId;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
		public String getProvince() {
			return province==null?"":province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getQq() {
			return qq;
		}
		public void setQq(String qq) {
			this.qq = qq;
		}
		public int getSex() {
			return sex;
		}
		public void setSex(int sex) {
			this.sex = sex;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public long getUtime() {
			return utime;
		}
		public void setUtime(long utime) {
			this.utime = utime;
		}
		public int getWriteNum() {
			return writeNum;
		}
		public void setWriteNum(int writeNum) {
			this.writeNum = writeNum;
		}
		
	}
}
