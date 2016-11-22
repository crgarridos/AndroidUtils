package com.cgarrido.android.utils.widget.html;

import android.content.Context;
import android.text.Html;
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
        String s = String.valueOf(text).replace("&lt;", "<").replace("\n", "<br>");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            super.setText(Html.fromHtml(s,Html.FROM_HTML_MODE_LEGACY, null, new UlTagHandler()), type);
        } else {
            super.setText(Html.fromHtml(s, null, new UlTagHandler()), type);
        }
    }
}
