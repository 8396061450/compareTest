package com.lzx.compareTest.spider;

import java.util.concurrent.BlockingQueue;

import com.lzx.compareTest.common.Stop;
import com.lzx.compareTest.queue.BlockingQueueHandle;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class MySpider extends Spider{

	public MySpider(PageProcessor pageProcessor) {
		super(pageProcessor);
	}
	
	public void run() {
		super.run();
		BlockingQueueHandle.putTask(new Stop());
	}

}
