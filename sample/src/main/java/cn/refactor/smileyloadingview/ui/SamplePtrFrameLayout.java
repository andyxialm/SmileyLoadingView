package cn.refactor.smileyloadingview.ui;

import android.content.Context;
import android.util.AttributeSet;

import cn.refactor.smileyloadingview.header.SmileyLoadingViewHeader;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Create by andy (https://github.com/andyxialm)
 * Create time: 16/8/24 10:11
 * Description : Sample: PtrFrameLayout with SmileyLoadingView
 */
public class SamplePtrFrameLayout extends PtrFrameLayout {
    public SamplePtrFrameLayout(Context context) {
        this(context, null);
    }

    public SamplePtrFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SamplePtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupViews();
    }

    private void setupViews() {
        SmileyLoadingViewHeader mHeaderView = new SmileyLoadingViewHeader(getContext());
        setResistance(1.7f);
        setLoadingMinTime(1000);
        setDurationToCloseHeader(500);
        setRatioOfHeaderHeightToRefresh(1.0f);
        setKeepHeaderWhenRefresh(true);
        setHeaderView(mHeaderView);
        addPtrUIHandler(mHeaderView);
    }
}
