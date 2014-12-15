/**
 *
 */
package com.gome.haoyuangong.update;



/**
 * the constants of the whole application
 *
 * @author <a href="mailto:raxlee@gmail.com">Rax Lee</a>
 */
public class Constants {


    /**
     * test server
     * 环境总开关
     */
	/*******************************************
	 * IS_TEST=false 表示正式环境
	 * 如果需要测试环境请只做本地修改，不可提交到svn
	 *******************************************/
    public static final boolean IS_TEST = false;
    
    public static final boolean IS_DEBUG_COURSOR = false;
    
    public static boolean IS_ALLOW_CHOICE = true;//是否允许用户选择模式
    public static boolean IS_IM_MODEL = false;// 是否是im 模式
    /*******************************************************
     * IS_ALLOW_CHOICE  true       false    false
     * IS_IM_MODEL       X         false    true
     * 结果                                      用户自由选择           老版本模式          IM模式
     * *****************************************************/
    
    public static final String CONTACTS_SERVER_HTTP = IS_TEST ? "test.address.y.sdo.com/youniws"
            : "address.y.sdo.com";

    /**
     * normal server
     */
//       public static final String CONTACTS_SERVER_HTTP = "address.y.sdo.com";

    public static final String REQUEST_URL_FRIENDLIST = "/fl.do";

    public static final String REQUEST_URL_CONTACTSCOMMIT = "/ca.do";

    public static final String REQUEST_URL_IMAGEUPLOAD = "/uu.do";

    public static final String REQUEST_URL_USERSETING = "/us.do";

    public static final String REQUEST_URL_REGIST = "/ur.do";

    /**
     * the url for getting userinfo
     */
    public static final String REQUEST_URL_USERINFO = "/uf.do";
    public static final String REQUEST_URL_SETTINGINFO = "/ui.do";
    public static final String REQUEST_URL_UPLOAD = "/ups.do";

    public static final String REQUEST_URL_ERRORUPLOAD = "/lu.do";

    public static final String REQUEST_URL_USEREXTRAINFO = "/user/extra.do";

    /**
     * the url for checking MD5
     */
    public static final String REQUEST_URL_CHECK_MD5 = "/contacts/get_md5contacts.do";

    public static final String REQUEST_URL_MULTI_USERINFO = "/user/mui.do";

    public static final String REQUEST_URL_USER_SET_CONFIG = "/user/set_config.do";
    public static final String REQUEST_URL_USER_GET_CONFIG = "/user/get_config.do";


    public static final String REQUEST_UPDATE_GROUP = "/avatar/update_group.do";

    /**test update*/
//       public static final String REQUEST_URL_UPDATE = "http://test.update.y.sdo.com/smooth/update.htm";
    /**
     * normal update
     */
    public static final String REQUEST_URL_UPDATE = IS_TEST ? "http://test.update.y.sdo.com/smooth/update.htm" : "http://update.y.sdo.com/smooth/update.htm";

    /**有你通行证*/
//       public static final String REQUEST_URL_LOGIN = "http://test.weboa.y.sdo.com/weboa/services/validate.shtml";
    /**
     * 盛大通行证
     */
    public static final String REQUEST_URL_LOGIN = IS_TEST ? "http://test.weboa.y.sdo.com/weboa/services/validate.shtml" : "http://weboa.y.sdo.com/weboa/services/validate.shtml";

    //       public static final String APK_PLUGIN_LIST_URL = "http://test.y.sdo.com/pf/lastestPlugin.htm";//test url
    public static final String APK_PLUGIN_LIST_URL = IS_TEST ? "http://test.y.sdo.com/pf/lastestPlugin.htm" : "http://plugin.y.sdo.com/pf/lastestPlugin.htm";

    public static final String PLUGIN_DOWNLOAD_FINISHED_URL = IS_TEST ? "http://test.y.sdo.com/pf/dlnotification.htm" : "http://plugin.y.sdo.com/pf/dlnotification.htm";

    public static final String UPLOAD_REG_STATUS_URL = "http://address.y.sdo.com/uic/misc/vwoa.do";

