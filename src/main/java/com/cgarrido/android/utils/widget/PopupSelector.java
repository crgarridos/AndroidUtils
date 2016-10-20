package com.cgarrido.android.utils.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cgarrido.android.utils.R;
import com.cgarrido.android.utils.widget.adapter.PopupSelectorAdapter;

public class PopupSelector extends TextView {

    private ListAdapter mAdapter;
    private OnItemSelectedListener mListener;
    private String mDefaultText;
    private Object mSelected;
    private String mTitle;

    public PopupSelector(Context context) {
        super(context);
    }

    public PopupSelector(Context context, AttributeSet attr) {
        this(context, attr, R.attr.spinnerStyle);
    }

    public PopupSelector(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }


    private DialogInterface.OnClickListener mChoiseListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            setSelected(which);
            if(mListener != null)
                mListener.onItemSelected(getSelected(), which);
            dialog.dismiss();
        }

    };

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            if (mTitle != null && !mTitle.isEmpty())
                builder.setTitle(mTitle);
            builder.setSingleChoiceItems(mAdapter, -1, mChoiseListener);
            builder.setCancelable(true);
            builder.show();
        }
    };

    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            mSelected = null;
        }
    };


    public void setAdapter(PopupSelectorAdapter adapter){
        ListAdapter oldAdapter = this.mAdapter;
        setOnClickListener(null);
        this.mAdapter = adapter;
        if (oldAdapter != null) {
            oldAdapter.unregisterDataSetObserver(dataSetObserver);
        }

        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(dataSetObserver);
            mSelected = null;
            setOnClickListener(onClickListener);
        }
        refreshSpinner();
    }

    private void refreshSpinner() {
        setText(mSelected != null ? mSelected.toString() : getDefaultText());
    }

    public Object getSelected() {
        return this.mSelected;
    }

    public void setSelected(int index) {
        if(index < 0) {
            setText(getDefaultText());
        } else {
            mSelected = mAdapter.getItem(index);
            setText(mSelected.toString());
        }
    }


    public void setSelectedItem(Object item) {
        for(int i = 0 ; i < mAdapter.getCount(); i++) {
            if (mAdapter.getItem(i).equals(item)) {
                setSelected(i);
                return;
            }
        }
        setSelected(-1);
    }

    public String getDefaultText() {
        return mDefaultText;
    }

    public void setDefaultText(String defaultText) {
        this.mDefaultText = defaultText;
        refreshSpinner();
    }


    public OnItemSelectedListener getListener() {
        return mListener;
    }

    public void setListener(OnItemSelectedListener mListener) {
        this.mListener = mListener;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public interface OnItemSelectedListener{
        void onItemSelected(Object itemSelected, int index);
    }

}