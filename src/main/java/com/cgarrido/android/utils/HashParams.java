package com.cgarrido.android.utils;

import java.util.HashMap;

/**
 * Created by cristian on 07/12/2015.
 */
public class HashParams extends HashMap<String,Object>{

    public HashMap add(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public static HashParams singleton(String key, Object value){
        HashParams hash = new HashParams();
        hash.add(key, value);
        return hash;
    }

    public String toJsonString(){
        return JsonString.valueOf(this);
    }
}
