package com.lzx.compareTest.pojo;

/**
 * 
 * 规则  
 * L 表示是list提取 
 * & 分割选择器
 * | 分割选择器 属性
 * 使用什么选择器:选择器对应的名字[如何提取内容]
 * 
 * > 子节点 或者获取
 * 
 * 
 * 提取内容规则
 * 选择器根据属性提取内容
 * 正则表达式提取内容：regex: 正则表达式|groupnum
 * regex:[\\s\\S].*?([\\d]+)[\\s\\S].*|1
 * 
 * @author a8396
 *
 */
public class DetailRegex {
	
	public static String nameRegex="CSS:.sku-name|text&";
	public static String priceRegex="CSS:.price|text&";
	public static String quanRegex="LCSS:.quan-item&CSS:.text|text&";
	public static String $numRegex="name&del:\\([\\s\\S].*?\\)>del:\\（[\\s\\S].*?\\）>regex:[\\s\\S].*?([\\d]+)[\\s\\S]*|1";//"CSS:.selected|title&regex:[\\s\\S].*?([\\d]+)[\\s\\S]*|1";
	public static String zhekouRegex="CSS:#prom-one&CSS:.hl_red|text&";
	
}
