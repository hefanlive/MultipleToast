package util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hefan.hftoast.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by HXD on 2017/7/21 0021.
 */
public class HFToast {
    private static final String TAG = "HFToast";
    public static final int DEFAULT_SUCCESS_STYLE = 1;
    public static final int DEFAULT_FAILED_STYLE = 2;
    public static final int LIVE_STYLE = 3;

    public static final int LENGTH_ALWAYS = 0;
    public static final int LENGTH_SHORT = 2;
    public static final int LENGTH_LONG = 4;

    private Toast toast;
    private Context mContext;
    private int mDuration = LENGTH_SHORT;
    private int animations = -1;
    private boolean isShow = false;

    private Object mTN;
    private Method show;
    private Method hide;
    private Builder mBuilder;
    View mToastRoot;
    ImageView mImageView;
    TextView mTextView;

//    private Handler handler = new Handler();

    public HFToast(Builder builder) {
        this.mContext = builder.mContext;
        this.mBuilder = builder;
        if (toast == null) {
            toast = new Toast(mContext);
        }
    }

//    private Runnable hideRunnable = new Runnable() {
//        @Override
//        public void run() {
//            hide();
//        }
//    };

    /**
     * Show the view for the specified duration.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void show() {
//        if (isShow) return;

        initTN();
        toast.show();
//        try {
////            show.invoke(mTN);
//            //解决Android 7.1问题
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
//                show.invoke(mTN, new Binder());
//            } else {
//                show.invoke(mTN);
//            }
//        } catch (InvocationTargetException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        isShow = true;
//        //判断duration，如果大于#LENGTH_ALWAYS 则设置消失时间
////        if (mDuration > LENGTH_ALWAYS) {
////            handler.postDelayed(hideRunnable, mDuration * 1000);
////        }
//        if (mBuilder.duration > LENGTH_ALWAYS) {
//            handler.postDelayed(hideRunnable, mBuilder.duration);
//        }
    }

    /**
     * Close the view if it's showing, or don't show it if it isn't showing yet.
     * You do not normally have to call this.  Normally view will disappear on its own
     * after the appropriate duration.
     */
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    public void hide() {
//        if (!isShow) return;
//        try {
//            hide.invoke(mTN);
//        } catch (InvocationTargetException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        isShow = false;
//    }
    public void setView(View view) {
        toast.setView(view);
    }

    public View getView() {
        return toast.getView();
    }

    /**
     * Set how long to show the view for.
     *
     * @see #LENGTH_SHORT
     * @see #LENGTH_LONG
     * @see #LENGTH_ALWAYS
     */
    public void setDuration(int duration) {
        mDuration = duration;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setMargin(float horizontalMargin, float verticalMargin) {
        toast.setMargin(horizontalMargin, verticalMargin);
    }

//    public float getHorizontalMargin() {
//        return toast.getHorizontalMargin();
//    }
//
//    public float getVerticalMargin() {
//        return toast.getVerticalMargin();
//    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        toast.setGravity(gravity, xOffset, yOffset);
    }

    public int getGravity() {
        return toast.getGravity();
    }

//    public int getXOffset() {
//        return toast.getXOffset();
//    }
//
//    public int getYOffset() {
//        return toast.getYOffset();
//    }

//    public static HFToast makeText(Context context, CharSequence text, int duration) {
//        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
//        HFToast exToast = new HFToast(context);
//        exToast.toast = toast;
//        exToast.mDuration = duration;
//        return exToast;
//    }
//
//    public static HFToast makeText(Context context, int resId, int duration)
//            throws Resources.NotFoundException {
//        return makeText(context, context.getResources().getText(resId), duration);
//    }

    public static HFToast makeText(Builder builder) {

        Toast toast = Toast.makeText(builder.mContext, builder.message, Toast.LENGTH_SHORT);
        HFToast exToast = new HFToast(builder);
        exToast.toast = toast;
        exToast.mDuration = builder.duration;
        return exToast;
    }

//    public static HFToast makeText(Builder builder)
//            throws Resources.NotFoundException {
//        return makeText(context, context.getResources().getText(resId), duration);
//    }

    public void setText(int resId) {
        setText(mContext.getText(resId));
    }

    public void setText(CharSequence s) {
        toast.setText(s);
    }

    public int getAnimations() {
        return animations;
    }

    public void setAnimations(int animations) {
        this.animations = animations;
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
            setToastType(mBuilder.toastStyle);
            toast.setView(mToastRoot);

            /**设置动画*/
//            if (animations != -1) {
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

            params.windowAnimations = R.style.anim_view;

            params.gravity = mBuilder.layoutGravity;
//            params.y = -200;
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
            toast.setGravity(Gravity.TOP, 0, 0);

//            }

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
            case DEFAULT_SUCCESS_STYLE:
                mToastRoot = LayoutInflater.from(mContext).inflate(R.layout.toast_default_view, null);
                mImageView = (ImageView) mToastRoot.findViewById(R.id.imageView);
                mTextView = (TextView) mToastRoot.findViewById(R.id.message);

                mImageView.setImageResource(R.drawable.icon_duigou);
                mTextView.setText(mBuilder.message);
                break;
            case DEFAULT_FAILED_STYLE:
                mToastRoot = LayoutInflater.from(mContext).inflate(R.layout.toast_default_view, null);
                mImageView = (ImageView) mToastRoot.findViewById(R.id.imageView);
                mTextView = (TextView) mToastRoot.findViewById(R.id.message);

                mImageView.setImageResource(R.drawable.icon_wangluoyichang);
                mTextView.setText(mBuilder.message);
                break;
            case LIVE_STYLE:
                mToastRoot = LayoutInflater.from(mContext).inflate(R.layout.toast_live_view, null);
                mTextView = (TextView) mToastRoot.findViewById(R.id.message);

                mTextView.setText(mBuilder.message);
                break;
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static class Builder {

        public String message;

        public int iconResId = R.drawable.icon_wangluoyichang;

        public int backgroundColor;

        public int messageColor;

        public int messageSize;

        public int duration = 800;

        public int layoutGravity = Gravity.TOP;

        public int toastHeight;

        public int statusHeight;

        public int toastStyle;

        public Context mContext;

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
            this.messageColor = messageColor;
            return this;
        }

        public Builder setMessageSize(int messageSize) {
            this.messageSize = messageSize;
            return this;
        }

        public Builder setBackgroundColor(@ColorRes int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setToastHeight(@ColorRes int toastHeight) {
            this.toastHeight = toastHeight;
            return this;
        }

        public Builder setLayoutGravity(int layoutGravity) {
            if (layoutGravity > 0) {
                this.layoutGravity = layoutGravity;
            }
            return this;
        }

        public Builder setToastDefaultSuccessStyle() {
            this.toastStyle = DEFAULT_SUCCESS_STYLE;
            this.iconResId = R.drawable.icon_duigou;
            return this;
        }

        public Builder setToastDefaultFailedStyle() {
            this.toastStyle = DEFAULT_FAILED_STYLE;
            this.iconResId = R.drawable.icon_wangluoyichang;
            return this;
        }

        public Builder setToastLiveStyle() {
            this.toastStyle = LIVE_STYLE;

            return this;
        }

        //        public HFToast create() {
//            HFToast toastBar = new HFToast(mActivity );
//            return toastBar;
//        }
//
//        public ToastBar show() {
//            if(mActivity == null){
//                return null;
//            }
//            final ToastBar toastBar = create();
//            toastBar.show();
//            return toastBar;
//        }
        public void show() {
            HFToast.makeText(this).show();
        }
    }
}
