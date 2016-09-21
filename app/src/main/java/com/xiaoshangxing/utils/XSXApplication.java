package com.xiaoshangxing.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.baidu.mapapi.SDKInitializer;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MessageNotifierCustomization;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.model.IMMessageFilter;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.R;
import com.xiaoshangxing.login_register.StartActivity.FlashActivity;
import com.xiaoshangxing.setting.DataSetting;
import com.xiaoshangxing.utils.normalUtils.MyLog;
import com.xiaoshangxing.utils.normalUtils.ScreenUtils;
import com.xiaoshangxing.yujian.IM.Contact.ContactProvider;
import com.xiaoshangxing.yujian.IM.NimUIKit;
import com.xiaoshangxing.yujian.IM.cache.FriendDataCache;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;
import com.xiaoshangxing.yujian.IM.cache.TeamDataCache;
import com.xiaoshangxing.yujian.IM.kit.ImageKit.ImageLoaderKit;
import com.xiaoshangxing.yujian.IM.kit.SystemUtil;
import com.xiaoshangxing.yujian.Serch.PinYin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by FengChaoQun
 * on 2016/6/11
 */
public class XSXApplication extends Application {

    private Map<String,Activity> mList=new HashMap<String, Activity>();
    private int activityCount = 0;
    private static XSXApplication instance;
    public static StatusBarNotificationConfig notificationConfig;

    public static XSXApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = this;
        }

//      初始化数据库
        RealmConfiguration config = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

//      初始化百度地图
        SDKInitializer.initialize(getApplicationContext());

