package com.mingle.widget;

import android.animation.Animator;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.mingle.SimpleAnimationListener;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * @author zzz40500
 * @version 1.0
 * @date 2015/8/9.
 * @github: https://github.com/zzz40500
 */
public class CircleRevealHelper {


    private static final String TAG = "CircleRevealHelper";

    public CircleRevealHelper(View view) {
        mView = view;
        if (view instanceof CircleRevealEnable) {
            mCircleRevealEnable = (CircleRevealEnable) view;
        } else {
            throw new RuntimeException("the View must implements CircleRevealEnable ");
        }

    }

    private Path mPath = new Path();

    private View mView;

    private int mAnchorX, mAnchorY;
    private float mRadius;

    private boolean isCircularReveal = false;

    private CircleRevealEnable mCircleRevealEnable;


    public void circularReveal(int centerX, int centerY, float startRadius, float endRadius) {
        this.circularReveal(centerX, centerY, startRadius, endRadius, 700, new AccelerateDecelerateInterpolator());
    }


    public void circularReveal(int centerX, int centerY, float startRadius, float endRadius, long duration, Interpolator interpolator) {

        //这个打印在这里，打印早了，所以打印出来的是0
//        Log.i("gainLength", System.currentTimeMillis() + "=====circularReveal====mAnchorX====" + mAnchorX + "=======mAnchorY=====" + mAnchorY + "====mView.getParent()=====" + mView.getParent());

        Log.i("gainLength", centerX+"=====centerX=======centerX=====" + centerX);
        mAnchorX = centerX;
        mAnchorY = centerY;
        if (mView.getParent() == null) {
            return;
        }
        /**
         * https://blog.csdn.net/qq_27634797/article/details/76775244
         * ViewAnimationUtils是Android5.0出来的API。其作用就是可以使控件能够呈现水波一样展开。先上一张效果图：
         *
         *
         *  这个是从中间展开的，类似水波，从中间向两边展开
         */
        Log.i(TAG, "=====(Build.VERSION.SDK_INT >= 21)========" + (Build.VERSION.SDK_INT >= 21));
        if (Build.VERSION.SDK_INT >= 21) {
            Animator animator = ViewAnimationUtils.createCircularReveal(
                    mView,
                    mAnchorX,
                    mAnchorY,
                    startRadius,
                    endRadius);
            animator.setInterpolator(interpolator);
            animator.setDuration(duration);
            animator.start();
        } else {

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(startRadius, endRadius);

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mRadius = (float) animation.getAnimatedValue();

                    mView.invalidate();
                }
            });
            valueAnimator.setInterpolator(interpolator);
            valueAnimator.addListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
                    isCircularReveal = true;
                }

                @Override
                public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                    isCircularReveal = false;
                }

                @Override
                public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {
                    isCircularReveal = false;
                }

            });
            valueAnimator.setDuration(duration);
            valueAnimator.start();
        }

    }


    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "=====onDraw=====isCircularReveal===" + isCircularReveal);
        if (isCircularReveal) {
            canvas.save();
            canvas.translate(0, 0);
            mPath.reset();
            mPath.addCircle(mAnchorX, mAnchorY, mRadius, Path.Direction.CCW);
            canvas.clipPath(mPath, Region.Op.REPLACE);
            mCircleRevealEnable.superOnDraw(canvas);
            canvas.restore();
        } else {
            mCircleRevealEnable.superOnDraw(canvas);
        }
    }


    public interface CircleRevealEnable {


        void superOnDraw(Canvas canvas);

        /**
         * circularReveal 这个单词翻译出来的含义是 循环显示
         */
        void circularReveal(int centerX, int centerY, float startRadius, float endRadius, long duration, Interpolator interpolator);

        void circularReveal(int centerX, int centerY, float startRadius, float endRadius);
    }

}
