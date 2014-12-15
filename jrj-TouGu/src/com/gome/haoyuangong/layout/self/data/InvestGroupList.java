package com.gome.haoyuangong.layout.self.data;

import java.util.ArrayList;
import java.util.List;

import com.gome.haoyuangong.net.result.TouguBaseResult;

public class InvestGroupList extends TouguBaseResult {

	private Data data;
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data{
		private List<InvestGroupItem> list = new ArrayList<InvestGroupList.InvestGroupItem>();
		private int relation;

		public List<InvestGroupItem> getList() {
			return list;
		}

		public void setList(List<InvestGroupItem> list) {
			this.list = list;
		}

		public int getRelation() {
			return relation;
		}

		public void setRelation(int relation) {
			this.relation = relation;
		}	
		
	}
	
	public class InvestGroupItem{
		int id;
		long ctime;
		long lasttrade;
		String pdesc;
		String pname;
		int status;
		String userid;
		String username;
		int limits;
		
		public int getLimits() {
			return limits;
		}
		public void setLimits(int limits) {
			this.limits = limits;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public long getCtime() {
			return ctime;
		}
		public void setCtime(long ctime) {
			this.ctime = ctime;
		}
		public long getLasttrade() {
			return lasttrade;
		}
		public void setLasttrade(long lasttrade) {
			this.lasttrade = lasttrade;
		}
		public String getPdesc() {
			return pdesc;
		}
		public void setPdesc(String pdesc) {
			this.pdesc = pdesc;
		}
		public String getPname() {
			return pname;
		}
		public void setPname(String pname) {
			this.pname = pname;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public String getUserid() {
			return userid;
		}
		public void setUserid(String userid) {
			this.userid = userid;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		
	}
}
