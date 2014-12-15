package com.gome.haoyuangong.net.result.tougu;

import java.util.ArrayList;
import java.util.List;

import com.gome.haoyuangong.net.result.TouguBaseResult;

public class LiveExchangeBean extends TouguBaseResult {

	
	private List<LiveExchangeItem> data = new ArrayList<LiveExchangeItem>();
	
	public List<LiveExchangeItem> getData() {
		return data;
	}

	public void setData(List<LiveExchangeItem> data) {
		this.data = data;
	}
	
	public static class LiveExchangeItem{
		
		private String id;
		private String from_id;
		private String uid;
		private String uname;
		private String time;
		private long timeStamp;
		private String content;
		private int isMaster;
		private int growupVal;
		private String headPic;
		private int verify;
		private int type;
		private String company;
		private String position;
//		private Quote quote = new Quote();
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getFrom_id() {
			return from_id;
		}
		public void setFrom_id(String from_id) {
			this.from_id = from_id;
		}
		public String getUid() {
			return uid;
		}
		public void setUid(String uid) {
			this.uid = uid;
		}
		public String getUname() {
			return uname;
		}
		public void setUname(String uname) {
			this.uname = uname;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public long getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(long timeStamp) {
			this.timeStamp = timeStamp;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public int getIsMaster() {
			return isMaster;
		}
		public void setIsMaster(int isMaster) {
			this.isMaster = isMaster;
		}
		public int getGrowupVal() {
			return growupVal;
		}
		public void setGrowupVal(int growupVal) {
			this.growupVal = growupVal;
		}
		public String getHeadPic() {
			return headPic;
		}
		public void setHeadPic(String headPic) {
			this.headPic = headPic;
		}
		public int getVerify() {
			return verify;
		}
		public void setVerify(int verify) {
			this.verify = verify;
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
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
//		public Quote getQuote() {
//			return quote;
//		}
//		public void setQuote(Quote quote) {
//			this.quote = quote;
//		}
		
	}
	
	
	public static class Quote{
		private String uid;
		private String uname;
		private String time;
		private String timeStamp;
		private String content;
		public String getUid() {
			return uid;
		}
		public void setUid(String uid) {
			this.uid = uid;
		}
		public String getUname() {
			return uname;
		}
		public void setUname(String uname) {
			this.uname = uname;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(String timeStamp) {
			this.timeStamp = timeStamp;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		
	}
}