    public static final String RANK_REMIND_URL = IS_TEST ? "http://temp.y.sdo.com/messagestatic/compare" : "http://switch.apps.y.sdo.com/messagestatic/compare";
    public static final String STATS_SHARE_FRIEND = IS_TEST ? "http://temp.y.sdo.com/messagestatic/upload-msg" : "http://switch.apps.y.sdo.com/messagestatic/upload-msg";

    public static final String ENTERPRISE_INFO_LIST_URL = "http://enterprise.apps.y.sdo.com/services/enterprise/list";
    public static final String ENTERPRISE_INFO_BLACK_LIST_URL = "http://enterprise.apps.y.sdo.com/services/enterprise/blacklist";
    public static final String SYNC_BLACK_LIST_URL = "http://blacklist.apps.y.sdo.com/sync";

    //       public static final String SWITCH_LIST_URL = "http://switch.apps.y.sdo.com/services/switch/list";
    // test url
    public static final String SWITCH_LIST_URL = IS_TEST ? "http://temp.y.sdo.com/services/switch/list"
            : "http://switch.apps.y.sdo.com/services/switch/list";

    //       public static final String SMS_BACKUP_SERVER = "http://test.msg.apps.y.sdo.com/services/message/";
    public static final String SMS_BACKUP_SERVER = "http://msg.apps.y.sdo.com/services/message/";
    public static final String REQUEST_URL_UPLOAD_SMS = SMS_BACKUP_SERVER + "backupMessages";
    public static final String REQUEST_URL_CHECK_BACKUP = SMS_BACKUP_SERVER + "checkBackup";
    public static final String REQUEST_URL_DELETE_BACKUP = SMS_BACKUP_SERVER + "deleteBackup";
    public static final String REQUEST_URL_GET_BACKUP_INFO = SMS_BACKUP_SERVER + "getBackupInfo";
    public static final String REQUEST_URL_ENCRYPTED = SMS_BACKUP_SERVER + "encrypted";

    public static final String REMOTE_HELP_SWITCH_URL = "http://openlog.y.sdo.com/openlog/post.php";

    public static final String PLUGIN_BANNER_URL = "http://appstore.apps.y.sdo.com/appstore/get-banner";

    public static final String FR_SERVER = IS_TEST ? "http://test.friend.apps.y.sdo.com/friend"
            : "http://friend.apps.y.sdo.com/friend";
    public static final String URL_SYNC_SETTING = FR_SERVER + "/settingsync";

    public static final String REQUEST_URL_SYNC_WEB_MSG = IS_TEST ? "http://test.address.y.sdo.com/api/message/web/sync/encrypt" : "http://websync.y.sdo.com/message/web/sync/encrypt";

    public static final String REQUEST_URL_SYNC_WEB_MSG_PROFILE = IS_TEST ? "http://test.address.y.sdo.com/api/message/web/sync/profile" : "http://websync.y.sdo.com/message/web/sync/profile";

    public static final String REQUEST_URL_WEBYOUNI_AUTH = IS_TEST ? "http://test.address.y.sdo.com/weblogin/clogin.json" : "http://wl.youni.im/weblogin/clogin.json";

    /**
     * 动态表情
     */
    public static final String EMOTION_SERVER = IS_TEST ? "http://temp.y.sdo.com/emo" : "http://emo.apps.y.sdo.com/emo";
    public static final String REQUEST_URL_EMOTION_GET_LIST = EMOTION_SERVER + "/getlist";
    public static final String REQUEST_URL_EMOTION_DOWNLOAD = EMOTION_SERVER + "/download";
    public static final String REQUEST_URL_EMOTION_GET_DETAIL = EMOTION_SERVER + "/get-detail";
    public static final String REQUEST_URL_EMOTION_GET_ITEM = EMOTION_SERVER + "/get-item";
    public static final String EMOTION_GET_ITEM_URL_PREFIX = EMOTION_SERVER + "/get-item";
    public static final String EMOTION_GET_SHORT_URL = EMOTION_SERVER + "/get-short";

