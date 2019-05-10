package com.lzx.compareTest.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class VelocityHtmlCreate {
	public static void createHtml(String templateFile,List list) {
		VelocityEngine ve = new VelocityEngine(); 
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName()); 
		ve.init(); 
		Template actionTpt = ve.getTemplate(templateFile); 
		VelocityContext ctx = new VelocityContext(); 
		ctx.put("list", list);
		String path=Thread.currentThread().getContextClassLoader().getResource(templateFile).getPath();
		merge(actionTpt,ctx,path+File.separator+"result.html");
	}
	 
	private static void merge(Template template, VelocityContext ctx, String path) { 
		 PrintWriter writer = null; 
		 try { 
		 writer = new PrintWriter(path); 
		 template.merge(ctx, writer); 
		 writer.flush(); 
		 } catch (FileNotFoundException e) { 
		 e.printStackTrace(); 
		 } finally { 
		 writer.close(); 
		 } 
	 } 
	
}
