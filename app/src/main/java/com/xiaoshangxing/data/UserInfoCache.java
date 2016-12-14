package com.xiaoshangxing.data;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.uinfo.UserServiceObserve;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.network.InfoNetwork;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.data.bean.User;
import com.xiaoshangxing.utils.XSXApplication;
import com.xiaoshangxing.utils.imageUtils.MyGlide;
import com.xiaoshangxing.utils.normalUtils.SPUtils;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
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

    private Observer<List<NimUserInfo>> observer;

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

//    public void getExInto

    public void getExIntoTextview(String id, String key, TextView textView) {
        getExIntoTextview(id, key, textView, "未知");
    }

    public void getExIntoTextview(String id, final String key, final TextView textView, final String null_notice) {
        final NimUserInfo userInfo = NimUserInfoCache.getInstance().getUserInfo(id);
        if (userInfo != null) {
            textView.setText(getStringFromUserInfo(userInfo, key, null_notice));
        } else {
            NimUserInfoCache.getInstance().getUserInfoFromRemote(id, new RequestCallback<NimUserInfo>() {
                @Override
                public void onSuccess(NimUserInfo nimUserInfo) {
                    textView.setText(getStringFromUserInfo(nimUserInfo, key, null_notice));
                }

                @Override
                public void onFailed(int i) {
                    textView.setText("error" + i);
                }

                @Override
                public void onException(Throwable throwable) {
                    textView.setText("error");
                    throwable.printStackTrace();
                }
            });
        }
    }

    private String getStringFromUserInfo(NimUserInfo userInfo, String key, String null_notice) {
        if (userInfo.getExtensionMap() != null) {
            String text = String.valueOf(userInfo.getExtensionMap().get(key));
            return TextUtils.isEmpty(text) ? null_notice : text;
        } else {
            return "信息未同步";
        }
    }

    public void getHeadIntoImage(String id, final ImageView imageView) {
        NimUserInfo userInfo = NimUserInfoCache.getInstance().getUserInfo(id);
        if (userInfo != null) {
            MyGlide.with_default_head(imageView.getContext(), userInfo.getAvatar(), imageView);
        } else {
            NimUserInfoCache.getInstance().getUserInfoFromRemote(id, new RequestCallback<NimUserInfo>() {
                @Override
                public void onSuccess(NimUserInfo nimUserInfo) {
                    MyGlide.with_default_head(imageView.getContext(), nimUserInfo.getAvatar(), imageView);
                }

                @Override
                public void onFailed(int i) {
                    Log.e("get userInfo error", "" + i);
                }

                @Override
                public void onException(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
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

    public void registerDataChangeListner(boolean is) {
        if (observer == null) {
            observer = new Observer<List<NimUserInfo>>() {
                @Override
                public void onEvent(List<NimUserInfo> nimUserInfos) {
                    for (NimUserInfo userInfo : nimUserInfos) {
                        if (userInfo.getAccount().equals(String.valueOf(TempUser.id))) {
                            if (((Integer) userInfo.getExtensionMap().get("isActive")).equals(1)) {
                                TempUser.isRealName = true;
                                SPUtils.put(XSXApplication.getInstance(), SPUtils.IS_REAL_NAME, true);
                            } else {
                                TempUser.isRealName = false;
                                SPUtils.put(XSXApplication.getInstance(), SPUtils.IS_REAL_NAME, false);
                            }
                        }
                    }
                }
            };
        }
        NIMClient.getService(UserServiceObserve.class).observeUserInfoUpdate(observer, is);
        Log.d("注册用户资料变更监听", "" + is);
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
