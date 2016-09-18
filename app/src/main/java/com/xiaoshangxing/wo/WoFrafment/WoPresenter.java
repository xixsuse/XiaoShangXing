package com.xiaoshangxing.wo.WoFrafment;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.netUtil.LoadUtils;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.PublishNetwork;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;

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
 * on 2016/8/4
 */
public class WoPresenter implements WoContract.Presenter {
    private WoContract.View mView;
    private Context context;
    private Realm realm;

    public WoPresenter(WoContract.View mView, Context context) {
        this.mView = mView;
        this.context = context;
    }

    @Override
    public void RefreshData(final PtrFrameLayout frame) {
        mView.setRefreshState(true);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, TempUser.getID(context));
        jsonObject.addProperty(NS.CATEGORY, NS.CATEGORY_STATE);
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());

        Subscriber<ResponseBody> subscriber1 = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                frame.refreshComplete();
                mView.setRefreshState(false);
                mView.showToast("更新信息成功");
                LoadUtils.refreshTime(LoadUtils.TIME_LOAD_STATE);
            }

            @Override
            public void onError(Throwable e) {
                frame.refreshComplete();
                mView.setRefreshState(false);
                mView.showToast("更新信息失败");
                e.printStackTrace();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
//                    LoadUtils.parseData(responseBody, mView.getRealm(), mView);
                    realm=mView.getRealm();
                    LoadUtils.parseData(responseBody,realm,mView);
                    RealmResults<Published> publisheds = realm.where(Published.class).findAll();
                    Log.d("saved_published", "--" + publisheds);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        PublishNetwork.getInstance().getPublished(subscriber1, jsonObject, context);
    }

    @Override
    public void LoadMore() {
        mView.setLoadState(true);
        mView.showNoData();
    }

    @Override
    public boolean isNeedRefresh() {
        return LoadUtils.needRefresh(LoadUtils.TIME_LOAD_STATE);
    }
}
