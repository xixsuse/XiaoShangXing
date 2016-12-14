package com.xiaoshangxing.data.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FengChaoQun
 * on 2016/10/27
 * 聊天背景
 */

public class BackGround extends RealmObject {
    public static final String DEFAULT = "0";
    @PrimaryKey
    private String id;
    private String selfId;
    private String objectId;
    private String imgae;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelfId() {
        return selfId;
    }

    public void setSelfId(String selfId) {
        this.selfId = selfId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getImgae() {
        return imgae;
    }

    public void setImgae(String imgae) {
        this.imgae = imgae;
    }

    @Override
    public String toString() {
        return "BackGround{" +
                "id='" + id + '\'' +
                ", selfId='" + selfId + '\'' +
                ", objectId='" + objectId + '\'' +
                ", imgae='" + imgae + '\'' +
                '}';
    }
}
