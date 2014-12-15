package com.gome.haoyuangong.net.result.Questionnaire;

import java.util.ArrayList;

import com.gome.haoyuangong.net.result.BaseResultWeb;

public class QuestionnarieBean extends BaseResultWeb{
	
	private Data data;
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data{
		private ArrayList<QuestionnarieData> list=new ArrayList<QuestionnarieData>();

		public ArrayList<QuestionnarieData> getList() {
			return list;
		}

		public void setList(ArrayList<QuestionnarieData> list) {
			this.list = list;
		}
	}

}
