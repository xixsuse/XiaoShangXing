package com.xiaoshangxing.yujian.Schoolfellow.ItemBean;

/**
 * Created by FengChaoQun
 * on 2016/10/22
 */

public class CollegeItem extends BaseItemBean {
    private String collegeName;
    private String signature;

    public CollegeItem(String id, String collegeName, String signature) {
        super(id, null, BaseItemBean.COLLEGE);
        this.collegeName = collegeName;
        this.signature = signature;
    }

    @Override
    public String getImage() {
        return null;
    }

    @Override
    public String getShowName() {
        return collegeName;
    }

    @Override
    public String getExText() {
        return signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CollegeItem that = (CollegeItem) o;

        return collegeName.equals(that.collegeName);

    }

    @Override
    public int hashCode() {
        return collegeName.hashCode();
    }
}
