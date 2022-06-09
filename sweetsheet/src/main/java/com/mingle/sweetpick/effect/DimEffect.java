package com.mingle.sweetpick.effect;

import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * @author zzz40500
 * @version 1.0
 * @date 2015/8/9.
 * @github: https://github.com/zzz40500
 */
public class DimEffect implements Effect {

    private static final String TAG = "DimEffect";
    private float Value;

    public DimEffect(float value) {
        Value = value;
    }

    public void setValue(float value) {
        Value = value;
    }

    @Override
    public float getValue() {
        return Value;
    }

    /**
     * 这个没有什么实际的意义啊，仅仅是一个设置操作，上面的方法都没有被调用到
     * 这个 Value 只是在构造方法里面传递进来的阿能数值
     */
    @Override
    public void effect(ViewGroup vp, ImageView view) {
        Log.i(TAG, "====effect 执行了几次============"+Value);
        view.setBackgroundColor(Color.argb((int) (150 * Value), 150, 150, 150));
    }
}
