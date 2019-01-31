package com.example.yueshaojun.myapplication;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.yueshaojun.injectmvp.ActivityBinder;
import com.yueshaojun.injectmvp.ActivitySupporter;
import com.yueshaojun.injectmvp.AndroidBinder;
import com.yueshaojun.injectmvp.FragmentBinder;
import com.yueshaojun.injectmvp.FragmentSupporter;


/**
 * Created by yueshaojun on 2018/5/22.
 */

public class MyApplication extends Application
        implements ActivitySupporter,FragmentSupporter {
    AndroidBinder<Activity> activityAndroidBinder = ActivityBinder.create();
    AndroidBinder<Fragment> fragmentAndroidBinder = FragmentBinder.create();
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                // 用法2
                activityAndroidBinder.bind(activity);
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
                // 用法2
                activityAndroidBinder.unbind(activity);
            }
        });
    }

    @Override
    public AndroidBinder<Activity> getActivitySupport() {
        return activityAndroidBinder;
    }

    @Override
    public AndroidBinder<Fragment> getFragmentSupport() {
        return fragmentAndroidBinder;
    }
}
