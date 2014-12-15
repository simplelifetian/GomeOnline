/**
 * 
 */
package com.gome.haoyuangong.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;


/**
 * @author tongzhui.peng
 * 
 */
public final class StringUtils {
	
	private static final String PASSWD_CHECK = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,}$";
//MD5加密
	public static String md5(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.length() == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isBlank(String str) {
		if (str == null || str.length() == 0 || str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public static String maskMobile(String mobileno) {
		if (StringUtils.isEmpty(mobileno)) {
			return null;
		}
		return mobileno.replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1****$3");
	}
	
	public static String maskIdNumber(String idNum) {
		if (StringUtils.isEmpty(idNum)) {
			return null;
		}
		return idNum.replaceAll("(\\w{3})(\\w{8,11})(\\w{4})", "$1***********$3");
	}
	
	public static final String TEXT_FORMAT = ""
			+"<html>"
			+"<head>"
			+"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
			+"<title>帮助</title>"
			+"</head>"
			+"<style type=\"text/css\">"
			+"    body {margin:0;padding:0;font-size:16px;font-family:\"微软雅黑\";background-color:#ffffff;padding:10px;}"
			+"h1 {font-size:18px;margin:10px 0 6px 0;}"
			+".color_red {color:#c00;}"
			+"a {color:#1515ef; text-decoration:none;}"
			+"a:hover {color:#c00; text-decoration:underline;}"
			+"p{line-height:25px;margin:0; text-indent:0em;}"
			+".Explain p {text-indent:0;}"
			+".pe2 a{color:#c00;}"
			+".mt30{margin-top:30px;}"
			+"</style>"
			+"<body>"
			+"    %@"
			+"</body>"
			+"</html>";

	public static String formatHtmlFragment(String str){
		if(str == null){
			str = "";
		}
		return TEXT_FORMAT.replace("%@", str);
	}
	
	public static boolean passIsAvaliable(String passwd){
		if (StringUtils.isBlank(passwd) || !passwd.matches(PASSWD_CHECK)) {
			return false;
		}
		return true;
	}
	
	public static boolean containsStringRegex(String total,String reg){
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(total);
		if(m.find()){
			return true;
		}
		return false;
	}
	
	public static String transToGetParams(String oldStr){
		StringBuilder sb = new StringBuilder();
		if(StringUtils.isEmpty(oldStr)){
			return sb.toString();
		}
		String[] args = oldStr.split("&");
		for(String pairs : args){
			if(StringUtils.isEmpty(pairs))
				continue;
			int index = pairs.indexOf("=");
			if(index < 0){
				continue;
			}
			String name = pairs.substring(0, index);
			String value = pairs.substring(index+1);
			if(!StringUtils.isEmpty(name)&&!StringUtils.isEmpty(value)){
				
				String newValue;
				try {
					newValue = URLEncoder.encode(value, "utf8");
					sb.append("&").append(name).append("=").append(newValue);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					return "";
				}
			}
			
		}
		if(sb.length() > 0){
			return sb.substring(1);
		}else{
			return sb.toString();
		}
	}
	public static boolean isPhoneNum(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}
		if(str.length()==11){
			return true;
		}
		return false;
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
	  
	  
	  public static String dealColorTag(String src){
		    String result = src;
		    try {
		      
		        int pos = -1;
		        if(src.indexOf("span")!=-1){
//		          System.out.println("old:"+src);
		          src = src.replace("span", "font");
		          result = src;
		          try {
		            while((pos=src.indexOf("style=\"color:"))!=-1){
		              src = src.substring(0, pos)+"color=\""+src.substring(pos+13);
		              int pos2 = src.indexOf(";\">",pos);
		              if(pos2!=-1){
		                src = result = src.substring(0, pos2)+src.substring(pos2+1);
		              }else{
		                result = src;
		              }
		            }
		            result = result.replace("color=\" #", "color=\"#");
		            result = result.replace("color=\"\n#", "color=\"#");
		          } catch (Exception e) {
		          }
		        }
		      
		      
//		      if((pos=result.indexOf("<font kwname="))!=-1){
//		        if(result.indexOf("</font>",pos)!=-1){
//		          result = result.substring(0, pos)+result.substring(7+src.indexOf("</font>",pos));
//		        }
//		      }
		        result = result.replace("img\n", "img ");
		        
		      src= result;
//		      int k = 0;
//		      int pos2 = -1;
//		      pos=src.indexOf("<");
//		      while(k<200&&pos!=-1){
//		        pos2 = src.indexOf(">",pos);
//		        if(pos2==-1){
//		          break;
//		        }
//		        if(pos!=0){
//		          result = src.substring(0, pos);
//		        }else{
//		          result = "";
//		        }
//		        result += src.substring(pos, pos2).replace("\n", " ");
//		        result +=src.substring(pos2);
//		        
//		        src = result ; 
//		        pos=src.indexOf("<",pos2);
//		        k++;
//		      }
//		      System.out.println("find "+k+" 个<> Tag");
		    } catch (Exception e) {
		      result = src;
		    }
		    return result;
		  }
	  
	  public static String replaceAllTag(String content){
		  if(StringUtils.isEmpty(content)){
			  return "";
		  }
		  return content.replaceAll("「|」", "");
	  }
}
