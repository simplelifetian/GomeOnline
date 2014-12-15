package com.gome.haoyuangong.db;

import java.sql.SQLException;

import com.gome.haoyuangong.bean.SearchRecordRecentInfo;
import com.j256.ormlite.dao.Dao;

/**
 * <h1>提供get DAO object接口.</h1>
 * 
 * @author 
 */
public interface IDao {

	Dao<SearchRecordRecentInfo, Integer> getCustInfoReqDao() throws SQLException;
	
}