    //       public static final String AVATAR_LIST_URL = "http://test.y.sdo.com:8080/clientresource/getUserResource";
    public static final String AVATAR_LIST_URL = IS_TEST ? "http://test.y.sdo.com/clientresource/getUserResource" : "http://resources.y.sdo.com/clientresource/getUserResource";

    /**
     * minipage *
     */
    public static final String MINIPAGE_SERVER = IS_TEST ? "http://test.address.y.sdo.com/mp" : "http://address.y.sdo.com/mp";
    public static final String URL_GET_MINIPAGE_ALL = MINIPAGE_SERVER + "/get_minipage.do";
    public static final String URL_GET_USER_PROFILE = MINIPAGE_SERVER + "/get_user_profile.do";
    public static final String URL_UPDATE_USER_PROFILE = MINIPAGE_SERVER + "/update_profile.do";
    public static final String URL_GET_FIXED_INFOS = MINIPAGE_SERVER + "/get_fixed_infos.do";
    public static final String URL_UPDATE_USER_LABEL = MINIPAGE_SERVER + "/update_tags.do";
    public static final String URL_UPDATE_USER_CIRCLE = MINIPAGE_SERVER + "/update_circle.do";
    public static final String URL_UPDATE_USER_GROUPS = MINIPAGE_SERVER + "/update_groups.do";
    public static final String URL_GET_USER_GROUPS = MINIPAGE_SERVER + "/get_groups.do";

    public static final String GET_USER_NAME_SERVER = IS_TEST ? "http://test.address.y.sdo.com/misc" : "http://address.y.sdo.com/misc";
    public static final String URL_GET_USER_NAME = GET_USER_NAME_SERVER + "/gn.do";

    public static final String GET_MUTUAL_FRIENDS_SERVER = IS_TEST ? "http://test.friend.apps.y.sdo.com/minipage" : "http://friend.apps.y.sdo.com/minipage";
    public static final String URL_GET_MUTUAL_FRIENDS = GET_MUTUAL_FRIENDS_SERVER + "/getmutual";

    public static final String URL_SET_USER_CONFIG = IS_TEST ? "http://test.address.y.sdo.com/user/cfg.do" : "http://address.y.sdo.com/user/cfg.do";
    public static final String URL_GET_USER_CONFIG = IS_TEST ? "http://test.address.y.sdo.com/user/gcfg.do" : "http://address.y.sdo.com/user/gcfg.do";

    /**
     * the contenttype for data upload
     */
    public static final String CONTENTTYPE_DATA = "application/octet-stream";

    /**
     * the contenttype for parameters upload
     */
    public static final String CONTENTTYPE_PARAMETERS = "application/x-www-form-urlencoded";

    /**
     * the switch for wap functions,false as default
     *
     * @create by lixiaohua@snda.com
     */
    public static final boolean wapSwitch = false;

    public static final String ACTION_WAP_MESSAGE_RECEIVED = "com.snda.youni.receive.RECEIVEWAP";


    //other app info
    public static final String MAIKU_PKG_NAME = "com.snda.inote";
    public static final String MAIKU_DOWNLOAD_URL = "http://m.note.sdo.com/home/download/android";
    public static final String MAIKU_DOWNLOAD_APK_URL = "http://download.note.sdo.com/res/aafc32b4-7b93-4944-907a-53b8abbbcd23";
    public static final String MAIKU_URL = "http://m.note.sdo.com";
    /* com.snda.inote.note.add_with_result 有返回 key为category_name 值为成功保持信息到 xxx     PS:xxx为分类名
       com.snda.inote.note.add 无返回值，只有判断为Result_OK*/
    public static final String MAIKU_ACTION_OLD = "com.snda.inote.note.add";
    public static final String MAIKU_ACTION = "com.snda.inote.note.add_with_result";
    public static final String MAIKU_RESULT_KEY = "category_name";

    public static final boolean abroadtest = false;

    public static final boolean woa1Only = false;

    public static final boolean abTest = false;
    // abtest的开关，默认false，如果打开则进行abtest，否则不做对比
    public static final boolean abTestSwitch = false;

    public static final boolean RECOMMEND_SWITCH = false;

    public static final boolean WANPU_ACTIVATE = false;//给万普的激活通知开关，给万普的包要打开这个

