package com.xwood.ps.service;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xwood.ps.bean.FileBean;
import com.xwood.ps.print.Printer;
import com.xwood.ps.util.PropertieFileReader;
import com.xwood.ps.util.TemplatePrintUtil;

/**
 * 通过http协议从服务器url地址下载文件
 */
public class Download extends Observable implements Runnable 
{    
	public Log log = LogFactory.getLog(getClass());
    // 定义下载文件最大缓存
    private static final int MAX_BUFFER_SIZE = 1024;
    
    // These are the status names.
    public static final String STATUSES[] = {"Downloading",
    "Paused", "Complete", "Cancelled", "Error"};
    
    // 状态代码
    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1;
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;
    
    private URL url; // 下载地址
    private int size; // 需要下载比特流大小
    private int downloaded; // 已下载比特流大小
    private int status; // 当前下载状态
    private PrintWriter out = null;
    
   
    public Download(URL url,PrintWriter out) {
        this.url = url;
        this.out = out;
        size = -1;
        downloaded = 0;
        status = DOWNLOADING;
        
        //开始下载
        download();
    }
    

    public String getUrl()
    {
        return url.toString();
    }
    

    public int getSize()
    {
        return size;
    }
    
    /**
     * @return 获得下载进程度（百分比）
     */
    public float getProgress() {
        return ((float) downloaded / size) * 100;
    }
    
   
    public int getStatus() {
        return status;
    }
    
    /**
     * 暂停下载
     */
    public void pause()
    {
        status = PAUSED;
        stateChanged();
    }
    
    /**
     * 重新下载
     */
    public void resume() 
    {
        status = DOWNLOADING;
        stateChanged();
        download();
    }
    
    /**
     * 恢复下载
     */
    public void cancel() 
    {
        status = CANCELLED;
        stateChanged();
    }
    
    /**
     * 下载过程出现错误，标志错误状态
     */
    private void error()
    {
        status = ERROR;
        stateChanged();
        System.out.println("下载出现问题");
    //    log.error(url.getFile()+"下载地址不正确");
    }
    
    /**
     * 开始或恢复下载
     */
    private void download() 
    {
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * @param url 下载地址
     * @return  获取下载文件名
     */
    private String getFileName(URL url)
    {
        String fileName = url.getFile();	
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }
    
    /**
     * 下载过程
     */
    public void run() 
    {
        RandomAccessFile file = null;
        InputStream stream = null;
        
        try {
            // 打开连接
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
            log.error("url地址："+url.getFile());
           
            connection.setRequestProperty("Range",
                    "bytes=" + downloaded + "-");
           // connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");          
            //连接到服务器	
            connection.connect();
            
            // 确保返回状态正常，为200
            if (connection.getResponseCode() / 100 != 2) {
            	
            	System.out.println("在服务器上未找到指定的下载文件");
            	log.error(connection.getResponseCode()+":::返回状态不正常,在服务器上未找到指定的下载文件:"+getFileName(url));
                error();
                return;
            }
            
            
            // 检查的有效内容长度。
            int contentLength = connection.getContentLength();
            if (contentLength < 1) 
            {
            	log.error("有效内容长度小于1，在服务器上未找到指定的下载文件:"+getFileName(url));
                error();
                return ;
            }
            
            // 设置文件已下载量
            if (size == -1) {
                size = contentLength;
                stateChanged();
            }
            
            // 打开文件
            file = new RandomAccessFile(PropertieFileReader.getString("tempDownload")+getFileName(url), "rw");
            file.seek(downloaded);
            
            stream = connection.getInputStream();
            while (status == DOWNLOADING) {
            // 文件剩余下载量
                byte buffer[];
                if (size - downloaded > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[size - downloaded];
                }
                
                // 从服务器端读入缓存
                int read = stream.read(buffer);
                if (read == -1)
                    break;
                
                //写缓存到文件中去
                file.write(buffer, 0, read);
                downloaded += read;
                stateChanged();
            }
            
      // 下载完后改变状态
            if (status == DOWNLOADING) 
            {
                status = COMPLETE;
                stateChanged();
                System.out.println(getFileName(url)+"下载完毕...");
                log.info(getFileName(url)+"下载完毕");
                if(out != null)
                	out.println("下载完毕...");
            }
            
        } catch (Exception e) {
            error();
        } finally {
            // 关闭文件
            if (file != null) {
                try {
                    file.close();
                } catch (Exception e) {}
            }	
            
            // 关闭连接
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {}
            }
        }
        
        
        if(status == COMPLETE)
        {
        	System.out.println("开始打印-------->");
        	FileBean fileBean = new FileBean();
        	fileBean.setUrl(url.getFile());
        	fileBean.setFilename(getFileName(url));
        	fileBean.setSize(size);
        	
        	TemplatePrintUtil.setFileInfo(fileBean);
        	//开始打印
        	System.out.println("======打印文件名："+fileBean.getFilename());
        	System.out.println("======打印文件模板:"+fileBean.getFiletemplate());
        	System.out.println("======打印文件对应打印机:"+fileBean.getPrintname());
        	
        	if(fileBean.getFiletemplate() == null || fileBean.getPrintname() == null)
        	{
        		System.out.println("打印机未找到或文件模板名不正确");
        		log.error(getFileName(url)+"打印机未找到或文件模板名不正确");
        		//记录进错误日志
        	}
        	else
        	{
        		try {
					Printer.printExcel(new File(PropertieFileReader.getString("tempDownload")+fileBean.getFilename()).getAbsolutePath(), fileBean.getPrintname());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("打印过程出错");
					log.error(getFileName(url)+"打印过程出错");
					e.printStackTrace();
					//记录进错误日志 
				}
        	}
        }
    }
    
    private void stateChanged() 
    {
        setChanged();
        notifyObservers();
    }

    /**
     * 测试下载文件
     * @param args
     */
    public static void main(String args[])
    {
	    try
	    {
			Download d = new Download(new URL(PropertieFileReader.getString("downloadUrl")+"差异报表_2012022308.xls"),null);
		}
	    	catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
	
    //System.out.println(PropertieFileReader.getString("downloadUrl"));
    }
}