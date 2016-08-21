package com.xiaoshangxing.xiaoshang.idlesale.SelectImageFromSd.ImageSelectActivity;

import com.xiaoshangxing.input_activity.album.ImageBucket;
import com.xiaoshangxing.utils.IBasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quchwe on 2016/8/12 0012.
 */

public interface ImageDirectoryContract {

    interface Presenter extends IBasePresenter{
       void setImageBucket();

    }

    interface View {
        void setImageDirectory(List<ImageBucket> list);
        void setPresenter(Presenter presenter);
        void toAllPhotoActivity(ArrayList<String> paths);
    }

}
