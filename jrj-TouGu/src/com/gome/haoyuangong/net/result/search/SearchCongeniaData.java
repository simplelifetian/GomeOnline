package com.gome.haoyuangong.net.result.search;

public class SearchCongeniaData {
	private String userId;  //用户id
	private String userName;  //用户姓名
	private String headImage;  //头像
	private int type;  //2、投顾 1、财经名人
	private int verify;  //是否加v（验证）默认都是1
	private int growupVal;  // 用户等级
	private String position;  //职位等级
	private int experienceScope;  //从业年限1代表1年以下，2代表1-3年，3代表3-5年，4代表5年以上
	private String company;  //从业机构
	private String investDirection;  //擅长投资方向
	private float useSatisfaction;  //满意度
	private String fansNum;  //粉丝数
	private String replyNum;  //回到数
	private int viewNum;//观点数
	private int relation;//与浏览者关系：0无关系，4关注，5签约
	private String status;//
	private int sex;//
	private String typeDesc;
	
	
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
	public String getHeadImage() {
		return headImage;
	}
	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getVerify() {
		return verify;
	}
	public void setVerify(int verify) {
		this.verify = verify;
	}
	public int getGrowupVal() {
		return growupVal;
	}
	public void setGrowupVal(int growupVal) {
		this.growupVal = growupVal;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getInvestDirection() {
		return investDirection;
	}
	public void setInvestDirection(String investDirection) {
		this.investDirection = investDirection;
	}
	public String getFansNum() {
		return fansNum;
	}
	public void setFansNum(String fansNum) {
		this.fansNum = fansNum;
	}
	public String getReplyNum() {
		return replyNum;
	}
	public void setReplyNum(String replyNum) {
		this.replyNum = replyNum;
	}
	

	public float getUseSatisfaction() {
		return useSatisfaction;
	}
	public void setUseSatisfaction(int useSatisfaction) {
		this.useSatisfaction = useSatisfaction;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public int getExperienceScope() {
		return experienceScope;
	}
	public void setExperienceScope(int experienceScope) {
		this.experienceScope = experienceScope;
	}

	public int getViewNum() {
		return viewNum;
	}
	public void setViewNum(int viewNum) {
		this.viewNum = viewNum;
	}

	public int getRelation() {
		return relation;
	}
	public void setRelation(int relation) {
		this.relation = relation;
	}

	public static class ContentData {
		
	}

}
