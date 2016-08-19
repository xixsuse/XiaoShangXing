package com.xiaoshangxing.yujian.chatInfo.chooseNewGroupMaster;

import android.graphics.Bitmap;

/**
 * Created by 15828 on 2016/8/15.
 */
public class SortBean {
    private Bitmap bitmap;
    private String name;
    private String sortLetters;  //显示数据拼音的首字母

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
