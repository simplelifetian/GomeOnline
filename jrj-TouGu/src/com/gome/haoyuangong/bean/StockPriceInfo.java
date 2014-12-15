package com.gome.haoyuangong.bean;

/**
 * 股票报价数据结构 类
 * @author Administrator
 *
 */
public class StockPriceInfo {
  public int colorCP;
  
  public int colorOP;
  public int colorHP;
  public int colorLP;
  
  
  public int DecimalNum = 2;
  ///股票编号
  public String m_nStockId;
  ///股票代码
  public String m_StrStockCode="600000";
  ///股票名称
  public String m_StrStockName = "--";
  ///股票类型 
  public int m_nStockType;
  ///昨收价
  public double m_dPreClosePrice;
  public String m_StrPreClosePrice;
///今开
  public String m_fOpenPrice;
///最高
  public String m_fHighPrice;
///最低
  public String m_fLowPrice;
///当前价
  public String m_fCurPrice;
///成交量
  public String m_fVol="";
///成交额
  public String m_fAmount;
///涨跌百分比
  public String m_fRangePercent;
///涨跌额
  public String m_fDifferRange;
///市盈率
  public String m_fShiYingLv;
///换手率
  public String m_fHuanShou;
///振幅
  public String m_fZhenFu;
///总市值
  public String m_fTotalValue;
///买入价1
  public double m_fBuyPrice1;
///卖出价1
  public double m_fSellPrice1;
///买入价2
  public double m_fBuyPrice2;
///卖出价
  public double m_fSellPrice2;
///买入价3
  public double m_fBuyPrice3;
///卖出价
  public double m_fSellPrice3;
///买入价4
  public double m_fBuyPrice4;
///卖出价4
  public double m_fSellPrice4;
///买入价5
  public double m_fBuyPrice5;
///卖出价5
  public double m_fSellPrice5;
///买入量1
  public int m_fBuyVol1;
///卖出量1
  public int m_fSellVol1;
///买入量2
  public int m_fBuyVol2;
///卖出量
  public int m_fSellVol2;
///买入量3
  public int m_fBuyVol3;
///卖出量
  public int m_fSellVol3;
///买入量4
  public int m_fBuyVol4;
///卖出量4
  public int m_fSellVol4;
///买入量5
  public int m_fBuyVol5;
///卖出量5
  public int m_fSellVol5;

  public String m_refreshTime;
}
