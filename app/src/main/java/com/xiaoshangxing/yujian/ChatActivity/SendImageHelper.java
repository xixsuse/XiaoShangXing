package com.xiaoshangxing.yujian.ChatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xiaoshangxing.yujian.IM.kit.file.AttachmentStore;
import com.xiaoshangxing.yujian.IM.kit.file.FileUtil;
import com.xiaoshangxing.yujian.IM.kit.storage.StorageType;
import com.xiaoshangxing.yujian.IM.kit.storage.StorageUtil;
import com.xiaoshangxing.yujian.IM.kit.string.MD5;
import com.xiaoshangxing.yujian.IM.media.ImageUtil;

import java.io.File;
import java.util.List;

public class SendImageHelper {
    public interface Callback {
        void sendImage(File file, boolean isOrig);
    }

    public static void sendImageAfterSelfImagePicker(Context context, boolean isOrig, List<String> data, final Callback callback) {

        if(data == null) {
            Toast.makeText(context, "图片为空", Toast.LENGTH_LONG).show();
            return;
        }
        for (int i=0;i<data.size();i++){
            new SendImageTask(context, isOrig, data.get(i), new Callback() {

                @Override
                public void sendImage(File file, boolean isOrig) {
                    if (callback != null) {
                        callback.sendImage(file, isOrig);
                    }
                }
            }).execute();
        }
    }

    public static File getLittleImage(String photoPath, final Context context) {
        File imageFile = new File(photoPath);
        String mimeType = FileUtil.getExtensionName(photoPath);
        imageFile = ImageUtil.getScaledImageFileWithMD5(imageFile, mimeType);
        if (imageFile == null) {
            new Handler(context.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "图片有误", Toast.LENGTH_LONG).show();
                }
            });
            return null;
        } else {
            ImageUtil.makeThumbnail(context, imageFile);
        }

        Log.d("yasuo ok",""+imageFile.getAbsolutePath());

        return imageFile;
    }

    // 从相册选择图片进行发送(Added by NYB)
    public static class SendImageTask extends AsyncTask<Void, Void, File> {

        private Context context;
        private boolean isOrig;
        private Callback callback;
        private String path;

        public SendImageTask(Context context, boolean isOrig, String path,
                             Callback callback) {
            this.context = context;
            this.isOrig = isOrig;
            this.callback = callback;
            this.path=path;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected File doInBackground(Void... params) {
            String photoPath = path;
            if (TextUtils.isEmpty(photoPath))
                return null;

            if (isOrig) {
                // 把原图按md5存放
                String origMD5 = MD5.getStreamMD5(photoPath);
                String extension = FileUtil.getExtensionName(photoPath);
                String origMD5Path = StorageUtil.getWritePath(origMD5 + "."
                        + extension, StorageType.TYPE_IMAGE);
                AttachmentStore.copy(photoPath, origMD5Path);
                // 生成缩略图
                File imageFile = new File(origMD5Path);
                ImageUtil.makeThumbnail(context, imageFile);

                return new File(origMD5Path);
            } else {
                File imageFile = new File(photoPath);
                String mimeType = FileUtil.getExtensionName(photoPath);
                imageFile = ImageUtil.getScaledImageFileWithMD5(imageFile, mimeType);
                if (imageFile == null) {
                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "图片有误", Toast.LENGTH_LONG).show();
                        }
                    });
                    return null;
                } else {
                    ImageUtil.makeThumbnail(context, imageFile);
                }

                return imageFile;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            super.onPostExecute(result);

            if (result != null) {
                if (callback != null) {
                    String imageFilepath = result.getAbsolutePath();
                    String md5 = FileUtil.getFileNameNoEx(FileUtil.getFileNameFromPath(imageFilepath));

                    if (callback != null) {
                        callback.sendImage(result, isOrig);
                    }
                }
            }
        }
    }
}
