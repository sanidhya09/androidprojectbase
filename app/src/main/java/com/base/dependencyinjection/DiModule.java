package com.base.dependencyinjection;


import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import xicom.com.baselibrary.UtilityClass;

/**
 * Created by sanidhya on 12/10/15.
 */
@Module
public class DiModule {
    @Provides
    @Singleton
    UtilityClass provideUtilitySingleton() {
        return new UtilityClass();
    }

}
