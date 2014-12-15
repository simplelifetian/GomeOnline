package com.gome.haoyuangong.net.url;

public class NetUrlTougu {
	
	static public final String HOST = "http://mapi.itougu.jrj.com.cn";
	static public final String OPINION_LIST = HOST+"/wireless/view/label/%s";
	static public final String OPINION_DETAIL = HOST+"/wireless/view/%d";
	static public final String OPINION_PRAISE = HOST+"/wireless/view/praise/%d?currUserid=%s&adUserid=%s";
	
	static public final String OPINION_POST = HOST+"/wireless/view";
	static public final String OPINION_GUANZHU = HOST+"/wireless/account/dynamic/_userid?pageId=_pointId&ps=_pageSize&d=_direction";
	static public final String OPINION_GUANDIAN = HOST+"/wireless/account/pointList/_pageSize/_direction/_pointId";
	public static final String OPINION_HOT = HOST+"/wireless/view/label/_label?viewid=_pointId&ps=_pageSize&d=_direction";
	public static final String OPINION_ADVISER = HOST+"/wireless/view/list/_userid?viewid=_pointId&ps=_pageSize&d=_direction";
	
	public static final String OPTION_LIMIT = HOST+ "/wireless/view/prepose/%d?currUserid=%s&adUserid=%s&limit=%d";
	
	public static final String USER_IDENTIFY = HOST+"/wireless/account/userIdentifyInfo/%s";
	public static final String OPINION_HOT_LABELS = HOST+"/wireless/view/hotlabel?ps=%d";
	
	public static final String OPINION_CONSULTING = HOST+"/wireless/ques/ask";
	
	public static final String OPINION_CONSULTING_TIMES = HOST+"/wireless/ques/ableAsk/%s";
	
	public static final String JUBAO = HOST+"/wireless/report/_adviserId/_deviceId/_contentId/_contentType/_reasonId/_reasonValue/";
	
	public static final String MY_LIVE_LIST = HOST+"/wireless/message/_userid/4";
	
	public static final String MY_NEW_MESSAGE_LIST = HOST+"/wireless/message/mit/_userid";
	
	public static final String MESSAGE_LIST = HOST+"/wireless/message/_userid/_mtype?id=_id&ps=_ps&b=_b";
	
	public static final String DONGTAI_HOT = HOST+"/wireless/fragment/hotViewFragment.jspa";
	
	public static final String BIAOQING = "http://static.91fool.com.cn/images/";
	
}
