package com.gome.haoyuangong.net.result.found;

import java.util.ArrayList;

import com.gome.haoyuangong.net.result.BaseResultWeb;

public class FoundBean extends BaseResultWeb{
	
	private ArrayList<HotPointsData> hotpoints = new ArrayList<HotPointsData>();;
	
	private ArrayList<RecommendsData> recommends = new ArrayList<RecommendsData>();
	
	private ArrayList<AnswerData> asklist = new ArrayList<AnswerData>();

	public ArrayList<AnswerData> getAsklist() {
		return asklist;
	}

	public void setAsklist(ArrayList<AnswerData> asklist) {
		this.asklist = asklist;
	}

	public ArrayList<RecommendsData> getRecommends() {
		return recommends;
	}

	public void setRecommends(ArrayList<RecommendsData> recommends) {
		this.recommends = recommends;
	}

	public ArrayList<HotPointsData> getHotpoints() {
		return hotpoints;
	}

	public void setHotpoints(ArrayList<HotPointsData> hotpoints) {
		this.hotpoints = hotpoints;
	}

}
