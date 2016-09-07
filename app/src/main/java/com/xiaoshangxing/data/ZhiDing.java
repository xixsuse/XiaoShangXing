package com.xiaoshangxing.data;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FengChaoQun
 * on 2016/9/6
 */
public class ZhiDing extends RealmObject {
    @PrimaryKey
    private int id=1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
