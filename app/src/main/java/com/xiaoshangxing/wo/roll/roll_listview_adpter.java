package com.xiaoshangxing.wo.roll;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.utils.layout.CirecleImage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/7/12
 */
public class roll_listview_adpter extends ArrayAdapter<String> {
    private Context context;
    List<String> list;

    public roll_listview_adpter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        list = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_roll_listview, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            UserInfoCache.getInstance().getHeadIntoImage(list.get(position), viewHolder.headImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        UserInfoCache.getInstance().getName(viewHolder.name, Integer.valueOf(list.get(position)));

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.head_image)
        CirecleImage headImage;
        @Bind(R.id.name)
        TextView name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
