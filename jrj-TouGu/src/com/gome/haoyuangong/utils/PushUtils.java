package com.gome.haoyuangong.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.bean.AlarmDataBean;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.GetChars;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class PushUtils {
  public static final String AlarmSharePreferenceName="alarm_push_share_preference"; 
  public static final String DEVICE_TYPE="3";
  public static final String API_KEY="jrj-android";
  public static final String JRJ_ANDROID_SECRET_KEY="jrj-android-arO+uy7KR7Sj6Oyx";
  public static final String INFO_SECRET_KEY="info-eZIe7LG:6gonsmF3";
  public static final String REMIND_SECRET_KEY="remind-H(Rg03mNqT5rWciz";
  public static final String PUSH_INIT_COMPLETED="com.jrj.push.INIT.COMPLETED";//推送初始化后，发送预警刷新广播
  
  public static final String ALARM_STOCK_TYPE_GP="gp";
  public static final String ALARM_STOCK_TYPE_ZS="zs";
  public static final String ALARM_STOCK_TYPE_JJ="jj";
  
  public static final String ALARM_EMPTY_SYMBOL="null";
  
  public static final String PUSH_MSG_CHECK_FLAG="push_msg_check_flag";
  
  public static Context appContext=null;
  
  public static void setPushMsgCheckFlag(boolean isHaveNew){
    SharedPreferences sp = PreferenceManager
        .getDefaultSharedPreferences(appContext);
    sp.edit().putBoolean(PUSH_MSG_CHECK_FLAG, isHaveNew).commit();
  }
  /**
   * 
   * @return true 有新消息  false 无新消息
   */
  public static boolean getPushMsgCheckFlag(){
    SharedPreferences sp = PreferenceManager
        .getDefaultSharedPreferences(appContext);
    return sp.getBoolean(PUSH_MSG_CHECK_FLAG, false);
  }
  
//获取ApiKey
  public static String getMetaValue( String metaKey) {
      Bundle metaData = null;
      String apiKey = null;
      if (appContext == null || metaKey == null) {
        return null;
      }
      try {
          ApplicationInfo ai = appContext.getPackageManager().getApplicationInfo(
              appContext.getPackageName(), PackageManager.GET_META_DATA);
          if (null != ai) {
              metaData = ai.metaData;
          }
          if (null != metaData) {
            apiKey = metaData.getString(metaKey);
          }
      } catch (NameNotFoundException e) {

      }
      return apiKey;
  }
  
//用share preference来实现是否绑定的开关。在ionBind且成功时设置true，unBind且成功时设置false
  public static boolean hasBind() {
    SharedPreferences sp = PreferenceManager
      .getDefaultSharedPreferences(appContext);
  String flag = sp.getString("bind_flag", "");
  if ("ok".equalsIgnoreCase(flag)) {
    return true;
  }
  return false;
  }
  
  public static void setBind(boolean flag) {
    String flagStr = "not";
    if (flag) {
      flagStr = "ok";
    }
    SharedPreferences sp = PreferenceManager
        .getDefaultSharedPreferences(appContext);
    Editor editor = sp.edit();
    editor.putString("bind_flag", flagStr);
    editor.commit();
  }
  public static void setPushData(String appId,String userId,String channelId,String requestID){
    SharedPreferences sp =appContext.getSharedPreferences(AlarmSharePreferenceName, 0);
    Editor editor = sp.edit();
    editor.putString("appId", appId);
    editor.putString("userId", userId);
    editor.putString("channelId", channelId);
    editor.putString("requestID", requestID);
    editor.commit();
  }
  /**
   * 返回百度返回的channelId，请求预警消息用
   * @return
   */
  public static String getPushDeviceID(){
  	SharedPreferences sp =appContext.getSharedPreferences(AlarmSharePreferenceName, 0);
  	return sp.getString("channelId", null);
  }
  
  /**
   * 返回推送是否开启
   * @return
   */
  public static boolean isPushOpenBaidu(){
    SharedPreferences sp =appContext.getSharedPreferences(AlarmSharePreferenceName, 0);
    String flag = sp.getString("push_state_baidu","");
    if(flag.equals("od")){
      return true;
    }
    return false;
  }
  public static void setPushStateBaidu(boolean isOpen){
    String flagStr = "not";
    if (isOpen) {
      flagStr = "ok";
    }
    SharedPreferences sp =appContext.getSharedPreferences(AlarmSharePreferenceName, 0);
    Editor editor = sp.edit();
    editor.putString("push_state_baidu", flagStr);
    editor.commit();
  }
  //返回本地推送状态 全开为3
  public static int getPushState(){
    SharedPreferences sp =appContext.getSharedPreferences(AlarmSharePreferenceName, 0);
    int flag = sp.getInt("push_state",3);
    return flag;
  }
  public static void setPushState(int flag){
    SharedPreferences sp =appContext.getSharedPreferences(AlarmSharePreferenceName, 0);
    Editor editor = sp.edit();
    editor.putInt("push_state", flag);
    editor.commit();
  }
  public static final int TYPE_PUSH_STATE_INFO_BIT=0;
  public static final int TYPE_PUSH_STATE_ALARM_BIT=1;
  
  public static boolean isPushOpenByType(int type,int flag){
    int ff = 0x1;
    int rt = flag&(ff<<type);
    return rt>0;
  }
  public static void setPushOpenState(boolean infoFlag,boolean alarmFlag){
    int ff = 0x1;
    int flag = getPushState();
    if(infoFlag){
      flag = flag|(ff<<TYPE_PUSH_STATE_INFO_BIT);
    }else{
      flag = flag&~(ff<<TYPE_PUSH_STATE_INFO_BIT);
    }
    if(alarmFlag){
      flag = flag|(ff<<TYPE_PUSH_STATE_ALARM_BIT);
    }else{
      flag = flag&~(ff<<TYPE_PUSH_STATE_ALARM_BIT);
    }
    
    setPushState(flag);
  }
  
  

  /**
   * 存储股票sType+stockCode  ceil floor rdp
   * @param context
   * @param stockId 
   */
  private static void setAlarmedData(Context context,List<List<String>> stocks){
    if(stocks.size()!=4)return;
    String[] stockStrs=new String[stocks.size()];
    int i=0;
    for(List<String> stock: stocks){
      StringBuffer bf = new StringBuffer();
      for(String s: stock){
        bf.append(s);
        bf.append(",");
      }
      if(bf.length()>0){
        bf.deleteCharAt(bf.length()-1);
      }
      stockStrs[i]=bf.toString();
      i++;
    }
    SharedPreferences sp =context.getSharedPreferences(AlarmSharePreferenceName, 0);
    Editor editor = sp.edit();
    editor.putString("stockid", stockStrs[0]);
    editor.putString("stockceil", stockStrs[1]);
    editor.putString("stockfloor", stockStrs[2]);
    editor.putString("stockrdp", stockStrs[3]);
    editor.commit();
  }
  private static List<List<String>> getAlarmedData(Context context){
    List<List<String>> rtn = new ArrayList<List<String>>();
    SharedPreferences sp =context.getSharedPreferences(AlarmSharePreferenceName, 0);
    String stockIds = sp.getString("stockid", "");
    String stockCeils = sp.getString("stockceil", "");
    String stockFloors = sp.getString("stockfloor", "");
    String stockfRdp = sp.getString("stockrdp", "");
    if(stockIds!=null&&stockIds.length()>0){
      rtn.add(new ArrayList<String>(Arrays.asList(stockIds.split(","))));
      rtn.add(new ArrayList<String>(Arrays.asList(stockCeils.split(","))));
      rtn.add(new ArrayList<String>(Arrays.asList(stockFloors.split(","))));
      rtn.add(new ArrayList<String>(Arrays.asList(stockfRdp.split(","))));
    }else{
      rtn.add(new ArrayList<String>());
      rtn.add(new ArrayList<String>());
      rtn.add(new ArrayList<String>());
      rtn.add(new ArrayList<String>());
    }
    return rtn;
  }
  /**
   * 储存单条预警数据记录（有可能有相同的，则数据不变）
   * @param context
   * @param sType
   * @param stockCode
   */
  public static void addAlarmedData(String sType,String stockCode,String ceil,String floor,String rdp){
    if(sType==null||sType.length()<=0||stockCode==null||stockCode.length()<=0){
      return;
    }
    String target = sType+stockCode;
    if(ceil==null||ceil.length()==0){
      ceil=ALARM_EMPTY_SYMBOL;
    }
    if(floor==null||floor.length()==0){
      floor=ALARM_EMPTY_SYMBOL;
    }
    if(rdp==null||rdp.length()==0){
      rdp=ALARM_EMPTY_SYMBOL;
    }
    List<List<String>> list = getAlarmedData(appContext);
    int index = list.get(0).indexOf(target);
    if(index>=0&&index<list.get(0).size()){
      list.get(1).set(index, ceil);
      list.get(2).set(index,floor);
      list.get(3).set(index,rdp);
    }else{
      list.get(0).add(target);
      list.get(1).add(ceil);
      list.get(2).add(floor);
      list.get(3).add(rdp);
    }
    setAlarmedData(appContext,list);
  }
  /**
   * 删除单条预警数据记录
   * @param context
   * @param sType
   * @param stockCode
   */
  public static void deleteAlarmedData(String sType,String stockCode){
    String target = sType+stockCode;
    List<List<String>> datas = getAlarmedData(appContext);
    List<String> item = datas.get(0);
    int index = item.indexOf(target);
    if(index>=0&&index<item.size()){
      item.remove(index);
      datas.get(1).remove(index);
      datas.get(2).remove(index);
      datas.get(3).remove(index);
    }
    setAlarmedData(appContext,datas);
  }
  
  public static boolean isMyAlarmedData(String sType,String stockCode){
    String target = sType+stockCode;
    List<List<String>> list = getAlarmedData(appContext);
    if(list.get(0).contains(target)){
      return true;
    }
    return false;
  }
  /**
   * 返回存储的预警数据
   * @param sType
   * @param stockCode
   * @return list 0:ceil  1:floor  2:rdp
   */
  public static List<String> getAlarmedData(String sType,String stockCode){
    String target = sType+stockCode;
    List<String> rtn = new ArrayList<String>();
    List<List<String>> list = getAlarmedData(appContext);
    int index  =list.get(0).indexOf(target);
    if(index>=0&&index<list.get(0).size()){
      String s = list.get(1).get(index);
      if(s.equals(ALARM_EMPTY_SYMBOL)){
        rtn.add("");
      }else{
        rtn.add(s);
      }
      s = list.get(2).get(index);
      if(s.equals(ALARM_EMPTY_SYMBOL)){
        rtn.add("");
      }else{
        rtn.add(s);
      }
      s = list.get(3).get(index);
      if(s.equals(ALARM_EMPTY_SYMBOL)){
        rtn.add("");
      }else{
        rtn.add(s);
      }
    }
    return rtn;
  }
  
  public static void updateAlarmedData(List<AlarmDataBean> list){
    cleanAlarmedData();
    for(AlarmDataBean b :list){
      addAlarmedData(b.getAlarmStype(), b.getAlarmStockCode(), b.getAlarmCeil(), b.getAlarmfloor(), b.getAlarmRdp());
    }
  }
  public static void cleanAlarmedData(){
    SharedPreferences sp =appContext.getSharedPreferences(AlarmSharePreferenceName, 0);
    Editor editor = sp.edit();
    editor.putString("stockid", "");
    editor.putString("stockceil", "");
    editor.putString("stockfloor", "");
    editor.putString("stockrdp", "");
    editor.commit();
  }
  
  
  /**
   * 从stock中的类型得到预警中相应类型
   * @param stockType
   * @return默认类型：股票
   */
  public static String getAlarmTypeFromStockType(String stockType){
    String stockGp = appContext.getString(R.string.alarm_stock_type_gp);
    String stockZs = appContext.getString(R.string.alarm_stock_type_zs);
    String stockJj = appContext.getString(R.string.alarm_stock_type_jj);
    
    String rtn = ALARM_STOCK_TYPE_GP;
    if(stockType!=null){
      if(stockType.startsWith(stockGp)){
        rtn = ALARM_STOCK_TYPE_GP;
      }else if(stockType.equals(stockZs)){
        rtn = ALARM_STOCK_TYPE_ZS;
      }else if(stockType.startsWith(stockJj)){
        rtn = ALARM_STOCK_TYPE_JJ;
      }
    }
    return rtn;
  }
  /**
   * 从预警中中的类型得到stock相应类型
   * @param stockType
   * @return 默认类型：股票
   */
  public static String getStockTypeFromAlarmType(String alarmSType){
    String stockGp = appContext.getString(R.string.alarm_stock_type_gp);
    String stockZs = appContext.getString(R.string.alarm_stock_type_zs);
    String stockJj = appContext.getString(R.string.alarm_stock_type_jj);
    String rtn = stockGp;
    if(alarmSType!=null){
      if(alarmSType.equals(ALARM_STOCK_TYPE_GP)){
        rtn = "s.sa";
      }else if(alarmSType.equals(ALARM_STOCK_TYPE_ZS)){
        rtn = "i";
      }else if(alarmSType.equals(ALARM_STOCK_TYPE_JJ)){
        rtn = "f.fc.fcs";
      }
    }
    return rtn;
  }
  /**
   * 返回list中特定seq的index
   * @param data
   * @param seq
   * @return
   */
  public static int getAlarmIndexBySeq(List<AlarmDataBean> data,String seq){
    for(int i=0;i<data.size();i++){
      if(data.get(i).getAlarmSeq()!=null&&data.get(i).getAlarmSeq().equals(seq)){
        return i;
      }
    }
    return -1;
  }
  
  
  
  public static void startPush(){
    if (!PushUtils.hasBind()) {
//      PushManager.startWork(appContext,
//          PushConstants.LOGIN_TYPE_API_KEY, 
//          PushUtils.getMetaValue("api_key"));
    }
  }
  public static void resumePush(){
//    if (!PushUtils.hasBind()) {
//      PushManager.startWork(appContext,
//          PushConstants.LOGIN_TYPE_API_KEY, 
//          PushUtils.getMetaValue("api_key"));
//    }else if(!PushManager.isPushEnabled(appContext)){
//      PushManager.resumeWork(appContext);
//    }
  }
  public static void stopPush(){
//    if(PushManager.isPushEnabled(appContext)){
//      PushManager.stopWork(appContext);
//    }
  }
  public static boolean isPushEnabled(){
//    return PushManager.isPushEnabled(appContext);
  	return false;
  }
  
  /**
   * 返回utf-8格式字符
   * @param str
   * @return
   */
  public static String getUTF8String(String str){
    try {
      return new String(str.getBytes("urf-8"));
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
  
  public static String calculateSign(List<NameValuePair> params,String key){
    TreeMap<String, String> map = new TreeMap<String, String>();
    for(NameValuePair p: params){
      map.put(p.getName(), p.getValue());
    }
    return Md5Util.md5Signature(map, key);
//    if(params==null)return "";
//    Collections.sort(params, new Comparator<NameValuePair>() {
//      @Override
//      public int compare(NameValuePair lhs, NameValuePair rhs) {
//        return lhs.getName().compareTo(rhs.getName());
//      }
//    });
//    System.out.println(params);
//    StringBuffer bf =new StringBuffer();
//    for(NameValuePair p:params){
//      bf.append(URLEncoder.encode(p.getName())+URLEncoder.encode(p.getValue()));
//    }
////    bf.insert(0, JRJ_ANDROID_SECRET_KEY);
//    bf.insert(0, "info-eZIe7LG:6gonsmF3");
//    System.out.println(bf.toString());
//    String sign = md5(bf.toString());
//    System.out.println(sign);
//    return sign;
  }
  
  
  public static char[] HEX_DIGITS = { '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};  

  /**
   * helper function : 将内存二进制流转换为16进制的字串
   * 
   * @param b  二进制流
   *           
   * @return 对应的16进制的字串
   */
  private static String toHexString(byte[] b) { // String to byte

    StringBuilder sb = new StringBuilder(b.length * 2);
    for (int i = 0; i < b.length; i++) {
      sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
      sb.append(HEX_DIGITS[b[i] & 0x0f]);
    }
    return sb.toString().toLowerCase();
  }

  /**
   * 计算md5值
   * @param s 源字串
   * @return md5字串
   */
  public static String md5(String s) {
    try {
      MessageDigest digest = java.security.MessageDigest
          .getInstance("MD5");
      digest.update(s.getBytes());
      byte messageDigest[] = digest.digest();

      return toHexString(messageDigest);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }
  static SimpleDateFormat formatR=new SimpleDateFormat("MM-dd HH:mm");
  static SimpleDateFormat formatRH=new SimpleDateFormat("HH:mm");
  static SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  static SimpleDateFormat formatDay=new SimpleDateFormat("yyyy-MM-dd");
  
  /**
   * 修改当前文字时间格式
   */
  public static String dealTimeFormat(String a_timeStr){
    String result = "";
    if(a_timeStr==null||"".equals(a_timeStr)){
      return result;
    }
    try {
      Calendar calendar = Calendar.getInstance();
      String todayStr = formatDay.format(calendar.getTime());
      calendar.add(Calendar.DAY_OF_YEAR, -1);
      String yestodayStr = formatDay.format(calendar.getTime());
      int timeStrLen = "2013-07-23 09:36:00".length();
      Date dateT = format.parse(a_timeStr);
      if(a_timeStr.indexOf(todayStr)!=-1){
        return "今天 "+formatRH.format(dateT);
      }else if(a_timeStr.indexOf(yestodayStr)!=-1){
        return "昨天 "+formatRH.format(dateT);
      }else{
        int len = a_timeStr.length();
        if(len == timeStrLen){
          result = formatR.format(dateT);
        }
        else{
          result = a_timeStr;
        }
      }
      
    } catch (Exception e) {
      result = a_timeStr;
    }
    return result;
  }
}
