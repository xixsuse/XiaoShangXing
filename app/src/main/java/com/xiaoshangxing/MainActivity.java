package com.xiaoshangxing;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.xiaoshangxing.input_activity.EmotAndPicture.EmotionGrideViewAdapter;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmoticonsEditText;
import com.xiaoshangxing.input_activity.InputBoxLayout;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.location.Bean;
import com.xiaoshangxing.utils.location.PoiSearchUtil;
import com.xiaoshangxing.utils.normalUtils.SPUtils;
import com.xiaoshangxing.wo.WoFrafment.WoFragment;
import com.xiaoshangxing.xiaoshang.XiaoShangFragment;
import com.xiaoshangxing.yujian.YujianFragment.YuJianFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/6/21
 */
public class MainActivity extends BaseActivity {
    public static final String TAG = BaseActivity.TAG + "-MainActivity";

    @Bind(R.id.image_xiaoshang)
    ImageView imageXiaoshang;
    @Bind(R.id.xiaoshang)
    TextView xiaoshang;
    @Bind(R.id.xiaoshang_lay)
    RelativeLayout xiaoshangLay;
    @Bind(R.id.image_yujian)
    ImageView imageYujian;
    @Bind(R.id.yujian)
    TextView yujian;
    @Bind(R.id.yujian_lay)
    RelativeLayout yujianLay;
    @Bind(R.id.image_wo)
    ImageView imageWo;
    @Bind(R.id.wo)
    TextView wo;
    @Bind(R.id.wolay)
    RelativeLayout wolay;
    @Bind(R.id.radio_layout)
    LinearLayout radioLayout;
    @Bind(R.id.xiaoshang_dot)
    CirecleImage xiaoshangDot;
    @Bind(R.id.yujian_dot)
    CirecleImage yujianDot;
    @Bind(R.id.wo_dot)
    CirecleImage woDot;

    private int current;

    private WoFragment woFragment;
    private XiaoShangFragment xiaoShangFragment;
    private YuJianFragment yuJianFragment;
    //    private GridView gridView;
//    private List<View> viewlist = new ArrayList<View>();
//    private ViewPager viewPager;
//    private LinearLayout emotion_lay;
//    private EmotionGrideViewAdapter adapter;
//
//    private RelativeLayout comment_input_layout;
//    private EmoticonsEditText emoticonsEditText;
//    private TextView send;
//    private ImageView emot;
//    private View normal_emot, favorite, delete_emot;
//    private RelativeLayout edit_and_emot;
//    private int screenHeight;
    private InputBoxLayout inputBoxLayout;


    private static final int BAIDU_READ_PHONE_STATE =1000;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);


