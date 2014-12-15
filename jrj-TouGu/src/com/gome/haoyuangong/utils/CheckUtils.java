package com.gome.haoyuangong.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtils {
	private static String regexstrName = "^[A-Za-z0-9\u4E00-\u9FA5][A-Za-z0-9\u4E00-\u9FA5\\-_\\.]*$";
	private static String regexstrLength = "[^x00-xff]";
	private static String regexstrPnone = "^1\\d{10}$";
	private static String regexstrPw = "^[0-9a-zA-Z\\w~!@#\\$%\\^&\\*\\(\\)\\+`\\-=\\[\\]\\\\\\{\\}\\|;':\"\\,\\./<>\\?]{6,20}$";

	public static CheckResponse CheckUserName(String name) {
		Pattern pattern = Pattern.compile(regexstrName);
		Matcher matcher = pattern.matcher(name);
		CheckResponse cr = new CheckResponse();
		if (matcher.matches()) {
			pattern = Pattern.compile(regexstrPnone);
			matcher = pattern.matcher(name);
			if(matcher.matches()){
				cr.isValid=false;
				cr.rtMsg = "请不要使用邮箱或手机当做用户名";		
				return cr;
			}
			int size = getStrLength(name);
			if (size >= 2 && size <= 30) {
				cr.isValid=true;
				cr.rtMsg = "";		
				return cr;
			}else{
				cr.isValid=false;
				cr.rtMsg = "用户名长度限制在2-30位";		
			}
		}else{
			cr.isValid=false;
			cr.rtMsg = "请使用汉字、数字、字母开头并无特殊字符的用户名注册";
		}
		return cr;
	}

	/**
	 * 返回字符串长度 汉字为2 其余为1
	 * 
	 * @param str
	 * @return
	 */
	public static int getStrLength(String str) {
		if (str == null)
			return 0;
		Pattern pattern = Pattern.compile(regexstrLength);
		Matcher matcher = pattern.matcher(str);
		int size = str.length();
		while (matcher.find()) {
			size++;
		}
		return size;
	}

	/**
	 * 判断
	 * 
	 * @param phone
	 * @return
	 */
	public static CheckResponse isValidPhoneNum(String phone) {
		Pattern pattern = Pattern.compile(regexstrPnone);
		Matcher matcher = pattern.matcher(phone);
		CheckResponse cr = new CheckResponse();
		if (matcher.matches()) {
			cr.isValid = true;
			cr.rtMsg = "";
		} else {
			cr.isValid = false;
			cr.rtMsg = "手机号格式错误，请重新输入";
		}
		return cr;
	}
	
	public static CheckResponse isValidPassword(String pw){
		CheckResponse cr = new CheckResponse();
		if(pw==null||pw.length()<=0){
			cr.isValid=false;
			cr.rtMsg="请输入密码";
			return cr;
		}
		Pattern pattern = Pattern.compile(regexstrPw);
		Matcher matcher = pattern.matcher(pw);
		if(pw.contains(" ")){
			cr.isValid=false;
			cr.rtMsg="密码不能包含空格";
			return cr;
		}
		if(pw.length()<6||pw.length()>20){
			cr.isValid=false;
			cr.rtMsg="密码长度限制在6-20位";
			return cr;
		}
		if(matcher.matches()){
			Pattern pattern1 = Pattern.compile("^\\d{1,8}$");
			Matcher matcher1 = pattern1.matcher(pw);
			if(matcher1.matches()){
				cr.isValid=false;
				cr.rtMsg="密码不能是小于9位的纯数字";
			}else{
				cr.isValid=true;
				cr.rtMsg="";
			}
		}else{
			cr.isValid=false;
			cr.rtMsg="请勿使用除“数字、字母、符号”以外的密码内容";
		}
		return cr;
	}

	public static class CheckResponse {
		public boolean isValid;
		public String rtMsg;
	}
}
