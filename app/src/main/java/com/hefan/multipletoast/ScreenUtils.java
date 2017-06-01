package com.hefan.multipletoast;

import android.content.Context;

/**
 * Created by hxd on 2017/6/1.
 */
public class ScreenUtils {

    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
