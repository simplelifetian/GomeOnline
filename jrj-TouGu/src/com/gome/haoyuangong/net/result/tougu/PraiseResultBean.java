package com.gome.haoyuangong.net.result.tougu;

import com.gome.haoyuangong.net.result.BaseResultWeb;

public class PraiseResultBean extends BaseResultWeb {
	
	private Data data = new Data();
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data{
		private String msg;

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

	}
}
