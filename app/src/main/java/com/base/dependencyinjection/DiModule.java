package com.base.dependencyinjection;

import com.base.utils.UtilitySingleton;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

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

}
