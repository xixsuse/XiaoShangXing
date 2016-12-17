package com.xiaoshangxing.utils.customView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;

/**
 * Created by FengChaoQun
 * on 2016/12/16
 */

public class RuleUtil {
    private View rootView;
    private Activity activity;
    private View mengban, wrapView;
    private ImageView ruleButton;
    private boolean isShow;
    private boolean isAnim;
    private float hideenStateY;

    public RuleUtil(View rootView, Activity activity) {
        this.rootView = rootView;
        this.activity = activity;
        init();
    }

    private void init() {
        mengban = rootView.findViewById(R.id.mengban);
        wrapView = rootView.findViewById(R.id.wrap_view);
        ruleButton = (ImageView) rootView.findViewById(R.id.rule_button);

        wrapView.post(new Runnable() {
            @Override
            public void run() {
                float orignalY = wrapView.getY();
                float wrapViewHeight = wrapView.getHeight();
                hideenStateY = -wrapViewHeight + orignalY + ScreenUtils.getAdapterPx(R.dimen.y96, activity);
                wrapView.setY(hideenStateY);
            }
        });
        ruleButton.setImageResource(R.mipmap.notice_rule_new);
        ruleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    hide();
                } else {
                    show();
                }
            }
        });

        mengban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

    }

    public void hide() {
        if (isAnim) {
            return;
        }
        isShow = false;
        ObjectAnimator wrapAnim =
                ObjectAnimator.ofFloat(wrapView, "y", wrapView.getY(), hideenStateY)
                        .setDuration(500);
        wrapAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnim = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mengban.setVisibility(View.GONE);
                isAnim = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isAnim = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        wrapAnim.start();
        ObjectAnimator animator = ObjectAnimator.ofFloat(mengban, "alpha", 0.5f, 0);
        animator.setDuration(500);
        animator.start();
        ruleButton.setImageResource(R.mipmap.notice_rule_new);
    }

    public void show() {
        if (isAnim) {
            return;
        }
        isShow = true;
        ObjectAnimator wrapAnim =
                ObjectAnimator.ofFloat(wrapView, "y", wrapView.getY(), 0)
                        .setDuration(500);
        wrapAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mengban.setVisibility(View.VISIBLE);
                isAnim = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnim = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isAnim = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        wrapAnim.start();
        ObjectAnimator animator = ObjectAnimator.ofFloat(mengban, "alpha", 0, 0.5f);
        animator.setDuration(500);
        animator.start();
        ruleButton.setImageResource(R.mipmap.rules_up);
    }

    public boolean needhideRules() {
        if (isShow) {
            hide();
            return true;
        }
        return false;
    }
}
