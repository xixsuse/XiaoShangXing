package com.xiaoshangxing.utils.image;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.utils.FileUtils;

import java.io.File;

/**
 * Created by FengChaoQun
 * on 2016/7/11
 */
public class SaveImageTask extends AsyncTask<String, Void, File> {
    private final Context context;
    private String savePath;
    private String successToast;
    private String FailToast;
    private SimpleCallBack simpleCallBack;

    public SaveImageTask(Context context, String savePath, String successToast, String failToast) {
        this.context = context;
        this.savePath = savePath;
        this.successToast = successToast;
        FailToast = failToast;
    }

    public void setSimpleCallBack(SimpleCallBack simpleCallBack) {
        this.simpleCallBack = simpleCallBack;
    }

    @Override
    protected File doInBackground(String... params) {
        String url = params[0]; // should be easy to extend to share multiple images at once
        try {
            return Glide
                    .with(context)
                    .load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get() // needs to be called on background thread
                    ;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(File result) {
        if (result == null) {
            if (FailToast == null) {
                return;
            }
            Toast.makeText(context, FailToast, Toast.LENGTH_SHORT).show();
            return;
        }

//        File file = new File(FileUtils.getXsxSaveIamge(), UUID.randomUUID().toString() + ".jpg");
        File file = new File(savePath);
        try {
            if (FileUtils.copyFileTo(result, file)) {
                if (successToast != null) {
                    Toast.makeText(context, successToast /*"保存成功：" + file.getPath()*/, Toast.LENGTH_SHORT).show();
                }
                if (simpleCallBack != null) {
                    simpleCallBack.onSuccess();
                }
            } else {
                if (FailToast != null) {
                    Toast.makeText(context, FailToast, Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}