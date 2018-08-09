package com.example.yueshaojun.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import com.dada.presenter.ActivitySupporter;


/**
 * activity  基类
 * @author yueshaojun
 * @date 2018/8/1
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
        ((ActivitySupporter)getApplication()).getActivitySupport().bind(this);
    }
    public abstract void injectMethod();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((ActivitySupporter)getApplication()).getActivitySupport().unbind(this);
    }
}
