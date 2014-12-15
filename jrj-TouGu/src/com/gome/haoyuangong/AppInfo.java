package com.gome.haoyuangong;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gome.haoyuangong.bean.Stock;
import com.gome.haoyuangong.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 应用程序全局静态常量、变量声明类
 * 全局类的常量声明
 * 变量调用
 * @author guohuiz
 * 
 * 投顾项目仅使用部分内容
 * 
 */

@SuppressLint("DefaultLocale")
public class AppInfo {
  public static String DEBUG_TAG = "JRJ_mainApp";
  public static long DataSize = 0;
  public static String NetWorkErrorStr = "网络服务不可用";
  public static int SYSTEM_STATE_HEIGHT = 38;
  public static boolean isUpdateStockDic = false;
  public static boolean isLogIn = false;
//  public static String jrjUserName = "金融界网友";
  public static String jrjUserSSoid = "";
//  public static String jrjToken;
  public static final int SCREEN_SMALL=1;//宽度小于480
  public static final int SCREEN_MEDIUM=2;//宽度大于等于480
  public static final int SCREEN_LARGE=3;//宽度大于等于720
  public static final int SCREEN_BIGLARGE=4;//宽度大于等于1080
  public static final int SCREEN_QUADHD=5;//宽度大于等于1600 2k屏
  
  public static int mScreenType=-1;//当前屏幕类型，已宽度为衡量标准
  public static final int LIST_PAGE_SIZE=15;//列表每页显示最大条数
  
  public static String APP_packageName = null;///程序应用程序包名
  public final static long AUTO_CLEAN_CASHE_SIZE=10*1024*1024;//10M缓存
  public final static long AUTO_CLEAN_CASHE_NUM=400;//400张图片大概6M内存
  public static final String FILE_CACHE_PATH  = "jrj/tougu/cache/image/";///图片资源缓存文件名称
  
