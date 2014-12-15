package com.gome.haoyuangong.db;

import java.sql.SQLException;

import com.gome.haoyuangong.BuildConfig;
import com.gome.haoyuangong.bean.SearchRecordRecentInfo;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;




/**
 * <h1>数据库操作类，进行数据库、创建表、提供DAO object等.</h1>
 * 
 * @author 
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper implements IDao {

	public static final String tag = DatabaseHelper.class.getName();

	// name of the database file for your application -- change to something
	// appropriate for your app
	private static final String DATABASE_NAME = "chinajrj.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 1;

	// the DAO object we use to access the FavoriteArticle table
	private Dao<SearchRecordRecentInfo, Integer> favoriteArticleDao = null;
	
	//private HashMap<String, Object> daos = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * This is called when the database is first created. Usually you should
	 * call createTable statements here to create the tables that will store
	 * your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			if(BuildConfig.DEBUG) Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, SearchRecordRecentInfo.class);

			// here we try inserting data in the on-create as a test
			/*
			 * Dao<Article, Integer> dao = getArticleDao(); long millis =
			 * System.currentTimeMillis(); // create some entries in the
			 * onCreate Article simple = new Article(millis);
			 * dao.create(simple); simple = new Article(millis + 1);
			 * dao.create(simple);
			 */
		} catch (SQLException e) {
			if(BuildConfig.DEBUG) Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the
	 * new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			if(BuildConfig.DEBUG) Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, SearchRecordRecentInfo.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			if(BuildConfig.DEBUG) Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the Database Access Object (DAO) for our FavoriteArticle class. It
	 * will create it or just give the cached value.
	 */
	@Override
	public Dao<SearchRecordRecentInfo, Integer> getCustInfoReqDao() throws SQLException {
		if (favoriteArticleDao == null) {
			favoriteArticleDao = getDao(SearchRecordRecentInfo.class);
		}
		return favoriteArticleDao;
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
	}
}
