package com.gome.haoyuangong.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

public class AlarmDataBean {
  private String alarmStockName;//股票名称
  private String alarmStockId;  //股票id
  private String alarmStockCode;//股票编号
  private String alarmContent;//提示内容
  private String alarmTime;//时间
  private String alarmDeviceType;
  private String alarmCeil;
  private String alarmfloor;
  private String alarmRdp;
  private String alarmSeq;
  private String alarmStype;
  
  public String getAlarmStype() {
    return alarmStype;
  }
  public void setAlarmStype(String alarmStype) {
    this.alarmStype = alarmStype;
  }
  public String getAlarmStockName() {
    return alarmStockName;
  }
  public String getSubStockName(){
    if(alarmStockName!=null&&alarmStockName.length()>6){
      return alarmStockName.substring(0, 6);
    }
    return alarmStockName;
  }
  public void setAlarmStockName(String alarmStockName) {
    this.alarmStockName = alarmStockName;
  }
  public String getAlarmStockId() {
    return alarmStockId;
  }
  public void setAlarmStockId(String alarmStockId) {
    this.alarmStockId = alarmStockId;
  }
  public String getAlarmStockCode() {
    return alarmStockCode;
  }
  public void setAlarmStockCode(String alarmStockCode) {
    this.alarmStockCode = alarmStockCode;
  }
  public String getAlarmDeviceType() {
    return alarmDeviceType;
  }
  public void setAlarmDeviceType(String alarmDeviceType) {
    this.alarmDeviceType = alarmDeviceType;
  }
  public String getAlarmCeil() {
    return alarmCeil;
  }
  public void setAlarmCeil(String alarmCeil) {
    this.alarmCeil = alarmCeil;
  }
  public String getAlarmfloor() {
    return alarmfloor;
  }
  public void setAlarmfloor(String alarmfloor) {
    this.alarmfloor = alarmfloor;
  }
  public String getAlarmRdp() {
    return alarmRdp;
  }
  public void setAlarmRdp(String alarmRdp) {
    this.alarmRdp = alarmRdp;
  }
  public String getAlarmSeq() {
    return alarmSeq;
  }
  public void setAlarmSeq(String alarmSeq) {
    this.alarmSeq = alarmSeq;
  }
  public String getAlarmContent() {
    return alarmContent;
  }
  public void setAlarmContent(String alarmContent) {
    this.alarmContent = alarmContent;
  }
  public String getAlarmTime() {
    return alarmTime;
  }
  public void setAlarmTime(String alarmTime) {
    this.alarmTime = alarmTime;
  }
  
  
  @SuppressLint("NewApi")
  public static List<AlarmDataBean> creatDataFromJSON(String json){
    List<AlarmDataBean> rtData=null;
    try {
      JSONObject object = new JSONObject(json);
      JSONArray array = object.getJSONArray("stockList");
      rtData = new ArrayList<AlarmDataBean>();
      for(int i=0;i<array.length();i++){
        AlarmDataBean data = new AlarmDataBean();
        object = array.getJSONObject(i);
        data.setAlarmTime(object.getString("ctime"));//时间
        
        
        JSONArray arrayT = object.getJSONArray("remindTypes");
        parseRemindData(arrayT,data);
        data.setAlarmStype(object.getString("sType"));
        data.setAlarmSeq(object.getString("seq"));
        data.setAlarmStockCode(object.getString("stockCode"));
        data.setAlarmStockName(object.getString("stockName"));
        rtData.add(data);
      }
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return rtData;
  }
  
  public static void parseRemindData(JSONArray array, AlarmDataBean data) {
    JSONObject object;
    try {
      for (int i = 0; i < array.length(); i++) {
        object = array.getJSONObject(i);
        if("ceil".equals(object.getString("remindkey"))){
          data.setAlarmCeil(object.getString("remindvalue"));
        }else if("floor".equals(object.getString("remindkey"))){
          data.setAlarmfloor(object.getString("remindvalue"));
        }else if("rdp".equals(object.getString("remindkey"))){
          data.setAlarmRdp(object.getString("remindvalue"));
        }
      }
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  
}
