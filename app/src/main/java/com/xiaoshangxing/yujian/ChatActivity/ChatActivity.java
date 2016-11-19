package com.xiaoshangxing.yujian.ChatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MessageReceipt;
import com.xiaoshangxing.Network.IMNetwork;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.OperateUtils;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.MessageListView;
import com.xiaoshangxing.utils.layout.MsgBkImageView;
import com.xiaoshangxing.yujian.IM.NimUIKit;
import com.xiaoshangxing.yujian.IM.cache.FriendDataCache;
import com.xiaoshangxing.yujian.IM.uinfo.UserInfoHelper;
import com.xiaoshangxing.yujian.IM.uinfo.UserInfoObservable;
import com.xiaoshangxing.yujian.personChatInfo.PersonChatInfoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by FengChaoQun
 * on 2016/9/2
 */
public class ChatActivity extends BaseActivity implements ModuleProxy, IBaseView {
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
    @Bind(R.id.invalid_team_text)
    TextView invalidTeamText;
    @Bind(R.id.invalid_team_tip)
    RelativeLayout invalidTeamTip;
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
    @Bind(R.id.black_text)
    TextView blackText;
    @Bind(R.id.black)
    RelativeLayout black;
    @Bind(R.id.love)
    RelativeLayout love;
    @Bind(R.id.stranger_layout)
    LinearLayout strangerLayout;

    //  标志是否是resume状态
    private boolean isResume = false;

    // 聊天对象
    protected String sessionId;
    //    聊天类型
    protected SessionTypeEnum sessionType;
    //聊天信息展示面板
    protected MessageListPanel messageListPanel;
    protected InputPanel inputPanel;    //输入面板

    private View rootView;

    private Handler handler = new Handler();
    private Runnable runnable;

