package com.cgarrido.android.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.HashMap;

/**
 * Created by cristian on 26/11/2015.
 */
public class JsonString{
    public String toString() {
        return (new GsonBuilder().setPrettyPrinting().create().toJson(this));
    }

    public static String prettyJson(String s) {
        try {
            JsonElement elem = new JsonParser().parse(s);
            return (new GsonBuilder().setPrettyPrinting().create().toJson(elem));
        }
        catch (Exception ex){
            return s;
        }
    }

    public static  String prettyJSON(Object obj) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(obj);
    }

    public static String valueOf(Object obj) {
        return new Gson().toJson(obj);
    }

    public JsonElement toJson() {
        return  new Gson().toJsonTree(this);
    }

    public HashMap<String, String> toHash() {
        return new Gson().fromJson(toJson(), HashMap.class);
    }
}
