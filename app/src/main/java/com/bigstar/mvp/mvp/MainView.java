package com.bigstar.mvp.mvp;
import com.lzy.okgo.model.Response;


/*
 * 我是大星
 */
/*
 * 我是View层,我是消息提供者,Presenter有任何结过通知到我,我都要把消息给activity
 */
public interface MainView {

    // 拿到数据内容,通过这个View,然后返回到Acitivty
    void getDataModel(Response response);
    // 请求失败的内容
    void getDataFail(String msg);
}
