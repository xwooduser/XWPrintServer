package com.xwood.ps.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client 
{
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	public Client()
	{
		try 
		{
			System.out.println("Try to connet to 127.0.0.1:10000");
			socket = new Socket("127.0.0.1",62222);
			System.out.println("The Server Connected!");
			System.out.println("Please enter some character:");
			BufferedReader line = new BufferedReader(new InputStreamReader(System.in));
			out = new PrintWriter(socket.getOutputStream(),true);
			//out.println(line.readLine());
			out.println("您好！！！");
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println(in.readLine());
			out.close();
			in.close();
			socket.close();
		}
		catch (Exception e) 
		{
			out.println("Wrong!!!");
		}
	}
	
	public static void main(String[] args) 
	{
		new Client();
	}
}
