package com.xiaoshangxing.xiaoshang.idlesale;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiaoshangxing.R;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImagePagerActivity;


import java.util.ArrayList;

/**
 * Created by quchwe on 2016/8/16 0016.
 */

public class IdleImageAdapter extends RecyclerView.Adapter<IdleImageAdapter.ImageViewHolder>{

    final ArrayList<String> bitmaps;
    final Context mContext;
    private final int resourceId;

    public IdleImageAdapter(Context context,ArrayList<String> bitmaps,int resId){
        this.mContext = context;
        this.bitmaps = bitmaps;
        this.resourceId = resId;
    }


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageViewHolder viewHolder = new ImageViewHolder(LayoutInflater.from(mContext).inflate(resourceId,parent,false));
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {


//            if (bitmaps.size()==1){
//                //holder.image.setImageResource(R.mipmap.sunluyang);
//                ViewGroup.LayoutParams params = holder.image.getLayoutParams();
//                params.height = R.dimen.y276;
//                params.width = R.dimen.x278;
//                holder.image.setLayoutParams(params);
//            }
//            else {
//                ViewGroup.LayoutParams params = holder.image.getLayoutParams();
//                params.height =276;
//                params.width = 276;
//                holder.image.setLayoutParams(params);
//            }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImagePagerActivity.class);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, bitmaps);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                mContext.startActivity(intent);
            }
        });
        Glide.with(mContext).
                load(bitmaps.get(position))
                .animate(R.anim.fade_in)
                .into(holder.image);
    }



    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        public ImageView image;
        public ImageViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.iv_image_idle);
        }
    }

}