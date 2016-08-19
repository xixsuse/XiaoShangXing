package com.xiaoshangxing.xiaoshang.idlesale.SelectImageFromSd.ImageSelectActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.utils.photo_choosing.ImageBucket;
import com.xiaoshangxing.setting.utils.photo_choosing.ImageItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by quchwe on 2016/8/12 0012.
 */

public class ImageDirectoryAdapter extends RecyclerView.Adapter<ImageDirectoryAdapter.ImageDirectoryViewHolder> {


    private final Context mContext;

    private List<ImageBucket> imageBuckets;

    public ImageDirectoryAdapter(Context context,List<ImageBucket> imageBuckets){
        this.mContext = context;
        this.imageBuckets = imageBuckets;
    }

    @Override
    public ImageDirectoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageDirectoryViewHolder holder = new ImageDirectoryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.idle_album_dirlist,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageDirectoryViewHolder holder, int position) {
        final ImageBucket imageBucket = imageBuckets.get(position);

        holder.directoryName.setText(imageBucket.bucketName+"("+imageBucket.imageList.size()+")");
        Glide.with(mContext)
                .load(imageBucket.imageList.get(0).getImagePath())
                .into(holder.firstIMage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDirectoryActivity activity = (ImageDirectoryActivity)mContext;
                ArrayList<String> paths = new ArrayList<String>();

                for (ImageItem item:imageBucket.imageList){
                    paths.add(item.getImagePath());
                }
                activity.toAllPhotoActivity(paths);
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageBuckets.size();
    }

    class ImageDirectoryViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.image)
        ImageView firstIMage;
        @Bind(R.id.name)
        TextView directoryName;

        public ImageDirectoryViewHolder(View itemView) {
            super(itemView);
            firstIMage = (ImageView) itemView.findViewById(R.id.image);
            directoryName = (TextView)itemView.findViewById(R.id.name);
        }
    }

}
