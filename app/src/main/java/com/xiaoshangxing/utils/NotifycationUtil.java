package com.xiaoshangxing.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.xiaoshangxing.MainActivity;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.PushMsg;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by FengChaoQun
 * on 2016/9/30
 * 自定义通知统一管理
 * 校上通知
 * 留心通知
 * 成为好友通知
 * [我]新消息通知
 */

public class NotifycationUtil {

    /**
     * 通知栏显示的类别
     */
    public static final int NOTIFY_XIAOSHANG = 1;//校上通知
    public static final int NOTIFY_STARTED = 2;//留心通知
    public static final int NOTIFY_FRIEND = 3;//成为好友通知
    public static final int NOTIFY_WO = 4;//[我]新消息通知

    /**
     * 通知信息的类别
     * NT=NOTICE_TYPE
     */
    public static final String NT_APP_NOTICE = "100";                   //APP通知
    public static final String NT_APP_UPDATE = "101";                   //APP更新

    public static final String NT_SCHOOLMATE_NOTICE = "200";            //校友圈公告
    public static final String NT_SCHOOLMATE_CHANGE = "201";            //校友圈动态变更
    public static final String NT_SCHOOLMATE_NOTICE_YOU = "202";        //校友圈提醒谁看

    public static final String NT_SCHOOL_NOTICE = "300";                //校上公告
    public static final String NT_SCHOOL_CALENDAAR = "301";             //校历资讯通知
    public static final String NT_SCHOOL_REWARD = "302";                //校内悬赏通知
    public static final String NT_SCHOOL_HELP = "303";                  //校友互帮通知
    public static final String NT_SCHOOL_PLAN = "304";                  //计划发起通知
    public static final String NT_SCHOOL_SALE = "305";                  //闲置出售通知
    public static final String NT_SCHOOL_NOTICE_YOU = "306";            //校上提醒谁看

    public static final String NT_IM_NOTICE = "400";                    //遇见公告
    public static final String NT_IM_STARED = "401";                    //被留心
    public static final String NT_IM_LOVED = "402";                     //被暗恋

    /**
     * 动态的操作类型
     **/
    public static final String OPERATION_COMMENT = "1";                  //评论
    public static final String OPERATION_PRAISE = "2";                   //赞
    public static final String OPERATION_TRANSMIT = "3";                 //转发


    private static Realm realm = Realm.getDefaultInstance();
    private static List<OnNotifyChange> onNotifyChanges = new ArrayList<>();

    public static NotificationManager mNotificationManager = (NotificationManager)
            XSXApplication.getInstance().getSystemService(NOTIFICATION_SERVICE);

    public static NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(XSXApplication.getInstance());

    public static void show(String s) {
        mBuilder.setContentTitle("测试标题")
                .setContentText(s)
//                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
//				.setNumber(number)//显示数量
                .setTicker("测试通知来啦")//通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
//				.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.mipmap.cirecleimage_default);
        mNotificationManager.notify(1, mBuilder.build());
    }

