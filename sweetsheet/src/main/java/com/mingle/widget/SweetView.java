package com.mingle.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.mingle.SimpleAnimationListener;
import com.mingle.sweetsheet.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;


/**
 * @author zzz40500
 * @version 1.0
 * @date 2015/8/5.
 * @github: https://github.com/zzz40500
 * <p>
 * 这个可能就是那个动画的感觉---------是的，把这个注销掉，在RecyclerViewDelegate 中，那种效果就会消失
 */
public class SweetView extends View {

    private Paint mPaint;
    //看到这个地方了吗？ 是有圆弧的
    private int mArcHeight;
    private int mMaxArcHeight;
    private Status mStatus = Status.NONE;
    private AnimationListener mAnimationListener;
    private Path mPath = new Path();

    public enum Status {
        NONE,
        STATUS_SMOOTH_UP,
        STATUS_UP,
        STATUS_DOWN,
    }

    public SweetView(Context context) {
        super(context);
        init();
    }

    public SweetView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public SweetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(android.R.color.white));
        mMaxArcHeight = getResources().getDimensionPixelSize(R.dimen.arc_max_height);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SweetView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBG(canvas);
    }

    public void show() {
        mStatus = Status.STATUS_SMOOTH_UP;


        if (mAnimationListener != null) {
            mAnimationListener.onStart();
            this.postDelayed(new Runnable() {
                @Override
                public void run() {

                    mAnimationListener.onContentShow();
                }
            }, 600);
        }

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mMaxArcHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mArcHeight = value;

                if (value == mMaxArcHeight) {
//                    duang();
                }
                invalidate();
            }
        });
        valueAnimator.setDuration(800);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();

    }

    /**
     * fixme 2022年6月8日17:57:13
     * 那个回弹的效果
     * 来自这里
     */
    public void duang() {
        mStatus = Status.STATUS_DOWN;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(mMaxArcHeight, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mArcHeight = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.addListener(new SimpleAnimationListener() {


            @Override
            public void onAnimationEnd(Animator animation) {
                if (mAnimationListener != null) {
                    mAnimationListener.onEnd();
                }
            }

        });
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new OvershootInterpolator(4f));
        valueAnimator.start();

    }


    private void drawBG(Canvas canvas) {
        mPath.reset();
        int currentPointY = 0;
        switch (mStatus) {
            case NONE:
                currentPointY = mMaxArcHeight;
                break;
            case STATUS_SMOOTH_UP:
            case STATUS_UP:
                currentPointY = getHeight() - (int) ((getHeight() - mMaxArcHeight) * Math.min(1, (mArcHeight - mMaxArcHeight / 4) * 2.0 / mMaxArcHeight * 1.3));
                break;
            case STATUS_DOWN:
                currentPointY = mMaxArcHeight;
                break;
            default:
        }

        mPath.moveTo(0, currentPointY);
        mPath.quadTo(getWidth() / 2, currentPointY - mArcHeight, getWidth(), currentPointY);
        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(0, getHeight());
        mPath.lineTo(0, currentPointY);
        canvas.drawPath(mPath, mPaint);
    }


    public AnimationListener getAnimationListener() {
        return mAnimationListener;
    }

    public void setAnimationListener(AnimationListener animationListener) {
        mAnimationListener = animationListener;
    }

    public interface AnimationListener {

        void onStart();

        void onEnd();

        /**
         * 延迟一段时间展示，这个展示的效果给人飞感觉不是从最底部展示出来的，而是从中间，显得时间没有那么长
         * <p>
         * fixme 2022年6月8日20:38:07
         * 你需要考虑那个动画，就是rv的item没有那个动画是什么展示效果，然后怎么得到想到的效果
         */
        void onContentShow();

    }

    public void setSweetSheetColor(int color) {
        mPaint.setColor(color);
    }
}
