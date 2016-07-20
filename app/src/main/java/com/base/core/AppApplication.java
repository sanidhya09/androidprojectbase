package com.base.core;

import android.app.Application;

import com.base.dependencyinjection.DaggerDiComponent;
import com.base.dependencyinjection.DiComponent;
import com.base.dependencyinjection.DiModule;
import com.base.network.RestService;

import javax.inject.Inject;

import xicom.com.baselibrary.RetroFitUtil;

/**
 * Created by sanidhya on 3/5/16.
 */
public class AppApplication extends Application {
    public DiComponent diComponent;
    @Inject
    RetroFitUtil retroFitUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        diComponent = DaggerDiComponent.builder().diModule(new DiModule()).build();
        diComponent.inject(this);
    }

    public RestService getRestService() {
        return retroFitUtil.getRetrofit().create(RestService.class);
    }
}
