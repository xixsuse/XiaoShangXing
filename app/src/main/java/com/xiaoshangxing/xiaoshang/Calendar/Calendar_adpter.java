package com.xiaoshangxing.xiaoshang.Calendar;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.CalendarData;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CirecleImage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class Calendar_adpter extends ArrayAdapter<CalendarData> {
    private Context context;
    List<CalendarData> publisheds;
    private int date_state;

    public Calendar_adpter(Context context, int resource, List<CalendarData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.publisheds = objects;

        if (objects.size() > 0) {
            CalendarData calendarData = objects.get(0);
            CalendarDay calendarDay = new CalendarDay(Integer.valueOf(calendarData.getYear()),
                    Integer.valueOf(calendarData.getMonth()), Integer.valueOf(calendarData.getDay()));
            if (calendarDay.isBefore(CalendarDay.today())) {
                date_state = -1;
            } else if (calendarDay.isAfter(CalendarDay.today())) {
                date_state = 1;
            } else {
                date_state = 0;
            }
        }

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

        final CalendarData calendarData = publisheds.get(position);

        viewHolder.text.setText(calendarData.getText());

        switch (date_state) {
            case -1:
                viewHolder.circle.setBackgroundColor(context.getResources().getColor(R.color.g2));
                break;
            case 0:
                viewHolder.circle.setBackgroundColor(context.getResources().getColor(R.color.green1));
                break;
            case 1:
                viewHolder.circle.setBackgroundColor(context.getResources().getColor(R.color.blue3));
                break;
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CalendarDetail.class);
                intent.putExtra(IntentStatic.DATA, calendarData.getMomentId());
                context.startActivity(intent);
            }
        });

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
