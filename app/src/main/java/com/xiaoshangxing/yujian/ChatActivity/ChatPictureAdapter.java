package com.xiaoshangxing.yujian.ChatActivity;

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
import com.xiaoshangxing.publicActivity.check_photo.inputSelectPhotoPagerActivity;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImagePagerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/7/25
 */
public class ChatPictureAdapter extends RecyclerView.Adapter<ChatPictureAdapter.MyViewHolder> {
    private Context context;
    private List<String> list = new ArrayList<String>();
    private List<String> select_image_urls = new ArrayList<String>();
    private int limit = 9;
    private InputPanel inputPanel;

    public ChatPictureAdapter(Context context, List<String> list, InputPanel inputPanel) {
        this.context = context;
        this.list = list;
        this.inputPanel = inputPanel;
        select_image_urls = inputPanel.getSelect_image_urls();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_picture_recycleview, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.view = view;
        viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        viewHolder.imageView = (ImageView) view.findViewById(R.id.iamge);
        viewHolder.CheckBox_lay = view.findViewById(R.id.checkbox_lay);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Glide.with(context).
                load(list.get(position))
                .animate(R.anim.fade_in)
                .into(holder.imageView);

        if (select_image_urls.contains(list.get(position))) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.CheckBox_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox.performClick();
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    if (select_image_urls.size() >= limit) {
                        Toast.makeText(context, "最多只能选择" + limit + "张图片", Toast.LENGTH_SHORT).show();
                        holder.checkBox.setChecked(false);
                    } else {
                        select_image_urls.add(list.get(position));
                    }

                } else {
                    if (select_image_urls.contains(list.get(position))) {
                        select_image_urls.remove(list.get(position));
                    }
                }
                inputPanel.setSelectCount(getSelectCount());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, inputSelectPhotoPagerActivity.class);
                ArrayList<String> urls = (ArrayList<String>) list;
                ArrayList<String> selected = (ArrayList<String>) select_image_urls;
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                intent.putExtra(inputSelectPhotoPagerActivity.SELECT_PICTURE, selected);
                intent.putExtra(inputSelectPhotoPagerActivity.LIMIT, limit);
                inputPanel.getActivity().startActivityForResult(intent, inputSelectPhotoPagerActivity.REQUEST_CODE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public int getSelectCount() {
        return select_image_urls.size();
    }

    public List<String> getSelect_image_urls() {
        return select_image_urls;
    }

    public void setSelect_image_urls(List<String> select_image_urls) {
        this.select_image_urls = select_image_urls;
        notifyDataSetChanged();
        inputPanel.setSelectCount(getSelectCount());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
        View view, CheckBox_lay;

        public MyViewHolder(View view) {
            super(view);
        }
    }


}
