package com.gome.haoyuangong.net.result.live;

import java.util.ArrayList;

import com.gome.haoyuangong.net.result.BaseResultWeb;
import com.gome.haoyuangong.net.result.tougu.TouguUserBean;

public class MyLiveListBean extends BaseResultWeb {

	private Data data = new Data();
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}


	public static class Data{
		
		private ArrayList<MyLiveData> list = new ArrayList<MyLiveData>();

		public ArrayList<MyLiveData> getList() {
			return list;
		}

		public void setList(ArrayList<MyLiveData> list) {
			this.list = list;
		}
		
	}
	
	public static class MyLiveData{
		private long ctime;
		private int dataid;
		private int rtype;
		private String senderid;
		private int status;
		private String summary;
		
		private TouguUserBean senderinfo = new TouguUserBean();

		public long getCtime() {
			return ctime;
		}

		public void setCtime(long ctime) {
			this.ctime = ctime;
		}

		public int getDataid() {
			return dataid;
		}

		public void setDataid(int dataid) {
			this.dataid = dataid;
		}

		public int getRtype() {
			return rtype;
		}

		public void setRtype(int rtype) {
			this.rtype = rtype;
		}

		public String getSenderid() {
			return senderid;
		}

		public void setSenderid(String senderid) {
			this.senderid = senderid;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getSummary() {
			return summary;
		}

		public void setSummary(String summary) {
			this.summary = summary;
		}

		public TouguUserBean getSenderinfo() {
			return senderinfo;
		}

		public void setSenderinfo(TouguUserBean senderinfo) {
			this.senderinfo = senderinfo;
		}
		
	}
}