    public static void showXiaoshangNotifycation() {
        Intent intent = new Intent(XSXApplication.getInstance(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(IntentStatic.TYPE, NOTIFY_XIAOSHANG);
        PendingIntent pendingIntent = PendingIntent.getActivity(XSXApplication.getInstance(), NOTIFY_XIAOSHANG, intent, 0);

        RealmResults<PushMsg> realmResults = realm.where(PushMsg.class).not().equalTo("isRead", "1")
                .beginGroup()
                .equalTo(NS.PUSH_TYPE, NT_SCHOOL_HELP)
                .or().equalTo(NS.PUSH_TYPE, NT_SCHOOL_REWARD)
                .or().equalTo(NS.PUSH_TYPE, NT_SCHOOL_PLAN)
                .or().equalTo(NS.PUSH_TYPE, NT_SCHOOL_SALE)
                .or().equalTo(NS.PUSH_TYPE, NT_SCHOOL_NOTICE_YOU)
                .endGroup()
                .findAllSorted(NS.PUSH_TIME, Sort.DESCENDING);
        if (realmResults.isEmpty()) {
            return;
        }
        PushMsg pushMsg = realmResults.first();

        int number = realmResults.size();
        String showText = "";
        if (pushMsg.getPushType().equals(NT_SCHOOL_NOTICE_YOU)) {
            showText = pushMsg.getUserName() + "提到了你";
        } else {
            switch (pushMsg.getOperationType()) {
                case OPERATION_COMMENT:
                    showText = pushMsg.getUserName() + ":" + pushMsg.getText();
                    break;
                case OPERATION_PRAISE:
                    showText = pushMsg.getUserName() + "赞了你";
                    break;
                case OPERATION_TRANSMIT:
                    showText = pushMsg.getUserName() + "转发了你的" + pushMsg.getCategoryName();
                    break;
            }
        }

        mBuilder.setContentTitle("校上通知")
                .setContentText(showText)
                .setContentIntent(pendingIntent)
                .setNumber(number)//显示数量
                .setTicker("校上通知")//通知首次出现在通知栏，带上升动画效果的
                .setWhen(pushMsg.getPushTime())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_HIGH)//设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                .setSmallIcon(R.mipmap.cirecleimage_default);
        mNotificationManager.notify(NOTIFY_XIAOSHANG, mBuilder.build());
    }

    public static void showStaredNotifycation() {
        Intent intent = new Intent(XSXApplication.getInstance(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(IntentStatic.TYPE, NOTIFY_STARTED);
        PendingIntent pendingIntent = PendingIntent.getActivity(XSXApplication.getInstance(), NOTIFY_STARTED, intent, 0);

        RealmResults<PushMsg> pushMsgs = realm.where(PushMsg.class)
                .equalTo(NS.PUSH_TYPE, NT_IM_STARED)
                .findAllSorted(NS.PUSH_TIME, Sort.DESCENDING);

        if (pushMsgs.isEmpty()) {
            return;
        }
        int number = pushMsgs.size();
        PushMsg pushMsg = pushMsgs.first();
        String showText = pushMsg.getUserName() + "留心了你";

        mBuilder.setContentTitle("留心通知")
                .setContentText(showText)
                .setContentIntent(pendingIntent)
                .setNumber(number)//显示数量
                .setTicker("留心通知")//通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_HIGH)//设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                .setSmallIcon(R.mipmap.cirecleimage_default);
        mNotificationManager.notify(NOTIFY_STARTED, mBuilder.build());
    }

    public static void showFriendNotifycation(String userName) {
        Intent intent = new Intent(XSXApplication.getInstance(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(IntentStatic.TYPE, NOTIFY_FRIEND);
        PendingIntent pendingIntent = PendingIntent.getActivity(XSXApplication.getInstance(), NOTIFY_FRIEND, intent, 0);
        mBuilder.setContentTitle("好友通知")
                .setContentText("你和" + userName + "成为了好友")
                .setContentIntent(pendingIntent)
//				.setNumber(number)//显示数量
                .setTicker("好友通知")//通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_HIGH)//设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                .setSmallIcon(R.mipmap.cirecleimage_default);
        mNotificationManager.notify(NOTIFY_FRIEND, mBuilder.build());
    }

    public static void showWoNotifycation() {
        Intent intent = new Intent(XSXApplication.getInstance(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(IntentStatic.TYPE, NOTIFY_WO);
        PendingIntent pendingIntent = PendingIntent.getActivity(XSXApplication.getInstance(), NOTIFY_WO, intent, 0);

        RealmResults<PushMsg> realmResults = realm.where(PushMsg.class).not().equalTo("isRead", "1")
                .beginGroup()
                .equalTo(NS.PUSH_TYPE, NT_SCHOOLMATE_CHANGE)
                .or().equalTo(NS.PUSH_TYPE, NT_SCHOOLMATE_NOTICE_YOU)
                .endGroup()
                .findAllSorted(NS.PUSH_TIME, Sort.DESCENDING);
        if (realmResults.isEmpty()) {
            Log.d("pushMsgs", "null");
            return;
        }
        PushMsg pushMsg = realmResults.first();

        int number = realmResults.size();
        String showText = "";
        if (pushMsg.getPushType().equals(NT_SCHOOLMATE_NOTICE_YOU)) {
            showText = pushMsg.getUserName() + "提到了你";
        } else {
            switch (pushMsg.getOperationType()) {
                case OPERATION_COMMENT:
                    showText = pushMsg.getUserName() + ":" + pushMsg.getText();
                    break;
                case OPERATION_PRAISE:
                    showText = pushMsg.getUserName() + "赞了你";
                    break;
            }
        }

        mBuilder.setContentTitle("校友圈通知")
                .setContentText(showText)
                .setContentIntent(pendingIntent)
                .setNumber(number)//显示数量
                .setTicker("校友圈通知")//通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_HIGH)//设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                .setSmallIcon(R.mipmap.cirecleimage_default);
        mNotificationManager.notify(NOTIFY_WO, mBuilder.build());
    }

    /**
     * 清除当前创建的通知栏
     */
    public static void clearNotify(int notifyId) {
        mNotificationManager.cancel(notifyId);
    }

    /**
     * 清除所有通知栏
     */
    public static void clearAllNotify() {
        mNotificationManager.cancelAll();
    }

    public static void parsePushMsg(final String msg) {
        Gson gson = new Gson();
        PushMsg pushMsg = gson.fromJson(msg, PushMsg.class);
        if (pushMsg == null) {
            Log.d("pushMsg", "null");
            return;
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createObjectFromJson(PushMsg.class, msg).setIsRead("0");
            }
        });

        for (OnNotifyChange i : onNotifyChanges) {
            i.onChange(pushMsg);
        }

        switch (pushMsg.getPushType()) {
            case "201":
            case "202":
                showWoNotifycation();
                break;
            case "301":
            case "302":
            case "303":
            case "304":
            case "305":
            case "306":
                showXiaoshangNotifycation();
                break;
            case "401":
                showStaredNotifycation();
                break;

        }
    }

    public static void registerObserver(OnNotifyChange onNotifyChange) {
        if (onNotifyChange != null) {
            onNotifyChanges.add(onNotifyChange);
        }
    }

    public static void unRegisterObserver(OnNotifyChange onNotifyChange) {
        if (onNotifyChange != null) {
            onNotifyChanges.remove(onNotifyChange);
        }
    }

    public interface OnNotifyChange {
        void onChange(PushMsg pushMsg);
    }

}
