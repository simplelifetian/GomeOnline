package com.gome.haoyuangong.bean;


/**
 * 网络数据对象
 * 新闻类列表数据
 * @author guohuiz
 */
public class NewsListItemObj {
  /**
   * 正文类型内容
   */
  public static final int NEWS_ITEM_TYPE_TEXT = 1;
  /**
   * 顶部大图类型
   */
  public static final int NEWS_ITEM_TYPE_IMAGE = 2;
  
  ///用于新闻列表类，不同显示风格标志 默认字符型
  private int showType = NEWS_ITEM_TYPE_TEXT;
  
  private String mNewsID;///新闻id
  private String mNewsTitle="";///标题 
  private String mNewsSummary="";///摘要
  private String mPubDate="";///发布时间
  private String mPicUrl="";///图片链接地址
  private String mNewsDetailUrl="";///新闻大网地址
  private String mNewsChanelID="";///新闻栏目id
  private String mOriginalPubDate="";///原始时间
  private boolean mIsF10Open = false;//f10内容是否展开
  public int getShowType() {
    return showType;
  }
  public void setShowType(int showType) {
    this.showType = showType;
  }
  public String getmNewsID() {
    return mNewsID;
  }
  public void setmNewsID(String mNewsID) {
    this.mNewsID = mNewsID;
  }
  public String getmNewsTitle() {
    return mNewsTitle;
  }
  public void setmNewsTitle(String aTitle) {
    this.mNewsTitle = aTitle;
  }
  public String getmNewsSummary() {
    return mNewsSummary;
  }
  public void setmNewsSummary(String a_summary) {
    this.mNewsSummary = a_summary;
  }
  public String getmPubDate() {
    return mPubDate;
  }
  public void setmPubDate(String mPubDate) {
    this.mPubDate = mPubDate;
  }
  public String getmPicUrl() {
    return mPicUrl;
  }
  public void setmPicUrl(String mPicUrl) {
    this.mPicUrl = mPicUrl;
  }
  public String getmNewsDetailUrl() {
    return mNewsDetailUrl;
  }
  public void setmNewsDetailUrl(String mNewsDetailUrl) {
    this.mNewsDetailUrl = mNewsDetailUrl;
  }
  public String getmNewsChanelID() {
    return mNewsChanelID;
  }
  public void setmNewsChanelID(String mNewsChanelID) {
    this.mNewsChanelID = mNewsChanelID;
  }
  public String getmOriginalPubDate() {
    return mOriginalPubDate;
  }
  public void setMOriginalPubDate(String moriginalPubDate) {
    this.mOriginalPubDate = moriginalPubDate;
  }
  public boolean ismIsF10Open() {
    return mIsF10Open;
  }
  public void setmIsF10Open(boolean mIsF10Open) {
    this.mIsF10Open = mIsF10Open;
  }
  
}
