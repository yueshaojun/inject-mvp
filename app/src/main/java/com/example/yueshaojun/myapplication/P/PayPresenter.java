package com.example.yueshaojun.myapplication.P;

import com.example.yueshaojun.myapplication.V.IPayView;

import javax.inject.Inject;

/**
 * Created by yueshaojun on 2018/5/29.
 */

public class PayPresenter extends BasePresenter<IPayView> {
    @Inject
    public PayPresenter(){

    }
    public void pay(){
        getView().paySuccess();
    }
}
