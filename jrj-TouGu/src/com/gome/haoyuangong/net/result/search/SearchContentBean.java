package com.gome.haoyuangong.net.result.search;

import java.util.ArrayList;
import java.util.List;

import com.gome.haoyuangong.net.result.BaseResultWeb;

public class SearchContentBean extends BaseResultWeb{
	
	private ArrayList<SearchContentData> data;
	
	private int from;

	public ArrayList<SearchContentData> getData() {
		return data;
	}

	public void setData(ArrayList<SearchContentData> data) {
		this.data = data;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}
	

}
