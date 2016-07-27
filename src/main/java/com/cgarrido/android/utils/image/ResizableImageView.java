package com.cgarrido.android.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cgarrido.android.utils.R;
import com.cgarrido.android.utils.MetricsUtils;
import com.cgarrido.android.utils.ResUtils;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by cgarrido on 11/08/15.
 * ;)
 */
public class ResizableImageView extends RelativeLayout implements View.OnFocusChangeListener {

    private static final String TAG = ResizableImageView.class.getSimpleName();

    public static int minW;
    public static int minH;

    private Context mContext;
    private ResizableImageView mThis;
    private OnChangeListener mChangeListener;

//    public ImageView getImageView() {
//        return mImageView;
//    }

    protected ImageView mImageView;

    protected ResizersList mResizerManager;
    protected View mResizerTop;
    protected View mResizerBottom;
    protected View mResizerTopRight;
    protected View mResizerTopLeft;
    protected View mResizerBottomRight;
    protected View mResizerBottomLeft;
    protected View mResizerRight;
    protected View mResizerLeft;

    public ResizableImageView(Context context) {
        super(context);
        init(context);
    }

    public ResizableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ResizableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setBorderColor(int borderColor) {
        mImageView.getBackground().setColorFilter(borderColor, PorterDuff.Mode.MULTIPLY);
        mResizerManager.setColorFilter(borderColor, PorterDuff.Mode.MULTIPLY);
    }

    private void init(Context context) {

        mContext = context;
        mThis = this;
        mThis.setId(ResUtils.generateViewId());

        RelativeLayout mLayout = (RelativeLayout) inflate(getContext(), R.layout.resize_imageview, this);

        mImageView = (ImageView) mLayout.findViewById(R.id.resize_image);

        mResizerManager = new ResizersList(new View[]{
                //C'EST SUPER IMPORTANT L'ORDRE!!!! (voir {@Link:ResizersList#getOpposite})
                mResizerBottom = mLayout.findViewById(R.id.resize_bottom),
                mResizerTop = mLayout.findViewById(R.id.resize_top),
                mResizerRight = mLayout.findViewById(R.id.resize_right),
                mResizerLeft = mLayout.findViewById(R.id.resize_left),
                mResizerTopRight = mLayout.findViewById(R.id.resize_top_right),
                mResizerBottomLeft = mLayout.findViewById(R.id.resize_bottom_left),
                mResizerTopLeft = mLayout.findViewById(R.id.resize_top_left),
                mResizerBottomRight = mLayout.findViewById(R.id.resize_bottom_right),
        });

        minW = mResizerManager.getResizeSize() * 3;
        minH = mResizerManager.getResizeSize() * 3;

        mLayout.setOnFocusChangeListener(this);
        mLayout.setOnTouchListener(new OnTouchListener() {
            float dx = 0, dy = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mThis.onTouchEvent(event);
                ResizableImageView.this.getFocus();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dx = v.getX() - event.getRawX();
                        dy = v.getY() - event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getRawX() + dx;
                        float y = event.getRawY() + dy;
                        float w = mThis.getMeasuredWidth();
                        float h = mThis.getMeasuredHeight();
                        move(x, y, w, h);
                        Log.d(TAG, "Moving: (x: " + x + ", y: " + y + ")");
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                }
                return false;
            }
        });

        this.setBorderColor(ResUtils.color(android.R.color.holo_green_light));
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setClickable(true);
        this.setEnabled(true);
        this.onFocusChange(this, false);
