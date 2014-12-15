package com.gome.haoyuangong.bean;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = CustInfoReq.TABLE_NAME)
public class CustInfoReq {
	
	/** 所有来源，或者所有分组*/
	public static final String ALL = "-1";
	public static final String GROUP_ESALES_0 = "0";//0：壹钱包
	public static final String GROUP_INVITE_1 = "1";//1：待邀请
	public static final String GROUP_INVITED_2 = "2";//2：已邀请
	
	public static final String SOURCE_ESALES_0 = "0";
	public static final String SOURCE_PHONE_1 = "1";
	
	public static final String TABLE_NAME = "cust_info";
	
	public static final String F_ID = "_id";
	public static final String F_GROUP_FLG = "groupFlg";
	public static final String F_CUST_SOURCE = "custSource";
	public static final String F_INVITE_COUNT = "inviteCount";
	
	/*@DatabaseField(generatedId = true, columnName = F_ID)
	private Long id;*/
	
	@DatabaseField
	@Expose private String custId;
	@DatabaseField
	@Expose private String agentId;
	@DatabaseField
	private String custName;
	@DatabaseField
	@Expose private String custMobile;
	@DatabaseField
	private String custAdress;
	@DatabaseField(columnName = F_GROUP_FLG)
	private String groupFlg;
	@DatabaseField(columnName = F_CUST_SOURCE)
	private String custSource;
	@DatabaseField(columnName = F_INVITE_COUNT)
	private int inviteCount;
	@DatabaseField
	private String deleteFlg;
	private String sortLetters;
	
	private boolean selected;
	
	public CustInfoReq() {
		super();
	}
	
	public CustInfoReq(String custId, String agentId, String custName,
			String custMobile, String custAdress, String groupFlg,
			String custSource, int inviteCount, String deleteFlg,
			String sortLetters) {
		super();
		this.custId = custId;
		this.agentId = agentId;
		this.custName = custName;
		this.custMobile = custMobile;
		this.custAdress = custAdress;
		this.groupFlg = groupFlg;
		this.custSource = custSource;
		this.inviteCount = inviteCount;
		this.deleteFlg = deleteFlg;
		this.sortLetters = sortLetters;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCustMobile() {
		return custMobile;
	}
	public void setCustMobile(String custMobile) {
		this.custMobile = custMobile;
	}
	public String getCustAdress() {
		return custAdress;
	}
	public void setCustAdress(String custAdress) {
		this.custAdress = custAdress;
	}
	public String getGroupFlg() {
		return groupFlg;
	}
	public void setGroupFlg(String groupFlg) {
		this.groupFlg = groupFlg;
	}
	public String getCustSource() {
		return custSource;
	}
	public void setCustSource(String custSource) {
		this.custSource = custSource;
	}
	public int getInviteCount() {
		return inviteCount;
	}
	public void setInviteCount(int inviteCount) {
		this.inviteCount = inviteCount;
	}
	public String getDeleteFlg() {
		return deleteFlg;
	}
	public void setDeleteFlg(String deleteFlg) {
		this.deleteFlg = deleteFlg;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public String toString() {
		return "CustInfoReq [custId=" + custId + ", agentId=" + agentId
				+ ", custName=" + custName + ", custMobile=" + custMobile
				+ ", custAdress=" + custAdress + ", groupFlg=" + groupFlg
				+ ", custSource=" + custSource + ", inviteCount=" + inviteCount
				+ ", deleteFlg=" + deleteFlg + ", sortLetters=" + sortLetters
				+ ", selected=" + selected + "]";
	}
}	
