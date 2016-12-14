package com.xiaoshangxing.data.bean;

import io.realm.RealmObject;

/**
 * Created by FengChaoQun
 * on 2016/11/21
 */

public class Schoolmate extends RealmObject {

    /**
     * id : 76
     * serverTime : 1479727068275
     * username : 诛仙
     * userImage : http://114.55.96.241:8080/images/a3e47e212bff496880ee55b3b2028b5b.jpg
     * signature : one world
     */

    private String id;
    private long serverTime;
    private String username;
    private String userImage;
    private String signature;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
