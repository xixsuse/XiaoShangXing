package com.xiaoshangxing.setting.personalinfo.showheadimg;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.photoview.PhotoViewAttacher;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/10/14
 */

public class HeadImageActivity extends BaseActivity {
    @Bind(R.id.image)
    ImageView image;

    private String account;
    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_image_detail);
        ButterKnife.bind(this);

        account = getIntent().getStringExtra(IntentStatic.EXTRA_ACCOUNT);
        if (TextUtils.isEmpty(account)) {
            showToast("账户信息不明");
            return;
        }

        String path = NimUserInfoCache.getInstance().getHeadImage(account);
        if (TextUtils.isEmpty(path)) {
            showToast("获取头像失败");
            finish();
            return;
        }

        mAttacher = new PhotoViewAttacher(image);

        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                finish();
            }
        });

        Glide.with(this).load(path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new GlideDrawableImageViewTarget(image) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        mAttacher.update();
                    }
                });


    }

    @Override
    protected void onPause() {
        super.onPause();
        image.destroyDrawingCache();
        mAttacher.cleanup();
    }
}
