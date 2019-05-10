package com.lzx.compareTest;


import java.util.List;

import com.lzx.compareTest.downloader.MySelenumDownLoad;
import com.lzx.compareTest.propare.Jingdong;
import com.lzx.compareTest.utils.ParseUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;

public class RegexTest {

	public static void main(String[] args) {
		Jingdong d=new Jingdong(null);
		Site site=d.getSite();
		String url="https://item.jd.com/14744552882.html";
		Page page=new MySelenumDownLoad().downloadPage(site, url);
		String regex="del:\\([\\s\\S].*?\\)>del:\\（[\\s\\S].*?\\）>regex:[\\s\\S].*?([\\d]+)[\\s\\S]*|1";
		List list;
		try {
			list = ParseUtils.parse(page, regex);
			System.out.println(list.get(0));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
