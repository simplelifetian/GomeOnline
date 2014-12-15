package com.gome.haoyuangong.net.url;

public class NetUrlLive {
	
	static public final String HOST = "http://mapi.itougu.jrj.com.cn";
	static public final String LIVE_LIST = HOST+"/live/index.php/Mobile/roomList/pz/%d/p/%d/";
	
	static public final String OPINION_LIST = HOST+"/live/index.php/Mobile/viewpointForPhone/?rid=%s&type=%s&answer_type=1&order=%s&lid=%d&num=%s";
	static public final String EXCHANGE_LIST = HOST+"/live/index.php/mobile/talkListForPhone?rid=%s&num=%s&lid=%d&type=%s";
	
	static public final String POST_REPLY = HOST+"/live/index.php/mobile/addTalk";
	
	public static final String OPINION_NEW_COUNT = HOST+"/live/index.php/Mobile/getNewAnswerCount/rid/%s/lid/%d";
	
}
