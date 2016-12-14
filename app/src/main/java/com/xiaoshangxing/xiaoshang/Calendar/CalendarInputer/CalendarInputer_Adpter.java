package com.xiaoshangxing.xiaoshang.Calendar.CalendarInputer;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.bean.User;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.utils.imageUtils.MyGlide;
import com.xiaoshangxing.xiaoshang.Calendar.CalendarInput;
import com.xiaoshangxing.yujian.personInfo.PersonInfoActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class CalendarInputer_Adpter extends ArrayAdapter<User> {
    List<User> publisheds;
    private Context context;

    public CalendarInputer_Adpter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        this.context = context;
        this.publisheds = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_calendar_inputer, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final User user = publisheds.get(position);
        MyGlide.with(context, user.getUserImage(), viewHolder.headImage);
        viewHolder.name.setText(user.getUsername() + "(" + user.getRole() + ")");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getId() == TempUser.id) {
                    Intent intent = new Intent(context, CalendarInput.class);
                    context.startActivity(intent);
                } else {
                    Intent state_intent = new Intent(context, PersonInfoActivity.class);
                    state_intent.putExtra(IntentStatic.EXTRA_ACCOUNT, String.valueOf(user.getId()));
                    context.startActivity(state_intent);
                }
            }
        });

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
