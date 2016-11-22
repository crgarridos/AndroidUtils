package com.cgarrido.android.utils.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by cristian on 09/11/2016.
 */

public class SwipePagingDisabledViewPager extends ViewPager {

    private boolean isSwipePagingEnabled = false;

    public SwipePagingDisabledViewPager(Context context) {
        super(context);
    }

    public SwipePagingDisabledViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isSwipePagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return isSwipePagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void enableSwipePaging(boolean enable) {
        this.isSwipePagingEnabled = enable;
    }

    public void prevPage() {
        setCurrentItem(getCurrentItem() - 1);
    }

    public void nextPage() {
        setCurrentItem(getCurrentItem() + 1);
    }
}
