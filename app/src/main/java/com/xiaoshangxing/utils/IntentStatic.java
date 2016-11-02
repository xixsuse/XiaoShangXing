package com.xiaoshangxing.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.xiaoshangxing.setting.utils.headimg_set.CommonUtils;

/**
 * Created by FengChaoQun
 * on 2016/8/20
 */
public class IntentStatic {
    public static final String TYPE="TYPE";
    public static final int  CODE=1000;
    public static final String DATA="DATA";
    public static final String EXTRA_ACCOUNT = "phone";
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_ANCHOR = "anchor";


    public static final int OTHERS=10002;
    public static final int MINE=10003;

    public static final int CLOSE=1000;

    public static final int REGISTER = 2000;

    public static final int PUBLISH = 3000;
    public static final int PUBLISH_SUCCESS = 3001;

    public static final String IS_ORIG = "IS_ORIG";

    public static void openCamera(Activity activity, Uri came_photo_path, int requestCode) {
        if (CommonUtils.isExistCamera(activity)) {
            if (Build.VERSION.SDK_INT >= 23) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 100);
                    Toast.makeText(activity, "你没有授权调用相机，请前往设置界面进行设置", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用android自带的照相机
            intent.putExtra(MediaStore.EXTRA_OUTPUT, came_photo_path);
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            activity.startActivityForResult(intent, requestCode);
        } else {
            Toast.makeText(activity,
                    "卧槽,没找到你手机上的相机你敢信?!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static void openCamera(Fragment fragment, Uri came_photo_path, int requestCode) {
        if (CommonUtils.isExistCamera(fragment.getContext())) {
            if (Build.VERSION.SDK_INT >= 23) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(fragment.getContext(), Manifest.permission.CAMERA);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{Manifest.permission.CAMERA}, 100);
                    Toast.makeText(fragment.getContext(), "你没有授权调用相机，请前往设置界面进行设置", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用android自带的照相机
            intent.putExtra(MediaStore.EXTRA_OUTPUT, came_photo_path);
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            fragment.startActivityForResult(intent, requestCode);
        } else {
            Toast.makeText(fragment.getContext(),
                    "卧槽,没找到你手机上的相机你敢信?!",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
