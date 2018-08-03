package com.example.yueshaojun.myapplication.di;

import com.example.yueshaojun.myapplication.activity.ActivityLogin;
import com.example.yueshaojun.myapplication.activity.ActivityPay;

import dagger.Component;

/**
 * Created by yueshaojun on 2018/4/7.
 */
@Component()
public interface MyComponent {
    void inject(ActivityPay activityPay);
    void inject(ActivityLogin activityLogin);
}
