package com.example.yueshaojun.myapplication.di;

import com.example.lib.MVPComponent;

/**
 * Created by yueshaojun on 2018/5/28.
 */
@MVPComponent
public interface IInjector<T> {
    void inject(T instance);
    void unbind(T instance);
}
