package com.gome.haoyuangong.net.url;

public class NetUrlComment {
	
	static public final String HOST = "http://news.comments.jrj.com.cn";
	static public final String COMMENT_LIST = HOST+"/index.php/commentslist?appId=%s&appItemid=%d&pageSize=%d&page=%d&frm=1";
	static public final String COMMENT_ACTION = HOST+"/index.php/comment";
	static public final String COMMENT_COUNT = HOST+"/index.php/commentscount?appId=%s&appItemid=%d&frm=";
}
