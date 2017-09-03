package com.map;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/10/010.
 * public vnew class void use
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

    class Entry<K,V> implements MyMap.Entry<K,V>{
        K k;
        V v;
        Entry<K,V> next;

        public Entry(K k , V v , Entry<K,V> next){
            this.k = k;
            this.v = v;
            this.next = next;
        }

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
        //获取到存放数据的数组位置
        int index = getIndex(k,table.length);
        Entry<K,V> entry = table[index];
        if(entry == null){
            //直接使用这个位置,就是将数据存入到这个位置
            table[index] = new Entry(k,v,null);
            //占用数组的位置数,
            useSize++;
        }else{
            //这个位置已经有一个数据了
            table[index] = new Entry(k,v,entry);
        }
        return table[index].getValue();
    }

    private int getIndex(K k, int length) {
        //数组长度都是2^n-1 = m 二进制都是1111 就是有效性数字,那么这个数字在位移的过程中才有意义
        int m = length - 1;
        int index = myHash(k.hashCode())&m;  //保证位置不会超出length之外
        return index;
    }

    // %m 的算法不建议
    private int myHash(int hashCode) {
        hashCode = hashCode ^ ((hashCode>>>20)^(hashCode>>>12));
        return hashCode ^ ((hashCode>>>7)^(hashCode>>>4));
    }

    private void up2Size() {
        Entry<K,V>[] newTable = new Entry[defaultLength<<1];
        //将老数组里面的内容拿出来再次散列
        againHash(newTable);
    }

    private void againHash(MyHashMap<K,V>.Entry<K,V>[] newTable) {
        //定义一个临时数组,将老数组的内容拿出来
        List<Entry<K,V>> entryList = new ArrayList<Entry<K, V>>();
        //对旧数组进行遍历
        for (int i = 0 ; i < table.length ; i ++ ){
            if(table[i] == null){
                continue;
            }
            //找到对象,并存入entryList
            foundEntryByNext(table[i],entryList);
            if(entryList.size() > 0){
                useSize = 0 ;
                defaultLength = defaultLength << 1 ;
                table = newTable ;
                for (Entry<K,V> entry : entryList){
                    //判断遍历的entry是否有链表结构,原先的链表关系我们要设置为无效
                    if(entry.next != null){
                        entry.next = null;
                    }
                    put(entry.getKey(),entry.getValue());
                }
            }
        }
    }

    //找到这个对象不为空存入entryList
    private void foundEntryByNext(MyHashMap<K,V>.Entry<K, V> entry, List<MyHashMap<K,V>.Entry<K, V>> entryList) {
        if(entry != null && entry.next != null){
            entryList.add(entry);
            //递归
            foundEntryByNext(entry.next,entryList);
            //为什么不写  entry != null && entry.next == null  因为 上面判断了table[i] == null 所以 else
            // 就相当于 entry != null && entry.next == null
        }else{
            entryList.add(entry);
        }
    }

    public V get(K k) {
        int index = getIndex(k,table.length);
        if(table[index] == null){
            throw new NullPointerException();
        }
        return findValueByEquesKey(k,table[index]);
    }

    private V findValueByEquesKey(K k, MyHashMap<K,V>.Entry<K, V> entry) {
        if(k==entry.getKey() || k.equals(entry.getKey())){
            return entry.getValue();
        }else if(entry.next != null){
            return findValueByEquesKey(k,entry.next);
        }
        return null;
    }

}
