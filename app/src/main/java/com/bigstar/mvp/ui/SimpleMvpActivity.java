package com.bigstar.mvp.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigstar.mvp.R;
import com.bigstar.mvp.base.MvpAcitivity;
import com.bigstar.mvp.mvp.MainPresenter;
import com.bigstar.mvp.mvp.MainView;
import com.lzy.okgo.model.Response;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SimpleMvpActivity extends MvpAcitivity<MainView, MainPresenter> implements MainView {

    @Bind(R.id.base_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.simple_mvp_layout)
    RelativeLayout simpleMvpLayout;
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
        setContentView(R.layout.activity_simple_mvp);
        ButterKnife.bind(this);
        // 请求数据
        findViewById(R.id.but_get_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 开始请求数据,并且展示
                mvpPresenter.getData();
            }
        });
        mToolbar.setNavigationIcon(R.mipmap.big_star_icon);
        mToolbar.setTitle("mvp");
        mToolbar.setTitleTextColor(Color.WHITE);
        // 指定当前的toolbar,toolbar就能取代原本的acitonbar了
        if (mToolbar!=null){
            setSupportActionBar(mToolbar);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    // 在此对Prenseter 实用类进行new,并且指定该View
    @Override
    protected MainPresenter setPresenter() {
        return new MainPresenter(this, TAG);
    }

    // 这里是请求成功,返回的对象
    @Override
    public void getDataModel(Response response) {
        httpHead.setText("请求状态码: \n" + response.code());
        httpTime.setText("请求是否成功: \n" + response.message());
        httpData.setText("请求到的数据: \n" + response.body());
    }

    // 这里是请求失败,返回的内容
    @Override
    public void getDataFail(String msg) {
        toastShow(msg);
    }
}
