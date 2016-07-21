package com.ylly.android.utils;

import android.content.Context;

/**
 * Created by cristian on 15/01/2016.
 */
public class YllyUtils {

    private static Context mContext;

    public static void initContext(Context context){
        mContext = context;
    }

    public static Context getCtx() {
        return mContext;
    }
}

