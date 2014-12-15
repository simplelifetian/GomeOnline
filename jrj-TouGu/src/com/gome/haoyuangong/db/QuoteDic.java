package com.gome.haoyuangong.db;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;
import android.preference.PreferenceManager;

import com.gome.haoyuangong.AppInfo;
import com.gome.haoyuangong.BaseViewImpl;
import com.gome.haoyuangong.R;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.url.MyStockUrl;
import com.gome.haoyuangong.net.volley.StringRequest;

public class QuoteDic extends Thread{
  public static final String Property_STOCKId = "stid";///常量 市场ID
  public static final String Property_MarketId = "marketid";///常量 市场ID
  public static final String Property_StockCode = "stcode";///常量 股票代码
  public static final String Property_StockName = "stname";///常量 股票名称
  public static final String Property_StockPY = "stpy";/// 常量 股票拼音
  public static final String Property_StockType = "sttype";/// 常量 股票拼音

  public static final String DATA_NAME =  "jrjstockdic.db";///常量 数据库名称
  public static final String TABLE_NAME = "dicTab";/// 常量 股票信息表名称
  private final String version = "versionTab";/// 常量 版本号表名称
  private final String property_Version = "VerisonMD5";/// 常量 版本号MD5 信息字段名
  private SQLiteDatabase dataBase;/// sqlite数据库变量
  SimpleDateFormat formatDateYMD = new SimpleDateFormat("yyyyMMdd");
  private Context ctx;
  static public void copyDatabase(final Context ctx){
  	new Thread(){
  		public void run() {
  			copyDatabase(ctx,DATA_NAME,R.raw.jrjstockdic);
  		};
  	}.start();
  }
  
