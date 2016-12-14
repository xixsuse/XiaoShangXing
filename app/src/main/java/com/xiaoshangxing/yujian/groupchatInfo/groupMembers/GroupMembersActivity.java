package com.xiaoshangxing.yujian.groupchatInfo.groupMembers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.xiaoshangxing.R;
import com.xiaoshangxing.publicActivity.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.utils.imageUtils.MyGlide;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;
import com.xiaoshangxing.yujian.IM.cache.TeamDataCache;

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
    private String account;
    private List<TeamMember> teamMembers;
    private BaseAdapter baseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_groupmembers);
        ButterKnife.bind(this);
        account = getIntent().getStringExtra(IntentStatic.ACCOUNT);
        init();
    }

    private void init() {
        teamMembers = TeamDataCache.getInstance().getTeamMemberList(account);
        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return teamMembers.size();
            }

            @Override
            public Object getItem(int position) {
                return teamMembers.get(position);
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
                    holder.img = (CirecleImage) convertView.findViewById(R.id.blacklist_img);
                    holder.name = (TextView) convertView.findViewById(R.id.blacklist_text);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.img.setIntent_type(CirecleImage.PERSON_INFO, teamMembers.get(position).getAccount());

                MyGlide.with_defaul_image(GroupMembersActivity.this,
                        NimUserInfoCache.getInstance().getHeadImage(teamMembers.get(position).getAccount()), holder.img);
                holder.name.setText(NimUserInfoCache.getInstance().getUserDisplayName(teamMembers.get(position).getAccount()));
                return convertView;
            }
        };

        listView.setAdapter(baseAdapter);


    }

    @OnClick({R.id.groupMembers_back, R.id.groupMembers_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.groupMembers_back:
                finish();
                break;
            case R.id.groupMembers_add:
                Intent intent = new Intent(this, SelectPersonActivity.class);
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < teamMembers.size(); i++) {
                    list.add(teamMembers.get(i).getAccount());
                }
                intent.putExtra(SelectPersonActivity.LOCKED, list);
                startActivityForResult(intent, SelectPersonActivity.SELECT_PERSON_CODE);
                break;
        }
    }

    /**
     * 邀请群成员
     *
     * @param accounts 邀请帐号
     */
    private void inviteMembers(ArrayList<String> accounts) {
        NIMClient.getService(TeamService.class).addMembers(account, accounts).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                Toast.makeText(GroupMembersActivity.this, "添加群成员成功", Toast.LENGTH_SHORT).show();
                //                    延迟刷新界面
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        init();
                    }
                }, 100);
            }

            @Override
            public void onFailed(int code) {
                if (code == ResponseCode.RES_TEAM_INVITE_SUCCESS) {
                    Toast.makeText(GroupMembersActivity.this, "发送邀请成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GroupMembersActivity.this, "invite members failed, code=" + code, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "invite members failed, code=" + code);
                }
            }

            @Override
            public void onException(Throwable exception) {
                Log.d("invite", "error");
                exception.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SelectPersonActivity.SELECT_PERSON_CODE) {
            if (data == null) {
                Toast.makeText(GroupMembersActivity.this, "没有选择联系人", Toast.LENGTH_SHORT).show();
                return;
            }
            ArrayList<String> arrayList = data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON);
            if (arrayList == null || arrayList.size() == 0) {
                Toast.makeText(GroupMembersActivity.this, "没有选择联系人", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("select phone", arrayList.toString());
                inviteMembers(arrayList);
            }
        }
    }

    class ViewHolder {
        public CirecleImage img;
        public TextView name;
    }
}