//        this.getFocus();
    }

    public void centerInParent() {
        int pw = getParentViewGroup().getMeasuredWidth();
        int ph = getParentViewGroup().getMeasuredHeight();
        int w = getLayoutParams().width;
        int h = getLayoutParams().height;
        setLocation(pw / 2 - w / 2, ph / 2 - h / 2);
    }

    public void adjustInParent() {
        int pw = getParentViewGroup().getMeasuredWidth();
        int ph = getParentViewGroup().getMeasuredHeight();
        while (getLayoutParams().width > pw || getLayoutParams().height > ph) {
            resize(0, 0, getLayoutParams().width - 1, getLayoutParams().height - 1);
        }
    }

    public ViewGroup getParentViewGroup() {
        return (ViewGroup) mThis.getParent();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == this) {
            for (View resizeBtn : mResizerManager)
                resizeBtn.setVisibility(hasFocus ? VISIBLE : GONE);
            mImageView.getBackground().setAlpha(hasFocus ? 255 : 0);
        }
    }

    public void setImage(@DrawableRes int drw) {
        setImage(ResUtils.drw(drw));
    }

    public void setImage(Drawable drw) {
        mImageView.setImageDrawable(drw);
        int w = drw.getIntrinsicWidth();
        int h = drw.getIntrinsicHeight();
        this.setLayoutParams(new LayoutParams(w, h));
        mResizerManager.setAutoPadding(w, h);
    }

    public void setImage(Bitmap drw) {
        mImageView.setImageBitmap(drw);
        int w = drw.getWidth();
        int h = drw.getHeight();
        this.setLayoutParams(new LayoutParams(w, h));
        mResizerManager.setAutoPadding(w, h);
    }

    public void setLocation(int x, int y) {
        this.animate().x(x).y(y).setDuration(0).start();
        callOnChange(x, y, this.getLayoutParams().width, this.getLayoutParams().height);
    }

    public void getFocus() {
        this.requestFocus();
        this.requestFocusFromTouch();
        this.bringToFront();
    }

    private void resize(int x, int y, int width, int height, boolean inner) {
        move(x, y, width, height);
        mThis.setLayoutParams(new LayoutParams(Math.round(width), Math.round(height)));
        mResizerManager.setAutoPadding(width, height);
        callOnChange(x, y, width, height);
    }

    public void resize(int x, int y, int width, int height) {
        resize(x, y, width, height, false);
    }

    protected void callOnChange(int x, int y, int width, int height) {
        if (mChangeListener != null)
            mChangeListener.onChange(mThis, x, y, width, height);
    }

    public void delete() {
        getParentViewGroup().removeView(this);
    }

    protected class ResizersList extends ArrayList<View> implements OnTouchListener {

        private ResizableImageView self;
        private float holdCornerX;
        private float holdCornerY;

        public ResizersList(View[] resizers) {
            self = ResizableImageView.this;
            this.addAll(Arrays.asList(resizers));
            this.setOnTouchListener(this);
        }

        public int getResizeSize() {
            return (int) mContext.getResources().getDimension(R.dimen.resize_button);
        }

        public boolean isResizer(View v) {
            return this.indexOf(v) >= 0;
        }

        public View getOpposite(View v) {
            return this.get(this.indexOf(v) + (this.indexOf(v) % 2 == 0 ? 1 : -1));
        }

        public void setOnTouchListener(OnTouchListener onTouchListener) {
            for (View resizer : this)
                resizer.setOnTouchListener(onTouchListener);
        }

        public void setColorFilter(int color, PorterDuff.Mode srcIn) {
            for (View v : this)
                if (v instanceof ViewGroup)
                    ((ImageView) ((ViewGroup) v).getChildAt(0)).getDrawable().setColorFilter(color, srcIn);
                else if (v instanceof ImageView)
                    ((ImageView) v).getDrawable().setColorFilter(color, srcIn);
        }


        float eventHolderX;
        float eventHolderY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mThis.onInterceptTouchEvent(event);
            if (this.isResizer(v)) {
                self.getFocus();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        this.holdCorner(v);
                        self.getFocus();
                        eventHolderX = event.getX();
                        eventHolderY = event.getY();
                        Log.d(TAG, "Down: (x: " + event.getRawX() + ", y: " + event.getRawY() + ")");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float eventX = event.getX() - eventHolderX;
                        float eventY = event.getY() - eventHolderY;
                        resize(calculateResize(v, eventX, eventY), v, (int) holdCornerX, (int) holdCornerY);
                        break;
                    case MotionEvent.ACTION_UP:
                }
                return true;
            }
            return false;
        }

        private void holdCorner(View resizer) {
            int[] cornerPos = getCornerPosition(mResizerManager.getOpposite(resizer), true);
            holdCornerX = cornerPos[0];
            holdCornerY = cornerPos[1];
        }


        private Rect calculateResize(View resizer, float eX, float eY) {

            float x = mThis.getX();
            float y = mThis.getY();

            float w = mThis.getMeasuredWidth();
            float h = mThis.getMeasuredHeight();

            int id = resizer.getId();

            if (id == R.id.resize_bottom) {
                h = h + eY;

            } else if (id == R.id.resize_top) {
                y = y + eY;
                h = h - eY;

            } else if (id == R.id.resize_right) {
                w = w + eX;

            } else if (id == R.id.resize_left) {
                x = x + eX;
                w = w - eX;

            } else if (id == R.id.resize_top_right) {
                y = y + eY;
                h = h - eY;
                w = w + eX;

            } else if (id == R.id.resize_bottom_left) {
                h = h + eY;
                x = x + eX;
                w = w - eX;

            } else if (id == R.id.resize_top_left) {
                y = y + eY;
                h = h - eY;
                x = x + eX;
                w = w - eX;

            } else if (id == R.id.resize_bottom_right) {
                h = h + eY;
                w = w + eX;
            }

            Log.d(TAG, "Resizing: (x: " + x + ", y: " + y + ", w: " + w + ", h: " + h + ")");
            return new Rect((int) (x), (int) (y), (int) (x + w), (int) (y + h));

        }

        public int[] getCornerPosition(View resizer, boolean offsetParent) {
            int id = resizer.getId();
            int x = 0, y = 0;
            if (id == R.id.resize_bottom) {
                x += self.getMeasuredWidth() / 2;
                y += self.getMeasuredHeight();

            } else if (id == R.id.resize_top) {
                x += self.getMeasuredWidth() / 2;

            } else if (id == R.id.resize_right) {
                x += self.getMeasuredWidth();
                y += self.getMeasuredHeight() / 2;

            } else if (id == R.id.resize_left) {
                y += self.getMeasuredHeight() / 2;

            } else if (id == R.id.resize_top_right) {
                x += self.getMeasuredWidth();

            } else if (id == R.id.resize_bottom_left) {
                y += self.getMeasuredHeight();

            } else if (id == R.id.resize_top_left) {
                //nothing to do ^^

            } else if (id == R.id.resize_bottom_right) {
                x += self.getMeasuredWidth();
                y += self.getMeasuredHeight();
            }
            return new int[]{
                    (int) (x + (offsetParent ? self.getX() : 0)),
                    (int) (y + (offsetParent ? self.getY() : 0))
            };

        }

        public void setAutoPadding(int w, int h) {
            int pw = (w - minW > 0 ? w - minW : 0) / 4;
            int ph = (h - minH > 0 ? h - minH : 0) / 4;

            mResizerTopLeft.setPadding(0, 0, pw, ph);
            mResizerTop.setPadding(pw, 0, pw, ph);
            mResizerTopRight.setPadding(pw, 0, 0, ph);

            mResizerBottomLeft.setPadding(0, ph, pw, 0);
            mResizerBottom.setPadding(pw, ph, pw, 0);
            mResizerBottomRight.setPadding(pw, ph, 0, 0);

            mResizerLeft.setPadding(0, ph, pw, ph);
            mResizerRight.setPadding(pw, ph, 0, ph);
        }
    }

    private void resize(Rect rect, View resizer, int holdX, int holdY) {
        int x = rect.left;
        int y = rect.top;
        int w = rect.right - x;
        int h = rect.bottom - y;

        boolean shiftX = false;
        boolean shiftY = false;
        int i = resizer.getId();
        if (i == R.id.resize_top || i == R.id.resize_left || i == R.id.resize_top_left) {
            shiftX = true;
            shiftY = true;
        } else if (i == R.id.resize_top_right) {
            shiftY = true;
        } else if (i == R.id.resize_bottom_left) {
            shiftX = true;
        }

        if (w < minW) {
            w = minW;
            x = holdX - (shiftX ? minW : 0);
        }
        if (h < minH) {
            h = minH;
            y = holdY - (shiftY ? minH : 0);
        }

        if (x < 0) {
            x = 0;
            w = holdX;
        }
        if (y < 0) {
            y = 0;
            h = holdY;
        }

        ViewGroup p = getParentViewGroup();
        int pw = p.getMeasuredWidth();
        int ph = p.getMeasuredHeight();

        if (x + w > pw) {
            w = pw - x;
        }
        if (y + h > ph) {
            h = ph - y;
        }

        resize(x, y, w, h, true);
    }

    private void move(float x, float y, float w, float h) {
        float pw = getParentViewGroup().getMeasuredWidth();
        float ph = getParentViewGroup().getMeasuredHeight();

        x = x < 0 ? 0 : (x + w > pw ? pw - w : x);
        y = y < 0 ? 0 : (y + h > ph ? ph - h : y);

        this.animate().x(x).y(y).setDuration(0).start();
        System.out.println("moving...");
        callOnChange((int) x, (int) y, (int) w, (int) h);
    }

    public void setOnChangeListener(OnChangeListener l) {
        mChangeListener = l;
    }

    public interface OnChangeListener {
        void onChange(ResizableImageView resizable,int x, int y, int w, int h);
    }
}
