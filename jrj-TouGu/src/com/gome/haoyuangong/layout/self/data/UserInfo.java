package com.gome.haoyuangong.layout.self.data;

import java.security.Principal;

import com.gome.haoyuangong.net.result.TouguBaseResult;

public class UserInfo extends TouguBaseResult {

	Item data;
	
	public Item getData() {
		return data;
	}

	public void setData(Item data) {
		this.data = data;
	}

	public class Item{
		String city;
		int isAdviser;
		String mobile;
		String nickName;
		String passportId;
		String passportName;
		String province;
		String userId;
		String userName;
		String headImage;
		public String getCity() {
			return city == null?"":city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public int getIsAdviser() {
			return isAdviser;
		}
		public void setIsAdviser(int isAdviser) {
			this.isAdviser = isAdviser;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getNickName() {
			return nickName;
		}
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}
		public String getPassportId() {
			return passportId;
		}
		public void setPassportId(String passportId) {
			this.passportId = passportId;
		}
		public String getPassportName() {
			return passportName;
		}
		public void setPassportName(String passportName) {
			this.passportName = passportName;
		}
		public String getProvince() {
			return province == null?"":province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getUserName() {
			return userName == null?"":userName;
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
		
	}
}
