package cn.refactor.smileyloadingview.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import cn.refactor.smileyloadingview.R;
import cn.refactor.smileyloadingview.header.SmileyLoadingViewHeader;
import cn.refactor.smileyloadingview.lib.SmileyLoadingView;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class MainActivity extends AppCompatActivity {

    private SmileyLoadingView mSmileyLoadingView;
    private PtrFrameLayout mPtrFrameLayout;

    private SmileyLoadingViewHeader mHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.ptr_frame_layout);
        mHeaderView = new SmileyLoadingViewHeader(this);
        mPtrFrameLayout.setHeaderView(mHeaderView);
        mPtrFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrameLayout.addPtrUIHandler(mHeaderView);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh();
            }
        });
    }

    private void refresh() {
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHeaderView.onUIRefreshComplete(mPtrFrameLayout);
                mPtrFrameLayout.refreshComplete();
            }
        }, 3000);
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
