package com.gome.haoyuangong.net.result.search;

public class SearchContentData {
	
	
	private int id;  // 主键id
	private String userId;  // 用户id
	private String userName;  // 用户姓名
	private String headImage;//头像
	private int vefify;//是否加V（验证）默认都是1
	private int adwiserType;//2,为投顾、1为财经名人
	private int growupVal;//用户等级（级别、成长值）
	private String company;//从业机构
	private int type;   //  类型
	private String typeDesc;  // 类型描述
	private String title;  // 标题
	private String summary;  // 摘要
	private String imgUrl;//第一张图片url
	private String linkUrl;//文章链接地址
	private String content;   //  内容
	private long ctime;  //  发表时间
	private int praise;  //  点赞数
	private int reads;   // 阅读数
	private int comments;//评论数
	private int limits;  // 权限（1、公开 2、秘密）
	private int status;  //状态 （0、删除 1、保存 2、发布 3、审核通过）
	
	private String adviserTypeDesc;
	private String thumbnailurl;
	
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
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
	public int getLimits() {
		return limits;
	}
	public void setLimits(int limits) {
		this.limits = limits;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public int getVefify() {
		return vefify;
	}
	public void setVefify(int vefify) {
		this.vefify = vefify;
	}
	public String getHeadImage() {
		return headImage;
	}
	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
	public int getAdwiserType() {
		return adwiserType;
	}
	public void setAdwiserType(int adwiserType) {
		this.adwiserType = adwiserType;
	}
	public int getGrowupVal() {
		return growupVal;
	}
	public void setGrowupVal(int growupVal) {
		this.growupVal = growupVal;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	public int getComments() {
		return comments;
	}
	public void setComments(int comments) {
		this.comments = comments;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAdviserTypeDesc() {
		return adviserTypeDesc;
	}
	public void setAdviserTypeDesc(String adviserTypeDesc) {
		this.adviserTypeDesc = adviserTypeDesc;
	}
	public String getThumbnailurl() {
		return thumbnailurl;
	}
	public void setThumbnailurl(String thumbnailurl) {
		this.thumbnailurl = thumbnailurl;
	}
	
}
