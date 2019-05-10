package com.lzx.compareTest.handle;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lzx.compareTest.common.Task;
import com.lzx.compareTest.queue.BlockingQueueHandle;

public class HandleThread extends Thread{
	public static Logger log = LogManager.getLogger("handle");
	public HandleThread() {
	}
	public void run() {
		try {
			while(BlockingQueueHandle.getStatus()!=BlockingQueueHandle.STOP) {
				Task run=(Task) BlockingQueueHandle.takeTask();
				run.run();
			}
		} catch (Exception e) {
			log.error(Thread.currentThread().getName()+"：执行任务失败："+e.getMessage());
		}
		
	}
}
