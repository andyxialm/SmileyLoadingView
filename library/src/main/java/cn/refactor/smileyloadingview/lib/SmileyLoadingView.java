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
import android.content.res.TypedArray;
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

    private static final int DEFAULT_ANIM_DURATION = 2000;
    private static final int DEFAULT_PAINT_WIDTH = 5;
    private static final int DEFAULT_PAINT_COLOR = Color.parseColor("#b3d8f3");

    private Paint mArcPaint, mCirclePaint;
    private Path  mCirclePath, mArcPath;
    private PathMeasure mPathMeasure;

    private RectF mRectF;
    private float[] mCenterPos, mLeftEyePos, mRightEyePos;
    private float mStartAngle, mSweepAngle;
    private float mCircleRadius, mEyeCircleRadius;

    private int mStrokeColor;
    private int mAnimDuration;

    private int mAnimRepeatCount;
    private int mStrokeWidth;
    private boolean mRunning;
    private boolean mFirstStep;

    private boolean mShowLeftEye, mShowRightEye;
    private boolean mStopUntilAnimationPerformCompleted;
    private OnStatusChangedListener mOnStatusChangedListener;

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

        // // get attrs
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SmileyLoadingView);
        mStrokeColor = ta.getColor(R.styleable.SmileyLoadingView_strokeColor, DEFAULT_PAINT_COLOR);
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.SmileyLoadingView_strokeWidth, dp2px(DEFAULT_PAINT_WIDTH));
        mAnimDuration = ta.getInt(R.styleable.SmileyLoadingView_duration, DEFAULT_ANIM_DURATION);
        mAnimRepeatCount = ta.getInt(R.styleable.SmileyLoadingView_animRepeatCount, ValueAnimator.INFINITE);
        ta.recycle();

        mSweepAngle = 180; // init sweepAngle
        mCirclePath = new Path();
        mArcPath = new Path();

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcPaint.setStrokeJoin(Paint.Join.ROUND);
        mArcPaint.setStrokeWidth(mStrokeWidth);
        mArcPaint.setColor(mStrokeColor);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mCirclePaint.setStrokeJoin(Paint.Join.ROUND);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(mStrokeColor);

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
                canvas.drawCircle(mLeftEyePos[0], mLeftEyePos[1], mEyeCircleRadius, mCirclePaint);
            }

            if (mShowRightEye) {
                canvas.drawCircle(mRightEyePos[0], mRightEyePos[1], mEyeCircleRadius, mCirclePaint);
            }

            if (mFirstStep) {
                mArcPath.reset();
                mArcPath.addArc(mRectF, mStartAngle, mSweepAngle);
                canvas.drawPath(mArcPath, mArcPaint);
            } else {
                mArcPath.reset();
                mArcPath.addArc(mRectF, mStartAngle, mSweepAngle);
                canvas.drawPath(mArcPath, mArcPaint);
            }
        } else {
            canvas.drawCircle(mLeftEyePos[0], mLeftEyePos[1], mEyeCircleRadius, mCirclePaint);
            canvas.drawCircle(mRightEyePos[0], mRightEyePos[1], mEyeCircleRadius, mCirclePaint);

            mArcPath.reset();
            mArcPath.addArc(mRectF, mStartAngle, mSweepAngle);
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


        float radiusX = (width - paddingRight - paddingLeft - mStrokeWidth) >> 1;
        float radiusY = (height - paddingTop - paddingBottom - mStrokeWidth) >> 1;
        mCircleRadius = Math.min(radiusX, radiusY);
        mEyeCircleRadius = mStrokeWidth / 2;

        mRectF = new RectF(paddingLeft + mStrokeWidth, paddingTop + mStrokeWidth,
                                width - mStrokeWidth - paddingRight, height - mStrokeWidth - paddingBottom);

        mArcPath.arcTo(mRectF, 0, 180);
        mCirclePath.addCircle(mCenterPos[0], mCenterPos[1], mCircleRadius, Path.Direction.CW);
        mPathMeasure = new PathMeasure(mCirclePath, true);

        mPathMeasure.getPosTan(mPathMeasure.getLength() / 8 * 5, mLeftEyePos, null);
        mPathMeasure.getPosTan(mPathMeasure.getLength() / 8 * 7, mRightEyePos, null);
        mLeftEyePos[0] += mStrokeWidth >> 2;
        mLeftEyePos[1] += mStrokeWidth >> 1;
        mRightEyePos[0] -= mStrokeWidth >> 2;
        mRightEyePos[1] += mStrokeWidth >> 1;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.end();
        }
    }

    /**
     * Set paint color alpha
     * @param alpha alpha
     */
    public void setAlpha(int alpha) {
        mArcPaint.setAlpha(alpha);
        mCirclePaint.setAlpha(alpha);
        invalidate();
    }

    /**
     * Set paint stroke color
     * @param color color
     */
    public void setStrokeColor(int color) {
        mStrokeColor = color;
        invalidate();
    }

    /**
     * Set paint stroke width
     * @param width px
     */
    public void setStrokeWidth(int width) {
        mStrokeWidth = width;
    }

    /**
     * Set animation running duration
     * @param duration duration
     */
    public void setAnimDuration(int duration) {
        mAnimDuration = duration;
    }

    /**
     * Set animation repeat count, ValueAnimator.INFINITE(-1) means cycle
     * @param repeatCount repeat count
     */
    public void setAnimRepeatCount(int repeatCount) {
        mAnimRepeatCount = repeatCount;
    }

    /**
     * set anim repeat count
     * @param animRepeatCount anim repeat count
     *                        value: -1 (INFINITE)
     */
    public void start(int animRepeatCount) {
        mAnimRepeatCount = animRepeatCount;

        mFirstStep = true;

        mValueAnimator = ValueAnimator.ofFloat(0.0f, 720.0f);
        mValueAnimator.setDuration(mAnimDuration);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setRepeatCount(mAnimRepeatCount);
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
                    mSweepAngle = calculateFirstStepSweepAngle();

                    mStartAngle = (float) animation.getAnimatedValue() + 90;
                } else {
                    mShowLeftEye = animatedValue <= 135.0f;
                    mShowRightEye = animatedValue <= 235.0f;
                    mStartAngle = (float) animation.getAnimatedValue() + 90;
                    mSweepAngle = animatedValue / 135.0f <= 1 ? animatedValue : (135.0f - (animatedValue - 135.0f) / 225 * 135);
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
                if (mOnStatusChangedListener != null) {
                    mOnStatusChangedListener.onCompleted();
                }
                reset();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mRunning = false;
                if (mOnStatusChangedListener != null) {
                    mOnStatusChangedListener.onCompleted();
                }
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
     * Start animation
     */
    public void start() {
        start(ValueAnimator.INFINITE);
    }

    /**
     * Stop animation
     */
    public void stop() {
        stop(true);
    }

    /**
     * stop it after animation perform completed
     * @param stopUntilAnimationPerformCompleted boolean
     */
    public void stop(boolean stopUntilAnimationPerformCompleted) {
        mStopUntilAnimationPerformCompleted = stopUntilAnimationPerformCompleted;
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            if (!stopUntilAnimationPerformCompleted) {
                mValueAnimator.end();
            }
        } else {
            if (mOnStatusChangedListener != null) {
                mOnStatusChangedListener.onCompleted();
            }
        }
    }

    /**
     * set status changed listener
     * @param l OnStatusChangedListener
     */
    public void setOnStatusChangedListener(OnStatusChangedListener l) {
        mOnStatusChangedListener = l;
    }

    /**
     * reset UI
     */
    private void reset() {
        mStartAngle = 0;
        mSweepAngle = 180;
        invalidate();
    }

    /**
     * set arc sweep angle when the step is first
     */
    private float calculateFirstStepSweepAngle() {
        // TODO: 16/8/20 calculateFirstStepSweepAngle
        //return (float) (180 * Math.asin(mEyeCircleRadius / mCircleRadius)/ Math.PI);
        return 1;
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

    /**
     * Callback
     */
    public interface OnStatusChangedListener {
        void onCompleted();
    }
}
