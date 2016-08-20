package com.xiaoshangxing.yujian.chatInfo.deleteMember;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.utils.ActionSheet;
import com.xiaoshangxing.setting.utils.photo_choosing.RoundedImageView;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.layout.CirecleImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/8/13.
 */
public class DeleteMemberActivity extends BaseActivity {
    @Bind(R.id.deleteMember_listView)
    ListView listView;

    private BaseAdapter baseAdapter;
    private static List<DeleteMember> data = new ArrayList<>();
    private ActionSheet mActionSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_deletemember);
        ButterKnife.bind(this);
        init();


    }

    private void init() {
        for (int i = 0; i < 50; i++) {
            DeleteMember member = new DeleteMember();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.greyblock);
            member.setBitmap(bitmap);
            member.setName("姓名" + i);
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
                    convertView = getLayoutInflater().inflate(R.layout.item_listview_deletemember, parent, false);
                    holder = new ViewHolder();
                    holder.check = (CheckBox) convertView.findViewById(R.id.deleteMember_Checkbox);
                    holder.img = (CirecleImage) convertView.findViewById(R.id.deleteMember_Img);
                    holder.name = (TextView) convertView.findViewById(R.id.deleteMember_Text);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.check.setChecked(data.get(position).isCheck());
                holder.img.setImageBitmap(data.get(position).getBitmap());
                holder.name.setText(data.get(position).getName());
                return convertView;
            }
        };
        listView.setAdapter(baseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data.get(position).setCheck(!data.get(position).isCheck());
                baseAdapter.notifyDataSetChanged();
            }
        });

    }

    class ViewHolder {
        private CheckBox check;
        public CirecleImage img;
        public TextView name;
    }


    @OnClick({R.id.deleteMember_Cancel, R.id.deleteMember_Delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.deleteMember_Cancel:
                finish();
                break;
            case R.id.deleteMember_Delete:
                if (mActionSheet == null) {
                    mActionSheet = new ActionSheet(DeleteMemberActivity.this);
                    mActionSheet.addMenuTopItem("确定要删除群成员？")
                            .addMenuBottomItem("确定");
                }
                mActionSheet.show();
                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = mActionSheet.getWindow().getAttributes();
                lp.width = (display.getWidth()); //设置宽度
                mActionSheet.getWindow().setAttributes(lp);
                mActionSheet.setMenuBottomListener(new ActionSheet.MenuListener() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        for (int i = data.size() - 1; i >= 0; i--) {
                            if (data.get(i).isCheck()) data.remove(data.get(i));
                        }
                        baseAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;

        }
    }


}
