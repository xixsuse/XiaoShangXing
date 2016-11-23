package com.xiaoshangxing.yujian.Schoolfellow.ItemBean;

/**
 * Created by FengChaoQun
 * on 2016/10/22
 */

public class ProfessionItem extends BaseItemBean {
    private String professionName;

    public ProfessionItem(String id, String professionName, BaseItemBean parent) {
        super(id, parent, BaseItemBean.PROFESSION);
        this.professionName = professionName;
    }

    @Override
    public String getImage() {
        return null;
    }

    @Override
    public String getShowName() {
        return professionName;
    }

    @Override
    public String getExText() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfessionItem that = (ProfessionItem) o;

        return professionName != null ? professionName.equals(that.professionName) : that.professionName == null;

    }

    @Override
    public int hashCode() {
        return professionName != null ? professionName.hashCode() : 0;
    }
}
