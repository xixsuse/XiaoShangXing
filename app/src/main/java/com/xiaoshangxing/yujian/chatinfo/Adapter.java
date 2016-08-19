package com.xiaoshangxing.yujian.chatInfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.setting.utils.photo_choosing.RoundedImageView;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.yujian.chatInfo.deleteMember.DeleteMemberActivity;

import java.util.List;

/**
 * Created by 15828 on 2016/8/12.
 */
class Adapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    List<Member> data;

    public Adapter(Context context, List<Member> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size() + 2;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview_chatinfo, parent, false);
            holder = new ViewHolder();
            holder.img = (CirecleImage) convertView.findViewById(R.id.gridview_chatinfo_img);
            holder.name = (TextView) convertView.findViewById(R.id.gridview_chatinfo_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == getCount() - 2) {
            holder.img.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.privacy_add));
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SelectPersonActivity.class);
                    context.startActivity(intent);
                }
            });
        } else if (position == getCount() - 1) {
            holder.img.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.privacy_remove));
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DeleteMemberActivity.class);
                    context.startActivity(intent);
                }
            });
        } else if (data.size() != 0) {
            holder.img.setImageBitmap(data.get(position).getBitmap());
            holder.name.setText(data.get(position).getName());
        }

        return convertView;
    }

    class ViewHolder {
        public CirecleImage img;
        public TextView name;
    }


}
