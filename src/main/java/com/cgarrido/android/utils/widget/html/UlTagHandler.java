package com.cgarrido.android.utils.widget.html;

import android.text.Editable;
import android.text.Html;

import org.xml.sax.XMLReader;

/**
 * Created by cristian on 13/10/2016.
 */

public class UlTagHandler implements Html.TagHandler{
    @Override
    public void handleTag(boolean opening, String tag, Editable output,
                          XMLReader xmlReader) {
        if(tag.equals("ul") && !opening) output.append("\n");
        if(tag.equals("li") && opening) output.append("\n  •  ");
    }
}