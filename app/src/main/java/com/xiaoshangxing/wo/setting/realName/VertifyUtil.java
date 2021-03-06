package com.xiaoshangxing.wo.setting.realName;

import android.graphics.Bitmap;
import android.os.Environment;

import com.xiaoshangxing.utils.normalUtils.FileUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *modified by FengChaoQun on 2016/12/24 16:33
 * description:优化代码
 */
public class VertifyUtil {
    public static String imgLeftName = "imgLeftName.jpeg";
    public static String imgRightName = "imgRightName.jpeg";

    public static void saveFile(Bitmap bitmap, String fileName) {
        String path = FileUtils.getXSX_CameraPhotoPath();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }


    public static String getLeftImgPath() {
//        return getSDPath() + "/VertifyJieTu/" + imgLeftName;
        return FileUtils.getXSX_CameraPhotoPath() + imgLeftName;
    }

    public static String getRightImgPath() {
//        return getSDPath() + "/VertifyJieTu/" + imgRightName;
        return FileUtils.getXSX_CameraPhotoPath() + imgRightName;
    }
}
