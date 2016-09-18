package com.xiaoshangxing.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.PublishNetwork;
import com.xiaoshangxing.Network.netUtil.NS;
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
 */
public class PublishCache {

    public static void getPublished(String id, publishedCallback callback/*, Realm realm*/) {
        Published published;
        Realm realm = Realm.getDefaultInstance();
        published = realm.where(Published.class).equalTo(NS.ID, Integer.valueOf(id)).findFirst();
        if (needReload(published)) {
            reload(id, callback);
        } else {
            Published published1 = DataCopy.copyPublished(published);
            callback.callback(published1);
        }
        realm.close();
    }

    /*
    **describe:刷新指定数据
    */
    public void refreshSomeone(User user) {

    }

    /*
    **describe:尝试加载数据加载数据
    */
//    private void try_reloadData(User user) {
//        if (needReload(user)) {
//            reload(null, user.getId());
//        }
//    }

    /*
    **describe:判断是否需要刷新数据
    */
    public static boolean needReload(Published published) {
        if (published == null) {
            return true;
        } else if (NS.currentTime() - published.getServerTime() > 10 * TimeUtil.MINUTE) {
            return true;
        }
        return false;
    }

    /*
    **describe:刷新数据
    */
    public static void reload(final String id, final publishedCallback callback) {
        Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
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
                                callback.callback(i);
                            }
                            break;
                        default:
                            Log.d("加载动态信息", "失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.MOMENTID, id);
        PublishNetwork.getInstance().refreshPublished(subscriber, jsonObject, XSXApplication.getInstance());
    }

    /**
     * ************************************ 单例 **********************************************
     */

    static class InstanceHolder {
        final static PublishCache instance = new PublishCache();
    }

    public interface publishedCallback {
        void callback(Published published);
    }

}
