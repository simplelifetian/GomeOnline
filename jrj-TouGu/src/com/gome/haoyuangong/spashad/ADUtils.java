/**
 * 
 */
package com.gome.haoyuangong.spashad;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 */
public class ADUtils {

	public static Date paserString(String dateStr, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		Date d;
		try {
			d = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
		return d;
	}

	public static long paserDate(Date date) {
		if (date == null) {
			return 0;
		}
		return date.getTime();
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.trim().length() == 0 || str.equalsIgnoreCase("null")) {
			return true;
		}
		return false;
	}
}
