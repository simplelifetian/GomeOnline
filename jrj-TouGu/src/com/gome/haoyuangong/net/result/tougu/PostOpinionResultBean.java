package com.gome.haoyuangong.net.result.tougu;

import com.gome.haoyuangong.net.result.BaseResultWeb;

public class PostOpinionResultBean extends BaseResultWeb {
	
	private Data data = new Data();
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data{
		private String id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}
}
