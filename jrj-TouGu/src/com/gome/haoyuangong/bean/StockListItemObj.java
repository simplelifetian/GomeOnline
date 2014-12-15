package com.gome.haoyuangong.bean;


/**
 * 网络数据对象
 * 股票类表格数据
 * @author guohuiz
 */
public class StockListItemObj {
  public String mStockId="";///股票id
  public String mStockCode="";///股票代码
  public String mStockName="--";///股票名称 
//  public String mCurPrice="--";///最新价
//  public String mDiff="--";///涨跌
//  public String mRange="--";///涨跌幅
//  public String mVol="--";///成交量
//  public String mAmo="--";///成交额
//  public String mHigh="--";///最高价
//  public String mLow = "--";//最低价
//  public String mPrePrice="--";///昨收
//  public String mOpen="--";///今开
//  public String mHuanshou="--";///换手
//  public String mZhenfu="--";///振幅
//  public String mShizhi = "--";//市值
  public String[] gridColContentStrs;
  public String stid;
  ///一行字段颜色值数组
  public int[] mColorArray;
  public int mDrawableIdRange;///涨跌幅的背景色drawable id
  
}
