package com.xiaoshangxing.Network.Bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/8/15
 */
public class Publish {
    private long userId;
    private String text;
    private List<File> images=new ArrayList<>();
    private String location;
    private Integer personLimit;
    private long clientTIme;
    private Integer category;
    private Integer sight;
    private String price;
    private String dorm;
    private String sightUserids;
    private long timeStamp;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<File> getImages() {
        return images;
    }

    public void setImages(List<File> images) {
        this.images = images;
    }

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

    public long getClientTIme() {
        return clientTIme;
    }

    public void setClientTIme(long clientTIme) {
        this.clientTIme = clientTIme;
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
