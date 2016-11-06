package com.xiaoshangxing.utils;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.WindowManager;
import android.widget.Toast;

import com.xiaoshangxing.R;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/6/11
 */
public class BaseActivity extends AppCompatActivity {

    public static final String TAG = AppContracts.TAG + "-Activity";

    protected FragmentManager mFragmentManager;
    protected XSXApplication mApplication;

    protected LoadingDialog loadingDialog;

    protected Realm realm;
    protected boolean isEnableRightSlide = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm=Realm.getDefaultInstance();

        mFragmentManager = getSupportFragmentManager();
        mApplication = (XSXApplication) getApplication();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        loadingDialog = new LoadingDialog(this);
        /*
        **describe:默认隐藏标题栏
        */
        hideTitle(true);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

        setDefaultRightSlide();

    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    protected void hideTitle(boolean hideTitle) {
        if (!hideTitle) {
            getSupportActionBar().show();
        } else {
            getSupportActionBar().hide();
        }

    }

    /*
    **describe:显示LoadingDialog
    */
    public void showLoadingDialog(String text) {
        loadingDialog.setLoadText(text);
        loadingDialog.show();
        WindowManager.LayoutParams lp = loadingDialog.getWindow().getAttributes();
        lp.width = getResources().getDimensionPixelSize(R.dimen.x360); //设置宽度
        lp.height = getResources().getDimensionPixelSize(R.dimen.y360); //设置宽度
        loadingDialog.getWindow().setAttributes(lp);
    }

    /*
    **describe:关闭LoadingDialog
    */
    public void hideLoadingDialog() {
        loadingDialog.dismiss();
    }

    /*
    **describe:设置关闭LoadingDialog时的回调
    */
    public void setonDismiss(LoadingDialog.onDismiss on) {
        loadingDialog.setOnDismiss(on);
    }

    /*
    **describe:Toast
    */
    public void showToast(String toast){
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    public Realm getRealm() {
        return realm;
    }


    //手指上下滑动时的最小速度
    private static final int YSPEED_MIN = 1000;

    //手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 150;

    //手指向上滑或下滑时的最小距离
    private static final int YDISTANCE_MIN = 100;

    //记录手指按下时的横坐标。
    private float xDown;

    //记录手指按下时的纵坐标。
    private float yDown;

    //记录手指移动时的横坐标。
    private float xMove;

    //记录手指移动时的纵坐标。
    private float yMove;

    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;

    protected RightSlide rightSlide;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                yDown = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                yMove = event.getRawY();
//                //滑动的距离
//                int distanceX = (int) (xMove - xDown);
//                int distanceY = (int) (yMove - yDown);
//                //获取顺时速度
//                int ySpeed = getScrollVelocity();
//                //关闭Activity需满足以下条件：
//                //1.x轴滑动的距离>XDISTANCE_MIN
//                //2.y轴滑动的距离在YDISTANCE_MIN范围内
//                //3.y轴上（即上下滑动的速度）<XSPEED_MIN，如果大于，则认为用户意图是在上下滑动而非左滑结束Activity
//                if (distanceX > XDISTANCE_MIN && (distanceY < YDISTANCE_MIN && distanceY > -YDISTANCE_MIN) && ySpeed < YSPEED_MIN) {
//                    if (rightSlide != null) {
//                        return rightSlide.rightSlide();
//                    }
//                }
                break;
            case MotionEvent.ACTION_UP:
                //滑动的距离
                int distanceX = (int) (xMove - xDown);
                int distanceY = (int) (yMove - yDown);
                //获取顺时速度
                int ySpeed = getScrollVelocity();
                //关闭Activity需满足以下条件：
                //1.x轴滑动的距离>XDISTANCE_MIN
                //2.y轴滑动的距离在YDISTANCE_MIN范围内
                //3.y轴上（即上下滑动的速度）<XSPEED_MIN，如果大于，则认为用户意图是在上下滑动而非左滑结束Activity
                if (distanceX > XDISTANCE_MIN && (distanceY < YDISTANCE_MIN && distanceY > -YDISTANCE_MIN) && ySpeed < YSPEED_MIN) {
                    if (rightSlide != null) {
                        return rightSlide.rightSlide();
                    }
                }
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 创建VelocityTracker对象，并将触摸界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getYVelocity();
        return Math.abs(velocity);
    }

    public void setRightSlide(RightSlide rightSlide) {
        if (isEnableRightSlide) {
            this.rightSlide = rightSlide;
        }
    }

    /*
    **describe:设置默认右滑关闭当前activity
    */
    public void setDefaultRightSlide() {
        setRightSlide(new RightSlide() {
            @Override
            public boolean rightSlide() {
                finish();
                return true;
            }
        });
    }

    public void clearRightSlide() {
        setRightSlide(null);
    }

    public interface RightSlide {
        boolean rightSlide();
    }

    public boolean isEnableRightSlide() {
        return isEnableRightSlide;
    }

    public void setEnableRightSlide(boolean enableRightSlide) {
        clearRightSlide();
        isEnableRightSlide = enableRightSlide;
    }
}
