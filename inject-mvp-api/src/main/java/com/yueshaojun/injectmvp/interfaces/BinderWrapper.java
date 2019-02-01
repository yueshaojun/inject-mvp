package com.yueshaojun.injectmvp.interfaces;


/**
 * wrap target
 * @author yueshaojun
 * @date 2018/5/29
 */

public interface BinderWrapper<T> {
    /**
     * bind
     * @param m
     */
    void bindMember(T m);

    /**
     * unbind
     * @param m
     */
    void unbind(T m);
}
