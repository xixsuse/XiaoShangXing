package com.xiaoshangxing.data;

import android.text.TextUtils;

import com.xiaoshangxing.Network.netUtil.NS;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FengChaoQun
 * on 2016/11/21
 * 推送信息
 */

public class PushMsg extends RealmObject {

    @PrimaryKey
    private String id;              //推送id
    private String pushType;        //推送类型
    private String userInfo;        //操作者id(触发推送的人的id)+name 公告之类的由系统发出
    private String text;            //推送的文字信息  例如：公告内容 评论和转发时说的文字信息等
    private String operationType;   //操作id  在与动态有关的推送时需要指明操作id 例如赞 评论等
    private String commentId;       //评论信息的id
    private String transmitId;      //转发信息的id
    private long pushTime;          //触发这条推送的时间 例如 点赞的时间

    /*动态有关信息
    * 考虑到数据流量问题  目前只推送相关动态的个别字段信息
    * 后续会根据需求改动
    * */
    private String momentId;        //动态id
    private String category;        //动态类型
    private String momentText;      //动态的文字内容
    private String images;          //动态的图片内容

    private String isRead = "0";    //标记是否已读


    public String getUserId() {
        if (!TextUtils.isEmpty(userInfo)) {
            String string[] = userInfo.split(NS.SPLIT);
            if (string.length == 2) {
                return string[0];
            }
        }
        return "-1";
    }

    public String getUserName() {
        if (!TextUtils.isEmpty(userInfo)) {
            String string[] = userInfo.split(NS.SPLIT);
            if (string.length == 2) {
                return string[1];
            }
        }
        return null;
    }

    public String getCategoryName() {
        if (TextUtils.isEmpty(category)) {
            return "no category";
        } else {
            switch (category) {
                case NS.CATEGORY_CALENDAR:
                    return "校历资讯";
                case NS.CATEGORY_HELP:
                    return "校友互帮";
                case NS.CATEGORY_PLAN:
                    return "计划发起";
                case NS.CATEGORY_REWARD:
                    return "校内悬赏";
                case NS.CATEGORY_SALE:
                    return "闲置出售";
                case NS.CATEGORY_STATE:
                    return "校友圈";
                default:
                    return category;
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getTransmitId() {
        return transmitId;
    }

    public void setTransmitId(String transmitId) {
        this.transmitId = transmitId;
    }

    public long getPushTime() {
        return pushTime;
    }

    public void setPushTime(long pushTime) {
        this.pushTime = pushTime;
    }

    public String getMomentId() {
        return momentId;
    }

    public void setMomentId(String momentId) {
        this.momentId = momentId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMomentText() {
        return momentText;
    }

    public void setMomentText(String momentText) {
        this.momentText = momentText;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}
