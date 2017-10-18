package com.bigstar.mvp.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bigstar.mvp.R;
import com.bigstar.mvp.base.BaseActivity;
import com.bigstar.mvp.view.PlayButtonView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SimplePlayViewActivity extends BaseActivity {

    @Bind(R.id.base_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.but_simple_play)
    Button butSimplePlay;
    @Bind(R.id.play_positive)
    PlayButtonView playPositive;
    @Bind(R.id.play_negative)
    PlayButtonView playNegative;
    @Bind(R.id.simple_play_view_layout)
    LinearLayout simplePlayViewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_play_view);
        ButterKnife.bind(this);
        mToolbar.setNavigationIcon(R.mipmap.big_star_icon);
        mToolbar.setTitle("自定义播放View");
        mToolbar.setTitleTextColor(Color.WHITE);
        // 指定当前的toolbar,toolbar就能取代原本的acitonbar了
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.but_simple_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 顺时针
                if (playPositive.isPlaying()){
                    playPositive.pause();
                }else{
                    playPositive.play();
                }
                // 逆时针
                if (playNegative.isPlaying()){
                    playNegative.pause();
                }else{
                    playNegative.play();
                }
            }
        });

        playPositive.setPlayPauseListener(new PlayButtonView.PlayPauseListener() {
            @Override
            public void Play() {
                // 播放 do some thing...
                toastShow("Play");
            }

            @Override
            public void Pause() {
                // 暂停 do some thing...
                toastShow("Pause");
            }
        });
    }
}
