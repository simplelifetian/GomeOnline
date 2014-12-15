package com.gome.haoyuangong.net.result.tougu;

import com.gome.haoyuangong.net.result.BaseResultWeb;

public class UserIdentifiedResult extends BaseResultWeb {
	
	private Data data = new Data();
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data{
		private int isAdviser;
		private int relationStatus;
		private TouguUserBean user = new TouguUserBean();
		public int getIsAdviser() {
			return isAdviser;
		}
		public void setIsAdviser(int isAdviser) {
			this.isAdviser = isAdviser;
		}
		public int getRelationStatus() {
			return relationStatus;
		}
		public void setRelationStatus(int relationStatus) {
			this.relationStatus = relationStatus;
		}
		public TouguUserBean getUser() {
			return user;
		}
		public void setUser(TouguUserBean user) {
			this.user = user;
		}
		
	}
}
