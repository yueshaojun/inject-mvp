package com.example.yueshaojun.myapplication;

import com.example.yueshaojun.myapplication.V.BaseView;

/**
 * Created by yueshaojun on 2018/4/11.
 */

public interface IApayView extends BaseView {
    void onPaySuccess();
    void onPayError(String errorMsg);
}
