package com.xiaoshangxing.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FengChaoQun
 * on 2016/8/23
 */
public class User extends RealmObject {

    /**
     * id : 1
     * username : 18896724766
     * phone : 18896724766
     * area : 天津
     * hometown : 无锡
     * signature : 天天向上
     * photoCover : http://127.0.0.1:8080/xsx/resources/images/201608221313.jpg
     * isVip : 1
     */
    @PrimaryKey
    private int id;
    private String username;
    private String phone;
    private String area;
    private String hometown;
    private String signature;
    private String photoCover;
    private String isVip;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPhotoCover() {
        return photoCover;
    }

    public void setPhotoCover(String photoCover) {
        this.photoCover = photoCover;
    }

    public String getIsVip() {
        return isVip;
    }

    public void setIsVip(String isVip) {
        this.isVip = isVip;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", area='" + area + '\'' +
                ", hometown='" + hometown + '\'' +
                ", signature='" + signature + '\'' +
                ", photoCover='" + photoCover + '\'' +
                ", isVip='" + isVip + '\'' +
                '}';
    }
}
