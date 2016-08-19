package com.xiaoshangxing.Network.Bean;

/**
 * Created by FengChaoQun
 * on 2016/8/15
 */
public class Publish {

    /**
     * userId : 1
     * text :
     * images :
     * location :
     * clientTime :
     * category :
     * sight :
     */

    private int userId;
    private String text;
    private String images;
    private String location;
    private String clientTime;
    private String category;
    private String sight;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getClientTime() {
        return clientTime;
    }

    public void setClientTime(String clientTime) {
        this.clientTime = clientTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSight() {
        return sight;
    }

    public void setSight(String sight) {
        this.sight = sight;
    }
}
