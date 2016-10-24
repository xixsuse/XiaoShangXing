package com.xiaoshangxing.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FengChaoQun
 * on 2016/10/9
 * 校历数据
 */

public class CalendarData extends RealmObject {

    /**
     * momentId : 263
     * userId : 29
     * category : 4
     * text : 18896724777(管理员):校历发布测试
     * serverTime : 1475945915007
     * createTime : 1475823579000
     * day : 15
     * year : 2016
     * month : 9
     */
    @PrimaryKey
    private int momentId;
    private int userId;
    private int category;
    private String text;
    private long serverTime;
    private long createTime;
    private String day;
    private String year;
    private String month;

    public int getMomentId() {
        return momentId;
    }

    public void setMomentId(int momentId) {
        this.momentId = momentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
