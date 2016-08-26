package com.cgarrido.android.utils.widget;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by cristian on 01/08/16.
 */
public class HtmlTextView extends TextView{


    public HtmlTextView(Context context) {
        super(context);
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HtmlTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        StringBuilder stringBuilder = new StringBuilder(text.length());
        stringBuilder.append(text);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            super.setText(Html.fromHtml(stringBuilder.toString(),Html.FROM_HTML_MODE_LEGACY), type);
        } else {
            super.setText(Html.fromHtml(stringBuilder.toString()), type);
        }
    }
}
