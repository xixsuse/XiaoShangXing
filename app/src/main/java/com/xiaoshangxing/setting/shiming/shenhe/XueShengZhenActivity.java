package com.xiaoshangxing.setting.shiming.shenhe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.shiming.vertify.VertifyActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.BroadCast.FinishActivityRecever;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tianyang on 2016/9/19.
 */
public class XueShengZhenActivity extends BaseActivity {
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.personinfo_headimg)
    ImageView personinfoHeadimg;
    @Bind(R.id.personinfo_name)
    TextView personinfoName;
    @Bind(R.id.imageLeft)
    ImageView imageLeft;
    @Bind(R.id.imageRight)
    ImageView imageRight;
    @Bind(R.id.personinfo_xueyaun)
    TextView personinfoXueyaun;
    @Bind(R.id.NextButtom)
    Button NextButtom;

    private FinishActivityRecever finishActivityRecever;
    private static boolean flagLeft = false, flagRight = false;

    @Bind(R.id.wrongLeft)
    ImageView wrongLeft;
    @Bind(R.id.wrongRight)
    ImageView wrongRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shenhe_xueshengzhen);
        ButterKnife.bind(this);
        finishActivityRecever = new FinishActivityRecever(this);
        finishActivityRecever.register();
        title.setText("实名认证");
        more.setVisibility(View.GONE);
        titleBottomLine.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int x, y, width, height;

        String pathLeft = PreviewActivity.getLeftImgPath("XueShengZhen");
        File fileLeft = new File(pathLeft);
        if (fileLeft.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(pathLeft);
            x = (int) (bm.getWidth() * ((float) 204 / 1920));
            y = (int) (bm.getHeight() * ((float) 174 / 1080));
            width = (int) (bm.getWidth() * ((float) 1146 / 1920));
            height = (int) (bm.getHeight() * ((float) 732 / 1080));
            Bitmap bitmap = Bitmap.createBitmap(bm, x, y, width, height);
            imageLeft.setImageBitmap(bitmap);
            flagLeft = true;
//            fileLeft.delete();
//            VertifyUtil.saveFile(bitmap, VertifyUtil.imgLeftName);
        }

        String pathRight = PreviewActivity.getRightImgPath("XueShengZhen");
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
//            fileRight.delete();
//            VertifyUtil.saveFile(bitmap, VertifyUtil.imgRightName);
        }

//        setButtonStyleGreen();

        if (flagLeft && flagRight) setButtonStyleGreen();
        else resetButtonStyle();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flagLeft = false;
        flagRight = false;
        finishActivityRecever.unregister();
    }

    public void Next(View view) {
        startActivity(new Intent(this, VertifyActivity.class));
        //成功后跳转VertifySucessActivity
//        startActivity(new Intent(this, VertifySucessActivity.class));
    }

    public void imgLeft(View view) {
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra("VertifyType", "XueShengZhen");
        intent.putExtra("ZuoYou", 0);
        startActivity(intent);
    }

    public void imgRight(View view) {
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra("VertifyType", "XueShengZhen");
        intent.putExtra("ZuoYou", 1);
        startActivity(intent);
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


    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}