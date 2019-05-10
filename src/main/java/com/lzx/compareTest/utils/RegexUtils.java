package com.lzx.compareTest.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

	/**
	 * 查找一个group
	 * @param original
	 * @param regex
	 * @param group
	 * @return
	 */
	public static String regexMathgroup(String original,String regex,int group) {
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(original);
		if(m.find()) {
			return m.group(group);
		}
		return null;
	}
	
	public static String[] regexMathgroup(String original,String regex,int group1,int group2) {
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(original);
		if(m.find()) {
			String[] result=new String[] {m.group(group1),m.group(group2)};
			return result;
		}
		return null;
	}
	public static Float[] regexFloatMathgroup(String original,String regex,int group1,int group2) {
		String[] result=regexMathgroup(original,regex,group1,group2);
		if(result!=null) {
			Float[] i=new Float[2];
			try {
				i[0]=Float.parseFloat(result[0]);
				i[1]=Float.parseFloat(result[1]);
				return i;
			}catch (Exception e) {
				return null;
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		String ori="满2件享5.7折 ";
		String regex="[\\s\\S].*?([\\d\\.]+)[\\s\\S].*?([\\d\\.]+)[\\s\\S]*";
		System.out.println(regexMathgroup(ori, regex, 1,2));;
		
	}
}
