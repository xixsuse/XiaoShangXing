package com.xiaoshangxing.xiaoshang.idlesale.SelectImageFromSd.ImageSelectActivity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.utils.photo_choosing.ImageBucket;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.xiaoshang.idlesale.SelectImageFromSd.AllPhotoActivity.AllIPhotosActivity;
import com.xiaoshangxing.xiaoshang.idlesale.SelectImageFromSd.PreviewPhotoPagerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageDirectoryActivity extends BaseActivity implements ImageDirectoryContract.View{


    private ImageDirectoryContract.Presenter mPresenter;

    @Bind(R.id.rv_images_directory)
    RecyclerView directoryList;
    ImageDirectoryAdapter adapter;
    private ArrayList<String> selectedImages = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_directory);
        ButterKnife.bind(this);
        mPresenter =    new ImageDirctoryPresenter(this,this);
        mPresenter.setImageBucket();
        selectedImages = getIntent().getStringArrayListExtra(PreviewPhotoPagerActivity.SELECTED_PICTURE_PATHES);
    }

    @Override
    public void setImageDirectory(List<ImageBucket> list) {
        adapter = new ImageDirectoryAdapter(this,list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        directoryList.setLayoutManager(manager);
        directoryList.setAdapter(adapter);
        directoryList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void setPresenter(@Nullable ImageDirectoryContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    public void toAllPhotoActivity(ArrayList<String> paths){
        Intent intent = new Intent(this,AllIPhotosActivity.class);
        intent.putExtra(AllIPhotosActivity.ALL_IMAGES,paths);
        intent.putExtra(AllIPhotosActivity.SELECTED_IMAGE,selectedImages);
        Log.d("ImageDir",selectedImages.size()+"");
        startActivity(intent);
        finish();
    }

    @Override
    public void finish() {

        super.finish();
    }
}
