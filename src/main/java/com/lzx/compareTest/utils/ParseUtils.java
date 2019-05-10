package com.lzx.compareTest.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.xsoup.xevaluator.ElementOperator.Regex;

public class ParseUtils {

	public static List parse(Page page,String regex) throws Exception {
		List<String> result=null;
		boolean listFlag=false;
		StringBuilder sb=new StringBuilder(regex);
		if(regex.startsWith("L")){//list
			listFlag=true;
			sb.replace(0, 1,"");
		}
		Selectable sc=null;
		Selectable or=page.getHtml();
		while(regex.contains("&")){
			sc=getSelectable(or,sb);
			regex=sb.toString();
			if(sc==null) {
				return null;
			}
			or=sc;
		}
		return getValueBySeletable(listFlag,sb,sc);
	}
	
	//根据规则得到一个选择器
	public static Selectable getSelectable(Selectable selecatable,StringBuilder sb) throws Exception {
		Selectable selector=null;
		String regex=sb.toString();
		if(regex.startsWith("CSS:")) { //使用css选择器
			int index=regex.indexOf("&");
			if(index==-1) {//规则错误
				throw new Exception("规则错误");
			}
			String re=regex.substring(4,index);
			if(re.indexOf("|")!=-1) {
				String[] ne=re.split("\\|");
				selector=selecatable.$(ne[0],ne[1]);
			}else {
				selector=selecatable.$(re);
			}
			sb.replace(0,index+1,"");
		}
		return selector;
	}
	
	public static List getValueBySeletable(boolean listFlag,StringBuilder sb,Selectable seletable) {
		List<String> result=null;
		String regex=sb.toString();
		if(regex.length()==0){
			if(listFlag){
				result=new ArrayList<String>((seletable.all()));
			}else {
				result=new ArrayList<String>();
				result.add(getFirstNoEmpty(seletable.all()));
			}
		}
		else if(regex.startsWith("regex:")){
		    regex= regex.substring(6, regex.length());
		    result=new ArrayList<String>();
		    String[] res=regex.split("\\|");
		    int group=0;
		    try {
		       group=Integer.parseInt(res[1]);
		    }catch (Exception e) {
		    	
			}
			if(listFlag){
				List<String> l=seletable.all();
				for(int i=0;i<l.size();i++) {
					result.add(RegexUtils.regexMathgroup(l.get(i),res[0],group));
				}
			}else {
				result=new ArrayList<String>();
				result.add(RegexUtils.regexMathgroup(getFirstNoEmpty(seletable.all()),res[0],group));
			}
		}
		return result;
	}
	
	public static String getValueByFieldValue(String fieldValue,String regex){
		return RegexUtils.regexMathgroup(fieldValue, regex, 1);
	}
	
	public static List<String> parse(String s,String regex){
		List<String> result=null;
		boolean listFlag=false;
		StringBuilder sb=new StringBuilder(regex);
		if(regex.startsWith("L")){
			listFlag=true;
			sb.replace(0, 1, "");
		}
		regex=sb.toString();
		while(regex.contains(">")) {
			if(regex.startsWith("del:")){
				String re=regex.substring(4,regex.indexOf(">"));
				s=s.replaceAll(re,"");
				sb.replace(0,regex.indexOf(">")+1,"");
				regex=sb.toString();
			}
		}
		//最后的计算
		if(regex.startsWith("regex:")) {
			regex= regex.substring(6, regex.length());
		    result=new ArrayList<String>();
		    String[] res=regex.split("\\|");
		    int group=0;
		    try {
		       group=Integer.parseInt(res[1]);
		    }catch (Exception e) {
		    	
			}
			if(listFlag){
				
			}else {
				result=new ArrayList<String>();
				result.add(RegexUtils.regexMathgroup(s,res[0],group));
			}
		}
		return result;
	}
	private static String getFirstNoEmpty(List<String> list) {
		if(list!=null) {
			for(int i=0;i<list.size();i++){
				if(StringUtils.notEmpty(list.get(i))) {
					return list.get(i);
				}
			}
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		System.out.println(RegexUtils.regexMathgroup("纸尿裤S82片", "[\\s\\S].*?([\\d]+)[\\s\\S].*", 1));;
	}
}
