package com.concurrent.lock;

import java.util.ArrayList;
import java.util.List;

/**
 * 公平锁
 * Created by xuhandong on 17-9-30.
 */
public class FairLock {
    private boolean isLocked = false;
    private Thread lockingThread = null;
    private List<QueueObject> waitingThreads = new ArrayList<>();

    public void lock() throws InterruptedException {
        QueueObject queueObject = new QueueObject();
        boolean isLockedForThisThread = true;
        synchronized (this){
            waitingThreads.add(queueObject);
        }
        while(isLockedForThisThread){
            synchronized (this){
                isLockedForThisThread = isLocked || waitingThreads.get(0) != queueObject;
                if(!isLockedForThisThread){
                    isLocked = true;
                    waitingThreads.remove(queueObject);
                    lockingThread = Thread.currentThread();
                    return ;
                }
            }
            try {
                queueObject.doWait();
            } catch (InterruptedException e) {
                synchronized (this){
                    waitingThreads.remove(queueObject);
                    throw e;
                }
            }
        }
    }

    /**
     * 调用unlock()的线程将从队列头部获取QueueObject，并对其调用doNotify()，
     * 以唤醒在该对象上等待的线程。通过这种方式，在同一时间仅有一个等待线程获得唤醒，
     * 而不是所有的等待线程。这也是实现FairLock公平性的核心所在。
     */
    public synchronized void unlock(){
        if(this.lockingThread != Thread.currentThread()){
           // throw new IllegalMonitorStateException("Calling thread has not locked this lock ");
            return ;
        }
        isLocked = false;
        lockingThread = null;
        if(waitingThreads.size()>0){
            waitingThreads.get(0).doNotify();
        }
    }

}
