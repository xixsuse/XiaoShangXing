package com.xiaoshangxing.yujian.groupchatInfo.groupMembers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.yujian.groupchatInfo.Member;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/8/13.
 */
public class GroupMembersActivity extends BaseActivity {
    @Bind(R.id.groupMembers_listView)
    ListView listView;

    private BaseAdapter baseAdapter;
    private List<Member> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_groupmembers);
        ButterKnife.bind(this);
        init();


    }

    private void init() {

        for (int i = 0; i < 6; i++) {
            Member member = new Member();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.greyblock);
            member.setBitmap(bitmap);
            member.setName("姓名");
            data.add(member);
        }

        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return data.size();
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
                    convertView = getLayoutInflater().inflate(R.layout.item_setting_blacklist, parent, false);
                    holder = new ViewHolder();
                    holder.img = (ImageView) convertView.findViewById(R.id.blacklist_img);
                    holder.name = (TextView) convertView.findViewById(R.id.blacklist_text);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.img.setImageBitmap(data.get(position).getBitmap());
                holder.name.setText(data.get(position).getName());
                return convertView;
            }
        };

        listView.setAdapter(baseAdapter);


    }
    class ViewHolder {
        public ImageView img;
        public TextView name;
    }


    @OnClick({R.id.groupMembers_back, R.id.groupMembers_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.groupMembers_back:
                finish();
                break;
            case R.id.groupMembers_add:
                Intent intent = new Intent(this, SelectPersonActivity.class);
                startActivity(intent);
                break;
        }
    }
}
