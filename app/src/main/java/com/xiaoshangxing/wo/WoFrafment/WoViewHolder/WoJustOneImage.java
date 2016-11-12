package com.xiaoshangxing.wo.WoFrafment.WoViewHolder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImagePagerActivity;

import java.util.ArrayList;

/**
 * Created by FengChaoQun
 * on 2016/9/10
 */
public class WoJustOneImage extends WoBaseHolder {

    private ImageView justOne;

    @Override
    public void inflate() {
        View.inflate(view.getContext(), R.layout.wo_viewhold_justone, content);
        justOne = (ImageView) view.findViewById(R.id.just_one);
    }

    @Override
    public void refresh(Published item) {

        setPublished(item);

        refreshBase();

        justOne.setImageResource(R.mipmap.greyblock);

        final String path = published.getImage().replace(NS.SPLIT, "");
        Glide.with(context)
                .load(path)
                .asBitmap()
                .placeholder(R.mipmap.greyblock)
                .error(R.mipmap.nim_image_download_failed)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (resource.getHeight() > context.getResources().getDimensionPixelSize(R.dimen.y600)) {
                            float width = resource.getWidth() * ((float) context.getResources().getDimensionPixelSize(R.dimen.y600) / resource.getHeight());
                            Log.d("width", "" + width);

                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) width,
                                    context.getResources().getDimensionPixelSize(R.dimen.y600));
                            params.gravity = Gravity.START;
                            justOne.setLayoutParams(params);
                        }
                        justOne.setImageBitmap(resource);
                    }
                });
        justOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImagePagerActivity.class);
                ArrayList<String> justone = new ArrayList<String>();
                justone.add(path);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, justone);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                context.startActivity(intent);
            }
        });
    }
}
