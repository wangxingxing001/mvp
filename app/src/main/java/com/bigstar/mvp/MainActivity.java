package com.bigstar.mvp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigstar.mvp.base.MvpAcitivity;
import com.bigstar.mvp.mvp.MainPresenter;
import com.bigstar.mvp.mvp.MainView;
import com.lzy.okgo.model.Response;

import butterknife.Bind;
import butterknife.ButterKnife;


/*
 * 我是大星
 */

/*
 * Activity不需要做任何事情,只需要指定当前的View,然后让Presenter工作,然后通过View层给返回
 * 举个列子
 * activity是个老板,他要出差,让Presenter去给他订票,然后Presenter去执行任务,但是不管什么结果
 * 都需要View小秘书给汇报到activity这块,然后activity拿到并打开任务即Ui
 */
public class MainActivity extends MvpAcitivity<MainView,MainPresenter> implements MainView {

    private String TAG = getClass().getName();

    @Bind(R.id.but_get_data)
    Button butGetData;
    @Bind(R.id.http_head)
    TextView httpHead;
    @Bind(R.id.http_time)
    TextView httpTime;
    @Bind(R.id.http_data)
    TextView httpData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 请求数据
        findViewById(R.id.but_get_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 开始请求数据,并且展示
                mvpPresenter.getData();
            }
        });
    }

    // 在此对Prenseter 实用类进行new,并且指定该View
    @Override
    protected MainPresenter setPresenter() {
        return new MainPresenter(this,TAG);
    }

    // 这里是请求成功,返回的对象
    @Override
    public void getDataModel(Response response) {
        httpHead.setText("请求状态码: \n"+response.code());
        httpTime.setText("请求是否成功: \n"+response.message());
        httpData.setText("请求到的数据: \n"+response.body());
    }

    // 这里是请求失败,返回的内容
    @Override
    public void getDataFail(String msg) {
        toastShow(msg);
    }
}
