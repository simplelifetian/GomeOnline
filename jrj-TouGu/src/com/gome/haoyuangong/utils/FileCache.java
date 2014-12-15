package com.gome.haoyuangong.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.gome.haoyuangong.SetupInfo;


/**
 * 文件缓存类，用于将数据文件存储读取操作
 * @author guohuiz from opensource
 */
public class FileCache {
  

  private File cacheDir;//当前文件对象
  
  /**
   * @param context
   */
  public FileCache(Context context,String aPath) {
    if(ExistSDCard()) {
      cacheDir = new File(Environment.getExternalStorageDirectory(), aPath);
      makeRootDirectory(aPath);
    } else {
      cacheDir = null;//context.getCacheDir();
    }
//    if(!cacheDir.exists()) {
//      cacheDir.mkdir();
//    }
  }
  
  /**
   * 判断是否存在sd卡
   */
  private boolean ExistSDCard() {  
    if (android.os.Environment.getExternalStorageState().equals(  
      android.os.Environment.MEDIA_MOUNTED)) {  
     return true;  
    } else  
     return false;  
   } 
  
  /**
   * 以url为名称写入数据
   */
  public void addToFileCache(String url, InputStream inputStream) {
    OutputStream outputStream = null;
    try {
      if(cacheDir!=null){
        outputStream = new FileOutputStream(getFromFileCache(url));
        copyStream(inputStream, outputStream);
      } 
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      if(outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }
  public void addBitmapToFileCache(String url,Bitmap b){
  	 OutputStream outputStream = null;
  	try {
  		if(cacheDir!=null){
        outputStream = new FileOutputStream(getFromFileCache(url));
        b.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
      } 
		} catch (FileNotFoundException e) {
		}
  }
  
  /**
   * 根据名称读取文件数据
   */
  public File getFromFileCache(String url) {
    String fileName = urlToFileName(url)+url.substring(url.length()-3, url.length());
    File file = null;
    
    if(cacheDir!=null){
      file = new File(cacheDir, fileName);
    }
    return file;
  }
  public void DelFromFileCache(String url) {
    String fileName = urlToFileName(url)+url.substring(url.length()-3, url.length());
    File file = null;
    
    if(cacheDir!=null){
      file = new File(cacheDir, fileName);
    }
    if(file!=null){
      file.delete();
    }
  }

  public Bitmap getBitmapFromFileCache(String url){
  	File f = getFromFileCache(url);
  	Bitmap b = null;
  	if(f!=null){
  		try {
				b =   BitmapFactory.decodeStream(new FileInputStream(f));
			} catch (FileNotFoundException e) {
			}
  	}
  	return b;
  }
  /**
   * 初始化文件对象
   */
  public void makeRootDirectory(String filePath) {
//    File file = null;
    try {
      String[] temp = filePath.split("/");
      String ALBUM_PATH=Environment.getExternalStorageDirectory()+File.separator+temp[0]+File.separator;
      File dirFirstFile=new File(ALBUM_PATH);//新建一级主目录  
      if(!dirFirstFile.exists()){//判断文件夹目录是否存在  
        dirFirstFile.mkdir();//如果不存在则创建  
      }
      if(temp.length>1){
        String Second_PATH=ALBUM_PATH;
        for (int i = 1; i < temp.length; i++) {
          Second_PATH += temp[i]+File.separator; 
          File dirSecondFile=new File(Second_PATH);//新建二级主目录  
          if(!dirSecondFile.exists()){//判断文件夹目录是否存在  
            dirSecondFile.mkdir();//如果不存在则创建  
          }  
        }
      }
    } catch (Exception e) {
    }
  }
  
  /**
   * 清除文件缓存
   */
  public void clearCache() {
    if(cacheDir!=null){
      File[] files=cacheDir.listFiles();
      if(files==null)
          return;
      for(File f:files)
          f.delete();
    }
  }
  
  /**
   * 调试方法，显示当前所有缓存数据信息
   */
  public void ShowCacheTime(){
    if(cacheDir!=null){
      File[] files=cacheDir.listFiles();
      if(files==null)
          return;
      String aFileName="";
      for(int i=files.length-1;i>=0;i--){
        aFileName=files[i].getName();
//        System.out.println("--qj--******time:"+getStringTime(files[i].lastModified())
//            +"  Size:"+FormetFileSize(files[i].length())
//            +"  类型:"+aFileName.substring(aFileName.length()-3,aFileName.length()));
        
      }
    } 
  }
  
  /**
   * 自动判断是否清除缓存，当缓存大于设定最大值，优先删除图片缓存对象
   */
  public void AutoDelCashe(){
    if(cacheDir!=null){
      File[] files=cacheDir.listFiles();
      int fileLength=files.length;
      if(fileLength>SetupInfo.AUTO_CLEAN_CASHE_NUM){
        int num=0;
        String aFileName="";
        for(int i=fileLength-1;i>=0;i--){
          if(num>=SetupInfo.AUTO_CLEAN_CASHE_NUM){
            break;
          }
          aFileName=files[i].getName();
          if(aFileName.endsWith("jpg")||aFileName.endsWith("gif")){
            files[i].delete();
            num++;
          }
        }
      }
    }
  }
  
  /**
   * 
   * @param url
   * @return
   */
  private String urlToFileName(String url) {
    return String.valueOf(url.hashCode());
  }
  
  private void copyStream(InputStream is, OutputStream os){
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
  
  public String GetFileCasheSize(){
    String cashe="";
    try {
      if(ExistSDCard()){
        cashe = FormetFileSize(getFileSize());
      }else{
        cashe="无内存卡";
      }
      
    } catch (Exception e) {
    }
    return cashe;
  }

  public long getFileSize() throws Exception
  {
   long size = 0;
   File flist[] = cacheDir.listFiles();
   if(flist==null)
     return 0;
   for (int i = 0; i < flist.length; i++)
   {
    if (flist[i].isDirectory())
    {
     size = size + getFileSize();
    }
    else
    {
     size = size + flist[i].length();
    }
   }
   return size;
  }
  
  public String FormetFileSize(long fileS)
  {// 转换文件大小
   DecimalFormat df = new DecimalFormat("0.00");
   String fileSizeString = "";
   if (fileS < 1024)
   {
    fileSizeString = df.format((double) fileS) + "B";
   }
   else if (fileS < 1048576)
    {
     fileSizeString = df.format((double) fileS / 1024) + "K";
    }
    else if (fileS < 1073741824)
    {
     fileSizeString = df.format((double) fileS / 1048576) + "M";
    }
    else
    {
     fileSizeString = df.format((double) fileS / 1073741824) + "G";
    }
   return fileSizeString;
  }
}
