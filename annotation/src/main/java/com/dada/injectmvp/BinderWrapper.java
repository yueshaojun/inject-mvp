package com.dada.injectmvp;


/**
 * Created by yueshaojun on 2018/5/29.
 */

public interface BinderWrapper<T> {
    void bindMember(T m);
    void unbind(T m);
}
