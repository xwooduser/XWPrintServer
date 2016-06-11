package com.xwood.ps.print;

import java.awt.print.PrinterJob;
import java.io.File;

import javax.print.PrintService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jacob.activeX.ActiveXComponent;
 import com.jacob.com.ComThread;
 import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
 

public class Printer
 {
	public static Log log = LogFactory.getLog(Printer.class);
    /**
     * 打印Excel文件
     * 
     * @param path
     *            Excel文件路径
     * @throws Exception 
     */
    public static void printExcel(String path, String printerName) throws Exception {
    	System.out.println("打印路径====="+path);
    	System.out.println("打印机名====="+printerName);
    	
    	boolean sign = false;
    	PrintService[] pss = PrinterJob.lookupPrintServices();
		for (PrintService p : pss)
		{
            if(p.getName().equals(printerName))
            {
            	sign = true;
            	break;
            }
        }
		
		if(!sign)
		{
			log.error(path+"未找到对应打印机"+printerName);
			return ;
		}
    	
        ComThread.InitSTA();
        ActiveXComponent xl = new ActiveXComponent("Excel.Application");
        System.out.println("version=" + xl.getProperty("Version")); 
        try {
            Dispatch workbooks = xl.getProperty("Workbooks").toDispatch();

            Dispatch workbook = Dispatch.call(workbooks, "Open", path).toDispatch();

            Dispatch.callN(workbook, "PrintOut", new Object[] { Variant.VT_MISSING, Variant.VT_MISSING, new Integer(1),
                    new Boolean(false), printerName, new Boolean(true), Variant.VT_MISSING, "" });
            Dispatch.call(workbook, "Close");
            log.info(path+"已成功打印");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            // 始终释放资源
            xl.invoke("Quit", new Variant[] {});
            ComThread.Release();
        }
    }
    
    public static void printTest()
    {
    	 try
    	 {
    	 ComThread.InitSTA();
    	 ActiveXComponent xl = new ActiveXComponent("Excel.Application"); 
    	 System.out.println("version=" + xl.getProperty("Version"));   
    	 //new Variant(false)  打印时是否显示文档       false不显示   
    	 Dispatch.put(xl, "Visible", new Variant(true)); 
    	 Dispatch workbooks = xl.getProperty("Workbooks").toDispatch(); 
    	    
    	 //打开文档 
    	 Dispatch excel=Dispatch.call(workbooks,"Open","D:/1_PO差异报表.xls").toDispatch(); 
    	  
    	                  Dispatch.get(excel,"PrintOut"); 
    	 

    	}
    	 catch (Exception e)
    	 {
    	 e.printStackTrace();
    	 }finally
    	 { 
    	 ComThread.Release();
    	 }
    	  
    }
	
	
 public static void main(String[] args)
 {
	 try {
		Printer.printExcel("d:\\1_PO差异报表.xls", "C4300-3F-right");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 }
 }
 
