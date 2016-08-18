/**
 *  * Copyright 2016 andy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.refactor.smileyloadingview.lib;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Create by andy (https://github.com/andyxialm)
 * Create time: 16/8/18 10:17
 * Description : SmileyLoadingView
 */
public class SmileyLoadingView extends View {

    private static final int DEFAULT_PAINT_WIDTH = 10;

    private Path  mCirclePath;
    private Paint mPaint;
    private Point mCenterPoint;

    private int mStrokeWidth;

    private ValueAnimator mValueAnimator;

    public SmileyLoadingView(Context context) {
        this(context, null);
    }

    public SmileyLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmileyLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SmileyLoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mCirclePath = new Path();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(dp2px(DEFAULT_PAINT_WIDTH));
        mPaint.setColor(getResources().getColor(android.R.color.holo_blue_dark));

        mCenterPoint = new Point();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mCirclePath, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int width = getWidth();
        int height = getHeight();
        mCenterPoint.x = (width - paddingRight + paddingLeft) >> 1;
        mCenterPoint.y = (height - paddingBottom + paddingTop) >> 1;

        //float radiusX = (width - paddingRight - paddingLeft - dp2px(mStrokeWidth)) >> 1;
        //float radiusY = (height - paddingTop - paddingBottom - dp2px(mStrokeWidth)) >> 1;
        //float radius = Math.min(radiusX, radiusY);

        RectF rectF = new RectF(paddingLeft + dp2px(mStrokeWidth), paddingTop + dp2px(mStrokeWidth),
                                width - dp2px(mStrokeWidth) - paddingRight, height - dp2px(mStrokeWidth) - paddingBottom);
        mCirclePath.arcTo(rectF, 0, 180);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // destory
    }

    /**
     * Start animation
     */
    public void start() {
        mValueAnimator = ValueAnimator.ofFloat(1.0f, 0f);
        mValueAnimator.setDuration(10000);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
            }
        });
        mValueAnimator.start();
    }

    /**
     * Stop animation
     */
    public void stop() {
        stop(true);
    }

    /**
     * @param stopUntilAnimationPerformCompleted 是否最后一次动画执行完毕后 才结束。
     */
    public void stop(boolean stopUntilAnimationPerformCompleted) {
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.end();
            invalidate();
        }
    }

    /**
     * dp to px
     * @param dpValue dp
     * @return px
     */
    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