    public static final String ERROR_STORE_ACTION = "com.snda.youni.storeErrorToFile";

    public static final String CONSUMER_KEY = "1743675149";
    public static final String CONSUMER_SECRET = "921dab93fbeb6dec1f1dc06b3af1d3a47";
    public static final String DEFAULT_CALLBACK_URL = "http://y.sdo.com";

    //       public static final String GET_STATS_MSG_URL = "http://wx.ops.y.sdo.com/share/weibo";
    public static final String GET_STATS_MSG_URL = "http://activities.y.sdo.com/services/activity/list";
//       public static final String GET_STATS_MSG_URL ="http://test.y.sdo.com:8080/services/activity/list";

    public static final String WOA1 = "woa1.0";

    public static final String WOA2 = "woa2.0";

    public static final boolean SAVE_MESSAGE_AS_READED = true;

    public static final boolean SCAN_DUAL_SIM_ENABLED = true;

    //不可见字符
    public static final char NULL_CHAR = '\0';

    public static final String GET_SERVER_TIPS_URL = IS_TEST ? "http://test.tips.apps.y.sdo.com/services/tips/list" : "http://tips.apps.y.sdo.com/services/tips/list";
    public static final String GET_BANNER_URL = IS_TEST ? "http://temp.y.sdo.com/appstore/get-banner"
            : "http://appstore.apps.y.sdo.com/appstore/get-banner";
    
       /*自言自語*/

    public static final String ZIYANZIYU_SERVER = IS_TEST ? "http://101.227.1.142:8099" : "http://feed.y.sdo.com";
    public static final String GET_ZIYANZIYU_LIST = ZIYANZIYU_SERVER + "/feed/list";
    public static final String PUBLISH_ZIYANZIYU = ZIYANZIYU_SERVER + "/feed/publish";
    public static final String GET_COMMENTS_LIST = ZIYANZIYU_SERVER + "/comment/list";
    public static final String REMOVE_ZIYANZIYU = ZIYANZIYU_SERVER + "/feed/remove";
    public static final String ZYZY_UPLOAD_IMAGE_URL = IS_TEST ? "http://test.isay.y.sdo.com/media/isay/image" :
            "http://isay.y.sdo.com/media/isay/image";
    public static final String ZYZY_QUERY_IMAGE_URL = IS_TEST ? "http://test.isay.y.sdo.com//media/query/image" :
            "http://isay.y.sdo.com//media/query/image";

    public static final String GET_ZYZY_SHAKE_WORD_URL = IS_TEST ? "http://test.shake.apps.y.sdo.com/shake/get-shake-message" : "http://shake.apps.y.sdo.com/shake/get-shake-message"; //

    public static final String GET_FEED_COMMENT_COUNT_URL = ZIYANZIYU_SERVER + "/comment/count";
    public static final String FEED_COMMENT_URL = ZIYANZIYU_SERVER + "/feed/comment";
    // 好友请求认证
    public static final String VALIDATE_ZIYANZIYU_REQUEST = IS_TEST ? "http://test.namecard.apps.y.sdo.com/exchange/request" : "http://namecard.apps.y.sdo.com/exchange/request";
    public static final String VALIDATE_ZIYANZIYU_RESPONSE = IS_TEST ? "http://test.namecard.apps.y.sdo.com/exchange/responset" : "http://namecard.apps.y.sdo.com/exchange/response";

    //万普注册激活通知接口
    public static final String WANPU_ACTIVATE_URL = "http://app.wapx.cn/action/receiver/activate";

    public static final String GET_BANNER_PIC_URL_TEMPLATE = "http://%s/getpic/%d_%d/%s";

    public static final boolean INCREMENTAL_UPDATE_SUPPORT = true;

