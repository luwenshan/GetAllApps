package com.lws.allapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final int FLAG_SIMPLE_LAUNCHER = 0;
    public static final int FLAG_ALL_APP = 1;
    public static final int FLAG_SYSTEM_APP = 2;
    public static final int FLAG_THIRD_APP = 3;
    public static final int FLAG_SDCARD_APP = 4;
    @BindView(R.id.btn_simple_launcher)
    Button mBtnSimpleLauncher;
    @BindView(R.id.btn_all_app)
    Button mBtnAllApp;
    @BindView(R.id.btn_system_app)
    Button mBtnSystemApp;
    @BindView(R.id.btn_third_app)
    Button mBtnThirdApp;
    @BindView(R.id.btn_sdcard_app)
    Button mBtnSdcardApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_simple_launcher, R.id.btn_all_app, R.id.btn_system_app, R.id.btn_third_app, R.id.btn_sdcard_app})
    public void onViewClicked(View view) {
        Intent intent = new Intent(MainActivity.this, AppListActivity.class);
        switch (view.getId()) {
            case R.id.btn_simple_launcher:
                intent.putExtra("flag", 0);
                break;
            case R.id.btn_all_app:
                intent.putExtra("flag", 1);
                break;
            case R.id.btn_system_app:
                intent.putExtra("flag", 2);
                break;
            case R.id.btn_third_app:
                intent.putExtra("flag", 3);
                break;
            case R.id.btn_sdcard_app:
                intent.putExtra("flag", 4);
                break;
        }
        startActivity(intent);
    }
}
