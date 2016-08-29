package com.cgarrido.android.utils;

/*  * This class is used to lock orientation of android app in nay android devices
 */

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

public class OrientationUtils {

    private OrientationUtils() {}

    public static void forceScreenOrientationAutomatically(Activity activity) {
        boolean forcePortrait = activity.getResources().getBoolean(R.bool.portraitDevice);
        Log.d("portraitDevice", ": " + forcePortrait);
        int orientation = forcePortrait
                ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
        if (orientation != activity.getRequestedOrientation())
            activity.setRequestedOrientation(orientation);
    }

    /** Locks the device window in landscape mode. */
    public static void lockOrientationLandscape(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    /** Locks the device window in portrait mode. */
    public static void lockOrientationPortrait(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /** Locks the device window in actual screen mode. */
    public static void lockOrientation(Activity activity) {
        final int orientation = activity.getResources().getConfiguration().orientation;
        final int rotation = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getRotation();

        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else if (rotation == Surface.ROTATION_180 || rotation == Surface.ROTATION_270) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            }
        }
    }

    /** Unlocks the device window in user defined screen mode. */
    public static void unlockOrientation(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
    }

}