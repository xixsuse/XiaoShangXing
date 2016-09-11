package com.xiaoshangxing.wo.WoFrafment.WoViewHolder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xiaoshangxing.Network.NS;
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

//    public WoJustOneImage(Context context, Published published, BaseActivity activity, Realm realm, WoFragment woFragment) {
//        super(context, published, activity, realm, woFragment);
//    }

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
                            justOne.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    context.getResources().getDimensionPixelSize(R.dimen.y600)));
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
