package com.bigstar.mvp.base;


import com.bigstar.mvp.mvp.MainView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/*
 * 我是大星
 */
public class BasePresenter<V extends MainView>  {

    //view接口类型的弱引用
    protected WeakReference<V> mViewRef;

    // 把当前的View 赋值,指定你正在使用的当前View
    public void attachView(V view){
        //建立关联
        mViewRef = new WeakReference<V>(view);
    }

    // 获取当前view
    protected V getView(){
        return  mViewRef.get();
    }

    // 当前view是否存在
    public boolean isViewAttached(){
        return  mViewRef !=null && mViewRef.get() !=null;
    }

    // 把当前的view置空,利于回收
    public void detachView() {
        if(mViewRef != null){
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
