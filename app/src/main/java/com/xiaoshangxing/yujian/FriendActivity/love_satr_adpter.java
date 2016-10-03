package com.xiaoshangxing.yujian.FriendActivity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.yujian.ChatActivity.ChatActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class love_satr_adpter extends ArrayAdapter<String> {
    private Context context;
    List<String> strings;
    private int type;

    public love_satr_adpter(Context context, int resource, List<String> objects,int type) {
        super(context, resource, objects);
        this.context = context;
        this.strings = objects;
        this.type=type;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_love_star_listview, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        if (type == LoveOrStartActivity.LOVE) {
            viewHolder.loveLay.setVisibility(View.VISIBLE);
            viewHolder.starLay.setVisibility(View.GONE);
            viewHolder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatActivity.start(context,"888888",null, SessionTypeEnum.P2P);
                }
            });
        }else {
            viewHolder.loveLay.setVisibility(View.GONE);
            viewHolder.starLay.setVisibility(View.VISIBLE);
            viewHolder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "你们成为了好友啦", Toast.LENGTH_SHORT).show();
                }
            });
        }

        viewHolder.headImage.setIntent_type(CirecleImage.PERSON_INFO, "17768345313");

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.head_image)
        CirecleImage headImage;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.sex)
        ImageView sex;
        @Bind(R.id.college)
        TextView college;
        @Bind(R.id.love_lay)
        LinearLayout loveLay;
        @Bind(R.id.star_lay)
        LinearLayout starLay;
        @Bind(R.id.more)
        FrameLayout more;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
