package com.bigstar.mvp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;


/*
 * 我是大星
 */
public abstract class MvpAcitivity<P extends BasePresenter> extends BaseActivity {

    protected P mvpPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 创建Presenter对象
        mvpPresenter = setPresenter();
    }
    // 构建抽象对象,不实现,实现方实现
    protected abstract P setPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 如果界面已经destroy,那么需要关闭流,以免内存溢出
        if (mvpPresenter != null){
            mvpPresenter.detachView();
        }
    }

    @Override
    public void toastShow(String msg) {
        super.toastShow(msg);
    }
}
