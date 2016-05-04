package com.base.core;

import android.app.Application;

import com.base.network.RestService;
import com.base.utils.FitFixConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sanidhya on 3/5/16.
 */
public class FitFixApplication extends Application {
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Retrofit getRetrofit() {
        return (retrofit == null) ? setRetrofit() : retrofit;
    }

    public Retrofit setRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        //.header("Authorization", "auth-value")
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        }).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(FitFixConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public RestService getRestService() {
        if (retrofit == null) {
            return getRetrofit().create(RestService.class);
        } else {
            return retrofit.create(RestService.class);
        }

    }
}
