package com.gome.haoyuangong.db;

/**
 * 本地用户设置存储表
 * 管理 用户已读资讯数据库和用户自选股数据库 信息
 * by guohuiz
 */
import java.util.Vector;

import com.gome.haoyuangong.MyApplication;
import com.gome.haoyuangong.bean.NewsListItemObj;
import com.gome.haoyuangong.bean.Stock;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class RecordStoreManager {
	// 股票代码
	public static final String STOCK_CODE = "stockCode";
	// 市场
	public static final String STOCK_MARKET = "stockMarket";
	// 名字
	public static final String STOCK_NAME = "stockName";
	// /类型
	public static final String STOCK_TYPE = "stockType";
	// /股票id
	public static final String STOCK_STID = "stid";
	// /顺序编号
	public static final String STOCK_ORDER_NO = "orderNo";
	// /同步状态
	public static final String STOCK_syc_State = "sycState";
	// 用户是否第一次使用
	public static final String IS_FIRST_USER = "isfirstUse";
	@SuppressLint("SdCardPath")
	private static String dataPath;
	private static final String DATA_NAME = "jrj_record.db";
	private static final String MYSTOCK_TAB_NAME = "myStockTable";
	private static final String APP_SET_TABLE = "userSetTable";
	private static final String INFO_Collect_TABLE = "userCollectTable";

	// private static final String P5W_USER_READ_TABLE
	// ="userReadRecordTable";//用户已读资讯表

	private static final String Collect_INFO_ID = "collInfoId";// /收藏的资讯id
	private static final String Collect_INFO_CLSID = "collinfoclsId";// /收藏的资讯栏目id
	private static final String Collect_INFO_DATE = "collinfoDate";// /资讯时间
	private static final String Collect_INFO_TYPE = "collinfoType";// /资讯类型
	private static final String Collect_INFO_TITLE = "collinfoTitle";// /资讯标题
	private static final String Collect_INFO_summary = "collinfoSummary";// /资讯摘要
	private static final String Collect_INFO_URL = "collinfoURL";// /资讯URL
	private static final String Collect_INFO_haveSearchNews = "collinfoSearch";// /是否有相关新闻

	private static RecordStoreManager instance;

	private SQLiteDatabase dataBase;

	private RecordStoreManager() {
		dataPath = "/data/data/"+MyApplication.get().getPackageName()+"/"+DATA_NAME;
		try {
			dataBase = SQLiteDatabase.openOrCreateDatabase(dataPath, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 创建表
		createUserSetTable();
		createMyStockTable();
		createCollectTable();
		// createUserReadTable();
	}

	/**
	 * 资讯已读表
	 */
	// private void createUserReadTable()
	// {
	// String sql = "CREATE TABLE IF NOT EXISTS "
	// + P5W_USER_READ_TABLE
	// +" (ID INTEGER ,"
	// +INFO_ID
	// +" VARCHAR PRIMARY KEY ,"
	// +INFO_TIME
	// +" VARCHAR)";
	// try {
	// dataBase.execSQL(sql);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	private void createCollectTable() {
		String sql = "CREATE TABLE IF NOT EXISTS " + INFO_Collect_TABLE + " (ID INTEGER PRIMARY KEY," + Collect_INFO_ID + " VARCHAR ," + Collect_INFO_CLSID + " VARCHAR ," + Collect_INFO_TITLE + " VARCHAR ," + Collect_INFO_DATE + " VARCHAR ," + Collect_INFO_summary + " VARCHAR ," + Collect_INFO_URL + " VARCHAR ," + Collect_INFO_TYPE + " VARCHAR ," + Collect_INFO_haveSearchNews + " VARCHAR)";
		try {
			dataBase.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 自选股表
	 */
	private void createUserSetTable() {
		String sql = "CREATE TABLE IF NOT EXISTS " + APP_SET_TABLE + " (ID INTEGER PRIMARY KEY ," + IS_FIRST_USER + " BOOLEAN);";
		try {
			dataBase.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 自选股表
	 */
	private void createMyStockTable() {
		String sql = "CREATE TABLE IF NOT EXISTS " + MYSTOCK_TAB_NAME + " (ID INTEGER," + STOCK_CODE + " VARCHAR," + STOCK_MARKET + " VARCHAR," + STOCK_NAME + " VARCHAR," + STOCK_STID + " VARCHAR PRIMARY KEY," + STOCK_TYPE + " VARCHAR," + STOCK_ORDER_NO + " VARCHAR," + STOCK_syc_State + " VARCHAR" + ");";
		try {
			dataBase.execSQL(sql);
			Cursor oCursor = dataBase.rawQuery("SELECT * FROM " + APP_SET_TABLE, null);
			if (oCursor.getCount() == 0) {
				String noS = System.currentTimeMillis() + "";
//				insertRecord("000001", "cn.sh", "上证指数", "sh000001", "i", noS, "");
//				insertRecord("399001", "cn.sz", "深证成指", "sz399001", "i", noS, "");
				sql = "insert into " + APP_SET_TABLE + "(" + IS_FIRST_USER + ") values ('true')";
				dataBase.execSQL(sql);
			}
			oCursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 插入数据
	 */
	public boolean insertRecord(String a_code, String a_market, String a_name, String stid, String type, String oNo, String state) {
		try {
			if (dataBase == null) {
				dataBase = SQLiteDatabase.openOrCreateDatabase(dataPath, null);
			}
			a_name = a_name.replace("'", "''");
			String sql;
			sql = "replace into " + MYSTOCK_TAB_NAME + "(" + STOCK_CODE + "," + STOCK_MARKET + "," + STOCK_NAME + "," + STOCK_STID + "," + STOCK_TYPE + "," + STOCK_ORDER_NO + "," + STOCK_syc_State + ") values (" + "'" + a_code + "','" + a_market + "','" + a_name + "','" + stid + "','" + type + "','" + oNo + "','" + state + "')";
			// System.out.println("sql:"+sql);
			dataBase.execSQL(sql);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// public boolean updateRecord(String a_code,String a_market,String a_name){
	// try {
	// if(dataBase ==null){
	// dataBase = SQLiteDatabase .openOrCreateDatabase(dataPath + "/" + DATA_NAME,
	// null);
	// }
	// a_name = a_name.replace("'", "''");
	// String sql = "update " + MYSTOCK_TAB_NAME +
	// "set " + STOCK_NAME + "='"+a_name+"'"+
	// " where "+STOCK_CODE+"="+"'"+a_code+
	// "' AND "+STOCK_MARKET+"='"+a_market+"'";
	// dataBase.execSQL(sql);
	// return true;
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// return false;
	// }
	// }
	public boolean deleteRecordAll() {
		try {
			if (dataBase == null) {
				dataBase = SQLiteDatabase.openOrCreateDatabase(dataPath, null);
			}
			String sql = "delete from " + MYSTOCK_TAB_NAME;
			dataBase.execSQL(sql);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public boolean deleteRecord(String stid) {
		try {
			if (dataBase == null) {
				dataBase = SQLiteDatabase.openOrCreateDatabase(dataPath, null);
			}
			String sql = "delete from " + MYSTOCK_TAB_NAME + " where " + STOCK_STID + "=" + "'" + stid/*
																																																 * +
																																																 * "' AND "
																																																 * +
																																																 * STOCK_MARKET
																																																 * +
																																																 * "='"
																																																 * +
																																																 * a_market
																																																 */
					+ "'";
			dataBase.execSQL(sql);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 读取数据
	 * 
	 * @param tab
	 * @return
	 * @throws Exception
	 */
	public Vector<Stock> getAllReadRecord() {
		Vector<Stock> vec = new Vector<Stock>();
		try {
			if (dataBase == null) {
				dataBase = SQLiteDatabase.openOrCreateDatabase(dataPath, null);
			}
			Cursor oCursor = null;
			// 获取最后记录的id
			try {
				oCursor = dataBase.rawQuery("SELECT * FROM " + MYSTOCK_TAB_NAME + " order by " + STOCK_ORDER_NO + " desc", null);
			} catch (Exception e) {
				// TODO: handle exception
				// dataBase.execSQL("ALTER TABLE "+TAB_NAME +" ADD "+ tab+" VARCHAR");
				createMyStockTable();
				oCursor = dataBase.rawQuery("SELECT * FROM " + MYSTOCK_TAB_NAME + " order by " + STOCK_ORDER_NO + " desc", null);
			}
			if (oCursor.getColumnIndex(STOCK_syc_State) == -1) {
				dataBase.execSQL("ALTER TABLE " + MYSTOCK_TAB_NAME + " ADD " + STOCK_syc_State + " VARCHAR");
				oCursor = dataBase.rawQuery("SELECT * FROM " + MYSTOCK_TAB_NAME + " order by " + STOCK_ORDER_NO + " desc", null);
			}
			// int cursorLen = oCursor.getCount();
			// Log.i(NF_GlobalData.DEBUG_TAG, "stockCount:"+cursorLen);

			for (oCursor.moveToFirst(); !oCursor.isAfterLast(); oCursor.moveToNext()) {
				Stock stk = new Stock();
				int index = oCursor.getColumnIndex(STOCK_CODE);
				stk.setStockCode(oCursor.getString(index));
				index = oCursor.getColumnIndex(STOCK_MARKET);
				stk.setMarketID(oCursor.getString(index));
				index = oCursor.getColumnIndex(STOCK_NAME);
				stk.setStockName(oCursor.getString(index));
				index = oCursor.getColumnIndex(STOCK_STID);
				stk.setStid(oCursor.getString(index));
				index = oCursor.getColumnIndex(STOCK_TYPE);
				stk.setType(oCursor.getString(index));
				index = oCursor.getColumnIndex(STOCK_syc_State);
				stk.setSyncState(oCursor.getString(index));
				index = oCursor.getColumnIndex(STOCK_ORDER_NO);
				stk.setOrderNo(oCursor.getString(index));
				vec.add(stk);
			}
			oCursor.close();
			close();
		} catch (Exception e) {
			e.printStackTrace();
			close();
		}

		return vec;
	}

	/**
	 * 获取单子类型实例
	 * 
	 * @return
	 */
	public static RecordStoreManager getInstance() {
		if (instance == null) {
			instance = new RecordStoreManager();
		}
		return instance;
	}

	/**
	 * 关闭当前数据库
	 */
	public void close() {
		if (dataBase != null) {
			dataBase.close();
			dataBase = null;
		}
		instance = null;
	}

	/**
	 * 读取数据--用户已读资讯
	 * 
	 * @param tab
	 * @return
	 * @throws Exception
	 */
	// public Hashtable<String,Boolean> getAllUserReadInfoRecord(){
	// Hashtable<String,Boolean> hsTable = new Hashtable<String,Boolean>();
	// try {
	// if(dataBase == null){
	// dataBase = SQLiteDatabase .openOrCreateDatabase(dataPath + "/" + DATA_NAME,
	// null);
	// }
	// Cursor oCursor = null;
	// //获取最后记录的id
	// try {
	// oCursor = dataBase.rawQuery("SELECT * FROM " + P5W_USER_READ_TABLE+
	// " ORDER BY "+INFO_TIME+" DESC",
	// null);
	// } catch (Exception e) {
	// }
	// if(oCursor!=null){
	// int cursorLen = oCursor.getCount();
	// // Log.i(NF_GlobalData.DEBUG_TAG, "readedInfo Count:"+cursorLen);
	// if(cursorLen>AppInfo.USER_READINFO_AUTOCLEAR_NUM){
	// try {
	// oCursor.moveToPosition(Math.min(AppInfo.USER_READINFO_MAX_NUM,
	// AppInfo.USER_READINFO_MAX_NUM)-1);
	// String timeStr = oCursor.getString(oCursor.getColumnIndex(INFO_TIME));
	// String sql = "delete from " + P5W_USER_READ_TABLE +
	// " where "+INFO_TIME+"<"+"'"+timeStr+"'";
	// dataBase.execSQL(sql);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// oCursor = dataBase.rawQuery("SELECT * FROM " + P5W_USER_READ_TABLE+
	// " ORDER BY "+INFO_TIME+" DESC LIMIT "+AppInfo.USER_READINFO_MAX_NUM,
	// null);
	// for(oCursor.moveToFirst();!oCursor.isAfterLast();oCursor.moveToNext())
	// {
	// int index = oCursor.getColumnIndex(INFO_ID);
	// String key = oCursor.getString(index);
	// // index = oCursor.getColumnIndex(INFO_TIME);
	// // String Time = oCursor.getString(index);
	// // System.out.println(Time+":"+key);
	// hsTable.put(key, true);
	// }
	// oCursor.close();
	// }
	// close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// close();
	// }
	// return hsTable;
	// }
	// public boolean insertUserReadInfoRecord(String a_infoId,String a_timeStr){
	// try {
	// if(dataBase ==null){
	// dataBase = SQLiteDatabase .openOrCreateDatabase(dataPath + "/" + DATA_NAME,
	// null);
	// }
	// String sql;
	// sql = "replace into " + P5W_USER_READ_TABLE +
	// "(" + INFO_ID + ","+INFO_TIME+") values ("+
	// "'"+a_infoId+"','"+a_timeStr+"')";
	// dataBase.execSQL(sql);
	// close();
	// return true;
	// } catch (Exception ex) {
	// close();
	// ex.printStackTrace();
	// return false;
	// }
	// }
	public synchronized Vector<NewsListItemObj> getAllUserCollectRecord() {
		Vector<NewsListItemObj> vec = new Vector<NewsListItemObj>();
		try {
			if (dataBase == null) {
				dataBase = SQLiteDatabase.openOrCreateDatabase(dataPath, null);
			}
			Cursor oCursor = null;

			// 获取最后记录的id
			try {
				oCursor = dataBase.rawQuery("SELECT * FROM " + INFO_Collect_TABLE + " ORDER BY " + Collect_INFO_DATE + " DESC", null);
			} catch (Exception e) {
				// oCursor.close();
			}
			if (oCursor != null) {
				int cursorLen = oCursor.getCount();
				// Log.i(NF_GlobalData.DEBUG_TAG, "readedInfo Count:"+cursorLen);
				if (cursorLen > 50) {
					try {
						oCursor.moveToPosition(50 - 1);
						String timeStr = oCursor.getString(oCursor.getColumnIndex(Collect_INFO_DATE));
						String sql = "delete from " + INFO_Collect_TABLE + " where " + Collect_INFO_DATE + "<" + "'" + timeStr + "'";
						dataBase.execSQL(sql);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				oCursor = dataBase.rawQuery("SELECT * FROM " + INFO_Collect_TABLE + " ORDER BY " + Collect_INFO_DATE + " DESC LIMIT 1000", null);
				for (oCursor.moveToFirst(); !oCursor.isAfterLast(); oCursor.moveToNext()) {
					NewsListItemObj nlio = new NewsListItemObj();
					int index = oCursor.getColumnIndex(Collect_INFO_ID);
					nlio.setmNewsID(oCursor.getString(index));
					index = oCursor.getColumnIndex(Collect_INFO_CLSID);
					nlio.setmNewsChanelID(oCursor.getString(index));

					index = oCursor.getColumnIndex(Collect_INFO_DATE);
					nlio.setmPubDate(oCursor.getString(index));
					index = oCursor.getColumnIndex(Collect_INFO_TITLE);
					nlio.setmNewsTitle(oCursor.getString(index));

					// index = oCursor.getColumnIndex(Collect_INFO_summary);
					nlio.setmNewsSummary("");
					index = oCursor.getColumnIndex(Collect_INFO_URL);
					nlio.setmNewsDetailUrl(oCursor.getString(index));

					index = oCursor.getColumnIndex(Collect_INFO_haveSearchNews);
					nlio.setmPicUrl(oCursor.getString(index));
					index = oCursor.getColumnIndex(Collect_INFO_TYPE);
					String type = (oCursor.getString(index));
					nlio.setShowType(getType(type));
					vec.add(nlio);
				}
			}
			oCursor.close();
			close();
		} catch (Exception e) {
			e.printStackTrace();
			close();
		} finally {

		}
		return vec;
	}

	private int getType(String type) {
		try {
			return Integer.parseInt(type);
		} catch (Exception e) {
			return 0;
		}
	}

	public boolean insertUserCollectRecord(String a_infoId, String a_clsId, String a_title, String a_summary, String a_date, String a_url, String a_type, String haveSearch) {
		try {
			if (dataBase == null) {
				dataBase = SQLiteDatabase.openOrCreateDatabase(dataPath, null);
			}
			String sql;
			sql = "insert into " + INFO_Collect_TABLE + "(" + Collect_INFO_ID + "," + Collect_INFO_CLSID + "," + Collect_INFO_TITLE + "," + Collect_INFO_summary + "," + Collect_INFO_DATE + "," + Collect_INFO_URL + "," + Collect_INFO_TYPE + "," + Collect_INFO_haveSearchNews + ") values (" + "'" + a_infoId + "','" + a_clsId + "','" + a_title + "','" + a_summary + "','" + a_date + "','" + a_url + "','" + a_type + "','" + haveSearch + "')";
			dataBase.execSQL(sql);
			close();
			return true;
		} catch (Exception ex) {
			close();
			ex.printStackTrace();
			return false;
		}
	}

	public boolean delUserCollectRecord(String a_infoId, String a_clsId) {
		try {
			if (dataBase == null) {
				dataBase = SQLiteDatabase.openOrCreateDatabase(dataPath, null);
			}
			String sql;
			sql = "delete from " + INFO_Collect_TABLE + " where " + Collect_INFO_CLSID + " ='" + a_clsId + "' AND " + Collect_INFO_ID + "='" + a_infoId + "'";
			dataBase.execSQL(sql);
			close();
			return true;
		} catch (Exception ex) {
			close();
			ex.printStackTrace();
			return false;
		}
	}

	public boolean isUserCollectRecord(String a_infoId, String a_clsId) {
		boolean result = false;
		try {
			if (dataBase == null || !dataBase.isOpen()) {
				dataBase = SQLiteDatabase.openOrCreateDatabase(dataPath, null);
			}
			Cursor oCursor = null;

			// 获取最后记录的id
			try {
				oCursor = dataBase.rawQuery("SELECT * FROM " + INFO_Collect_TABLE + " where " + Collect_INFO_CLSID + " ='" + a_clsId + "' AND " + Collect_INFO_ID + "='" + a_infoId + "'", null);
			} catch (Exception e) {
			}
			if (oCursor != null) {
				int cursorLen = oCursor.getCount();
				if (cursorLen > 0) {
					result = true;
				}
				oCursor.close();
			}
			close();
			return result;
		} catch (Exception ex) {
			close();
			ex.printStackTrace();
			return result;
		}
	}
}
