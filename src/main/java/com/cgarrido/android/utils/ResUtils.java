package com.ylly.android.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cristian on 12/01/2016
 */
public class ResUtils {

    @ColorInt
    public static int color(@ColorRes int resId) {
        return ContextCompat.getColor(YllyUtils.getCtx(), resId);
    }

    /**
     * Returns a localized string from the application's package's
     * default string table.
     *
     * @param resId Resource id for the string
     * @return The string data associated with the resource, stripped of styled
     * text information.
     */
    @NonNull
    public static String str(@StringRes int resId) {
        return YllyUtils.getCtx().getResources().getString(resId);
    }

    public static Drawable drw(@DrawableRes int resId){
        assert YllyUtils.getCtx() != null;
        return ContextCompat.getDrawable(YllyUtils.getCtx(), resId);
    }


    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    /**
     * Generate a value suitable for use in {@link View#setId(int)}.
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (;;) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }
    }
}