//        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) !=PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(this, new String[]{
//                    Manifest.permission.READ_PHONE_STATE,
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.WRITE_SETTINGS,
//                    Manifest.permission.ACCESS_WIFI_STATE,
//                    Manifest.permission.ACCESS_NETWORK_STATE,
//                    Manifest.permission.CHANGE_WIFI_STATE
//            },BAIDU_READ_PHONE_STATE);
//        }
        getPersimmions();
        PoiSearchUtil.LocationUtil(this);

        initInputBox();
        initAllFragments();
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if(checkSelfPermission(Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.WRITE_SETTINGS);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
            }
            if(checkSelfPermission(Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.CHANGE_WIFI_STATE);
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 127);
            }
        }
    }

    private void initInputBox() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.edit_and_emot);
        inputBoxLayout = new InputBoxLayout(this, relativeLayout, this);
    }

    private void initAllFragments() {
        SPUtils.put(this, SPUtils.IS_FIRS_COME, false);//当这个页面打开时，表明不是第一次进入APP了
        SPUtils.put(this, SPUtils.IS_QUIT, false);//当这个页面打开时，清除退出记录
        Fragment frag;
        frag = mFragmentManager.findFragmentByTag(WoFragment.TAG);
        woFragment = (frag == null) ? WoFragment.newInstance() : (WoFragment) frag;

        frag = mFragmentManager.findFragmentByTag(XiaoShangFragment.TAG);
        xiaoShangFragment = (frag == null) ? XiaoShangFragment.newInstance() : (XiaoShangFragment) frag;

        frag = mFragmentManager.findFragmentByTag(yuJianFragment.TAG);
        yuJianFragment = (frag == null) ? yuJianFragment.newInstance() : (YuJianFragment) frag;

        if (!xiaoShangFragment.isAdded()) {
            mFragmentManager.beginTransaction().add(R.id.main_fragment, xiaoShangFragment, XiaoShangFragment.TAG)
                    .commit();
        }
        if (!woFragment.isAdded()) {
            mFragmentManager.beginTransaction().add(R.id.main_fragment, woFragment, WoFragment.TAG)
                    .commit();
        }

        if (!yuJianFragment.isAdded()) {
            mFragmentManager.beginTransaction().add(R.id.main_fragment, yuJianFragment, YuJianFragment.TAG)
                    .commit();
        }

        setXiaoshang(true);
//        setYUjian(true);
    }

    @OnClick({R.id.xiaoshang_lay, R.id.yujian_lay, R.id.wolay, R.id.emotion, R.id.normal_emot, R.id.favorite
            , R.id.send, R.id.delete_emot})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.xiaoshang_lay:
                if (current == 1) {
                    return;
                } else {
                    setXiaoshang(true);
                    setYUjian(false);
                    setWo(false);
                }

                break;
            case R.id.yujian_lay:
                if (current == 2) {
                    return;
                } else {
                    setXiaoshang(false);
                    setYUjian(true);
                    setWo(false);
                }
                break;
            case R.id.wolay:
                if (current == 3) {
                    return;
                } else {
                    setXiaoshang(false);
                    setYUjian(false);
                    setWo(true);
                }
                break;
//            case R.id.emotion:
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                break;
//            case R.id.normal_emot:
//                viewPager.setCurrentItem(0);
//                break;
//            case R.id.favorite:
//                viewPager.setCurrentItem(1);
//                break;
//            case R.id.send:
////                showOrHideLayout(false);
//                inputBoxLayout.showOrHideLayout(false);
//                emoticonsEditText.setText("");
//                break;
//            case R.id.delete_emot:
//                emoticonsEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
//                break;
        }
    }

    private void setWo(boolean is) {
        if (is) {
            imageWo.setImageResource(R.mipmap.wo_on);
            wo.setTextColor(getResources().getColor(R.color.green1));

//            mFragmentManager.beginTransaction().replace(R.id.main_fragment,
//                    woFragment, XiaoShangFragment.TAG).commit();
            mFragmentManager.beginTransaction().hide(xiaoShangFragment).hide(yuJianFragment).show(woFragment)
                    .commit();
            woFragment.autoRefresh();
            current = 3;
        } else {
            imageWo.setImageResource(R.mipmap.wo_off);
            wo.setTextColor(getResources().getColor(R.color.g0));
        }

    }

    private void setYUjian(boolean is) {
        if (is) {
            imageYujian.setImageResource(R.mipmap.yujian_on);
            yujian.setTextColor(getResources().getColor(R.color.green1));
            mFragmentManager.beginTransaction().hide(woFragment).hide(xiaoShangFragment).show(yuJianFragment)
                    .commit();
            current = 2;
        } else {
            imageYujian.setImageResource(R.mipmap.yujian_off);
            yujian.setTextColor(getResources().getColor(R.color.g0));
        }

    }

    private void setXiaoshang(boolean is) {
        if (is) {
            imageXiaoshang.setImageResource(R.mipmap.xiaoshang_on);
            xiaoshang.setTextColor(getResources().getColor(R.color.green1));

//            mFragmentManager.beginTransaction().replace(R.id.main_fragment,
//                    xiaoShangFragment, XiaoShangFragment.TAG).commit();
            mFragmentManager.beginTransaction().hide(woFragment).hide(yuJianFragment).show(xiaoShangFragment)
                    .commit();
            current = 1;
        } else {
            imageXiaoshang.setImageResource(R.mipmap.xiaoshang_off);
            xiaoshang.setTextColor(getResources().getColor(R.color.g0));
        }
    }

    public InputBoxLayout getInputBoxLayout() {
        return inputBoxLayout;
    }

    @Override
    public String toString() {
        return TAG;
    }
}
