package com.xiaoshangxing.yujian.YujianFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.xiaoshangxing.Network.netUtil.AppNetUtil;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TopChat;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.layout.LayoutHelp;
import com.xiaoshangxing.utils.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.yujian.ChatActivity.ChatActivity;
import com.xiaoshangxing.yujian.ChatActivity.GroupActivity;
import com.xiaoshangxing.yujian.FriendActivity.FriendActivity;
import com.xiaoshangxing.yujian.IM.cache.FriendDataCache;
import com.xiaoshangxing.yujian.IM.cache.TeamDataCache;
import com.xiaoshangxing.yujian.IM.kit.ListViewUtil;
import com.xiaoshangxing.yujian.IM.kit.LoginSyncDataStatusObserver;
import com.xiaoshangxing.yujian.IM.kit.reminder.ReminderItem;
import com.xiaoshangxing.yujian.IM.kit.reminder.ReminderManager;
import com.xiaoshangxing.yujian.IM.kit.reminder.SystemMessageUnreadManager;
import com.xiaoshangxing.yujian.IM.uinfo.UserInfoHelper;
import com.xiaoshangxing.yujian.IM.uinfo.UserInfoObservable;
import com.xiaoshangxing.yujian.IM.viewHodler.CommonRecentViewHolder;
import com.xiaoshangxing.yujian.IM.viewHodler.RecentContactAdapter;
import com.xiaoshangxing.yujian.IM.viewHodler.RecentContactsCallback;
import com.xiaoshangxing.yujian.IM.viewHodler.RecentViewHolder;
import com.xiaoshangxing.yujian.IM.viewHodler.TAdapterDelegate;
import com.xiaoshangxing.yujian.IM.viewHodler.TViewHolder;
import com.xiaoshangxing.yujian.IM.viewHodler.TeamRecentViewHolder;
import com.xiaoshangxing.yujian.Serch.GlobalSearchActivity;
import com.xiaoshangxing.yujian.xiaoyou.XiaoYouActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by FengChaoQun
 * on 2016/8/11
 */

public class YuJianFragment extends BaseFragment implements ReminderManager.UnreadNumChangedCallback, TAdapterDelegate {

    public static final String TAG = BaseFragment.TAG + "-YuJianFragment";

