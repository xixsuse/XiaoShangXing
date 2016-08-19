package com.xiaoshangxing.xiaoshang.idlesale;

import com.xiaoshangxing.xiaoshang.BaseList;

import java.util.ArrayList;

/**
 * Created by quchwe on 2016/7/30 0030.
 */
public class IdleBean extends BaseList {
    private ArrayList<String> photos;
    private String message;
    private boolean isComplete;
    private String department;
    private String price;

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
