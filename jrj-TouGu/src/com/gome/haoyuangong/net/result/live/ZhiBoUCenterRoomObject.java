package com.gome.haoyuangong.net.result.live;
/**
 * 用户中心用户显示收藏和浏览等使用的结构体
 * @author guohuiz
 */
public class ZhiBoUCenterRoomObject {
  private String mRoomID;///直播室Id
  private String mRoomName;///直播室名称
  private String mSso_userid;///播主ssoid
  private String linkPic;///图片链接地址
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
  public String getmSso_userid() {
    return mSso_userid;
  }
  public void setmSso_userid(String mSso_userid) {
    this.mSso_userid = mSso_userid;
  }
  public String getLinkPic() {
    return linkPic;
  }
  public void setLinkPic(String linkPic) {
    this.linkPic = linkPic;
  }
  
}
