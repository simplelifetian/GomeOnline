package com.gome.haoyuangong.net.url;

public class MyStockUrl {
	
	public static final String base_interface_url = "http://mapi.jrj.com.cn/";
	public static final String myStock_ListURL = base_interface_url+"q_jrjimg/?%s";
	public static final String stcok_tradeMxURL = "http://data.share.jrj.com.cn/mx.do?code=%s&page=1&size=20";
	public static final String stcok_search_opinion = "http://mapi.itougu.jrj.com.cn/wireless/search/?type=view&keyword=%s,%s&from=%s&size=10";
	
	public static final String Check_stockDic_URL = "http://code.jrjimg.cn/client?key=%s";
	//预警
	public static final String ALARM_REQUEST_LIST_URL = base_interface_url+"stock/action/stockremind/listStocks.jspa?userid=%s";
	public static final String ALARM_ADD_ALARM_URL = base_interface_url+"stock/action/stockremind/insertStock.jspa";
	public static final String ALARM_DELETE_ALARM_URL = base_interface_url+"stock/action/stockremind/deleteStock.jspa?seq=%s";
	public static final String ALARM_DELETE_ALARM_URL2 = base_interface_url+"stock/action/stockremind/deleteUserRemindStock.jspa?userid=%s&stockCode=%s&sType=%s";
	public static final String ALARM_REQUEST_DATA_URL = base_interface_url+"stock/action/stockremind/getUserRemindByCode.jspa?userid=%s&stockCode=%s&sType=%s";
}
