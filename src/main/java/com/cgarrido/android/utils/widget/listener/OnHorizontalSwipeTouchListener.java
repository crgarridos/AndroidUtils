package com.cgarrido.android.utils.widget.listener;

/**
 * Created by cristian on 23/08/16.
 *
 * Usage:

 imageView.setOnTouchListener(new OnHorizontalSwipeTouchListener() {
 public void onSwipeTop() {
 Toast.makeText(MyActivity.this, "top", Toast.LENGTH_SHORT).show();
 }
 public void onSwipeRight() {
 Toast.makeText(MyActivity.this, "right", Toast.LENGTH_SHORT).show();
 }
 public void onSwipeLeft() {
 Toast.makeText(MyActivity.this, "left", Toast.LENGTH_SHORT).show();
 }
 public void onSwipeBottom() {
 Toast.makeText(MyActivity.this, "bottom", Toast.LENGTH_SHORT).show();
 }

 public boolean onTouch(View v, MotionEvent event) {
 return gestureDetector.onTouchEvent(event);
 }
 });

 */
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public abstract class OnHorizontalSwipeTouchListener implements OnTouchListener {

//    @SuppressWarnings("deprecation")
    private final GestureDetector gestureDetector = new GestureDetector(new GestureListener());

    public boolean onTouch(final View v, final MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
//            onTouch(e);
            gestureDetector.onTouchEvent(event);
            return true;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    // onTouch(e);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return false;
        }
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        return false;
//    }
    public abstract void onSwipeRight();

    public abstract void onSwipeLeft();
}