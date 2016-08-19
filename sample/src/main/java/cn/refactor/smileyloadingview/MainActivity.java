package cn.refactor.smileyloadingview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.refactor.smileyloadingview.lib.SmileyLoadingView;

public class MainActivity extends AppCompatActivity {

    private SmileyLoadingView mSmileyLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSmileyLoadingView = (SmileyLoadingView) findViewById(R.id.loading_view);
    }

    public void start(View v) {
        mSmileyLoadingView.start();
    }

    public void stop(View v) {
        mSmileyLoadingView.stop(false);
    }

    public void stopUtilAnimationCompleted(View v) {
        mSmileyLoadingView.stop();
    }
}
