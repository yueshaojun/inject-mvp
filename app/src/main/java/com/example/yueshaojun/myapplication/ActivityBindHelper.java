package com.example.yueshaojun.myapplication;

import android.app.Activity;

import com.dada.presenter.ActivityBinder;
import com.dada.presenter.ActivitySupporter;
import com.example.lib.AndroidBinder;

/**
 * Created by yueshaojun on 2018/8/9.
 */

public class ActivityBindHelper implements AndroidBinder<Activity> {


    @Override
    public void bind(Activity instance) {
        ((ActivitySupporter)instance.getApplication()).getActivitySupport().bind(instance);
    }

    @Override
    public void unbind(Activity instance) {

    }
}
