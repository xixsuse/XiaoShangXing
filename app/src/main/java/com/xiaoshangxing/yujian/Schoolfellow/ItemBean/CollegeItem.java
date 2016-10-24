package com.xiaoshangxing.yujian.Schoolfellow.ItemBean;

/**
 * Created by FengChaoQun
 * on 2016/10/22
 */

public class CollegeItem extends BaseItemBean {

    public CollegeItem(int id) {
        super(id, null, BaseItemBean.COLLEGE);
    }

    @Override
    public String getImage() {
        return null;
    }

    @Override
    public String getShowName() {
        return "至善学院";
    }

    @Override
    public String getExText() {
        return "宁静致远，止于至善";
    }
}
