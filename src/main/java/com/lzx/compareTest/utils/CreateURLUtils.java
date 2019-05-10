package com.lzx.compareTest.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class CreateURLUtils {

	public static String createUrl(String urlRegex,String keyWord) {
		if(!StringUtils.notEmpty(urlRegex)||!StringUtils.notEmpty(keyWord)) {
			throw new IllegalArgumentException();
		}
		String encode=URLEncoder.encode(keyWord);
		
		String url=urlRegex.replaceAll("\\[keyWord\\]",encode);
		return url;
	}
	
	public static String[] createUrl(String urlRegex,String keyWord,int ...page) {
		List<String> urls=new ArrayList<String>();
		if(!StringUtils.notEmpty(urlRegex)||!StringUtils.notEmpty(keyWord)) {
			throw new IllegalArgumentException();
		}
		String encode=URLEncoder.encode(keyWord);
		
		String url=urlRegex.replaceAll("\\[keyWord\\]",encode);
		for(int i=0;i<page.length;i++) {
			urls.add(url.replaceAll("\\[page\\]", ""+page[i]));
		}
		return urls.toArray(new String[urls.size()]);
	}
	public static void main(String[] args) {
		System.out.println(URLEncoder.encode("纸尿裤 自营"));
	}
}
