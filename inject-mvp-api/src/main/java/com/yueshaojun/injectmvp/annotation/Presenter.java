package com.yueshaojun.injectmvp.annotation;

import com.yueshaojun.injectmvp.PresenterType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yueshaojun on 2018/4/8.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface Presenter {
    PresenterType type();
}
