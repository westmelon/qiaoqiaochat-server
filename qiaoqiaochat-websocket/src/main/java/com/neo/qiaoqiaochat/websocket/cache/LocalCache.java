package com.neo.qiaoqiaochat.websocket.cache;


import java.util.concurrent.ConcurrentHashMap;

public class LocalCache<K, V> {

    private ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();


    public void put(K key, V value){
        cache.put(key, value);
    }

    public V get(K key){
        return cache.get(key);
    }

    public V remove(K key){
        return cache.remove(key);
    }


}
