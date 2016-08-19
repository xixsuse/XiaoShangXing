package com.xiaoshangxing.xiaoshang.idlesale.SelectImageFromSd.AllPhotoActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.normalUtils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quchwe on 2016/8/6 0006.
 */
public class ImageSelectAdapter extends RecyclerView.Adapter<ImageSelectAdapter.ImageSelectViewHoler> {

    List<String> paths;
    private final Context mContext;
    private final int limitCount = 3;

    private   ArrayList<String> mSelectedImage = new ArrayList<>();
    private int pos = -1;
    private String currentSelectPath = null;
    ArrayList<String> selectedImages = new ArrayList<>();
    public ImageSelectAdapter(Context context,List<String> paths,ArrayList<String> selectedList){
        this.mContext = context;
        this.paths = paths;
        this.selectedImages = selectedList;

    }
    @Override
    public ImageSelectViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageSelectViewHoler viewHoler = new ImageSelectViewHoler(LayoutInflater.from(mContext).
                inflate(R.layout.rv_idle_select_image_,parent,false));
        return viewHoler;
    }

    @Override
    public void onBindViewHolder(final ImageSelectViewHoler holder, final int position) {
        final String currentPath = paths.get(position);
        Glide.with(mContext)
                .load(currentPath)
                .into(holder.image);
        if (selectedImages!=null){
            mSelectedImage = selectedImages;
        }
        if (selectedImages!=null&&selectedImages.contains(currentPath)) {
            holder.image.setColorFilter(Color.parseColor("#77000000"));
            holder.selectBox.setChecked(true);
        }else {
            holder.image.setColorFilter(null);
            holder.selectBox.setChecked(false);
        }
       final AllIPhotosActivity activity = (AllIPhotosActivity)mContext;

        holder.selectBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.selectBox.isChecked()) {
                    // 已经选择过该图片
                    if (mSelectedImage.contains(currentPath)) {
                        mSelectedImage.remove(currentPath);
                        holder.selectBox.setChecked(false);
                        holder.image.setColorFilter(null);
                    } else
                    // 未选择该图片
                    {
                        if (mSelectedImage.size() == limitCount) {
                            Toast.makeText(mContext, "只能三张照片", Toast.LENGTH_SHORT).show();
                            holder.selectBox.setChecked(false);
                            return;
                        }
                        mSelectedImage.add(currentPath);
                        holder.selectBox.setChecked(true);
                        holder.image.setColorFilter(Color.parseColor("#77000000"));
                    }
                    activity.setPicteureSelectCount(mSelectedImage.size());
                    currentSelectPath = currentPath;
                    pos = position;
                    if (mSelectedImage.size()>0){
                        activity.setPreview(true);

                    }else {
                        activity.setPreview(false);
                    }
                    activity.setSelectedPos(pos,mSelectedImage.size()-1);
                    activity.setSlectPicturePath(mSelectedImage);

                }
                selectedImages = mSelectedImage;
            }
        });
        //设置ImageView的点击事件
       // holder.image.setFocusable(false);
        holder.image.setOnClickListener(new View.OnClickListener()
        {
            //选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v)
            {

//
//                // 已经选择过该图片
//                if (mSelectedImage.contains(currentPath))
//                {
//                    mSelectedImage.remove(currentPath);
//                    holder.selectBox.setChecked(false);
//                   holder.image.setColorFilter(null);
//                } else
//                // 未选择该图片
//                {
//                    if (mSelectedImage.size()==3){
//                        ToastUtil.show(mContext,"你只能三张照片",ToastUtil.LENGTH_SHORT);
//                        return;
//                    }
//                    mSelectedImage.add(currentPath);
//                   holder.selectBox.setChecked(true);
//                    holder.image.setColorFilter(Color.parseColor("#77000000"));
//                }
//                activity.setPicteureSelectCount(mSelectedImage.size());
//                currentSelectPath = currentPath;
//                currentAllSlectedPos = position;

               activity.toPreview();
            }
        });

    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    public List<String> getmSelectedImage(){
        Log.d("adapter mSelectedImage",mSelectedImage.size()+"");
        return this.mSelectedImage;
    }
    public String getCurrentSelect(){
        return this.currentSelectPath;
    }
    class ImageSelectViewHoler extends RecyclerView.ViewHolder{
        public ImageView image;
        public CheckBox selectBox;
        public ImageSelectViewHoler(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.iv_image);
            selectBox = (CheckBox)itemView.findViewById(R.id.checkbox);
        }
    }

    public int getPosition() {
        return pos;
    }

    public void setSelectedImages(List<String> images){
        selectedImages = (ArrayList)images;
    }
}
