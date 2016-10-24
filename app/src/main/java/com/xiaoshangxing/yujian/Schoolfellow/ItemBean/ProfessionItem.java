package com.xiaoshangxing.yujian.Schoolfellow.ItemBean;

/**
 * Created by FengChaoQun
 * on 2016/10/22
 */

public class ProfessionItem extends BaseItemBean {

    public ProfessionItem(int id, BaseItemBean parent) {
        super(id, parent, BaseItemBean.PROFESSION);
    }

    @Override
    public String getImage() {
        return null;
    }

    @Override
    public String getShowName() {
        return "计算机科学与技术";
    }

    @Override
    public String getExText() {
        return null;
    }
}
