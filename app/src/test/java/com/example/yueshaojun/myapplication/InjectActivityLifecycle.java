package com.example.yueshaojun.myapplication;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;


/**
 * Created by yueshaojun on 2018/4/13.
 */

public class InjectActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.d("MyApplication","MVP "+activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
