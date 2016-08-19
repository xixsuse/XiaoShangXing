package com.xiaoshangxing.xiaoshang.schoolcalender.addinfofragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xiaoshangxing.utils.normalUtils.UpKeyBoard;

/**
 * Created by quchwe on 2016/7/22 0022.
 */
public class MyEditText extends EditText {
    Context context;
    public MyEditText(Context context) {
        super(context);
        this.context = context;
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    protected int downX = 0, downY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventCode = event.getAction();
        switch (eventCode) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                peform();

                UpKeyBoard.autoUpKeyBoard(MyEditText.this);
//                if (Math.abs(upX - downX) < 10 && Math.abs(upY - downY) < 10) {//点击事件
//                    //performClick();
//                    //doClickAction((upX + downX)/2,(upY + downY)/2);
//                    peform();
//
//                } else if (upY - downY > 50) {
//                    //onLeftClick();
//                    peform();
//
//                } else if (downY - upY > 50) {
//                    // onRightClick();
//                    peform();
//                }
                break;
        }
        return true;
    }

    private LinearLayout layout;
    Animation animation;

    public void setLayout(LinearLayout layout, Animation a) {
        this.layout = layout;
        this.animation = a;
    }

    private void peform() {
        if (layout != null && animation != null) {
            if (layout.getVisibility() == View.VISIBLE) {

                layout.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        layout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                layout.startAnimation(animation);

            }
        }

    }
}
