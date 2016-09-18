package com.xiaoshangxing.Network.netUtil;

import android.util.Log;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.PublishNetwork;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.XSXApplication;
import com.xiaoshangxing.utils.normalUtils.SPUtils;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by FengChaoQun
 * on 2016/9/12
 */
public class LoadUtils {

    public static final String TIME_LOAD_STATE = "TIME_LOAD_STATE";
    public static final String TIME_LOAD_SELFSTATE = "TIME_LOAD_SELFSTATE";
    public static final String TIME_LOAD_HELP = "TIME_LOAD_HELP";
    public static final String TIME_LOAD_SELFHELP = "TIME_LOAD_SELFHELP";

    /*
    **describe:判断是否需要刷新
    */
    public static boolean needRefresh(String type) {
        return (NS.currentTime() - (long) SPUtils.get(XSXApplication.getInstance(), type,
                SPUtils.DEFAULT_LONG) > 5 * TimeUtil.MINUTE);
    }

    /*
    **describe:记录刷新时间
    */
    public static void refreshTime(String type) {
        SPUtils.put(XSXApplication.getInstance(), type, NS.currentTime());
    }

    /*
    **describe:请求刷新自己的动态
    */
    public static void getSelfState(final PtrFrameLayout frame, final IBaseView mView,
                                    final Realm realm, String type, final AroundLoading aroundLoading) {
        if (aroundLoading != null) {
            aroundLoading.before();
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, TempUser.id);
        jsonObject.addProperty(NS.CATEGORY, type);
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());

        Subscriber<ResponseBody> subscriber1 = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                frame.refreshComplete();
                mView.showToast("更新信息成功");
                LoadUtils.refreshTime(LoadUtils.TIME_LOAD_SELFHELP);
                if (aroundLoading != null) {
                    aroundLoading.complete();
                }
            }

            @Override
            public void onError(Throwable e) {
                frame.refreshComplete();
                mView.showToast("更新信息失败");
                e.printStackTrace();
                if (aroundLoading != null) {
                    aroundLoading.error();
                }
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    LoadUtils.parseData(responseBody, realm, mView);
                    RealmResults<Published> publisheds = realm.where(Published.class).findAll();
                    Log.d("saved_published", "--" + publisheds);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        PublishNetwork.getInstance().getPublished(subscriber1, jsonObject, XSXApplication.getInstance());
    }

    /*
    **describe:将数据存入数据库
    */
    public static void parseData(ResponseBody responseBody, Realm realm, IBaseView iBaseView) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject(responseBody.string());
        switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
            case 200:
                final JSONArray jsonArray = jsonObject.getJSONObject(NS.MSG).getJSONArray("moments");

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.createOrUpdateAllFromJson(Published.class, jsonArray);
                    }
                });
                break;
            default:
                iBaseView.showToast(jsonObject.getString(NS.MSG));
                break;
        }
    }

    public interface AroundLoading {
        void before();

        void complete();

        void error();
    }


}
