package com.lzx.compareTest.handle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Handle{
	
	private ExecutorService executor;
	private int threadNum=5;
	public Handle(int threadNum){
		this.threadNum=threadNum;
		executor=new ThreadPoolExecutor(threadNum, threadNum+2,100,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
	    init();
	}
	
	public void init() {
		for(int i=0;i<threadNum;i++) {
    		executor.execute(new HandleThread());
    	}
	}
    
	public void shutdown() {
		executor.shutdownNow();
	}
     
	
    
}
