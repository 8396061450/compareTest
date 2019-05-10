package com.lzx.compareTest.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import com.lzx.compareTest.utils.ParseUtils;
import com.lzx.compareTest.utils.StringUtils;

import us.codecraft.webmagic.Page;

public class PojoCreateUtils{

	public static <T> void createPojo(T t,Page page) throws Exception {
		Class c=t.getClass();
		Class regex=Class.forName(c.getName()+"Regex");
		Field[] fields=c.getDeclaredFields();
		String fieldname=null;
		for(int i=0;i<fields.length;i++){
			Field f=fields[i];
			fieldname=f.getName();
			if(fieldname.startsWith("$")) {//根据属性值获取
				String re=(String) regex.getField(fieldname+"Regex").get(null);
				String fieldName=re.substring(0,re.indexOf("&"));
				String ori=(String) getValue(c,c.getDeclaredField(fieldName),t);
				re=re.replaceAll(fieldName+"\\&", "");
				List list=ParseUtils.parse(ori,re);
				setValue(c, f, t, list);
			}else if(!fieldname.startsWith("_")){
				List list=ParseUtils.parse(page,(String)regex.getField(fieldname+"Regex").get(null));
				setValue(c, f, t, list);
			}
		}
	}
	
	private static <T> void setValue(Class c,Field f,T t,List list) throws Exception{
		String fieldname=f.getName();
		Method m=c.getMethod("set"+StringUtils.toUpperCaseFirstWord(fieldname),f.getType());
		if(String.class ==f.getType()){
			if(list!=null) {
				m.invoke(t,list.get(0));
			}
		}else if(List.class==f.getType()){
			m.invoke(t, list);
		}
		
	}
	
	private static <T> Object getValue(Class c,Field f,T t) throws Exception{
		String fieldname=f.getName();
		Method m=c.getMethod("get"+StringUtils.toUpperCaseFirstWord(fieldname));
		return m.invoke(t, null);
	}
	
	
}
