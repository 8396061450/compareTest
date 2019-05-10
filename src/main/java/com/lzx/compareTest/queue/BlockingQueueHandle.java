package com.lzx.compareTest.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lzx.compareTest.common.Stop;
import com.lzx.compareTest.common.Task;

public class BlockingQueueHandle {
	public final static int INIT=1;
	public final static int RUNNNG=2;
	public final static int STOP=3;
	public static Logger log = LogManager.getLogger("handle");
	private static BlockingQueue queue=new LinkedBlockingQueue<Task>();
	private static AtomicInteger status=new AtomicInteger(INIT);
	private  static volatile BlockingQueueHandle instance=new BlockingQueueHandle();
	
	private BlockingQueueHandle() {
		status.compareAndSet(INIT, RUNNNG);
	}
	
	private static BlockingQueueHandle getInstance() {
		return instance;
	}
	
	public static void putTask(Task task) {
		try {
			   queue.put(task);
		} catch (InterruptedException e) {
			log.info("存放任务失败"+e.getMessage());
		}
	}
	
	public static Task takeTask() {
		try {
			Task t=(Task) queue.take();
			if(t instanceof Stop){
				status.compareAndSet(RUNNNG, STOP);
			}
			return t;
		}catch (Exception e) {
			log.info("提取任务失败"+e.getMessage());
		}
		return null;
	}
	
	public static int getStatus() {
		return status.get();
	}
	 
}