    @Bind(R.id.schoolfellow)
    ImageView schoolfellow;
    @Bind(R.id.serch_layout)
    RelativeLayout serchLayout;
    @Bind(R.id.friend)
    ImageView friend;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.reflesh_layout)
    PtrFrameLayout ptrFrameLayout;
    @Bind(R.id.no_net_lay)
    FrameLayout noNetLay;
    @Bind(R.id.listview)
    ListView listView;
    @Bind(R.id.date_state)
    TextView date_state;
    @Bind(R.id.current_state)
    TextView currentState;

    Realm realm;

    // 置顶功能可直接使用，也可作为思路，供开发者充分利用RecentContact的tag字段
    public static final long RECENT_TAG_STICKY = 1; // 联系人置顶tag

    public static YuJianFragment newInstance() {
        return new YuJianFragment();
    }

    private View mView;
    private Observer<Void> data_state_observer;
    private Handler handler = new Handler();

    // data
    private List<RecentContact> items;

    private RecentContactAdapter adapter;

    private boolean msgLoaded = false;

    private RecentContactsCallback callback;

    private UserInfoObservable.UserInfoObserver userInfoObserver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = View.inflate(getContext(), R.layout.frag_yujian, null);
        ButterKnife.bind(this, mView);
        initView();
        initFresh();
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestMessages(true);
        registerObservers(true);
    }


    private void initView() {

        realm = Realm.getDefaultInstance();
        callback = new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {

            }

            @Override
            public void onUnreadCountChange(int unreadCount) {
                ReminderManager.getInstance().updateSessionUnreadNum(unreadCount);
            }

            @Override
            public void onItemClick(RecentContact recent) {
                switch (recent.getSessionType()) {
                    case P2P:
                        ChatActivity.start(getContext(), recent.getContactId(), null, SessionTypeEnum.P2P);
                        break;
                    case Team:
                        GroupActivity.start(getContext(), recent.getContactId(), null, SessionTypeEnum.Team);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public String getDigestOfAttachment(MsgAttachment attachment) {
                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                String msgId = recent.getRecentMessageId();
                List<String> uuids = new ArrayList<>(1);
                uuids.add(msgId);
                List<IMMessage> msgs = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuids);
                if (msgs != null && !msgs.isEmpty()) {
                    IMMessage msg = msgs.get(0);
                    Map<String, Object> content = msg.getRemoteExtension();
                    if (content != null && !content.isEmpty()) {
                        return (String) content.get(NS.CONTENT);
                    }
                }
                return null;
            }
        };
        dataSyncState();
        initMessageList();
        serchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalSearchActivity.start(getContext());
            }
        });
    }

    private void dataSyncState() {
         /*
        **describe:在数据未同步完成之前  显示正在加载
        */
        // 等待同步数据完成
        data_state_observer = new Observer<Void>() {
            @Override
            public void onEvent(Void aVoid) {
                date_state.setVisibility(View.GONE);
            }
        };
        boolean syncCompleted = LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(data_state_observer);
        if (!syncCompleted) {
            date_state.setVisibility(View.VISIBLE);
        }
    }

    //    实现TAdapterDelegate
    @Override
    public Class<? extends TViewHolder> viewHolderAtPosition(int position) {
        SessionTypeEnum type = items.get(position).getSessionType();
        if (type == SessionTypeEnum.Team) {
            return TeamRecentViewHolder.class;  //群聊布局
        } else {
            return CommonRecentViewHolder.class; //普通布局
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean enabled(int position) {
        return true;
    }

    /**
     * 初始化消息列表
     */
    private void initMessageList() {
        items = new ArrayList<>();

        adapter = new RecentContactAdapter(getActivity(), items, this);
        adapter.setCallback(callback);

        listView.setAdapter(adapter);
        listView.setItemsCanFocus(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (callback != null) {
                    RecentContact recent = (RecentContact) parent.getAdapter().getItem(position);
                    callback.onItemClick(recent);
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < listView.getHeaderViewsCount()) {
                    return false;
                }
//                showLongClickMenu((RecentContact) parent.getAdapter().getItem(position));
                showMenu(view, (RecentContact) parent.getAdapter().getItem(position));

                return true;
            }
        });
    }


    private void showMenu(View v, final RecentContact recent) {

        final View view = v;
        int[] xy = new int[2];
        v.getLocationInWindow(xy);
        View menu = View.inflate(getContext(), R.layout.popup_yujian, null);

        final PopupWindow popupWindow = new PopupWindow(menu, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(getContext().getResources().
                getDrawable(R.drawable.nothing));
        popupWindow.setAnimationStyle(R.style.popwindow_anim);

        menu.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int mShowMorePopupWindowWidth = menu.getMeasuredWidth();
        int mShowMorePopupWindowHeight = menu.getMeasuredHeight();
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.showAsDropDown(v,
                -mShowMorePopupWindowWidth / 2 + v.getWidth() / 2,
                -mShowMorePopupWindowHeight - v.getHeight());

        final TextView zhiding = (TextView) menu.findViewById(R.id.zhiding);
        zhiding.setText(isTagSet(recent, RECENT_TAG_STICKY) ? "取消置顶" : "置顶");
        TextView delete = (TextView) menu.findViewById(R.id.delete);

        zhiding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTagSet(recent, RECENT_TAG_STICKY)) {
                    removeTag(recent, RECENT_TAG_STICKY);
                } else {
                    addTag(recent, RECENT_TAG_STICKY);
                }
                NIMClient.getService(MsgService.class).updateRecent(recent);

                refreshMessages(false);
                popupWindow.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NIMClient.getService(MsgService.class).deleteRecentContact(recent);
                items.remove(recent);
                if (recent.getUnreadCount() > 0) {
                    refreshMessages(true);
                } else {
                    notifyDataSetChanged();
                }
                popupWindow.dismiss();
            }
        });

    }

    //  刷新数据 如果没有数据则显示无聊天的提示页面
    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
