package com.xiaoshangxing.wo.setting.shiming.shenhe;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.normalUtils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tianyang on 2016/9/20.
 */
public class PreviewActivity extends Activity {
    public static String imgName = "img.jpeg";
    public static String STUDENT_CARD_LEFT = "STUDENT_CARD_LEFT.jpg";
    public static String STUDENT_CARD_RIGHT = "STUDENT_CARD_RIGHT.jpg";
    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.text2)
    TextView text2;
    @Bind(R.id.use)
    TextView use;
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.retake)
    TextView retake;
    @Bind(R.id.take)
    ImageView take;
    private CameraPreview mPreview;
    //    private boolean isSaved = false;
    private String type;
    private int zuoyou;
    private FrameLayout preview;
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("qqq", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("qqq", "Error accessing file: " + e.getMessage());
            }
        }
    };

    public static String getLeftImgPath(String type) {
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "MyCameraApp");
//        return mediaStorageDir.getPath() + File.separator + type + "imgLeft.jpeg";
        return FileUtils.getXSX_CameraPhotoPath() + STUDENT_CARD_LEFT;
    }

    public static String getRightImgPath(String type) {
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "MyCameraApp");
//        return mediaStorageDir.getPath() + File.separator + type + "imgRight.jpeg";
        return FileUtils.getXSX_CameraPhotoPath() + STUDENT_CARD_RIGHT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_preview);
        ButterKnife.bind(this);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        initText();

        mPreview = new CameraPreview(this);
        preview.addView(mPreview);
    }

    private void initText() {

        type = getIntent().getStringExtra("VertifyType");
        zuoyou = getIntent().getIntExtra("ZuoYou", 0);
        if (type.equals("XueShengZhen")) {
            text1.setText("学生证");
            if (zuoyou == 0) text2.setText("【封面】");
            else if (zuoyou == 1) text2.setText("【内页姓名页】");
        } else if (type.equals("XueShengKa")) {
            text1.setText("学生卡");
            if (zuoyou == 0) text2.setText("【正面】");
            else if (zuoyou == 1) text2.setText("【反面】");
        } else if (type.equals("TongZhiShu")) {
            text1.setText("录取通知书");
            if (zuoyou == 0) text2.setText("【正面姓名页】");
            else if (zuoyou == 1) text2.setText("【学校公章】");
        }

//        if (zuoyou == 0) imgName = type + "imgLeft.jpeg";
//        else imgName = type + "imgRight.jpeg";

    }

    private File getOutputMediaFile() {
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "MyCameraApp");
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Log.d("MyCameraApp", "failed to create directory");
//                return null;
//            }
//        }
//
//
//        File mediaFile;
//        mediaFile = new File(mediaStorageDir.getPath() + File.separator + imgName);
//        return mediaFile;
        File file;
        if (zuoyou == 0) {
            file = new File(getLeftImgPath("0"));
        } else {
            file = new File(getRightImgPath("0"));
        }
        return file;
    }

    private int getPictureSize(List<Camera.Size> sizes) {
        // 屏幕的宽度  
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int index = -1;

        for (int i = 0; i < sizes.size(); i++) {
            if (Math.abs(screenWidth - sizes.get(i).width) == 0) {
                index = i;
                break;
            }
        }
        // 当未找到与手机分辨率相等的数值,取列表中间的分辨率  
        if (index == -1) {
            index = sizes.size() / 2;
        }
        return index;
    }


    public void Use(View view) {
        finish();
    }


    public void Take(View view) {
        if (mPreview.getmCamera() != null) {
            //解决拍照后照片模糊的bug
            Camera mCamera = mPreview.getmCamera();
            Camera.Parameters parameters = mCamera.getParameters();// 获取相机参数集 
//                        List<Camera.Size> SupportedPreviewSizes = parameters.getSupportedPreviewSizes();// 获取支持预览照片的尺寸  
//                        Camera.Size previewSize = SupportedPreviewSizes.get(0);// 从List取出Size  
//                        parameters.setPreviewSize(previewSize.width, previewSize.height);//  
            //  设置预览照片的大小  
            List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();// 获取支持保存图片的尺寸  
            int index = getPictureSize(supportedPictureSizes);
            Camera.Size pictureSize = supportedPictureSizes.get(index);
            parameters.setPictureSize(pictureSize.width, pictureSize.height);
            // 设置照片的大小  
            mCamera.setParameters(parameters);
            mCamera.takePicture(null, null, mPicture);

            cancel.setVisibility(View.GONE);
            use.setVisibility(View.VISIBLE);
            retake.setVisibility(View.VISIBLE);
            take.setEnabled(false);
        }

    }

    public void Cancel(View view) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile != null) {
            pictureFile.delete();
        }
        finish();
    }

    public void ReTake(View view) {
        take.setEnabled(true);

        mPreview.getmCamera().startPreview();

        retake.setVisibility(View.GONE);
        use.setVisibility(View.GONE);
        cancel.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile != null) {
                pictureFile.delete();
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
