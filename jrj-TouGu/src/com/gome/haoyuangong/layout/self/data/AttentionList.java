package com.gome.haoyuangong.layout.self.data;

import java.util.ArrayList;

import com.gome.haoyuangong.layout.self.data.AttentionList.Data;
import com.gome.haoyuangong.net.result.TouguBaseResult;

public class AttentionList extends TouguBaseResult {
	private Data data = new Data();
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data{
		
	private ArrayList<AttentionItem> list = new ArrayList<AttentionItem>();

	public ArrayList<AttentionItem> getList() {
		return list;
	}

	public void setList(ArrayList<AttentionItem> list) {
		this.list = list;
	}

	}
	public class AttentionItem {
		String city;
		String company;
		int growupVal;
		String headImage;
		int isAdviser;
		String pageId;
		String position;
		String province;
		int signV;
		int type;
		String typeDesc;
		String userId;
		String userName;
		
		public String getTypeDesc() {
			return typeDesc==null?"":typeDesc;
		}
		public void setTypeDesc(String typeDesc) {
			this.typeDesc = typeDesc;
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
		public int getGrowupVal() {
			return growupVal;
		}
		public void setGrowupVal(int growupVal) {
			this.growupVal = growupVal;
		}
		public String getHeadImage() {
			return headImage;
		}
		public void setHeadImage(String headImage) {
			this.headImage = headImage;
		}
		public int getIsAdviser() {
			return isAdviser;
		}
		public void setIsAdviser(int isAdviser) {
			this.isAdviser = isAdviser;
		}
		public String getPageId() {
			return pageId;
		}
		public void setPageId(String pageId) {
			this.pageId = pageId;
		}
		public String getPosition() {
			return position==null?"":position;
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
		public int getSignV() {
			return signV;
		}
		public void setSignV(int signV) {
			this.signV = signV;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
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
		
	}
}
