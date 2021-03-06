package com.xiaoshangxing.yujian.IM;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.utils.NotifycationUtil;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.yujian.IM.Contact.ContactEventListener;
import com.xiaoshangxing.yujian.IM.Contact.ContactProvider;
import com.xiaoshangxing.yujian.IM.CustomMessage.CustomAttachParser;
import com.xiaoshangxing.yujian.IM.cache.DataCacheManager;
import com.xiaoshangxing.yujian.IM.cache.FriendDataCache;
import com.xiaoshangxing.yujian.IM.kit.ImageKit.ImageLoaderKit;
import com.xiaoshangxing.yujian.IM.kit.LogUtil;
import com.xiaoshangxing.yujian.IM.kit.LoginSyncDataStatusObserver;
import com.xiaoshangxing.yujian.IM.kit.MsgForwardFilter;
import com.xiaoshangxing.yujian.IM.kit.storage.StorageType;
import com.xiaoshangxing.yujian.IM.kit.storage.StorageUtil;
import com.xiaoshangxing.yujian.IM.uinfo.UserInfoHelper;

import org.json.JSONObject;

import java.util.List;

/**
 * UIKit能力输出类。
 */
public final class NimUIKit {

    // context
    private static Context context;

    // 自己的用户帐号
    private static String account;

    // 用户信息提供者
    private static UserInfoProvider userInfoProvider;

    // 通讯录信息提供者
    private static ContactProvider contactProvider;

    // 图片加载、缓存与管理组件
    private static ImageLoaderKit imageLoaderKit;

    // 转发消息过滤器
    private static MsgForwardFilter msgForwardFilter;

    // 通讯录列表一些点击事件的响应处理函数
    private static ContactEventListener contactEventListener;

    /**
     * 初始化UIKit，须传入context以及用户信息提供者
     *
     * @param context          上下文
     * @param userInfoProvider 用户信息提供者
     * @param contactProvider  通讯录信息提供者
     */
    public static void init(Context context, UserInfoProvider userInfoProvider, ContactProvider contactProvider) {
        NimUIKit.context = context.getApplicationContext();
        NimUIKit.userInfoProvider = userInfoProvider;
        NimUIKit.contactProvider = contactProvider;
        NimUIKit.imageLoaderKit = new ImageLoaderKit(context, null);

        // 注册自定义消息附件解析器
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());

        // init data cache
        LoginSyncDataStatusObserver.getInstance().registerLoginSyncDataStatus(true);  // 监听登录同步数据完成通知
        DataCacheManager.observeSDKDataChanged(true);
        if (!TextUtils.isEmpty(getAccount())) {
            DataCacheManager.buildDataCache(); // build data cache on auto login
        }

        // init tools
        StorageUtil.init(context, null);
        ScreenUtils.init(context);

        // init log
        String path = StorageUtil.getDirectoryByDirType(StorageType.TYPE_LOG);
        LogUtil.init(path, Log.DEBUG);

        //自定义推送
        Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
            @Override
            public void onEvent(CustomNotification message) {
                String content = message.getContent();
                Log.d("推送", "-----" + content);
                try {
                    JSONObject json = new JSONObject(content);
                    String id = json.getString(NS.PUSH_TYPE);
                    //忽略掉正在输入的自定义推送
                    if (!id.equals(NotifycationUtil.NT_IM_TYPING)) {
                        NotifycationUtil.parsePushMsg(message.getContent());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };


        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, true);
        //注册用户资料变更监听 主要是更新实名认证状态
        UserInfoCache.getInstance().registerDataChangeListner(true);
        //注册新加好友通知
        FriendDataCache.getInstance().registerNewFriendListner(true);
    }


    /**
     * 释放缓存，一般在注销时调用
     */
    public static void clearCache() {
        DataCacheManager.clearDataCache();
    }

    public static Context getContext() {
        return context;
    }

    public static String getAccount() {
        return account;
    }

    /**
     * 设置当前登录用户的帐号
     *
     * @param account 帐号
     */
    public static void setAccount(String account) {
        NimUIKit.account = account;
    }

    public static UserInfoProvider getUserInfoProvider() {
        return userInfoProvider;
    }

    public static ContactProvider getContactProvider() {
        return contactProvider;
    }


    /**
     * 根据消息附件类型注册对应的消息项展示ViewHolder
     *
     * @param attach     附件类型
     * @param viewHolder 消息ViewHolder
     */
//    public static void registerMsgItemViewHolder(Class<? extends MsgAttachment> attach, Class<? extends MsgViewHolderBase> viewHolder) {
//        MsgViewHolderFactory.register(attach, viewHolder);
//    }

    /**
     * 注册Tip类型消息项展示ViewHolder
     *
     * @param viewHolder Tip消息ViewHolder
     */
//    public static void registerTipMsgViewHolder(Class<? extends MsgViewHolderBase> viewHolder) {
//        MsgViewHolderFactory.registerTipMsgViewHolder(viewHolder);
//    }
    public static ImageLoaderKit getImageLoaderKit() {
        return imageLoaderKit;
    }

//    /**
//     * 获取聊天界面事件监听器
//     *
//     * @return
//     */
//    public static SessionEventListener getSessionListener() {
//        return sessionListener;
//    }

//    /**
//     * 设置聊天界面的事件监听器
//     *
//     * @param sessionListener
//     */
//    public static void setSessionListener(SessionEventListener sessionListener) {
//        NimUIKit.sessionListener = sessionListener;
//    }

    /**
     * 获取通讯录列表的事件监听器
     *
     * @return
     */
    public static ContactEventListener getContactEventListener() {
        return contactEventListener;
    }

    /**
     * 设置通讯录列表的事件监听器
     *
     * @param contactEventListener
     */
    public static void setContactEventListener(ContactEventListener contactEventListener) {
        NimUIKit.contactEventListener = contactEventListener;
    }

    /**
     * 当用户资料发生改动时，请调用此接口，通知更新UI
     *
     * @param accounts 有用户信息改动的帐号列表
     */
    public static void notifyUserInfoChanged(List<String> accounts) {
        UserInfoHelper.notifyChanged(accounts);
    }

    /**
     * 获取转发消息过滤的监听器
     *
     * @return
     */
    public static MsgForwardFilter getMsgForwardFilter() {
        return msgForwardFilter;
    }

    /**
     * 设置转发消息过滤的监听器
     *
     * @param msgForwardFilter
     */
    public static void setMsgForwardFilter(MsgForwardFilter msgForwardFilter) {
        NimUIKit.msgForwardFilter = msgForwardFilter;
    }
}
