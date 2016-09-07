package com.xiaoshangxing.yujian.ChatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.utils.ClipboardUtil;
import com.xiaoshangxing.utils.CustomAlertDialog;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.layout.AutoRefreshListView;
import com.xiaoshangxing.utils.layout.MessageListView;
import com.xiaoshangxing.utils.normalUtils.SPUtils;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.yujian.IM.NimUIKit;
import com.xiaoshangxing.yujian.IM.kit.ListViewUtil;
import com.xiaoshangxing.yujian.IM.media.BitmapDecoder;
import com.xiaoshangxing.yujian.IM.uinfo.UserInfoHelper;
import com.xiaoshangxing.yujian.IM.uinfo.UserInfoObservable;
import com.xiaoshangxing.yujian.IM.viewHodler.MessageVH.MessageAudioControl;
import com.xiaoshangxing.yujian.IM.viewHodler.MessageVH.MsgAdapter;
import com.xiaoshangxing.yujian.IM.viewHodler.MessageVH.MsgViewHolderBase;
import com.xiaoshangxing.yujian.IM.viewHodler.MessageVH.MsgViewHolderFactory;
import com.xiaoshangxing.yujian.IM.viewHodler.TAdapterDelegate;
import com.xiaoshangxing.yujian.IM.viewHodler.TViewHolder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 消息收发模块
 * Created by hzxuwen on 2015/6/10.
 */
public class MessageListPanel implements TAdapterDelegate {

    private static final int REQUEST_CODE_FORWARD_PERSON = 0x01;
    private static final int REQUEST_CODE_FORWARD_TEAM = 0x02;

    // container
    private Container container;
    private View rootView;

    // message list view
    private MessageListView messageListView;
    private List<IMMessage> items;
    private MsgAdapter adapter;
    private ImageView listviewBk;

    // 新消息到达提醒
    private IncomingMsgPrompt incomingMsgPrompt;
    private Handler uiHandler;

    // 仅显示消息记录，不接收和发送消息
    private boolean recordOnly;
    // 从服务器拉取消息记录
    private boolean remote;

    // 初始时是否定位到指定消息
    private boolean jump;

    // 语音转文字
    private VoiceTrans voiceTrans;

    // 待转发消息
    private IMMessage forwardMessage;

    // 背景图片缓存
    private static Pair<String, Bitmap> background;

    public MessageListPanel(Container container, View rootView) {
        this(container, rootView, null, false, false);
    }

    public MessageListPanel(Container container, View rootView, boolean recordOnly, boolean remote) {
        this(container, rootView, null, recordOnly, remote);
    }

    public MessageListPanel(Container container, View rootView, IMMessage anchor, boolean recordOnly, boolean remote) {
        this.container = container;
        this.rootView = rootView;
        this.recordOnly = recordOnly;
        this.remote = remote;
        this.jump = anchor != null;

        init(anchor);
    }

    //      听筒有关
    public void onResume() {
        setEarPhoneMode((boolean) SPUtils.get(container.activity, SPUtils.EarPhone, true));
    }

    public void onPause() {
        MessageAudioControl.getInstance(container.activity).stopAudio();
    }

    public void onDestroy() {
        registerObservers(false);
    }

    public boolean onBackPressed() {
        uiHandler.removeCallbacks(null);
        MessageAudioControl.getInstance(container.activity).stopAudio(); // 界面返回，停止语音播放
        if (voiceTrans != null && voiceTrans.isShow()) {
            voiceTrans.hide();
            return true;
        }
        return false;
    }

    public void reload(Container container, IMMessage anchor) {
        this.container = container;
        items.clear();
        // 重新load
        messageListView.setOnRefreshListener(new MessageLoader(anchor, remote));
    }

    public void jumpReload() {
        if (jump) {
            jump = false;
            items.clear();
            messageListView.setOnRefreshListener(new MessageLoader(null, remote));
        }
    }

    private void init(IMMessage anchor) {
        initListView(anchor);

        this.uiHandler = new Handler();
//        新消息展示
        if (!recordOnly) {
            incomingMsgPrompt = new IncomingMsgPrompt(container.activity, rootView, messageListView, uiHandler);
        }

        registerObservers(true);
    }

