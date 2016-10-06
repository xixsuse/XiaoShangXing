package com.xiaoshangxing.setting.personalinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.personalinfo.MyCode.MyCodeFragment;
import com.xiaoshangxing.setting.personalinfo.QianMing.QianMingFragment;
import com.xiaoshangxing.setting.personalinfo.TagView.TagViewActivity;
import com.xiaoshangxing.setting.personalinfo.hometown.HometownFragment;
import com.xiaoshangxing.setting.personalinfo.personalinfo.PersonalInfoFragment;
import com.xiaoshangxing.setting.personalinfo.showheadimg.ShowHeadimgFragment;
import com.xiaoshangxing.setting.shiming.ShenheActivity;
import com.xiaoshangxing.setting.shiming.result.VertifyFailedActivity;
import com.xiaoshangxing.setting.shiming.result.VertifySucessActivity;
import com.xiaoshangxing.utils.BaseActivity;

/**
 * Created by tianyang on 2016/7/9.
 */
public class PersonalInfoActivity extends BaseActivity {
    private int imagCoverWidth, imagCoverHeight;
    public static boolean isbacked = false;
    private boolean isVertified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_personinfo);
        mFragmentManager.beginTransaction()
                .add(R.id.setting_personinfo_Content, new PersonalInfoFragment(), PersonalInfoFragment.TAG)
                .commit();
    }


    public void ShowHeadimg(View view) {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .addToBackStack(ShowHeadimgFragment.TAG)
                .replace(R.id.setting_personinfo_Content, new ShowHeadimgFragment(), ShowHeadimgFragment.TAG)
                .commit();
    }

    public void MyCode(View view) {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .addToBackStack(null)
                .replace(R.id.setting_personinfo_Content, new MyCodeFragment())
                .commit();
    }

    public void ChooseHometown(View view) {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .addToBackStack(null)
                .replace(R.id.setting_personinfo_Content, new HometownFragment())
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
                .replace(R.id.setting_personinfo_Content, new QianMingFragment())
                .commit();
    }


    public void Vertify(View view) {
 /*       mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .addToBackStack(VertifyFragment.TAG)
                .replace(R.id.setting_personinfo_Content, new VertifyFragment(), VertifyFragment.TAG)
                .commit();*/

        //尚未认证
        startActivity(new Intent(PersonalInfoActivity.this, ShenheActivity.class));
        //认证成功
//        startActivity(new Intent(PersonalInfoActivity.this,VertifySucessActivity.class));
        //认证失败
//        startActivity(new Intent(PersonalInfoActivity.this,VertifyFailedActivity.class));
    }

    public void back(View view) {
        finish();
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
