package com.gome.haoyuangong.net.result.tougu;

import com.gome.haoyuangong.net.result.BaseResultWeb;

public class HotLabelResult extends BaseResultWeb {

	private Data data = new Data();
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data{
		private String[] list = new String[0];

		public String[] getList() {
			return list;
		}

		public void setList(String[] list) {
			this.list = list;
		}
		
		
	}
}
