package com.cgarrido.android.utils;

import java.util.HashMap;

/**
 * Created by cristian on 07/12/2015.
 */
public class HashParams<T> extends HashMap<String, T> {

    public static <T> HashParams<T> singleton(String key, T value) {
        HashParams hash = new HashParams();
        hash.add(key, value);
        return hash;
    }

    public HashMap add(String key, T value) {
        super.put(key, value);
        return this;
    }

    public String toJsonString(){
        return JsonString.valueOf(this);
    }
}
