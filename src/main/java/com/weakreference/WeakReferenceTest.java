package com.weakreference;

import java.lang.ref.WeakReference;

public class WeakReferenceTest {
    public static void main(String[] args) {
        Person person = new Person();
        WeakReference<Person> wr = new WeakReference<Person>(person);
        while(true){
            //System.out.println(person);
            if(wr.get()!=null){
                System.out.println("对象还存在");
            }else {
                System.out.println("对象被回收了！！！");
                break;
            }
        }

    }
}
