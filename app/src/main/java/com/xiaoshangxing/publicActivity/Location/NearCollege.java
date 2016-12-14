package com.xiaoshangxing.publicActivity.Location;

import android.content.Context;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;

import java.util.HashSet;

/**
 * Created by FengChaoQun
 * on 2016/10/6
 */

public class NearCollege {
    private PoiSearch poiSearch;
    private LocationBean mlocationBean;
    private HashSet<String> shcools = new HashSet<>();
    private OnGetPoiSearchResultListener poiListener;
    private Context context;
    private NearShoolCallback nearShoolCallback;

    public NearCollege(Context context, NearShoolCallback nearShoolCallback) {

        this.context = context;
        this.nearShoolCallback = nearShoolCallback;

        poiSearch = PoiSearch.newInstance();

        poiListener = new OnGetPoiSearchResultListener() {
            public void onGetPoiResult(PoiResult result) {
                getResult(result);
            }

            public void onGetPoiDetailResult(PoiDetailResult result) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        };
        poiSearch.setOnGetPoiSearchResultListener(poiListener);
    }

    private void getResult(PoiResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            nearShoolCallback.Erro();
        } else {
            //检索成功
            for (PoiInfo o : result.getAllPoi()) {
                String name = o.name;
                Log.d("--", o.name);

                if (name.contains("大学") && name.contains("学院")) {
                    continue;
                }

                String names[] = name.split("\\(");
                if (names[0].endsWith("大学") || names[0].endsWith("校区)") || names[0].endsWith("学院")) {
                    if (names[0].length() < 15) {
                        shcools.add(names[0]);
                        continue;
                    }
                }

                String namess[] = name.split("-");
                if (namess[0].endsWith("大学") || namess[0].endsWith("校区)") || namess[0].endsWith("学院")) {
                    if (namess[0].length() < 15) {
                        shcools.add(namess[0]);
                    }
                }

            }

            if (result.getCurrentPageNum() < result.getTotalPageNum() - 1) {
                serch(result.getCurrentPageNum() + 1, 5000);
            } else {
                nearShoolCallback.Success(shcools);
            }

        }
    }

    public void start() {
        nearShoolCallback.Star();
        serch(0, 5000);
    }

    private void serch(final int pager, final int reaius) {
        if (mlocationBean == null) {
            BaiduMapUtilByRacer.locateByBaiduMap(context, 1000, new BaiduMapUtilByRacer.LocateListener() {
                @Override
                public void onLocateSucceed(LocationBean locationBean) {
                    mlocationBean = locationBean;
                    poiSearch.searchNearby(
                            new PoiNearbySearchOption()
                                    .location(new LatLng(locationBean.getLatitude(), locationBean.getLongitude()))
                                    .keyword("学校").radius(reaius).pageCapacity(50)
                                    .sortType(PoiSortType.distance_from_near_to_far)
                                    .pageNum(pager)
                    );
                }

                @Override
                public void onLocateFiled() {
                    nearShoolCallback.Erro();
                }

                @Override
                public void onLocating() {

                }
            });
        } else {
            poiSearch.searchNearby(
                    new PoiNearbySearchOption()
                            .location(new LatLng(mlocationBean.getLatitude(), mlocationBean.getLongitude()))
                            .keyword("学校").radius(reaius).pageCapacity(50)
                            .sortType(PoiSortType.distance_from_near_to_far)
                            .pageNum(pager)
            );
        }
    }

    public interface NearShoolCallback {
        void Star();

        void Erro();

        void Success(HashSet<String> hashSet);
    }

}


