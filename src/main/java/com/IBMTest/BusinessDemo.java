package com.IBMTest;

/**
 * 资源类
 */
public class BusinessDemo {
    //内部 有一个控制开关
    private boolean isShowSonThread = true;
    //子线程先进来搞事儿
    public synchronized void sonBusiness(int i){
        while (!isShowSonThread){
            try {
                //子线程在外面等待
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //代表厕所没人
        for (int j = 1 ; j<=30;j++){
            System.out.println("=============子线程运行第："+i+"轮， 第："+j+"次！");
        }
        //厕所门打开
        isShowSonThread = false;
        //通知主线程
        this.notify();
    }

    //主线程业务模块synchronized 隐式锁，锁对象，封装到里面，不好控制，效率非常底下，
    //sychronized 时间换线程安全(线程空间）
    public synchronized void mainBusiness(int i){
        while (isShowSonThread){
            try {
                //主线程在外面等待
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //代表厕所没人 主线程就去
        for (int j = 1 ; j<=40;j++){
            System.out.println("主线程运行第："+i+"轮， 第："+j+"次！");
        }
        //厕所门打开
        isShowSonThread = true;
        //通知主线程
        this.notify();
    }
}
