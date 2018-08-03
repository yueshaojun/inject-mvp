package com.example.yueshaojun.myapplication.P;

import android.util.Log;

import com.example.yueshaojun.myapplication.V.ILoginView;

import javax.inject.Inject;

/**
 * Created by yueshaojun on 2018/4/8.
 */

public class LoginPresenter extends BasePresenter<ILoginView> {
    private static final String TAG = "LoginPresenter";
    @Inject
    public LoginPresenter() {

    }

    public void login() {
        Log.i("MyModule",TAG+" http request ...");
        getView().onLoginSuccess();
    }
}
