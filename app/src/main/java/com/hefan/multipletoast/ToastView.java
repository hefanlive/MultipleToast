package com.hefan.multipletoast;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by hxd on 2017/5/26.
 */
public class ToastView extends LinearLayout {
    private Animation slideInAnimation;
    private Animation slideOutAnimation;

    private LinearLayout layoutRoot;
    private LinearLayout layoutToast;
    private LinearLayout statusView;
    private TextView tvMessage;
    private ImageView ivIcon;
    private long duration;
    private int layoutGravity = Gravity.BOTTOM;

    public ToastView(@NonNull final Context context) {
        this(context, null);
    }

    public ToastView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToastView(@NonNull final Context context, @Nullable final AttributeSet attrs,
                     final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public int getLayoutGravity() {
        return layoutGravity;
    }

    private void initViews(Context context) {
        inflate(getContext(), R.layout.toast_layout, this);

        layoutRoot = (LinearLayout) findViewById(R.id.ll_root);
        layoutToast = (LinearLayout) findViewById(R.id.ll_toast);
        statusView = (LinearLayout) findViewById(R.id.status_view);
        tvMessage = (TextView) findViewById(R.id.tv_unified_toast);
        ivIcon = (ImageView) findViewById(R.id.iv_unified_toast);
        initDefaultStyle(context);
    }


    private void initDefaultStyle(Context context) {
        int messageColor = ThemeResolver.getColor(context, R.attr.cookieMessageColor, R.color.black64);
        int backgroundColor = ThemeResolver.getColor(context, R.attr.cookieBackgroundColor,
                ContextCompat.getColor(context, R.color.white));

        tvMessage.setTextColor(messageColor);
        layoutRoot.setBackgroundColor(backgroundColor);
    }

    public void setParams(final ToastBar.Params params) {
        if (params != null) {
            duration = params.duration;
            layoutGravity = params.layoutGravity;

            //Icon
            if (params.iconResId != 0) {
                ivIcon.setVisibility(VISIBLE);
                ivIcon.setBackgroundResource(params.iconResId);
            }

            //Message
            if (!TextUtils.isEmpty(params.message)) {
                tvMessage.setVisibility(VISIBLE);
                tvMessage.setText(params.message);
                if (params.messageColor != 0) {
                    tvMessage.setTextColor(ContextCompat.getColor(getContext(), params.messageColor));
                }
            }

            int padding = getContext().getResources().getDimensionPixelSize(R.dimen.default_16dp_hf);
            if (layoutGravity == Gravity.BOTTOM) {
                layoutRoot.setPadding(padding, padding, padding, padding);
            }

            ViewGroup.LayoutParams lt = layoutToast.getLayoutParams();
            lt.height = params.toastHeight;
            layoutToast.setLayoutParams(lt);

            ViewGroup.LayoutParams sv = statusView.getLayoutParams();
            sv.height = params.statusHeight;
            statusView.setLayoutParams(sv);

//            ViewGroup.LayoutParams lr = layoutRoot.getLayoutParams();
//            lr.height = params.statusHeight + params.toastHeight;
//            layoutRoot.setLayoutParams(lr);

            //Background
            if (params.backgroundColor != 0) {
                layoutRoot
                        .setBackgroundColor(ContextCompat.getColor(getContext(), params.backgroundColor));
            }

            
            createInAnim();
            createOutAnim();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (layoutGravity == Gravity.TOP) {
            super.onLayout(changed, l, 0, r, layoutRoot.getMeasuredHeight());
        } else {
            super.onLayout(changed, l, t, r, b);
        }
    }

    private void createInAnim() {
        slideInAnimation = AnimationUtils.loadAnimation(getContext(),
                layoutGravity == Gravity.BOTTOM ? R.anim.slide_in_from_bottom : R.anim.slide_in_from_top);

        slideInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, duration);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        setAnimation(slideInAnimation);
    }

    private void createOutAnim() {
        slideOutAnimation = AnimationUtils.loadAnimation(getContext(),
                layoutGravity == Gravity.BOTTOM ? R.anim.slide_out_to_bottom : R.anim.slide_out_to_top);
        slideOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void dismiss() {
        slideOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {
            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                destroy();
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {
            }
        });
        startAnimation(slideOutAnimation);
    }

    private void destroy() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewParent parent = getParent();
                if (parent != null) {
                    ToastView.this.clearAnimation();
                    ((ViewGroup) parent).removeView(ToastView.this);
                }
            }
        }, 100);
    }

}
