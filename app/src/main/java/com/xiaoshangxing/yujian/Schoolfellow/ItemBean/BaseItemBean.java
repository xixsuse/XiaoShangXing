package com.xiaoshangxing.yujian.Schoolfellow.ItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/10/22
 * 校友列表基础bean
 */

public abstract class BaseItemBean {

    public static final int COLLEGE = 0;
    public static final int PROFESSION = 1;
    public static final int GRADE = 2;
    public static final int PERSON = 3;

    private int type;

    private int id;

    /**
     * 当前的级别
     */
    private int level;

    /**
     * 是否展开
     */
    private boolean isExpand = false;

    /**
     * 下一级的子Node
     */
    private List<BaseItemBean> children = new ArrayList<BaseItemBean>();

    /**
     * 父Node
     */
    private BaseItemBean parent;

    public BaseItemBean(int id, BaseItemBean parent, int type) {
        this.id = id;
        this.parent = parent;
        this.type = type;
    }

    /**
     * 是否为跟节点
     *
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * 判断父节点是否展开
     *
     * @return
     */
    public boolean isParentExpand() {
        if (parent == null)
            return false;
        return parent.isExpand();
    }

    /**
     * 是否是叶子界点
     *
     * @return
     */
    public boolean isLeaf() {
        return children.size() == 0;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取level
     */
    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public List<BaseItemBean> getChildren() {
        return children;
    }

    public void setChildren(List<BaseItemBean> children) {
        this.children = children;
    }

    public BaseItemBean getParent() {
        return parent;
    }

    public void setParent(BaseItemBean parent) {
        this.parent = parent;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /*
    **describe:子类需要实现的方法
    */

    /**
     * description:获取图片
     *
     * @return 图片地址
     */

    public abstract String getImage();

    /**
     * description:获取显示名称
     *
     * @return 显示的名称
     */

    public abstract String getShowName();

    /**
     * description:获取需要额外显示的信息
     *
     * @return 额外信息
     */

    public abstract String getExText();

}
