package com.lzx.compareTest.downloader;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.selector.PlainText;

public class MySelenumDownLoad implements Downloader, Closeable{
	
	private volatile WebDriverPool webDriverPool;

	private int sleepTime = 0;

	private int poolSize = 1;


	/**
	 * 鏂板缓
	 *
	 * @param chromeDriverPath chromeDriverPath
	 */
	public MySelenumDownLoad(String chromeDriverPath) {
		System.getProperties().setProperty("webdriver.chrome.driver",
				chromeDriverPath);
	}

	/**
	 * Constructor without any filed. Construct PhantomJS browser
	 * 
	 * @author bob.li.0718@gmail.com
	 */
	public MySelenumDownLoad() {
	}

	/**
	 * set sleep time to wait until load success
	 *
	 * @param sleepTime sleepTime
	 * @return this
	 */
	public MySelenumDownLoad setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
		return this;
	}
	
	public static Page downloadPage(Site site,String url) {
	    ChromeOptions chromeOptions=null;
		chromeOptions = new ChromeOptions();
		//设置chrome不显示界面
		chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        //设置头文件（查看浏览器头文件网址：https://httpbin.org/get?show_env=1）
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        chromeOptions.addArguments("Accept=text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        chromeOptions.addArguments("Accept-Encoding=gzip, deflate, br");
        chromeOptions.addArguments("Accept-Language=zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        chromeOptions.addArguments("Cache-Control=max-age=0");
        System.setProperty("webdriver.chrome.driver" , "E:/chromeDreiver/chromedriver.exe");
		Request request=new Request(url);
		WebDriver webDriver;
		webDriver = new ChromeDriver(chromeOptions);
		webDriver.get(request.getUrl());
		String title = webDriver.getTitle();
		WebElement element=webDriver.findElement(By.xpath("/html"));
	    String count=element.getAttribute("outerHTML");
        Page page=new Page();
        page.setRequest(new Request(url));
        page.setRawText(count);
        webDriver.close();
		return page;
	}
	

	private void checkInit() {
		if (webDriverPool == null) {
			synchronized (this) {
				webDriverPool = new WebDriverPool(poolSize);
			}
		}
	}

	public void setThread(int thread) {
		this.poolSize = thread;
	}

	public void close() throws IOException {
		try {
		webDriverPool.closeAll();
		}catch (Exception e) {
		}
	}

	public Page download(Request request, Task task) {
		checkInit();
		WebDriver webDriver;
		try {
			webDriver = webDriverPool.get();
		} catch (InterruptedException e) {
			return MyDownLoad.downloadPage(task.getSite(), request.getUrl());
		}
		webDriver.get(request.getUrl());
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebDriver.Options manage = webDriver.manage();
		Site site = task.getSite();
		if (site.getCookies() != null) {
			for (Map.Entry<String, String> cookieEntry : site.getCookies()
					.entrySet()) {
				Cookie cookie = new Cookie(cookieEntry.getKey(),
						cookieEntry.getValue());
				manage.addCookie(cookie);
			}
		}

		/*
		 * TODO You can add mouse event or other processes
		 * 
		 * @author: bob.li.0718@gmail.com
		 */

		WebElement webElement = webDriver.findElement(By.xpath("/html"));
		String content = webElement.getAttribute("outerHTML");
		Page page = new Page();
		page.setRawText(content);
		page.setUrl(new PlainText(request.getUrl()));
		page.setRequest(request);
		webDriverPool.returnToPool(webDriver);
		return page;
	}
	
}
