package com.xiaoshangxing.yujian.ChatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MessageReceipt;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.MessageListView;
import com.xiaoshangxing.utils.layout.MsgBkImageView;
import com.xiaoshangxing.yujian.IM.cache.FriendDataCache;
import com.xiaoshangxing.yujian.IM.cache.SimpleCallback;
import com.xiaoshangxing.yujian.IM.cache.TeamDataCache;
import com.xiaoshangxing.yujian.groupchatInfo.ChatInfoActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/9/2
 */
public class GroupActivity extends BaseActivity implements ModuleProxy {
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title)
    RelativeLayout title;
    @Bind(R.id.message_activity_background)
    MsgBkImageView messageActivityBackground;
    @Bind(R.id.team_notify_bar_panel)
    FrameLayout teamNotifyBarPanel;
    @Bind(R.id.messageListView)
    MessageListView messageListView;
    @Bind(R.id.timer)
    Chronometer timer;
    @Bind(R.id.timer_tip)
    TextView timerTip;
    @Bind(R.id.timer_tip_container)
    LinearLayout timerTipContainer;
    @Bind(R.id.layoutPlayAudio)
    FrameLayout layoutPlayAudio;
    @Bind(R.id.message_activity_list_view_container)
    FrameLayout messageActivityListViewContainer;
    @Bind(R.id.messageActivityLayout)
    LinearLayout messageActivityLayout;

    //  标志是否是resume状态
    private boolean isResume = false;

    // 聊天对象
    protected String sessionId;
    //    聊天类型
    protected SessionTypeEnum sessionType;
    //聊天信息展示面板
    protected MessageListPanel messageListPanel;
    protected InputPanel inputPanel;    //输入面板

    // model
    private Team team;

    private View invalidTeamTipView;

    private TextView invalidTeamTipText;


    private View rootView;

    public static void start(Context context, String contactId, IMMessage anchor, SessionTypeEnum sessionType) {
        Intent intent = new Intent();
        intent.putExtra(IntentStatic.EXTRA_ACCOUNT, contactId);   //id
        if (anchor != null) {
            intent.putExtra(IntentStatic.EXTRA_ANCHOR, anchor);
        }

        if (sessionType != null) {
            intent.putExtra(IntentStatic.EXTRA_TYPE, sessionType);
        }
        intent.setClass(context, GroupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_message_fragment);
        ButterKnife.bind(this);
        findViews();
        parseIntent();
//        开始监听
        registerObservers(true);

    }

    protected void findViews() {
        invalidTeamTipView = findViewById(R.id.invalid_team_tip);
        invalidTeamTipText = (TextView) findViewById(R.id.invalid_team_text);
        more.setImageResource(R.mipmap.group_chat_more);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestTeamInfo();
        isResume = true;
        messageListPanel.onResume();
        //        设置会话状态  自己不接受该对话id的通知栏提醒
        NIMClient.getService(MsgService.class).setChattingAccount(sessionId, sessionType);
        // 默认使用听筒播放
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    @Override
    public void onPause() {
        super.onPause();
//      设置会话状态   此时接受所有通知栏提醒
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE,
                SessionTypeEnum.None);
        inputPanel.onPause();
        messageListPanel.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isResume = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        messageListPanel.onDestroy();
//        取消监听
        registerObservers(false);
    }

    //  刷新消息列表
    public void refreshMessageList() {
        messageListPanel.refreshMessageList();
    }

    private void parseIntent() {
        sessionId = getIntent().getStringExtra(IntentStatic.EXTRA_ACCOUNT);
        sessionType = (SessionTypeEnum) getIntent().getSerializableExtra(IntentStatic.EXTRA_TYPE);
        IMMessage anchor = (IMMessage) getIntent().getSerializableExtra(IntentStatic.EXTRA_ANCHOR);

        Container container = new Container(this, sessionId, sessionType, this);
        rootView = findViewById(R.id.messageActivityLayout);
//      初始化消息面板
        if (messageListPanel == null) {
            messageListPanel = new MessageListPanel(container, rootView, anchor, false, false);
        } else {
            messageListPanel.reload(container, anchor);
        }

        if (inputPanel == null) {
            inputPanel = new InputPanel(container, rootView, true);
        }
    }

    /**
     * ********************** implements ModuleProxy *********************
     */
    @Override
    public boolean sendMessage(IMMessage message) {
        if (team == null || !team.isMyTeam()) {
            Toast.makeText(this, "您已不在该群,不能发送消息", Toast.LENGTH_SHORT).show();
            return false;
        }
        // send message to server and save to db
        NIMClient.getService(MsgService.class).sendMessage(message, false);

        messageListPanel.onMsgSend(message);

        return true;
    }

    @Override
    public void onInputPanelExpand() {
        messageListPanel.jumpReload();
        messageListPanel.scrollToBottom();
    }

    @Override
    public void shouldCollapseInputPanel() {
        inputPanel.collapse(false);
    }

    @Override
    public boolean isLongClickEnabled() {
        return !inputPanel.isRecording();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputPanel.onActivityResult(requestCode, resultCode, data);
        messageListPanel.onActivityResult(requestCode, resultCode, data);
    }


    //  注册监听
    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(incomingMessageObserver, register);
        service.observeMessageReceipt(messageReceiptObserver, register);
        registerTeamUpdateObserver(register);
    }


    /**
     * 请求群基本信息
     */
    private void requestTeamInfo() {
        // 请求群基本信息
        Team t = TeamDataCache.getInstance().getTeamById(sessionId);
        if (t != null) {
            updateTeamInfo(t);
        } else {
            TeamDataCache.getInstance().fetchTeamById(sessionId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result) {
                    if (success && result != null) {
                        updateTeamInfo(result);
                    } else {
                        onRequestTeamInfoFailed();
                    }
                }
            });
        }
    }

    private void onRequestTeamInfoFailed() {
        Toast.makeText(this, "获取群组信息失败!", Toast.LENGTH_SHORT);
        finish();
    }

    /**
     * 更新群信息
     *
     * @param d
     */
    private void updateTeamInfo(final Team d) {
        if (d == null) {
            return;
        }

        team = d;
        name.setText(team == null ? sessionId : team.getName() + "(" + team.getMemberCount() + "人)");

        invalidTeamTipText.setText(team.getType() == TeamTypeEnum.Normal ? "您已退出该讨论组" : "您已退出该群");
        invalidTeamTipView.setVisibility(team.isMyTeam() ? View.GONE : View.VISIBLE);
        if (!team.isMyTeam()) {
            NIMClient.getService(MsgService.class).deleteRecentContact2(team.getId(), SessionTypeEnum.Team);
        }
    }

    /**
     * 注册群信息更新监听
     *
     * @param register
     */
    private void registerTeamUpdateObserver(boolean register) {
        if (register) {
            TeamDataCache.getInstance().registerTeamDataChangedObserver(teamDataChangedObserver);
            TeamDataCache.getInstance().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        } else {
            TeamDataCache.getInstance().unregisterTeamDataChangedObserver(teamDataChangedObserver);
            TeamDataCache.getInstance().unregisterTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        }
        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
    }

    /**
     * 群资料变动通知和移除群的通知（包括自己退群和群被解散）
     */
    TeamDataCache.TeamDataChangedObserver teamDataChangedObserver = new TeamDataCache.TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            if (team == null) {
                return;
            }
            for (Team t : teams) {
                if (t.getId().equals(team.getId())) {
                    updateTeamInfo(t);
                    break;
                }
            }
        }

        @Override
        public void onRemoveTeam(Team team) {
            if (team == null) {
                return;
            }
            if (team.getId().equals(GroupActivity.this.team.getId())) {
                updateTeamInfo(team);
            }
        }
    };

    /**
     * 群成员资料变动通知和移除群成员通知
     */
    TeamDataCache.TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamDataCache.TeamMemberDataChangedObserver() {

        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            refreshMessageList();
        }

        @Override
        public void onRemoveTeamMember(TeamMember member) {
        }
    };

    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            refreshMessageList();
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            refreshMessageList();
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            refreshMessageList();
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            refreshMessageList();
        }
    };

    /**
     * 消息接收观察者
     */
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }

            messageListPanel.onIncomingMessage(messages);
            sendMsgReceipt(); // 发送已读回执
        }
    };

    private Observer<List<MessageReceipt>> messageReceiptObserver = new Observer<List<MessageReceipt>>() {
        @Override
        public void onEvent(List<MessageReceipt> messageReceipts) {
            receiveReceipt();
        }
    };

    /**
     * 发送已读回执
     */
    private void sendMsgReceipt() {
        messageListPanel.sendReceipt();
    }

    /**
     * 收到已读回执
     */
    public void receiveReceipt() {
        messageListPanel.receiveReceipt();
    }

    @OnClick({R.id.back, R.id.more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.more:
                if (!team.isMyTeam()) {
                    showToast("您已不在该群");
                    return;
                }
                Intent intent = new Intent(GroupActivity.this, ChatInfoActivity.class);
                intent.putExtra(IntentStatic.EXTRA_ACCOUNT,sessionId);
                startActivity(intent);
                break;
        }
    }
}
