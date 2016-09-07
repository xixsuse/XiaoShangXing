package com.xiaoshangxing.yujian.personChatInfo;

import android.app.Activity;
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
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.yujian.groupchatInfo.Member;
import com.xiaoshangxing.yujian.groupchatInfo.deleteMember.DeleteMemberActivity;
import com.xiaoshangxing.yujian.personInfo.PersonInfoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyang on 2016/8/22.
 */
public class PersonalAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private Activity activity;
    List<Member> data;
    private String account;

    public PersonalAdapter(Context context, List<Member> data, Activity activity,String account) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.activity=activity;
        this.account=account;
    }

    @Override
    public int getCount() {
        return data.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
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
        if (position == getCount() - 1) {
            holder.img.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.privacy_add));
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SelectPersonActivity.class);
                    intent.putExtra(IntentStatic.TYPE,SelectPersonActivity.MY_FRIEND);
                    ArrayList<String> locked=new ArrayList<String>();
                    locked.add(account);
                    intent.putExtra(SelectPersonActivity.LOCKED,locked);
                    activity.startActivityForResult(intent,SelectPersonActivity.SELECT_PERSON_CODE);
                }
            });
        } else if (data.size() != 0) {
            holder.img.setImageBitmap(data.get(position).getBitmap());
            holder.name.setText(data.get(position).getName());
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PersonInfoActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        return convertView;
    }

    class ViewHolder {
        public CirecleImage img;
        public TextView name;
    }
}
