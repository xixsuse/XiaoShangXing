package com.xiaoshangxing.xiaoshang.idlesale.SelectImageFromSd.AllPhotoActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.xiaoshang.idlesale.SelectImageFromSd.ImageSelectActivity.ImageDirectoryActivity;
import com.xiaoshangxing.xiaoshang.idlesale.SelectImageFromSd.PreviewPhotoPagerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by quchwe on 2016/8/5 0005.
 */
public class AllIPhotosActivity extends BaseActivity implements View.OnClickListener,SeletPhotoContract.View {

    public static ArrayList<String> SELECTED_IMAGES = new ArrayList<>();
    public static ArrayList<String> CURRENTDIR_IMAGES = new ArrayList<>();
    public static final int SELECT_PHOTO_RESULT_OK = 1;
    public static final String SELECTED_IMAGE = "selectedImages";
    public static final String ALL_IMAGES = "allImages";
    @Bind(R.id.back)
    Button back;
    @Bind(R.id.btn_preview)
    Button preview;
    @Bind(R.id.picture_count)
    TextView pictureCount;
    @Bind(R.id.complete)
    TextView complete;
    @Bind(R.id.rv_album_grid)
    RecyclerView myRecyler;

    int currentAllSlectedPos;
    int previewSelectPos;
    ImageSelectAdapter adapter;
    private List<String> paths = new ArrayList<>();
    private SeletPhotoContract.Presenter mPresenter;
    private ArrayList<String> selectPicturePath = new ArrayList<>();
    String currentSelect;
    ArrayList<String> selectedImages = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idle_select_image);

        ButterKnife.bind(this);
        selectedImages = getIntent().getStringArrayListExtra(SELECTED_IMAGE);
        if (selectedImages!=null&&selectedImages.size()!=0){
            setPicteureSelectCount(selectedImages.size());
            setPreview(true);
        }
        paths = getIntent().getStringArrayListExtra(ALL_IMAGES);

        mPresenter = new SelectPhotoPresenter(this,this);
        if (paths==null||paths.size()==0){
            paths = mPresenter.getAllPicturePath();
        }

        setRecylerView();

    }

    @OnClick({R.id.back, R.id.complete, R.id.btn_preview})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
               toDirectoryView();
                finish();
                break;
            case R.id.complete:
//                setSlectPicturePath();
                Log.d("wqvbthis",selectPicturePath.size()+"");
                Intent k = new Intent(this, InputActivity.class);
                k.putExtra(InputActivity.EDIT_STATE, InputActivity.XIANZHI);
                k.putStringArrayListExtra("path",selectPicturePath);
                startActivity(k);
                finish();
                break;
            case R.id.btn_preview:
//                toPreview();
        }

    }

    @Override
    public void setPicteureSelectCount(int count) {
            pictureCount.setText(""+count);
    }

    @Override
    public void toPreview() {
        Log.d("点击图片","点击");
    currentSelected(null);
        if (currentSelect==null){
            return;
        }
//        setSlectPicturePath();
        Intent intent = new Intent(this,PreviewPhotoPagerActivity.class);
        ArrayList<String> urls=(ArrayList<String>) paths;
        ArrayList<String> selected=(ArrayList<String>) selectPicturePath;
        intent.putExtra(PreviewPhotoPagerActivity.ALL_IMAGE_PATHS, urls);
        intent.putExtra(PreviewPhotoPagerActivity.EXTRA_IMAGE_INDEX, currentAllSlectedPos);
        intent.putExtra(PreviewPhotoPagerActivity.SELECTED_PICTURE_PATHES, selected);
        intent.putExtra(PreviewPhotoPagerActivity.LIMIT,3);
        startActivityForResult(intent,SELECT_PHOTO_RESULT_OK);
    }
    @Override
    public void setRecylerView() {
        adapter = new ImageSelectAdapter(this, paths,selectedImages);
        myRecyler.setLayoutManager(new GridLayoutManager(this,4));
        myRecyler.setAdapter(adapter);
        myRecyler.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void currentSelected(String picturePath) {
        currentSelect  = adapter.getCurrentSelect();
        currentAllSlectedPos = adapter.getPosition();

    }

    @Override
    public void setmPresenter(@NonNull SeletPhotoContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
    @Override
    public void setSlectPicturePath(ArrayList<String> selectedImages){
       selectPicturePath = selectedImages;
    }

    public void setPreview(boolean b){
        if (b){
            preview.setClickable(true);
            preview.setTextColor(getResources().getColor(R.color.b0));

            preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickPreview();
                }
            });
        }else {
            preview.setClickable(false);
            preview.setTextColor(getResources().getColor(R.color.g0));
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        toDirectoryView();
        finish();
        return super.onKeyDown(keyCode, event);

    }

    public void onClickPreview(){
        Intent intent = new Intent(this,PreviewPhotoPagerActivity.class);
        ArrayList<String> selected=(ArrayList<String>) selectPicturePath;
        intent.putExtra(PreviewPhotoPagerActivity.ALL_IMAGE_PATHS, selected);
        intent.putExtra(PreviewPhotoPagerActivity.EXTRA_IMAGE_INDEX, previewSelectPos);
        intent.putExtra(PreviewPhotoPagerActivity.SELECTED_PICTURE_PATHES, selected);
        intent.putExtra(PreviewPhotoPagerActivity.LIMIT,3);
        startActivityForResult(intent,SELECT_PHOTO_RESULT_OK);
    }
    //
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//       if (requestCode==InputActivity.SELECT_PHOTO_RESULT_1&&requestCode==RESULT_OK){
//           ArrayList<String> select = data.getStringArrayListExtra(InputActivity.SELECT_IMAGE_URLS);
//           selectPicturePath = select;
//           setPicteureSelectCount(selectPicturePath.size());
////           for ()
////           adapter.notifyItemChanged();
//       }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO_RESULT_OK&&data!=null){
            ArrayList<String> selected  = data.getStringArrayListExtra(SELECTED_IMAGE);
            setPicteureSelectCount(selected.size());
            selectPicturePath = selected;
            adapter.setSelectedImages(selected);
            adapter.notifyDataSetChanged();
        }
    }

    public  void toDirectoryView(){
        Intent intent =  new Intent(AllIPhotosActivity.this,ImageDirectoryActivity.class) ;
        ArrayList<String> selected=(ArrayList<String>) selectPicturePath;
        intent.putExtra(PreviewPhotoPagerActivity.SELECTED_PICTURE_PATHES, selected);
        startActivity(intent);
    }
    public void setSelectedPos(int currentAllSlectedPos,int previewSelectPos){
        this.currentAllSlectedPos = currentAllSlectedPos;
        this.previewSelectPos = previewSelectPos;
    }

}
