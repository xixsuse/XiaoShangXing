package com.xiaoshangxing.setting.personalinfo.personalinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.setting.personalinfo.PersonalInfoActivity;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.image.MyGlide;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by tianyang on 2016/7/9.
 */
public class PersonalInfoFragment extends BaseFragment {
    public static final String TAG = BaseFragment.TAG + "-PersonalInfoFragment";
    @Bind(R.id.toolbar_setting_leftarrow)
    ImageView toolbarSettingLeftarrow;
    @Bind(R.id.toolbar_setting_back)
    TextView toolbarSettingBack;
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
    private Realm realm;

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
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo("id",
                (Integer) SPUtils.get(getContext(),SPUtils.ID,SPUtils.DEFAULT_int)).findFirst();
        user.addChangeListener(new RealmChangeListener<User>() {
            @Override
            public void onChange(User element) {
                initInfo(element);
            }
        });

        initInfo(user);

        Subscriber<ResponseBody> subscriber=new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                mActivity.showToast("更新信息成功");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject=new JSONObject(responseBody.string());
                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))){
                        case 9001:
                            final JSONObject user=jsonObject.getJSONObject(NS.MSG);
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.createOrUpdateObjectFromJson(User.class,user);
                                }
                            });
                            break;
                        default:
                            mActivity.showToast("更新信息失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("id",user.getId());
        jsonObject.addProperty("timestamp",System.currentTimeMillis());
        InfoNetwork.getInstance().GetUser(subscriber,jsonObject,getContext());
    }

    private void initInfo(User user) {
        MyGlide.with(this,user.getPhotoCover(),settingPersoninfoHeadView);
        name.setText(user.getUsername());
        sex.setText("男");
        personinfoHometown.setText(user.getHometown());
        if (!TextUtils.isEmpty(user.getSignature())&&!user.getSignature().equals("null")){
            signature.setVisibility(View.GONE);
        }else {
            signature.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
        ButterKnife.unbind(this);
    }
}
