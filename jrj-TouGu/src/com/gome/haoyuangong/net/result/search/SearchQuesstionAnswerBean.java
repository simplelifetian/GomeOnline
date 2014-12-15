package com.gome.haoyuangong.net.result.search;

import java.util.ArrayList;

import com.gome.haoyuangong.net.result.BaseResultWeb;

public class SearchQuesstionAnswerBean extends BaseResultWeb{
	
	private int from ;
	
	private ArrayList<SearchQuesstionAnswerData>  data=new ArrayList<SearchQuesstionAnswerData>();

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public ArrayList<SearchQuesstionAnswerData> getData() {
		return data;
	}

	public void setData(ArrayList<SearchQuesstionAnswerData> data) {
		this.data = data;
	}

}
