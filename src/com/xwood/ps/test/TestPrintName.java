package com.xwood.ps.test;

import java.awt.print.PrinterJob;

import javax.print.PrintService;

public class TestPrintName 
{
	public static void main(String[] args) 
	{
		PrintService[] pss = PrinterJob.lookupPrintServices();
		for (PrintService p : pss) {
            System.out.println(p.getName());
        }
	}
}
