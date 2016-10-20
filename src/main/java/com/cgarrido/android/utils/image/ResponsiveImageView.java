package com.cgarrido.android.utils.image;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by cristian on 13/10/2015.
 */
public class ResponsiveImageView extends ImageView {

    private static final String TAG = ResponsiveImageView.class.getSimpleName();
    //overlay is black with transparency of 0x77 (119)
    private int mColorOverlay = 0x33000000;

    public ResponsiveImageView(Context context) {
        this(context, null);
    }

    public ResponsiveImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ResponsiveImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        clearOverlay();
        this.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent e) {
                getParent().requestDisallowInterceptTouchEvent(true);
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        applyOverlay();
                        return true;
                    case MotionEvent.ACTION_UP:
                        clearOverlay();
                        performClick();
                        return false;
                        //continue in down case...
                    case MotionEvent.ACTION_CANCEL:
                        //clearOverlay the overlay
                        clearOverlay();
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        if (isOutside(e)) {
                            clearOverlay();
                            return false;
                        }
                        return true;
                }
                return false;
            }

        });
    }

    private void applyOverlay() {
        getDrawable().setColorFilter(mColorOverlay, PorterDuff.Mode.SRC_ATOP);
        Log.d(TAG,  "applyOverlay : " + this);
//        invalidate();
    }

    private boolean isOutside(MotionEvent e) {
        return  e.getX() < 0 || e.getY() < 0
                || e.getX() > getMeasuredWidth() || e.getY() > getMeasuredHeight();
    }

    private void clearOverlay() {
        getDrawable().clearColorFilter();
        Log.d(TAG,  "clearOverlay : " + this);
//        invalidate();
    }

    public void setColorOverlay(int colorOverlay) {
        this.mColorOverlay = colorOverlay;
    }
}
