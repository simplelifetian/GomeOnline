package com.gome.haoyuangong.net.result.tougu;

import java.util.ArrayList;
import java.util.List;

import com.gome.haoyuangong.net.result.BaseResultWeb;

public class SysMessageItemResult extends BaseResultWeb {

	private Data data = new Data();
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data{
		
		private List<MsgBean> list = new ArrayList<MsgBean>();

		public List<MsgBean> getList() {
			return list;
		}

		public void setList(List<MsgBean> list) {
			this.list = list;
		}
		
	}
	
	public static class MsgBean{
		private long ctime;
		private int dataid;
		private int status;
		private String summary;
		private TouguUserBean receiverinfo = new TouguUserBean();
		private String sendername;
		private String stockcode;
		private String stockname;
		private String title;
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
		public TouguUserBean getReceiverinfo() {
			return receiverinfo;
		}
		public void setReceiverinfo(TouguUserBean receiverinfo) {
			this.receiverinfo = receiverinfo;
		}
		public String getSendername() {
			return sendername;
		}
		public void setSendername(String sendername) {
			this.sendername = sendername;
		}
		public String getStockcode() {
			return stockcode;
		}
		public void setStockcode(String stockcode) {
			this.stockcode = stockcode;
		}
		public String getStockname() {
			return stockname;
		}
		public void setStockname(String stockname) {
			this.stockname = stockname;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		
	}
	
}



