package com.gome.haoyuangong.utils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.SparseArray;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.net.result.live.LinkPicBean;
import com.gome.haoyuangong.net.url.NetUrlTougu;

public class HtmlUtils {

	public static SpannableStringBuilder parseImgTag(Context context,int screenWidth,String content, LinkPicBean loi) {
		SpannableStringBuilder ssBuilder = new SpannableStringBuilder();
		String ss = content;
		try {
			int pos = ss.indexOf("<img");
			if (pos == -1) {
				ssBuilder.append(Html.fromHtml(ss));
				return ssBuilder;
			}
			int lastPos = 0;
			while (pos != -1) {
				if (lastPos != pos) {
					ssBuilder.append(Html.fromHtml(ss.substring(lastPos, pos)));
				}
				int pos2 = ss.indexOf(">", pos);
				if (pos2 != -1) {
					String imgTag = ss.substring(pos, pos2 + 1);
					String src = getMatcher("src=\"(.*?)\"", imgTag);
					boolean isEmote = false;
					int ePos = src.lastIndexOf("/");
					if (src.startsWith("http://i0.jrjimg.cn/live/emote")) {
						isEmote = true;
					} else {
						if (ePos != -1 && src.substring(0, ePos).endsWith("emote")) {
							isEmote = true;
						}
					}
					if (isEmote && ePos != -1) {
						String tempStr = "emote" + src.substring(ePos);
						ssBuilder.append(getImageSpan(context,tempStr));
					} else {
						if (screenWidth >= 720) {
							src = src.replace("_s.jpg", "_m.jpg");
						}
						if(loi !=null){
							loi.setLinkPic(src);
						}
					}
					lastPos = pos2 + 1;
					pos = ss.indexOf("<img", pos2);
					if (pos == -1) {
						if (lastPos < ss.length()) {
							ssBuilder.append(Html.fromHtml(ss.substring(lastPos)));
						}
					}
				} else {
					// Log.e(JRJ_GlobalData.DEBUG_TAG, content);
					ssBuilder.append(Html.fromHtml(ss.substring(pos)));
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ssBuilder;
	}

	public static String getMatcher(String regex, String source) {
		String result = "";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(source);
		while (matcher.find()) {
			result = matcher.group(1);// 只取第一组
		}
		return result;
	}

	public static SpannableString getImageSpan(Context context , String imgTag) {
//		initEmoteHashMap();
		SpannableString result = new SpannableString(imgTag);
		Integer resId = emoteHashTable.get(imgTag).getResId();
		if (resId == null) {
			resId = R.drawable.zb_emote_failed;
		}
		// 得到drawable对象，即所要插入的图片
		Drawable d = context.getResources().getDrawable(resId);
		float sizeV = d.getIntrinsicWidth()/** density */
		;
		// if(sizeV<m_contentFontH){
		// sizeV = m_contentFontH;
		// }
		d.setBounds(0, 0, (int) sizeV, (int) sizeV);
		ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
		result.setSpan(span, 0, imgTag.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		return result;
	}
	
	static HashMap<String, EmoBean> emoteHashTable = new HashMap<String, EmoBean>(64);
	static SparseArray<EmoBean> emoteMap = new SparseArray<EmoBean>();
	

	static{
			int[] ids = { R.drawable.emote0, R.drawable.emote1, R.drawable.emote2, R.drawable.emote3,
					R.drawable.emote4, R.drawable.emote5, R.drawable.emote6, R.drawable.emote7, R.drawable.emote8,
					R.drawable.emote9, R.drawable.emote10, R.drawable.emote11, R.drawable.emote12, R.drawable.emote13,
					R.drawable.emote14, R.drawable.emote15, R.drawable.emote16, R.drawable.emote17, R.drawable.emote18,
					R.drawable.emote19, R.drawable.emote20, R.drawable.emote21, R.drawable.emote22, R.drawable.emote23,
					R.drawable.emote24, R.drawable.emote25, R.drawable.emote26, R.drawable.emote27, R.drawable.emote28,
					R.drawable.emote29, R.drawable.emote30, R.drawable.emote31, R.drawable.emote32, R.drawable.emote33,
					R.drawable.emote34, R.drawable.emote35, R.drawable.emote36, R.drawable.emote37, R.drawable.emote38,
					R.drawable.emote39, R.drawable.emote40, R.drawable.emote41, R.drawable.emote42, R.drawable.emote43,
					R.drawable.emote44, R.drawable.emote45, R.drawable.emote46, R.drawable.emote47, R.drawable.emote48,
					R.drawable.emote49, R.drawable.emote50, R.drawable.emote51, R.drawable.emote52, R.drawable.emote53,
					R.drawable.emote54, R.drawable.emote55, R.drawable.emote56, R.drawable.emote57, R.drawable.emote58,
					R.drawable.emote59, R.drawable.emote60, R.drawable.emote61, R.drawable.emote62, R.drawable.emote63 };
			int[] codePoint = {
					0x1f436,0x1f43a,0x1f431,0x1f42d,0x1f439,0x1f430,0x1f438,0x1f42f,0x1f428,0x1f43b,0x1f437,0x1f43d,0x1f42e,0x1f417,0x1f435,0x1f412,0x1f434,0x1f411,0x1f418,0x1f43c,0x1f427,0x1f426,0x1f424,0x1f425,0x1f423,0x1f414,0x1f40d,0x1f422,0x1f41b,0x1f41d,0x1f41c,0x1f41e,0x1f40c,0x1f419,0x1f41a,0x1f420,0x1f41f,0x1f42c,0x1f433,0x1f40b,0x1f404,0x1f40f,0x1f400,0x1f403,0x1f405,0x1f407,0x1f409,0x1f40e,0x1f410,0x1f413,0x1f415,0x1f416,0x1f401,0x1f402,0x1f432,0x1f421,0x1f40a,0x1f42b,0x1f42a,0x1f406,0x1f408,0x1f429,0x1f43e,0x1f490
			};
			for (int i = 0; i < ids.length; i++) {
				String name = "emote/" + i + ".gif";
				String eName = newString(codePoint[i]);
				EmoBean emoBean = new EmoBean(name,codePoint[i],ids[i],eName);
				emoteHashTable.put(name, emoBean);
				emoteMap.put(codePoint[i], emoBean);
			}
	}
	
	public static EmoBean getEmotionResId(String key){
		return emoteHashTable.get(key);
	}
	
	public static EmoBean getEmotionByCode(int codePoint){
		return emoteMap.get(codePoint);
	}
	
	public static class EmoBean{
		private String fileName;
		private int codePoint;
		private int resId;
		private String eName;
		public EmoBean(String fileName,int codePoint,int resId,String eName){
			this.fileName = fileName;
			this.codePoint = codePoint;
			this.resId = resId;
			this.eName = eName;
		}
		
		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public int getCodePoint() {
			return codePoint;
		}
		public void setCodePoint(int codePoint) {
			this.codePoint = codePoint;
		}
		public int getResId() {
			return resId;
		}
		public void setResId(int resId) {
			this.resId = resId;
		}

		public String geteName() {
			return eName;
		}

		public void seteName(String eName) {
			this.eName = eName;
		}
		
	}
	
	public static final String newString(int codePoint) {
        if (Character.charCount(codePoint) == 1) {
            return String.valueOf(codePoint);
        } else {
            return new String(Character.toChars(codePoint));
        }
    }
	
	public static String getSenderText(Spannable spannable){
		StringBuilder sb = new StringBuilder();
		ImageSpan[] imageSpans = spannable.getSpans(0, spannable.length(), ImageSpan.class);
		if(imageSpans == null || imageSpans.length == 0){
			sb.append(spannable.toString());
		}else{
			int length = spannable.length();
			int skip = 0;
			for(int i = 0 ;  i < length ; i+=skip){
				skip = 0;
				int unicode = Character.codePointAt(spannable, i);
				skip = Character.charCount(unicode);
				if(unicode > 0xff){
					EmoBean emoBean = getEmotionByCode(unicode);
					if(emoBean != null){
						sb.append("<img src=\""+NetUrlTougu.BIAOQING+emoBean.getFileName()+"\" height=\"26\" width=\"26\"/>");
					}else{ 
						sb.append(spannable.subSequence(i, i+1));
					}
				}else{
					sb.append(spannable.subSequence(i, i+1));
				}
			}
		}
		
		return sb.toString();
	}
}
