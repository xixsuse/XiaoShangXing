package com.xiaoshangxing.input_activity.Location;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.search.poi.PoiResult;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.normalUtils.KeyBoardUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/8/25
 */
public class SerchLocationActivity extends AppCompatActivity {
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.listview)
    ListView listview;

    public static final String LOCATION_BEAN = "LOCATION_BEAN";

    public List<AddressBean> dataList = new ArrayList<>();
    @Bind(R.id.edittext)
    EditText edittext;
    @Bind(R.id.root_view)
    LinearLayout rootView;
    private String city;
    private Location_adpter adpter;
    private String selected = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serch_location);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        edittext.setFocusable(true);
        edittext.setFocusableInTouchMode(true);
        edittext.requestFocus();
        KeyBoardUtils.openKeybord(edittext, this);
        city = getIntent().getStringExtra(LOCATION_BEAN);
        adpter = new Location_adpter(SerchLocationActivity.this, 1, dataList);
        listview.setAdapter(adpter);

        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                serch(edittext.getText().toString());
                if (!TextUtils.isEmpty(edittext.getText().toString())) {
                    listview.setVisibility(View.VISIBLE);
                } else {
                    listview.setVisibility(View.INVISIBLE);
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adpter.setSelected(position);
                selected = dataList.get(position).getName();
                finish();
            }
        });
    }

    private void serch(final String s) {
        Log.d("qqq", "PoiSearch...");
        BaiduMapUtilByRacer.getPoiByPoiSearch(city,
                s.trim(), 0,
                new BaiduMapUtilByRacer.PoiSearchListener() {

                    @Override
                    public void onGetSucceed(List<LocationBean> locationList,
                                             PoiResult res) {
                        if (s.trim().length() > 0) {
//                            for (int i = 0; i < locationList.size(); i++) {
//                                Log.d("www", locationList.get(i).getLocName() + "\n    " + locationList.get(i).getAddStr());
//                            }
                            dataList.clear();
                            for (int i = 0; i < locationList.size(); i++) {
                                AddressBean bean = new AddressBean(locationList.get(i).getLocName(), locationList.get(i).getAddStr());
                                if (bean.getName().equals(city)) {
                                    bean.setAddress("");
                                }
                                dataList.add(bean);
                            }
                            adpter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onGetFailed() {
//                        Toast.makeText(MainActivity.this, "抱歉，未能找到结果",
//                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick({R.id.cancel, R.id.root_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                selected = "null";
                finish();
                break;
            case R.id.root_view:
                finish();
                break;
        }
    }

    @Override
    public void finish() {
        if (!TextUtils.isEmpty(selected)) {
            Intent intent = new Intent();
            intent.putExtra(LocationActivity.SELECTED, selected);
            setResult(IntentStatic.CODE, intent);
        }
        KeyBoardUtils.closeKeybord(edittext, this);
        super.finish();
    }
}
