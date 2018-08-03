package com.example.yueshaojun.myapplication.V;

/**
 * Created by yueshaojun on 2018/4/11.
 */

public interface ILoginView extends BaseView{
    void onLoginSuccess();
    void onLoginError(String  errorMsg);
}
