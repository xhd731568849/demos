package com.concurrent.lock;

/**
 * Created by xuhandong on 17-9-30.
 */
public class Test {
    public static void main(String[] args) {
        Integer key = new Integer(1);
        A a = new A(key);
        Thread thread1 = new Thread(a);
        Thread thread2 = new Thread(a);

        thread1.start();
        thread2.start();

    }
}
class A implements Runnable{

    Integer key;
    Lock lock = new Lock();

    public A(Integer key){
        this.key = key;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            lock.lock();
            key ++;
            System.out.println(key);
            lock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
