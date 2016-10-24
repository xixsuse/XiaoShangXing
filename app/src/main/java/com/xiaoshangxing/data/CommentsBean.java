package com.xiaoshangxing.data;

import android.text.TextUtils;

import com.xiaoshangxing.Network.netUtil.NS;

import io.realm.RealmObject;

/**
 * Created by FengChaoQun
 * on 2016/8/24
 * 评论数据
 */
public class CommentsBean extends RealmObject {

    /**
     * commentId : 89
     * serverTime : 1475487664338
     * userInfo : 66$17768345313
     * createTime : 1475482289000
     * text : 哦啦啦啦
     * lastUserInfo : 8888
     * momentId : 225
     */

    private int commentId;
    private long serverTime;
    private String userInfo;
    private long createTime;
    private String text;
    private String lastUserInfo;
    private int momentId;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLastUserInfo() {
        return lastUserInfo;
    }

    public void setLastUserInfo(String lastUserInfo) {
        this.lastUserInfo = lastUserInfo;
    }

    public int getMomentId() {
        return momentId;
    }

    public void setMomentId(int momentId) {
        this.momentId = momentId;
    }

    //获取评论人id
    public int getUserId() {
        if (TextUtils.isEmpty(getUserInfo())) {
            return -1;
        }
        String[] id_name = getUserInfo().split(NS.SPLIT2);
        return Integer.valueOf(id_name[0]);
    }

    //    获取评论人名字
    public String getUserName() {
        if (TextUtils.isEmpty(getUserInfo())) {
            return "未知";
        }
        String[] id_name = getUserInfo().split(NS.SPLIT2);
        return id_name[1];
    }

    //    获取对象id
    public int getObejectId() {

        if (TextUtils.isEmpty(getLastUserInfo())) {
            return -1;
        }

        String[] id_name = getLastUserInfo().split(NS.SPLIT2);
        return Integer.valueOf(id_name[0]);
    }

    //    获取对象名字
    public String getObjectName() {
        if (TextUtils.isEmpty(getLastUserInfo())) {
            return "未知";
        }
        String[] id_name = getLastUserInfo().split(NS.SPLIT2);
        return id_name[1];
    }

    //    是否是回复某个人
    public boolean isReply() {
        return !TextUtils.isEmpty(getLastUserInfo());
    }

    public int getId() {
        return commentId;
    }

}
