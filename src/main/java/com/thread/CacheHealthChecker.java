package com.thread;

import java.util.concurrent.CountDownLatch;

/**
 * Created by xuhandong on 2017/7/27/027.
 */
public class CacheHealthChecker extends BaseHealthChecker  {
    public CacheHealthChecker(CountDownLatch latch) {
        super("cache Service", latch);
    }

    public void verifyService() {
        System.out.println("Checking ..... "+this.getServiceName());
        try{
            Thread.sleep(5000);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(this.getServiceName()+" is  UP");
    }
}
