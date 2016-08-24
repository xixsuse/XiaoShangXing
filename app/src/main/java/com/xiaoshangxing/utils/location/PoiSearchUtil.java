package com.xiaoshangxing.utils.location;

import android.content.Context;
import android.util.Log;


import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.PoiInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyang on 2016/8/24.
 */
public class PoiSearchUtil {
    public final static List<Bean> dataList = new ArrayList<>();
    public static void LocationUtil(Context context) {
        BaiduMapUtilByRacer.locateByBaiduMap(context, 1000,
                new BaiduMapUtilByRacer.LocateListener() {
                    @Override
                    public void onLocateSucceed(LocationBean locationBean) {
                        Log.d("qqq", "onLocateSucceed...");
                        LocationBean  mLocationBean = locationBean;
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


}
