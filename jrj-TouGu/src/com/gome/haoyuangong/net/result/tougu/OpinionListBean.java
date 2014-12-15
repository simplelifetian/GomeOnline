package com.gome.haoyuangong.net.result.tougu;

import java.util.ArrayList;

import com.gome.haoyuangong.net.result.TouguBaseResult;

public class OpinionListBean extends TouguBaseResult {

	
	private Data data = new Data();
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data{
		
		private ArrayList<OpinionItem> list = new ArrayList<OpinionItem>();

		public ArrayList<OpinionItem> getList() {
			return list;
		}

		public void setList(ArrayList<OpinionItem> list) {
			this.list = list;
		}

	}
	
	public static class OpinionItem{
		
		private AdviserInfo adviserInfo = new AdviserInfo();
		private int pageId;
		private PointInfo pointInfo = new PointInfo();
		public AdviserInfo getAdviserInfo() {
			return adviserInfo;
		}
		public void setAdviserInfo(AdviserInfo adviserInfo) {
			this.adviserInfo = adviserInfo;
		}
		public int getPageId() {
			return pageId;
		}
		public void setPageId(int pageId) {
			this.pageId = pageId;
		}
		public PointInfo getPointInfo() {
			return pointInfo;
		}
		public void setPointInfo(PointInfo pointInfo) {
			this.pointInfo = pointInfo;
		}
		
		
	}
	
	public static class AdviserInfo{
		private int category;
		private String company;
		private int growupVal;
		private String headImage;
		private int isVerify;
		private String userId;
		private String userName;
		private String typeDesc;
		public int getCategory() {
			return category;
		}
		public void setCategory(int category) {
			this.category = category;
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
		public int getIsVerify() {
			return isVerify;
		}
		public void setIsVerify(int isVerify) {
			this.isVerify = isVerify;
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
		public String getTypeDesc() {
			return typeDesc;
		}
		public void setTypeDesc(String typeDesc) {
			this.typeDesc = typeDesc;
		}
		
	}
	
	public static class PointInfo{
		private int commentNum;
		private long ctime;
		private int id;
		private int likeNum;
		private int limits;
		private String pointPic;
		private String summary;
		private String title;
		private int type;
		private String webUrl;
		public int getCommentNum() {
			return commentNum;
		}
		public void setCommentNum(int commentNum) {
			this.commentNum = commentNum;
		}
		public long getCtime() {
			return ctime;
		}
		public void setCtime(long ctime) {
			this.ctime = ctime;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getLikeNum() {
			return likeNum;
		}
		public void setLikeNum(int likeNum) {
			this.likeNum = likeNum;
		}
		public int getLimits() {
			return limits;
		}
		public void setLimits(int limits) {
			this.limits = limits;
		}
		public String getPointPic() {
			return pointPic;
		}
		public void setPointPic(String pointPic) {
			this.pointPic = pointPic;
		}
		public String getSummary() {
			return summary;
		}
		public void setSummary(String summary) {
			this.summary = summary;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getWebUrl() {
			return webUrl;
		}
		public void setWebUrl(String webUrl) {
			this.webUrl = webUrl;
		}
		
	}
}
