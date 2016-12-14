package com.xiaoshangxing.publicActivity.Location;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/8/25
 */
public class LocationActivity extends BaseActivity {
    public static final String SELECTED = "SELECTED";
    public static final int LOCATION = 0x1;
    public List<AddressBean> dataList = new ArrayList<>();
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.myState)
    TextView myState;
    @Bind(R.id.title)
    RelativeLayout title;
    @Bind(R.id.serch_layout)
    RelativeLayout serchLayout;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.complete)
    TextView complete;
    private LocationBean mLocationBean;
    private Location_adpter adpter;
    private View head;
    private View head_gou;
    private String selected = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
        getPersimmions();
        initView();
    }

    private void initView() {

        if (getIntent().hasExtra(IntentStatic.DATA)) {
            String location = getIntent().getStringExtra(IntentStatic.DATA);
            if (location != null) {
                selected = location;
                complete.setAlpha(1);
                complete.setEnabled(true);
            }
        }

        BaiduMapUtilByRacer.locateByBaiduMap(this, 1000,
                new BaiduMapUtilByRacer.LocateListener() {
                    @Override
                    public void onLocateSucceed(LocationBean locationBean) {
                        mLocationBean = locationBean;
                        double latitude = mLocationBean.getLatitude();
                        double longitude = mLocationBean.getLongitude();
                        BaiduMapUtilByRacer.getPoisByGeoCode(latitude, longitude, new BaiduMapUtilByRacer.GeoCodePoiListener() {
                            @Override
                            public void onGetSucceed(LocationBean locationBean, List<PoiInfo> poiList) {
                                dataList.clear();
                                for (int i = 0; i < poiList.size(); i++) {
                                    AddressBean bean = new AddressBean(poiList.get(i).name, poiList.get(i).address);
                                    dataList.add(bean);
                                    adpter = new Location_adpter(LocationActivity.this, 1, dataList);
                                    if (selected != null && selected != "null") {
                                        adpter.setSelectedLocation(selected);
                                    }
                                    listview.setAdapter(adpter);
                                    Log.d("qqq", poiList.get(i).name + "\n    " + poiList.get(i).address);
                                }

                            }

                            @Override
                            public void onGetFailed() {
                                Log.d("qqq", "getfailed...");
                            }
                        });

                    }

                    @Override
                    public void onLocateFiled() {
                        Log.d("qqq", "onLocateFiled...");
                    }

                    @Override
                    public void onLocating() {
                        Log.d("qqq", "onLocating...");
                    }
                });

        head = View.inflate(this, R.layout.head_location, null);
        head_gou = head.findViewById(R.id.gou);
        listview.addHeaderView(head);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    head_gou.setVisibility(View.VISIBLE);
                    adpter.setSelected(-1);
                    selected = null;
                } else {
                    head_gou.setVisibility(View.INVISIBLE);
                    adpter.setSelected(position - 1);
                    selected = adpter.getItem(position - 1).getName();
                }
                complete.setAlpha(1);
                complete.setEnabled(true);
                finish();
            }
        });
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_SETTINGS);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
            }
            if (checkSelfPermission(Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CHANGE_WIFI_STATE);
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 127);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            finish();
        }
    }

    @OnClick({R.id.back, R.id.serch_layout, R.id.complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                selected = "null";
                finish();
                break;
            case R.id.serch_layout:
                Intent intent = new Intent(LocationActivity.this, SerchLocationActivity.class);
                intent.putExtra(SerchLocationActivity.LOCATION_BEAN, mLocationBean.getCity());
                startActivityForResult(intent, IntentStatic.SIMPLE_CODE);
                break;
            case R.id.complete:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentStatic.SIMPLE_CODE) {
            selected = data.getStringExtra(SELECTED);
            if (!selected.equals("null")) {
                finish();
            }
        }
    }

    @Override
    public void finish() {
        if (!TextUtils.isEmpty(selected) && !selected.equals("null")) {
            Intent intent = new Intent();
            intent.putExtra(LocationActivity.SELECTED, selected);
            setResult(RESULT_OK, intent);
        }
        super.finish();
    }

}
