package com.xiaoshangxing.Network.Bean;

import java.io.File;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/8/15
 */
public class Publish {
    private Integer userId;
    private String text;
//    private List<File> images=new ArrayList<>();
    private String location;
    private Integer personLimit;
    private long clientTime;
    private Integer category;
    private Integer sight;
    private String price;
    private String dorm;
    private String sightUserids;
    private long timeStamp;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

//    public List<File> getImages() {
//        return images;
//    }
//
//    public void setImages(List<File> images) {
//        this.images = images;
//    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getPersonLimit() {
        return personLimit;
    }

    public void setPersonLimit(Integer personLimit) {
        this.personLimit = personLimit;
    }

    public long getClientTime() {
        return clientTime;
    }

    public void setClientTime(long clientTime) {
        this.clientTime = clientTime;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getSight() {
        return sight;
    }

    public void setSight(Integer sight) {
        this.sight = sight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDorm() {
        return dorm;
    }

    public void setDorm(String dorm) {
        this.dorm = dorm;
    }

    public String getSightUserids() {
        return sightUserids;
    }

    public void setSightUserids(String sightUserids) {
        this.sightUserids = sightUserids;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
