/**
 * Copyright 2016 andy
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

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
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

    private static final int DEFAULT_PAINT_WIDTH = 5;
    private static final int DEFAULT_PAINT_COLOR = Color.parseColor("#b3d8f3");

    private Paint mArcPaint, mCirclePaint;
    private Path  mCirclePath, mArcPath;
    private PathMeasure mPathMeasure;

    private RectF mRectF;
    private float[] mCenterPos, mLeftEyePos, mRightEyePos;
    private float mStartAngle, mSwipeAngle;

    private int mStrokeWidth;
    private boolean mRunning;
    private boolean mFirstStep;

    private boolean mShowLeftEye, mShowRightEye;
    private boolean mStopUntilAnimationPerformCompleted;

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

        // // TODO: 16/8/19 默认值
        mStrokeWidth = DEFAULT_PAINT_WIDTH;


        mSwipeAngle = 180; // 初始状态
        mCirclePath = new Path();
        mArcPath = new Path();

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcPaint.setStrokeJoin(Paint.Join.ROUND);
        mArcPaint.setStrokeWidth(dp2px(DEFAULT_PAINT_WIDTH));
        mArcPaint.setColor(DEFAULT_PAINT_COLOR);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mCirclePaint.setStrokeJoin(Paint.Join.ROUND);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(DEFAULT_PAINT_COLOR);

        mCenterPos = new float[2];
        mLeftEyePos = new float[2];
        mRightEyePos = new float[2];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mRunning) {
            if (mShowLeftEye) {
                canvas.drawCircle(mLeftEyePos[0], mLeftEyePos[1], dp2px(mStrokeWidth) / 2, mCirclePaint);
            }

            if (mShowRightEye) {
                canvas.drawCircle(mRightEyePos[0], mRightEyePos[1], dp2px(mStrokeWidth) / 2, mCirclePaint);
            }

            if (mFirstStep) {
                mPathMeasure.getPosTan(mPathMeasure.getLength() / 8 * 5, mLeftEyePos, null);
                mPathMeasure.getPosTan(mPathMeasure.getLength() / 8 * 7, mRightEyePos, null);
                mLeftEyePos[0] += mStrokeWidth / 4 * 3;
                mLeftEyePos[1] += mStrokeWidth / 4 * 3;
                mRightEyePos[0] -= mStrokeWidth / 4 * 3;
                mRightEyePos[1] += mStrokeWidth / 4 * 3;

                mArcPath.reset();
                mArcPath.addArc(mRectF, mStartAngle, mSwipeAngle);
                canvas.drawPath(mArcPath, mArcPaint);
            } else {
                mArcPath.reset();
                mArcPath.addArc(mRectF, mStartAngle, mSwipeAngle);
                canvas.drawPath(mArcPath, mArcPaint);
            }
        } else {
            canvas.drawCircle(mLeftEyePos[0], mLeftEyePos[1], dp2px(mStrokeWidth) / 2, mCirclePaint);
            canvas.drawCircle(mRightEyePos[0], mRightEyePos[1], dp2px(mStrokeWidth) / 2, mCirclePaint);

            mPathMeasure.getPosTan(mPathMeasure.getLength() / 8 * 5, mLeftEyePos, null);
            mPathMeasure.getPosTan(mPathMeasure.getLength() / 8 * 7, mRightEyePos, null);
            mLeftEyePos[0] += mStrokeWidth / 4 * 3;
            mLeftEyePos[1] += mStrokeWidth / 4 * 3;
            mRightEyePos[0] -= mStrokeWidth / 4 * 3;
            mRightEyePos[1] += mStrokeWidth / 4 * 3;

            mArcPath.reset();
            mArcPath.addArc(mRectF, mStartAngle, mSwipeAngle);
            canvas.drawPath(mArcPath, mArcPaint);
        }
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
        mCenterPos[0] = (width - paddingRight + paddingLeft) >> 1;
        mCenterPos[1] = (height - paddingBottom + paddingTop) >> 1;


        float radiusX = (width - paddingRight - paddingLeft - dp2px(mStrokeWidth)) >> 1;
        float radiusY = (height - paddingTop - paddingBottom - dp2px(mStrokeWidth)) >> 1;
        float radius = Math.min(radiusX, radiusY);

        mRectF = new RectF(paddingLeft + dp2px(mStrokeWidth), paddingTop + dp2px(mStrokeWidth),
                                width - dp2px(mStrokeWidth) - paddingRight, height - dp2px(mStrokeWidth) - paddingBottom);

        mArcPath.arcTo(mRectF, 0, 180);
        mCirclePath.addCircle(mCenterPos[0], mCenterPos[1], radius, Path.Direction.CW);
        mPathMeasure = new PathMeasure(mCirclePath, true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.end();
        }
    }

    /**
     * Start animation
     */
    public void start() {
        mFirstStep = true;

        mValueAnimator = ValueAnimator.ofFloat(0.0f, 720.0f);
        mValueAnimator.setDuration(2000);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!animation.isRunning()) {
                    return;
                }
                float animatedValue = (float) animation.getAnimatedValue();
                mFirstStep = animatedValue / 360.0f <= 1;
                animatedValue %= 360;
                if (mFirstStep) {
                    mShowLeftEye = animatedValue > 135.0f;
                    mShowRightEye = animatedValue > 235.0f;
                    // TODO: 16/8/19 需要计算角度 mSwipeAngle = 1;
                    mSwipeAngle = 1;

                    mStartAngle = (float) animation.getAnimatedValue() + 90;
                } else {
                    mShowLeftEye = animatedValue <= 135.0f;
                    mShowRightEye = animatedValue <= 235.0f;
                    mStartAngle = (float) animation.getAnimatedValue() + 90;
                    mSwipeAngle = animatedValue / 135.0f <= 1 ? animatedValue : (135.0f - (animatedValue - 135.0f) / 225 * 135);
                }
                invalidate();
            }
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRunning = false;
                reset();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mRunning = false;
                reset();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (mStopUntilAnimationPerformCompleted) {
                    animation.cancel();
                    mStopUntilAnimationPerformCompleted = false;
                }
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
        mStopUntilAnimationPerformCompleted = stopUntilAnimationPerformCompleted;
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            if (!stopUntilAnimationPerformCompleted) {
                mValueAnimator.end();
            }
        }
    }

    /**
     * reset UI
     */
    private void reset() {
        mStartAngle = 0;
        mSwipeAngle = 180;
        invalidate();
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
