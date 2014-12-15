package com.gome.haoyuangong.bean;
/**
 * 股票对象
 * 用于对股票进行操作时 使用的对象
 */
import java.io.Serializable;

public class Stock implements Serializable {
  private static final long serialVersionUID = -3507260088936427566L;
  private String stockCode;
  private String marketID;
  private String stockName = "--";
  private String type;
  private String stid;
  private String stockPinyin;
  private String syncState = "";
  private String orderNo = "0";
  
  public String getStockCode() {
    return stockCode;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getStid() {
    return stid;
  }

  public void setStid(String stid) {
    this.stid = stid;
  }

  public void setStockCode(String stockCode) {
    this.stockCode = stockCode;
  }

  public String getMarketID() {
    return marketID;
  }

  public void setMarketID(String marketID) {
    this.marketID = marketID;
  }

  public String getStockName() {
    return stockName;
  }

  public void setStockName(String stockName) {
    this.stockName = stockName;
  }

  public String getStockPinyin() {
    return stockPinyin;
  }

  public void setStockPinyin(String stockPinyin) {
    this.stockPinyin = stockPinyin;
  }
  
  public String getSyncState() {
    return syncState;
  }

  public void setSyncState(String a_syncState) {
    if(a_syncState==null){
      a_syncState = "";
    }
    this.syncState = a_syncState;
  }

  public String getOrderNo() {
    return orderNo;
  }

  public void setOrderNo(String a_orderNo) {
    if(a_orderNo==null){
      a_orderNo = "0";
    }
    this.orderNo = a_orderNo;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    } else if (!(o instanceof Stock)) {
      return false;
    } else {
      return (((Stock) o).stid.equals(stid));
    }
  }
}
