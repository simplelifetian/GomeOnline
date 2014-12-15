package com.gome.haoyuangong.net.result.tougu;

import java.util.ArrayList;
import java.util.List;

import com.gome.haoyuangong.net.result.CommentBaseResult;

public class CommentListBean extends CommentBaseResult {
	
	private int totalCount;
	private int pageSize;
	private int page;
	private int totalPage;

	private List<CommentData[]> listData = new ArrayList<CommentData[]>();
	
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<CommentData[]> getListData() {
		return listData;
	}

	public void setListData(List<CommentData[]> listData) {
		this.listData = listData;
	}

	public static class CommentData{
		private String id;
		private String appId;
		private String appItemid;
		private String senderId;
		private String senderName;
		private int type;
		private String content;
		private String replyToid;
		private String replyRootid;
		private String district;
		private long ctime;
		private String headPic;
		
		private String city;
		private String company;
		private int growupVal;
		private String headImage;
		private int isAdviser;
		private String passportName;
		private String position;
		private String province;
		private int relationStatus;
		private int signV;
		private String typeDesc;
		private String userId;
		private String userName;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getAppId() {
			return appId;
		}
		public void setAppId(String appId) {
			this.appId = appId;
		}
		public String getAppItemid() {
			return appItemid;
		}
		public void setAppItemid(String appItemid) {
			this.appItemid = appItemid;
		}
		public String getSenderId() {
			return senderId;
		}
		public void setSenderId(String senderId) {
			this.senderId = senderId;
		}
		public String getSenderName() {
			return senderName;
		}
		public void setSenderName(String senderName) {
			this.senderName = senderName;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getReplyToid() {
			return replyToid;
		}
		public void setReplyToid(String replyToid) {
			this.replyToid = replyToid;
		}
		public String getReplyRootid() {
			return replyRootid;
		}
		public void setReplyRootid(String replyRootid) {
			this.replyRootid = replyRootid;
		}
		public String getDistrict() {
			return district;
		}
		public void setDistrict(String district) {
			this.district = district;
		}
		public long getCtime() {
			return ctime;
		}
		public void setCtime(long ctime) {
			this.ctime = ctime;
		}
		public String getHeadPic() {
			return headPic;
		}
		public void setHeadPic(String headPic) {
			this.headPic = headPic;
		}
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
		public String getPassportName() {
			return passportName;
		}
		public void setPassportName(String passportName) {
			this.passportName = passportName;
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
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
		public int getSignV() {
			return signV;
		}
		public void setSignV(int signV) {
			this.signV = signV;
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
		
	}
}
