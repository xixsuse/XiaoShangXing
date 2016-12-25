package com.xiaoshangxing.yujian.groupchatInfo;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamMemberType;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.bean.TopChat;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.publicActivity.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.customView.SwitchView;
import com.xiaoshangxing.utils.customView.dialog.ActionSheet;
import com.xiaoshangxing.utils.customView.dialog.DialogLocationAndSize;
import com.xiaoshangxing.utils.customView.dialog.DialogUtils;
import com.xiaoshangxing.wo.setting.currency.chatBackground.ChatBackgroundActivity;
import com.xiaoshangxing.yujian.IM.NimUIKit;
import com.xiaoshangxing.yujian.IM.cache.SimpleCallback;
import com.xiaoshangxing.yujian.IM.cache.TeamDataCache;
import com.xiaoshangxing.yujian.IM.uinfo.UserInfoHelper;
import com.xiaoshangxing.yujian.IM.uinfo.UserInfoObservable;
import com.xiaoshangxing.yujian.YujianFragment.YuJianFragment;
import com.xiaoshangxing.yujian.groupchatInfo.chooseNewGroupMaster.ChooseNewGroupMasterActivity;
import com.xiaoshangxing.yujian.groupchatInfo.groupCode.GroupCodeActivity;
import com.xiaoshangxing.yujian.groupchatInfo.groupMembers.GroupMembersActivity;
import com.xiaoshangxing.yujian.groupchatInfo.groupName.GroupNameActivity;
import com.xiaoshangxing.yujian.groupchatInfo.groupNotice.GroupNoticeEditActivity;
import com.xiaoshangxing.yujian.groupchatInfo.groupNotice.GroupNoticeShowActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by 15828 on 2016/8/12.
 */
