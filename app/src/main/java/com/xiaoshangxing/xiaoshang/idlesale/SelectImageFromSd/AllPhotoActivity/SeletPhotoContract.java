package com.xiaoshangxing.xiaoshang.idlesale.SelectImageFromSd.AllPhotoActivity;

import com.xiaoshangxing.utils.IBasePresenter;
import com.xiaoshangxing.utils.IBaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quchwe on 2016/8/6 0006.
 */
public interface SeletPhotoContract {
    interface Presenter extends IBasePresenter {

        List<String> getAllPicturePath();
        void getAllImageItems();



    }
    interface View extends IBaseView<Presenter> {

        void setPicteureSelectCount(int count);

        void toPreview();
        void setRecylerView();
        void currentSelected(String picturePath);
        void setSlectPicturePath(ArrayList<String> paths);
    }
}
