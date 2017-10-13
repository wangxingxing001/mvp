package com.bigstar.mvp.mvp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.bigstar.mvp.base.BasePresenter;
import com.bigstar.mvp.callback.StringDialogCallback;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

/*
 * 我是Prensenter层,我的任务则是去做一些耗时操纵,网络请求等
 * 然后把结果返回给View
 */
public class MainPresenter extends BasePresenter<MainView>{

    private Context mContext;
    private String TAG;
    public MainPresenter(MainView mainView,Context context,String tag){
        // 既然继承了BasePresenter,把当前的viwe填入进来
        attachView(mainView);
        this.mContext = context;
        this.TAG = tag;
    }

    // 进行网络请求
    public void getData(){
        OkGo.<String>post("http://test.uhainiu.com/api/newslist")//
                .tag(this)//
                .headers("header1", "headerValue1")// 请求头参数
//                .params("cityId", "101310222")// 参数
                .execute(new StringDialogCallback((Activity) mContext) {
                    @Override
                    public void onSuccess(Response<String> response) {
                        // 判断view,以免内存泄露
                        if (mvpView!=null) {
                            mvpView.getDataModel(response);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        // 判断view,以免内存泄露
                        if (mvpView!=null) {
                            mvpView.getDataFail(response.message());
                        }
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        // 如果关闭了View,那就关闭网络请求,以免内存泄露
        OkGo.getInstance().cancelTag(TAG);
    }
}