public class ChatInfoActivity extends BaseActivity {

    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.chatinfo_gridview)
    MyGridView mGridview;
    @Bind(R.id.allGroupMember)
    TextView allGroupMember;
    @Bind(R.id.AllGroupMemberView)
    RelativeLayout AllGroupMemberView;
    @Bind(R.id.GroupChatName)
    TextView GroupChatName;
    @Bind(R.id.GroupChatNameImag)
    ImageView GroupChatNameImag;
    @Bind(R.id.GroupChatNameView)
    RelativeLayout GroupChatNameView;
    @Bind(R.id.GroupCodeImag)
    ImageView GroupCodeImag;
    @Bind(R.id.GroupCodeView)
    RelativeLayout GroupCodeView;
    @Bind(R.id.IsGroupNoticeSetted)
    TextView IsGroupNoticeSetted;
    @Bind(R.id.GroupNoticeImg)
    ImageView GroupNoticeImg;
    @Bind(R.id.GroupNoticeView1)
    RelativeLayout GroupNoticeView1;
    @Bind(R.id.GroupNotice_text1)
    TextView GroupNoticeText1;
    @Bind(R.id.GroupNotice_content)
    TextView GroupNoticeContent;
    @Bind(R.id.GroupNoticeView2)
    RelativeLayout GroupNoticeView2;
    @Bind(R.id.topChat)
    SwitchView topChat;
    @Bind(R.id.noDisturb)
    SwitchView noDisturb;
    @Bind(R.id.saveToFriend_GroupChat)
    SwitchView saveToFriendGroupChat;
    @Bind(R.id.SetChatBackGroundView)
    RelativeLayout SetChatBackGroundView;
    @Bind(R.id.TransferMainRightView)
    RelativeLayout TransferMainRightView;
    @Bind(R.id.CleanChatRecordView)
    RelativeLayout CleanChatRecordView;
    @Bind(R.id.bindmailbox_breakmaibox)
    Button bindmailboxBreakmaibox;

    private Adapter adapter;
    private String groupNoticeContent;
    private ActionSheet mActionSheet1;
    private ActionSheet mActionSheet2;
    private String account;
    private List<TeamMember> teamMembers;

    private boolean isMyteam;
    private Team team;
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
            adapter.notifyDataSetChanged();
            refresh();
        }

        @Override
        public void onRemoveTeamMember(TeamMember member) {
            teamMembers.remove(member);
            adapter.notifyDataSetChanged();
            refresh();
        }
    };
    TeamDataCache.TeamDataChangedObserver teamDataObserver = new TeamDataCache.TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            for (Team team1 : teams) {
                if (team1.getId().equals(account)) {
                    team = team1;
                    refresh();
                    break;
                }
            }
        }

        @Override
        public void onRemoveTeam(Team team) {
            if (team.getId().equals(account)) {
                Toast.makeText(ChatInfoActivity.this, "您已不在该群", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };
    private UserInfoObservable.UserInfoObserver userInfoObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_chatinfo);
        ButterKnife.bind(this);
        parseData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerObservers(true);
        refresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        registerObservers(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        title.setText("聊天信息");
        more.setVisibility(View.GONE);
        adapter = new Adapter(ChatInfoActivity.this, teamMembers, this);
        mGridview.setAdapter(adapter);
        isMyteam();
        requestMembers();
        setGroupChatName();
        setGroupNoticeContent();
        ZhiDing();
        MessageMute();
    }

    private void isMyteam() {
        TeamDataCache.getInstance().fetchTeamMember(account, NimUIKit.getAccount(), new SimpleCallback<TeamMember>() {
            @Override
            public void onResult(boolean success, TeamMember result) {
                if (result.getType() == TeamMemberType.Owner) {
                    isMyteam = true;
                }
                setGroupNoticeContent();
                TransferMainRightView.setVisibility(isMyteam ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void parseData() {
        account = getIntent().getStringExtra(IntentStatic.ACCOUNT);
        if (account == null) {
            Toast.makeText(ChatInfoActivity.this, "群资料有误", Toast.LENGTH_SHORT).show();
            finish();
        }
        team = TeamDataCache.getInstance().getTeamById(account);
        if (team == null) {
            showToast("获取群信息失败");
            finish();
        }
    }

    private void refresh() {
        requestMembers();
        setGroupChatName();
        setGroupNoticeContent();
        refreshNotice();
    }

    //    消息免打扰
    private void MessageMute() {
        noDisturb.setState(team.mute());
        Log.d("消息免打扰", "" + team.mute());
        noDisturb.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                NIMClient.getService(TeamService.class).muteTeam(account, true).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        noDisturb.setState(true);
                        showToast("设置成功,现在不接受该群消息通知");
                        Log.d("消息免打扰1", "" + team.mute());
                    }

                    @Override
                    public void onFailed(int code) {
                        showToast("设置失败:" + code);
                    }

                    @Override
                    public void onException(Throwable exception) {
                        showToast("设置失败:异常");
                    }
                });
            }

            @Override
            public void toggleToOff() {
                NIMClient.getService(TeamService.class).muteTeam(account, false).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        noDisturb.setState(false);
                        showToast("设置成功,现在接受该群消息通知");
                        Log.d("消息免打扰1", "" + team.mute());
                    }

                    @Override
                    public void onFailed(int code) {
                        showToast("设置失败:" + code);
                    }

                    @Override
                    public void onException(Throwable exception) {
                        showToast("设置失败:异常");
                    }
                });
            }
        });
    }

    /**
     * *************************** 加载&变更数据源 ********************************
     */
    private void requestMembers() {
        TeamDataCache.getInstance().fetchTeamMemberList(account, new SimpleCallback<List<TeamMember>>() {
            @Override
            public void onResult(boolean success, List<TeamMember> members) {
                if (success && members != null && !members.isEmpty()) {
                    teamMembers = members;
                    adapter.setData(members);
                    String a = String.format("全部群成员（%S）", teamMembers.size());
                    allGroupMember.setText(a);
                } else {
                    Toast.makeText(ChatInfoActivity.this, "加载成员消息有误", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private void registerUserInfoChangedObserver(boolean register) {
        if (register) {
            if (userInfoObserver == null) {
                userInfoObserver = new UserInfoObservable.UserInfoObserver() {
                    @Override
                    public void onUserInfoChanged(List<String> accounts) {
                        adapter.notifyDataSetChanged();
                        refresh();
                    }
                };
            }
            UserInfoHelper.registerObserver(userInfoObserver);
        } else {
            UserInfoHelper.unregisterObserver(userInfoObserver);
        }
    }

    private void refreshNotice() {
        team = TeamDataCache.getInstance().getTeamById(account);
        if (team == null) {
            showToast("获取群信息失败");
            finish();
        }
        Log.d("refresh notice", "ok");
        setGroupNoticeContent();
    }

    private void setGroupNoticeContent() {
        if (!TextUtils.isEmpty(team.getAnnouncement())) {
            try {
                JSONObject jsonObject = new JSONObject(team.getAnnouncement());
                groupNoticeContent = jsonObject.getString(NS.CONTENT);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(groupNoticeContent)) {
            GroupNoticeView1.setVisibility(View.GONE);
            GroupNoticeView2.setVisibility(View.VISIBLE);
            GroupNoticeContent.setText(groupNoticeContent);
        } else {
            GroupNoticeView1.setVisibility(View.VISIBLE);
            GroupNoticeView2.setVisibility(View.GONE);
            if (isMyteam) {
                IsGroupNoticeSetted.setText("未填写");
            } else {
                IsGroupNoticeSetted.setText("");
            }
        }
    }

    private void ZhiDing() {
        TopChat mytopChat = realm.where(TopChat.class).equalTo("account", account).findFirst();
        if (mytopChat == null) {
            topChat.setState(false);
        } else {
            topChat.setState(true);
        }


        topChat.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        TopChat topchat = new TopChat();
                        topchat.setAccount(account);
                        realm.copyToRealmOrUpdate(topchat);
                        Log.d("copy_to_realm", realm.where(TopChat.class).toString());
                        topChat.setState(true);

                        NIMClient.getService(MsgService.class).queryRecentContacts().
                                setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                                    @Override
                                    public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                                        if (code != ResponseCode.RES_SUCCESS || recents == null) {
                                            return;
                                        }
                                        for (int i = 0; i < recents.size(); i++) {
                                            if (recents.get(i).getContactId().equals(account)) {
                                                recents.get(i).setTag(YuJianFragment.RECENT_TAG_STICKY);
                                                NIMClient.getService(MsgService.class).updateRecent(recents.get(i));
                                                Log.d("置顶", "--" + recents.get(i).getContactId() + "tag" + recents.get(i).getTag());
                                            }
                                        }
                                    }
                                });
                        showToast("置顶成功");

                    }
                });
            }

            @Override
            public void toggleToOff() {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        TopChat mytopChat = realm.where(TopChat.class).equalTo("account", account).findFirst();
                        if (mytopChat != null) {
                            mytopChat.deleteFromRealm();
                        }
                        topChat.setState(false);
                        NIMClient.getService(MsgService.class).queryRecentContacts().
                                setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                                    @Override
                                    public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                                        if (code != ResponseCode.RES_SUCCESS || recents == null) {
                                            return;
                                        }
                                        for (int i = 0; i < recents.size(); i++) {
                                            if (recents.get(i).getContactId().equals(account)) {
                                                recents.get(i).setTag(0);
                                                NIMClient.getService(MsgService.class).updateRecent(recents.get(i));
                                                Log.d("置顶", "--" + recents.get(i).getContactId() + "tag" + recents.get(i).getTag());
                                            }
                                        }
                                    }
                                });
                        showToast("取消置顶");
                    }
                });
            }
        });
    }

    private void setGroupChatName() {
        GroupChatName.setText(TeamDataCache.getInstance().getTeamName(account));
    }

    public void AllGroupMember(View view) {
        Intent intent = new Intent(this, GroupMembersActivity.class);
        intent.putExtra(IntentStatic.ACCOUNT, account);
        startActivity(intent);
    }

    public void GroupChatName(View view) {
        Intent intent = new Intent(this, GroupNameActivity.class);
        intent.putExtra(IntentStatic.ACCOUNT, account);
        startActivity(intent);
    }

    public void GroupCode(View view) {
        Intent intent = new Intent(this, GroupCodeActivity.class);
        startActivity(intent);
    }

    public void GroupNotice(View view) {
        boolean isQunzhu = isMyteam;
//        if (isQunzhu) {
//            Intent intent = new Intent(this, GroupNoticeEditActivity.class);
//            intent.putExtra(IntentStatic.ACCOUNT, account);
//            startActivity(intent);
//        } else {
//            final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(this);
//            final Dialog alertDialog = dialogUtils.Message("只有群主才能修改群公告")
//                    .Button("我知道了").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
//                        @Override
//                        public void onButton1() {
//                            dialogUtils.close();
//                        }
//
//                        @Override
//                        public void onButton2() {
//
//                        }
//
//                    }).create();
//            alertDialog.show();
//            DialogLocationAndSize.setWidth(alertDialog, R.dimen.x780);
//        }
        if (TextUtils.isEmpty(groupNoticeContent)) {
            if (isQunzhu) {
                Intent intent = new Intent(this, GroupNoticeEditActivity.class);
                intent.putExtra(IntentStatic.ACCOUNT, account);
                startActivity(intent);
            } else {
                final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(this);
                final Dialog alertDialog = dialogUtils.Message("只有群主才能修改群公告")
                        .Button("我知道了").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
                            @Override
                            public void onButton1() {
                                dialogUtils.close();
                            }

                            @Override
                            public void onButton2() {

                            }

                        }).create();
                alertDialog.show();
                DialogLocationAndSize.setWidth(alertDialog, R.dimen.x780);
            }
        } else {
            Intent intent = new Intent(this, GroupNoticeShowActivity.class);
            intent.putExtra(IntentStatic.ACCOUNT, account);
            startActivity(intent);
        }
    }

    public void SetChatBackGround(View view) {
        Intent intent = new Intent(this, ChatBackgroundActivity.class);
        intent.putExtra(IntentStatic.ACCOUNT, account);
        startActivity(intent);
    }

    public void TransferMainRight(View view) {
        if (!isMyteam) {
            showToast("只有群主才可以转让群");
            return;
        }
        Intent intent = new Intent(this, ChooseNewGroupMasterActivity.class);
        intent.putExtra(IntentStatic.ACCOUNT, account);
        startActivity(intent);
    }

    public void CleanChatRecord(View view) {
        if (mActionSheet1 == null) {
            mActionSheet1 = new ActionSheet(this);
            mActionSheet1.addMenuBottomItem("清空聊天记录");
        }
        mActionSheet1.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mActionSheet1.getWindow().getAttributes();
        lp.width = (display.getWidth()); //设置宽度
        mActionSheet1.getWindow().setAttributes(lp);
        mActionSheet1.setMenuBottomListener(new ActionSheet.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                NIMClient.getService(MsgService.class).clearChattingHistory(account, SessionTypeEnum.Team);
                Toast.makeText(ChatInfoActivity.this, item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    public void DeleteAndQuit(View view) {
        if (mActionSheet2 == null) {
            mActionSheet2 = new ActionSheet(this);
            mActionSheet2.addMenuTopItem("退出后不会通知群中其他成员，且不会再接收此群聊消息")
                    .addMenuBottomItem("确定");
        }
        mActionSheet2.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mActionSheet2.getWindow().getAttributes();
        lp.width = (display.getWidth()); //设置宽度
        mActionSheet2.getWindow().setAttributes(lp);
        mActionSheet2.setMenuBottomListener(new ActionSheet.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                if (team.getType() == TeamTypeEnum.Advanced && isMyteam) {
                    NIMClient.getService(TeamService.class).dismissTeam(account).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast("解散成功");
                            finish();
                        }

                        @Override
                        public void onFailed(int i) {
                            showToast("操作失败:" + i);
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            showToast("操作失败:异常");
                            throwable.printStackTrace();
                        }
                    });
                } else {
                    NIMClient.getService(TeamService.class).quitTeam(account).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast("退出成功");
                            finish();
                        }

                        @Override
                        public void onFailed(int i) {
                            showToast("操作失败:" + i);
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            showToast("操作失败:异常");
                            throwable.printStackTrace();
                        }
                    });
                }

            }

            @Override
            public void onCancel() {

            }
        });
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
                Toast.makeText(ChatInfoActivity.this, "添加群成员成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int code) {
                if (code == ResponseCode.RES_TEAM_INVITE_SUCCESS) {
                    Toast.makeText(ChatInfoActivity.this, "发送邀请成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChatInfoActivity.this, "invite members failed, code=" + code, Toast.LENGTH_SHORT).show();
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SelectPersonActivity.SELECT_PERSON_CODE) {
            if (data == null) {
                Toast.makeText(ChatInfoActivity.this, "没有选择联系人", Toast.LENGTH_SHORT).show();
                return;
            }
            ArrayList<String> arrayList = data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON);
            if (arrayList == null || arrayList.size() == 0) {
//                Toast.makeText(ChatInfoActivity.this, "没有选择联系人", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("select phone", arrayList.toString());
                inviteMembers(arrayList);
            }
        }
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
