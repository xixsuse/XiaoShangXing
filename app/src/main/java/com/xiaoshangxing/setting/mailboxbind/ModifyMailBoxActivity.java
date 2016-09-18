package com.xiaoshangxing.setting.mailboxbind;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import okhttp3.ResponseBody;

/**
 * Created by 15828 on 2016/7/15.
 */
public class ModifyMailBoxActivity extends BaseActivity implements IBaseView {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.modifymailbox_back)
    TextView modifymailboxBack;
    @Bind(R.id.bindmailbox_breakmaibox)
    Button bindmailboxBreakmaibox;
    @Bind(R.id.text)
    TextView text;

    private IBaseView iBaseView = this;

    private Realm realm;
    User user;

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_modifymailbox);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).equalTo("id",
                (Integer) SPUtils.get(ModifyMailBoxActivity.this,
                        SPUtils.ID, SPUtils.DEFAULT_int)).findFirst();
        if (user == null) {
            showToast("用户信息有误");
            finish();
        }
        String email = user.getEmail();
        text.setText(email);
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @OnClick({R.id.back, R.id.modifymailbox_back, R.id.bindmailbox_breakmaibox})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.modifymailbox_back:
                finish();
                break;
            case R.id.bindmailbox_breakmaibox:
                unBind();
                break;
        }
    }

    private void unBind() {
        final DialogUtils.Dialog_WithEditText dialogUtils = new DialogUtils.Dialog_WithEditText(this);
        final Dialog alertDialog = dialogUtils.Title("验证密码")
                .Message("为保障您的账号安全,解绑邮箱前请填写\n账号的登录密码.")
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
                                        case 201:
                                            showToast("邮箱解绑成功");
                                            realm.executeTransaction(new Realm.Transaction() {
                                                @Override
                                                public void execute(Realm realm) {
                                                    User user = realm.where(User.class).equalTo("id",
                                                            (Integer) SPUtils.get(ModifyMailBoxActivity.this,
                                                                    SPUtils.ID, SPUtils.DEFAULT_int)).findFirst();
                                                    if (user != null) {
                                                        user.setActiveStatus(0);
                                                    }
                                                }
                                            });
                                            dialogUtils.close();
                                            finish();
                                            break;
                                        case 8006:
                                            showToast("密码错误");
                                            break;
                                        case 8007:
                                            showToast("你还没有激活邮箱，请先激活");
                                            break;
                                        default:
                                            showToast(jsonObject.getString(NS.MSG));
                                    }
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        };

                        ProgressSubsciber<ResponseBody> progressSubsciber = new ProgressSubsciber<ResponseBody>(next, iBaseView);

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("userId", (int) SPUtils.get(ModifyMailBoxActivity.this, SPUtils.ID, SPUtils.DEFAULT_int));
                        jsonObject.addProperty("password", dialogUtils.getText());
                        jsonObject.addProperty(NS.TIMESTAMP, System.currentTimeMillis());
                        InfoNetwork.getInstance().unBindEmail(progressSubsciber, jsonObject, ModifyMailBoxActivity.this);
                    }
                }).create();
        alertDialog.show();
    }
}
