package cn.refactor.smileyloadingview.header;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import cn.refactor.smileyloadingview.R;
import cn.refactor.smileyloadingview.lib.SmileyLoadingView;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Create by andy (https://github.com/andyxialm)
 * Create time: 16/8/22 10:37
 * Description : SmileyLoadingViewHeader
 */
public class SmileyLoadingViewHeader extends FrameLayout implements PtrUIHandler {

    private SmileyLoadingView mLoadingView;

    public SmileyLoadingViewHeader(Context context) {
        this(context, null);
    }

    public SmileyLoadingViewHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmileyLoadingViewHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SmileyLoadingViewHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View contentView = View.inflate(getContext(), R.layout.header_loading, null);
        mLoadingView = (SmileyLoadingView) contentView.findViewById(R.id.loading_view);
        addView(contentView);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        mLoadingView.setVisibility(VISIBLE);
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        mLoadingView.setVisibility(VISIBLE);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mLoadingView.start();
    }

    @Override
    public void onUIRefreshComplete(final PtrFrameLayout frame) {
        mLoadingView.stop();
        mLoadingView.setOnAnimPerformCompletedListener(new SmileyLoadingView.OnAnimPerformCompletedListener() {
            @Override
            public void onCompleted() {
                mLoadingView.setPaintAlpha(0x0);
            }
        });
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        mLoadingView.setPaintAlpha(ptrIndicator.getCurrentPercent() >= 1 ? 0xFF : (int) (ptrIndicator.getCurrentPercent() * 0xFF));
    }

    private void executeLoadingViewAnim() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        alphaAnimation.setDuration(150);
        alphaAnimation.setFillAfter(true);
        mLoadingView.startAnimation(alphaAnimation);
    }

}
