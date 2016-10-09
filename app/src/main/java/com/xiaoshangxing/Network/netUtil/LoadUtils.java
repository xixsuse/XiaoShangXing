package com.xiaoshangxing.Network.netUtil;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.PublishNetwork;
import com.xiaoshangxing.data.CalendarData;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.utils.XSXApplication;
import com.xiaoshangxing.utils.normalUtils.SPUtils;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
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
    public static final String TIME_COLLECT_REWARD = "TIME_COLLECT_REWARD";             //校内悬赏收藏
    public static final String TIME_COLLECT_SALE = "TIME_COLLECT_SALE";                 //闲置出售收藏

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

    public static void tryLoadPublished(final Realm realm, String type, final String loadtime,
                                        final Context context, boolean isSelf, final AroundLoading aroundLoading) {
        if (needRefresh(loadtime)) {
            getPublished(realm, type, loadtime, context, isSelf, aroundLoading);
        }
    }

    /**
     * description:刷新动态
     *
     * @param realm         数据库
     * @param type          需要刷新的动态类型
     * @param loadtime      对应的刷新时间的类型
     * @param isSelf        是否刷新自己的动态 若否 则是刷新校友或好友的动态
     * @param aroundLoading 回调
     * @return
     */

    public static void getPublished(final Realm realm, String type, final String loadtime,
                                    final Context context, boolean isSelf, final AroundLoading aroundLoading) {

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
        if (isSelf) {
            PublishNetwork.getInstance().getPublished(subscriber1, jsonObject, XSXApplication.getInstance());
        } else {
            PublishNetwork.getInstance().getAllPublished(subscriber1, jsonObject, XSXApplication.getInstance());
        }
    }

    /**
     * description:刷新动态
     *
     * @param realm         数据库
     * @param type          需要刷新的动态类型
     * @param loadtime      对应的刷新时间的类型
     * @param aroundLoading 回调
     * @return
     */
    public static void getCollected(final Realm realm, String type, final String loadtime,
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
        PublishNetwork.getInstance().getCollect(subscriber1, jsonObject, XSXApplication.getInstance());
    }

    /**
     * description:获取指定人的指定动态
     *
     * @param realm         数据库
     * @param type          需要刷新的动态类型
     * @param peopleId      对象id
     * @param aroundLoading 回调
     * @return
     */

    public static void getOthersPublished(final Realm realm, String type, int peopleId,
                                          final Context context, final AroundLoading aroundLoading) {
        if (aroundLoading != null) {
            aroundLoading.before();
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, peopleId);
        jsonObject.addProperty(NS.CATEGORY, type);
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());

        Subscriber<ResponseBody> subscriber1 = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
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
                RealmResults<Published> publisheds = realm.where(Published.class).findAll();
                Log.d("saved_published", "--" + publisheds);
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
     * description:  清除数据库中指定动态
     *
     * @param category   指定清除数据的类型
     * @param onlyself   是否只清除用户自己的动态
     * @param remainsome 是否只保留若干动态
     * @return
     */

    public static void clearDatabase(String category, boolean onlyself, boolean remainsome) {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Published> realmResults;
        final List<Published> deleteItems;
        if (onlyself) {
            realmResults = realm.where(Published.class).equalTo(NS.CATEGORY, Integer.valueOf(category))
                    .equalTo(NS.ID, TempUser.id).findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        } else {
            realmResults = realm.where(Published.class).equalTo(NS.CATEGORY, Integer.valueOf(category))
                    .findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        }

        if (remainsome) {
            if (realmResults.size() > 20) {
                deleteItems = realmResults.subList(20, realmResults.size());
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (Published i : deleteItems) {
                            i.deleteFromRealm();
                        }
                        Log.d("clear data ", "success");
                    }
                });
            }
        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realmResults.deleteAllFromRealm();
                    Log.d("clear data ", "success");
                }
            });
        }

    }

    /**
     * description:清除收藏
     *
     * @param category 清除的类型
     * @return
     */

    public static void clearCollect(String category) {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Published> realmResults;

        if (category.equals(NS.COLLECT_REWARD)) {
            realmResults = realm.where(Published.class).equalTo(NS.CATEGORY, 6)
                    .findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        } else if (category.equals(NS.COLLECT_SALE)) {
            realmResults = realm.where(Published.class).equalTo(NS.CATEGORY, 5)
                    .findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        } else {
            return;
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmResults.deleteAllFromRealm();
                Log.d("clear data ", "success");
            }
        });
    }


    /**
     * description:获取校历
     *
     * @param year          年份
     * @param month         月份
     * @param realm         数据库
     * @param aroundLoading 回调
     * @return
     */

    public static void getCalendar(String year, String month, final Context context, final Realm realm,
                                   final AroundLoading aroundLoading) {

        if (aroundLoading != null) {
            aroundLoading.before();
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.YEAR, year);
        jsonObject.addProperty(NS.MONTH, month);
        jsonObject.addProperty(NS.CATEGORY, NS.CATEGORY_CALENDAR);

        Subscriber<ResponseBody> subscriber1 = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
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
                    LoadUtils.parseCalendarData(responseBody, realm, context, aroundLoading);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PublishNetwork.getInstance().getCalendar(subscriber1, jsonObject, XSXApplication.getInstance());
    }

    /**
     * description:  将返回校历数据存入数据库
     *
     * @param responseBody  返回体
     * @param realm         数据库
     * @param aroundLoading 回调
     * @return
     */

    public static void parseCalendarData(ResponseBody responseBody, Realm realm, Context context, AroundLoading aroundLoading) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject(responseBody.string());
        switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
            case 200:
                final JSONArray jsonArray = jsonObject.getJSONObject(NS.MSG).getJSONArray("moments");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.createOrUpdateAllFromJson(CalendarData.class, jsonArray);
                    }
                });
                RealmResults<CalendarData> publisheds = realm.where(CalendarData.class).findAll();
                Log.d("saved_calendar", "--" + publisheds);
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
     * description:获取校历发布者
     *
     * @param realm         数据库
     * @param aroundLoading 回调
     * @return
     */

    public static void getCalendarInputer(final Context context, final Realm realm, final AroundLoading aroundLoading) {

        if (aroundLoading != null) {
            aroundLoading.before();
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.CATEGORY, NS.CATEGORY_CALENDAR);

        Subscriber<ResponseBody> subscriber1 = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
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
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                        case 200:
                            final JSONArray jsonArray = jsonObject.getJSONObject(NS.MSG).getJSONArray("leaders");
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.createOrUpdateAllFromJson(User.class, jsonArray);
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
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PublishNetwork.getInstance().getCalendarInputer(subscriber1, jsonObject, XSXApplication.getInstance());
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
