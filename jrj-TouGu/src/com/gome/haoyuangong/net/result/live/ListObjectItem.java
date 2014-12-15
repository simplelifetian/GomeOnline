package com.gome.haoyuangong.net.result.live;

import java.util.Vector;

import android.graphics.Bitmap;
import android.text.SpannableStringBuilder;

/**
 * 网络数据对象
 * 为所有列表数据临时存放各字段数据使用
 * @author guohuiz
 */
public class ListObjectItem extends LinkPicBean {
  
  public static final int USER_CENTER_TYPE_TEXT = 1;
  public static final int USER_CENTER_TYPE_ROOM = 2;
  public static final int USER_CENTER_TYPE_BTN_LABLE = 3;
  public static final int USER_CENTER_TYPE_USERINFO_LABLE = 4;
  ///用于 用户中心，不同显示风格标志
  private int showType = USER_CENTER_TYPE_TEXT;
  
  private String mRoomID;///播主ID,观点Id等共用
  private String mRoomName;///名称
  private String mRoomTitle;///标题  --送礼品时，礼品名称借用本变量
  private String mNotice;///提示
  private String pubDate;///发布时间
//  private String linkPic;///图片链接地址
  private boolean isOnLine = false;///是否在线
  private String mContent="";///内容  --送礼品时，价格借用本变量
  private String mQuestion;///提问 问题
  private String mQuestioner;///提问人名称
  private String mQuestionedAt;//提问时间
  private String mReplyTime;//回复时间
  private String mReplyName;//回复人名
  private String mReplyContent;//回复内容
  private String mSso_userid;
  private String mSso_userName;
  private String mUV_number="-";
//  private String mUV_show;
  private int verify;
  private String headPic;
  private int isAdviser;
  
  private Vector<ZhiBoUCenterRoomObject> uCenterVec = null;
  
  private SpannableStringBuilder mContentSSB;
  private SpannableStringBuilder mReplyContentSSB;
  
  
  public SpannableStringBuilder getmReplyContentSSB() {
    return mReplyContentSSB;
  }
  public void setmReplyContentSSB(SpannableStringBuilder mReplyContentSSB) {
    this.mReplyContentSSB = mReplyContentSSB;
  }
  public SpannableStringBuilder getmContentSSB() {
    return mContentSSB;
  }
  public void setmContentSSB(SpannableStringBuilder mContentSSB) {
    this.mContentSSB = mContentSSB;
  }
  public Vector<ZhiBoUCenterRoomObject> getuCenterVec() {
    return uCenterVec;
  }
  public void setuCenterVec(Vector<ZhiBoUCenterRoomObject> uCenterVec) {
    this.uCenterVec = uCenterVec;
  }
  public int getShowType() {
    return showType;
  }
  public void setShowType(int showType) {
    this.showType = showType;
  }
  public String getmSso_userid() {
    return mSso_userid;
  }
  public void setmSso_userid(String mSso_userid) {
    this.mSso_userid = mSso_userid;
  }
  public String getmSso_userName() {
    return mSso_userName;
  }
  public void setmSso_userName(String mSso_userName) {
    this.mSso_userName = mSso_userName;
  }
  public String getmUV_number() {
    return mUV_number;
  }
  public void setmUV_number(String mUV_number) {
    this.mUV_number = mUV_number;
  }
//  public String getmUV_show() {
//    return mUV_show;
//  }
//  public void setmUV_show(String mUV_show) {
//    this.mUV_show = mUV_show;
//  }
  public String getmNotice() {
    return mNotice;
  }
  public void setmNotice(String mNotice) {
    this.mNotice = mNotice;
  }
  public String getmRoomTitle() {
    return mRoomTitle;
  }
  public void setmRoomTitle(String mRoomTitle) {
    this.mRoomTitle = mRoomTitle;
  }
  public String getmRoomID() {
    return mRoomID;
  }
  public void setmRoomID(String mRoomID) {
    this.mRoomID = mRoomID;
  }
  public String getmRoomName() {
    return mRoomName;
  }
  public void setmRoomName(String mRoomName) {
    this.mRoomName = mRoomName;
  }
  public String getPubDate() {
    return pubDate;
  }
  public void setPubDate(String pubDate) {
    this.pubDate = pubDate;
  }
//  public String getLinkPic() {
//    return linkPic;
//  }
//  public void setLinkPic(String linkPic) {
//    this.linkPic = linkPic;
//  }
  
  public boolean isOnLine() {
    return isOnLine;
  }
  public void setOnLine(boolean isOnLine) {
    this.isOnLine = isOnLine;
  }
  public String getmContent() {
    return mContent;
  }
  public void setmContent(String mContent) {
    this.mContent = mContent;
  }
  public String getmQuestion() {
    return mQuestion;
  }
  public void setmQuestion(String mQuestion) {
    this.mQuestion = mQuestion;
  }
  public String getmQuestioner() {
    return mQuestioner;
  }
  public void setmQuestioner(String mQuestioner) {
    this.mQuestioner = mQuestioner;
  }
  public String getmQuestionedAt() {
    return mQuestionedAt;
  }
  public void setmQuestionedAt(String mQuestionedAt) {
    this.mQuestionedAt = mQuestionedAt;
  }
  public String getmReplyTime() {
    return mReplyTime;
  }
  public void setmReplyTime(String mReplyTime) {
    this.mReplyTime = mReplyTime;
  }
  public String getmReplyName() {
    return mReplyName;
  }
  public void setmReplyName(String mReplyName) {
    this.mReplyName = mReplyName;
  }
  public String getmReplyContent() {
    return mReplyContent;
  }
  public void setmReplyContent(String mReplyContent) {
    this.mReplyContent = mReplyContent;
  }
public int getVerify() {
	return verify;
}
public void setVerify(int verify) {
	this.verify = verify;
}
public String getHeadPic() {
	return headPic;
}
public void setHeadPic(String headPic) {
	this.headPic = headPic;
}
public int getIsAdviser() {
	return isAdviser;
}
public void setIsAdviser(int isAdviser) {
	this.isAdviser = isAdviser;
}
  
  
}
