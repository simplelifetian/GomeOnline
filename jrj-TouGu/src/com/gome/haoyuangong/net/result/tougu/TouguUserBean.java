package com.gome.haoyuangong.net.result.tougu;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.gome.haoyuangong.UserInfo;


public class TouguUserBean {
	private String city;
	private String company;
	private int growupVal;
	private String headImage;
	private int isAdviser;
	private int pageId;
	private String position;
	private int signV;
	private int type;
	private String typeDesc;
	private String userId;
	private String userName;
	
	private String province;
	private int relationStatus;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public int getGrowupVal() {
		return growupVal;
	}
	public void setGrowupVal(int growupVal) {
		this.growupVal = growupVal;
	}
	public String getHeadImage() {
		return headImage;
	}
	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
	public int getIsAdviser() {
		return isAdviser;
	}
	public void setIsAdviser(int isAdviser) {
		this.isAdviser = isAdviser;
	}
	public int getPageId() {
		return pageId;
	}
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public int getSignV() {
		return signV;
	}
	public void setSignV(int signV) {
		this.signV = signV;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public int getRelationStatus() {
		return relationStatus;
	}
	public void setRelationStatus(int relationStatus) {
		this.relationStatus = relationStatus;
	}
	
	public void clear(){
		this.city = null;
		this.company = null;
		this.growupVal = 0;
		this.headImage = null;
		this.isAdviser = 0;
		this.pageId = 0;
		this.position = null;
		this.signV = 0;
		this.type = 0;
		this.typeDesc = null;
		this.userId = null;
		this.userName = null;
		
		this.province = null;
		this.relationStatus = 0;
	}
	
	public static void saveTouguUserBean(Context context,TouguUserBean touguUserBean){
		
		SharedPreferences.Editor editor = context.getSharedPreferences(UserInfo.LOGINED_USER_INFO, Activity.MODE_PRIVATE).edit();
		editor.putString("touguUserBean.city", touguUserBean.getCity());
		editor.putString("touguUserBean.company", touguUserBean.getCompany());
		editor.putInt("touguUserBean.growupVal", touguUserBean.getGrowupVal());
		editor.putString("touguUserBean.headImage", touguUserBean.getHeadImage());
		editor.putInt("touguUserBean.isAdviser", touguUserBean.getIsAdviser());
		editor.putInt("touguUserBean.pageId", touguUserBean.getPageId());
		editor.putString("touguUserBean.position", touguUserBean.getPosition());
		editor.putInt("touguUserBean.signV", touguUserBean.getSignV());
		editor.putInt("touguUserBean.type", touguUserBean.getType());
		editor.putString("touguUserBean.typeDesc", touguUserBean.getTypeDesc());
		editor.putString("touguUserBean.userId", touguUserBean.getUserId());
		editor.putString("touguUserBean.userName", touguUserBean.getUserName());
		editor.putString("touguUserBean.province", touguUserBean.getProvince());
		editor.putInt("touguUserBean.relationStatus", touguUserBean.getRelationStatus());
		editor.commit();
	}
	
	public static void readTouguUserBean(Context context,TouguUserBean touguUserBean){
		
		SharedPreferences reader = context.getSharedPreferences(UserInfo.LOGINED_USER_INFO, Activity.MODE_PRIVATE);
		touguUserBean.setCity(reader.getString("touguUserBean.city", null));
		touguUserBean.setCompany(reader.getString("touguUserBean.company", null));
		touguUserBean.setGrowupVal(reader.getInt("touguUserBean.growupVal", 0));
		touguUserBean.setHeadImage(reader.getString("touguUserBean.headImage", null));
		touguUserBean.setIsAdviser(reader.getInt("touguUserBean.isAdviser", 0));
		touguUserBean.setPageId(reader.getInt("touguUserBean.pageId", 0));
		touguUserBean.setPosition(reader.getString("touguUserBean.position", null));
		touguUserBean.setSignV(reader.getInt("touguUserBean.signV", 0));
		touguUserBean.setType(reader.getInt("touguUserBean.type", 0));
		touguUserBean.setTypeDesc(reader.getString("touguUserBean.typeDesc", null));
		touguUserBean.setUserId(reader.getString("touguUserBean.userId", null));
		touguUserBean.setUserName(reader.getString("touguUserBean.userName", null));
		touguUserBean.setProvince(reader.getString("touguUserBean.province", null));
		touguUserBean.setRelationStatus(reader.getInt("touguUserBean.relationStatus", 0));
		
	}
}
