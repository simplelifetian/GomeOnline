package com.gome.haoyuangong.net.url;

public class NetUrlLoginAndRegist {
	static public final String HOST = "https://sso.jrj.com.cn";
	static public final String CHECK_PHONE = HOST+"/sso/passport/mobileExist.jsp";
	static public final String GET_VCODE = HOST+"/sso/passport/sendVerifyCode.jsp";
	static public final String VERIFY_VCODE = HOST+"/sso/passport/verifyCode.jsp";
	static public final String RIGIST = HOST+"/sso/passport/updatePassportMobile.jsp";
	static public final String RIGIST_AND_LOGIN = HOST+"/sso/passport/addUserPassportApp.jsp";
	
	static public final String LOGIN = HOST+"/sso/passport/appLoginReturnAccessToken.jsp";
	static public final String UPDATE_HEAD = "http://i.jrj.com.cn/pass/saveHeadPicApp.jspa";
	
	static public final String GET_ACCESS_TOKEN = HOST+"/sso/passport/getAccessToken.jsp";
	
	static public final String UPLOAD_HOST = "http://upfile.jrj.com.cn";
	static public final String UPLOAD_HOST_TEST	 = "http://i5.jrjimg.cn";
	static public final String UPLOAD_URL = UPLOAD_HOST+"/upload";
	static public final String UPLOAD_IDENTITY_URL = "http://up3.jrj.com.cn/upload";
	static public final String UPLOAD_TOUGU_HEAD_URL = "http://up1.jrj.com.cn/upload";
	static public final String UPLOAD_PREFIX = "http://itg1.jrjimg.cn";
	
	static public final String USER_AGREEMENT = "http://itougu.jrj.com.cn/site/user_agreement.html";
}
