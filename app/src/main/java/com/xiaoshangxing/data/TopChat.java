package com.xiaoshangxing.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FengChaoQun
 * on 2016/9/6
 * 指定消息数据
 */
public class TopChat extends RealmObject {
    @PrimaryKey
    public String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
