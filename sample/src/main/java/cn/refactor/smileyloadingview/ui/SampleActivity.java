package cn.refactor.smileyloadingview.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import cn.refactor.smileyloadingview.R;
import cn.refactor.smileyloadingview.lib.SmileyLoadingView;

/**
 * Create by andy (https://github.com/andyxialm)
 * Create time: 16/8/24 10:34
 * Description : Sample: SmileyLoadingView in pull to refresh
 */
public class SampleActivity extends AppCompatActivity {

    private SmileyLoadingView mSmileyLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        mSmileyLoadingView = (SmileyLoadingView) findViewById(R.id.loading_view);
        mSmileyLoadingView.setOnAnimPerformCompletedListener(new SmileyLoadingView.OnAnimPerformCompletedListener() {
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
                mSmileyLoadingView.setPaintAlpha(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setProgress(0xFF);

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
