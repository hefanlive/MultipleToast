package com.hefan.multipletoast;

import android.app.Activity;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.ViewGroup;


/**
 * Created by hxd on 2017/5/26.
 */
public class ToastBar {
    private static final String TAG = "ToastBar";
    public static final int DEFAULT_STYLE = 1;
    public static final int LIVE_STYLE = 2;

    private ToastView toastView;
    private Activity mActivity;

    private ToastBar() {
    }

    private ToastBar(Activity context, Params params) {
        this.mActivity = context;
        toastView = new ToastView(context);
        toastView.setParams(context,params);
    }

    public void show() {
        if (toastView != null && mActivity != null) {
            final ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
            final ViewGroup content = (ViewGroup) decorView.findViewById(android.R.id.content);
            if (toastView.getParent() == null) {
                if (toastView.getLayoutGravity() == Gravity.BOTTOM) {
                    content.addView(toastView);
                } else {
//                    decorView.addView(toastView);
                    content.addView(toastView);
                }
            }
        }
    }

    public static class Builder {

        private Params params = new Params();

        public Activity mActivity;

        /**
         * Create a builder for an ToastBar.
         */
        public Builder(@NonNull Activity activity) {
            this.mActivity = activity;
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
            if(mActivity != null) {
                params.message = mActivity.getString(resId);
            }
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

        public Builder setMessageSize(int messageSize) {
            params.messageSize = messageSize;
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
            params.toastStyle = DEFAULT_STYLE;

            return this;
        }

        public Builder setToastLiveStyle() {
            params.toastStyle = LIVE_STYLE;

            return this;
        }

        public ToastBar create() {
            ToastBar toastBar = new ToastBar(mActivity, params);
            return toastBar;
        }

        public ToastBar show() {
            if(mActivity == null){
                return null;
            }
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

        public int messageSize;

        public long duration = 1200;

        public int layoutGravity = Gravity.TOP;

        public int toastHeight;

        public int statusHeight;

        public int toastStyle;
    }


}
