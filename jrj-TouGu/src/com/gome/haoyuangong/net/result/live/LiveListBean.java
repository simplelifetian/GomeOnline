package com.gome.haoyuangong.net.result.live;

import java.util.ArrayList;

import com.gome.haoyuangong.net.result.BaseResultWeb;

public class LiveListBean extends BaseResultWeb {

	private Data data = new Data();
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}


	public static class Data{
		
		private ArrayList<LiveData> list = new ArrayList<LiveData>();

		public ArrayList<LiveData> getList() {
			return list;
		}

		public void setList(ArrayList<LiveData> list) {
			this.list = list;
		}
		
	}
	
	public static class LiveData{
		private String room_id; //直播室id 
		private String room_name;//用户名 
		private String zhibo_title;//头像 
		private String zhibo_intro;//是否投顾
		private String zhibo_notice;//等级 
		private String userid;//投顾身份
		private String userName;//最新发布内容
		private String uv_number;//最新发布时间 Y-m-d H:i:s
		private String uv_show;//最新发布时间戳
		private String zhibo_isopen;
		private String lastContent;
		private long timeStamp;
		private int growupVal;
		private int type;
		private String typeDesc;
		private String company;
		private String position;
		private String headPic;
		private int signV;
		public String getRoom_id() {
			return room_id;
		}
		public void setRoom_id(String room_id) {
			this.room_id = room_id;
		}
		public String getRoom_name() {
			return room_name;
		}
		public void setRoom_name(String room_name) {
			this.room_name = room_name;
		}
		public String getZhibo_title() {
			return zhibo_title;
		}
		public void setZhibo_title(String zhibo_title) {
			this.zhibo_title = zhibo_title;
		}
		public String getZhibo_intro() {
			return zhibo_intro;
		}
		public void setZhibo_intro(String zhibo_intro) {
			this.zhibo_intro = zhibo_intro;
		}
		public String getZhibo_notice() {
			return zhibo_notice;
		}
		public void setZhibo_notice(String zhibo_notice) {
			this.zhibo_notice = zhibo_notice;
		}
		public String getUserid() {
			return userid;
		}
		public void setUserid(String userid) {
			this.userid = userid;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getUv_number() {
			return uv_number;
		}
		public void setUv_number(String uv_number) {
			this.uv_number = uv_number;
		}
		public String getUv_show() {
			return uv_show;
		}
		public void setUv_show(String uv_show) {
			this.uv_show = uv_show;
		}
		public String getZhibo_isopen() {
			return zhibo_isopen;
		}
		public void setZhibo_isopen(String zhibo_isopen) {
			this.zhibo_isopen = zhibo_isopen;
		}
		
		public String getLastContent() {
			return lastContent;
		}
		public void setLastContent(String lastContent) {
			this.lastContent = lastContent;
		}
		public long getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(long timeStamp) {
			this.timeStamp = timeStamp;
		}
		public int getGrowupVal() {
			return growupVal;
		}
		public void setGrowupVal(int growupVal) {
			this.growupVal = growupVal;
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
		public String getCompany() {
			return company;
		}
		public void setCompany(String company) {
			this.company = company;
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
		public String getHeadPic() {
			return headPic;
		}
		public void setHeadPic(String headPic) {
			this.headPic = headPic;
		}
		public int getSignV() {
			return signV;
		}
		public void setSignV(int signV) {
			this.signV = signV;
		}
		
	}
}
