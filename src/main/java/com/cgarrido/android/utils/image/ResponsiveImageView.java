package com.ylly.android.utils.image;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by cristian on 13/10/2015.
 */
public class ResponsiveImageView extends ImageView {

    //overlay is black with transparency of 0x77 (119)
    private int mColorOverlay = 0x77000000;

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
        this.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        view.getDrawable().setColorFilter(mColorOverlay, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        view.performClick();
                        //continue in down case...
                    }
                    case MotionEvent.ACTION_OUTSIDE:
                    case MotionEvent.ACTION_CANCEL: {
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        return false;
                    }


                }
                return false;

            }
        });
    }

    public void setColorOverlay(int colorOverlay) {
        this.mColorOverlay = colorOverlay;
    }
}