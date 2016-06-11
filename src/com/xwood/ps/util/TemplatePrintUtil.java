package com.xwood.ps.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.xwood.ps.bean.FileBean;

/**
 * 通过配置文件获取模板和相应打印机的信息
 */
public class TemplatePrintUtil 
{
	private static int templateCount;							    //模板数量
	private static Map<String, String> templates;					//模板信息
	private static Map<String,String> prints;						//模板对应打印机信息

	//初始化属性值
	static
	{
		templateCount = Integer.parseInt(PropertieFileReader.getString("templateCount"));
		templates = new HashMap<String, String>();
		prints = new HashMap<String, String>();
		
		for(int i = 1;i<=templateCount;i++)
		{
			String template = PropertieFileReader.getString("template"+i);
			if(template != null)
			{
				String print = PropertieFileReader.getString(template);
				templates.put("template"+i, template);
				prints.put(template, print);
			}
		}
	}
	
	public static Map<String, String> getTemplates() {
		return templates;
	}

	public static Map<String, String> getPrints() {
		return prints;
	}

	public static int getTemplateCount() {
		return templateCount;
	}	
	
	/**
	 * 设置文件信息，根据文件名获得文件模板名和打印机名
	 * @param fileBean
	 */
	public static void setFileInfo(FileBean fileBean)
	{
		if(fileBean.getFilename() == null)
			return;
		
		//获得打印机信息
		Iterator iterPrint = getPrints().entrySet().iterator();
		while(iterPrint.hasNext())
		{
			Map.Entry<String, String> entryPrint = (Map.Entry<String, String>)iterPrint.next();
			if(fileBean.getFilename().contains(entryPrint.getKey()))
			{
				fileBean.setFiletemplate(entryPrint.getKey());
				fileBean.setPrintname(entryPrint.getValue());
				System.out.println("模板："+entryPrint.getKey());
				System.out.println("打印机名："+entryPrint.getValue());
				break;
			}
		}
	}
	
	/**
	 * 遍历HashMap
	 */
	public static void traverseMap()
	{
		Iterator iterTemplate = getTemplates().entrySet().iterator();
		while(iterTemplate.hasNext())
		{
			Map.Entry<String, String> entryTemplate = (Map.Entry<String, String>)iterTemplate.next();
			System.out.println(entryTemplate.getKey()+"--------->"+entryTemplate.getValue());
		}
		System.out.println();
	
		Iterator iterPrint = getPrints().entrySet().iterator();
		while(iterPrint.hasNext())
		{
			Map.Entry<String, String> entryPrint = (Map.Entry<String, String>)iterPrint.next();
			System.out.println(entryPrint.getKey()+"-------------->"+entryPrint.getValue());
		}
	}
	
	public static void main(String[] args) {
		TemplatePrintUtil.traverseMap();
	}
}
