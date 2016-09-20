package com.xiaoshangxing.xiaoshang.ShoolfellowHelp.ShoolfellowHelpFragment;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.netUtil.LoadUtils;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.PublishNetwork;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;

import org.json.JSONException;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by FengChaoQun
 * on 2016/8/6
 */
public class ShoolHelpPresenter implements ShoolHelpContract.Presenter {
    private ShoolHelpContract.View mView;
    private Context context;

    public ShoolHelpPresenter(ShoolHelpContract.View mView, Context context) {
        this.mView = mView;
        this.context = context;
    }

    @Override
    public boolean isNeedRefresh() {
        return true;
    }

    @Override
    public void RefreshData(final PtrFrameLayout frame, final Realm realm) {
        mView.setLoadState(true);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, TempUser.id);
        jsonObject.addProperty(NS.CATEGORY, NS.CATEGORY_HELP);
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());

        Subscriber<ResponseBody> subscriber1 = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                frame.refreshComplete();
                mView.setLoadState(false);
                mView.showToast("更新信息成功");
                LoadUtils.refreshTime(LoadUtils.TIME_LOAD_SELFHELP);
            }

            @Override
            public void onError(Throwable e) {
                frame.refreshComplete();
                mView.setLoadState(false);
                mView.showToast("更新信息失败");
                e.printStackTrace();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    LoadUtils.parseData(responseBody, realm, context,null);
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
        mView.refreshPager();
    }

    @Override
    public void LoadMore() {

    }
}
