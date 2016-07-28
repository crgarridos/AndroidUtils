package com.cgarrido.android.utils.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cristian on 03/06/16.
 */
public class View2Fragment extends Fragment {

    private View mView;

    public static View2Fragment convert(View view) {
        View2Fragment fragment = new View2Fragment();
        fragment.mView = view;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mView;
    }
}
