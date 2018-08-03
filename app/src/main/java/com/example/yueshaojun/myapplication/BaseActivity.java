package com.example.yueshaojun.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.dada.presenter.PresentersBinder;

/**
 * Created by yueshaojun on 2018/8/1.
 */

public abstract class BaseActivity extends Activity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // dagger 生成实例
        injectMethod();
        // 自动attach
        PresentersBinder.create().inject(this);
    }
    public abstract void injectMethod();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PresentersBinder.create().unbind(this);
    }
}
