package com.xiaoshangxing.data;

import android.accounts.Account;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FengChaoQun
 * on 2016/9/6
 */
public class TopChat extends RealmObject {
    @PrimaryKey
    public String account;
//    public RealmList<Acount> accounts;
//
//    public RealmList<Acount> getAccounts() {
//        return accounts;
//    }
//
//    public void setAccounts(RealmList<Acount> accounts) {
//        this.accounts = accounts;
//    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
