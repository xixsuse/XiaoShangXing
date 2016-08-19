package com.xiaoshangxing.yujian.xiaoyou;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by 15828 on 2016/8/15.
 */
public class XiaoyouBean {
    private Bitmap bitmap;
    private String name1;
    private String name2;
    public List<XiaoyouBean> datas;

    public XiaoyouBean(String name1, String name2, Bitmap bitmap) {
        this.name1 = name1;
        this.name2 = name2;
        this.bitmap = bitmap;
    }

    public XiaoyouBean(String name) {
        this.name1 = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }


}
