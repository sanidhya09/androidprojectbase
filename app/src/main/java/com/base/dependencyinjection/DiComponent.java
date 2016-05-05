package com.base.dependencyinjection;

import com.base.core.BaseActivity;
import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by sanidhya on 12/10/15.
 */
@Singleton
@Component(modules = DiModule.class)
public interface DiComponent {
    void inject(BaseActivity activity);
}