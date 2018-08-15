package com.example.yueshaojun.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dada.injectmvp.Presenter;
import com.dada.injectmvp.PresenterType;
import com.dada.presenter.ActivitySupporter;
import com.example.yueshaojun.myapplication.P.PayPresenter;
import com.example.yueshaojun.myapplication.V.IPayView;
import com.example.yueshaojun.myapplication.di.DaggerMyComponent;

import javax.inject.Inject;


/**
 * activity  基类
 * @author yueshaojun
 * @date 2018/8/1
 */

public class BaseActivity extends Activity implements IPayView{
    @Inject
    @Presenter(type = PresenterType.ACTIVITY)
    PayPresenter payPresenter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("injectMvp",getClass().getName());
        // dagger 生成实例
        injectMethod();
        DaggerMyComponent.create().inject(this);

        // 自动attach
        ((ActivitySupporter)getApplication()).getActivitySupport().bind(this);

        payPresenter.pay();
    }
    public  void injectMethod(){};

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((ActivitySupporter)getApplication()).getActivitySupport().unbind(this);
    }

    @Override
    public void paySuccess() {

    }
}
