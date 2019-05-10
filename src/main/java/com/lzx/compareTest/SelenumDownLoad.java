package com.lzx.compareTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

public class SelenumDownLoad {

	public static void main(String[] args) {
		ChromeOptions chromeOptions = new ChromeOptions();
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
		WebDriver driver = new ChromeDriver(chromeOptions);
		String url="https://search.jd.com/Search?keyword=%E7%BA%B8%E5%B0%BF%E8%A3%A4l%E5%8F%B7%E8%87%AA%E8%90%A5&enc=utf-8&wq=%E7%BA%B8%E5%B0%BF%E8%A3%A4l%E5%8F%B7%E8%87%AA%E8%90%A5&pvid=1ceb6e3a6df845bbaa42570195a9d37d";
        driver.get(url);
        
        String title = driver.getTitle();
        WebElement element=driver.findElement(By.xpath("/html"));
        String count=element.getAttribute("outerHTML");
 
        Page page=new Page();
        page.setRequest(new Request(url));
        page.setRawText(count);
        String ne=".quan-item";
        driver.close();
        System.out.println(page.getHtml().$(ne).$(".text","text"));

	}
}
