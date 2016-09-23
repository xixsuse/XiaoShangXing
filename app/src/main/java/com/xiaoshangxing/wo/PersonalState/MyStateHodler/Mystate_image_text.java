package com.xiaoshangxing.wo.PersonalState.MyStateHodler;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.image.MyGlide;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImagePagerActivity;
import com.xiaoshangxing.wo.PersonalState.check_photo.myStateImagePagerActivity;

/**
 * Created by FengChaoQun
 * on 2016/9/11
 */
public class Mystate_image_text extends MyStateHodlerBase {
    private ImageView first, second, third, four;
    private TextView text, count;
    private View first_group,second_group;

    @Override
    public void inflate() {
        View.inflate(view.getContext(), R.layout.mystate_hodler_image_text, content);
        first = (ImageView) view.findViewById(R.id.first_image);
        second = (ImageView) view.findViewById(R.id.sencond_image);
        third = (ImageView) view.findViewById(R.id.third_image);
        four = (ImageView) view.findViewById(R.id.forth_image);
        text = (TextView) view.findViewById(R.id.image_text);
        count = (TextView) view.findViewById(R.id.image_count);
        first_group=view.findViewById(R.id.first_group);
        second_group=view.findViewById(R.id.sencond_group);
    }

    @Override
    public void refresh(final Published published) {
        setPublished(published);
        refreshBase();
        text.setText(TextUtils.isEmpty(published.getText())?"":published.getText());
        first.setVisibility(View.GONE);
        second.setVisibility(View.GONE);
        third.setVisibility(View.GONE);
        four.setVisibility(View.GONE);
        first_group.setVisibility(View.GONE);
        second_group.setVisibility(View.GONE);

        final String[] paths = published.getImage().split(NS.SPLIT);
        count.setText("共" + paths.length + "张");
        for (int i = 0; i < paths.length; i++) {

            if (i > 3) {
                break;
            }

            switch (i) {
                case 0:
                    MyGlide.with(context, paths[i], first);
                    first.setVisibility(View.VISIBLE);
                    first_group.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    MyGlide.with(context, paths[i], second);
                    second.setVisibility(View.VISIBLE);
                    second_group.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    MyGlide.with(context, paths[i], third);
                    third.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    MyGlide.with(context, paths[i], four);
                    four.setVisibility(View.VISIBLE);
                    break;
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, myStateImagePagerActivity.class);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, paths);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                intent.putExtra(IntentStatic.DATA,published.getId());
                context.startActivity(intent);
            }
        });
    }
}
