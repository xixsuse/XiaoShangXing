package com.xiaoshangxing.utils.location;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;


import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyang on 2016/8/24.
 */
public class PoiSearchUtil {
    public final static List<Bean> dataList = new ArrayList<>();
    private static LocationBean  mLocationBean;
    public static void LocationUtil(Context context) {
        Log.d("qqq", "LocationUtil...");
        BaiduMapUtilByRacer.locateByBaiduMap(context, 1000,
                new BaiduMapUtilByRacer.LocateListener() {
                    @Override
                    public void onLocateSucceed(LocationBean locationBean) {
                        Log.d("qqq", "onLocateSucceed...");
                        mLocationBean = locationBean;
                        double latitude = mLocationBean.getLatitude();
                        double longitude = mLocationBean.getLongitude();
                        Log.d("qqq", "mLocationBean   " + latitude + "," + longitude);
                        BaiduMapUtilByRacer.getPoisByGeoCode(latitude, longitude, new BaiduMapUtilByRacer.GeoCodePoiListener() {
                            @Override
                            public void onGetSucceed(LocationBean locationBean, List<PoiInfo> poiList) {
                                dataList.clear();
                                for (int i = 0; i < poiList.size(); i++) {
                                    Log.d("qqq", poiList.get(i).name + "\n    " + poiList.get(i).address);
                                    Bean bean = new Bean(poiList.get(i).name ,poiList.get(i).address);
                                    dataList.add(bean);

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
    }

    public static void PoiSearch(final String s){
        Log.d("qqq", "PoiSearch...");
        BaiduMapUtilByRacer.getPoiByPoiSearch(mLocationBean.getCity(),
                s.trim(), 0,
                new BaiduMapUtilByRacer.PoiSearchListener() {

                    @Override
                    public void onGetSucceed(List<LocationBean> locationList,
                                             PoiResult res) {
                        if (s.trim().length() > 0) {
                            for (int i = 0; i < locationList.size(); i++) {
                                Log.d("www", locationList.get(i).getLocName() + "\n    " + locationList.get(i).getAddStr());
                            }

                        }
                    }

                    @Override
                    public void onGetFailed() {
//                        Toast.makeText(MainActivity.this, "抱歉，未能找到结果",
//                                Toast.LENGTH_SHORT).show();
                    }
                });
    }





}
