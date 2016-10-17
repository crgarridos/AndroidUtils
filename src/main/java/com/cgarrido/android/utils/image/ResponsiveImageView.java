package com.cgarrido.android.utils.image;

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
        this.setOnTouchListener(new OnTouchListener() {

            public boolean mCancelClick = false;

            @Override
            public boolean onTouch(View v, MotionEvent e) {
                ImageView view = (ImageView) v;
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        mCancelClick = false;// reset state for future events
                        view.getDrawable().setColorFilter(mColorOverlay, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        if(mCancelClick)
                            return false;
                        view.performClick();
                        //continue in down case...
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        return false;
                    }
                    case MotionEvent.ACTION_MOVE:{
                        mCancelClick = e.getX() < 0 || e.getY() < 0
                                    || e.getX() > getMeasuredWidth() || e.getY() > getMeasuredHeight();
                        if(mCancelClick){
                            view.getDrawable().clearColorFilter();
                            view.invalidate();
                        }
                        return mCancelClick;
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
