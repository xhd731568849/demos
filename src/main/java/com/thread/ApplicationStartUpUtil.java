package com.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by xuhandong on 2017/7/27/027.
 */
public class ApplicationStartUpUtil {

    private static List<BaseHealthChecker> _services;
    private static CountDownLatch _latch;

    private ApplicationStartUpUtil(){}

    private final static ApplicationStartUpUtil INSTANCE = new ApplicationStartUpUtil();

    public static ApplicationStartUpUtil getInstance(){
        return INSTANCE;
    }

    public static boolean checkExternalServices() throws InterruptedException {
        _latch = new CountDownLatch(3);
        _services = new ArrayList<BaseHealthChecker>();
        _services.add(new NetWorkHealthChecker(_latch));
        _services.add(new CacheHealthChecker(_latch));
        _services.add(new DatabaseHealthChecker(_latch));

        Executor executor = Executors.newFixedThreadPool(_services.size());

        for(final BaseHealthChecker v : _services){
            executor.execute(v);
        }

        _latch.await();

        for (final BaseHealthChecker v : _services){
            if( !v.isServiceUp()){
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        boolean result = false;
        try{
            result = ApplicationStartUpUtil.checkExternalServices();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("External services validation completed !! Result was :"+ result);
    }

}
