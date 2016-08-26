package com.cgarrido.android.utils.widget.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.cgarrido.android.utils.R;

import java.util.List;

/**
 * Created by cristian on 11/08/16.
 */
public class PopupSelectorAdapter<T> extends ArrayAdapter<T> {
    public PopupSelectorAdapter(Context context, List<T> data) {
        super(context, R.layout.dropdown_simple_item, data);
    }
}
