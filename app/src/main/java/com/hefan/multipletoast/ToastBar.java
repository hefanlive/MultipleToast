package com.hefan.multipletoast;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Created by hxd on 2017/5/26.
 */
public class ToastBar {
    private static final String TAG = "ToastBar";
    public static final int DEFAULT_STYLE = 1;
    public static final int LIVE_STYLE = 2;
    public static final int CUSTOM_STYLE = 3;

    public static final int LENGTH_ALWAYS = 0;
    public static final int LENGTH_SHORT = 2;
    public static final int LENGTH_LONG = 4;

    private ToastView toastView;
    private Activity mActivity;

    private Builder mBuilder;
    private Context mContext;
    private Toast toast;
    private int mDuration = LENGTH_SHORT;

    private Object mTN;
    private Method show;
    private Method hide;
    View mToastRoot;
    ImageView mImageView;
    TextView mTextView;

//    private ToastBar(Activity context, Params params) {
//        this.mActivity = context;
//        toastView = new ToastView(context);
//        toastView.setParams(context, params);
//    }
//
//    public void show() {
//        if (toastView != null && mActivity != null) {
//            final ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
//            final ViewGroup content = (ViewGroup) decorView.findViewById(android.R.id.content);
//            if (toastView.getParent() == null) {
//                if (toastView.getLayoutGravity() == Gravity.BOTTOM) {
//                    content.addView(toastView);
//                } else {
////                    decorView.addView(toastView);
//                    content.addView(toastView);
//                }
//            }
//        }
//    }

    public ToastBar(Builder builder) {
        this.mContext = builder.mContext;
        this.mBuilder = builder;
        if (toast == null) {
            toast = new Toast(mContext);
        }
    }