    public static final String BACKUP_STORE_URL = IS_TEST ? "http://temp.y.sdo.com/backup/upload" : "http://backup.apps.y.sdo.com/backup/upload";
    public static final String BACKUP_RESTORE_URL = IS_TEST ? "http://temp.y.sdo.com/backup/restore" : "http://backup.apps.y.sdo.com/backup/restore";
    public static final String BACKUP_RESTORE_STREAM_URL = IS_TEST ? "http://temp.y.sdo.com/backup/restore-stream" : "http://backup.apps.y.sdo.com/backup/restore-stream";
    public static final String BACKUP_GET_CONTACTS_URL = IS_TEST ? "http://temp.y.sdo.com/backup/get-contacts" : "http://backup.apps.y.sdo.com/backup/get-contacts";
    public static final String BACKUP_GET_BACKUP_TIMES_URL = IS_TEST ? "http://temp.y.sdo.com/backup/get-backup-times" : "http://backup.apps.y.sdo.com/backup/get-backup-times";

    public static final String NICK_SERVER = IS_TEST ? "http://test.wine.y.sdo.com/uic"
            : "http://test.wine.y.sdo.com/uic";
    public static final String NICK_URL = "/user/update_nick.json";

    public static final String OTHER_USER_PROFILE_URL = IS_TEST ? "http://test.address.y.sdo.com/user/getProfile.do"
            : "http://address.y.sdo.com/user/getProfile.do";

    //wine上传|发布|评论|转发|喜欢
    //wine上传
    public static final String WINE_UPLOAD_VIDEO_URL = IS_TEST ? "http://uptest.wine.y.sdo.com/upload/video.json"
            : "http://media.wine.y.sdo.com/upload/video.json";
    public static final String WINE_UPLOAD_IMAGE_URL = IS_TEST ? "http://uptest.wine.y.sdo.com/upload/image.json"
            : "http://media.wine.y.sdo.com/upload/image.json";
    public static final String WINE_UPLOAD_SEGMENT_URL = IS_TEST ? "http://uptest.wine.y.sdo.com/upload/segment.json"
            : "http://media.wine.y.sdo.com/upload/segment.json";
    public static final String WINE_UPLOAD_SEGMENT_IMAGE_URL = IS_TEST ? "http://uptest.wine.y.sdo.com/upload/segment-image.json"
            : "http://media.wine.y.sdo.com/upload/segment-image.json";
    public static final String WINE_UPLOAD_SEGMENT_MULTI_URL = IS_TEST ? "http://uptest.wine.y.sdo.com/upload/segment-multi.json"
            : "http://media.wine.y.sdo.com/upload/segment-multi.json";
    
    public static final String WINE_SHARE_APPS_SERVER=IS_TEST?"http://temp.y.sdo.com":"http://share.apps.y.sdo.com";
    public static final String WINE_REPORT_URL =WINE_SHARE_APPS_SERVER + "/report";
    
    public static final String WINE_SERVER_HOST = IS_TEST ? "http://test.wine.y.sdo.com" : "http://wine.y.sdo.com";
    //wine发布|评论|转发|喜欢|获取资源列表
    //以下接口都是由陈威负责的发布的资源相关的内容
    public static final String WINE_SERVER_RESOURCE = WINE_SERVER_HOST + "/feed";
    public static final String WINE_POST_TEXT_URL = WINE_SERVER_RESOURCE + "/postText";
    public static final String WINE_POST_IMAGE_URL = WINE_SERVER_RESOURCE + "/postImage";
    public static final String WINE_POST_VIDEO_URL = WINE_SERVER_RESOURCE + "/postVideo";
    public static final String WINE_FORWARD_URL = WINE_SERVER_RESOURCE + "/forward";
    public static final String WINE_REISSUE_FORWARD_REWARD_URL = WINE_SERVER_RESOURCE + "/reissue_forward_reward";
    public static final String WINE_POST_COMMENT_URL = WINE_SERVER_RESOURCE + "/comment";
    public static final String WINE_REMOVE_URL = WINE_SERVER_RESOURCE + "/remove";
    public static final String WINE_RESOURCE_BY_TAG_URL = WINE_SERVER_RESOURCE + "/search_by_tag";
    public static final String WINE_USER_RESOURCE_URL = WINE_SERVER_RESOURCE + "/list_user_post";
    public static final String WINE_FAVORITE_RESOURCE_URL = WINE_SERVER_RESOURCE + "/list_user_like";
    public static final String WINE_FOLLOW_RESOURCE_URL = WINE_SERVER_RESOURCE + "/list_following_post";
    public static final String WINE_COMMENT_LIST_URL = WINE_SERVER_RESOURCE + "/list_resource";
    public static final String WINE_LATEST_RESOURCE_URL = WINE_SERVER_RESOURCE + "/list_new_post";
    public static final String WINE_HOT_RESOURCE_URL = WINE_SERVER_RESOURCE + "/list_hot_post";
    public static final String WINE_FEED_DETAIL_URL = WINE_SERVER_RESOURCE + "/list_resource_detail";
    public static final String WINE_DUAL_RELATION_CREATE_RESOURCE_URL = WINE_SERVER_RESOURCE+"/list_following_org_post";

