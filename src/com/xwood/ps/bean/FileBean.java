package com.xwood.ps.bean;

public class FileBean 
{
	private String url;						//下载地址
	private String filename;				//下载后的文件名
	private String filetemplate;			//打印文件模板名
	public String getFiletemplate() {
		return filetemplate;
	}
	public void setFiletemplate(String filetemplate) {
		this.filetemplate = filetemplate;
	}
	private int size;						//文件大小
	private String printname;				//打印机名
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getPrintname() {
		return printname;
	}
	public void setPrintname(String printname) {
		this.printname = printname;
	}
}

