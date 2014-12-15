package com.gome.haoyuangong.net.result.tougu;

import java.util.ArrayList;

import com.gome.haoyuangong.net.result.TouguBaseResult;

public class HotOpinionListBean extends TouguBaseResult {

	
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
	
//	public static class OpinionItem{
//		
//		private int id;
//		private int adviserId;
//		private String userName;
//		private int isVerify;
//		private int growupVal;
//		private int category;
//		private String company;
//		private String headimage;
//		private int type;
//		private String typeDesc;//所属类型,1:A股,2:港股,3:美股,4:贵金属,5:理财
//		private int limits;//权限, 1:公开,2:私密
//		private String title;
//		private String summary;
//		private String pointPic;
//		private String ctime;
//		private String comment;
//		
//		public int getId() {
//			return id;
//		}
//		public void setId(int id) {
//			this.id = id;
//		}
//		public int getAdviserId() {
//			return adviserId;
//		}
//		public void setAdviserId(int adviserId) {
//			this.adviserId = adviserId;
//		}
//		public String getUserName() {
//			return userName;
//		}
//		public void setUserName(String userName) {
//			this.userName = userName;
//		}
//		public int getIsVerify() {
//			return isVerify;
//		}
//		public void setIsVerify(int isVerify) {
//			this.isVerify = isVerify;
//		}
//		public int getGrowupVal() {
//			return growupVal;
//		}
//		public void setGrowupVal(int growupVal) {
//			this.growupVal = growupVal;
//		}
//		public int getCategory() {
//			return category;
//		}
//		public void setCategory(int category) {
//			this.category = category;
//		}
//		public String getCompany() {
//			return company;
//		}
//		public void setCompany(String company) {
//			this.company = company;
//		}
//		public String getHeadimage() {
//			return headimage;
//		}
//		public void setHeadimage(String headimage) {
//			this.headimage = headimage;
//		}
//		public int getType() {
//			return type;
//		}
//		public void setType(int type) {
//			this.type = type;
//		}
//		public String getTypeDesc() {
//			return typeDesc;
//		}
//		public void setTypeDesc(String typeDesc) {
//			this.typeDesc = typeDesc;
//		}
//		public int getLimits() {
//			return limits;
//		}
//		public void setLimits(int limits) {
//			this.limits = limits;
//		}
//		public String getTitle() {
//			return title;
//		}
//		public void setTittle(String title) {
//			this.title = title;
//		}
//		public String getSummary() {
//			return summary;
//		}
//		public void setSummary(String summary) {
//			this.summary = summary;
//		}
//		public String getPointPic() {
//			return pointPic;
//		}
//		public void setPointPic(String pointPic) {
//			this.pointPic = pointPic;
//		}
//		public String getCtime() {
//			return ctime;
//		}
//		public void setCtime(String ctime) {
//			this.ctime = ctime;
//		}
//		public String getComment() {
//			return comment;
//		}
//		public void setComment(String comment) {
//			this.comment = comment;
//		}
//		
//	}
	
	public static class OpinionItem{
		private int adviserType;
		private int comments;
		private String content;
		private long ctime;
		private int growupVal;
		private int id;
		private int limits;
		private String linkTitle;
		private String detailUrl;
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
		private String thumbnailurl;
		
		private TouguUserBean userInfo = new TouguUserBean();

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
		
		public String getDetailUrl() {
			return detailUrl;
		}

		public void setDetailUrl(String detailUrl) {
			this.detailUrl = detailUrl;
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

		public TouguUserBean getUserInfo() {
			return userInfo;
		}

		public void setUserInfo(TouguUserBean userInfo) {
			this.userInfo = userInfo;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public String getThumbnailurl() {
			return thumbnailurl;
		}

		public void setThumbnailurl(String thumbnailurl) {
			this.thumbnailurl = thumbnailurl;
		}
		
	}
	
}
