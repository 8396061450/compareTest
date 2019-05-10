package com.lzx.compareTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lzx.compareTest.pojo.Detail;

import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;

public class Main1 {

	public static void main(String[] args) throws Exception {
		SeleniumDownloader s=new SeleniumDownloader("D:\\ChromeDriver\\chromedriver_win32(2)\\chromedriver.exe");
		a al=new a();
		
		String s1=al.getL();
	    List<String> b=new ArrayList<String>();
	    System.out.println(b.contains(s1)); ;
	}
	
	static class a{
		private String l;

		public String getL() {
			return l;
		}

		public void setL(String l) {
			this.l = l;
		}
		
	}
}