    public static ToastBar makeText(Builder builder) {

        Toast toast = Toast.makeText(builder.mContext, builder.message, Toast.LENGTH_SHORT);
        ToastBar exToast = new ToastBar(builder);
        exToast.toast = toast;
        exToast.mDuration = builder.duration;
        return exToast;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void show() {
//        if (isShow) return;

        initTN();
        toast.show();
    }

    private void initTN() {
        try {
            Field tnField = toast.getClass().getDeclaredField("mTN");
            tnField.setAccessible(true);
            mTN = tnField.get(toast);
            //解决Android 7.1问题
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
//                show = mTN.getClass().getMethod("show", IBinder.class);
//            } else {
//                show = mTN.getClass().getMethod("show");
//            }
            hide = mTN.getClass().getMethod("hide");

            //设置样式
            if (mBuilder.rootView != null) {
                toast.setView(mBuilder.rootView);
                mToastRoot = mBuilder.rootView;
            } else {
                //默认样式
                setToastType(mBuilder.toastStyle);
                toast.setView(mToastRoot);
            }

            Field tnParamsField = mTN.getClass().getDeclaredField("mParams");
            tnParamsField.setAccessible(true);
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) tnParamsField.get(mTN);
            params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
//                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;

            //设置动画
            if (mBuilder.windowAnimations > 0) {
                params.windowAnimations = mBuilder.windowAnimations;
            } else {
                params.windowAnimations = com.hefan.hftoast.R.style.anim_view;
            }

            params.gravity = mBuilder.layoutGravity;
            //弹窗类型
            params.type = WindowManager.LayoutParams.TYPE_TOAST;

            toast.setGravity(Gravity.TOP, 0, 0);

            /**调用tn.show()之前一定要先设置mNextView*/
            Field tnNextViewField = mTN.getClass().getDeclaredField("mNextView");
            tnNextViewField.setAccessible(true);
            tnNextViewField.set(mTN, toast.getView());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setToastType(int type) {
        switch (type) {
            case DEFAULT_STYLE:
                mToastRoot = LayoutInflater.from(mContext).inflate(R.layout.toast_default_view, null);
                mImageView = (ImageView) mToastRoot.findViewById(R.id.imageView);
                mTextView = (TextView) mToastRoot.findViewById(R.id.message);

                mImageView.setImageResource(mBuilder.iconResId);
                mTextView.setText(mBuilder.message);
                break;
//            case DEFAULT_FAILED_STYLE:
//                mToastRoot = LayoutInflater.from(mContext).inflate(com.hefan.hftoast.R.layout.toast_default_view, null);
//                mImageView = (ImageView) mToastRoot.findViewById(com.hefan.hftoast.R.id.imageView);
//                mTextView = (TextView) mToastRoot.findViewById(com.hefan.hftoast.R.id.message);
//
//                mImageView.setImageResource(com.hefan.hftoast.R.drawable.icon_wangluoyichang);
//                mTextView.setText(mBuilder.message);
//                break;
            case LIVE_STYLE:
                mToastRoot = LayoutInflater.from(mContext).inflate(com.hefan.hftoast.R.layout.toast_live_view, null);
                mTextView = (TextView) mToastRoot.findViewById(com.hefan.hftoast.R.id.message);

                mTextView.setText(mBuilder.message);
                break;
            case CUSTOM_STYLE:
                mToastRoot = LayoutInflater.from(mContext).inflate(com.hefan.hftoast.R.layout.toast_live_view, null);
//                mTextView = (TextView) mToastRoot.findViewById(com.hefan.hftoast.R.id.message);
//
//                mTextView.setText(mBuilder.message);

                //设置弹窗高度
                if (mBuilder.toastHeight > 0 && mToastRoot != null) {
                    ViewGroup.LayoutParams layoutParams = mToastRoot.getLayoutParams();
                    layoutParams.height = mBuilder.toastHeight;
                    mToastRoot.setLayoutParams(layoutParams);
                }

                break;
        }


    }

    public static class Builder {
        public Context mContext;

        public String message;

        public int iconResId;

        public int backgroundColor;

        public int messageColor;

        public int messageSize;

        public int duration = 800;

        public int layoutGravity = Gravity.TOP;

        public int toastHeight = 0;

        public int statusHeight;

        public int toastStyle;

        public View rootView = null;

        public int windowAnimations = -1;

        /**
         * Create a builder for an ToastBar.
         */
        public Builder(@NonNull Context context) {
            this.mContext = context;
        }

        public Builder setIcon(@DrawableRes int iconResId) {
            if (iconResId > 0) {
                this.iconResId = iconResId;
            }
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(@StringRes int resId) {
            if (mContext != null) {
                this.message = mContext.getString(resId);
            }
            return this;
        }

        public Builder setDuration(int duration) {
            if (duration > 0) {
                this.duration = duration;
            }
            return this;
        }

        public Builder setMessageColor(@ColorRes int messageColor) {
            if (messageColor > 0) {
                this.messageColor = messageColor;
            }
            return this;
        }

        public Builder setMessageSize(int messageSize) {
            if (messageSize > 0) {
                this.messageSize = messageSize;
            }
            return this;
        }

        public Builder setBackgroundColor(@ColorRes int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setToastHeight(@ColorRes int toastHeight) {
            if (toastHeight > 0) {
                this.toastHeight = toastHeight;
            }
            return this;
        }

        public Builder setLayoutGravity(int layoutGravity) {
            if (layoutGravity > 0) {
                this.layoutGravity = layoutGravity;
            }
            return this;
        }

        public Builder setRootView(View rootView) {
            if (rootView != null) {
                this.rootView = rootView;
            }
            return this;
        }

        public Builder setAnimation(int animation) {
            if (animation > 0) {
                this.windowAnimations = animation;
            }
            return this;
        }

        public Builder setToastDefaultStyle() {
            this.toastStyle = DEFAULT_STYLE;
            return this;
        }

        public Builder setToastLiveStyle() {
            this.toastStyle = LIVE_STYLE;
            return this;
        }

//        public ToastBar create() {
//            ToastBar toastBar = new ToastBar(mContext, params);
//            return toastBar;
//        }

//        public ToastBar show() {
//            if(mActivity == null){
//                return null;
//            }
//            final ToastBar toastBar = create();
//            toastBar.show();
//            return toastBar;
//        }

        public void show() {
            ToastBar.makeText(this).show();
        }
    }


}
