package com.gome.haoyuangong.utils.next;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class FileUtil {
    public static final String SDCARD_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
                                                  +File.separator
                                                  +"tougu";
    
    public static Uri saveDataToSDCard(Context context, byte[] data, String filename)
    {
        return saveDataToSDCard(context, data, filename, SDCARD_STORAGE_PATH);
    }
    
    public static Uri saveDataToSDCard(Context context, byte[] data, String filename, String dir)
    {
        Uri uri = null;
        
        FileOutputStream fileOS = null;
        try{
            File file = new File(dir,filename);
            File fdir = new File(dir);
            fdir.mkdirs();
//            file.mkdirs();
            fileOS = new FileOutputStream(file);
            fileOS.write(data);

        }catch(Exception e)
        {
            e.printStackTrace();
        }finally{
        	if(fileOS!=null)
            try {
                fileOS.close();
           } catch (java.io.IOException e) {
                 ;// do nothing
                 e.printStackTrace();
           }
        }
        
        return uri;
    }
    
    public static byte[] loadDataFromSDCard(Context context, String filename)
    {
        byte[] data = null;
        File file = new File(SDCARD_STORAGE_PATH,filename);

        FileInputStream is = null;
        try{
            is = new FileInputStream(file);
            int length = is.available();
            data = new byte[length];
            is.read(data); 

        }catch(Exception e)
        {
            
        }finally{
        	if(is!=null)
            try {
                is.close();
           } catch (java.io.IOException e) {
                 ;// do nothing
           }
        }
        
        return data;
    }
    
    public static void deleteFileFromSDCard(Context context, String filename)
    {
        File file = new File(SDCARD_STORAGE_PATH, filename);
        file.delete();
    }
    
    public static void deleteFile(Context context, String filename, String dir)
    {
        File file = new File(dir, filename);
        if(file.exists())
            file.delete();
    }
    
    public static void copyFile(Context context, String filename_from, String dir_from, String filename_to, String dir_to)
    {
        //copy file from (dir_from, filename_from) to (dir_to, filename_to)
    	FileInputStream is = null;
    	FileOutputStream os = null;
    	try {
        	File from = null;
        	if(dir_from == null)
        		from = new File(filename_from);
        	else
        		from = new File(dir_from,filename_from);
        	File to = null;
        	if(dir_to == null){
        		to = new File(filename_to);
        	}else
        		to = new File(dir_to, filename_to);
            is = new FileInputStream(from);
            os = new FileOutputStream(to);
            
            byte[] b = new byte[1024];
            
            int r;
                while((r=is.read(b))!=-1)
                {
                    os.write(b,0,r);
                }
            
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
        	if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	if(os!=null)
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
    }
    
    public static void copyFile(Context context, String file_from, String filename_to, String dir_to)
    {
        //copy file from (dir_from, filename_from) to (dir_to, filename_to)
    	FileInputStream is = null;
    	FileOutputStream os = null;
        try {
            is = new FileInputStream(new File(file_from));
            os = new FileOutputStream(new File(dir_to,filename_to));
            
            byte[] b = new byte[1024];
            
            int r;
                while((r=is.read(b))!=-1)
                {
                    os.write(b,0,r);
                }

            
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
        	if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	if(os!=null)
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
    }
    
    public static void saveTextToSDCard(Context context, String content, String filename, String dir)
    {
    	FileOutputStream is = null;
		try {
			is = new FileOutputStream(new File(dir,filename));	    	
	    	//try {
	    		is.write(content.getBytes());
			//	is.close();
			//} catch (IOException e) {
				// TODO Auto-generated catch block
		//		e.printStackTrace();
		//	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
    }
    
    public static String loadTextFromSDCard(Context context, String filename, String dir)
    {
        String ret = null;
        File file = new File(dir,filename);
        FileInputStream is = null;
        try{
            is = new FileInputStream(file);
            int length = is.available();
            byte[] data = new byte[length];
            is.read(data);

           ret = new String(data);
        }catch(Exception e)
        {
            
        }finally{
            if(is!=null)
        	try {
                is.close();
           } catch (java.io.IOException e) {
                 ;// do nothing
           }
        }
        
        return ret;
    }
    public static String loadTextFromSDCard(Context context, String filename, String dir, String encoding)
    {
        String ret = null;
        File file = new File(dir,filename);
        FileInputStream is = null;
        try{
            is = new FileInputStream(file);
            int length = is.available();
            byte[] data = new byte[length];
            is.read(data);

            ret = new String(data, encoding);
        }catch(Exception e)
        {
            
        }finally{
        	if(is!=null)
            try {
                is.close();
            } catch (java.io.IOException e) {
                ;// do nothing
            }
        }
        
        return ret;
    }

    public static boolean hasFileInSDCard(Context context, String filename, String dir)
    {        
        File file = new File(dir, filename);
        return file.exists();
    }
    
    public static boolean hasFileInSDCard(Context context, String filename)
    {
        return hasFileInSDCard(context, filename, SDCARD_STORAGE_PATH);
    }
    
    public static boolean checkFileExistWithFullPath(Context context, String filename)
    {
        File file = new File(filename);
        return file.exists();
    }
    
    public static int getFileSize(String filename, String dir)
    {
        File file = new File(dir,filename);
        int size = (int)(file.length());
        return size;
    }
    
    public static long getFileSize2(String filename)
    {
    	File file = new File(filename);
    	return file.length();
    }
    
    public static long getFileSize2(String filename, String dir)
    {
        File file = new File(dir,filename);
        return file.length();
    }
    
    public static boolean checkFileExist(String filename, String dir)
    {
    	if (TextUtils.isEmpty(filename) || TextUtils.isEmpty(dir)) {
    		return false;
    	}
    	
    	File file = new File(dir, filename);
    	if(file.exists())
    	{
    		return true;
    	}
    	else 
    		return false;
    }
    
    public static String getNewFileName(String filename, String dir)
    {
    	int i=1;
    	int pos = filename.lastIndexOf(".");
    	String type = "";
    	String name = filename;
    	if(pos>0)
    	{
    		type = filename.substring(pos);
    		name = filename.substring(0,pos);
    	}
    	if(!checkFileExist(name+type,dir) && !checkFileExist(name+type+"_tmp",dir))
    	{
    		return name+type;
    	}
    	while(true)
    	{
    		if(checkFileExist(name+"("+i+")"+type,dir) || checkFileExist(name+"("+i+")"+type+"_tmp",dir))
    		{
    			i++;
    		}
    		else
    		{
    			return name+"("+i+")"+type;
    		}
    	}
    }

    /* 判断文件MimeType的method */
    public static String getMIMEType(File f) 
    { 
        String type="";
        String fName=f.getName();
        /* 取得扩展名 */
        String end=fName.substring(fName.lastIndexOf(".")
                +1,fName.length()).toLowerCase(); 

        /* 依扩展名的类型决定MimeType */
        if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
                end.equals("xmf")||end.equals("ogg")||end.equals("wav") ||
                end.equals("m3u") || end.equals("m4b") || end.equals("m4p") || 
                end.equals("mp2") || end.equals("mp3") || end.equals("mpga") ||
                end.equals("ogg") || end.equals("wma") || end.equals("ape") ||
                end.equals("flac") || end.equals("amr") || end.equals("aac")
        		)
        {
            type = "audio/*"; 
        }
        else if(end.equals("3gp")||end.equals("mp4") || end.equals("avi") || end.equals("asf") ||
        		end.equals("m4u") || end.equals("m4v") || end.equals("mov") || end.equals("mpe") ||
        		end.equals("mpeg") || end.equals("mpg") || end.equals("mpg4") || end.equals("rmvb") || 
        		end.equals("wmv")
        		)
        {
            type = "video/*";
        }
        else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
                end.equals("jpeg")||end.equals("bmp") || end.equals("tif"))
        {
            type = "image/*";
        }
        else if(end.equals("apk")) 
        { 
            /* android.permission.INSTALL_PACKAGES */ 
            type = "application/vnd.android.package-archive"; 
        } 
        else if(end.equals("txt"))
        {
        	type = "text/plain";
        }
        else if(end.equals("html") || end.equals("htm") || end.equals("xml") || end.equals("xhtml"))
        {
        	type = "text/html";
        }
        else if(end.equals("pdf"))
        {
        	type = "application/pdf";
        }
        else if(end.equals("epub"))
        {
        	type = "application/epub+zip";
        }
        else if(end.equals("chm"))
        {
        	type = "application/x-chm";
        }
        else if(end.equals("doc") || end.equals("docx"))
        {
        	type = "application/msword";
        }
        else if(end.equals("xls") || end.equals("xlsx"))
        {
        	type = "application/vnd.ms-excel";
        }
        else if(end.endsWith("ppt") || end.equals("pptx"))
        {
        	type = "application/vnd.ms-powerpoint";
        }
        else if(end.equals("zip") || end.equals("tar") || end.equals("gz") ||
        		end.equals("rar") || end.equals("7z") || end.equals("z"))
        {
        	type = "application/zip";
        }
        else
        {
            type="*/*";
        }

        return type;  
    }
    
    
    private static final long K = 1024;
    private static final long M = K * K;
    private static final long G = M * K;

    public static String normalize(long data) {
        if (data > G)
            return String.format("%.2fG", data / (double)G);
        else if (data > M)
            return String.format("%.2fM", data / (double)M);
        else if (data > K)
            return String.format("%.2fK", data / (double)K);
        else
            return String.valueOf(data + "B");
    }
    
    public static Uri getUriFromPath(Context context, String path) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;
        Uri retUri = null;
        try {
        	cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[] { MediaStore.Images.ImageColumns._ID },
                    MediaStore.Images.ImageColumns.DATA + "=?", new String[]{path}, null);
            if(cursor!=null && cursor.moveToFirst()){
            	retUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getString(0));
            }
            if(retUri!=null)
            	return retUri;
        }finally{
        	if(cursor!=null)
        		cursor.close();
        }
        cursor = null;
        try{
        	 cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    new String[] { MediaStore.Video.VideoColumns._ID },
                    MediaStore.Video.VideoColumns.DATA + "=?", new String[]{path}, null);
            if(cursor!=null && cursor.moveToFirst()){
            	retUri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, cursor.getString(0));
            }
            if(retUri!=null)
            	return retUri;
        }finally{
            if(cursor!=null)
            	cursor.close();
        }
        cursor = null;
        try{          
            cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    new String[] { MediaStore.Audio.AudioColumns._ID },
                    MediaStore.Audio.AudioColumns.DATA + "=?", new String[]{path}, null);
            if(cursor!=null && cursor.moveToFirst()){
            	retUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor.getString(0));
            }

            if(retUri!=null)
            	return retUri;
        }catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	if (cursor != null)
        		cursor.close();
        } 
    	return null;
    }

    public static String getPathFromUri(Context context, Uri contentUri) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;
        Uri retUri = null;
        try {
            String path = contentUri.getPath();
            if (path.contains("images")) {
                cursor = resolver.query(contentUri,
                        new String[] { MediaStore.Images.ImageColumns.DATA },
                        null, null, null);
                cursor.moveToFirst();
                retUri = Uri.fromFile(new File(cursor.getString(0)));
            } else if (path.contains("video")) {
                cursor = resolver.query(contentUri,
                        new String[] { MediaStore.Video.VideoColumns.DATA },
                        null, null, null);
                cursor.moveToFirst();
                retUri = Uri.fromFile(new File(cursor.getString(0)));
            }	else if (path.contains("audio")) {
                cursor = resolver.query(contentUri,
                        new String[] { MediaStore.Audio.AudioColumns.DATA },
                        null, null, null);
                cursor.moveToFirst();
                retUri = Uri.fromFile(new File(cursor.getString(0)));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        if(retUri!=null)
        	return retUri.getPath();
        else
        	return null;
    }
    
    public static Uri saveDataToSDCardAppend(Context context, byte[] data, String filename)
    {
        return saveDataToSDCardAppend(context, data, filename, SDCARD_STORAGE_PATH);
    }
    
    public static Uri saveDataToSDCardAppend(Context context, byte[] data, String filename, String dir)
    {
        Uri uri = null;
        
        FileOutputStream fileOS = null;
        try{
            File file = new File(dir,filename);
            File fdir = new File(dir);
            fdir.mkdirs();
//            file.mkdirs();
            fileOS = new FileOutputStream(file,true);
            fileOS.write(data);

        }catch(Exception e)
        {
            e.printStackTrace();
        }finally{
        	if(fileOS!=null)
            try {
                fileOS.close();
           } catch (java.io.IOException e) {
                 ;// do nothing
                 e.printStackTrace();
           }
        }
        
        return uri;
    }
}
