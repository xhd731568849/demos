package com.thread;

import java.util.concurrent.CountDownLatch;

/**
 * Created by xuhandong on 2017/7/27/027.
 */
public abstract class BaseHealthChecker implements Runnable {

    private CountDownLatch _latch;
    private String _serviceName;
    private boolean _serviceUp;

    public BaseHealthChecker(String serviceName , CountDownLatch latch){
        super();
        this._latch = latch;
        this._serviceName = serviceName;
        this._serviceUp = false;
    }

    public void run() {
        try{
            verifyService();
            _serviceUp = true;
        }catch (Throwable t){
            t.printStackTrace(System.err);
            _serviceUp = false;
        }finally {
            if(_latch != null){
                _latch.countDown();
            }
        }
    }

    public String getServiceName(){
        return _serviceName;
    }

    public boolean isServiceUp(){
        return _serviceUp;
    }

    public abstract void verifyService();

}
