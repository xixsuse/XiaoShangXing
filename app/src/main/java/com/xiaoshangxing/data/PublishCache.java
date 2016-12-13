package com.xiaoshangxing.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.PublishNetwork;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.XSXApplication;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by FengChaoQun
 * on 2016/9/12
 * 动态信息缓存加载处理类
 */
public class PublishCache {

    /**
     * description:获取指定动态内容 先从数据库中获取 如果需要刷新 则刷新
     *
     * @param id       动态id
     * @param callback 回调
     */

    public static void getPublished(String id, SimpleCallBack callback) {
        Published published;
        Realm realm = Realm.getDefaultInstance();
        published = realm.where(Published.class).equalTo(NS.ID, Integer.valueOf(id)).findFirst();
        if (needReload(published)) {
            reload(id, callback);
        } else {
            Published published1 = DataCopy.copyPublished(published);
            callback.onSuccess();
            callback.onBackData(published1);
        }
        realm.close();
    }


    /*
    **describe:判断是否需要刷新数据
    */
    private static boolean needReload(Published published) {
        if (published == null) {
            return true;
        } else if (NS.currentTime() - published.getServerTime() > 5 * TimeUtil.MINUTE) {
            return true;
        }
        return false;
    }


    /*
    **describe:刷新数据
    */
    public static void reload(final String id, final SimpleCallBack callback) {
        Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                callback.onError(e);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                        case 50000001:
                            final JSONObject published = jsonObject.getJSONObject(NS.MSG);
                            Realm realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.createOrUpdateObjectFromJson(Published.class, published);
                                }
                            });
                            realm.close();

                            Gson gson = new Gson();
                            Published i = gson.fromJson(published.toString(), Published.class);
                            if (callback != null) {
                                callback.onSuccess();
                                callback.onBackData(i);
                            }
                            break;
                        default:
                            Log.d("加载动态信息", "失败");
                            callback.onError(null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError(e);
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onError(e);
                }
            }
        };
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.MOMENTID, id);
        PublishNetwork.getInstance().refreshPublished(subscriber, jsonObject, XSXApplication.getInstance());
    }

    public static void reload(final String id) {
        reload(id, new SimpleCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onBackData(Object o) {
                Log.d("refresh publish", id + "ok");
            }
        });
    }

    public static void reloadWithLoading(final String id, final IBaseView iBaseView, final SimpleCallBack callback) {
        iBaseView.showLoadingDialog("加载数据中...");
        reload(id, new SimpleCallBack() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
                iBaseView.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                callback.onError(e);
                iBaseView.hideLoadingDialog();
            }

            @Override
            public void onBackData(Object o) {
                callback.onBackData(o);
                iBaseView.hideLoadingDialog();
            }
        });
    }
}
