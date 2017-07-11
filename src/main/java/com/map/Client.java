package com.map;

/**
 * Created by Administrator on 2017/7/11/011.
 */
public class Client {
    public static void main(String[] args) {
        MyMap<String,Object> map = new MyHashMap<String, Object>();
        for(int i = 0 ; i < 1000 ; i ++ ){
            map.put("xhd"+i,"hy"+i);
        }

    }
}
