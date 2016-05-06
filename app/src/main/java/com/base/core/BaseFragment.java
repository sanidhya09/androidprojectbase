package com.base.core;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.base.utils.UtilitySingleton;

import javax.inject.Inject;

/**
 * Created by sanidhya on 6/5/16.
 */
public class BaseFragment extends Fragment {
    public Activity context;
    @Inject
    public UtilitySingleton utilitySingleton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = getActivity();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppApplication appApplication = (AppApplication) context.getApplication();
        appApplication.diComponent.inject(this);
        utilitySingleton.setContext(context);
    }
}
