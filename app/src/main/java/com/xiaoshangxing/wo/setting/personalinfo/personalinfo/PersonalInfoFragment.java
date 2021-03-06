package com.xiaoshangxing.wo.setting.personalinfo.personalinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.utils.baseClass.BaseFragment;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.wo.setting.personalinfo.PersonalInfoActivity;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;
import com.xiaoshangxing.yujian.IM.uinfo.SelfInfoObserver;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tianyang on 2016/7/9.
 */
public class PersonalInfoFragment extends BaseFragment {
    public static final String TAG = BaseFragment.TAG + "-PersonalInfoFragment";
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
    @Bind(R.id.setting_personinfo_headView)
    CirecleImage settingPersoninfoHeadView;
    @Bind(R.id.right_arrow)
    ImageView rightArrow;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.mycode_rightarrow)
    ImageView mycodeRightarrow;
    @Bind(R.id.linear1)
    LinearLayout linear1;
    @Bind(R.id.sex)
    TextView sex;
    @Bind(R.id.personinfo_hometown)
    TextView personinfoHometown;
    @Bind(R.id.hometown_rightarrow)
    ImageView hometownRightarrow;
    @Bind(R.id.signature)
    TextView signature;
    @Bind(R.id.autograph_rightarrow)
    ImageView autographRightarrow;
    @Bind(R.id.real_name)
    TextView realName;
    @Bind(R.id.certification_rightarrow)
    ImageView certificationRightarrow;


    private View mView;
    private PersonalInfoActivity mActivity;
    private NimUserInfo nimUserInfo;
    private String id;
    private SelfInfoObserver.SelfInfoCallback observer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_personinfo, container, false);
        ButterKnife.bind(this, mView);
        mActivity = (PersonalInfoActivity) getActivity();
        ViewGroup.LayoutParams para;
        para = settingPersoninfoHeadView.getLayoutParams();
        mActivity.setImagCoverHeight(para.height);
        mActivity.setImagCoverWidth(para.width);
        initView();
        return mView;
    }

    private void initView() {
        setCloseActivity();
        id = String.valueOf(TempUser.id);
        title.setText("个人信息");
        more.setVisibility(View.GONE);
        oberverUserInfo(true);
        initInfo();
    }

    private void oberverUserInfo(boolean is) {
        if (observer == null) {
            observer = new SelfInfoObserver.SelfInfoCallback() {
                @Override
                public void onCallback(NimUserInfo userInfo) {
                    initInfo();
                }
            };
        }
        SelfInfoObserver.getInstance().registerObserver(observer, is);
    }

    private void initInfo() {
        nimUserInfo = NimUserInfoCache.getInstance().getUserInfo(id);
        if (nimUserInfo == null) {
            Toast.makeText(getContext(), "账号异常,请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        name.setText(nimUserInfo.getName());
        if (nimUserInfo.getGenderEnum().equals(GenderEnum.FEMALE)) {
            sex.setText("女");
        } else if (nimUserInfo.getGenderEnum().equals(GenderEnum.MALE)) {
            sex.setText("男");
        } else {
            sex.setText("未知");
        }

        UserInfoCache.getInstance().getExIntoTextview(id, NS.HOMETOWN, personinfoHometown, "未填写");
        UserInfoCache.getInstance().getHeadIntoImage(id, settingPersoninfoHeadView);
//        Log.d("homeString", "" + nimUserInfo.getExtensionMap());
//        if (nimUserInfo.getExtensionMap() == null) {
//            Log.d("homeString", "null");
//            personinfoHometown.setText("未填写");
//        } else {
//            String homeString = String.valueOf(nimUserInfo.getExtensionMap().get(NS.HOMETOWN));
//            Log.d("homeString", "" + homeString);
//            if (TextUtils.isEmpty(homeString) || homeString.equals("null")) {
//                personinfoHometown.setText("未填写");
//            }
//        }

        if (!TextUtils.isEmpty(nimUserInfo.getSignature()) && !nimUserInfo.getSignature().equals("null")) {
            signature.setVisibility(View.GONE);
        } else {
            signature.setVisibility(View.VISIBLE);
        }

        if (nimUserInfo.getExtensionMap() != null) {
            if (nimUserInfo.getExtensionMap().get("isActive") != null) {
                switch ((int) nimUserInfo.getExtensionMap().get("isActive")) {
                    case 0:
                        realName.setText("未认证");
                        break;
                    case 1:
                        realName.setText("已认证");
                        break;
                    case 2:
                        realName.setText("审核中");
                        break;
                    case 3:
                        realName.setText("审核失败");
                        break;
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setCloseActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        oberverUserInfo(false);
    }

    @OnClick(R.id.back)
    public void onClick() {
        getActivity().finish();
    }
}
