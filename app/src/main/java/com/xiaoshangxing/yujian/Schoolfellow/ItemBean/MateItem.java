package com.xiaoshangxing.yujian.Schoolfellow.ItemBean;

/**
 * Created by FengChaoQun
 * on 2016/10/22
 */

public class MateItem extends BaseItemBean {

    public MateItem(int id, BaseItemBean parent) {
        super(id, parent, BaseItemBean.PERSON);
    }

    @Override
    public String getImage() {
        return null;
    }

    @Override
    public String getShowName() {
        return "冯超群";
    }

    @Override
    public String getExText() {
        return "天气真好";
    }
}
