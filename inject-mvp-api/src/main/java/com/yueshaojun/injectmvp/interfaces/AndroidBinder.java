package com.yueshaojun.injectmvp.interfaces;



/**
 * Created by yueshaojun on 2018/5/28.
 */
public interface AndroidBinder<T> {
    void bind(T instance);
    void unbind(T instance);
}
