package com.xiaoshangxing.setting.shiming.chooseschool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.xiaoshangxing.MainActivity;
import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.shiming.vertify.VertifyActivity;
import com.xiaoshangxing.setting.shiming.vertify.XueYuanActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.IntentStatic;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * Created by tianyang on 2016/10/5.
 */
public class SerchSchoolActivity extends BaseActivity implements IBaseView {
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
    @Bind(R.id.notice)
    TextView notice;
    private ListView mListView;
    private String[] strings;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_xueyuan);
        ButterKnife.bind(this);

        title.setText("学校");
        next.setText("下一步");
        next.setTextColor(getResources().getColor(R.color.green1));
        next.setAlpha(0.5f);
        next.setEnabled(false);
        editText.setHint("请输入你的学校");
        notice.setText("已有学校");

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
                onEdittextChange(s.toString());
            }
        });
        getSchool();
    }

    private void onEdittextChange(String s) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            next.setEnabled(false);
            next.setAlpha(0.5f);
        } else {
            next.setEnabled(true);
            next.setAlpha(1);
        }
        if (strings != null) {
            ArrayList<String> arrayList = new ArrayList<>();
            for (String i : strings) {
                if (i.contains(s)) {
                    arrayList.add(i);
                }
            }
            initListview(arrayList);
        }
    }

    private void initListview(final List<String> arrayList) {
        if (arrayList == null) {
            return;
        }
        mAdapter = new ArrayAdapter<String>(this, R.layout.item_nodisturb, arrayList);
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
                            strings = jsonObject.getString(NS.MSG).split(NS.SPLIT);
                            List<String> arrayList = Arrays.asList(strings);
                            initListview(arrayList);
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
        InfoNetwork.getInstance().getSchool(subsciber, this);
    }

    @OnClick({R.id.back, R.id.right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right_text:
                if (getIntent().getBooleanExtra(IntentStatic.TYPE, false)) {
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    startActivity(new Intent(this, XueYuanActivity.class));
                    VertifyActivity.schoolStr = editText.getText().toString();
                }
                break;
        }
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }
}