  public final static DecimalFormat df2 = new DecimalFormat("0.00");///2位小数 格式化工具变量
  public final static DecimalFormat df3 = new DecimalFormat("0.000");///3位小数 格式化工具变量
  public final static DecimalFormat df1 = new DecimalFormat("0.0");///1位小数 格式化工具变量
  public final static DecimalFormat df0 = new DecimalFormat("0");///取整数 格式化工具变量
  
  
  /**
   * px与dip转换函数
   * @param context
   * @param dipValue
   * @return
   */
  public static int dip2px(Context context, float dipValue){  
    final float scale = context.getResources().getDisplayMetrics().density;  
    return (int)(dipValue * scale + 0.5f);  
  }
  public static int px2dip(Context context, float pxValue){  
    final float scale = context.getResources().getDisplayMetrics().density;  
    return (int)((pxValue - 0.5f)/scale);  
  }
  /**
   * php返回utf8为 十六进制格式，如果要可视需本函数转换
   * @param theString
   * @return
   */
  public static String decodeUnicode(String theString) {
    try {
      char aChar;
      int len = theString.length();
      StringBuffer outBuffer = new StringBuffer(len);
      for (int x = 0; x < len;) {
        aChar = theString.charAt(x++);
        if (aChar == '\\') {
          aChar = theString.charAt(x++);
          if (aChar == 'u') {
            // Read the xxxx
            int value = 0;
            for (int i = 0; i < 4; i++) {
              aChar = theString.charAt(x++);
              switch (aChar) {
              case '0':
              case '1':
              case '2':
              case '3':
              case '4':
              case '5':
              case '6':
              case '7':
              case '8':
              case '9':
                value = (value << 4) + aChar - '0';
                break;
              case 'a':
              case 'b':
              case 'c':
              case 'd':
              case 'e':
              case 'f':
                value = (value << 4) + 10 + aChar - 'a';
                break;
              case 'A':
              case 'B':
              case 'C':
              case 'D':
              case 'E':
              case 'F':
                value = (value << 4) + 10 + aChar - 'A';
                break;
              default:
                throw new IllegalArgumentException(
                    "Malformed   \\uxxxx   encoding.");
              }

            }
            outBuffer.append((char) value);
          } else {
            if (aChar == 't')
              aChar = '\t';
            else if (aChar == 'r')
              aChar = '\r';
            else if (aChar == 'n')
              aChar = '\n';
            else if (aChar == 'f')
              aChar = '\f';
            outBuffer.append(aChar);
          }
        } else
          outBuffer.append(aChar);
      }
      return outBuffer.toString();
    } catch (Exception e) {
      return "";
    }
  }
  public static String EncodedUnicode(String theString) {
    boolean escapeSpace = false;
    int len = theString.length();
    int bufLen = len * 2;
    if (bufLen < 0) {
      bufLen = Integer.MAX_VALUE;
    }
    StringBuffer outBuffer = new StringBuffer(bufLen);
    for (int x = 0; x < len; x++) {
      char aChar = theString.charAt(x);
      // Handle common case first, selecting largest block that
      // avoids the specials below
      if ((aChar > 61) && (aChar < 127)) {
        if (aChar == '\\') {
          outBuffer.append('\\');
          outBuffer.append('\\');
          continue;
        }
        outBuffer.append(aChar);
        continue;
      }
      switch (aChar) {
      case ' ':
        if (x == 0 || escapeSpace) outBuffer.append('\\');
        outBuffer.append(' ');
        break;
      case '\t':
        outBuffer.append('\\');
        outBuffer.append('t');
        break;
      case '\n':
        outBuffer.append('\\');
        outBuffer.append('n');
        break;
      case '\r':
        outBuffer.append('\\');
        outBuffer.append('r');
        break;
      case '\f':
        outBuffer.append('\\');
        outBuffer.append('f');
        break;
      case '=': // Fall through
      case ':': // Fall through
      case '#': // Fall through
      case '!':
        outBuffer.append('\\');
        outBuffer.append(aChar);
        break;
      default:
        if ((aChar < 0x0020) || (aChar > 0x007e)) {
          // 每个unicode有16位，每四位对应的16进制从高位保存到低位
          outBuffer.append('\\');
          outBuffer.append('u');
          outBuffer.append(toHex((aChar >> 12) & 0xF));
          outBuffer.append(toHex((aChar >> 8) & 0xF));
          outBuffer.append(toHex((aChar >> 4) & 0xF));
          outBuffer.append(toHex(aChar & 0xF));
        } else {
          outBuffer.append(aChar);
        }
      }
    }
    return outBuffer.toString();
  }
  /**
   * 将字符串转成unicode
   * @param str 待转字符串
   * @return unicode字符串
   */
   public static String convert(String str) {
     str = (str == null ? "" : str);
     String tmp;
     StringBuffer sb = new StringBuffer(1000);
     char c;
     int i, j;
     sb.setLength(0);
     for (i = 0; i < str.length(); i++) {
       c = str.charAt(i);
       sb.append("\\u");
       j = (c >>> 8); // 取出高8位
       tmp = Integer.toHexString(j);
       if (tmp.length() == 1)
         sb.append("0");
       sb.append(tmp);
       j = (c & 0xFF); // 取出低8位
       tmp = Integer.toHexString(j);
       if (tmp.length() == 1)
         sb.append("0");
       sb.append(tmp);

     }
     return (new String(sb));
   }
  private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
    'B', 'C', 'D', 'E', 'F' };

  private static char toHex(int nibble) {
    return hexDigit[(nibble & 0xF)];
  }
  /**  
   *   
   * 基本功能：过滤指定标签  
   * <p>  
   *   
   * @param str  
   * @param tag  
   *            指定标签  
   * @return String  
   */  
  public static String fiterHtmlTag(String str, String tag) {   
      String regxp = "<\\s*" + tag + "\\s+([^>]*)\\s*>";  
      Pattern pattern = Pattern.compile(regxp);   
      Matcher matcher = pattern.matcher(str);   
      StringBuffer sb = new StringBuffer();   
      boolean result1 = matcher.find();
      while (result1) {   
          matcher.appendReplacement(sb, "");
          result1 = matcher.find();  
      }   
      matcher.appendTail(sb);   
      return sb.toString().replace("</"+tag+">", " ");   
  }   

  /**
   * 把int的color值转化成字符串（如#ff0000）
   * 
   * @param c
   * @return
   */
  public static String formatColorInt2HexString(int c) {
    String s = "";
    try {
      s = String.format("#%06X", (0xFFFFFF & c));
    } catch (Exception e) {
    }
    return s;
  }
  /**
   * 计算md5值
   * @param s 源字串
   * @return md5字串
   */
  public static String md5(String s) {
    try {
      MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
      digest.update(s.getBytes());
      byte messageDigest[] = digest.digest();

      return toHexString(messageDigest);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return "";
  }
  /**
   * helper function : 将内存二进制流转换为16进制的字串
   * 
   * @param b  二进制流
   *           
   * @return 对应的16进制的字串
   */
  @SuppressLint("DefaultLocale")
  private static String toHexString(byte[] b) { // String to byte

    StringBuilder sb = new StringBuilder(b.length * 2);
    for (int i = 0; i < b.length; i++) {
      sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
      sb.append(HEX_DIGITS[b[i] & 0x0f]);
    }
    return sb.toString().toLowerCase();
  }
  private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
    '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
  
  private static final String myStockColHomeKey = "&c=id,code,name,lcp,np,pl,hlp,stp";
  public static final String myStockColListKey = 
      "&c=id,code,name,lcp,np,pl,hlp,tm,ta,lp,hp,op,tr,sl,tmv,stp";
  public static String makeMystockHomePara(){
    return makeMystockHomePara(myStockColHomeKey);
  }
  public static String makeMystockListPara(){
    return makeMystockHomePara(myStockColListKey);
  }
  private static String makeMystockHomePara(String orderCol){
    String resultS = "q=cn|s&n=mystock_hqs"+orderCol+"&i=";
    String resultI = "q=cn|i&n=mystock_hqi"+orderCol+"&i=";
    String resultF = "q=cn|f&n=mystock_hqf"+orderCol+"&i=";
    int countS = 0;
    int countI = 0;
    int countf = 0;
    for (int i = 0; i < myStockVec.size(); i++) {
      Stock st = myStockVec.elementAt(i);
      if(st.getType().startsWith("s.s")){
        countS++;
        resultS += st.getStockCode()+",";
      }else
      if(st.getType().equals("i")){
        countI++;
        resultI += st.getStockCode()+",";
      }else
      if(st.getType().startsWith("f")){
        countf++;
        resultF += st.getStockCode()+",";
      }
    }
    String result = "";
    if(countS>0){
      result = resultS;
      if(countI>0){
        result += "&"+resultI;
      }
      if(countf>0){
        result += "&"+resultF;
      }
    }else if(countI>0){
      result = resultI;
      if(countf>0){
        result += "&"+resultF;
      }
    }else if(countf>0){
      result = resultF;
    }
    return result;
  }
  
  public static String _makeMystockHomePara(){
	  StringBuilder sb = new StringBuilder();
	  for (int i = 0; i < myStockVec.size(); i++) {
		  sb.append(myStockVec.get(i).getStockCode());
	  }
	  return sb.toString();
  }
  
  public static String getStopPercentage(double l,double curP) {
    if(curP==0){
      return "停牌";
    }
    String per = df2.format(l)+"%";
    if(l > 0)
      per = "+" + per;
    return per;
  }
  public static String getStopPercentage(double l,double curP,double lastP) {
    if(lastP==0){
      return "未上市";
    }else
    if(curP==0){
      return "停牌";
    }
    String per = df2.format(l)+"%";
    if(l > 0)
      per = "+" + per;
    return per;
  }
  public static String getPercentage(double l) {
//    BigDecimal bd = new BigDecimal(l+"");
//    String per = bd.setScale(dec, BigDecimal.ROUND_HALF_UP).toString()+ "%";
    String per = df2.format(l)+"%";
    if(l > 0)
      per = "+" + per;
    return per;
  }
  public static String getPercentageRatio(double l) {
  String per = df2.format(l)+"%";
  return per;
}
  public static int MYCOLOR_GRAY,MYCOLOR_RED,MYCOLOR_GREEN;
  public static int getUpDownColor(double diff){
//    int res = valuePrice.compareTo(valuePreP);
    if(diff==0){
      return MYCOLOR_GRAY;
    }else{
      if(diff<0){
        return MYCOLOR_GREEN;
      }else{
        return MYCOLOR_RED;
      }
    }
  }
  
  /**
   * 价格转换，如果为0 输出为 -
   * @param l
   * @param dec
   * @return
   */
  public static String DiffDoubleToString(double l, int dec) {
    String ss = "--";
//    if(l==0){
//      return ss;
//    }
    if(dec == 3){
      ss = df3.format(l);
    }else if(dec == 1){
      ss = df1.format(l);
    }else{
      ss = df2.format(l);
    }
    if(l>0){
      ss = "+"+ss;
    }
    return ss;
  }
  /**
   * 价格转换，如果为0 输出为 -
   * @param l
   * @param dec
   * @return
   */
  public static String PriceDoubleToString(double l, int dec) {
    String ss = "--";
    if(l==0){
      return ss;
    }
    if(dec == 3){
      ss = df3.format(l);
    }else if(dec == 1){
      ss = df1.format(l);
    }else{
      ss = df2.format(l);
    }
    return ss;
  }
  /**
   * 量、额 数据进行万亿转换
   * @param number
   * @return
   */
  public static String doubleToStringVol(double number) {
    String ss = "";
    if(number > 100000000||number <= -100000000){
      ss = df2.format(number/100000000f)+"亿";
    }else if(number >= 100000||number <= -100000){
      ss = df2.format(number/10000f)+"万";
    }
    else{
      ss = df2.format(number);
    }
    return ss;
  }
  /**
   * 量、额 数据进行万亿转换
   * @param number
   * @return
   */
  public static String doubleToStringVol2(double number) {
  	String ss = "";
  	if(number > 100000000||number <= -100000000){
  		ss = df2.format(number/100000000f)+"亿";
  	}else if(number >= 100000||number <= -100000){
  		ss = df2.format(number/10000f)+"万";
  	}
  	else{
  		ss = df0.format(number);
  	}
  	return ss;
  }
  public static String doubleToStringAmo(double number) {
    String ss = "";
    number = number*10000f;
    if(number > 100000000||number <= -100000000){
      ss = df2.format(number/100000000f)+"亿";
    }else if(number >= 100000||number <= -100000){
      ss = df2.format(number/10000f)+"万";
    }
    else{
      ss = df2.format(number);
    }
    return ss;
  }
  public static String getFormatRefreshTime(){
  	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm",Locale.US);
  	return df.format(new Date(System.currentTimeMillis()));
  }
  
  /**
   * 自选股Vector容器
   */
  public static Vector<Stock> myStockVec = new Vector<Stock>();
  /**
   * 最大自选股条数
   */
  public static final int MYSTOCK_MAX_COUNT = 50;
  
  
  public static  void initQuoteColor(Context ctx) {
  	Resources resources = ctx.getResources();
		AppInfo.MYCOLOR_GRAY = resources
				.getColor(R.color.quote_gray_color);
		AppInfo.MYCOLOR_RED = resources.getColor(R.color.quote_red_color);
		AppInfo.MYCOLOR_GREEN = resources.getColor(
				R.color.quote_green_color);
	}
  public static void init(Context ctx){
  	initQuoteColor(ctx);
  }
  /**
   * 是否在wifi下
   * @param mContext
   * @return
   */
  public static boolean isWifi(Context mContext) {  
      ConnectivityManager connectivityManager = (ConnectivityManager) mContext  
              .getSystemService(Context.CONNECTIVITY_SERVICE);  
      NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();  
      if (activeNetInfo != null  
              && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {  
          return true;  
      }  
      return false;  
  }  
}
