package com.gome.haoyuangong.layout.self.data;

import java.util.ArrayList;
import java.util.List;

import com.gome.haoyuangong.net.result.TouguBaseResult;

public class InvestGroupPosition extends TouguBaseResult {

	Data data;
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data{
		List<InvestGroupPositionItem> list = new ArrayList<InvestGroupPosition.InvestGroupPositionItem>();
		int relation;
		public List<InvestGroupPositionItem> getList() {
			return list;
		}

		public void setList(List<InvestGroupPositionItem> list) {
			this.list = list;
		}

		public int getRelation() {
			return relation;
		}

		public void setRelation(int relation) {
			this.relation = relation;
		}
		
	}
	
	public class InvestGroupPositionItem{
		String userId;
		int pid;
		String stockCode;
		String stockName;
		String market;
		double position;
		
		public String getMarket() {
			return market;
		}
		public void setMarket(String market) {
			this.market = market;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public int getPid() {
			return pid;
		}
		public void setPid(int pid) {
			this.pid = pid;
		}
		public String getStockCode() {
			return stockCode;
		}
		public void setStockCode(String stockCode) {
			this.stockCode = stockCode;
		}
		public String getStockName() {
			return stockName;
		}
		public void setStockName(String stockName) {
			this.stockName = stockName;
		}
		public double getPosition() {
			return position;
		}
		public void setPosition(double position) {
			this.position = position;
		}
		
	}
}
