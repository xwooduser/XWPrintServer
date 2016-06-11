package com.xwood.ps.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server 
{
	private ServerSocket ss;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	public Server()
	{
		try 
		{
			ss = new ServerSocket(62222);
			
			while(true)
			{
				socket = ss.accept();
				String remoteIP = socket.getInetAddress().getHostAddress();
				String remotePort = ":"+socket.getLocalPort();
				System.out.println("A client come in ! IP:"+remoteIP+remotePort);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String line = in.readLine();
				System.out.println("Client send is :"+line);
				out = new PrintWriter(socket.getOutputStream(),true);
				out.println("Your Message Received!");
				out.close();
				in.close();
				socket.close();
			}
		}
		catch (Exception e)
		{
			out.println("Wrong!!!");
		}
	}
	
	public static void main(String[] args)
	{
		new Server();
	}
}

