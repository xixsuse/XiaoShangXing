package com.xiaoshangxing.input_activity.EmotAndPicture;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.input_activity.check_photo.inputSelectPhotoPagerActivity;
import com.xiaoshangxing.wo.school_circle.check_photo.ImagePagerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/7/25
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.MyViewHolder> {
    private Context context;
    private List<String> list = new ArrayList<String>();
    private List<Integer> selectPosition=new ArrayList<Integer>();
    private List<String> selectPhoto=new ArrayList<String>();

    private InputActivity activity;

    public PictureAdapter(Context context, List<String> list,InputActivity activity) {
        this.context = context;
        this.list = list;
        this.activity=activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_picture_recycleview, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.view = view;
        viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        viewHolder.imageView = (ImageView) view.findViewById(R.id.iamge);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Glide.with(context).
                load(list.get(position))
                .animate(R.anim.fade_in)
                .into(holder.imageView);

        if (selectPosition.contains(position)){
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()){
                    if (selectPosition.size()>=9){
                        Toast.makeText(context, "最多只能选择九张图片", Toast.LENGTH_SHORT).show();
                        holder.checkBox.setChecked(false);
                    }else {
                        selectPosition.add(position);
                    }

                }else {
                    if (selectPosition.contains(position)){
                        selectPosition.remove(position);
                    }
                }
                activity.setSelectCount(getSelectCount());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,inputSelectPhotoPagerActivity.class);
                ArrayList<String> urls=(ArrayList<String>) list;
                ArrayList<Integer> selected=(ArrayList<Integer>) selectPosition;
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                intent.putExtra(inputSelectPhotoPagerActivity.SELECT_PICTURE, selected);
                activity.startActivityForResult(intent,InputActivity.SELECT_PHOTO_RESULT);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setSelectPosition(ArrayList<Integer> select){
        selectPosition=select;
        activity.setSelectCount(getSelectCount());
        notifyDataSetChanged();
    }

    public int getSelectCount(){
        return selectPosition.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
        View view;

        public MyViewHolder(View view) {
            super(view);
        }
    }
}