//        boolean empty = items.isEmpty() && msgLoaded;
//        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
//        emptyHint.setHint("还没有会话，在通讯录中找个人聊聊吧！");
    }

    //    置顶相关
    private void addTag(RecentContact recent, long tag) {
        tag = recent.getTag() | tag;
        recent.setTag(tag);
        final TopChat topChat = new TopChat();
        topChat.setAccount(recent.getContactId());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(topChat);
            }
        });
    }

    private void removeTag(final RecentContact recent, long tag) {
        tag = recent.getTag() & ~tag;
        recent.setTag(tag);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TopChat topChat = realm.where(TopChat.class).equalTo("account", recent.getContactId()).findFirst();
                if (topChat != null) {
                    topChat.deleteFromRealm();
                }
            }
        });
    }

    private boolean isTagSet(RecentContact recent, long tag) {
        return (recent.getTag() & tag) == tag;
    }

    //      查询到的最近联系人
    private List<RecentContact> loadedRecents;

    //      查询最近联系人
    private void requestMessages(boolean delay) {
        if (msgLoaded) {
            return;
        }
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (msgLoaded) {
                    return;
                }
                // 查询最近联系人列表数据
                NIMClient.getService(MsgService.class).queryRecentContacts().
                        setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                            @Override
                            public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                                if (code != ResponseCode.RES_SUCCESS || recents == null) {
                                    return;
                                }
//                                查询数据库  将置顶的会话置顶
                                RealmResults<TopChat> topChats = realm.where(TopChat.class).findAll();
                                if (topChats.size() > 0) {
                                    for (int i = 0; i < topChats.size(); i++) {
                                        for (int j = 0; j < recents.size(); j++) {
                                            if (topChats.get(i).getAccount().equals(recents.get(j).getContactId())) {
                                                addTag(recents.get(j), RECENT_TAG_STICKY);
                                            }
                                        }
                                    }
                                }
                                loadedRecents = recents;

                                // 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
                                //
                                msgLoaded = true;
                                if (isAdded()) {
                                    onRecentContactsLoaded();
                                }
                            }
                        });
            }
        }, delay ? 250 : 0);
    }

    private void RefreshContact() {
        NIMClient.getService(MsgService.class).queryRecentContacts().
                setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                        if (code != ResponseCode.RES_SUCCESS || recents == null) {
                            return;
                        }
//                                查询数据库  将置顶的会话置顶
                        if (realm.isClosed()){
                            return;
                        }
                        RealmResults<TopChat> topChats = realm.where(TopChat.class).findAll();
                        if (topChats.size() > 0) {
                            for (int i = 0; i < topChats.size(); i++) {
                                for (int j = 0; j < recents.size(); j++) {
                                    if (topChats.get(i).getAccount().equals(recents.get(j).getContactId())) {
                                        addTag(recents.get(j), RECENT_TAG_STICKY);
                                    }
                                }
                            }
                        }
                        loadedRecents = recents;

                        // 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
                        //
                        msgLoaded = true;
                        if (isAdded()) {
                            onRecentContactsLoaded();
                        }
                    }
                });
    }

    //      刷新items
    private void onRecentContactsLoaded() {
        items.clear();
        if (loadedRecents != null) {
            items.addAll(loadedRecents);
            loadedRecents = null;
        }
        refreshMessages(true);

        if (callback != null) {
            callback.onRecentContactsLoaded();
        }
    }

    /**
     * description:刷新数据 并给数据排序
     *
     * @param unreadChanged 未读信息数目有无改的
     * @return
     */

    private void refreshMessages(boolean unreadChanged) {
        sortRecentContacts(items);
        notifyDataSetChanged();

        if (unreadChanged) {

            // 方式一：累加每个最近联系人的未读（快）
            /*
            int unreadNum = 0;
            for (RecentContact r : items) {
                unreadNum += r.getUnreadCount();
            }
            */

            // 方式二：直接从SDK读取（相对慢）
            int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();

            if (callback != null) {
                callback.onUnreadCountChange(unreadNum);
            }
        }
    }

    /**
     * **************************** 排序 ***********************************
     */
    private void sortRecentContacts(List<RecentContact> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    private static Comparator<RecentContact> comp = new Comparator<RecentContact>() {

        @Override
        public int compare(RecentContact o1, RecentContact o2) {
            // 先比较置顶tag
            long sticky = (o1.getTag() & RECENT_TAG_STICKY) - (o2.getTag() & RECENT_TAG_STICKY);
            if (sticky != 0) {
                return sticky > 0 ? -1 : 1;
            } else {
                long time = o1.getTime() - o2.getTime();
                return time == 0 ? 0 : (time > 0 ? -1 : 1);
            }
        }
    };


    private void registerObservers(boolean register) {
        if (register) {
            requestSystemMessageUnreadCount();
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
        //      注册未读消息观察者
        registerMsgUnreadInfoObserver(register);
        registerSystemMessageObservers(register);
        //      监听账号在线状态
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);

        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeRecentContact(messageObserver, register);
        service.observeMsgStatus(statusObserver, register);
        service.observeRecentContactDeleted(deleteObserver, register);
        registerTeamUpdateObserver(register);
        registerTeamMemberUpdateObserver(register);
        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
    }

    /**
     * 注册群信息&群成员更新监听
     */
    private void registerTeamUpdateObserver(boolean register) {
        if (register) {
            TeamDataCache.getInstance().registerTeamDataChangedObserver(teamDataChangedObserver);
        } else {
            TeamDataCache.getInstance().unregisterTeamDataChangedObserver(teamDataChangedObserver);
        }
    }

    private void registerTeamMemberUpdateObserver(boolean register) {
        if (register) {
            TeamDataCache.getInstance().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        } else {
            TeamDataCache.getInstance().unregisterTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        }
    }

    //    如果有新联系人变化 替换掉以前的相同联系人
    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> messages) {
            int index;
            for (RecentContact msg : messages) {
                index = -1;
                for (int i = 0; i < items.size(); i++) {
                    if (msg.getContactId().equals(items.get(i).getContactId())
                            && msg.getSessionType() == (items.get(i).getSessionType())) {
                        index = i;
                        break;
                    }
                }

                if (index >= 0) {
                    items.remove(index);
                }

                items.add(msg);
            }

            refreshMessages(true);
        }
    };

    //    若变化的消息在联系人列表里 刷新联系人的信息
    Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            int index = getItemIndex(message.getUuid());
            if (index >= 0 && index < items.size()) {
                RecentContact item = items.get(index);
                item.setMsgStatus(message.getStatus());
                refreshViewHolderByIndex(index);
            }
        }
    };

    //  若删除了一个联系人 判断是否在列表里 在的话删除该项并刷新
