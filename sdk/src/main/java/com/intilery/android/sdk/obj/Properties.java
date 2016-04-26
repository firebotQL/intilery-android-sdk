package com.intilery.android.sdk.obj;

import java.util.Collections;
import java.util.Map;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Properties {
    @Getter(onMethod = @__(@Deprecated)) //Internal Structure of this class may change at any time so it is not map based so getMap is deprecated.
    private final Map<String, String> map;

    public Properties(Map<String,String> map){
        this.map = Collections.unmodifiableMap(map);
    }

    public String get(String key) {
        return map.get(key);
    }
}
