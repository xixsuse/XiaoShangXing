package com.xiaoshangxing.xiaoshang.Calendar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.layout.CirecleImage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class Calendar_adpter extends ArrayAdapter<Published> {
    private Context context;
    List<Published> publisheds;

    public Calendar_adpter(Context context, int resource, List<Published> objects) {
        super(context, resource, objects);
        this.context = context;
        this.publisheds = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_calendar_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.circle)
        CirecleImage circle;
        @Bind(R.id.text)
        EmotinText text;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
