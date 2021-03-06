package com.xiaoshangxing.utils.imageUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xiaoshangxing.R;

/**
 * Created by FengChaoQun
 * on 2016/7/14
 */
public class MyGlide {

    public static void with(Activity activity, String url, ImageView view) {
        Glide.with(activity)
                .load(url)
                .placeholder(R.mipmap.greyblock)
                .error(R.mipmap.nim_image_download_failed)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void with(Fragment fragment, String url, ImageView view) {
        Glide.with(fragment)
                .load(url)
                .placeholder(R.mipmap.greyblock)
                .error(R.mipmap.nim_image_download_failed)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void with(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.greyblock)
                .error(R.mipmap.nim_image_download_failed)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void with_defaul_image(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.greyblock)
                .error(R.mipmap.nim_image_download_failed)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void with_app_log(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.cirecleimage_default)
                .error(R.mipmap.cirecleimage_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void with_default_head(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void with_default_college(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.icon_xueyuan_img)
                .error(R.mipmap.icon_xueyuan_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void withBitmap(Context context, String url, final ImageView view) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(R.mipmap.greyblock)
                .error(R.mipmap.nim_image_download_failed)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        view.setImageBitmap(resource);
                    }
                });
    }

}
