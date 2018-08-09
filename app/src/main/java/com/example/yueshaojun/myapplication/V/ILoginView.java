package com.example.yueshaojun.myapplication.V;

/**
 * Created by yueshaojun on 2018/4/11.
 */

public interface ILoginView extends BaseView{
    /**
     * 登录成功
     */
    void onLoginSuccess();

    /**
     * 登录失败
     * @param errorMsg 失败信息
     */
    void onLoginError(String  errorMsg);
}