    public static final String WINE_FEED_DISLIKE = WINE_SERVER_RESOURCE + "/cancel_like";
    public static final String WINE_FEED_CANCEL_RETWEET = WINE_SERVER_RESOURCE + "/cancel_forward";
    public static final String WINE_FEED_DELETE = WINE_SERVER_RESOURCE + "/remove";
    public static final String WINE_FEED_DELETE_COMMENT = WINE_SERVER_RESOURCE + "/remove_comment";    
    
    public static final String WINE_GET_RESOURCE_LOVER_LIST_URL = WINE_SERVER_RESOURCE + "/list_like_user";
    public static final String WINE_GET_RESOURCE_SHARER_LIST_URL = WINE_SERVER_RESOURCE + "/list_forward_user";
    
    public static final String WINE_NETWORK_DETECT_URL = WINE_SERVER_RESOURCE + "/detect";// 探测网络

    //交易相关
    public static final String WINE_FEED_TRADE_USER_POST = WINE_SERVER_RESOURCE + "/list_user_commodity";
    public static final String WINE_FEED_TRADE_LATEST_POST = WINE_SERVER_RESOURCE + "/list_new_commodity";
    public static final String WINE_FEED_TRADE_FRIENDS_POST = WINE_SERVER_RESOURCE + "/list_following_commodity";
    public static final String WINE_FEED_TRADE_MYPURCHASE_POST = WINE_SERVER_RESOURCE + "/list_user_purchase";
    public static final String WINE_FEED_TRADE_PURCHASE_LIST_POST = WINE_SERVER_RESOURCE + "/list_commodity";

    //wine收听|观众|个人信息
    //以下接口都是由畏三负责的用户关系相关的内容
    public static final String WINE_SERVER_RELATIONSHIP = WINE_SERVER_HOST + "/uic";
    public static final String WINE_FOLLOW_URL = WINE_SERVER_RELATIONSHIP + "/friendships/create.json";
    public static final String WINE_CANCEL_FOLLOW_URL = WINE_SERVER_RELATIONSHIP + "/friendships/destroy.json";
    public static final String WINE_GIVE_SPEAKER_URL = WINE_SERVER_RELATIONSHIP + "/forwarder/grant.json";
    public static final String WINE_CANCEL_SPEAKER_URL = WINE_SERVER_RELATIONSHIP + "/forwarder/destroy.json";
    public static final String WINE_GET_FOLLOW_LIST_URL = WINE_SERVER_RELATIONSHIP + "/friendships/friends.json";
    public static final String WINE_GET_FANS_LIST_URL = WINE_SERVER_RELATIONSHIP + "/friendships/followers.json";
    public static final String WINE_GET_SUGGESTION_TAGS_URL = WINE_SERVER_RELATIONSHIP + "/tags/suggestions.json";
    public static final String WINE_SET_TAGS_URL = WINE_SERVER_RELATIONSHIP + "/tags/add.json";
    public static final String WINE_SET_NICK_URL = WINE_SERVER_RELATIONSHIP + "/user/update_nick.json";
    public static final String WINE_INIT_USER_INFO_URL = WINE_SERVER_RELATIONSHIP + "/user/init.json";
    public static final String WINE_GET_USER_INFO_URL = WINE_SERVER_RELATIONSHIP + "/user/profile.json";
    public static final String WINE_VERIFY_NICK_URL = WINE_SERVER_RELATIONSHIP + "/user/verify_nick.json";
//    public static final String WINE_INIT_USER_URL = WINE_SERVER_RELATIONSHIP + "/user/init.json";
    public static final String WINE_REGISTER_USER_URL = WINE_SERVER_RELATIONSHIP + "/user/register.json";
    public static final String WINE_GET_FRIEND_LIST = WINE_SERVER_RELATIONSHIP + "/friendships/friends.json";
    public static final String WINE_BLACK_LIST_USER_INFO = WINE_SERVER_RELATIONSHIP + "/user/blkus.json";
    public static final String WINE_SERVER_RELATIONSHIP_V2 = WINE_SERVER_HOST + "/uic/v2";
    public static final String WINE_GET_FRIEND_LIST_V2 = WINE_SERVER_RELATIONSHIP_V2 + "/friendships/friends.json";
    public static final String WINE_DELETE_FRIEND_V2 = WINE_SERVER_RELATIONSHIP_V2+"/friendships/remove.json";
    public static final String WINE_GET_USER_INFO_URL_V2 = WINE_SERVER_RELATIONSHIP_V2 + "/user/profile.json";
    public static final String WINE_GET_MU_FRIEND = WINE_SERVER_RELATIONSHIP + "/v2/friendships/mutual.json";
    //wine推荐好友接口
    public static final String WINE_RECOMMENDED_FRIEND = WINE_SERVER_HOST + "/recommend";
    public static final String WINE_GET_SELF_RECOMMENDED_FRIEND_URL = WINE_RECOMMENDED_FRIEND + "/user";
    public static final String WINE_GET_OTHER_RECOMMENDED_FRIEND_URL = WINE_RECOMMENDED_FRIEND + "/user-friend";
    
