package com.gome.haoyuangong.net.result.congenia;

import java.util.ArrayList;

import com.gome.haoyuangong.net.result.BaseResultWeb;

public class TitleResult extends BaseResultWeb{
	
	private Data data = new Data();
	
	public static class Data{
		
		private ArrayList<Item> list = new ArrayList<Item>();

		public ArrayList<Item> getList() {
			return list;
		}

		public void setList(ArrayList<Item> list) {
			this.list = list;
		}
		
	}
	
	public static class Item{
		private String key;
		private String value;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}


}
