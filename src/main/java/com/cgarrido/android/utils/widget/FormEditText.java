package com.cgarrido.android.utils.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.andreabaccega.widget.DefaultEditTextValidator;


/**
 * Created by cristian on 29/07/16.
 */
public class FormEditText extends com.andreabaccega.widget.FormEditText implements View.OnFocusChangeListener {
    private boolean mCheckValidityOnLostFocus = true;

    public FormEditText(Context context) {
        super(context);
        setOnFocusChangeListener(this);
    }

    public FormEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnFocusChangeListener(this);
    }

    public FormEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnFocusChangeListener(this);
    }

    public void setTestErrorString(String msg){
        ((DefaultEditTextValidator)getEditTextValidator()).setTestErrorString(msg, getContext());
    }

    public static boolean testValidity(FormEditText... formEditTexts) {
        boolean formValid = true;
        boolean alreadyFocused = false;
        for (FormEditText edit : formEditTexts) {
            formValid = edit.testValidity() && formValid;
            if(!formValid && !alreadyFocused) {
                edit.requestFocus();
                alreadyFocused = true;
            }
        }
        return formValid;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(!hasFocus && mCheckValidityOnLostFocus)
            testValidity();
    }

    public void setCheckValidityOnLostFocus(boolean activate) {
        this.mCheckValidityOnLostFocus = activate;
    }
}
