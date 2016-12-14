package com.xiaoshangxing.wo.StateDetailsActivity;

import android.content.Context;

import com.google.gson.JsonObject;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.network.PublishNetwork;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.bean.Published;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.ResponseBody;

/**
 * Created by FengChaoQun
 * on 2016/8/6
 */
public class DetailPresenter implements DetailsContract.Presenter {
    private DetailsContract.View mView;
    private Context context;

    public DetailPresenter(DetailsContract.View mView, Context context) {
        this.mView = mView;
        this.context = context;
    }

    @Override
    public void delete(final Realm realm, final Published published) {
        ProgressSubscriberOnNext<ResponseBody> next = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                        case 8001:
                            mView.showToast("删除成功");
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    published.deleteFromRealm();
                                }
                            });
                            mView.finishPager();
                            break;
                        default:
                            mView.showToast(jsonObject.getString(NS.MSG));
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };

        ProgressSubsciber<ResponseBody> subsciber = new ProgressSubsciber<>(next, mView);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, TempUser.id);
        jsonObject.addProperty(NS.MOMENTID, published.getId());
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());
        PublishNetwork.getInstance().deletePublished(subsciber, jsonObject, context);
    }

    @Override
    public void praise() {

    }
}