  public QuoteDic(Context ctx){
  	this.ctx = ctx;
  }
  public void checkTime(){
  	if(!getCheckDicTime().equals(formatDateYMD.format(new Date()))){
  		start();
  	}
  }
  /**
   * 线程 启动 获取历史版本号信息
   * 发送网络请求，根据返回数据判断是否更新本地股票信息
   * 同时更新版本号信息
   */
  public void run(){
  	Looper.prepare();
    try {
      dataBase = openDatabase(DATA_NAME);
      //获取版本号
      Cursor oCursor = dataBase.rawQuery("SELECT * FROM " + version, null);
      StringBuffer sb = new StringBuffer();
      for(oCursor.moveToFirst();!oCursor.isAfterLast();oCursor.moveToNext())
      {
        int index = oCursor.getColumnIndex(property_Version);
        sb.append(oCursor.getString(index));
      }
      oCursor.close();
      String url = String.format(MyStockUrl.Check_stockDic_URL, sb.toString());
      doHttpPostRequest(url);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Looper.loop();
  }
  private void doHttpPostRequest(String url) {
    Request request =new StringRequest(Method.GET, url, new RequestHandlerListener<String>(ctx) {
			@Override
			public void onSuccess(String id, String data) {
				fillData(data);
			}
		});
    ((BaseViewImpl)ctx).send(request); 
  }
  private void fillData(String content){
    try {
      if(content!=null&&content.length()>0){
        AppInfo.isUpdateStockDic = true;
        try {
          JSONObject jsonO = new JSONObject(content);
          JSONArray array = jsonO.optJSONArray("CodeData");
          JSONObject jsonSummary =  jsonO.optJSONObject("Summary");
          String sVersion =  jsonSummary.getString("key");
          int len = array.length();
          if(len>0){
            long sl = System.currentTimeMillis();
            dataBase.beginTransaction();
            deleteAll();
            int count = 0;
            for (int i = 0; i < len; i++) {
              JSONObject jsonItem = array.optJSONObject(i);
//              String tempStr = jsonManager.getMultipleItemByName(i, "seccode");
//              int a_index = tempStr.indexOf(".");
              String type = jsonItem.optString("type");
              if(type!=null&&(type.startsWith("s")||
                  type.startsWith("i")||type.equals("f.fc.fcs")||
                  type.equals("f.fo.fos.fte"))){
                String stid = jsonItem.optString("stid");
                String mid = jsonItem.optString("mrkt");
                if(!stid.startsWith("sz")&&!stid.startsWith("sh")){
                  if(mid.equals("cn.sz")){
                    stid = "sz"+stid;
                  }else if(mid.equals("cn.sh")){
                    stid = "sh"+stid;
                  }
                }
                insertItem(stid,
                    mid,
                    jsonItem.optString("type"),
                    jsonItem.optString("code"),
                    jsonItem.optString("name"),
                    jsonItem.optString("shrt")
                    );
                if((i%1000)==0){
                  dataBase.setTransactionSuccessful();
                  dataBase.endTransaction();
                  dataBase.beginTransaction();
                }
                count++;
              }/*else{
                System.out.println("不加:"+jsonItem.optString("code")+":"+
                    jsonItem.optString("name"));
              }*/
            }
            
            dataBase.setTransactionSuccessful();//设置事务处理成功，不设置会自动回滚不提交
            dataBase.endTransaction();        //处理完成
            ///更新版本号信息
            try {
              String sql;
              sql = "delete from "+version;
              dataBase.execSQL(sql);
              sql = "insert into " + this.version + "(" + this.property_Version +
                  ") values (" + "'" + sVersion + "')";
              dataBase.execSQL(sql);
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }else{
//            Log.i(JRJ_GlobalData.DEBUG_TAG, "no need update!~");
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      // TODO: handle exception
    }finally{
      if(dataBase != null&&dataBase.isOpen()){
        dataBase.close();
      }
      AppInfo.isUpdateStockDic = false;
      dataBase = null;
      SaveCheckDicTime();
//      if(listener!=null){
//        listener.OnAction(-1, 1);
//      }
    }
  }
//  public void setListener(AppOper oper){
//    this.listener = oper;
//  }
  /**
   * 添加一个新的记录或更新一个新纪录
   * @param mid
   * @param sType
   * @param stkCode
   * @param stkName
   * @param py
   */
  private void insertItem(String stid,String mid,String stType,String stkCode,String stkName,String py)
  {     
    try {
      stkName = stkName.replace("'", "''");                 
      py = py.replace("'", "''");
      String sql = "replace into "+TABLE_NAME
      +"("+Property_MarketId+","+Property_StockCode+","+Property_StockName+","+
          Property_StockPY+","+Property_STOCKId+","+Property_StockType+") values ('"
      +mid +"','"+stkCode+"','"+stkName+"','"+py+"','"+stid+"','"+stType+"')";
      dataBase.execSQL(sql);
    } catch (Throwable ex) {
      ex.printStackTrace();
    }
  }
  private void deleteAll(){
    try {
        //DELETE FROM 表名称 WHERE 列名称 = 值
        String sql = "DELETE FROM "+TABLE_NAME;
        dataBase.execSQL(sql);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  public void SaveCheckDicTime() {
		// 建立一个缓存时间的文件
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(ctx).edit();
		editor.putString("appCheckDicTime", formatDateYMD.format(new Date()));
		editor.commit();
	}

	public String getCheckDicTime() {
		// 建立一个缓存时间的文件
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		return sp.getString("appCheckDicTime", "");
	}
  /**
   * 打开数据库，获取到sqliteDatebase对象
   * @param dbfile
   * @return
   */
  @SuppressLint("SdCardPath")
  private SQLiteDatabase openDatabase(String dbfile)
  {
    try
    {
      String path="/data/data/"+ctx.getPackageName()+"/"+dbfile;
      // 判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
      if (!(new File(path).exists()))
      { 
        
        InputStream input = ctx.getResources().openRawResource(R.raw.jrjstockdic); // 欲导入的数据库
        byte[] data = new byte[8];
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        int nRead;
        while ((nRead = input.read(data)) != -1) {
          bOut.write(data, 0, nRead);
        }
        byte[] src = bOut.toByteArray();
        File file = new File("/data/data/"+ctx.getPackageName()+"/");
        if(!file.exists()){
          file.mkdir();
        }
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(src);
        fos.flush();
        fos.close();
        input.close();
      }
      SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(path,null);
      return db;
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  @SuppressLint("SdCardPath")
	static public void copyDatabase(Context ctx,String dbfile,int dbResId)
  {
    try
    {
      String path="/data/data/"+ctx.getPackageName()+"/"+dbfile;
      // 判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
      if (!(new File(path).exists()))
      { 
        
        InputStream input = ctx.getResources().openRawResource(dbResId); // 欲导入的数据库
        byte[] data = new byte[8];
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        int nRead;
        while ((nRead = input.read(data)) != -1) {
          bOut.write(data, 0, nRead);
        }
        byte[] src = bOut.toByteArray();
        File file = new File("/data/data/"+ctx.getPackageName()+"/");
        if(!file.exists()){
          file.mkdir();
        }
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(src);
        fos.flush();
        fos.close();
        input.close();
      }
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
