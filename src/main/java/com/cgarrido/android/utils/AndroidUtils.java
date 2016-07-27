package com.cgarrido.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by cristian on 15/01/2016.
 */
public class AndroidUtils {

    private static final String TAG = AndroidUtils.class.getSimpleName();
    private static Context mContext;

    public static void initContext(Context context){
        mContext = context;
    }

    public static Context getCtx() {
        return mContext;
    }

    private static boolean checkForInternetConnection() {
        NetworkInfo activeNetworkInfo = null;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getCtx().getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            Log.d(TAG, "checkForInternetConnection Exception", e);
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isAvailable();
    }
}

