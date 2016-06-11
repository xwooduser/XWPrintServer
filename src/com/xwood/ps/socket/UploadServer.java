package com.xwood.ps.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.xwood.ps.util.PropertieFileReader;


/**
 * 服务器端
 */
public class UploadServer extends Thread
{
	static boolean startup=true;
	static private int port;
	static private ServerSocket ss=null;	
	private BufferedReader in;
	private PrintWriter out;
	private UploadServer(){}
	 
	public void run() 
	{
		try 
		{
			while(startup)
			{
				Socket socket = ss.accept();	
				
				String remoteIP = socket.getInetAddress().getHostAddress();
				String remotePort = ":"+socket.getLocalPort();
				System.out.println("有客户端请求进来！IP:"+remoteIP+remotePort);
				out = new PrintWriter(socket.getOutputStream(),true);
				BufferedReader line = new BufferedReader(new InputStreamReader(System.in));
				out.println(line.readLine());
				//in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//	String line = in.readLine();
			//	System.out.println("客户端发送内容："+line);
				out.close();
			//	in.close();
				socket.close();
			}
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 开启服务器端
	 * @param port1
	 */
	public static  void startup(int port1){		
		try {
			port=port1;		
			startup=true;
			UploadServer server=new UploadServer();
			ss = new ServerSocket(port);
			server.start();
			System.out.println("socket端口"+port);
		} catch (IOException e) {
			System.out.println("服务端出现异常:"+e.toString());
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭服务器端
	 */
	public static void shutdown()
	{
		try 
		{
			startup=false;	
			ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return 启动状态
	 */
	public static boolean isStartup()
	{
		return startup;
	}
	
    /**
     * 启动服务器端
     */
    public static void main(String[] args) throws Exception
    {
    	System.out.println(Integer.parseInt(PropertieFileReader.getString("uploadDataPort")));
    	UploadServer.startup(Integer.parseInt(PropertieFileReader.getString("uploadDataPort")));
	}
}
