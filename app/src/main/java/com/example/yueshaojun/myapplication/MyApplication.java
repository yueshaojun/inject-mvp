package com.example.yueshaojun.myapplication;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.dada.presenter.ActivityBinder;
import com.dada.presenter.ActivitySupporter;
import com.dada.presenter.FragmentBinder;
import com.dada.presenter.FragmentSupporter;
import com.example.lib.AndroidBinder;


/**
 * Created by yueshaojun on 2018/5/22.
 */

public class MyApplication extends Application implements ActivitySupporter,FragmentSupporter {
    AndroidBinder<Activity> activityAndroidBinder = ActivityBinder.create();
    AndroidBinder<Fragment> fragmentAndroidBinder = FragmentBinder.create();
    @Override
    public void onCreate() {
        super.onCreate();
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
