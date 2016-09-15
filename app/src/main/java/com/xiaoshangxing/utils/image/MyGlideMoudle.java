package com.xiaoshangxing.utils.image;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.module.GlideModule;
import com.xiaoshangxing.utils.FileUtils;

import java.io.File;

/**
 * Created by FengChaoQun
 * on 2016/9/14
 */
public class MyGlideMoudle implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                File file = new File(FileUtils.getGlideCache());
                if (!file.exists()) {
                    file.mkdirs();
                }
                return DiskLruCacheWrapper.get(file, 300 * FileUtils.MB);
            }
        });
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
