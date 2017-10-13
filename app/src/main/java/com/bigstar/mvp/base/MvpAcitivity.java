package com.bigstar.mvp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bigstar.mvp.mvp.MainView;

/*
 * 我是大星
 */
public abstract class MvpAcitivity<V extends MainView,P extends BasePresenter> extends BaseActivity {

    protected P mvpPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 创建Presenter对象
        mvpPresenter = setPresenter();
        // 让view与prensenter关联
        mvpPresenter.attachView((V)this);
    }
    // 构建抽象对象,不实现,实现方实现
    protected abstract P setPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 如果界面已经destroy,那么需要关闭流,以免内存溢出
        // 关闭了view回调,还需要关闭prensenter
        mvpPresenter.detachView();
    }

    @Override
    public void toastShow(String msg) {
        super.toastShow(msg);
    }
}
