package com.gome.haoyuangong.layout.self.data;

import com.gome.haoyuangong.net.result.TouguBaseResult;

public class UserManageBean extends TouguBaseResult {

	Item data;
	
	public Item getData() {
		return data;
	}

	public void setData(Item data) {
		this.data = data;
	}

	public class Item{
		String loginId;
		String userName;
		String headImage;
		String province;
		String city;
		int attentionNum;
		int consultNum;
		String adviserId;
		
		public String getAdviserId() {
			return adviserId;
		}
		public void setAdviserId(String adviserId) {
			this.adviserId = adviserId;
		}
		public String getLoginId() {
			return loginId;
		}
		public void setLoginId(String loginId) {
			this.loginId = loginId;
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
		public String getProvince() {
			return province==null?"":province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getCity() {
			return city==null?"":city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public int getAttentionNum() {
			return attentionNum;
		}
		public void setAttentionNum(int attentionNum) {
			this.attentionNum = attentionNum;
		}
		public int getConsultNum() {
			return consultNum;
		}
		public void setConsultNum(int consultNum) {
			this.consultNum = consultNum;
		}
		
	}
}
