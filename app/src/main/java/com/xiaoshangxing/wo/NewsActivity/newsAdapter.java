package com.xiaoshangxing.wo.NewsActivity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.data.bean.PushMsg;
import com.xiaoshangxing.utils.customView.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.NotifycationUtil;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.utils.customView.Name;
import com.xiaoshangxing.utils.imageUtils.MyGlide;
import com.xiaoshangxing.wo.StateDetailsActivity.DetailsActivity;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/7/11
 */
public class newsAdapter extends ArrayAdapter<PushMsg> {
    private Context context;
    private List<PushMsg> pushMsgs;

    public newsAdapter(Context context, int resource, List<PushMsg> pushMsgs) {
        super(context, resource, pushMsgs);
        this.context = context;
        this.pushMsgs = pushMsgs;
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

        final PushMsg pushMsg = pushMsgs.get(position);
        UserInfoCache.getInstance().getHeadIntoImage(pushMsg.getUserId(), viewHolder.headImage);
        viewHolder.name.setText(pushMsg.getUserName());
        viewHolder.time.setText(TimeUtil.getTimeShowString(pushMsg.getPushTime(), false));

        if (pushMsg.getPushType().equals(NotifycationUtil.NT_SCHOOLMATE_NOTICE_YOU)) {
            viewHolder.praise.setVisibility(View.GONE);
            viewHolder.commentText.setVisibility(View.VISIBLE);
            viewHolder.commentText.setText("同时提到了你");
        } else if (pushMsg.getOperationType().equals(NotifycationUtil.OPERATION_PRAISE)) {
            viewHolder.praise.setVisibility(View.VISIBLE);
            viewHolder.commentText.setVisibility(View.GONE);
        } else if (pushMsg.getOperationType().equals(NotifycationUtil.OPERATION_COMMENT)) {
            viewHolder.praise.setVisibility(View.GONE);
            viewHolder.commentText.setVisibility(View.VISIBLE);
            viewHolder.commentText.setText(pushMsg.getText());
        }

        if (TextUtils.isEmpty(pushMsg.getImages())) {
            viewHolder.image.setVisibility(View.GONE);
            viewHolder.text.setVisibility(View.VISIBLE);
            viewHolder.text.setText(pushMsg.getMomentText());
        } else {
            viewHolder.image.setVisibility(View.VISIBLE);
            viewHolder.text.setVisibility(View.GONE);
            String path = pushMsg.getImages().split(NS.SPLIT)[0];
            MyGlide.with_defaul_image(context, path, viewHolder.image);
        }


        viewHolder.headImage.setIntent_type(CirecleImage.PERSON_INFO, pushMsg.getUserId());
        viewHolder.name.setIntent_type(Name.PERSON_INFO, pushMsg.getUserId());
        viewHolder.rightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(IntentStatic.DATA, Integer.valueOf(pushMsg.getMomentId()));
                context.startActivity(intent);
            }
        });


        return convertView;
    }

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
