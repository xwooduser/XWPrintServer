package com.xwood.ps.bean;

public class FileProperties 
{
	private String host;
	private String port;
	
	private String fileName;
	private String fileSize;

	/**
	 * @return 返回文件名
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param 设置文件名
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return 返回文件大小
	 */
	public String getFileSize() {
		return fileSize;
	}
	/**
	 * @param 设置文件大小
	 */
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	/**
	 * @return 返回主机地址
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @param 设置主机地址
	 */
	public void setHost(String host) {
		this.host = host;
	}
	
	
	/**
	 * @return 返回端口
	 */
	public String getPort() {
		return port;
	}
	/**
	 * @param 设置端口
	 */
	public void setPort(String port) {
		this.port = port;
	}
}