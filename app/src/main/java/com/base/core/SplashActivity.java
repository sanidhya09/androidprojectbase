package com.base.core;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;

import com.base.R;

public class SplashActivity extends Activity {

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (!isTaskRoot()) {
            // Android launched another instance of the root activity into an existing task
            //  so just quietly finish and go away, dropping the user back into the activity
            //  at the top of the stack (ie: the last state of this task)
            finish();
            return;
        }
        loadApp();
    }

    private void loadApp() {
        findViewById(R.id.splash_text_line_1).startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_anim_text_line_1));
        findViewById(R.id.splash_text_line_2).startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_anim_text_line_2));
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                finish();
            }
        };

        handler.postDelayed(runnable, 4000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null)
            handler.removeCallbacks(runnable);
    }
}

