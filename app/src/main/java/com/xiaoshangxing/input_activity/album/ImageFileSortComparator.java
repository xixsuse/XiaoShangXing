package com.xiaoshangxing.input_activity.album;

import java.io.File;
import java.util.Comparator;

/**
 * Created by FengChaoQun
 * on 2016/8/10
 */
public class ImageFileSortComparator implements Comparator<ImageItem>{
    public int compare(ImageItem o1, ImageItem o2) {
        File file1=new File(o1.imagePath);
        File file2=new File(o2.imagePath);
        if (file1.lastModified()>file2.lastModified()){
            return -1;
        } else if (file1.lastModified() == file2.lastModified()) {
            return 0;
        }else {
            return 1;
        }
    }

}