    private void initListView(IMMessage anchor) {
        items = new ArrayList<>();
        adapter = new MsgAdapter(container.activity, items, this);
        adapter.setEventListener(new MsgItemEventListener());
//      聊天背景
        listviewBk = (ImageView) rootView.findViewById(R.id.message_activity_background);
        listviewBk.setBackgroundColor(container.activity.getResources().getColor(R.color.w3));
        messageListView = (MessageListView) rootView.findViewById(R.id.messageListView);
        messageListView.requestDisallowInterceptTouchEvent(true);

        if (recordOnly && !remote || jump) {
            messageListView.setMode(AutoRefreshListView.Mode.BOTH);
        } else {
            messageListView.setMode(AutoRefreshListView.Mode.START);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            messageListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        // adapter
        messageListView.setAdapter(adapter);
//      关闭输入面板
        messageListView.setListViewEventListener(new MessageListView.OnListViewEventListener() {
            @Override
            public void onListViewStartScroll() {
                container.proxy.shouldCollapseInputPanel();
            }
        });
        messageListView.setOnRefreshListener(new MessageLoader(anchor, remote));

    }

    // 刷新消息列表
    public void refreshMessageList() {
        container.activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    //  滑动到底部
    public void scrollToBottom() {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ListViewUtil.scrollToBottom(messageListView);
            }
        }, 200);
    }

    //  滑动到指定位置
    public void scrollToItem(final int position) {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ListViewUtil.scrollToPosition(messageListView, position, 0);
            }
        }, 200);
    }

    //  滑动到锚点
    public void scrollToAnchor(final IMMessage anchor) {
        if (anchor == null) {
            return;
        }

        int position = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getUuid().equals(anchor.getUuid())) {
                position = i;
                break;
            }
        }

        if (position >= 0) {
            final int jumpTo = position == 0 ? 0 : position + messageListView.getHeaderViewsCount();
            uiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ListViewUtil.scrollToPosition(messageListView, jumpTo, jumpTo == 0 ? 0 : ScreenUtils.dip2px(30));
                }
            }, 30);
        }
    }

    //  接受到新消息
    public void onIncomingMessage(List<IMMessage> messages) {
        boolean needScrollToBottom = ListViewUtil.isLastMessageVisible(messageListView);
        boolean needRefresh = false;
        List<IMMessage> addedListItems = new ArrayList<>(messages.size());
        for (IMMessage message : messages) {
            if (isMyMessage(message)) {
                items.add(message);
                addedListItems.add(message);
                needRefresh = true;
            }
        }
        if (needRefresh) {
            adapter.notifyDataSetChanged();
        }
//      刷新时间显示
        adapter.updateShowTimeItem(addedListItems, false, true);

        // incoming messages tip
//        展示最新的一条消息  如果本来就在最底部 则刷新数据并滚动到最底部  否则交由incomingMsgPrompt展示
        IMMessage lastMsg = messages.get(messages.size() - 1);
        if (isMyMessage(lastMsg)) {
            if (needScrollToBottom) {
                ListViewUtil.scrollToBottom(messageListView);
            } else if (incomingMsgPrompt != null && lastMsg.getSessionType() != SessionTypeEnum.ChatRoom) {
                incomingMsgPrompt.show(lastMsg);
            }
        }
    }

    // 发送消息后，更新本地消息列表
    public void onMsgSend(IMMessage message) {
        // add to listView and refresh
        items.add(message);
        List<IMMessage> addedListItems = new ArrayList<>(1);
        addedListItems.add(message);
//        刷新时间显示
        adapter.updateShowTimeItem(addedListItems, false, true);

        adapter.notifyDataSetChanged();
        ListViewUtil.scrollToBottom(messageListView);
    }

    /**
     * *************** implements TAdapterDelegate ***************
     */
    @Override
    public int getViewTypeCount() {
        return MsgViewHolderFactory.getViewTypeCount();
    }

    @Override
    public Class<? extends TViewHolder> viewHolderAtPosition(int position) {
        return MsgViewHolderFactory.getViewHolderByType(items.get(position));
    }

    @Override
    public boolean enabled(int position) {
        return false;
    }

    /**
     * ************************* 观察者 ********************************
     */

    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeMsgStatus(messageStatusObserver, register);
        service.observeAttachmentProgress(attachmentProgressObserver, register);
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }

        MessageListPanelHelper.getInstance().registerObserver(incomingLocalMessageObserver, register);
    }

    /**
     * 消息状态变化观察者
     */
    Observer<IMMessage> messageStatusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            if (isMyMessage(message)) {
                onMessageStatusChange(message);
            }
        }
    };

    /**
     * 消息附件上传/下载进度观察者
     */
    Observer<AttachmentProgress> attachmentProgressObserver = new Observer<AttachmentProgress>() {
        @Override
        public void onEvent(AttachmentProgress progress) {
            onAttachmentProgressChange(progress);
        }
    };

    /**
     * 本地消息接收观察者
     */
    MessageListPanelHelper.LocalMessageObserver incomingLocalMessageObserver = new MessageListPanelHelper.LocalMessageObserver() {
        @Override
        public void onAddMessage(IMMessage message) {
            if (message == null || !container.account.equals(message.getSessionId())) {
                return;
            }

            onMsgSend(message);
        }

        @Override
        public void onClearMessages(String account) {
            items.clear();
            refreshMessageList();
        }
    };

    //  消息状态变化  实时更新listview中的item
    private void onMessageStatusChange(IMMessage message) {
        int index = getItemIndex(message.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            item.setStatus(message.getStatus());
            item.setAttachStatus(message.getAttachStatus());
            if (item.getAttachment() instanceof AudioAttachment) {
                item.setAttachment(message.getAttachment());
            }
            refreshViewHolderByIndex(index);
        }
    }

    //  实时更新文件上传下载状态
    private void onAttachmentProgressChange(AttachmentProgress progress) {
        int index = getItemIndex(progress.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            float value = (float) progress.getTransferred() / (float) progress.getTotal();
            adapter.putProgress(item, value);
            refreshViewHolderByIndex(index);
        }
    }

    //   判断是否是该会话的消息
    public boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == container.sessionType
                && message.getSessionId() != null
                && message.getSessionId().equals(container.account);
    }

    /**
     * 刷新单条消息
     * 当目标消息可见才刷新
     *
     * @param index
     */
    private void refreshViewHolderByIndex(final int index) {
        container.activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (index < 0) {
                    return;
                }

                Object tag = ListViewUtil.getViewHolderByIndex(messageListView, index);
                if (tag instanceof MsgViewHolderBase) {
                    MsgViewHolderBase viewHolder = (MsgViewHolderBase) tag;
                    viewHolder.refreshCurrentItem();
                }
            }
        });
    }

    //    通过uuid获取message在items中的位置
    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            IMMessage message = items.get(i);
            if (TextUtils.equals(message.getUuid(), uuid)) {
                return i;
            }
        }

        return -1;
    }

    //      设置聊天背景
    public void setChattingBackground(String uriString, int color) {
        if (uriString != null) {
            Uri uri = Uri.parse(uriString);
            if (uri.getScheme().equalsIgnoreCase("file") && uri.getPath() != null) {
                listviewBk.setImageBitmap(getBackground(uri.getPath()));
            } else if (uri.getScheme().equalsIgnoreCase("android.resource")) {
                List<String> paths = uri.getPathSegments();
                if (paths == null || paths.size() != 2) {
                    return;
                }
                String type = paths.get(0);
                String name = paths.get(1);
                String pkg = uri.getHost();
                int resId = container.activity.getResources().getIdentifier(name, type, pkg);
                if (resId != 0) {
                    listviewBk.setBackgroundResource(resId);
                }
            }
        } else if (color != 0) {
            listviewBk.setBackgroundColor(color);
        }
    }

    /*
    **describe:消息加载
    */
    private class MessageLoader implements AutoRefreshListView.OnRefreshListener {

        private static final int LOAD_MESSAGE_COUNT = 20;

        private QueryDirectionEnum direction = null;

        private IMMessage anchor;
        private boolean remote;

        private boolean firstLoad = true;

        public MessageLoader(IMMessage anchor, boolean remote) {
            this.anchor = anchor;
            this.remote = remote;
            if (remote) {
                loadFromRemote();
            } else {
                if (anchor == null) {
                    //      加载本地消息
                    loadFromLocal(QueryDirectionEnum.QUERY_OLD);
                } else {
                    // 加载指定anchor的上下文
                    loadAnchorContext();
                }
            }
        }

        private RequestCallback<List<IMMessage>> callback = new RequestCallbackWrapper<List<IMMessage>>() {
            @Override
            public void onResult(int code, List<IMMessage> messages, Throwable exception) {
                if (messages != null) {
                    onMessageLoaded(messages);
                }
            }
        };

        // 加载指定anchor的上下文
        /*
        **describe:先以items的第一项为锚点  查询前面20项
        * 然后以items的最后一项为锚点 查询后面20项
        * 最后滑动到锚点
        */
        private void loadAnchorContext() {
            // query old
            this.direction = QueryDirectionEnum.QUERY_OLD;
            messageListView.onRefreshStart(AutoRefreshListView.Mode.START);
            NIMClient.getService(MsgService.class).queryMessageListEx(anchor(), direction, LOAD_MESSAGE_COUNT, true)
                    .setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
                        @Override
                        public void onResult(int code, List<IMMessage> messages, Throwable exception) {
                            if (code != ResponseCode.RES_SUCCESS || exception != null) {
                                return;
                            }
                            onMessageLoaded(messages);

                            // query new
                            direction = QueryDirectionEnum.QUERY_NEW;
                            messageListView.onRefreshStart(AutoRefreshListView.Mode.END);
                            NIMClient.getService(MsgService.class).queryMessageListEx(anchor(), direction, LOAD_MESSAGE_COUNT, true)
                                    .setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
                                        @Override
                                        public void onResult(int code, List<IMMessage> messages, Throwable exception) {
                                            if (code != ResponseCode.RES_SUCCESS || exception != null) {
                                                return;
                                            }
                                            onMessageLoaded(messages);
                                            // scroll to position
                                            scrollToAnchor(anchor);
                                        }
                                    });
                        }
                    });
        }

        //      加载本地消息
        private void loadFromLocal(QueryDirectionEnum direction) {
            this.direction = direction;
            messageListView.onRefreshStart(direction == QueryDirectionEnum.QUERY_NEW ? AutoRefreshListView.Mode.END : AutoRefreshListView.Mode.START);
            NIMClient.getService(MsgService.class).queryMessageListEx(anchor(), direction, LOAD_MESSAGE_COUNT, true)
                    .setCallback(callback);
        }

        //      加载云端消息
        private void loadFromRemote() {
            this.direction = QueryDirectionEnum.QUERY_OLD;
            NIMClient.getService(MsgService.class).pullMessageHistory(anchor(), LOAD_MESSAGE_COUNT, true)
                    .setCallback(callback);
        }

        //      设置锚点 如果是查询锚点后面的消息 锚点设置为items的最后一项  反之设为items的第一项
        private IMMessage anchor() {
            if (items.size() == 0) {
                return anchor == null ? MessageBuilder.createEmptyMessage(container.account, container.sessionType, 0) : anchor;
            } else {
                int index = (direction == QueryDirectionEnum.QUERY_NEW ? items.size() - 1 : 0);
                return items.get(index);
            }
        }

        /**
         * 历史消息加载处理
         *
         * @param messages
         */
        private void onMessageLoaded(List<IMMessage> messages) {
            int count = messages.size();


            if (remote) {
                Collections.reverse(messages);
            }

            if (firstLoad && items.size() > 0) {
                // 在第一次加载的过程中又收到了新消息，做一下去重
                for (IMMessage message : messages) {
                    for (IMMessage item : items) {
                        if (item.isTheSame(message)) {
                            items.remove(item);
                            break;
                        }
                    }
                }
            }
//          第一次加载且带锚点 则把锚点加入
            if (firstLoad && anchor != null) {
                items.add(anchor);
            }

            List<IMMessage> result = new ArrayList<>();
            for (IMMessage message : messages) {
                result.add(message);
            }
//            若是新消息 则直接加入到items的后面 否则加入到items的第一项
            if (direction == QueryDirectionEnum.QUERY_NEW) {
                items.addAll(result);
            } else {
                items.addAll(0, result);
            }

            // 如果是第一次加载，updateShowTimeItem返回的就是lastShowTimeItem
            if (firstLoad) {
                ListViewUtil.scrollToBottom(messageListView);
                sendReceipt(); // 发送已读回执
            }
//  更新时间
            adapter.updateShowTimeItem(items, true, firstLoad);
            updateReceipt(items); // 更新已读回执标签

            refreshMessageList();
//            listview结束加载
            messageListView.onRefreshComplete(count, LOAD_MESSAGE_COUNT, true);

            firstLoad = false;
        }

        /**
         * *************** OnRefreshListener ***************
         */
        @Override
        public void onRefreshFromStart() {
            if (remote) {
                loadFromRemote();
            } else {
                loadFromLocal(QueryDirectionEnum.QUERY_OLD);
            }
        }

        @Override
        public void onRefreshFromEnd() {
            if (!remote) {
                loadFromLocal(QueryDirectionEnum.QUERY_NEW);
            }
        }
    }

    //  点击失败按钮  长按消息选项
    private class MsgItemEventListener implements MsgAdapter.ViewHolderEventListener {
        /*
        **describe:点击失败按钮
        * 两种情况 一种是发送失败 需要重新发送
        * 第二章是文件上传下载失败 需要重新上传下载
        */
        @Override
        public void onFailedBtnClick(IMMessage message) {
            if (message.getDirect() == MsgDirectionEnum.Out) {
                // 发出的消息，如果是发送失败，直接重发，否则有可能是漫游到的多媒体消息，但文件下载
                if (message.getStatus() == MsgStatusEnum.fail) {
                    resendMessage(message); // 重发
                } else {
                    if (message.getAttachment() instanceof FileAttachment) {
                        FileAttachment attachment = (FileAttachment) message.getAttachment();
                        if (TextUtils.isEmpty(attachment.getPath())
                                && TextUtils.isEmpty(attachment.getThumbPath())) {
                            showReDownloadConfirmDlg(message);
                        }
                    } else {
                        resendMessage(message);
                    }
                }
            } else {
                showReDownloadConfirmDlg(message);
            }
        }

        //      长按事件
        @Override
        public boolean onViewHolderLongClick(View clickView, View viewHolderView, IMMessage item) {
            if (container.proxy.isLongClickEnabled()) {
                showLongClickAction(item);
            }
            return true;
        }

        // 重新下载(对话框提示)
        private void showReDownloadConfirmDlg(final IMMessage message) {
//            EasyAlertDialogHelper.OnDialogActionListener listener = new EasyAlertDialogHelper.OnDialogActionListener() {
//
//                @Override
//                public void doCancelAction() {
//                }
//
//                @Override
//                public void doOkAction() {
//                    // 正常情况收到消息后附件会自动下载。如果下载失败，可调用该接口重新下载
//                    if (message.getAttachment() != null && message.getAttachment() instanceof FileAttachment)
//                        NIMClient.getService(MsgService.class).downloadAttachment(message, true);
//                }
//            };
//
//            final EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(container.activity, null,
//                    container.activity.getString(R.string.repeat_download_message), true, listener);
//            dialog.show();
        }

        // 重发消息到服务器 并设置本地消息对应状态为sending
        private void resendMessage(IMMessage message) {
            // 重置状态为unsent
            int index = getItemIndex(message.getUuid());
            if (index >= 0 && index < items.size()) {
                IMMessage item = items.get(index);
                item.setStatus(MsgStatusEnum.sending);
                refreshViewHolderByIndex(index);
            }

            NIMClient.getService(MsgService.class).sendMessage(message, true);
        }

        /**
         * ****************************** 长按菜单 ********************************
         */

        // 长按消息Item后弹出菜单控制
        private void showLongClickAction(IMMessage selectedItem) {
            onNormalLongClick(selectedItem);
        }

        /**
         * 长按菜单操作
         *
         * @param item
         */
        private void onNormalLongClick(IMMessage item) {
            CustomAlertDialog alertDialog = new CustomAlertDialog(container.activity);
            alertDialog.setCancelable(true);
            alertDialog.setCanceledOnTouchOutside(true);

            prepareDialogItems(item, alertDialog);
            alertDialog.show();
        }

        // 长按消息item的菜单项准备。如果消息item的MsgViewHolder处理长按事件(MsgViewHolderBase#onItemLongClick),且返回为true，
        // 则对应项的长按事件不会调用到此处
        private void prepareDialogItems(final IMMessage selectedItem, CustomAlertDialog alertDialog) {
            MsgTypeEnum msgType = selectedItem.getMsgType();
//          停止播放语音
            MessageAudioControl.getInstance(container.activity).stopAudio();

            // 0 EarPhoneMode   扬声器 听筒转换
            longClickItemEarPhoneMode(alertDialog, msgType);
            // 1 resend         重新发送
            longClickItemResend(selectedItem, alertDialog);
            // 2 copy           复制
            longClickItemCopy(selectedItem, alertDialog, msgType);
            // 3 delete         删除
            longClickItemDelete(selectedItem, alertDialog);
            // 4 trans          语音转换为文字
//            longClickItemVoidToText(selectedItem, alertDialog, msgType);

//            if (!NimUIKit.getMsgForwardFilter().shouldIgnore(selectedItem) && !recordOnly) {
            // 5 forward to person      转发到个人
            longClickItemForwardToPerson(selectedItem, alertDialog);
            // 6 forward to team        转发到群组
            longClickItemForwardToTeam(selectedItem, alertDialog);
//            }
        }

        // 长按菜单项--重发
        private void longClickItemResend(final IMMessage item, CustomAlertDialog alertDialog) {
            if (item.getStatus() != MsgStatusEnum.fail) {
                return;
            }
            alertDialog.addItem("重发", new CustomAlertDialog.onSeparateItemClickListener() {

                @Override
                public void onClick() {
                    onResendMessageItem(item);
                }
            });
        }

        private void onResendMessageItem(IMMessage message) {
            int index = getItemIndex(message.getUuid());
            if (index >= 0) {
                showResendConfirm(message, index); // 重发确认
            }
        }

        private void showResendConfirm(final IMMessage message, final int index) {

            final DialogUtils.Dialog_Center dialog_center = new DialogUtils.Dialog_Center(container.activity);
            dialog_center.Button("确定", "取消").Message("确认重发?").MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
                @Override
                public void onButton1() {
                    dialog_center.close();
                }

                @Override
                public void onButton2() {
                    resendMessage(message);
                    dialog_center.close();
                }
            });
            Dialog dialog = dialog_center.create();
            dialog.show();
            LocationUtil.setWidth(container.activity, dialog,
                    container.activity.getResources().getDimensionPixelSize(R.dimen.x780));
        }

        // 长按菜单项--复制
        private void longClickItemCopy(final IMMessage item, CustomAlertDialog alertDialog, MsgTypeEnum msgType) {
            if (msgType != MsgTypeEnum.text) {
                return;
            }
            alertDialog.addItem("复制", new CustomAlertDialog.onSeparateItemClickListener() {

                @Override
                public void onClick() {
                    onCopyMessageItem(item);
                }
            });
        }

        private void onCopyMessageItem(IMMessage item) {
            ClipboardUtil.clipboardCopyText(container.activity, item.getContent());
        }

        // 长按菜单项--删除
        private void longClickItemDelete(final IMMessage selectedItem, CustomAlertDialog alertDialog) {
            if (recordOnly) {
                return;
            }
            alertDialog.addItem("删除", new CustomAlertDialog.onSeparateItemClickListener() {

                @Override
                public void onClick() {
                    deleteItem(selectedItem);
                }
            });
        }

        public void deleteItem(IMMessage messageItem) {
            NIMClient.getService(MsgService.class).deleteChattingHistory(messageItem);
            List<IMMessage> messages = new ArrayList<>();
            for (IMMessage message : items) {
                if (message.getUuid().equals(messageItem.getUuid())) {
                    continue;
                }
                messages.add(message);
            }
            updateReceipt(messages);
            adapter.deleteItem(messageItem);
        }


        // 长按菜单项 -- 音频转文字   (暂不支持)
        private void longClickItemVoidToText(final IMMessage item, CustomAlertDialog alertDialog, MsgTypeEnum msgType) {
            if (msgType != MsgTypeEnum.audio) return;

            if (item.getDirect() == MsgDirectionEnum.In
                    && item.getAttachStatus() != AttachStatusEnum.transferred)
                return;
            if (item.getDirect() == MsgDirectionEnum.Out
                    && item.getAttachStatus() != AttachStatusEnum.transferred)
                return;

            alertDialog.addItem("转文字", new CustomAlertDialog.onSeparateItemClickListener() {

                @Override
                public void onClick() {
                    onVoiceToText(item);
                }
            });
        }

        // 语音转文字
        private void onVoiceToText(IMMessage item) {
            if (voiceTrans == null)
                voiceTrans = new VoiceTrans(container.activity);
            voiceTrans.voiceToText(item);
            if (item.getDirect() == MsgDirectionEnum.In && item.getStatus() != MsgStatusEnum.read) {
                item.setStatus(MsgStatusEnum.read);
                NIMClient.getService(MsgService.class).updateIMMessageStatus(item);
                adapter.notifyDataSetChanged();
            }
        }

        // 长按菜单项 -- 听筒扬声器切换
        private void longClickItemEarPhoneMode(CustomAlertDialog alertDialog, MsgTypeEnum msgType) {
            if (msgType != MsgTypeEnum.audio) return;

            String content = null;
            if ((boolean) SPUtils.get(container.activity, SPUtils.EarPhone, true)) {
                content = "切换成扬声器播放";
            } else {
                content = "切换成听筒播放";
            }
            final String finalContent = content;
            alertDialog.addItem(content, new CustomAlertDialog.onSeparateItemClickListener() {
                @Override
                public void onClick() {
                    Toast.makeText(container.activity, finalContent, Toast.LENGTH_SHORT).show();
                    setEarPhoneMode(!(boolean) SPUtils.get(container.activity, SPUtils.EarPhone, true));
                }
            });
        }

        // 长按菜单项 -- 转发到个人
        private void longClickItemForwardToPerson(final IMMessage item, CustomAlertDialog alertDialog) {
            alertDialog.addItem("转发给好友", new CustomAlertDialog.onSeparateItemClickListener() {

                @Override
                public void onClick() {
                    forwardMessage = item;
//                    ContactSelectActivity.Option option = new ContactSelectActivity.Option();
//                    option.title = "选择转发的人";
//                    option.type = ContactSelectActivity.ContactSelectType.BUDDY;
//                    option.multi = false;
//                    option.maxSelectNum = 1;
//                    NimUIKit.startContactSelect(container.activity, option, REQUEST_CODE_FORWARD_PERSON);
                    Intent intent = new Intent(container.activity, SelectPersonActivity.class);
                    intent.putExtra(SelectPersonActivity.REQUSET_CODE, REQUEST_CODE_FORWARD_PERSON);
                    intent.putExtra(SelectPersonActivity.LIMIT, 1);
                    container.activity.startActivityForResult(intent, REQUEST_CODE_FORWARD_PERSON);
                }
            });
        }

        // 长按菜单项 -- 转发到群组
        private void longClickItemForwardToTeam(final IMMessage item, CustomAlertDialog alertDialog) {
            alertDialog.addItem("转发到群组", new CustomAlertDialog.onSeparateItemClickListener() {

                @Override
                public void onClick() {
                    forwardMessage = item;
//                    ContactSelectActivity.Option option = new ContactSelectActivity.Option();
//                    option.title = "选择转发的群";
//                    option.type = ContactSelectActivity.ContactSelectType.TEAM;
//                    option.multi = false;
//                    option.maxSelectNum = 1;
//                    NimUIKit.startContactSelect(container.activity, option, REQUEST_CODE_FORWARD_TEAM);
                    Intent intent = new Intent(container.activity, SelectPersonActivity.class);
                    intent.putExtra(SelectPersonActivity.REQUSET_CODE, REQUEST_CODE_FORWARD_TEAM);
                    intent.putExtra(SelectPersonActivity.LIMIT, 1);
                    intent.putExtra(IntentStatic.TYPE, SelectPersonActivity.GROUP);
                    container.activity.startActivityForResult(intent, REQUEST_CODE_FORWARD_TEAM);
                }
            });
        }
    }

    //      设置听筒或扬声器模式
    private void setEarPhoneMode(boolean earPhoneMode) {
        SPUtils.put(container.activity, SPUtils.EarPhone, earPhoneMode);
        MessageAudioControl.getInstance(container.activity).setEarPhoneModeEnable(earPhoneMode);
    }

    //      获取背景图片
    private Bitmap getBackground(String path) {
        if (background != null && path.equals(background.first) && background.second != null) {
            return background.second;
        }

        if (background != null && background.second != null) {
            background.second.recycle();
        }

        Bitmap bitmap = null;
        if (path.startsWith("/android_asset")) {
            String asset = path.substring(path.indexOf("/", 1) + 1);
            try {
                InputStream ais = container.activity.getAssets().open(asset);
                bitmap = BitmapDecoder.decodeSampled(ais, ScreenUtils.screenWidth, ScreenUtils.screenHeight);
                ais.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bitmap = BitmapDecoder.decodeSampled(path, ScreenUtils.screenWidth, ScreenUtils.screenHeight);
        }
        background = new Pair<>(path, bitmap);
        return bitmap;
    }

    private UserInfoObservable.UserInfoObserver uinfoObserver;

    private void registerUserInfoObserver() {
        if (uinfoObserver == null) {
            uinfoObserver = new UserInfoObservable.UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    if (container.sessionType == SessionTypeEnum.P2P) {
                        if (accounts.contains(container.account) || accounts.contains(NimUIKit.getAccount())) {
                            adapter.notifyDataSetChanged();
                        }
                    } else { // 群的，简单的全部重刷
                        adapter.notifyDataSetChanged();
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
     * 收到已读回执（更新VH的已读label）
     */

    public void receiveReceipt() {
        updateReceipt(items);
        refreshMessageList();
    }

    public void updateReceipt(final List<IMMessage> messages) {
        for (int i = messages.size() - 1; i >= 0; i--) {
            if (receiveReceiptCheck(messages.get(i))) {
                adapter.setUuid(messages.get(i).getUuid());
                break;
            }
        }
    }

    //      判断是否已读
    private boolean receiveReceiptCheck(final IMMessage msg) {
        if (msg != null && msg.getSessionType() == SessionTypeEnum.P2P
                && msg.getDirect() == MsgDirectionEnum.Out
                && msg.getMsgType() != MsgTypeEnum.tip
                && msg.getMsgType() != MsgTypeEnum.notification
                && msg.isRemoteRead()) {
            return true;
        }

        return false;
    }

    /**
     * 发送已读回执（需要过滤）
     */

    public void sendReceipt() {
        if (container.account == null || container.sessionType != SessionTypeEnum.P2P) {
            return;
        }

        IMMessage message = getLastReceivedMessage();
        if (!sendReceiptCheck(message)) {
            return;
        }

        NIMClient.getService(MsgService.class).sendMessageReceipt(container.account, message);
    }

    private IMMessage getLastReceivedMessage() {
        IMMessage lastMessage = null;
        for (int i = items.size() - 1; i >= 0; i--) {
            if (sendReceiptCheck(items.get(i))) {
                lastMessage = items.get(i);
                break;
            }
        }

        return lastMessage;
    }

    private boolean sendReceiptCheck(final IMMessage msg) {
        if (msg == null || msg.getDirect() != MsgDirectionEnum.In ||
                msg.getMsgType() == MsgTypeEnum.tip || msg.getMsgType() == MsgTypeEnum.notification) {
            return false; // 非收到的消息，Tip消息和通知类消息，不要发已读回执
        }

        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != Activity.RESULT_OK) {
//            return;
//        }

//        if (selected != null && !selected.isEmpty()) {
        switch (requestCode) {
            case REQUEST_CODE_FORWARD_TEAM:
                ArrayList<String> selected = data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON);
                if (selected != null && !selected.isEmpty()) {
                    for (int i = 0; i < selected.size(); i++) {
                        doForwardMessage(selected.get(i), SessionTypeEnum.Team);
                    }
                }
                break;
            case REQUEST_CODE_FORWARD_PERSON:
                ArrayList<String> selected1 = data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON);
                if (selected1 != null && !selected1.isEmpty()) {
                    doForwardMessage(selected1.get(0), SessionTypeEnum.P2P);
                }
                break;
        }
//        }
    }

    // 转发消息
    private void doForwardMessage(final String sessionId, SessionTypeEnum sessionTypeEnum) {
        IMMessage message = MessageBuilder.createForwardMessage(forwardMessage, sessionId, sessionTypeEnum);
        if (message == null) {
            Toast.makeText(container.activity, "该类型不支持转发", Toast.LENGTH_SHORT).show();
            return;
        }
        NIMClient.getService(MsgService.class).sendMessage(message, false).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(container.activity, "转发成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int i) {
                Toast.makeText(container.activity, "转发失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable throwable) {
                Toast.makeText(container.activity, "转发失败", Toast.LENGTH_SHORT).show();
            }
        });
        if (container.account.equals(sessionId)) {
            onMsgSend(message);
        }
    }
}
