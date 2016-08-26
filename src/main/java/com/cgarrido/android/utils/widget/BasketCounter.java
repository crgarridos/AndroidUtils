package com.cgarrido.android.utils.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cgarrido.android.utils.MetricsUtils;
import com.cgarrido.android.utils.R;

/**
 * Created by cristian on 10/08/16.
 */
public class BasketCounter extends FrameLayout{
    private FrameLayout mBasket;

    public BasketCounter(Context context) {
        this(context, null);
    }

    public BasketCounter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasketCounter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.basket_counter, this);
//        findViewById(R.id.basket).;
//        buildCounterDrawable(3,0);
    }


    private Drawable buildCounterDrawable(int count, int backgroundImageId) {

//        view.setBackgroundResource(backgroundImageId);


        TextView counter = (TextView) findViewById(R.id.basket_items_count);
        if (count < 0) {
            counter.setVisibility(View.GONE);
        } else if (count <= 5) {
            counter.setText(String.valueOf(count));
        }
        else {
            counter.setText("5+");
        }

        int sw = MetricsUtils.convertDpToPixel(36,getContext()).intValue();

        measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        layout(0, 0, sw, sw);


        setDrawingCacheEnabled(true);
        setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(getDrawingCache());
        setDrawingCacheEnabled(false);

        return new BitmapDrawable(getContext().getResources(), bitmap);
    }
}
