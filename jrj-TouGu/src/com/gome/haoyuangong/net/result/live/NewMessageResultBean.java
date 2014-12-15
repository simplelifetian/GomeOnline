package com.gome.haoyuangong.net.result.live;

import java.util.ArrayList;
import java.util.List;

import com.gome.haoyuangong.net.result.BaseResultWeb;

public class NewMessageResultBean extends BaseResultWeb {
	
	private Data data = new Data();
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data{
		
		private List<NewMsgBean> list = new ArrayList<NewMsgBean>();

		public List<NewMsgBean> getList() {
			return list;
		}

		public void setList(List<NewMsgBean> list) {
			this.list = list;
		}
		
	}
	
	public static class NewMsgBean{
		
		private int maxID;
		private int type;
		public int getMaxID() {
			return maxID;
		}
		public void setMaxID(int maxID) {
			this.maxID = maxID;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
	}
}


