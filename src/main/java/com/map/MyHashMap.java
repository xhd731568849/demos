package com.map;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/10/010.
 */
public class MyHashMap<K,V> implements MyMap<K,V> {

    private static int defaultLength = 1<<4;

    private static double defaultAddSizeFactor = 0.75;

    private int useSize;

    private Entry<K,V>[] table = null;

    public MyHashMap(){
        this(defaultLength,defaultAddSizeFactor);
    }

    public MyHashMap(int length , double factor){
        if(length < 0){
            throw new IllegalArgumentException("参数不能为负数："+length);
        }
        if(factor < 0 || Double.isNaN(factor)){
            throw new IllegalArgumentException("扩容必须是大于零的数字："+factor);
        }
        this.defaultLength = length;
        this.defaultAddSizeFactor = factor;
        table = new Entry[defaultLength];
    }

    class Entry<K,V> implements Map.Entry<K,V>{
        K k;
        V v;
        Entry<K,V> next;

        public K getKey() {
            return k;
        }

        public V getValue() {
            return v;
        }

        public V setValue(V value) {
            return null;
        }
    }

    public V put(K k, V v) {
        //存之前判断是否要扩容
        if(useSize > defaultLength*defaultAddSizeFactor){
            //扩容2倍
            up2Size();
        }
        return null;
    }

    private void up2Size() {
        
    }

    public V get(K k) {
        return null;
    }

}
