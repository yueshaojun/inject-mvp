package com.example.yueshaojun.myapplication.P;
import com.example.yueshaojun.myapplication.V.BaseView;

import java.lang.ref.WeakReference;


public class BasePresenter<V extends BaseView>{

    private WeakReference<V> viewRef;

    public void attachView(V view) {
        viewRef = new WeakReference<>(view);
    }


    public void detachView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }

    protected V getView() {
        return viewRef == null ? null : viewRef.get();
    }
}
