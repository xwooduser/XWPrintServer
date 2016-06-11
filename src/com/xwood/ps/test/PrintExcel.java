package com.xwood.ps.test;

import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;
import jp.ne.so_net.ga2.no_ji.jcom.excel8.ExcelApplication;
import jp.ne.so_net.ga2.no_ji.jcom.excel8.ExcelWorkbook;
import jp.ne.so_net.ga2.no_ji.jcom.excel8.ExcelWorkbooks;
import jp.ne.so_net.ga2.no_ji.jcom.excel8.ExcelWorksheet;

public class PrintExcel {
	
	/** 
	* 实现打印Excel的方法 
	* @param fileName[文件路径] 
	* @return[返回是否打印成功] 
	*/ 
	public boolean printExcel(String fileName){   
	        //查找JOM能够操作的组件 
	ReleaseManager rm = new ReleaseManager();   
	        try{ 
	        //创建可加载的组件 
	        ExcelApplication excel = new ExcelApplication(rm);   
	             //创建工作薄对象 
	        ExcelWorkbooks xlBooks = excel.Workbooks(); 
	        //打开工作薄 
	             ExcelWorkbook xlBook = xlBooks.Open(fileName); 
	             //打印 
	             ExcelWorksheet xlSheet = excel.ActiveSheet();   
	             if(xlSheet!=null){ 
	            //出现正在打印对话框 
	                 xlSheet.PrintOut(); 
	                 //退出 
	                 excel.Quit(); 
	             }     
	        }catch(Exception e){ 
	        e.printStackTrace();   
	            return false;      
	        }finally{  
	        //关闭 
	            rm.release();   
	        }   
	        return true;   
	}   
	
	
	public static void main(String[] args){   
	//实例化打印excel类 
	PrintExcel prEcl = new PrintExcel();   
	    try{   
	       //调用打印Excel方法，并进来一个xls文件 
	    prEcl.printExcel("D:/1_PO差异报表.xls"); 
	       
	    }catch(Exception e){ 
	    //捕获异常处理 
	    e.printStackTrace();       
	    }   
	   }   

	
}
