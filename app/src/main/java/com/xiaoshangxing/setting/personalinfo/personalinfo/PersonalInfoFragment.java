package com.xiaoshangxing.setting.personalinfo.personalinfo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.setting.personalinfo.PersonalInfoActivity;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.XSXApplication;
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
    private Handler handler;
    private User user;

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
        handler = new Handler();
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).equalTo("id",
                (Integer) SPUtils.get(getContext(),SPUtils.ID,SPUtils.DEFAULT_int)).findFirst();
        if (user == null) {
            Toast.makeText(getContext(), "账号异常,请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }

        user.addChangeListener(new RealmChangeListener<User>() {
            @Override
            public void onChange(User element) {
                initInfo(element);
            }
        });



        Subscriber<ResponseBody> subscriber=new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                /*mActivity.showToast("更新信息成功");*/
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
                                    User user1 = realm.createOrUpdateObjectFromJson(User.class, user);
                                    UserInfoCache.getInstance().refreshSomeone(user1);
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

        initInfo(user);
    }

    private void initInfo(final User user) {
        MyGlide.with(XSXApplication.getInstance(), user.getUserImage(), settingPersoninfoHeadView);

        name.setText(user.getUsername());
        if (user.getSex() != null) {
            sex.setText(user.getSex() == 0 ? "男" : "女");
        }
        personinfoHometown.setText(user.getHometown());
        if (!TextUtils.isEmpty(user.getSignature())&&!user.getSignature().equals("null")){
            signature.setVisibility(View.GONE);
        }else {
            signature.setVisibility(View.VISIBLE);
        }
        if (user.getIsActive() != null) {
            realName.setText(user.getIsActive() == 0 ? "未认证" : "已认证");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (user!=null){
            user.removeChangeListeners();
        }
        realm.close();
        ButterKnife.unbind(this);
    }
}
