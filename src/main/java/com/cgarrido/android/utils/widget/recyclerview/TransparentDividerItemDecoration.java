package com.cgarrido.android.utils.widget.recyclerview;

//https://gist.github.com/polbins/e37206fbc444207c0e92

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cgarrido.android.utils.AndroidUtils;
import com.cgarrido.android.utils.MetricsUtils;

public class TransparentDividerItemDecoration extends RecyclerView.ItemDecoration {
    private int mDividerSize;

    public TransparentDividerItemDecoration(int dpSize) {
        mDividerSize = MetricsUtils.convertDpToPixel(dpSize, AndroidUtils.getCtx()).intValue();
    }

//
//    @Override
//    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//
//        int childCount = parent.getChildCount();
//
//        if(mMode == Mode.HORIZONTAL){
//            int top = parent.getPaddingTop();
//            int bottom = parent.getHeight() - parent.getPaddingBottom();
//
//            for (int i = 0; i < childCount - 1; i++) {
//                View child = parent.getChildAt(i);
//
//                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
//
//                int left = child.getRight() + params.leftMargin;
//                int right = left + mDividerSize;//mDivider.getIntrinsicWidth();
//
//                Paint p = new Paint();
//                p.setColor(Color.TRANSPARENT);
//                c.drawRect(left, top, right, bottom, p);
//            }
//        } else {
//            int left = parent.getPaddingLeft();
//            int right = parent.getWidth() - parent.getPaddingRight();
//
//            for (int i = 0; i < childCount; i++) {
//                View child = parent.getChildAt(i);
//
//                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
//
//                int top = child.getBottom() + params.bottomMargin;
//                int bottom = top + mDividerSize;
//
//                Paint p = new Paint();
//                p.setColor(Color.TRANSPARENT);
//                c.drawRect(left, top, right, bottom, p);
//            }
//        }
//    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) == 0) {
            return;
        }

        int mOrientation = ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
        if (mOrientation == LinearLayoutManager.VERTICAL)
            outRect.top = mDividerSize;

        if (mOrientation == LinearLayoutManager.HORIZONTAL)
            outRect.left = mDividerSize;
    }
}