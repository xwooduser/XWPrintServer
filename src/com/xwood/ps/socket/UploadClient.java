package com.xwood.ps.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xwood.ps.service.Download;
import com.xwood.ps.util.PropertieFileReader;

/**
 * 客户端
 */
public class UploadClient extends Thread
{
	public Log log = LogFactory.getLog(getClass());
	private static boolean startup = true;
	private static String url;
	private static int port;
	private static Socket socket = null;
	private BufferedReader in;
	private PrintWriter out;
	
	private UploadClient(){}
	
	public void run()
	{
		try 
		{
			while(startup)
			{
					System.out.println("客户端开始接收"+url+":"+port+"的信息...");
					socket = new Socket(url,port);
					
					in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
					String filename = in.readLine();
					System.out.println(filename);
					out = new PrintWriter(socket.getOutputStream(),true);
					
					if(filename != null && filename.length() > 4 && 
							(".xls".equalsIgnoreCase(filename.substring(filename.length()-4)) || ".xlsx".equalsIgnoreCase(filename.substring(filename.length()-5))))
					{
						try
					    {
							System.out.println(PropertieFileReader.getString("downloadUrl")+filename);
							Download d = new Download(new URL(PropertieFileReader.getString("downloadUrl")+filename),out);
						}
					    	catch (MalformedURLException e) 
						{
							e.printStackTrace();
						}
					}	
					else
					{
						System.out.println("服务器端传过来的文件名不是excel格式,不予以下载");
						log.error("服务器端传过来的文件名不是excel格式,不予以下载");
					}
					out.close();
					in.close();
					socket.close();
			
			}
		}
		catch (IOException e)
		{
			// 写入错误日志
			System.out.println("IO流错误："+e.toString());
			e.printStackTrace();
		}
	}
	
	/**
	 * @param url1  服务器端地址
	 * @param port1 端口
	 * 开启客户端
	 */
	public static void startup(String url1,int port1)
	{	
		
			url = url1;
			port = port1;
			startup = true;
			System.out.println("服务器端地址："+url+":"+port);
			UploadClient client = new UploadClient();
			//socket = new Socket(url,port);
			client.start();
			
	}
	
	/**
	 * 关闭服务器端
	 */
	public static void shutdown()
	{	
		try
		{
			startup = false;
			socket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @return 返回客户端启动状态
	 */
	public static boolean isStartup()
	{
		return startup;
	}
	
	/**
	 * 启动客户端
	 */
	public static void main(String[] args) 
	{
		UploadClient.startup(PropertieFileReader.getString("uploadServerHost"),Integer.parseInt(PropertieFileReader.getString("uploadDataPort")));
	}
}

