package com.lzx.compareTest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lzx.compareTest.downloader.MySelenumDownLoad;
import com.lzx.compareTest.handle.Handle;
import com.lzx.compareTest.pojo.Detail;
import com.lzx.compareTest.propare.Jingdong;
import com.lzx.compareTest.queue.BlockingQueueHandle;
import com.lzx.compareTest.spider.MySpider;
import com.lzx.compareTest.utils.CreateURLUtils;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Spider.Status;

public class Main {
	static Logger log = LogManager.getRootLogger();

	public static void main(String[] args) {
		int[] page = new int[] { 1, 2, 3 };
		String keyWord = "好奇 铂金 拉拉裤 L";
		// String
		//String urlRegex="https://search.jd.com/Search?keyword=[keyWord]&enc=utf-8&&wq=[keyWord]&page=[page]&s=56&click=0";
		// String
		// urlRegex="https://search.jd.com/Search?keyword=[keyWord]&enc=utf-8&wq=[keyWord]";
		String urlRegex = "https://item.jd.com/1990569433.html";// "https://item.jd.com/1990569433.html";

		String url[] = CreateURLUtils.createUrl(urlRegex, keyWord, page);
		System.setProperty("selenuim_config",
				"D:\\springworkspace\\compareTest\\src\\main\\java\\com\\lzx\\compareTest\\config.ini");
		final MySpider spider = new MySpider(new Jingdong());
		spider.addUrl(url)
				.setDownloader(new MySelenumDownLoad("E:/chromeDreiver/chromedriver.exe")).thread(5);
		final Handle handle = new Handle(1);
		new Thread(new Runnable() {
			public void run() {
				while (BlockingQueueHandle.getStatus() != BlockingQueueHandle.STOP) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("关闭线程池");
				handle.shutdown();
			}
		}).start();

		spider.run();
		spider.close();

	}

}
