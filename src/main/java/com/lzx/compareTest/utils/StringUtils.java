package com.lzx.compareTest.utils;

public class StringUtils {

	public static String toUpperCaseFirstWord(String s) {
		if(s==null) {
			throw new NullPointerException();
		}
		return s.substring(0,1).toUpperCase()+s.substring(1);
	}
	
	public static boolean notEmpty(String s) {
		if(s==null||"".equals(s.trim())) {
			return false;
		}
		return true;
	}
}
