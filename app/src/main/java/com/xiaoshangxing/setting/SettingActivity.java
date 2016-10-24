package com.xiaoshangxing.setting;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.netUtil.AppNetUtil;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.setting.about.AboutActivity;
import com.xiaoshangxing.setting.currency.CurrencyActivity;
import com.xiaoshangxing.setting.mailboxbind.MailBoxBindActivity;
import com.xiaoshangxing.setting.mailboxbind.ModifyMailBoxActivity;
import com.xiaoshangxing.setting.modifypassword.ModifyPassWordActivity;
import com.xiaoshangxing.setting.newNotice.NewNoticeActivity;
import com.xiaoshangxing.setting.personalinfo.PersonalInfoActivity;
import com.xiaoshangxing.setting.privacy.PrivacyActivity;
import com.xiaoshangxing.setting.utils.ActionSheet;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.image.MyGlide;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.normalUtils.SPUtils;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * Created by tianyang on 2016/7/9.
 */
public class SettingActivity extends BaseActivity implements IBaseView {

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
    @Bind(R.id.setting_main_imag)
    CirecleImage settingMainImag;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.linear1)
    LinearLayout linear1;
    @Bind(R.id.bindEmailStaet)
    TextView bindEmailStaet;
    @Bind(R.id.bindmail_rightarrow)
    ImageView bindmailRightarrow;
    private ActionSheet mActionSheet;
    private CirecleImage imgCover;
    private IBaseView iBaseView;
    private boolean bindEmail;
    private NimUserInfo nimUserInfo;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_setmain);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        imgCover = (CirecleImage) findViewById(R.id.setting_main_imag);
        iBaseView = this;
        id = String.valueOf(TempUser.id);
        nimUserInfo = NimUserInfoCache.getInstance().getUserInfo(id);
        if (nimUserInfo == null) {
            showToast("账号异常");
            return;
        }
        MyGlide.with(this, nimUserInfo.getAvatar(), imgCover);
        if (nimUserInfo.getExtension() != null && nimUserInfo.getExtension().contains("activeStatus")) {
            bindEmail = (int) nimUserInfo.getExtensionMap().get("activeStatus") == 1;
            Log.d("activeStatus", "" + bindEmail);
        }

        bindEmailStaet.setText(bindEmail ? "已绑定" : "未绑定");
        name.setText(nimUserInfo.getName());

        title.setText("设置");
        more.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        initView();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }
    public void ExitSetting(View view) {
        if (mActionSheet == null) {
            mActionSheet = new ActionSheet(this);
            mActionSheet.addMenuTopItem("退出后不会删除任何历史数据，下次登录依然可以使用此账号")
                    .addMenuBottomItem("退出登录");
        }
        mActionSheet.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mActionSheet.getWindow().getAttributes();
        lp.width = (display.getWidth()); //设置宽度
        mActionSheet.getWindow().setAttributes(lp);
        mActionSheet.setMenuBottomListener(new ActionSheet.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                AppNetUtil.LogOut(SettingActivity.this);
            }

            @Override
            public void onCancel() {
            }
        });
    }

    public void PersonInfo(View view) {
        Intent intent = new Intent(this, PersonalInfoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public void NewNotice(View view) {
        Intent intent = new Intent(this, NewNoticeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public void BindMailBox(View view) {
        if (bindEmail) {
            Intent intent = new Intent(this, ModifyMailBoxActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MailBoxBindActivity.class);
            startActivity(intent);
        }
    }

    public void Privacy(View view) {
        Intent intent = new Intent(this, PrivacyActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public void Currency(View view) {
        Intent intent = new Intent(this, CurrencyActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }


    public void VertifyPassWord(View view) {
        final DialogUtils.Dialog_WithEditText dialogUtils = new DialogUtils.Dialog_WithEditText(this);
        final Dialog alertDialog = dialogUtils.Title("验证原密码")
                .Message("为保障您的数据安全，修改密码前请填写\n原密码。")
                .Button("取消", "确定").MbuttonOnClick(new DialogUtils.Dialog_WithEditText.buttonOnClick() {
                    @Override
                    public void onButton1() {
                        dialogUtils.close();
                    }

                    @Override
                    public void onButton2() {
                        ProgressSubscriberOnNext<ResponseBody> next = new ProgressSubscriberOnNext<ResponseBody>() {
                            @Override
                            public void onNext(ResponseBody e) throws JSONException {
                                try {
                                    JSONObject jsonObject = new JSONObject(e.string());
                                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                                        case 200:
                                            Intent intent = new Intent(SettingActivity.this, ModifyPassWordActivity.class);
                                            intent.putExtra(IntentStatic.DATA, dialogUtils.getText());
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                            dialogUtils.close();
                                            break;
                                        case 9102:
                                            showToast("密码错误");
                                            break;
                                        default:
                                            showToast("验证失败");
                                    }
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        };

                        ProgressSubsciber<ResponseBody> progressSubsciber = new ProgressSubsciber<ResponseBody>(next, iBaseView);

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("phone", (String) SPUtils.get(SettingActivity.this, SPUtils.PHONENUMNBER, SPUtils.DEFAULT_STRING));
                        jsonObject.addProperty("password", dialogUtils.getText());
                        jsonObject.addProperty(NS.TIMESTAMP, System.currentTimeMillis());

                        InfoNetwork.getInstance().CheckPassword(progressSubsciber, jsonObject, SettingActivity.this);
                    }
                }).create();
        alertDialog.show();
    }

    public void about(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
