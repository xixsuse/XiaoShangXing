package com.xiaoshangxing.input_activity.album;

import java.util.Arrays;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/8/10
 */
public class SortImage {
    public static void  SortImages(ImageBucket imageBucket){
        List<ImageItem> imageItems=imageBucket.imageList;
        ImageItem [] lists=new ImageItem[imageItems.size()];
        imageItems.toArray(lists);
        Arrays.sort(imageItems.toArray(lists),new ImageFileSortComparator());
        imageItems.clear();
        for (int i=0;i<lists.length;i++){
            imageItems.add(i,lists[i]);
        }
        imageBucket.imageList=imageItems;
    }

}
