package com.xiaoshangxing.wo.PersonalState.check_photo;

import android.content.Context;

import com.xiaoshangxing.utils.FileUtils;
import com.xiaoshangxing.utils.image.SaveImageTask;

import java.util.UUID;

/**
 * Created by FengChaoQun
 * on 2016/8/6
 */
public class myStateImagePresenter implements myStateImagePagerContract.Presenter {
    private myStateImagePagerContract.View mView;
    private Context context;

    public myStateImagePresenter(myStateImagePagerContract.View mView, Context context) {
        this.mView = mView;
        this.context = context;
    }

    @Override
    public void save() {

    }

    @Override
    public void sendToFriend() {

    }

    @Override
    public void saveImage(String url) {
        String savePath = FileUtils.getXsxSaveIamge() + UUID.randomUUID().toString() + ".jpg";
        SaveImageTask s = new SaveImageTask(context, savePath, "保存成功:" + savePath, "保存失败");
        s.execute(url);
    }

    @Override
    public void deleteImage() {

    }
}
