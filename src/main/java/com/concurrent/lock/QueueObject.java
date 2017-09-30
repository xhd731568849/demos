package com.concurrent.lock;

/**
 * Created by xuhandong on 17-9-30.
 */
public class QueueObject {
    private boolean isNotified = false;
    public synchronized void doWait() throws InterruptedException {
        while(!isNotified){
            this.wait();
        }
        this.isNotified = false;
    }
    public synchronized void doNotify(){
        this.isNotified = true;
        this.notify();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
}
