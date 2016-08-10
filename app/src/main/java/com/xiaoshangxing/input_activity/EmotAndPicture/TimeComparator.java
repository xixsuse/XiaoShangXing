package com.xiaoshangxing.input_activity.EmotAndPicture;

/**
 * Created by FengChaoQun
 * on 2016/7/27
 */
import com.xiaoshangxing.SelectPerson.SortModel;

import java.io.File;
import java.util.Comparator;

/**
 *
 * @author xiaanming
 *
 */
public class TimeComparator implements Comparator<File> {

    public int compare(File o1, File o2) {
        if (o1.lastModified()>o2.lastModified()){
            return -1;
        }else {
            return 1;
        }
    }
}
