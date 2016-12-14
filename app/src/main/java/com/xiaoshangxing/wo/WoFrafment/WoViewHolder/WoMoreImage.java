package com.xiaoshangxing.wo.WoFrafment.WoViewHolder;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.wo.WoFrafment.NoScrollGridAdapter;
import com.xiaoshangxing.wo.WoFrafment.NoScrollGridView;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImagePagerActivity;

import java.util.ArrayList;

/**
 * Created by FengChaoQun
 * on 2016/9/10
 */
public class WoMoreImage extends WoBaseHolder {
    private NoScrollGridView noScrollGridView;

    @Override
    public void inflate() {
        View.inflate(view.getContext(), R.layout.wo_viewholder_more_image, content);
        noScrollGridView = (NoScrollGridView) view.findViewById(R.id.photos1);
    }

    @Override
    public void refresh(Published item) {

        setPublished(item);
        refreshBase();

        final ArrayList<String> imageUrls = new ArrayList<>();
        String[] splits = published.getImage().split(NS.SPLIT);
        for (String i : splits) {
            imageUrls.add(i);
        }

        noScrollGridView.setVisibility(View.VISIBLE);
        noScrollGridView.setAdapter(new NoScrollGridAdapter(context, imageUrls));
        noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ImagePagerActivity.class);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrls);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                context.startActivity(intent);
            }
        });

    }
}
