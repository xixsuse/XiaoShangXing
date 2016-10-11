package com.xiaoshangxing.yujian.Serch.SerchPerson;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.image.MyGlide;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.yujian.personInfo.PersonInfoActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class SerchPerson_Adpter extends ArrayAdapter<User> {
    private Context context;
    List<User> publisheds;

    public SerchPerson_Adpter(Context context, int resource, List<User> objects) {
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
        viewHolder.name.setText(user.getUsername());

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (user.getId() == TempUser.id) {
                            Toast.makeText(context, "这就是你自己啊...", Toast.LENGTH_SHORT).show();
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
