package com.xiaoshangxing.publicActivity.album;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xiaoshangxing.R;
import com.xiaoshangxing.publicActivity.inputActivity.InputActivity;
import com.xiaoshangxing.publicActivity.check_photo.inputSelectPhotoPagerActivity;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImagePagerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AlbumDetailAdapter extends BaseAdapter {

    /**
     * 上下文
     */
    private Context context;
    private List<String> select_image_urls = new ArrayList<String>();
    private ImageBucket imageBucket;
    private int limit;
    private List<String> image_urls = new ArrayList<String>();
    private AlbumDetailFragment fragment;
    private AlbumActivity activity;

    public AlbumDetailAdapter(Context ctx, int limit, List<String> select_image_urls,
                              ImageBucket imageBucket, AlbumDetailFragment fragment, AlbumActivity activity) {
        this.context = ctx;
        this.limit = limit;
        this.select_image_urls = select_image_urls;
        this.imageBucket = imageBucket;
        this.fragment = fragment;
        this.activity = activity;
        for (int i = 0; i < imageBucket.imageList.size(); i++) {
            image_urls.add(imageBucket.imageList.get(i).imagePath);
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return image_urls.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return image_urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_imageview_336, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ViewHolder finalViewHolder = viewHolder;

        Glide.with(context).
                load(image_urls.get(position))
                .animate(R.anim.fade_in)
                .into(viewHolder.ivImage);

        viewHolder.checkboxLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalViewHolder.checkbox.performClick();
            }
        });

        viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalViewHolder.checkbox.isChecked()) {
                    if (select_image_urls.size() >= limit) {
                        Toast.makeText(context, "最多只能选择" + limit + "张图片", Toast.LENGTH_SHORT).show();
                        finalViewHolder.checkbox.setChecked(false);
                    } else {
                        select_image_urls.add(image_urls.get(position));
                    }

                } else {
                    if (select_image_urls.contains(image_urls.get(position))) {
                        select_image_urls.remove(image_urls.get(position));
                    }
                }
                fragment.setSelectCount(select_image_urls.size());
            }
        });

        if (select_image_urls.contains(image_urls.get(position))) {
            finalViewHolder.checkbox.setChecked(true);
        } else {
            finalViewHolder.checkbox.setChecked(false);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, inputSelectPhotoPagerActivity.class);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) image_urls);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                intent.putExtra(inputSelectPhotoPagerActivity.SELECT_PICTURE, (ArrayList<String>) select_image_urls);
                intent.putExtra(inputSelectPhotoPagerActivity.LIMIT, limit);
                activity.startActivityForResult(intent, InputActivity.SELECT_PHOTO_ONE_BY_ONE);
            }
        });

        return convertView;
    }

    public List<String> getSelect_image_urls() {
        return select_image_urls;
    }

    public void setSelect_image_urls(List<String> select_image_urls) {
        this.select_image_urls = select_image_urls;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @Bind(R.id.iv_image)
        ImageView ivImage;
        @Bind(R.id.checkbox)
        CheckBox checkbox;
        @Bind(R.id.checkbox_lay)
        FrameLayout checkboxLay;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
