package com.base.dependencyinjection;


import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import xicom.com.baselibrary.retrofit2.RetroFitUtil;
import xicom.com.baselibrary.UtilitySingleton;

/**
 * Created by sanidhya on 12/10/15.
 */
@Module
public class DiModule {
    @Provides
    @Singleton
    UtilitySingleton provideUtilitySingleton() {
        return new UtilitySingleton();
    }

    @Provides
    @Singleton
    RetroFitUtil provideRetroFitUtil() {
        return new RetroFitUtil("https://api.github.com");
    }
}
