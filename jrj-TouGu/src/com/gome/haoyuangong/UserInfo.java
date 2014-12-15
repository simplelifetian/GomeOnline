package com.gome.haoyuangong;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

//import com.jrj.stock.trade.logs.Logger;

import com.gome.haoyuangong.db.QuoteDic;
import com.gome.haoyuangong.db.RecordStoreManager;
import com.gome.haoyuangong.net.result.tougu.TouguUserBean;
import com.gome.haoyuangong.utils.StringUtils;

public class UserInfo {
	
	public static final String LOGINED_USER_INFO = "LOGINED_USER_INFO";
	
	private static UserInfo instance;
	private String passportId;
	private String userId = "default";
	private String userName;
	private String deivceId;
	private String headPath;
	
	private String loginToken;
	private String accessToken;
	private int isAdviser;
	private String mobile;
	private String loginName;
	
	private TouguUserBean touguUserBean = new TouguUserBean();
	
	private UserInfo(){
		
	}
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
//		AppInfo.jrjUserName=loginName;
		this.loginName = loginName;
	}

	public static String getLoginedUserInfo() {
		return LOGINED_USER_INFO;
	}

	public static void setInstance(UserInfo instance) {
		UserInfo.instance = instance;
	}

	static void init(Context app){
		instance = new UserInfo();
	}

	
	public String getPassportId() {
		return passportId;
	}
	public void setPassportId(String passportId) {
		this.passportId = passportId;
	}
	public String getHeadPath() {
		return headPath;
	}
	public void setHeadPath(String headPath) {
//		AppInfo.jrjHeadUrl = headPath;
			this.headPath = headPath;
	}
	public String getDeivceId() {
		return deivceId;
	}
	public void setDeivceId(String deivceId) {
		this.deivceId = deivceId;
	}
	public String getUserId() {
		return StringUtils.isEmpty(userId) ? "default":userId;
	}
	public void setUserId(String userId) {
		if(StringUtils.isEmpty(userId)){
			userId = "default";
		}
		AppInfo.jrjUserSSoid=userId;
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
		this.touguUserBean.setUserName(userName);
	}
	static public UserInfo getInstance(){
		if(instance==null){
			throw new IllegalArgumentException("this should be inited in application first");
		}
		return instance;
	}
	
	public boolean isLogin(){
		return !StringUtils.isEmpty(accessToken);
	}
	
	public boolean isTougu(){
		return isAdviser == 1;
	}
	
	public String getLoginToken() {
		return loginToken;
	}
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
//		AppInfo.jrjToken = accessToken;
		this.accessToken = accessToken;
	}
	public void setIsAdviser(int isAdviser) {
		this.isAdviser = isAdviser;
		this.touguUserBean.setIsAdviser(isAdviser);
	}
	
	public int getIsAdviser() {
		return isAdviser;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public boolean isMySelf(String userId){
		if(isLogin()){
			if(getUserId().equals(userId)){
				return true;
			}
		}
		return false;
	}
	
	public void clearUserInfo(Context context){
		this.passportId = null;
		this.userId = "default";
		this.userName = null;
		this.deivceId = null;
		this.headPath = null;
		
		this.loginToken = null;
		this.accessToken = null;
		this.isAdviser = 0;
		this.mobile = null;
		this.touguUserBean.clear();
		SharedPreferences.Editor editor = context.getSharedPreferences(LOGINED_USER_INFO, Activity.MODE_PRIVATE).edit();
		editor.clear().commit();
		RecordStoreManager.getInstance().deleteRecordAll();
		RecordStoreManager.getInstance().close();
		com.gome.haoyuangong.AppInfo.myStockVec.removeAllElements();
		//-----
//		AppInfo.jrjToken = null;
//		AppInfo.jrjHeadUrl=null;
		AppInfo.jrjUserSSoid=null;
//		AppInfo.jrjUserName="";
	}
	SQLiteDatabase dataBase;
	private final String SEARCH_HISTORY_TAB = "searchHistoryTab";
	public boolean deleteAllHisRecord(Context ctx) {
		try {
			if (dataBase == null) {
				String path = "/data/data/" + ctx.getPackageName() + "/" + QuoteDic.DATA_NAME;
				dataBase = SQLiteDatabase.openOrCreateDatabase(path, null);
			}
			String sql = "delete from " + SEARCH_HISTORY_TAB;
			dataBase.execSQL(sql);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	public TouguUserBean getTouguUserBean() {
		return touguUserBean;
	}
	/**
	 * 设置投顾接口，参数为null 不执行操作，清除用户使用clearUserInfo(Context context)
	 * @param touguUserBean
	 */
	public void setTouguUserBean(TouguUserBean touguUserBean) {
		if(touguUserBean == null){
			return;
		}
		this.touguUserBean = touguUserBean;
		setUserId(touguUserBean.getUserId());
		setUserName(touguUserBean.getUserName());
		setHeadPath(touguUserBean.getHeadImage());
		setIsAdviser(touguUserBean.getIsAdviser());
	}
	
	public static String getUserTypeStr(int isAdviser,int type){
		return isAdviser == 1 ? type != 2 ? (type == 1 ? "财经名人" :"") : "投资顾问" : "";
	}
	
	
	public static void saveUserInfo(Context context,UserInfo userInfo){
		SharedPreferences.Editor editor = context.getSharedPreferences(LOGINED_USER_INFO, Activity.MODE_PRIVATE).edit();
		editor.putString("passportId", userInfo.getPassportId());
		editor.putString("userId", userInfo.getUserId());
		editor.putString("loginName", userInfo.getLoginName());
		editor.putString("userName", userInfo.getUserName());
		editor.putString("deivceId", userInfo.getDeivceId());
		editor.putString("headPath", userInfo.getHeadPath());
		editor.putString("loginToken", userInfo.getLoginToken());
		editor.putString("accessToken", userInfo.getAccessToken());
		editor.putInt("isAdviser", userInfo.getIsAdviser());
		editor.putString("mobile", userInfo.getMobile());
		editor.commit();
		TouguUserBean.saveTouguUserBean(context, userInfo.getTouguUserBean());
	}
	
	public static void readUserInfo(Context context,UserInfo userInfo){
		
		SharedPreferences reader = context.getSharedPreferences(LOGINED_USER_INFO, Activity.MODE_PRIVATE);
		userInfo.setPassportId(reader.getString("passportId", null));
		userInfo.setUserId(reader.getString("userId", null));
		userInfo.setLoginName(reader.getString("loginName", null));
		userInfo.setUserName(reader.getString("userName", null));
		userInfo.setDeivceId(reader.getString("deivceId", null));
		userInfo.setHeadPath(reader.getString("headPath", null));
		userInfo.setLoginToken(reader.getString("loginToken", null));
		userInfo.setAccessToken(reader.getString("accessToken", null));
		userInfo.setIsAdviser(reader.getInt("isAdviser", 0));
		userInfo.setMobile(reader.getString("mobile", null));
		
		if(userInfo.getTouguUserBean() == null){
			userInfo.setTouguUserBean(new TouguUserBean());
		}
		TouguUserBean.readTouguUserBean(context, userInfo.getTouguUserBean());
	}
	
}
