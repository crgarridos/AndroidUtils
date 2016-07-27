package com.cgarrido.android.utils.widgets;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;

import java.lang.reflect.Field;

/**
 * Created by cristian on 03/06/16.
 */
public class ClickableViewPager extends ViewPager {

    private static final float DRAG_THRESHOLD = 10;
    private float mDownX;
    private float mDownY;
    private boolean mDownTouched;

    private int mCyclicCount = 0;

    public ClickableViewPager(Context context) {
        super(context);
        postInitViewPager();
    }

    public ClickableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        postInitViewPager();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(getAdapter() ==  null) return super.onTouchEvent(ev);
        Log.d("count", getCurrentItem() + " " + mCyclicCount + " " + getAdapter().getCount() + ", event:" + ev);
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            mDownX = ev.getX();
            mDownY = ev.getY();
            mDownTouched = true;
            return true;
        } else if(ev.getAction() == MotionEvent.ACTION_UP ){
            if (mDownTouched ) {
                if(ev.getX() > getWidth()*3/4) {
                    //go to next
                    setCurrentItem(++mCyclicCount);

                }else if(ev.getX() < getWidth()/4){
                    setCurrentItem(--mCyclicCount);
                }
                return true;

            }
        }
        else if(ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (mDownTouched && (Math.abs(mDownX - ev.getX()) > DRAG_THRESHOLD || Math.abs(mDownY - ev.getY()) > DRAG_THRESHOLD)) {
                mDownTouched = false;
            }
        }
        return super.onTouchEvent(ev);
    }


    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (item < 0){
            mCyclicCount = 0;
            return;
        }
        if (item >= getAdapter().getCount()) {
            mCyclicCount = getAdapter().getCount() - 1;
            return;
        }
        mCyclicCount = item;
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }



    private ScrollerCustomDuration mScroller = null;

    /**
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private void postInitViewPager() {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new ScrollerCustomDuration(getContext(),
                    (Interpolator) interpolator.get(null));
            scroller.set(this, mScroller);
        } catch (Exception e) {
        }
    }

    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }

}
