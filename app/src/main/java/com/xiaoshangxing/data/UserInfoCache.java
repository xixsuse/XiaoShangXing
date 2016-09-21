package com.xiaoshangxing.data;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.utils.XSXApplication;
import com.xiaoshangxing.utils.image.MyGlide;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by FengChaoQun
 * on 2016/9/12
 */
public class UserInfoCache {

    private Map<Integer, User> userMap = new ConcurrentHashMap<>();

    public static UserInfoCache getInstance() {
        return InstanceHolder.instance;
    }

    public void build() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<User> users = realm.where(User.class).findAll();

        if (users.isEmpty()) {
            return;
        }
        for (User i : users) {
            userMap.put(i.getId(), DataCopy.copyUser(i));
        }
        realm.close();
    }


    public User getUser(int id) {
        if (userMap.containsKey(id)) {
            User user = userMap.get(id);
            try_reloadData(user);
            return user;
        } else {
            return null;
        }
    }

    public void getName(final TextView textView, int id) {
        if (userMap.containsKey(id)) {
            textView.setText(userMap.get(id).getUsername());
        } else {
            reload(new ReloadCallback() {
                @Override
                public void callback(JSONObject jsonObject) throws JSONException {
                    textView.setText(jsonObject.getString(NS.USER_NAME));
                }
            }, id);
        }
    }

    public void getCollege(final TextView textView, int id) {
        if (userMap.containsKey(id)) {
            textView.setText(userMap.get(id).getIsCollege());
        } else {
            reload(new ReloadCallback() {
                @Override
                public void callback(JSONObject jsonObject) throws JSONException {
                    textView.setText(jsonObject.getString("isCollege"));
                }
            }, id);
        }

    }

    public void getHead(final ImageView imageView, int id, final Context context) {
        if (userMap.containsKey(id)) {
            MyGlide.with(context, userMap.get(id).getUserImage(), imageView);
        } else {
            reload(new ReloadCallback() {
                @Override
                public void callback(JSONObject jsonObject) throws JSONException {
                    MyGlide.with(context, jsonObject.getString(NS.USER_IMAGE), imageView);
                }
            }, id);
        }
    }

    /*
    **describe:刷新指定数据
    */
    public void refreshSomeone(User user) {
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
        }
    }

    /*
    **describe:尝试加载数据加载数据
    */
    private void try_reloadData(User user) {
        if (needReload(user)) {
            reload(null, user.getId());
        }
    }

    /*
    **describe:判断是否需要刷新数据
    */
    private boolean needReload(User user) {
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
    public void reload(final ReloadCallback callback, final int id) {
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

                            Realm realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.createOrUpdateObjectFromJson(User.class, user);
                                }
                            });
                            realm.close();

                            Gson gson = new Gson();
                            User user1 = gson.fromJson(user.toString(), User.class);
                            if (user1 != null) {
                                userMap.put(user1.getId(), user1);
                            }


                            if (callback != null) {
                                callback.callback(user);
                            }
                            break;
                        case 9201:
                            Log.d("用户不存在:", "--" + id);
                            /*
                            **describe:存储未知信息到数据库 防止不断刷新
                            */
                            final Realm realm1 = Realm.getDefaultInstance();
                            realm1.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    final User user2 = new User();
                                    user2.setId(id);
                                    user2.setUsername("未知");
                                    user2.setServerTime(NS.currentTime());
                                    realm.copyToRealmOrUpdate(user2);
                                    userMap.put(id, user2);
                                }
                            });
                            realm1.close();
                            break;
                        default:
                            Log.d("更新个人信息失败:", "--" + id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.ID, id);
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());
        InfoNetwork.getInstance().GetUser(subscriber, jsonObject, XSXApplication.getInstance());
    }

   public interface ReloadCallback {
        void callback(JSONObject jsonObject) throws JSONException;
    }

    /**
     * ************************************ 单例 **********************************************
     */

    static class InstanceHolder {
        final static UserInfoCache instance = new UserInfoCache();
    }
}
