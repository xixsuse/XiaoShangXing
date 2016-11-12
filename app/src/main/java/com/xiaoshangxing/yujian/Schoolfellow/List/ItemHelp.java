package com.xiaoshangxing.yujian.Schoolfellow.List;

import com.xiaoshangxing.yujian.Schoolfellow.ItemBean.BaseItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/10/22
 */

public class ItemHelp {

    /**
     * description:最数据进行排序
     *
     * @param itemBeanList 数据
     * @return
     */

    public static List<BaseItemBean> sort(List<BaseItemBean> itemBeanList) {
        List<BaseItemBean> result = new ArrayList<BaseItemBean>();
        // 拿到根节点
        List<BaseItemBean> rootItem = getRootNodes(itemBeanList);
        // 排序以及设置Node间关系
        for (BaseItemBean itemBean : rootItem) {
            addItem(result, itemBean, 1);
        }
        return result;
    }


    /**
     * description:获取根item
     *
     * @param itemBeanList 所有item
     * @return 所有根item
     */

    private static List<BaseItemBean> getRootNodes(List<BaseItemBean> itemBeanList) {
        List<BaseItemBean> root = new ArrayList<BaseItemBean>();
        for (BaseItemBean itemBean : itemBeanList) {
            if (itemBean.isRoot())
                root.add(itemBean);
        }
        return root;
    }

    /**
     * 把一个节点上的所有的内容都挂上去
     */
    private static void addItem(List<BaseItemBean> itemBeanList, BaseItemBean itemBean, int currentLevel) {

        itemBeanList.add(itemBean);

        if (itemBean.isLeaf())
            return;
        for (int i = 0; i < itemBean.getChildren().size(); i++) {
            addItem(itemBeanList, itemBean.getChildren().get(i),
                    currentLevel + 1);
        }
    }

    /**
     * 过滤出所有可见的Node
     *
     * @param itemBeanList
     * @return
     */
    public static List<BaseItemBean> filterVisibleItem(List<BaseItemBean> itemBeanList) {
        List<BaseItemBean> result = new ArrayList<BaseItemBean>();

        for (BaseItemBean i : itemBeanList) {
            // 如果为跟 节点，或者上层目录为展开状态
            if (i.isRoot() || i.isParentExpand()) {
                result.add(i);
            }
        }
        return result;
    }

    public static void Collasp(BaseItemBean baseItemBean) {
        if (baseItemBean.isLeaf()) {
            return;
        } else {
            baseItemBean.setExpand(false);
            for (BaseItemBean i : baseItemBean.getChildren()) {
                Collasp(i);
            }
        }
    }
}
