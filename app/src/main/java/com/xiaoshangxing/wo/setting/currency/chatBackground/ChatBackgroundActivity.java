package com.xiaoshangxing.wo.setting.currency.chatBackground;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.LocalDataUtils;
import com.xiaoshangxing.wo.setting.currency.chooseBackgroundFragment.ChooseBackgroundFragment;
import com.xiaoshangxing.wo.setting.utils.headimg_set.CommonUtils;
import com.xiaoshangxing.wo.setting.utils.headimg_set.FileUtil;
import com.xiaoshangxing.wo.setting.utils.headimg_set.ToastUtils;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.normalUtils.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by tianyang on 2016/8/20.
 */
public class ChatBackgroundActivity extends BaseActivity {
    public static final int ACTIVITY_ALBUM_REQUESTCODE = 1000;
    public static final int ACTIVITY_CAMERA_REQUESTCODE = 1001;
    public static final int ACTIVITY_MODIFY_PHOTO_REQUESTCODE = 2002;
    public static String image;
    private ImageView mImageView;
    private String account;//记录是否是设置个别背景

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_fraglayout);

        mFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, new ChatBackgroundFragment())
                .commit();

        account = getIntent().getStringExtra(IntentStatic.ACCOUNT);
    }


    public void ChooseBackground(View view) {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .addToBackStack(null)
                .replace(R.id.main_fragment, new ChooseBackgroundFragment())
                .commit();
    }

    public void ChooseFromPhone(View view) {
        IntentStatic.openCamera(this, Uri.fromFile(FileUtil.getHeadPhotoFileRaw()), ACTIVITY_CAMERA_REQUESTCODE);
    }

    public void ChooseFromAlbum(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, null);// 调用android的图库
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(i, ACTIVITY_ALBUM_REQUESTCODE);
    }


    public void setmImageView(ImageView mImageView) {
        this.mImageView = mImageView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_ALBUM_REQUESTCODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getData() == null) {
                        ToastUtils.toast(this, getString(R.string.pic_not_valid));
                        return;
                    }
                    WindowManager wm = this.getWindowManager();
                    int width = wm.getDefaultDisplay().getWidth();
                    int height = wm.getDefaultDisplay().getHeight();
                    CommonUtils.cutPhoto(this, data.getData(), false, width, height);
                }

                break;
            case ACTIVITY_CAMERA_REQUESTCODE:
                if (resultCode == Activity.RESULT_OK) {
                    WindowManager wm = this.getWindowManager();
                    int width = wm.getDefaultDisplay().getWidth();
                    int height = wm.getDefaultDisplay().getHeight();
                    CommonUtils.cutPhoto(this, Uri.fromFile(FileUtil.getHeadPhotoFileRaw()), false, width, height);
                }
                break;
            case ACTIVITY_MODIFY_PHOTO_REQUESTCODE:
                String coverPath = FileUtil.getHeadPhotoDir() + FileUtil.HEADPHOTO_NAME_TEMP;
                final File file = FileUtils.newImageFile();
                try {
                    FileUtils.copyFileTo(new File(coverPath), file);
                    image = file.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LocalDataUtils.saveBackgroud(account, image, false, this);
            default:
                break;
        }
    }

    public String getAccount() {
        return account;
    }

}
