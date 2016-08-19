package com.xiaoshangxing.yujian.chatInfo.deleteMember;

import android.graphics.Bitmap;

/**
 * Created by 15828 on 2016/8/13.
 */
public class DeleteMember {
    private boolean check = false;
    private Bitmap bitmap;
    private String name;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

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
}
