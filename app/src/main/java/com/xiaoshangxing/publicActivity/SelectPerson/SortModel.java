package com.xiaoshangxing.publicActivity.SelectPerson;

/**
 * Created by FengChaoQun
 * on 2016/7/27
 */
public class SortModel {
    private String account;
    private String name;   //显示的数据
    private String sortLetters;  //显示数据拼音的首字母
    private boolean isSpecial;

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

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "SortModel{" +
                "account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", sortLetters='" + sortLetters + '\'' +
                ", isSpecial=" + isSpecial +
                '}';
    }
}
