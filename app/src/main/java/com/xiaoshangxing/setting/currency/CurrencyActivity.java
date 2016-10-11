package com.xiaoshangxing.setting.currency;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.currency.chatBackground.ChatBackgroundActivity;
import com.xiaoshangxing.setting.currency.chatBackground.ChatBackgroundFragment;
import com.xiaoshangxing.setting.currency.chooseBackgroundFragment.ChooseBackgroundFragment;
import com.xiaoshangxing.setting.currency.currenctFragment.CurrencyFragment;
import com.xiaoshangxing.setting.utils.headimg_set.CommonUtils;
import com.xiaoshangxing.setting.utils.headimg_set.FileUtil;
import com.xiaoshangxing.setting.utils.headimg_set.ToastUtils;
import com.xiaoshangxing.utils.BaseActivity;

/**
 * Created by 15828 on 2016/7/14.
 */
public class CurrencyActivity extends BaseActivity {
    public static final int ACTIVITY_ALBUM_REQUESTCODE = 1000;
    public static final int ACTIVITY_CAMERA_REQUESTCODE = 1001;
    public static final int ACTIVITY_MODIFY_PHOTO_REQUESTCODE = 2002;
    private Bitmap btmap_album, btmap_phone, mBitmap;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_fraglayout);
        mFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, new CurrencyFragment())
                .commit();
    }

    public void currency_back(View view) {
        finish();
    }

    public void ChatBackground(View view) {

        Intent intent = new Intent(this, ChatBackgroundActivity.class);
        startActivity(intent);
    }

}
