package com.xiaoshangxing.setting.personalinfo.QianMing;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.utils.AppContracts;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import org.json.JSONObject;

import io.realm.Realm;
import okhttp3.ResponseBody;

/**
 * Created by 15828 on 2016/7/12.
 */
public class QianMingFragment extends BaseFragment implements IBaseView {
    private View mView;
    private EditText editText;
    private TextView count, save, back;
    private Realm realm;
    private User user;

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_personalinfo_qianming, container, false);
        editText = (EditText) mView.findViewById(R.id.qianming_editText);
        count = (TextView) mView.findViewById(R.id.qianming_count);
        save = (TextView) mView.findViewById(R.id.qianming_save);
        back = (TextView) mView.findViewById(R.id.qianming_back);
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).equalTo(NS.ID, TempUser.getID(getContext())).findFirst();
        if (user == null) {
            showToast(AppContracts.NO_USER);
            getActivity().getSupportFragmentManager().popBackStack();
        }

        editText.setText(user.getSignature());
        int a = (int) calculateLength(editText.getText());
        int num = 30 - a;
        count.setText(String.valueOf(num));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mView, InputMethodManager.SHOW_FORCED);
                imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    save.setAlpha(1);
                    save.setClickable(true);
                } else {
                    save.setAlpha((float) 0.5);
                    save.setClickable(false);
                }
                int a = (int) calculateLength(s);
                int num = 30 - a;
                count.setText(String.valueOf(num));
                if (num < 0) {
                    count.setTextColor(getResources().getColor(R.color.red1));
                    save.setAlpha((float) 0.5);
                    save.setClickable(false);
                }
                else {
                    count.setTextColor(getResources().getColor(R.color.g0));
                }

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeInfo(editText.getText().toString());
            }
        });
        return mView;
    }

    private void ChangeInfo(String text) {
        ProgressSubscriberOnNext<ResponseBody> next = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    if (jsonObject.getString(NS.CODE).equals("200")) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        showToast("修改失败");
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        };

        ProgressSubsciber<ResponseBody> progressSubsciber = new ProgressSubsciber<>(next, this);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", (Integer) SPUtils.get(getContext(), SPUtils.ID, SPUtils.DEFAULT_int));
        jsonObject.addProperty("signature", text);
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());

        InfoNetwork.getInstance().ModifyInfo(progressSubsciber, jsonObject, getContext());
    }


    public long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int temp = (int) c.charAt(i);
            if (temp >= 0 && temp <= 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return (long) len;
    }

}
