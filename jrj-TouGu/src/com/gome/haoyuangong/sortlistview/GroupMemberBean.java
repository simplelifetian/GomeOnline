package com.gome.haoyuangong.sortlistview;

import com.gome.haoyuangong.layout.self.PersonalInfoItem;

public class GroupMemberBean {

	private String name;   //��ʾ�����
	private String sortLetters;  //��ʾ���ƴ��������ĸ
	private PersonalInfoItem infoItem;
	
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
	public PersonalInfoItem getInfoItem() {
		return infoItem;
	}
	public void setInfoItem(PersonalInfoItem infoItem) {
		this.infoItem = infoItem;
	}
	
}
