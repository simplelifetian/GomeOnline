package com.gome.haoyuangong.net.result.live;

import java.util.ArrayList;
import java.util.List;

import android.text.SpannableStringBuilder;

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
		
		private int id;
		private int from_id;
		private String uid;
		private String uname;
		private String time;
		private String content;
		private LiveExchangeItem quote[];
		private int isMaster;
		private int delIDs;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getFrom_id() {
			return from_id;
		}
		public void setFrom_id(int from_id) {
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
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public LiveExchangeItem[] getQuote() {
			return quote;
		}
		public void setQuote(LiveExchangeItem[] quote) {
			this.quote = quote;
		}
		public int getIsMaster() {
			return isMaster;
		}
		public void setIsMaster(int isMaster) {
			this.isMaster = isMaster;
		}
		public int getDelIDs() {
			return delIDs;
		}
		public void setDelIDs(int delIDs) {
			this.delIDs = delIDs;
		}
		
	}
	
}
