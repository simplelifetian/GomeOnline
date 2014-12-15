package com.gome.haoyuangong.utils.next;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static final int BUF_SIZE = 4096;
	public static String encrypt(String src) {
        MessageDigest md5=null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
       /* char[] charArray = src.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];*/

        byte[] md5Bytes=null;
		try {
			byte[] sb=src.getBytes("utf-8");
			//md5.update(sb);
			md5Bytes = md5.digest(/*byteArray*/sb);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return byteToHex(md5Bytes);
	}
	
	public static String encrypt(File file){
	    FileInputStream in = null;
	    try{
    	    MessageDigest messagedigest = MessageDigest.getInstance("MD5");
    	    in = new FileInputStream(file);
    	    byte[] buffer = new byte[BUF_SIZE];
    	    int byteCount;
    	    while ((byteCount = in.read(buffer)) != -1) {
    	        messagedigest.update(buffer, 0, byteCount);
            }
    	    return byteToHex(messagedigest.digest());
	    }catch(IOException e){
	        e.printStackTrace();
	        return null;
	    } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }finally{
            try {
                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
	}
	
	public static String byteToHex(byte[] md5Bytes){
	    StringBuffer hexValue = new StringBuffer();
	    for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(encrypt("sfsd中"));
		
	}

}
