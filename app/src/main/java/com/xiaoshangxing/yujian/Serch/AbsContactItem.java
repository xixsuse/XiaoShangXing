package com.xiaoshangxing.yujian.Serch;

/**
 * 通讯录数据项抽象类
 * Created by huangjun on 2015/2/10.
 */
public abstract class AbsContactItem {
    public static int compareType(int lhs, int rhs) {
        return lhs - rhs;
    }

    /**
     * 所属的类型
     */
    public abstract int getItemType();

    /**
     * 所属的分组
     */
    public abstract String belongsGroup();

    protected final int compareType(AbsContactItem item) {
        return compareType(getItemType(), item.getItemType());
    }
}
