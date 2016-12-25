package com.xiaoshangxing.wo.setting.realName.shenhe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xiaoshangxing.network.InfoNetwork;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.bean.RealNameInfo;
import com.xiaoshangxing.wo.setting.realName.vertify.VertifyActivity;
import com.xiaoshangxing.utils.BroadCast.FinishActivityRecever;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.baseClass.IBaseView;
import com.xiaoshangxing.utils.imageUtils.SaveImageTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 *modified by FengChaoQun on 2016/12/24 16:29
 * description:优化代码
 */
public class StudentIdentityCardActivity extends BaseActivity implements IBaseView {
    public static RealNameInfo realNameInfo;
    private static boolean flagLeft = false, flagRight = false;
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
    @Bind(R.id.wrongLeft)
    ImageView wrongLeft;
    @Bind(R.id.wrongRight)
    ImageView wrongRight;
    private FinishActivityRecever finishActivityRecever;
    private boolean isFailed;

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
        if (getIntent().getBooleanExtra(IntentStatic.TYPE, false)) {
            isFailed = true;
            getInfo();
        }
    }

    private void getInfo() {
        ProgressSubscriberOnNext<ResponseBody> next = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case NS.CODE_200:
                            Gson gson = new Gson();
                            realNameInfo = gson.fromJson(jsonObject.getString(NS.MSG), RealNameInfo.class);
                            refresh();
                            break;
                        default:
                            showToast(jsonObject.getString(NS.MSG));
                            break;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };

        ProgressSubsciber<ResponseBody> subsciber = new ProgressSubsciber<>(next, this);
        InfoNetwork.getInstance().queryRealInfo(subsciber, TempUser.getId(), this);
    }

    private void refresh() {
        if (realNameInfo == null) {
            return;
        }
        if (realNameInfo.getSidImages() != null) {
            String[] images = realNameInfo.getSidImages().split(NS.SPLIT3);
            if (images.length == 3) {
                SimpleCallBack simpleCallBack = new SimpleCallBack() {
                    @Override
                    public void onSuccess() {
                        loadImages();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onBackData(Object o) {

                    }
                };
                SaveImageTask saveLeft = new SaveImageTask(this, PreviewActivity.getLeftImgPath("XueShengZhen"), null, null);
                saveLeft.setSimpleCallBack(simpleCallBack);
                saveLeft.execute(images[0]);
                SaveImageTask saveRight = new SaveImageTask(this, PreviewActivity.getRightImgPath("XueShengZhen"), null, null);
                saveRight.setSimpleCallBack(simpleCallBack);
                saveRight.execute(images[1]);
                if (!images[2].equals("1")) {
                    wrongLeft.setVisibility(View.VISIBLE);
                    wrongRight.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadImages();
    }

    private void loadImages() {
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
        }

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
        Intent intent = new Intent(this, VertifyActivity.class);
        intent.putExtra(IntentStatic.TYPE, true);
        startActivity(intent);
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

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }
}