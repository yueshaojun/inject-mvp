package com.example.yueshaojun.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.yueshaojun.myapplication.di.DaggerMyComponent;


/**
 * activity  基类
 * @author yueshaojun
 * @date 2018/8/1
 */

public class BaseActivity extends Activity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 使用先实例对象
        injectMethod();
        super.onCreate(savedInstanceState);
        Log.i("injectMvp",getClass().getName());
        // dagger 生成实例
        DaggerMyComponent.create().inject(this);

        // 用法1：自动attach
//        ((ActivitySupporter)getApplication()).getActivitySupport().bind(this);

    }
    public  void injectMethod(){}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 用法1：自动detach
//        ((ActivitySupporter)getApplication()).getActivitySupport().unbind(this);
    }
}
