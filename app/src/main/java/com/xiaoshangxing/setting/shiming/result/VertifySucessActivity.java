package com.xiaoshangxing.setting.shiming.result;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tianyang on 2016/9/20.
 */
public class VertifySucessActivity extends BaseActivity {


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
        name.setText(nimUserInfo.getName());
        if (nimUserInfo.getGenderEnum().equals(GenderEnum.MALE)) {
            sex.setText("男");
        } else if (nimUserInfo.getGenderEnum().equals(GenderEnum.FEMALE)) {
            sex.setText("女");
        } else {
            sex.setText("未知");
        }
        xuehao.setText("未知");
        school.setText("未知");
        ruxuenianfen.setText("未知");
    }


    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
