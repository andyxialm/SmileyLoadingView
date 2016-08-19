package cn.refactor.smileyloadingview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import cn.refactor.smileyloadingview.lib.SmileyLoadingView;

public class MainActivity extends AppCompatActivity {

    private SmileyLoadingView mSmileyLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSmileyLoadingView = (SmileyLoadingView) findViewById(R.id.loading_view);
        mSmileyLoadingView.setOnStatusChangedListener(new SmileyLoadingView.OnStatusChangedListener() {
            @Override
            public void onCompleted() {
                mSmileyLoadingView.setVisibility(View.INVISIBLE);
            }
        });

        SeekBar seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setMax(0xFF);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSmileyLoadingView.setAlpha(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void showView(View v) {
        mSmileyLoadingView.setVisibility(View.VISIBLE);
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
