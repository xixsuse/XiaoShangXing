package com.xiaoshangxing.setting.shiming.vertify;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.setting.shiming.result.VertifyingActivity;
import com.xiaoshangxing.setting.shiming.shenhe.PreviewActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.BroadCast.FinishActivityRecever;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.IntentStatic;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by tianyang on 2016/10/5.
 */
public class VertifyActivity extends BaseActivity implements IBaseView {

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
    @Bind(R.id.nameWrongImg)
    ImageView nameWrongImg;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.sexWrongImg)
    ImageView sexWrongImg;
    @Bind(R.id.sex)
    TextView sex;
    @Bind(R.id.sexRightArrow)
    ImageView sexRightArrow;
    @Bind(R.id.xuehaoWrongImg)
    ImageView xuehaoWrongImg;
    @Bind(R.id.xuehao)
    TextView xuehao;
    @Bind(R.id.schholWrongImg)
    ImageView schholWrongImg;
    @Bind(R.id.school)
    TextView school;
    @Bind(R.id.schoolRightArrow)
    ImageView schoolRightArrow;
    @Bind(R.id.yearWrongImg)
    ImageView yearWrongImg;
    @Bind(R.id.ruxuenianfen)
    TextView ruxuenianfen;
    @Bind(R.id.ruxuenianfenRightArrow)
    ImageView ruxuenianfenRightArrow;
    @Bind(R.id.VertifyButton)
    Button VertifyButton;
    private FinishActivityRecever finishActivityRecever;

    public static String nameStr, sexStr, xuehaoStr, schoolStr, colleg,
            professional, ruxuenianfenStr, degree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify);
        ButterKnife.bind(this);
        finishActivityRecever = new FinishActivityRecever(this);
        finishActivityRecever.register();
        title.setText("实名认证");
        more.setVisibility(View.GONE);
        titleBottomLine.setVisibility(View.GONE);
    }


    public void Name(View view) {
        startActivity(new Intent(this, NameActivity.class));
    }

    public void XingBie(View view) {
        startActivity(new Intent(this, XingBieActivity.class));
    }

    public void XueHao(View view) {
        startActivity(new Intent(this, XueHaoActivity.class));
    }

    public void XueXiao(View view) {
        startActivity(new Intent(this, XueXiaoActivity.class));
    }

    public void RuXueNianFen(View view) {
        startActivity(new Intent(this, YearActivity.class));
    }

    public void VertifyNow(View view) {
//        startActivity(new Intent(this, VertifyingActivity.class));

//        final Map<String, String> map = new HashMap<>();
//        map.put(NS.USER_ID, TempUser.getId());
//        map.put("name", nameStr);
//        map.put("sex", sexStr);
//        map.put("studentNum", xuehaoStr);
//        map.put("schoolName", schoolStr);
//        map.put("college", colleg);
//        map.put("profession", professional);
//        map.put("admissionYear", ruxuenianfenStr);
//        map.put("degree", degree);
//        final ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add(PreviewActivity.getLeftImgPath("XueShengZhen"));
//        arrayList.add(PreviewActivity.getRightImgPath("XueShengZhen"));
//
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Formmat formmat = new Formmat(VertifyActivity.this, VertifyActivity.this, BaseUrl.BASE_URL + BaseUrl.REAL_NAME);
//                formmat.setSimpleCallBack(new SimpleCallBack() {
//                    @Override
//                    public void onSuccess() {
//                        startActivity(new Intent(VertifyActivity.this, VertifyingActivity.class));
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onBackData(Object o) {
//
//                    }
//                });
//
//                try {
//                    formmat.addFormField(map)
//                            .addFilePart(arrayList, VertifyActivity.this)
//                            .doUpload();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    showToast("图片出错");
//                }
//            }
//        });
//        thread.start();
        realName();
    }

    private void realName() {
        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    if (jsonObject.getString(NS.CODE).equals("200")) {
                        startActivity(new Intent(VertifyActivity.this, VertifyingActivity.class));
                    } else {
                        showToast(jsonObject.getString(NS.MSG));
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };

        ProgressSubsciber<ResponseBody> progressSubsciber = new ProgressSubsciber<>(onNext, this);
        progressSubsciber.setLoadingText("上传中...");

        File left = new File(PreviewActivity.getLeftImgPath("XueShengZhen"));
        File right = new File(PreviewActivity.getRightImgPath("XueShengZhen"));
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), left/*SendImageHelper.getLittleImage(coverPath, getContext())*/);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", left.getName(), requestFile);
        RequestBody requestFile1 =
                RequestBody.create(MediaType.parse("multipart/form-data"), right/*SendImageHelper.getLittleImage(coverPath, getContext())*/);
        MultipartBody.Part body1 =
                MultipartBody.Part.createFormData("file", left.getName(), requestFile1);

        String sex = sexStr.equals("男") ? "1" : "2";

        InfoNetwork.getInstance().realName(progressSubsciber, TempUser.getID(this), nameStr, sex, xuehaoStr, schoolStr,
                colleg, professional, ruxuenianfenStr, degree, body, body1, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (nameStr != null) {
            name.setText(nameStr);
        }
        if (xuehaoStr != null) {
            xuehao.setText(xuehaoStr);
        }
        if (sexStr != null) {
            sex.setText(sexStr);
        }
        if (ruxuenianfenStr != null) {
            ruxuenianfen.setText(ruxuenianfenStr);
        }
        if (schoolStr != null) {
            school.setText(schoolStr);
        }
        if (isfilled()) setButtonStyleGreen();
        else resetButtonStyle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishActivityRecever.unregister();
    }

    public void setButtonStyleGreen() {
        VertifyButton.setAlpha(1);
        VertifyButton.setBackground(getResources().getDrawable(R.drawable.buttonstyle_green1));
        VertifyButton.setTextColor(getResources().getColor(R.color.w0));
        VertifyButton.setEnabled(true);
    }

    //还原button为浅色
    public void resetButtonStyle() {
        VertifyButton.setAlpha((float) 0.3);
        VertifyButton.setBackground(getResources().getDrawable(R.drawable.buttonstyle_w0));
        VertifyButton.setTextColor(getResources().getColor(R.color.b0));
        VertifyButton.setEnabled(false);
    }

    public boolean isfilled() {
        return !(TextUtils.isEmpty(nameStr) || TextUtils.isEmpty(sexStr) || TextUtils.isEmpty(xuehaoStr)
                || TextUtils.isEmpty(schoolStr) || TextUtils.isEmpty(colleg) || TextUtils.isEmpty(sexStr)
                || TextUtils.isEmpty(professional) || TextUtils.isEmpty(ruxuenianfenStr) || TextUtils.isEmpty(degree));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getIntExtra(IntentStatic.TYPE, -1) == IntentStatic.CLOSE) {
            finish();
        }
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }
}
