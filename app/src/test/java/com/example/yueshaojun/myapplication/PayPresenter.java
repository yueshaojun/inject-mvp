package com.example.yueshaojun.myapplication;

import android.util.Log;

import com.example.yueshaojun.myapplication.P.BasePresenter;


/**
 * Created by yueshaojun on 2018/4/8.
 */

public class PayPresenter extends BasePresenter<IApayView> {
    private static final String TAG = "PayPresenter";

    public void pay() {
        Log.i("MyModule",TAG+" http request ...");
        getView().onPaySuccess();
    }
}
