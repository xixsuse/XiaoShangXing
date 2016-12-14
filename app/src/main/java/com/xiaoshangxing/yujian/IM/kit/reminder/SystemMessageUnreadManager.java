package com.xiaoshangxing.yujian.IM.kit.reminder;

/**
 * Created by huangjun on 2015/8/20.
 */
public class SystemMessageUnreadManager {

    private static SystemMessageUnreadManager instance = new SystemMessageUnreadManager();
    private int sysMsgUnreadCount = 0;

    public static SystemMessageUnreadManager getInstance() {
        return instance;
    }

    public int getSysMsgUnreadCount() {
        return sysMsgUnreadCount;
    }

    public synchronized void setSysMsgUnreadCount(int unreadCount) {
        this.sysMsgUnreadCount = unreadCount;
    }
}
