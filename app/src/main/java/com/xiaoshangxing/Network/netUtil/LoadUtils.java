package com.xiaoshangxing.network.netUtil;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.xiaoshangxing.network.PublishNetwork;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.bean.CalendarData;
import com.xiaoshangxing.data.bean.JoinedPlan;
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.data.bean.User;
import com.xiaoshangxing.utils.AppContracts;
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
    public static final String TIME_JOINED_PLAN = "TIME_JOINED_PLAN";                   //加入的计划
    private static final String COLLECT = "COLLECT"; //收藏
    private static final String PUBLISHED = "PUBLISHED"; //普通动态
    private static final String JOINED_PLAN = "JOINED_PLAN"; //加入的计划

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

    public static boolean needRefresh(String loadtime, Realm realm) {
        return (NS.currentTime() - (long) SPUtils.get(XSXApplication.getInstance(), loadtime,
                SPUtils.DEFAULT_LONG) > 5 * TimeUtil.MINUTE || quryRealm(loadtime, realm));
    }

    private static boolean quryRealm(String loadtime, Realm realm) {
        boolean isNeedRefresh = false;
        switch (loadtime) {
            case TIME_LOAD_STATE:
                isNeedRefresh = realm.where(Published.class)
                        .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_STATE))
                        .findAll().sort(NS.CREATETIME, Sort.DESCENDING).isEmpty();
                break;
            case TIME_LOAD_REWARD:
                isNeedRefresh = realm.where(Published.class)
                        .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_REWARD))
                        .findAll().sort(NS.CREATETIME, Sort.DESCENDING).isEmpty();
                break;
            case TIME_LOAD_HELP:
                isNeedRefresh = realm.where(Published.class)
                        .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_HELP))
                        .findAll().sort(NS.CREATETIME, Sort.DESCENDING).isEmpty();
                break;
            case TIME_LOAD_PLAN:
                isNeedRefresh = realm.where(Published.class)
                        .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_PLAN))
                        .findAll().sort(NS.CREATETIME, Sort.DESCENDING).isEmpty();
                break;
            case TIME_LOAD_SALE:
                isNeedRefresh = realm.where(Published.class)
                        .equalTo(NS.CATEGORY, Integer.valueOf(NS.CATEGORY_SALE))
                        .findAll().sort(NS.CREATETIME, Sort.DESCENDING).isEmpty();
                break;
        }
        return isNeedRefresh;
    }

    /**
     * description:记录刷新时间
     *
     * @param loadtime 类型
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
     */

    public static void tryLoadPublished(final Realm realm, String type, final String loadtime,
                                        final Context context, boolean isSelf, final AroundLoading aroundLoading) {
        if (needRefresh(loadtime)) {
            getPublished(realm, type, loadtime, context, isSelf, aroundLoading);
        }
    }

    /**
     * description:刷新动态 校友或自己的动态
     *
     * @param realm         数据库
     * @param type          需要刷新的动态类型
     * @param loadtime      对应的刷新时间的类型
     * @param isSelf        是否刷新自己的动态 若否 则是刷新校友或好友的动态
     * @param aroundLoading 回调
     */

    public static void getPublished(final Realm realm, final String type, final String loadtime,
                                    final Context context, final boolean isSelf, final AroundLoading aroundLoading) {

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
                Toast.makeText(context, AppContracts.REFRESH_FAIL, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    LoadUtils.parseData(responseBody, realm, context, PUBLISHED, type, isSelf ? TempUser.getId() : null, aroundLoading);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        if (isSelf) {
            PublishNetwork.getInstance().getPersonalPublished(subscriber1, jsonObject, XSXApplication.getInstance());
        } else {
            PublishNetwork.getInstance().getAllPublished(subscriber1, jsonObject, XSXApplication.getInstance());
        }
    }

    /**
     * description:获取指定人的指定动态
     *
     * @param realm         数据库
     * @param type          需要刷新的动态类型
     * @param peopleId      对象id
     * @param aroundLoading 回调
     */

    public static void getPersonalPublished(final Realm realm, final String type, final int peopleId,
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
                Toast.makeText(context, AppContracts.REFRESH_FAIL, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    LoadUtils.parseData(responseBody, realm, context, PUBLISHED, type, String.valueOf(peopleId), aroundLoading);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PublishNetwork.getInstance().getPersonalPublished(subscriber1, jsonObject, XSXApplication.getInstance());
    }

    /**
     * description:获取收藏
     *
     * @param realm         数据库
     * @param type          需要刷新的收藏类型
     * @param loadtime      对应的刷新时间的类型
     * @param aroundLoading 回调
     */
    public static void getCollected(final Realm realm, final String type, final String loadtime,
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
                Toast.makeText(context, AppContracts.REFRESH_FAIL, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    LoadUtils.parseData(responseBody, realm, context, COLLECT, type, TempUser.getId(), aroundLoading);
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
     * description:获取加入的计划
     *
     * @param userid        用户id
     * @param realm         数据库
     * @param aroundLoading 回调
     */

    public static void getJoinedPlan(String userid, final Realm realm, final Context context,
                                     final AroundLoading aroundLoading) {
        if (aroundLoading != null) {
            aroundLoading.before();
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, userid);

        Subscriber<ResponseBody> subscriber1 = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                LoadUtils.refreshTime(TIME_JOINED_PLAN);
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
                Toast.makeText(context, AppContracts.REFRESH_FAIL, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                        case 200:

                            clearData(JOINED_PLAN, null, null, false);

                            final JSONArray jsonArray = jsonObject.getJSONObject(NS.MSG).getJSONArray("moments");
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.createOrUpdateAllFromJson(JoinedPlan.class, jsonArray);
                                }
                            });
                            if (aroundLoading != null) {
                                aroundLoading.onSuccess();
                            }
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
        PublishNetwork.getInstance().getJoinedPlan(subscriber1, jsonObject, XSXApplication.getInstance());
    }

    /**
     * description:  将返回的动态数据存入数据库
     *
     * @param responseBody  返回体
     * @param realm         数据库
     * @param aroundLoading 回调
     */

    public static void parseData(ResponseBody responseBody, Realm realm, Context context, String dataType, String type,
                                 String personId, AroundLoading aroundLoading) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject(responseBody.string());
        switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
            case 200:

                clearData(dataType, type, personId, false);

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
                break;
            default:
                Toast.makeText(context, jsonObject.getString(NS.MSG), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * description:获取校历
     *
     * @param year          年份
     * @param month         月份
     * @param realm         数据库
     * @param aroundLoading 回调
     */

    public static void getCalendar(final String year, final String month, final Context context, final Realm realm,
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
                Toast.makeText(context, AppContracts.REFRESH_FAIL, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    LoadUtils.parseCalendarData(responseBody, realm, context, year, month, aroundLoading);
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
     */

    private static void parseCalendarData(ResponseBody responseBody, Realm realm, Context context,
                                          String year, String month, AroundLoading aroundLoading) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject(responseBody.string());
        switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
            case 200:

                ClearCalendar(year, month);

                final JSONArray jsonArray = jsonObject.getJSONObject(NS.MSG).getJSONArray("moments");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.createOrUpdateAllFromJson(CalendarData.class, jsonArray);
                    }
                });
                if (aroundLoading != null) {
                    aroundLoading.onSuccess();
                }
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
                Toast.makeText(context, AppContracts.REFRESH_FAIL, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                        case 200:
                            ClearInputer();
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
     * description:清除本地数据库中指定数据
     *
     * @param dataType   数据库类型
     * @param category   数据类型
     * @param personId   指定人id
     * @param remainsome 是否需要保存前十条
     */

    private static void clearData(String dataType, final String category, String personId, boolean remainsome) {
        switch (dataType) {
            case COLLECT:
                clearCollect(category);
                break;
            case PUBLISHED:
                clearDatabase(category, personId, remainsome);
                break;
            case JOINED_PLAN:
                clearJoinPlan();
                break;
        }
    }

    /**
     * description:  清除数据库中指定发布的动态
     *
     * @param category   指定清除数据的类型
     * @param personId   清除指定人的id
     * @param remainsome 是否保留若干动态  保留前十条动态
     */

    private static void clearDatabase(final String category, String personId, boolean remainsome) {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Published> realmResults;
        final List<Published> deleteItems;
        if (!TextUtils.isEmpty(personId)) {
            realmResults = realm.where(Published.class).equalTo(NS.CATEGORY, Integer.valueOf(category))
                    .equalTo(NS.ID, Integer.valueOf(personId)).findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        } else {
            realmResults = realm.where(Published.class).equalTo(NS.CATEGORY, Integer.valueOf(category))
                    .findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        }

        if (remainsome) {
            if (realmResults.size() > 10) {
                deleteItems = realmResults.subList(10, realmResults.size());
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (Published i : deleteItems) {
                            i.deleteFromRealm();
                        }
                        Log.d("clear published data " + category, "success");
                    }
                });
            }
        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realmResults.deleteAllFromRealm();
                    Log.d("clear published data " + category, "success");
                }
            });
        }

    }

    /**
     * description:清除收藏
     *
     * @param category 清除的类型
     */

    private static void clearCollect(final String category) {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Published> realmResults;

        if (category.equals(NS.COLLECT_REWARD)) {
            realmResults = realm.where(Published.class).equalTo(NS.CATEGORY, 6)
                    .equalTo(NS.COLLECT_STATU, "1")
                    .findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        } else if (category.equals(NS.COLLECT_SALE)) {
            realmResults = realm.where(Published.class).equalTo(NS.CATEGORY, 5)
                    .equalTo(NS.COLLECT_STATU, "1")
                    .findAllSorted(NS.CREATETIME, Sort.DESCENDING);
        } else {
            return;
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmResults.deleteAllFromRealm();
                Log.d("clear collect data " + category, "success");
            }
        });
    }

    /**
     * description:清除加入的计划
     */

    private static void clearJoinPlan() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<JoinedPlan> joinedPlen = realm.where(JoinedPlan.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                joinedPlen.deleteAllFromRealm();
                Log.d("clear joined_plan ", "success");
            }
        });
    }


    /**
     * description:清除校历发布者
     */

    private static void ClearInputer() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<User> users = realm.where(User.class).equalTo("isVip", "1").findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                users.deleteAllFromRealm();
                Log.d("clear inpuer ", "success");
            }
        });
    }

    /**
     * description:清除指定年月的校历数据
     *
     * @param year  年
     * @param month 月
     */

    private static void ClearCalendar(final String year, final String month) {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<CalendarData> calendarDatas = realm.where(CalendarData.class)
                .equalTo(NS.YEAR, year)
                .equalTo(NS.MONTH, month).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                calendarDatas.deleteAllFromRealm();
                Log.d("clear calendar " + year + "--" + month, "success");
            }
        });
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
