package com.gome.haoyuangong.net.result.search;

import java.util.ArrayList;
import java.util.List;

import com.gome.haoyuangong.net.result.BaseResultWeb;

public class SearchCongeniaBean extends BaseResultWeb {
	
//	private Data data;
	
	private int from;
	
	private ArrayList<SearchCongeniaData> data=new ArrayList<SearchCongeniaData>();

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public ArrayList<SearchCongeniaData> getData() {
		return data;
	}

	public void setData(ArrayList<SearchCongeniaData> data) {
		this.data = data;
	}
	

//	public static class Data {
//		
//		private List<SearchCongeniaData>  list;
//		
//		private int recordnum;
//
//		public List<SearchCongeniaData> getList() {
//			return list;
//		}
//
//		public void setList(List<SearchCongeniaData> list) {
//			this.list = list;
//		}
//
//		public int getRecordnum() {
//			return recordnum;
//		}
//
//		public void setRecordnum(int recordnum) {
//			this.recordnum = recordnum;
//		}
//
//		
//	}


//	public Data getData() {
//		return data;
//	}
//
//
//	public void setData(Data data) {
//		this.data = data;
//	}



}