//  若返回的数据为null 表明全部删除 清空列表
    Observer<RecentContact> deleteObserver = new Observer<RecentContact>() {
        @Override
        public void onEvent(RecentContact recentContact) {
            if (recentContact != null) {
                for (RecentContact item : items) {
                    if (TextUtils.equals(item.getContactId(), recentContact.getContactId())
                            && item.getSessionType() == recentContact.getSessionType()) {
                        items.remove(item);
                        refreshMessages(true);
                        break;
                    }
                }
            } else {
                items.clear();
                refreshMessages(true);
            }
        }
    };
    //  监听群组信息变化  个人觉得没有卵用
    TeamDataCache.TeamDataChangedObserver teamDataChangedObserver = new TeamDataCache.TeamDataChangedObserver() {

        @Override
        public void onUpdateTeams(List<Team> teams) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeam(Team team) {

        }
    };

    TeamDataCache.TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamDataCache.TeamMemberDataChangedObserver() {
        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeamMember(TeamMember member) {

        }
    };

    //  通过信息uuid获得该信息是否在列表里
    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            RecentContact item = items.get(i);
            if (TextUtils.equals(item.getRecentMessageId(), uuid)) {
                return i;
            }
        }

        return -1;
    }

    //  刷新指定项
    protected void refreshViewHolderByIndex(final int index) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Object tag = ListViewUtil.getViewHolderByIndex(listView, index);
                if (tag instanceof RecentViewHolder) {
                    RecentViewHolder viewHolder = (RecentViewHolder) tag;
                    viewHolder.refreshCurrentItem();
                }
            }
        });
    }

    public void setCallback(RecentContactsCallback callback) {
        this.callback = callback;
    }

    private void registerUserInfoObserver() {
        if (userInfoObserver == null) {
            userInfoObserver = new UserInfoObservable.UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    refreshMessages(false);
                }
            };
        }

        UserInfoHelper.registerObserver(userInfoObserver);
    }

    private void unregisterUserInfoObserver() {
        if (userInfoObserver != null) {
            UserInfoHelper.unregisterObserver(userInfoObserver);
        }
    }

    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            refreshMessages(false);
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            refreshMessages(false);
        }
    };

    private void initFresh() {
        LayoutHelp.initPTR(ptrFrameLayout, false, new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 1500);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        enableMsgNotification(false);
        RefreshContact();
    }

    //  在fragment展示 隐藏时设置会话状态
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            enableMsgNotification(true);
        } else {
            enableMsgNotification(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        enableMsgNotification(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        realm.close();
        registerObservers(false);
    }

    @OnClick({R.id.schoolfellow, R.id.serch_layout, R.id.friend, R.id.current_state})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.schoolfellow:
                Intent fellow_intent = new Intent(getContext(), XiaoYouActivity.class);
                startActivity(fellow_intent);
                break;
            case R.id.serch_layout:
                break;
            case R.id.friend:
                Intent friendIntent = new Intent(getContext(), FriendActivity.class);
                startActivity(friendIntent);
                break;
            case R.id.current_state:
                AppNetUtil.LoginIm(getContext());
                break;

        }
    }


    //  设置消息通知
    private void enableMsgNotification(boolean enable) {
        if (enable) {
            /**
             * 设置最近联系人的消息为已读
             *
             * @param account,    聊天对象帐号，或者以下两个值：
             *                    {@link #MSG_CHATTING_ACCOUNT_ALL} 目前没有与任何人对话，但能看到消息提醒（比如在消息列表界面），不需要在状态栏做消息通知
             *                    {@link #MSG_CHATTING_ACCOUNT_NONE} 目前没有与任何人对话，需要状态栏消息通知
             */
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        } else {

            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);

        }

        Log.d("state", "" + enable);
    }

    /**
     * 注册未读消息数量观察者
     */
    private void registerMsgUnreadInfoObserver(boolean register) {
        if (register) {
            ReminderManager.getInstance().registerUnreadNumChangedCallback(this);
        } else {
            ReminderManager.getInstance().unregisterUnreadNumChangedCallback(this);
        }
    }

    /**
     * 未读消息数量观察者实现
     */
    @Override
    public void onUnreadNumChanged(ReminderItem item) {
//        MainTab tab = MainTab.fromReminderId(item.getId());
//        if (tab != null) {
//            tabs.updateTab(tab.tabIndex, item);
//        }
    }


    /**
     * 注册/注销系统消息未读数变化
     *
     * @param register
     */
    private void registerSystemMessageObservers(boolean register) {
        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(sysMsgUnreadCountChangedObserver,
                register);
    }

    private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer unreadCount) {
            SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unreadCount);
            ReminderManager.getInstance().updateContactUnreadNum(unreadCount);
        }
    };

    /**
     * 查询系统消息未读数
     */
    private void requestSystemMessageUnreadCount() {
        int unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountBlock();
        SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unread);
        ReminderManager.getInstance().updateContactUnreadNum(unread);
    }

    /**
     * 用户状态变化
     */
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                kickOut(code);
            } else {
                if (code == StatusCode.NET_BROKEN) {
                    noNetLay.setVisibility(View.VISIBLE);
                    currentState.setText(R.string.no_net);
                } else if (code == StatusCode.UNLOGIN) {
                    noNetLay.setVisibility(View.VISIBLE);
                    currentState.setText(R.string.nim_status_unlogin);
                } else if (code == StatusCode.CONNECTING) {
                    noNetLay.setVisibility(View.VISIBLE);
                    currentState.setText(R.string.nim_status_connecting);
                } else if (code == StatusCode.LOGINING) {
                    noNetLay.setVisibility(View.VISIBLE);
                    currentState.setText(R.string.nim_status_logining);
                } else {
                    noNetLay.setVisibility(View.GONE);
                }
            }
        }
    };

    //    被踢掉的情况
    private void kickOut(StatusCode code) {
        if (code == StatusCode.PWD_ERROR) {
            noNetLay.setVisibility(View.VISIBLE);
            currentState.setText("您处于离线状态");
        }
    }

}
