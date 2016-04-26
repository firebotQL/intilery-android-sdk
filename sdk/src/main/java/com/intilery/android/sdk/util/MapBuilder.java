package com.intilery.android.sdk.util;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder<K, V> {

    private Map<K, V> map;

    public MapBuilder(){
        map = new HashMap<>();
    }

    public MapBuilder<K, V> put(K key, V v){
        map.put(key, v);
        return this;
    }

    public Map<K, V> build() {
        return map;
    }

}
