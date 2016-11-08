package com.xiaoshangxing.setting.shiming.result;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.RealNameInfo;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * Created by tianyang on 2016/9/20.
 */
public class VertifySucessActivity extends BaseActivity implements IBaseView {


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
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.sex)
    TextView sex;
    @Bind(R.id.xuehao)
    TextView xuehao;
    @Bind(R.id.school)
    TextView school;
    @Bind(R.id.ruxuenianfen)
    TextView ruxuenianfen;
    private NimUserInfo nimUserInfo;
    private RealNameInfo realNameInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_sucess);
        ButterKnife.bind(this);
        title.setText("实名认证");
        more.setVisibility(View.GONE);
        titleBottomLine.setVisibility(View.GONE);

        nimUserInfo = NimUserInfoCache.getInstance().getUserInfo(TempUser.getId());
        if (nimUserInfo == null) {
            showToast("个人信息获取失败");
            return;
        }
        getInfo();
//        name.setText(nimUserInfo.getName());
//        if (nimUserInfo.getGenderEnum().equals(GenderEnum.MALE)) {
//            sex.setText("男");
//        } else if (nimUserInfo.getGenderEnum().equals(GenderEnum.FEMALE)) {
//            sex.setText("女");
//        } else {
//            sex.setText("未知");
//        }
    }

    private void refresh() {
        name.setText(RealNameInfo.getOnlyString(realNameInfo.getName()));
        String sexString = RealNameInfo.getOnlyString(realNameInfo.getSex());
        if (sexString.equals("1")) {
            sex.setText("男");
        } else if (sexString.equals("2")) {
            sex.setText("女");
        } else {
            sex.setText("未知");
        }
        xuehao.setText(RealNameInfo.getOnlyString(realNameInfo.getStudentNum()));
        school.setText(RealNameInfo.getOnlyString(realNameInfo.getSchoolName()));
        ruxuenianfen.setText(RealNameInfo.getOnlyString(realNameInfo.getAdmissionYear()));
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


    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }
}
