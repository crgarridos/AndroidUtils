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
}
