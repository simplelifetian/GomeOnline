package com.gome.haoyuangong.layout.self;

import java.util.ArrayList;
import java.util.List;

public class InvestGroupData {
	public class CategoryData{
		public CategoryData(){
			items = new ArrayList<CategoryDatas>();
		}
		private String categoryName;
		private List<CategoryDatas> items;
		public String getCategoryName() {
			return categoryName;
		}
		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}
		public List<CategoryDatas> getItems() {
			return items;
		}
		
	}
	public class CategoryDatas{
		private int date;
		private int value;
		public int getDate() {
			return date;
		}
		public void setDate(int date) {
			this.date = date;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		
	}

}
