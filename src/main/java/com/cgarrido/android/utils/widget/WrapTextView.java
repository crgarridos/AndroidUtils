package com.cgarrido.android.utils.widget;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Tightly wraps the text when setting the maxWidth.
 * @author sky (http://stackoverflow.com/questions/10913384/how-to-make-textview-wrap-its-multiline-content-exactly)
 */
public class WrapTextView extends TextView {
    private boolean hasMaxWidth;

    public WrapTextView(Context context) {
        this(context, null, 0);
    }

    public WrapTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Layout layout = getLayout();
        if (layout != null) {
            int width = (int) Math.ceil(getMaxLineWidth(layout))
                    + getCompoundPaddingLeft() + getCompoundPaddingRight();
            int height = getMeasuredHeight();
            setMeasuredDimension(width, height);
        }
    }

    private float getMaxLineWidth(Layout layout) {
        float max_width = 0.0f;
        int lines = layout.getLineCount();
        for (int i = 0; i < lines; i++) {
            if (layout.getLineWidth(i) > max_width) {
                max_width = layout.getLineWidth(i);
            }
        }
        return max_width;
    }
}