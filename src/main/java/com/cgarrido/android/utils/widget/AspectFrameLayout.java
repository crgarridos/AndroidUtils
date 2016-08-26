package com.cgarrido.android.utils.widget;
/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.cgarrido.android.utils.R;

/**
 * Layout that adjusts to maintain a specific aspect ratio.
 */
public class AspectFrameLayout extends FrameLayout {
    private static final String TAG = "AFL";
    public static boolean ENABLE_LOGS = false;

    private double mTargetAspect = -1.0;        // initially use default window size

    public AspectFrameLayout(Context context) {
        super(context, null);
    }

    public AspectFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AspectFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AspectFrameLayout);
        mTargetAspect = typedArray.getFraction(R.styleable.AspectFrameLayout_aspectRatio,1,1, .75f);
        typedArray.recycle();
    }


    /**
     * Sets the desired aspect ratio.  The value is <code>width / height</code>.
     */
    public void setAspectRatio(double aspectRatio) {
        if (aspectRatio < 0) {
            throw new IllegalArgumentException();
        }
        if(ENABLE_LOGS)
            Log.d(TAG, "Setting aspect ratio to " + aspectRatio + " (was " + mTargetAspect + ")");
        if (mTargetAspect != aspectRatio) {
            mTargetAspect = aspectRatio;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(ENABLE_LOGS)
            Log.d(TAG, "onMeasure target=" + mTargetAspect +
                " width=[" + MeasureSpec.toString(widthMeasureSpec) +
                "] height=[" + View.MeasureSpec.toString(heightMeasureSpec) + "]");

        // Target aspect ratio will be < 0 if it hasn't been set yet.  In that case,
        // we just use whatever we've been handed.
        if (mTargetAspect > 0) {
            int initialWidth = MeasureSpec.getSize(widthMeasureSpec);
            int initialHeight = MeasureSpec.getSize(heightMeasureSpec);

            // factor the padding out
            int horizPadding = getPaddingLeft() + getPaddingRight();
            int vertPadding = getPaddingTop() + getPaddingBottom();
            initialWidth -= horizPadding;
            initialHeight -= vertPadding;

            double viewAspectRatio = (double) initialWidth / initialHeight;
            double aspectDiff = mTargetAspect / viewAspectRatio - 1;

            if (Math.abs(aspectDiff) < 0.01) {
                // We're very close already.  We don't want to risk switching from e.g. non-scaled
                // 1280x720 to scaled 1280x719 because of some floating-point round-off error,
                // so if we're really close just leave it alone.
                if(ENABLE_LOGS)
                    Log.d(TAG, "aspect ratio is good (target=" + mTargetAspect +
                        ", view=" + initialWidth + "x" + initialHeight + ")");
            } else {
//                if (aspectDiff > 0) {
                    // limited by narrow width; restrict height
                    initialHeight = (int) (initialWidth * mTargetAspect);
//                } else {
//                    // limited by short height; restrict width
//                    initialWidth = (int) (initialHeight * mTargetAspect);
//                }
                if(ENABLE_LOGS)
                    Log.d(TAG, "new size=" + initialWidth + "x" + initialHeight + " + padding " +
                        horizPadding + "x" + vertPadding);
                initialWidth += horizPadding;
                initialHeight += vertPadding;
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(initialWidth, MeasureSpec.EXACTLY);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(initialHeight, MeasureSpec.EXACTLY);
            }
        }
        if(ENABLE_LOGS)
            Log.d(TAG, "set width=[" + MeasureSpec.toString(widthMeasureSpec) +
                "] height=[" + View.MeasureSpec.toString(heightMeasureSpec) + "]");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}