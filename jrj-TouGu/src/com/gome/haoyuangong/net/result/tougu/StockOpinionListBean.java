package com.gome.haoyuangong.net.result.tougu;

import java.util.ArrayList;

import com.gome.haoyuangong.net.result.TouguBaseResult;

public class StockOpinionListBean extends TouguBaseResult {

	private ArrayList<StockOpinionItem> data = new ArrayList<StockOpinionItem>();

	public ArrayList<StockOpinionItem> getData() {
		return data;
	}

	public void setData(ArrayList<StockOpinionItem> data) {
		this.data = data;
	}

	public static class StockOpinionItem {
		private int adviserType;
		private int comments;
		private String company;
		private String content;
		private long ctime;
		private int growupVal;
		private int id;
		private int limits;
		private String linkTitle;
		private int praise;
		private int reads;
		private int status;
		private String summary;
		private String title;
		private int type;
		private String userId;
		private String userName;
		private int verify;
		private String imgUrl;
		private String headImage;
		private String linkUrl;
		private String adviserTypeDesc;
		
		
		public String getAdviserTypeDesc() {
			return adviserTypeDesc;
		}

		public void setAdviserTypeDesc(String adviserTypeDesc) {
			this.adviserTypeDesc = adviserTypeDesc;
		}

		public String getLinkUrl() {
			return linkUrl;
		}

		public void setLinkUrl(String linkUrl) {
			this.linkUrl = linkUrl;
		}

		public String getCompany() {
			return company;
		}

		public void setCompany(String company) {
			this.company = company;
		}

		public String getHeadImage() {
			return headImage;
		}

		public void setHeadImage(String headImage) {
			this.headImage = headImage;
		}

		public int getAdviserType() {
			return adviserType;
		}

		public void setAdviserType(int adviserType) {
			this.adviserType = adviserType;
		}

		public int getComments() {
			return comments;
		}

		public void setComments(int comments) {
			this.comments = comments;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public long getCtime() {
			return ctime;
		}

		public void setCtime(long ctime) {
			this.ctime = ctime;
		}

		public int getGrowupVal() {
			return growupVal;
		}

		public void setGrowupVal(int growupVal) {
			this.growupVal = growupVal;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getLimits() {
			return limits;
		}

		public void setLimits(int limits) {
			this.limits = limits;
		}

		public String getLinkTitle() {
			return linkTitle;
		}

		public void setLinkTitle(String linkTitle) {
			this.linkTitle = linkTitle;
		}

		public int getPraise() {
			return praise;
		}

		public void setPraise(int praise) {
			this.praise = praise;
		}

		public int getReads() {
			return reads;
		}

		public void setReads(int reads) {
			this.reads = reads;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
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

		public int getVerify() {
			return verify;
		}

		public void setVerify(int verify) {
			this.verify = verify;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

	}

}