    public static final String WINE_CHECK_LBS_ENABLED = WINE_SERVER_RESOURCE+"/is_in_area";
    public static final String WINE_GET_LIST_NEAR_POST = WINE_SERVER_RESOURCE+"/list_near_post";
    public static final String WINE_GET_NEAR_NEW_RESOURCE_NUM = WINE_SERVER_RESOURCE+"/near_new_resource_num";
    
    static final String GET_PROFILE_SERVER = IS_TEST ? "http://test.address.y.sdo.com"
            : "http://address.y.sdo.com";
    public static final String WINE_GET_PROFILE_URL = GET_PROFILE_SERVER + "/user/getProfile.do";
    
    public static final String WINE_ADD_FRIENDS = IS_TEST ?"http://test.wine.y.sdo.com/uic/v2/friendships/add_friend.json":"http://wine.y.sdo.com/uic/v2/friendships/add_friend.json";
    public static final String WINE_V2_RESPONSE_ADD_FRIENDS = IS_TEST ?"http://test.wine.y.sdo.com/uic/v2/friendships/add_friend_res.json":"http://wine.y.sdo.com/uic/v2/friendships/add_friend_res.json";
    public static final String WINE_V3_RESPONSE_ADD_FRIENDS = IS_TEST ?"http://test.wine.y.sdo.com/uic/v3/friendships/add_friend_res.json":"http://wine.y.sdo.com/uic/v3/friendships/add_friend_res.json";

    //朋友圈状态接口地址
    public static final String WINE_REG_URL = IS_TEST ?"http://test.wine.y.sdo.com/uic/user/stat.json":"http://wine.y.sdo.com/uic/user/stat.json";

    public static final String TRAIL_AWARDD = IS_TEST ?"http://test.task.y.sdo.com/task/post":"http://task.y.sdo.com/task/post";
    
    public static final String QP_PRIVACY_URL = IS_TEST ? "http://wx.y.sdo.com/protocol/youni_wallet.html":"http://y.sdo.com/protocol/youni_wallet.html";
    public static final String MONEY_TREE_INVITE_URL ="http://task.y.sdo.com/shake-reward/save-invite";
    
    
    //wine是否启用共同好友的开关
    public static final boolean MUTUAL_FRIEND_SWITCH = true;
    //wine发布图片，视频内容是否启动后台上传和发布
    public static final boolean WINE_LAUNCH_BACKGROUND_PUBLISH = true; 
    
