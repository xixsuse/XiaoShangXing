package com.xiaoshangxing.yujian.chatInfo.chooseNewGroupMaster;

/**
 * Created by FengChaoQun
 * on 2016/7/27
 */
import com.xiaoshangxing.SelectPerson.SortModel;

import java.util.Comparator;

/**
 *
 * @author xiaanming
 *
 */
public class NewMasterPinyinComparator implements Comparator<SortBean> {

    public int compare(SortBean o1, SortBean o2) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
        if (o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}
