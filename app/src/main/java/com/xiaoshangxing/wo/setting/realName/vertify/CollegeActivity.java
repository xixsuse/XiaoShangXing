package com.xiaoshangxing.wo.setting.realName.vertify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.xiaoshangxing.network.InfoNetwork;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.utils.baseClass.IBaseView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 *modified by FengChaoQun on 2016/12/24 16:31
 * description:优化代码
 */
public class CollegeActivity extends BaseActivity implements IBaseView {
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_text)
    TextView next;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.edittext)
    EditText editText;
    @Bind(R.id.list)
    ListView list;
    private ListView mListView;
    private List<String> strings = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    private HashMap<String, String> hashMap = new HashMap<>();
    private CheckTextviewAdpter adpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serch_school);
        ButterKnife.bind(this);

        title.setText("学院");
        next.setText("下一步");
        next.setTextColor(getResources().getColor(R.color.green1));
        next.setAlpha(0.5f);
        next.setEnabled(false);

        mListView = (ListView) findViewById(R.id.list);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    next.setEnabled(false);
                    next.setAlpha(0.5f);
                } else {
                    next.setEnabled(true);
                    next.setAlpha(1);
                }
                refreshChecked(editText.getText().toString());
            }
        });
        getCollege();
        if (VertifyActivity.colleg != null) {
            editText.setText(VertifyActivity.colleg);
        }
    }

    private void initListview() {
        adpter = new CheckTextviewAdpter(this, R.layout.item_nodisturb, strings);
        mListView.setAdapter(adpter);
        adpter.setCallback(new CheckTextviewAdpter.Callback() {
            @Override
            public void callback(String string) {
                editText.setText(string);
            }
        });
    }

    private void refreshChecked(String string) {
        if (adpter == null) {
            return;
        }
        if (strings.contains(string)) {
            adpter.setCheckedPosition(strings.indexOf(string));
        } else {
            adpter.setCheckedPosition(-1);
        }
    }

    private void getCollege() {
        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case NS.CODE_200:
                            String[] temp = jsonObject.getString(NS.MSG).split(NS.SPLIT);
                            for (String i : temp) {
                                String[] temp2 = i.split(NS.SPLIT2);
                                strings.add(temp2[1]);
                                hashMap.put(temp2[1], temp2[0]);
                            }
                            initListview();
                            break;
                        case 403:
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

        ProgressSubsciber<ResponseBody> subsciber = new ProgressSubsciber<>(onNext, this);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("schoolName", VertifyActivity.schoolStr);
        InfoNetwork.getInstance().getCollge(subsciber, jsonObject, this);
    }

    @OnClick({R.id.back, R.id.right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right_text:
                VertifyActivity.colleg = editText.getText().toString();
                if (hashMap.containsKey(VertifyActivity.colleg)) {
                    VertifyActivity.collegeId = hashMap.get(VertifyActivity.colleg);
                } else {
                    VertifyActivity.collegeId = null;
                }
                startActivity(new Intent(this, MajorActivity.class));
                break;
        }
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }
}
