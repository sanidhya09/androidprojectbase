package com.base.core;

import android.app.Application;

import com.base.dependencyinjection.DaggerDiComponent;
import com.base.dependencyinjection.DiComponent;
import com.base.dependencyinjection.DiModule;
import com.base.network.RestService;

import okhttp3.Headers;
import xicom.com.baselibrary.retrofit2.RetroFitSingleton;
import xicom.com.baselibrary.retrofit2.RetrofitConfigModel;

/**
 * Created by sanidhya on 3/5/16.
 */
public class AppApplication extends Application {
    public DiComponent diComponent;
    public static final String API_URL = "https://api.github.com";
    public static final String API_URL2 = "https://pubs.usgs.gov/";

    @Override
    public void onCreate() {
        super.onCreate();
        diComponent = DaggerDiComponent.builder().diModule(new DiModule()).build();
        diComponent.inject(this);

        Headers.Builder builder = new Headers.Builder();
        builder.add("OS", "ANDROID");

        RetrofitConfigModel retrofitConfigModel = new RetrofitConfigModel.Builder()
                .setBaseUrl(API_URL)
                .setConnectOutTime(60)
                .setReadOutTime(45)
                .setLoggingEnabled(true)
                .setHeaders(builder)
                .build();

        RetroFitSingleton.INSTANCE.setRetrofitConfig(retrofitConfigModel);

    }

    public RestService getRestService() {
        return RetroFitSingleton.INSTANCE.getRetrofit().create(RestService.class);
    }


}
