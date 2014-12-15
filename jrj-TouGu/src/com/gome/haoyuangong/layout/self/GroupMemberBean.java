package com.gome.haoyuangong.layout.self;


public class GroupMemberBean {

	boolean selected;
	private String name;   //��ʾ�����
	private String sortLetters;  //��ʾ���ƴ��������ĸ
	private Object item;
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	public Object getInfoItem() {
		return item;
	}
	public void setInfoItem(Object obj) {
		this.item = obj;
	}
	
}
