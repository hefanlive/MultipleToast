package com.hefan.multipletoast;

import android.app.Activity;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.ViewGroup;


/**
 * Created by hxd on 2017/5/26.
 */
public class ToastBar {
    private static final String TAG = "ToastBar";

    private ToastView toastView;
    private Activity context;

    private ToastBar() {
    }

    private ToastBar(Activity context, Params params) {
        this.context = context;
        toastView = new ToastView(context);
        toastView.setParams(params);
    }

    public void show() {
        if (toastView != null) {
            final ViewGroup decorView = (ViewGroup) context.getWindow().getDecorView();
            final ViewGroup content = (ViewGroup) decorView.findViewById(android.R.id.content);
            if (toastView.getParent() == null) {
                if (toastView.getLayoutGravity() == Gravity.BOTTOM) {
                    content.addView(toastView);
                } else {
                    decorView.addView(toastView);
                }
            }
        }
    }

    public static class Builder {

        private Params params = new Params();

        public Activity context;

        /**
         * Create a builder for an ToastBar.
         */
        public Builder(Activity activity) {
            this.context = activity;
        }

        public Builder setIcon(@DrawableRes int iconResId) {
            params.iconResId = iconResId;
            return this;
        }

        public Builder setMessage(String message) {
            params.message = message;
            return this;
        }

        public Builder setMessage(@StringRes int resId) {
            params.message = context.getString(resId);
            return this;
        }

        public Builder setDuration(long duration) {
            params.duration = duration;
            return this;
        }

        public Builder setMessageColor(@ColorRes int messageColor) {
            params.messageColor = messageColor;
            return this;
        }

        public Builder setBackgroundColor(@ColorRes int backgroundColor) {
            params.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setToastHeight(@ColorRes int toastHeight) {
            params.toastHeight = toastHeight;
            return this;
        }

        public Builder setLayoutGravity(int layoutGravity) {
            params.layoutGravity = layoutGravity;
            return this;
        }

        public Builder setToastDefaultStyle() {
            params.backgroundColor = R.color.white;
            params.toastHeight = ScreenUtils.Dp2Px(context, 50);
            params.statusHeight = getStatusBarHeight(context);
            return this;
        }

        public Builder setToastLiveStyle() {
            params.backgroundColor = R.color.yellow;
            params.toastHeight = ScreenUtils.Dp2Px(context, 25);
            params.statusHeight = getStatusBarHeight(context);
            return this;
        }

        public ToastBar create() {
            ToastBar toastBar = new ToastBar(context, params);
            return toastBar;
        }

        public ToastBar show() {
            final ToastBar toastBar = create();
            toastBar.show();
            return toastBar;
        }
    }

    final static class Params {
        public String message;

        public int iconResId;

        public int backgroundColor;

        public int messageColor;

        public long duration = 1200;

        public int layoutGravity = Gravity.TOP;

        public int toastHeight;

        public int statusHeight;

    }

    public static int getStatusBarHeight(Activity context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
