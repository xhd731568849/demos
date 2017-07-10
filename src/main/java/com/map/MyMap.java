package com.map;

/**
 * Created by Administrator on 2017/7/10/010.
 */
public interface MyMap<K,V> {

    public V put(K k , V v);

    public V get(K k);

    public interface Entry<K,V>{
        public K getKey();
        public V getValue();
    }

}
