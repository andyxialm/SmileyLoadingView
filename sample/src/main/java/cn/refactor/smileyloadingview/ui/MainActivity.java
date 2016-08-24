package cn.refactor.smileyloadingview.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.refactor.smileyloadingview.R;

/**
 * Create by andy (https://github.com/andyxialm)
 * Create time: 16/8/24 10:30
 * Description : Sample: SmileyLoadingView in pull to refresh
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gotoSample(View v) {
        startActivity(new Intent(this, SampleActivity.class));
    }

    public void gotoPullToRefresh(View v) {
        startActivity(new Intent(this, PullToRefreshActivity.class));
    }
}
