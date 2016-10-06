package com.xiaoshangxing.xiaoshang.Calendar.CalendarInputer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.utils.layout.CirecleImage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class CalendarInputer_Adpter extends ArrayAdapter<Published> {
    private Context context;
    List<Published> publisheds;

    public CalendarInputer_Adpter(Context context, int resource, List<Published> objects) {
        super(context, resource, objects);
        this.context = context;
        this.publisheds = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_calendar_inputer, null);
//            viewHolder = new ViewHolder(convertView);
//            convertView.setTag(viewHolder);
        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
        }

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
