package com.xiaoshangxing.utils.photoChoosing;

import android.os.Bundle;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;

/**
 * Created by 15828 on 2016/7/26.
 */
public class PhotoChoosingActivity extends BaseActivity {
    private String folderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_choose);
        mFragmentManager.beginTransaction()
                .replace(R.id.photoChooseContent, new AlbumFragment())
                .commit();
    }


    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
