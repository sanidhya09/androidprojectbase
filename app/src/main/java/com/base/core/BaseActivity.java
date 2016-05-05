package com.base.core;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.base.utils.UtilitySingleton;

import javax.inject.Inject;

public class BaseActivity extends AppCompatActivity {
    public Context context;
    @Inject
    public UtilitySingleton utilitySingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        AppApplication appApplication = (AppApplication) getApplication();
        appApplication.diComponent.inject(this);
        utilitySingleton.setContext(this);
    }
}
