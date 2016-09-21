package com.xiaoshangxing.setting.shiming.shenhe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.shiming.vertify.VertifyShiMingActivity;
import com.xiaoshangxing.utils.BaseActivity;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tianyang on 2016/9/19.
 */
public class TongZhiShuActivity extends BaseActivity {
    @Bind(R.id.imageLeft)
    ImageView imageLeft;
    @Bind(R.id.imageRight)
    ImageView imageRight;

    private static boolean flagLeft = false, flagRight = false;
    @Bind(R.id.NextButtom)
    Button NextButtom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shenhe_tongzhishu);
        ButterKnife.bind(this);
    }

    public void Back(View view) {
        finish();
    }

    public void Next(View view) {
        startActivity(new Intent(this, VertifyShiMingActivity.class));
    }

    public void imgLeft(View view) {
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra("VertifyType", "TongZhiShu");
        intent.putExtra("ZuoYou", 0);
        startActivity(intent);
    }

    public void imgRight(View view) {
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra("VertifyType", "TongZhiShu");
        intent.putExtra("ZuoYou", 1);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        int x, y, width, height;
        String pathLeft = PreviewActivity.getLeftImgPath("TongZhiShu");
        File fileLeft = new File(pathLeft);
        if (fileLeft.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(pathLeft);
            x = (int) (bm.getWidth() * ((float) 204 / 1920));
            y = (int) (bm.getHeight() * ((float) 174 / 1080));
            width = (int) (bm.getWidth() * ((float) 1146 / 1920));
            height = (int) (bm.getHeight() * ((float) 732 / 1080));
            Log.d("qqq", "x:" + x + "  y:" + y + "  width:" + width + "  height:" + height);
            Log.d("qqq", "bm    w:" + bm.getWidth() + "   h:" + bm.getHeight());
            Bitmap bitmap = Bitmap.createBitmap(bm, x, y, width, height);
            imageLeft.setImageBitmap(bitmap);
            flagLeft = true;
        } else flagLeft = false;

        String pathRight = PreviewActivity.getRightImgPath("TongZhiShu");
        File fileRight = new File(pathRight);
        if (fileRight.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(pathRight);
            x = (int) (bm.getWidth() * ((float) 204 / 1920));
            y = (int) (bm.getHeight() * ((float) 174 / 1080));
            width = (int) (bm.getWidth() * ((float) 1146 / 1920));
            height = (int) (bm.getHeight() * ((float) 732 / 1080));
            Bitmap bitmap = Bitmap.createBitmap(bm, x, y, width, height);
            imageRight.setImageBitmap(bitmap);
            flagRight = true;
        } else flagRight = false;

        if (flagLeft && flagRight) setButtonStyleGreen();
        else resetButtonStyle();

    }

    //设置button为Green样式
    public void setButtonStyleGreen() {
        NextButtom.setAlpha(1);
        NextButtom.setBackground(getResources().getDrawable(R.drawable.buttonstyle_green1));
        NextButtom.setTextColor(getResources().getColor(R.color.w0));
        NextButtom.setEnabled(true);
    }

    //还原button为浅色
    public void resetButtonStyle() {
        NextButtom.setAlpha((float) 0.3);
        NextButtom.setBackground(getResources().getDrawable(R.drawable.buttonstyle_w0));
        NextButtom.setTextColor(getResources().getColor(R.color.b0));
        NextButtom.setEnabled(false);
    }
}
