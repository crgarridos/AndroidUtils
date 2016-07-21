package com.ylly.android.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cgarrido on 12/08/15.
 */
public abstract class MetricsUtils {

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static Float convertDpToPixel(float dp, Context context){
//        Resources resources = context.getResources();
//        DisplayMetrics metrics = resources.getDisplayMetrics();
//        float px = dp * (metrics.densityDpi / 160f);
//        return px;

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static Float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static Point getWindowMesures(Context ctx){
        Display display = ((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return  size;
    }

    public static boolean inViewInBounds(View view, float x, float y){
        return inViewInBounds(view, (int) x, (int) y);
    }

    public static boolean inViewInBounds(View view, int x, int y){
        return x >= 0 && x <= view.getMeasuredWidth() && y >= 0 && y <= view.getMeasuredHeight();
//        Rect outRect = new Rect();
//        int[] location = new int[2];
//        location[0] = (int) view.getX();
//        location[1] = (int) view.getY();
//        view.getDrawingRect(outRect);
////        view.getLocationOnScreen(location);
//        outRect.offset(location[0], location[1]);
//        return outRect.contains(x, y);
    }
}
