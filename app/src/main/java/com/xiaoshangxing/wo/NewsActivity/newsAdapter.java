package com.xiaoshangxing.wo.NewsActivity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.Name;

import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/7/11
 */
public class newsAdapter extends ArrayAdapter<String> {
    private Context context;

    public newsAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_news_listview, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Random random = new Random(NS.currentTime());
        int a = random.nextInt(100);
        switch (a % 3) {
            case 0:
                viewHolder.praise.setVisibility(View.VISIBLE);
                viewHolder.commentText.setVisibility(View.GONE);
                break;
            case 1:
                viewHolder.praise.setVisibility(View.GONE);
                viewHolder.commentText.setVisibility(View.VISIBLE);
                viewHolder.commentText.setText("哈哈哈哈哈哈哈");
                break;
            case 2:
                viewHolder.praise.setVisibility(View.GONE);
                viewHolder.commentText.setVisibility(View.VISIBLE);
                viewHolder.commentText.setText("同时提到了你");
                break;
        }

        switch (a % 2) {
            case 0:
                viewHolder.image.setVisibility(View.VISIBLE);
                viewHolder.text.setVisibility(View.GONE);
                break;
            case 1:
                viewHolder.image.setVisibility(View.GONE);
                viewHolder.text.setVisibility(View.VISIBLE);
                break;
        }

        return convertView;
    }

//    private void cleanView(viewHolder holder) {
//        holder.praise.setVisibility(View.GONE);
//        holder.comment_text.setVisibility(View.GONE);
//        holder.image.setVisibility(View.GONE);
//        holder.text.setVisibility(View.GONE);
//    }


    static class ViewHolder {
        @Bind(R.id.head_image)
        CirecleImage headImage;
        @Bind(R.id.name)
        Name name;
        @Bind(R.id.praise)
        ImageView praise;
        @Bind(R.id.comment_text)
        EmotinText commentText;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.text)
        EmotinText text;
        @Bind(R.id.right_layout)
        FrameLayout rightLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