    public static void start(Context context, String contactId, IMMessage anchor, SessionTypeEnum sessionType) {
        Intent intent = new Intent();
        intent.putExtra(IntentStatic.EXTRA_ACCOUNT, contactId);   //id
        if (anchor != null) {
            intent.putExtra(IntentStatic.EXTRA_ANCHOR, anchor);
        }

        if (sessionType != null) {
            intent.putExtra(IntentStatic.EXTRA_TYPE, sessionType);
        }
        intent.setClass(context, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_message_fragment);
        ButterKnife.bind(this);
        parseIntent();
        // 单聊特例话数据，包括个人信息，
        requestBuddyInfo();
//        开始监听
        registerObservers(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
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
        handler.removeCallbacks(runnable);
        inputPanel.onDestroy();
    }

    //  刷新消息列表
    public void refreshMessageList() {
        messageListPanel.refreshMessageList();
    }

    //  刷新标题名称
    private void requestBuddyInfo() {
//        title.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
//        if (!FriendDataCache.getInstance().isMyFriend(sessionId)) {
//            strangerLayout.setVisibility(View.VISIBLE);
//            if (FriendDataCache.getInstance().isInblack(sessionId)) {
//                blackText.setText("取消");
//            }
//            getStar();
//        }

        if (!FriendDataCache.getInstance().isMyFriend(sessionId) && !NimUIKit.getAccount().equals(sessionId)) {
            title.setText("临时会话");
        } else {
            title.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }
    }

    private List<User> loves = new ArrayList<>();

    private void getStar() {
        Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case 200:
                            Gson gson = new Gson();
                            loves = gson.fromJson(jsonObject.getJSONArray(NS.MSG).toString(), new TypeToken<List<User>>() {
                            }.getType());
                            for (User user : loves) {
                                if (user.getId() == Integer.valueOf(sessionId)) {
//                                    isLoved = true;
//                                    bt1.setChecked(true);
//                                    bt1.getImgView().setImageResource(R.mipmap.icon_liuxin_select);
                                    strangerLayout.setVisibility(View.GONE);
                                }
                            }
                            break;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        IMNetwork.getInstance().MyFavor(subscriber, String.valueOf(TempUser.id), this);
    }

    private void black() {
        if (FriendDataCache.getInstance().isMyFriend(sessionId)) {
            NIMClient.getService(FriendService.class).addToBlackList(sessionId)
                    .setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            requestBuddyInfo();
                            showToast("设置成功");
                        }

                        @Override
                        public void onFailed(int i) {
                            showToast("设置失败:" + i);
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            showToast("设置失败:异常");
                            throwable.printStackTrace();
                        }
                    });
        } else {
            NIMClient.getService(FriendService.class).removeFromBlackList(sessionId)
                    .setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            requestBuddyInfo();
                            showToast("设置成功");
                        }

                        @Override
                        public void onFailed(int i) {
                            showToast("设置失败:" + i);
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            showToast("设置失败:异常");
                            throwable.printStackTrace();
                        }
                    });
        }

    }

    private void favor() {

        OperateUtils.Favor(sessionId, this, this, new SimpleCallBack() {
            @Override
            public void onSuccess() {
                requestBuddyInfo();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onBackData(Object o) {

            }
        });
    }

    private void parseIntent() {
        more.setImageResource(R.mipmap.chat_more);
        more.setPadding(0, 0, 0, 0);
        sessionId = getIntent().getStringExtra(IntentStatic.EXTRA_ACCOUNT);
        sessionType = (SessionTypeEnum) getIntent().getSerializableExtra(IntentStatic.EXTRA_TYPE);
        IMMessage anchor = (IMMessage) getIntent().getSerializableExtra(IntentStatic.EXTRA_ANCHOR);

        Container container = new Container(this, sessionId, sessionType, this);
        rootView = findViewById(R.id.messageActivityLayout);
//      初始化消息面板
        if (messageListPanel == null) {
//            View rootView = View.inflateChild(this, R.layout.nim_message_fragment, null);
            messageListPanel = new MessageListPanel(container, rootView, anchor, false, false);
        } else {
            messageListPanel.reload(container, anchor);
        }

        if (inputPanel == null) {
            inputPanel = new InputPanel(container, rootView, true);
        }

        runnable = new Runnable() {
            @Override
            public void run() {
                requestBuddyInfo();
            }
        };
    }

    /**
     * ********************** implements ModuleProxy *********************
     */
    @Override
    public boolean sendMessage(IMMessage message) {

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
//     用户信息变更监听
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
//        自定义消息 主要是输入状态
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, register);
//        好友信息监听
        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);

        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(incomingMessageObserver, register);
        service.observeMessageReceipt(messageReceiptObserver, register);
    }

    //  好友信息变化时 刷新标题
    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            requestBuddyInfo();
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            requestBuddyInfo();
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            requestBuddyInfo();
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            requestBuddyInfo();
        }
    };
    //  用户信息变化时 刷新标题
    private UserInfoObservable.UserInfoObserver uinfoObserver;

    private void registerUserInfoObserver() {
        if (uinfoObserver == null) {
            uinfoObserver = new UserInfoObservable.UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    if (accounts.contains(sessionId)) {
                        requestBuddyInfo();
                    }
                }
            };
        }

        UserInfoHelper.registerObserver(uinfoObserver);
    }

    private void unregisterUserInfoObserver() {
        if (uinfoObserver != null) {
            UserInfoHelper.unregisterObserver(uinfoObserver);
        }
    }

    /**
     * 命令消息接收观察者
     */
    Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            if (!sessionId.equals(message.getSessionId()) || message.getSessionType() != SessionTypeEnum.P2P) {
                return;
            }
            showCommandMessage(message);
        }
    };

    //  显示“对方正在输入”
    protected void showCommandMessage(CustomNotification message) {
        if (!isResume) {
            return;
        }

        String content = message.getContent();
        try {
            JSONObject json = new JSONObject(content);
            int id = json.getInt(NS.ID);
            if (id == 1) {
                // 正在输入
//                Toast.makeText(ChatActivity.this, "对方正在输入...", Toast.LENGTH_LONG).show();
                title.setText("对方正在输入...");
                handler.postDelayed(runnable, 3000);
            } else {
                Toast.makeText(ChatActivity.this, "command: " + content, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }
    }

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
                Intent more_intent = new Intent(ChatActivity.this, PersonChatInfoActivity.class);
                more_intent.putExtra(IntentStatic.EXTRA_ACCOUNT, sessionId);
                startActivity(more_intent);
                break;
        }
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }
}
