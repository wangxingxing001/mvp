package com.bigstar.mvp.base;


/*
 * 我是大星
 */
public class BasePresenter<V> {

    protected V mvpView;
    // 把当前的View 赋值,指定你正在使用的当前View
    public void attachView(V v){
        this.mvpView = v;
    }

    // 把当前的view置空,利于回收
    public void detachView() {
        this.mvpView = null;
    }
}
