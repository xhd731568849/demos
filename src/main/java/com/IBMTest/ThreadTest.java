package com.IBMTest;

/**
 * IBM非常经典的线程面试题
 * 有这样一个场景，要求：子线程循环跑30次，暂停，然后转到主线程跑40次，接着，子线程循环跑30次，暂停，
 * 然后，转到主线程跑40次，如此往复，一共这样交替50次
 */
public class ThreadTest {
    public static void main(String[] args) {
        //把资源类引进来
        final BusinessDemo bDemo = new BusinessDemo();
        //子线程要求，50轮换，每个轮换循环跑30次
        new Thread(new Runnable() {
            public void run() {
                for(int i = 1 ; i <= 50 ; i ++ ){
                    //要跑30次
                    //操作资源类
                    bDemo.sonBusiness(i);
                    //封装没有实现线程安全的对象Map
                }
            }
        }).start();

        //50轮回，主线程部分
        for(int i = 1 ; i <= 50 ; i ++ ){
            //主线程进来一次40次
            //操作资源类
            bDemo.mainBusiness(i);
        }
    }
}
