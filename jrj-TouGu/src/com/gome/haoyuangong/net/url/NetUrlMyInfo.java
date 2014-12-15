package com.gome.haoyuangong.net.url;

public class NetUrlMyInfo {
	static public final String HOST = "http://mapi.itougu.jrj.com.cn";
	//投顾个人管理
	static public final String INVESTERSELFMANAGEURL = HOST+"/wireless/account/adviserManager/%s";
	static public final String INVESTERHOME = HOST+"/wireless/account/adviserPage/%s?userId=%s";
	//投顾列表
	static public final String INVESTERLISTURL = HOST+"/wireless/account/%s";
	//投顾的基本信息接口
	static public final String INVESTERINFOURL = HOST+"/wireless/account/userBasicInfo/%s";
	//投顾的关注列表接口
	static public final String ATTENTIONSURL = HOST+"/wireless/account/focusList/%s?pageId=%s&d=%s&ps=%s";
	static public final String ATTENTION = HOST+"/wireless/account/focus/%s/%s/%d";
	//用户看投顾的信息接口
	static public final String USERVIEWADVISERURL = HOST+"/wireless/account/adviserInfo?userId=%s&adviserId=%s";
	
	
	//signFlag表示是否签约，1表示是，0表示取消
	static public final String SIGNURL = HOST+"/wireless/account/sign/%s/%s/%d";
	static public final String SIGN_VERIFY_URL = HOST+"/wireless/account/signVerify/%s/%s";
	//签约用户列表
	static public final String SIGNERLISTURL = HOST+"/wireless/account/signList/%s?pageId=%s&d=%s&ps=%s";
	
	
	//粉丝列表
	static public final String FUNSLISTURL = HOST+"/wireless/account/fansList/%s?pageId=%s&d=%s&ps=%s";
	//粉丝用户详情接口(区分3种身份)
	static public final String FUNSINFOURL = HOST+"/wireless/account/userInfo?userid=%s";
	
	//普通用户信息
	static public final String USERINFOURL = HOST+"/wireless/account/userPage/%s";
	//普通用户管理
	static public final String USERMANAGEURL = HOST+"/wireless/account/userManage/%s";
	//投顾回答记录
	static public final String ANSWERDED_ADIVISTER = HOST+"/wireless/ques/myanswer/%s/%d?ps=%d&d=%s&%s";
	//投顾回答
	static public final String ADVISER_ANSWER_USER = HOST+"/wireless/ques/answer";
	//用户咨询记录
	static public final String USER_ANSWER = HOST+"/wireless/ques/advice/%s/%d?ps=%d&d=%s&%s";
	
	//普通用户申请投顾获取基本信息接口
	static public final String USERINFOFORAPPLY = HOST+"/wireless/account/userInfoForApply/%S";
	//更新投顾申请接口
	static public final String APPLYADVISERINFO = HOST+"/wireless/account/adviserApply";
	
	static public final String AdvisorCertification = HOST+"/wireless/account/userFlagForApply/%s";
	
	static public final String CHECKSIGNED = HOST+"/wireless/account/signVerify/%s";
	//投资符合列表
	static public final String INVESTGROUPLIST = HOST+"/wireless/portfolio/list/%s";
	//投资组合持仓
	static public final String INVESTGROUPLISTPOSITION = HOST+"/wireless/portfolio/positions/%s/%d";
	//追问
	static public final String AGAINASK = HOST+"/wireless/ques/againask";
	//评价
	static public final String EVALUATE = HOST+"/wireless/ques/evaluate";
	
	static public final String SIGNVERIFY = HOST+"/wireless/account/signVerify/%s/%s";
}
