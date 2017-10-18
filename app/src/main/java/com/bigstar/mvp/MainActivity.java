package com.bigstar.mvp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bigstar.mvp.base.BaseActivity;
import com.bigstar.mvp.ui.SimpleMvpActivity;
import com.bigstar.mvp.ui.SimplePlayViewActivity;


/*
 * 我是大星
 */

/*
 * Activity不需要做任何事情,只需要指定当前的View,然后让Presenter工作,然后通过View层给返回
 * 举个列子
 * activity是个老板,他要出差,让Presenter去给他订票,然后Presenter去执行任务,但是不管什么结果
 * 都需要View小秘书给汇报到activity这块,然后activity拿到并打开任务即Ui
 */
public class MainActivity extends BaseActivity {

    private String TAG = getClass().getName();

    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.base_toolbar);

        mToolbar.setNavigationIcon(R.mipmap.perm_group_system_tools);
        mToolbar.setTitle("派大星");
        mToolbar.setTitleTextColor(Color.WHITE);
        // 指定当前的toolbar,toolbar就能取代原本的acitonbar了
        if (mToolbar!=null){
            setSupportActionBar(mToolbar);
        }
        initDrawerLayout();
        // mvp
        findViewById(R.id.but_mvp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SimpleMvpActivity.class));
            }
        });
        // 播放View
        findViewById(R.id.but_play_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SimplePlayViewActivity.class));
            }
        });

    }

    private void initDrawerLayout() {
//        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        // 创建抽屉开关,将tooblar 和 DrawerLayout 作为参数
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.drawer_open,R.string.drawer_close);
        // 这句话实现联动功能
        mToggle.syncState();
        // 设置抽屉的开关
        mDrawerLayout.addDrawerListener(mToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // 选择了那些item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