//        初始化IM
        NIMClient.init(this, getLoginInfo(), getOptions());

        if (inMainProcess()) {

            // 初始化UIKit模块
            initUIKit();

            // 注册通知消息过滤器
            registerIMMessageFilter();

            // 初始化消息提醒
            NIMClient.toggleNotification(DataSetting.IsAcceptedNews(this));

        }

        /*
        **describe:集中监视Activity生命周期
        */
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                addActivity(activity);
                MyLog.d(activity,"created");
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                MyLog.d(activity,"resumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                removeActivity(activity);
                activityCount--;
                MyLog.d(activity, "destroyed");
            }
        });

    }

    /*
    **describe:添加Activity到Map中
    */

    public void addActivity(Activity activity){
        mList.put(activity.toString(),activity);
        activityCount++;
    }

    /*
    **describe:移除Activity
    */
    public void removeActivity(Activity activity) {
        mList.remove(activity.toString());
    }

    /*
    **describe:完全退出APP
    */
    public void exit() {
        try {
            for(Map.Entry<String, Activity> entry:mList.entrySet()){
                entry.getValue().finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    **describe:finish某个activity
    */
    public void finish_activity(String tag){
        if (mList.containsKey(tag)){
            mList.get(tag).finish();
        }
    }

    //  判断是否处于主线程
    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }

    /**
     * 通知消息过滤器（如果过滤则该消息不存储不上报）
     */
    private void registerIMMessageFilter() {
        NIMClient.getService(MsgService.class).registerIMMessageFilter(new IMMessageFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (message.getAttachment() != null) {
                    if (message.getAttachment() instanceof UpdateTeamAttachment) {
                        UpdateTeamAttachment attachment = (UpdateTeamAttachment) message.getAttachment();
                        for (Map.Entry<TeamFieldEnum, Object> field : attachment.getUpdatedFields().entrySet()) {
                            if (field.getKey() == TeamFieldEnum.ICON) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        });
    }

    private void initUIKit() {
        // init pinyin
        PinYin.init(this);
        PinYin.validate();
        // 初始化，需要传入用户信息提供者
        NimUIKit.init(this, infoProvider, contactProvider);
    }


    //    获取用户信息
    private LoginInfo getLoginInfo() {
//        String account = (String) SPUtils.get(this, SPUtils.CURRENT_COUNT, SPUtils.DEFAULT_STRING);
//        if (!(boolean) SPUtils.get(this, SPUtils.IS_QUIT, true) && !account.equals(SPUtils.DEFAULT_STRING)) {
//            NimUIKit.setAccount(account);
//            return new LoginInfo(account, "123456");
//        } else {
//            return null;
//        }
        return null;
    }

    //  配置SDK
    private SDKOptions getOptions() {

        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
//        if (config == null) {
//            config = new StatusBarNotificationConfig();
//        }
        // 点击通知需要跳转到的界面
        config.notificationEntrance = FlashActivity.class;
        config.notificationSmallIconId = R.mipmap.cirecleimage_default;

        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";

        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;

        options.statusBarNotificationConfig = config;
        notificationConfig = config;
        DataSetting.setStatusConfig(config);

        // 配置保存图片，文件，log等数据的目录
        options.sdkStorageRootPath = FileUtils.getImCache();

        // 配置数据库加密秘钥
        options.databaseEncryptKey = "XSX";

        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小，
        options.thumbnailSize = (int) (165.0 / 320.0 * ScreenUtils.screenWidth);

        // 用户信息提供者
        options.userInfoProvider = infoProvider;

        // 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
        options.messageNotifierCustomization = messageNotifierCustomization;

        return options;
    }

    //    用户信息提供者
    private UserInfoProvider infoProvider = new UserInfoProvider() {
        @Override
        public UserInfo getUserInfo(String account) {
            UserInfo user = NimUserInfoCache.getInstance().getUserInfo(account);
            if (user == null) {
                NimUserInfoCache.getInstance().getUserInfoFromRemote(account, null);
            }

            return null;
        }

        @Override
        public int getDefaultIconResId() {
            return R.mipmap.cirecleimage_default;
        }

        @Override
        public Bitmap getTeamIcon(String teamId) {
            Drawable drawable = getResources().getDrawable(R.mipmap.cirecleimage_default);
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }

            return null;
        }

        @Override
        public Bitmap getAvatarForMessageNotifier(String account) {
            /**
             * 注意：这里最好从缓存里拿，如果读取本地头像可能导致UI进程阻塞，导致通知栏提醒延时弹出。
             */
            UserInfo user = getUserInfo(account);
            return (user != null) ? ImageLoaderKit.getNotificationBitmapFromCache(user) : null;
        }

        @Override
        public String getDisplayNameForMessageNotifier(String account, String sessionId, SessionTypeEnum sessionType) {
            String nick = null;
            if (sessionType == SessionTypeEnum.P2P) {
                nick = NimUserInfoCache.getInstance().getAlias(account);
            } else if (sessionType == SessionTypeEnum.Team) {
                nick = TeamDataCache.getInstance().getTeamNick(sessionId, account);
                if (TextUtils.isEmpty(nick)) {
                    nick = NimUserInfoCache.getInstance().getAlias(account);
                }
            }
            // 返回null，交给sdk处理。如果对方有设置nick，sdk会显示nick
            if (TextUtils.isEmpty(nick)) {
                return null;
            }

            return nick;
        }
    };

    //  好友信息提供者
    private ContactProvider contactProvider = new ContactProvider() {
        @Override
        public List<UserInfoProvider.UserInfo> getUserInfoOfMyFriends() {
            List<NimUserInfo> nimUsers = NimUserInfoCache.getInstance().getAllUsersOfMyFriend();
            List<UserInfoProvider.UserInfo> users = new ArrayList<>(nimUsers.size());
            if (!nimUsers.isEmpty()) {
                users.addAll(nimUsers);
            }

            return users;
        }

        @Override
        public int getMyFriendsCount() {
            return FriendDataCache.getInstance().getMyFriendCounts();
        }

        @Override
        public String getUserDisplayName(String account) {
            return NimUserInfoCache.getInstance().getUserDisplayName(account);
        }
    };

    //  通知文案
    private MessageNotifierCustomization messageNotifierCustomization = new MessageNotifierCustomization() {
        @Override
        public String makeNotifyContent(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }

        @Override
        public String makeTicker(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }
    };

//    解决64k
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
