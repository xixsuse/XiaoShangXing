package com.xiaoshangxing.data;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.NS;
import com.xiaoshangxing.Network.Network;
import com.xiaoshangxing.Network.api.InfoApi.GetUser;
import com.xiaoshangxing.utils.image.MyGlide;
import com.xiaoshangxing.utils.school_circle.PraisePeople;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by FengChaoQun
 * on 2016/9/10
 */
public class UserCache {
    private Context context;
    private String userId;
    private Realm realm;
    private User user;

    public UserCache(Context context, String userId, Realm realm) {
        this.context = context;
        this.userId = userId;
        this.realm = realm;
        user = realm.where(User.class).equalTo(NS.ID, Integer.valueOf(userId)).findFirst();
        try_reloadData(null);
    }

    /*
    **describe:尝试加载数据加载数据
    */
    private void try_reloadData(ReloadCallback callback) {
        if (needReload()) {
            reload(callback);
        }
    }

    /*
    **describe:判断是否需要刷新数据
    */
    private boolean needReload() {
        if (user == null) {
            return true;
        } else if (NS.currentTime() - user.getServerTime() > 10 * TimeUtil.MINUTE) {
            return true;
        }

        return false;
    }

    /*
    **describe:刷新数据
    */
    private void reload(final ReloadCallback callback) {
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
                        case 9001:
                            final JSONObject user = jsonObject.getJSONObject(NS.MSG);
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.createOrUpdateObjectFromJson(User.class, user);
                                }
                            });
                            if (callback != null) {
                                callback.callback(user);
                            }
                            break;
                        case 9201:
                            Log.d("用户不存在:", "--" + userId);
                        default:
                            Log.d("更新个人信息失败:", "--" + userId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.ID, userId);
        jsonObject.addProperty(NS.TIMESTAMP, System.currentTimeMillis());
        InfoNetwork.getInstance().GetUser(subscriber, jsonObject, context);
    }

    public void getName(final TextView textView) {
        if (user != null) {
            textView.setText(user.getUsername());
        }
        try_reloadData(new ReloadCallback() {
            @Override
            public void callback(JSONObject jsonObject) throws JSONException {
                textView.setText(jsonObject.getString(NS.USER_NAME));
            }
        });
    }

    public void getCollege(final TextView textView){
        if (user!=null){
            textView.setText(user.getIsCollege());
        }
        try_reloadData(new ReloadCallback() {
            @Override
            public void callback(JSONObject jsonObject) throws JSONException {
                textView.setText(jsonObject.getString("isCollege"));
            }
        });
    }

    public void getHead(final ImageView imageView) {
        if (user != null) {
            MyGlide.with(context, user.getUserImage(), imageView);
        }
        try_reloadData(new ReloadCallback() {
            @Override
            public void callback(JSONObject jsonObject) throws JSONException {
                MyGlide.with(context, jsonObject.getString(NS.USER_IMAGE), imageView);
            }
        });
    }

    public void addName(final String id, final PraisePeople praisePeople) {
        if (user != null) {
            praisePeople.addName(user.getUsername(), id);
        }
        try_reloadData(new ReloadCallback() {
            @Override
            public void callback(JSONObject jsonObject) throws JSONException {
                praisePeople.addName(jsonObject.getString(NS.USER_NAME), id);
            }
        });
    }

    public User getUserBlock() {
        if (user != null) {
            return user;
        }

        Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                        case 9001:
                            final JSONObject user = jsonObject.getJSONObject(NS.MSG);
                            user.put("loadTime", NS.currentTime());
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.createOrUpdateObjectFromJson(User.class, user);
                                }
                            });

                            break;
                        case 9201:
                            Log.d("用户不存在:", "--" + userId);
                        default:
                            Log.d("更新个人信息失败:", "--" + userId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.ID, userId);
        jsonObject.addProperty(NS.TIMESTAMP, System.currentTimeMillis());
        Network.getRetrofitWithHeader(context).create(GetUser.class).user(jsonObject).subscribe(subscriber);
        user = realm.where(User.class).equalTo(NS.ID, Integer.valueOf(userId)).findFirst();
        return user;
    }

    public static User getUserByBlock(final String userId, Context context) {
        final User[] result = {new User()};
        Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                        case 9001:
                            final JSONObject user = jsonObject.getJSONObject(NS.MSG);
                            user.put("loadTime", NS.currentTime());

                            Gson gson = new Gson();
                            result[0] = gson.fromJson(user.toString(), User.class);

                            Realm realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.createOrUpdateObjectFromJson(User.class, user);
                                }
                            });
                            realm.close();

                            break;
                        case 9201:
                            Log.d("用户不存在:", "--" + userId);
                        default:
                            Log.d("更新个人信息失败:", "--" + userId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.ID, userId);
        jsonObject.addProperty(NS.TIMESTAMP, System.currentTimeMillis());
        Network.getRetrofitWithHeader(context).create(GetUser.class).user(jsonObject).subscribe(subscriber);
        return result[0];
    }

//    public static void getName(final Realm realm, final TextView textView, final String userId, Context context) {
//        User user = realm.where(User.class).equalTo(NS.ID, userId).findFirst();
//        if (user != null) {
//            textView.setText(user.getUsername());
//        }
//        if (user == null || isNeedRefresh(userId)) {
//
//            Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
//                @Override
//                public void onCompleted() {
//
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onNext(ResponseBody responseBody) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(responseBody.string());
//                        switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
//                            case 9001:
//                                final JSONObject user = jsonObject.getJSONObject(NS.MSG);
//                                realm.executeTransaction(new Realm.Transaction() {
//                                    @Override
//                                    public void execute(Realm realm) {
//                                        realm.createOrUpdateObjectFromJson(User.class, user);
//                                    }
//                                });
//                                textView.setText(user.getString("username"));
//                                break;
//                            default:
//                                Log.d("更新个人信息失败:", "--" + userId);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty(NS.ID, userId);
//            jsonObject.addProperty("timestamp", System.currentTimeMillis());
//            InfoNetwork.getInstance().GetUser(subscriber, jsonObject, context);
//        }
//    }
//
//    public static void getHead(final Realm realm, final ImageView imageView, final String userId, final Context context){
//        User user = realm.where(User.class).equalTo(NS.ID, userId).findFirst();
//        if (user != null) {
//            MyGlide.with(context,user.getUserImage(),imageView);
//        }
//        if (user == null || isNeedRefresh(userId)) {
//
//            Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
//                @Override
//                public void onCompleted() {
//
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onNext(ResponseBody responseBody) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(responseBody.string());
//                        switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
//                            case 9001:
//                                final JSONObject user = jsonObject.getJSONObject(NS.MSG);
//                                realm.executeTransaction(new Realm.Transaction() {
//                                    @Override
//                                    public void execute(Realm realm) {
//                                        realm.createOrUpdateObjectFromJson(User.class, user);
//                                    }
//                                });
//                                MyGlide.with(context,user.getString("userImage"),imageView);
//                                break;
//                            default:
//                                Log.d("更新个人信息失败:", "--" + userId);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty(NS.ID, userId);
//            jsonObject.addProperty("timestamp", System.currentTimeMillis());
//            InfoNetwork.getInstance().GetUser(subscriber, jsonObject, XSXApplication.getInstance());
//        }
//    }
//
//    public static boolean isNeedRefresh(String id) {
//        return true;
//    }

    interface ReloadCallback {
        void callback(JSONObject jsonObject) throws JSONException;
    }
}
