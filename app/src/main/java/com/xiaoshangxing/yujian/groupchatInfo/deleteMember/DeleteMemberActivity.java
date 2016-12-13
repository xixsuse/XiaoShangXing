package com.xiaoshangxing.yujian.groupchatInfo.deleteMember;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.utils.ActionSheet;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogLocationAndSize;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.yujian.IM.NimUIKit;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;
import com.xiaoshangxing.yujian.IM.cache.SimpleCallback;
import com.xiaoshangxing.yujian.IM.cache.TeamDataCache;
import com.xiaoshangxing.yujian.IM.uinfo.UserInfoHelper;
import com.xiaoshangxing.yujian.IM.uinfo.UserInfoObservable;

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
    @Bind(R.id.deleteMember_Delete)
    TextView delete;
    private String account;

    private BaseAdapter baseAdapter;
    //    private static List<DeleteMember> data = new ArrayList<>();
    private ActionSheet mActionSheet;
    private static int count = 0;
    private List<TeamMember> teamMembers;
    private List<String> select_account = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_deletemember);
        ButterKnife.bind(this);
        requestMembers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerObservers(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        registerObservers(false);
    }

    private void init() {
        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return teamMembers.size();
            }

            @Override
            public TeamMember getItem(int position) {
                return teamMembers.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final ViewHolder holder;
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
                if (select_account.contains(teamMembers.get(position).getAccount())) {
                    holder.check.setChecked(true);
                } else {
                    holder.check.setChecked(false);
                }

                holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (!select_account.contains(teamMembers.get(position).getAccount())) {
                                select_account.add(teamMembers.get(position).getAccount());
                            } else {
                                select_account.remove(teamMembers.get(position).getAccount());
                            }
                        }
                        if (select_account.size() > 0) {
                            delete.setEnabled(true);
                            delete.setAlpha(1);
                        } else {
                            delete.setEnabled(false);
                            delete.setAlpha(0.5f);
                        }
                    }
                });

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.check.performClick();
                    }
                });

                final NimUserInfo userInfo = NimUserInfoCache.getInstance().getUserInfo(teamMembers.get(position).getAccount());
                Glide.with(DeleteMemberActivity.this)
                        .load(userInfo.getAvatar())
                        .placeholder(R.mipmap.greyblock)
                        .error(R.mipmap.cirecleimage_default)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.img);
                holder.name.setText(userInfo.getName());
                return convertView;
            }
        };
        listView.setAdapter(baseAdapter);

    }

    private void requestMembers() {
        account = getIntent().getStringExtra(IntentStatic.DATA);
        TeamDataCache.getInstance().fetchTeamMemberList(account, new SimpleCallback<List<TeamMember>>() {
            @Override
            public void onResult(boolean success, List<TeamMember> members) {
                if (success && members != null && !members.isEmpty()) {
                    teamMembers = members;

                    for (int i = 0; i < teamMembers.size(); i++) {
                        if (teamMembers.get(i).getAccount().equals(NimUIKit.getAccount())) {
                            Log.d("myaccount",teamMembers.get(i).getAccount());
                            teamMembers.remove(i);
                        }
                    }
                    init();
                } else {
                    Toast.makeText(DeleteMemberActivity.this, "加载成员消息有误", Toast.LENGTH_SHORT).show();
                }
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
                delete();
                break;
        }


    }


    public void delete() {
        final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(this);
        Dialog alertDialog = dialogUtils.Message("确定要删除群成员？")
                .Button("确定","取消").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
                    @Override
                    public void onButton1() {
                        removePeople();
                        baseAdapter.notifyDataSetChanged();
//                        count = 0;
//                        delete.setText("删除");
//                        delete.setAlpha((float) 0.5);
//                        delete.setEnabled(false);
                        dialogUtils.close();
                    }

                    @Override
                    public void onButton2() {
                        dialogUtils.close();
                    }
                }).create();
        DialogLocationAndSize.setWidth(alertDialog, R.dimen.x780);
        alertDialog.show();

    }

    private void removePeople() {
        if (select_account.size() == 0) {
            Toast.makeText(DeleteMemberActivity.this, "没有选中人", Toast.LENGTH_SHORT).show();
            return;
        } else {
            NIMClient.getService(TeamService.class).removeMembers(account, select_account).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(DeleteMemberActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    select_account.clear();
                    baseAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed(int i) {
                    Toast.makeText(DeleteMemberActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    select_account.clear();
                    baseAdapter.notifyDataSetChanged();
                }

                @Override
                public void onException(Throwable throwable) {

                }
            });

        }
    }


    private void registerObservers(boolean register) {
        if (register) {
            TeamDataCache.getInstance().registerTeamMemberDataChangedObserver(teamMemberObserver);
            TeamDataCache.getInstance().registerTeamDataChangedObserver(teamDataObserver);
        } else {
            TeamDataCache.getInstance().unregisterTeamMemberDataChangedObserver(teamMemberObserver);
            TeamDataCache.getInstance().unregisterTeamDataChangedObserver(teamDataObserver);
        }

        registerUserInfoChangedObserver(register);
    }

    TeamDataCache.TeamMemberDataChangedObserver teamMemberObserver = new TeamDataCache.TeamMemberDataChangedObserver() {

        @Override
        public void onUpdateTeamMember(List<TeamMember> m) {
            for (TeamMember mm : m) {
                for (TeamMember member : teamMembers) {
                    if (mm.getAccount().equals(member.getAccount())) {
                        teamMembers.set(teamMembers.indexOf(member), mm);
                        break;
                    }
                }
            }
            baseAdapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeamMember(TeamMember member) {
            teamMembers.remove(member);
            baseAdapter.notifyDataSetChanged();
        }
    };

    TeamDataCache.TeamDataChangedObserver teamDataObserver = new TeamDataCache.TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            for (Team team : teams) {
                if (team.getId().equals(account)) {
                    requestMembers();
                    break;
                }
            }
        }

        @Override
        public void onRemoveTeam(Team team) {
            if (team.getId().equals(account)) {
                Toast.makeText(DeleteMemberActivity.this, "群已解散", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };
    private UserInfoObservable.UserInfoObserver userInfoObserver;

    private void registerUserInfoChangedObserver(boolean register) {
        if (register) {
            if (userInfoObserver == null) {
                userInfoObserver = new UserInfoObservable.UserInfoObserver() {
                    @Override
                    public void onUserInfoChanged(List<String> accounts) {
                        baseAdapter.notifyDataSetChanged();
                    }
                };
            }
            UserInfoHelper.registerObserver(userInfoObserver);
        } else {
            UserInfoHelper.unregisterObserver(userInfoObserver);
        }
    }


}
