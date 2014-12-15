package com.gome.haoyuangong.net.result.tougu;

import com.gome.haoyuangong.net.result.TouguBaseResult;

public class OpinionDetailBean extends TouguBaseResult {

	
	private Data data = new Data();
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data{
		private long id ; //主键ID 
		private String title;
		private String ctime;
		private int type;
		private String typeDesc;
		private String content;
		private int praise;
		private int limits;
		private int status;
		private UserInfo userinfo = new UserInfo();
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		
		public String getCtime() {
			return ctime;
		}
		public void setCtime(String ctime) {
			this.ctime = ctime;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getTypeDesc() {
			return typeDesc;
		}
		public void setTypeDesc(String typeDesc) {
			this.typeDesc = typeDesc;
		}
		
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public int getPraise() {
			return praise;
		}
		public void setPraise(int praise) {
			this.praise = praise;
		}
		public int getLimits() {
			return limits;
		}
		public void setLimits(int limits) {
			this.limits = limits;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public UserInfo getUserinfo() {
			return userinfo;
		}
		public void setUserinfo(UserInfo userinfo) {
			this.userinfo = userinfo;
		}
		
	}
	
}