    //wine发布图片，视频内容是否启动断点续传 目前只是控制单图
    public static final boolean WINE_LAUNCH_UPLOAD_FROME_BREAK = true; 
    
    public static final boolean ADD_QIANBAO_PHONE_NUMBER = true;
    
    public static final String QIANBAO_ROBOT_NUMBER = "520007";
    public static final String QIANBAO_ROBOT_NAME = "钱包小助手";
    public static final String QIANBAO_ROBOT_SIGN = "像发消息一样发钱";
    
    public static final String FEEDBACK_ROBOT_NUMBER = "5208890";
    
    public static final String YOUNI_OFFICIAL_SDID_HEAD="82000001";
    
    public static final String WINE_RANKING_LIST_URL = IS_TEST?"http://y.sdo.com/tuhaobang/w.html#"
    		:"http://y.sdo.com/tuhaobang/w.html#";
    
    public static final String WINE_SHARE_REWARD_CALLBACK_URL = IS_TEST?"http://test.wine.y.sdo.com/feed/forward_reward_to_ext":"http://wine.y.sdo.com/feed/forward_reward_to_ext";

    public static final String UPLOAD_INVITE_URL = IS_TEST ? "http://test.invite.y.sdo.com/services/save" : "http://invite.y.sdo.com/services/save";
    
    public static final String WINE_FORWARD_GET_SHORTURL_URL = IS_TEST ? "http://test.y.sdo.com/protocol/add" : "http://img.y.sdo.com/protocol/add";
    public static final String HISTORY_MESSAGE_COUNT_URL =IS_TEST? "http://test.address.y.sdo.com/api/message/history/total":"http://websync.y.sdo.com/message/history/total";
    
    public static final String HISTORY_MESSAGE_URL=IS_TEST?"http://test.address.y.sdo.com/api/message/history":"http://websync.y.sdo.com/message/history";
    public static final String HISTORY_MESSAGE_DELETE_URL = IS_TEST?"http://test.address.y.sdo.com/api/message/history/delete":"http://websync.y.sdo.com/message/history/delete";

    public static final String MUC_BASE_URL = IS_TEST ? "http://test.groupchat.y.sdo.com" : "http://groupchat.y.sdo.com";
    public static final String MUC_REPORT_URL = MUC_BASE_URL + "/groupchat/report";
    public static final String MUC_APPLY_JOIN_URL = MUC_BASE_URL + "/groupchat/applyJoin";
    public static final String MUC_GROUP_OWNER_AGREE = MUC_BASE_URL + "/groupchat/ownerAgree";
    public static final String MUC_GROUP_USER_AGREE = MUC_BASE_URL + "/groupchat/userAgree";
    public static final String MUC_INVITE_USER = MUC_BASE_URL + "/groupchat/inviteUser";
    public static final String MUC_ROOM_SEARCH_RUL = MUC_BASE_URL+"/groupchat/roomSearch";
    public static final String MUC_ROOM_CHECKNAME = MUC_BASE_URL+"/groupchat/checkRoomName";
    public static final String MUC_USER_AGREE_ENABLE = MUC_BASE_URL + "/groupchat/userAgreeEnable";
    public static final String MUC_GROUP_REFUSE = MUC_BASE_URL + "/groupchat/refuseUser";
    public static final String MUC_QUERY_PUBLIC_URL = MUC_BASE_URL + "/groupchat/queryPublicRoom";
    
    public static final boolean REALTIME_LOG_OPENED = true;//基础服务实时日志同步开关，默认打开  lixiaohua01@0328
    
    //基础服务实时日志上传地址
    public static final String SERVICE_REALTIMELOG_UPLOAD_URL = IS_TEST ? "http://test.address.y.sdo.com/youniws/lreg.do" : "http://address.y.sdo.com/lreg.do";

    public static final boolean IS_OPEN_WIFI_SDK = false;//wifi万能钥匙接入开关,add by wangjun 20140313
    //地理位置日志上传地址
    public static final String SERVICE_LOCATION_UPLOAD_URL = IS_TEST ? "http://test.address.y.sdo.com/llbs.do" : "http://address.y.sdo.com/llbs.do";
    
    
}
