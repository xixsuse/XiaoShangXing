package com.xiaoshangxing.wo.setting.personalinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.wo.setting.personalinfo.MyCode.MyCodeFragment;
import com.xiaoshangxing.wo.setting.personalinfo.QianMing.QianMingFragment;
import com.xiaoshangxing.wo.setting.personalinfo.TagView.TagViewActivity;
import com.xiaoshangxing.wo.setting.personalinfo.hometown.HometownFragment;
import com.xiaoshangxing.wo.setting.personalinfo.personalinfo.PersonalInfoFragment;
import com.xiaoshangxing.wo.setting.personalinfo.showheadimg.ShowHeadimgFragment;
import com.xiaoshangxing.wo.setting.shiming.result.VertifySucessActivity;
import com.xiaoshangxing.wo.setting.shiming.result.VertifyingActivity;
import com.xiaoshangxing.wo.setting.shiming.shenhe.XueShengZhenActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;

/**
 * Created by tianyang on 2016/7/9.
 */
public class PersonalInfoActivity extends BaseActivity {
    public static boolean isbacked = false;
    private int imagCoverWidth, imagCoverHeight;
    private boolean isVertified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_fraglayout);
        mFragmentManager.beginTransaction()
                .add(R.id.main_fragment, new PersonalInfoFragment(), PersonalInfoFragment.TAG)
                .commit();
    }


    public void ShowHeadimg(View view) {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .addToBackStack(ShowHeadimgFragment.TAG)
                .replace(R.id.main_fragment, new ShowHeadimgFragment(), ShowHeadimgFragment.TAG)
                .commit();
    }

    public void MyCode(View view) {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .addToBackStack(null)
                .replace(R.id.main_fragment, new MyCodeFragment())
                .commit();
    }

    public void ChooseHometown(View view) {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .addToBackStack(null)
                .replace(R.id.main_fragment, new HometownFragment())
                .commit();
    }


    public void TagView(View view) {
        Intent intent = new Intent(this, TagViewActivity.class);
        startActivity(intent);
    }

    public void QianName(View view) {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .addToBackStack(null)
                .replace(R.id.main_fragment, new QianMingFragment())
                .commit();
    }


    public void Vertify(View view) {
        //尚未认证
        NimUserInfo nimUserInfo = NimUserInfoCache.getInstance().getUserInfo(TempUser.getId());
        if (nimUserInfo == null) {
            showToast("账号异常,请重新登录");
            return;
        }
        if (nimUserInfo.getExtensionMap().get("isActive") != null) {
            switch ((int) nimUserInfo.getExtensionMap().get("isActive")) {
                case 0:
                    startActivity(new Intent(PersonalInfoActivity.this, XueShengZhenActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(PersonalInfoActivity.this, VertifySucessActivity.class));
//                    startActivity(new Intent(PersonalInfoActivity.this, XueShengZhenActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(PersonalInfoActivity.this, VertifyingActivity.class));
//                    startActivity(new Intent(PersonalInfoActivity.this, XueShengZhenActivity.class));
                    break;
                case 3:
                    Intent intent = new Intent(PersonalInfoActivity.this, XueShengZhenActivity.class);
                    intent.putExtra(IntentStatic.TYPE, true);
                    startActivity(intent);
                    break;
            }
        }
    }


    public int getImagCoverWidth() {
        return imagCoverWidth;
    }

    public void setImagCoverWidth(int imagCoverWidth) {
        this.imagCoverWidth = imagCoverWidth;
    }

    public int getImagCoverHeight() {
        return imagCoverHeight;
    }

    public void setImagCoverHeight(int imagCoverHeight) {
        this.imagCoverHeight = imagCoverHeight;
    }

    public void setIsbacked(boolean isbacked) {
        this.isbacked = isbacked;
    }

    public void setVertified(boolean vertified) {
        isVertified = vertified;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = mFragmentManager.findFragmentByTag(ShowHeadimgFragment.TAG);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        TagViewFragment fragment = (TagViewFragment) mFragmentManager.findFragmentByTag(TagViewFragment.TAG);
//        if (isbacked) {
//            fragment.onKeyDown(keyCode, event);
//            isbacked = false;
//            return true;
//        }

/*        VertifySucessFragment fragment1 = (VertifySucessFragment) mFragmentManager.findFragmentByTag(VertifySucessFragment.TAG);
        if (isVertified) {
            fragment1.onKeyDown(keyCode, event);
            isVertified = false;
            return true;
        }*/
        return super.onKeyDown(keyCode, event);
    }


}
