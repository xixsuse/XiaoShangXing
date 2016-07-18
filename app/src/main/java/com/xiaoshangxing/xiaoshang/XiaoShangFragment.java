package com.xiaoshangxing.xiaoshang;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.login_register.LoginRegisterActivity.LoginFragment.LoginFragment;
import com.xiaoshangxing.utils.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/7/18
 */
public class XiaoShangFragment extends BaseFragment {
    public static final String TAG = BaseFragment.TAG + "-XiaoShang";
    @Bind(R.id.xiaoshang_notice)
    ImageView xiaoshangNotice;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.first)
    ImageView first;
    @Bind(R.id.second)
    ImageView second;
    @Bind(R.id.third)
    ImageView third;
    @Bind(R.id.forth)
    ImageView forth;
    @Bind(R.id.five)
    ImageView five;
    @Bind(R.id.scrollView)
    HorizontalScrollView scrollView;
    @Bind(R.id.tuch)
    ImageView tuch;
    private View mview;

    private float current, result, current2;
    private int start = 0;
    private int currentImage;
    private boolean isMoving;
    private Handler handler = new Handler();

    private int image_width,divider,padding_start,total;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_test, null);
        mview = view;
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        image_width=getResources().getDimensionPixelSize(R.dimen.x808);
        divider=getResources().getDimensionPixelSize(R.dimen.x48);
        padding_start=getResources().getDimensionPixelSize(R.dimen.x136);
        total=image_width*5+divider*4+padding_start*2;

        Log.d("length",""+image_width+":"+divider+":"+padding_start+":"+total);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isMoving) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            current = event.getX();
                            current2 = event.getX();
                            return true;
                        case MotionEvent.ACTION_UP:
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    tuchUp(currentImage);
//                                }
//                            }, 250);
                            int des=(int)(current-event.getX());
                            jujiment(des);
                            return true;
//                        case MotionEvent.ACTION_MOVE:
//                            int x = (int) (current2 - event.getX());
//                            myMove(x);
//                            current2 = event.getX();
//                            break;
                    }
                }
                return true;
            }
        });

        tuch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        current = event.getX();
//                        int des1=(int) current;
//                        if (des1<=150){
//                            setPosition(1);
//                        }else if (des1<=300){
//                            setPosition(2);
//                        }else if (des1<=450){
//                            setPosition(3);
//                        }else if (des1<=600){
//                            setPosition(4);
//                        }else if (des1<=750){
//                            setPosition(5);
//                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float i = event.getX() - current;
//                        if (i > 0 && event.getX() > (currentImage * 150 - 150) ||
//                                i < 0 && event.getX() < (currentImage * 150)) {
                        float sca = (float) total / tuch.getWidth();
                        scrollView.smoothScrollBy((int) (i * sca), 0);
                        current = event.getX();
//                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        final int des = (int) event.getX();
                        final int item=tuch.getWidth()/5;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (des <= item) {
                                    setFoucus(1);
                                } else if (des <= item*2) {
                                    setFoucus(2);
                                } else if (des <= item*3) {
                                    setFoucus(3);
                                } else if (des <= item*4) {
                                    setFoucus(4);
                                } else if (des <= item*5) {
                                    setFoucus(5);
                                }
                            }
                        }, 250);
                }
                return false;
            }
        });
    }


    private void myMove(int x) {
        scrollView.smoothScrollBy(x, 0);
    }

    public static XiaoShangFragment newInstance() {
        return new XiaoShangFragment();
    }

    private void tuchUp(int position) {
        View v = first;
        switch (position) {
            case 1:
                v = first;
                break;
            case 2:
                v = second;
                break;
            case 3:
                v = third;
                break;
            case 4:
                v = forth;
                break;
            case 5:
                v = five;
                break;
        }

        int[] xy = new int[2];
        v.getLocationOnScreen(xy);
        if (xy[0] < 0) {
            Log.d("select", "1");
            jujiment(20);
        } else if (xy[0] > 96 * 2) {
            Log.d("select", "2");
            jujiment(-20);
        } else {
            Log.d("select", "3");
            setFoucus(currentImage);
        }
    }

    private void setFoucus(final int position) {
        if (isMoving) {
            return;
        }
        int[] xy = new int[2];
//        first.getLocationOnScreen(xy);
//        Log.d("first", "" + xy[0]);
//        second.getLocationOnScreen(xy);
//        Log.d("second", "" + xy[0]);
//        third.getLocationOnScreen(xy);
//        Log.d("third", "" + xy[0]);
//        forth.getLocationOnScreen(xy);
//        Log.d("forth", "" + xy[0]);
//        five.getLocationOnScreen(xy);
//        Log.d("five", "" + xy[0]);
        switch (position) {
            case 1:
                first.getLocationOnScreen(xy);
                break;
            case 2:
                second.getLocationOnScreen(xy);
                break;
            case 3:
                third.getLocationOnScreen(xy);
                break;
            case 4:
                forth.getLocationOnScreen(xy);
                break;
            case 5:
                five.getLocationOnScreen(xy);
                break;
        }

        setImagePosition(position);

        final ValueAnimator animator = ValueAnimator.ofInt(0, xy[0] - padding_start);
        int abs = Math.abs(xy[0] - padding_start);
        abs = abs > 250 ? abs : 250;
        animator.setDuration(abs <= 500 ? abs : 500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int i = (int) animator.getAnimatedValue();
                scrollView.smoothScrollBy(i - start, 0);
                start = i;
            }
        });
        currentImage = position;
        animator.start();
//        isMoving=true;
//        Log.d("set ismoving","true");
//        animator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                text.setText(""+currentImage);
//                isMoving=false;
//
//                Log.d("set ismoving","false");
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//                Log.d("erro","animator cancel");
//                isMoving=false;
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
        isMoving = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isMoving = false;
            }
        },750);


    }

    private void setImagePosition(int position){
        switch (position){
            case 1:
                tuch.setImageResource(R.mipmap.xiaoshang_select1);
                title.setText("校历资讯");
                break;
            case 2:
                tuch.setImageResource(R.mipmap.xiaoshang_select2);
                title.setText("校内悬赏");
                break;
            case 3:
                tuch.setImageResource(R.mipmap.xiaoshang_select3);
                title.setText("校友互帮");
                break;
            case 4:
                tuch.setImageResource(R.mipmap.xiaoshang_select4);
                title.setText("计划发起");
                break;
            case 5:
                tuch.setImageResource(R.mipmap.xiaoshang_select5);
                title.setText("闲置出售");
                break;
        }
    }

    private void jujiment(int result) {
        Log.d("current", "" + currentImage);
        if (!isMoving) {
            if (result > 15) {
                if (currentImage != 5) {
                    setFoucus(currentImage + 1);
                    Log.d("current", "to" + (currentImage));
                }

            } else if (result < -15) {
                if (currentImage != 1) {
                    setFoucus(currentImage - 1);
                    Log.d("current", "to" + (currentImage));
                }
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.xiaoshang_notice)
    public void onClick() {
    }

    @Override
    public void onResume() {
        super.onResume();
        setImagePosition(currentImage);
    }
}
