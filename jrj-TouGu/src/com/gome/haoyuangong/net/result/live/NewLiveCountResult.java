package com.gome.haoyuangong.net.result.live;

import com.gome.haoyuangong.net.result.LiveBaseResult;

public class NewLiveCountResult extends LiveBaseResult {

	private int status;
	private String info;
	private Data data = new Data();
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data{
		private int count;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}
		
	}
}
