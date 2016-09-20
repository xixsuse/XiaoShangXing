package com.xiaoshangxing.Network.netUtil;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.PublishNetwork;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.utils.XSXApplication;
import com.xiaoshangxing.utils.normalUtils.SPUtils;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by FengChaoQun
 * on 2016/9/12
 * 加载动态消息集中处理类
 */
public class LoadUtils {

    /*
    **describe:记录刷新的时间  目前策略 五分钟之内不需刷新
    */
    public static final String TIME_LOAD_STATE = "TIME_LOAD_STATE";                     //好友动态
    public static final String TIME_LOAD_SELFSTATE = "TIME_LOAD_SELFSTATE";             //我的动态
    public static final String TIME_LOAD_HELP = "TIME_LOAD_HELP";                       //好友互帮
    public static final String TIME_LOAD_SELFHELP = "TIME_LOAD_SELFHELP";               //我的互帮
    public static final String TIME_LOAD_REWARD = "TIME_LOAD_REWARD";                   //校内悬赏
    public static final String TIME_LOAD_SELFREWARD = "TIME_LOAD_SELFREWARD";           //我的悬赏
    public static final String TIME_LOAD_PLAN = "TIME_LOAD_PLAN";                       //计划发起
    public static final String TIME_LOAD_SELFPLAN = "TIME_LOAD_SELFPLAN";               //我的计划
    public static final String TIME_LOAD_SALE = "TIME_LOAD_SALE";                       //闲置出售
    public static final String TIME_LOAD_SELFSALE = "TIME_LOAD_SELFSALE";               //我的闲置
    public static final String TIME_LOAD_CALENDAR = "TIME_LOAD_CALENDAR";               //校历资讯

    /**
     * description:判断是否需要刷新
     *
     * @param loadtime 类型
     * @return true表示需要刷新
     */

    public static boolean needRefresh(String loadtime) {
        return (NS.currentTime() - (long) SPUtils.get(XSXApplication.getInstance(), loadtime,
                SPUtils.DEFAULT_LONG) > 5 * TimeUtil.MINUTE);
    }

    /**
     * description:记录刷新时间
     *
     * @param loadtime 类型
     * @return
     */

    public static void refreshTime(String loadtime) {
        SPUtils.put(XSXApplication.getInstance(), loadtime, NS.currentTime());
    }

    /**
     * description:尝试刷新  若不需刷新 则不刷新
     *
     * @param realm         数据库
     * @param type          动态类型
     * @param loadtime      动态刷新时间类型
     * @param aroundLoading 回调
     * @return
     */

    public static void tryLoadSelfState(final Realm realm, String type, final String loadtime,
                                        final Context context, final AroundLoading aroundLoading) {
        if (needRefresh(loadtime)) {
            getSelfState(realm, type, loadtime, context, aroundLoading);
        }
    }

    /**
     * description:刷新自己的动态
     *
     * @param realm         数据库
     * @param type          需要刷新的动态类型
     * @param loadtime      对应的刷新时间的类型
     * @param aroundLoading 回调
     * @return
     */

    public static void getSelfState(final Realm realm, String type, final String loadtime,
                                    final Context context, final AroundLoading aroundLoading) {
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
                LoadUtils.refreshTime(loadtime);
                if (aroundLoading != null) {
                    aroundLoading.complete();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (aroundLoading != null) {
                    aroundLoading.error();
                }
                Toast.makeText(context, NS.REFRESH_FAIL, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    LoadUtils.parseData(responseBody, realm, context, aroundLoading);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        PublishNetwork.getInstance().getPublished(subscriber1, jsonObject, XSXApplication.getInstance());
    }

    /**
     * description:  将返回数据存入数据库
     *
     * @param responseBody  返回体
     * @param realm         数据库
     * @param aroundLoading 回调
     * @return
     */

    public static void parseData(ResponseBody responseBody, Realm realm, Context context, AroundLoading aroundLoading) throws IOException, JSONException {
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
                if (aroundLoading != null) {
                    aroundLoading.onSuccess();
                }
                Toast.makeText(context, NS.REFRESH_SUCCESS, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, jsonObject.getString(NS.MSG), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * description: 回调
     */

    public interface AroundLoading {
        void before();

        void complete();

        void onSuccess();

        void error();

    }
}
