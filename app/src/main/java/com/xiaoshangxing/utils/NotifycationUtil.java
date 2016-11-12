package com.xiaoshangxing.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.xiaoshangxing.MainActivity;
import com.xiaoshangxing.R;

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

    public static final int NOTIFY_XIAOSHANG = 1;//校上通知
    public static final int NOTIFY_LOVE = 2;//留心通知
    public static final int NOTIFY_FRIEND = 3;//成为好友通知
    public static final int NOTIFY_WO = 4;//[我]新消息通知

    public static NotificationManager mNotificationManager = (NotificationManager)
            XSXApplication.getInstance().getSystemService(NOTIFICATION_SERVICE);

    public static NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(XSXApplication.getInstance());

    public static void show() {
        mBuilder.setContentTitle("测试标题")
                .setContentText("测试内容")
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
        mBuilder.setContentTitle("校上通知")
                .setContentText("测试内容")
                .setContentIntent(pendingIntent)
//				.setNumber(number)//显示数量
                .setTicker("校上通知")//通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_HIGH)//设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                .setSmallIcon(R.mipmap.cirecleimage_default);
        mNotificationManager.notify(NOTIFY_XIAOSHANG, mBuilder.build());
    }

    public static void showLoveNotifycation() {
        Intent intent = new Intent(XSXApplication.getInstance(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(IntentStatic.TYPE, NOTIFY_LOVE);
        PendingIntent pendingIntent = PendingIntent.getActivity(XSXApplication.getInstance(), NOTIFY_LOVE, intent, 0);
        mBuilder.setContentTitle("留心通知")
                .setContentText("测试内容")
                .setContentIntent(pendingIntent)
//				.setNumber(number)//显示数量
                .setTicker("留心通知")//通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_HIGH)//设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                .setSmallIcon(R.mipmap.cirecleimage_default);
        mNotificationManager.notify(NOTIFY_LOVE, mBuilder.build());
    }

    public static void showFriendNotifycation() {
        Intent intent = new Intent(XSXApplication.getInstance(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(IntentStatic.TYPE, NOTIFY_FRIEND);
        PendingIntent pendingIntent = PendingIntent.getActivity(XSXApplication.getInstance(), NOTIFY_FRIEND, intent, 0);
        mBuilder.setContentTitle("好友通知")
                .setContentText("测试内容")
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
        mBuilder.setContentTitle("校友圈通知")
                .setContentText("测试内容")
                .setContentIntent(pendingIntent)
//				.setNumber(number)//显示数量
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


}
