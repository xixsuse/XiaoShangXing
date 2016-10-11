package com.xiaoshangxing.yujian.Serch.SerchPerson;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoshangxing.Network.IMNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.LocationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * Created by FengChaoQun
 * on 2016/10/10
 */

public class SerchPersonActivity extends BaseActivity implements IBaseView {
    @Bind(R.id.input)
    EditText input;
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.serch_param)
    TextView serchParam;
    @Bind(R.id.serch)
    LinearLayout serch;
    @Bind(R.id.listview)
    ListView listview;

    private List<User> users = new ArrayList<>();
    private SerchPerson_Adpter adpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serch_person);
        ButterKnife.bind(this);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(input.getText().toString())) {
                    serchParam.setText("手机号/名字");
                } else {
                    serchParam.setText(input.getText().toString());
                }
            }
        });

    }

    @OnClick({R.id.cancel, R.id.serch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.serch:
                serch();
                break;
        }
    }

    private void refreshListview() {
        if (users.size() < 1) {
            showNoFound();
            listview.setVisibility(View.GONE);
        }
        listview.setVisibility(View.VISIBLE);
        adpter = new SerchPerson_Adpter(this, 1, users);
        listview.setAdapter(adpter);
    }

    private void showNoFound() {
        final DialogUtils.Dialog_Center dialogUtils = new DialogUtils.Dialog_Center(this);
        final Dialog alertDialog = dialogUtils.Message("无法找到该用户,\n请检查是否输入正确。").Title("该用户不存在")
                .Button("确定").MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
                    @Override
                    public void onButton1() {
                        dialogUtils.close();
                    }

                    @Override
                    public void onButton2() {

                    }
                }).create();
        alertDialog.show();
        LocationUtil.setWidth(this, alertDialog,
                getResources().getDimensionPixelSize(R.dimen.x780));
    }

    private void serch() {

        if (TextUtils.isEmpty(input.getText().toString())) {
            showToast("请输入查询的信息");
            return;
        }

        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case 200:
                            Gson gson = new Gson();
                            users = gson.fromJson(jsonObject.getJSONArray(NS.MSG).toString(), new TypeToken<List<User>>() {
                            }.getType());
                            refreshListview();
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

        ProgressSubsciber<ResponseBody> progressSubsciber = new ProgressSubsciber<>(onNext, this);
        IMNetwork.getInstance().SerchPerson(progressSubsciber, input.getText().toString(), this);
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }
}
