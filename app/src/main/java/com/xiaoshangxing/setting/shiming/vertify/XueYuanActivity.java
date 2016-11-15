package com.xiaoshangxing.setting.shiming.vertify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IBaseView;

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
 * Created by tianyang on 2016/10/5.
 */
public class XueYuanActivity extends BaseActivity implements IBaseView {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_xueyuan);
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
            }
        });
        getSchool();
        if (VertifyActivity.colleg != null) {
            editText.setText(VertifyActivity.colleg);
        }
    }

    private void initListview() {
        mAdapter = new ArrayAdapter<String>(this, R.layout.item_nodisturb, strings);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editText.setText(mAdapter.getItem(position));
            }
        });
    }

    private void getSchool() {
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
                Log.d("collegeId", "" + VertifyActivity.collegeId);
                startActivity(new Intent(this, ZhuanYeActivity.class));
                break;
        }
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }
}
