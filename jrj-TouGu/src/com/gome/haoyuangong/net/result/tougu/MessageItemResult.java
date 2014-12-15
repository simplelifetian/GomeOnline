package com.gome.haoyuangong.net.result.tougu;

import java.util.ArrayList;
import java.util.List;

import com.gome.haoyuangong.net.result.BaseResultWeb;

public class MessageItemResult extends BaseResultWeb {

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
		private int mtype;
		private int stype;
		private int send;
		private Msg msg = new Msg();
		
		private long ctime;
		private int dataid;
		private String ip;
		private String rtype;
		private TouguUserBean senderinfo = new TouguUserBean();
		private int status;
		private String summary;
		
		private int dealtype;
		private int pid;
		private String pname;
		private float position;
		private float price;
		private String stockcode;
		private String stockname;
		
		public int getMtype() {
			return mtype;
		}
		public void setMtype(int mtype) {
			this.mtype = mtype;
		}
		public int getStype() {
			return stype;
		}
		public void setStype(int stype) {
			this.stype = stype;
		}
		public int getSend() {
			return send;
		}
		public void setSend(int send) {
			this.send = send;
		}
		public Msg getMsg() {
			return msg;
		}
		public void setMsg(Msg msg) {
			this.msg = msg;
		}
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
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public String getRtype() {
			return rtype;
		}
		public void setRtype(String rtype) {
			this.rtype = rtype;
		}
		public TouguUserBean getSenderinfo() {
			return senderinfo;
		}
		public void setSenderinfo(TouguUserBean senderinfo) {
			this.senderinfo = senderinfo;
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
		public int getDealtype() {
			return dealtype;
		}
		public void setDealtype(int dealtype) {
			this.dealtype = dealtype;
		}
		public int getPid() {
			return pid;
		}
		public void setPid(int pid) {
			this.pid = pid;
		}
		public String getPname() {
			return pname;
		}
		public void setPname(String pname) {
			this.pname = pname;
		}
		public float getPosition() {
			return position;
		}
		public void setPosition(float position) {
			this.position = position;
		}
		public float getPrice() {
			return price;
		}
		public void setPrice(float price) {
			this.price = price;
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
		
	}
	
	public static class Msg{
		private String sendername;
		private String receiverid;
		private int dataid;
		private String stockcode;
		private String stockname;
		private String title;
		private String summary;
		private long ctime;
		public String getSendername() {
			return sendername;
		}
		public void setSendername(String sendername) {
			this.sendername = sendername;
		}
		public String getReceiverid() {
			return receiverid;
		}
		public void setReceiverid(String receiverid) {
			this.receiverid = receiverid;
		}
		public int getDataid() {
			return dataid;
		}
		public void setDataid(int dataid) {
			this.dataid = dataid;
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
		public String getSummary() {
			return summary;
		}
		public void setSummary(String summary) {
			this.summary = summary;
		}
		public long getCtime() {
			return ctime;
		}
		public void setCtime(long ctime) {
			this.ctime = ctime;
		}
	}
}



