package com.cgarrido.android.utils.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.cgarrido.android.utils.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiSpinner extends TextView implements OnMultiChoiceClickListener {

    private SpinnerAdapter mAdapter;
    private boolean[] mOldSelection;
    private boolean[] mSelected;
    private String mDefaultText;
    private String mAllText;
    private boolean mAllSelected;
    private MultiSpinnerListener mListener;
    private boolean mPrependCheckMark = false;
    private String mSeparator = ", ";

    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context context, AttributeSet attr) {
        this(context, attr, R.attr.spinnerStyle);
    }

    public MultiSpinner(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        mSelected[which] = isChecked;
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            String choices[] = new String[mAdapter.getCount()];

            for (int i = 0; i < choices.length; i++) {
                choices[i] = mAdapter.getItem(i).toString();
            }

            System.arraycopy(mSelected, 0, mOldSelection, 0, mSelected.length);

            builder.setMultiChoiceItems(choices, mSelected, MultiSpinner.this);

            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    System.arraycopy(mOldSelection, 0, mSelected, 0, mSelected.length);
                    dialog.dismiss();
                }
            });

            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    refreshSpinner();
                    if(mListener != null)
                        mListener.onItemsSelected(mSelected);
                    dialog.dismiss();
                }
            });
            builder.setCancelable(true);
            builder.show();
        }
    };

    public SpinnerAdapter getAdapter() {
        return this.mAdapter;
    }

    DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            // all selected by default
            mOldSelection = new boolean[mAdapter.getCount()];
            mSelected = new boolean[mAdapter.getCount()];
            for (int i = 0; i < mSelected.length; i++) {
                mOldSelection[i] = false;
                mSelected[i] = mAllSelected;
            }
        }
    };


    public void setAdapter(SpinnerAdapter adapter){
        setAdapter(adapter, false, null);
    }

    public void setAdapter(SpinnerAdapter adapter, boolean allSelected){
        setAdapter(adapter, allSelected, null);
    }

    public void setAdapter(SpinnerAdapter adapter, boolean allSelected, MultiSpinnerListener listener) {
        SpinnerAdapter oldAdapter = this.mAdapter;

        setOnClickListener(null);

        this.mAdapter = adapter;
        this.mListener = listener;
        this.mAllSelected = allSelected;

        if (oldAdapter != null) {
            oldAdapter.unregisterDataSetObserver(dataSetObserver);
        }

        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(dataSetObserver);

            // all selected by default
            mOldSelection = new boolean[mAdapter.getCount()];
            mSelected = new boolean[mAdapter.getCount()];
            for (int i = 0; i < mSelected.length; i++) {
                mOldSelection[i] = false;
                mSelected[i] = allSelected;
            }

            setOnClickListener(onClickListener);
        }

        // all text on the spinner
        if (mAllText != null)
            setText(mAllText);
        else
            refreshSpinner();
    }

    public void setOnItemsSelectedListener(MultiSpinnerListener listener) {
        this.mListener = listener;
    }

    public void setSelectedItems(List<?> items) {
        boolean[] selected = new boolean[mAdapter.getCount()];
        Arrays.fill(selected, false);
        for(int i = 0 ; i < mAdapter.getCount(); i++) {
            for (Object item : items) {
                if(mAdapter.getItem(i).equals(item))
                    selected[i] = true;
            }
        }
        setSelected(selected);
    }

    public List<?> getSelectedItems() {
        List<Object> result = new ArrayList<>();
        for(int i=0, s=0; i < mSelected.length; i++)
            if(mSelected[i])
                result.add(mAdapter.getItem(i));
        return result;
    }


    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected);
    }


    public int getSelectedCount() {
        int count = 0;
        for (boolean selected : mSelected) {
            if (selected)
                count++;
        }
        return count;
    }

    public boolean[] getSelected() {
        return this.mSelected;
    }

    @Deprecated
    public int[] getSelectedIndices() {
        int[] idx = new int[getSelectedCount()];
        for(int i=0, s=0; i < mSelected.length; i++)
            if(mSelected[i])
                idx[s++] = i;
        return idx;
    }

    @Deprecated
    public void setSelected(boolean[] selected) {
        if (this.mSelected.length != selected.length)
            return;

        this.mSelected = selected;

        refreshSpinner();
    }

    private void refreshSpinner() {
        if(mAdapter == null)
            return;
        // refresh text on spinner
        StringBuilder spinnerBuffer = new StringBuilder();
        boolean someUnselected = false;
        boolean allUnselected = true;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            if (mSelected[i]) {
                allUnselected = false;
                spinnerBuffer.append(mPrependCheckMark ? "✓ " : "")
                        .append(mAdapter.getItem(i).toString())
                        .append(mSeparator);
            } else {
                someUnselected = true;
            }
        }

        String spinnerText;

        if (!allUnselected) {
            if (someUnselected || mAllText == null) {
                spinnerText = spinnerBuffer.toString();
                if (spinnerText.length() > mSeparator.length())
                    spinnerText = spinnerText.substring(0, spinnerText.length() - mSeparator.length());
            } else {
                spinnerText = mAllText;
            }
        } else {
            spinnerText = mDefaultText;
        }

        setText(spinnerText);
    }

    public String getDefaultText() {
        return mDefaultText;
    }

    public void setDefaultText(String defaultText) {
        this.mDefaultText = defaultText;
        refreshSpinner();
    }

    public String getAllText() {
        return mAllText;
    }

    public void setAllText(String allText) {
        this.mAllText = allText;
        refreshSpinner();
    }

    public void prependCheckMark(boolean prependCheckMark){
        mPrependCheckMark = prependCheckMark;
        refreshSpinner();
    }

    public void setSeparator(String separator) {
        mSeparator = separator;
        refreshSpinner();
    }
}