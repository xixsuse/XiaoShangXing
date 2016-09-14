package com.xiaoshangxing.xiaoshang.ShoolfellowHelp.MyShoolfellowHelp;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.LoadUtils;
import com.xiaoshangxing.Network.NS;
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
public class MyHelpPresenter implements MyhelpContract.Presenter {
    private MyhelpContract.View mView;
    private Context context;
    private Realm realm;

    public MyHelpPresenter(MyhelpContract.View mView, Context context, Realm realm) {
        this.mView = mView;
        this.context = context;
        this.realm = realm;
    }

    @Override
    public void transmit() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void completeOrCancle() {

    }

    @Override
    public void refreshData(final PtrFrameLayout frame) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, TempUser.getID(context));
        jsonObject.addProperty(NS.CATEGORY, NS.CATEGORY_HELP);
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());

        Subscriber<ResponseBody> subscriber1 = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                frame.refreshComplete();
                mView.showToast("更新信息成功");
                LoadUtils.refreshTime(LoadUtils.TIME_LOAD_SELFHELP);
            }

            @Override
            public void onError(Throwable e) {
                frame.refreshComplete();
                mView.showToast("更新信息失败");
                e.printStackTrace();
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

        PublishNetwork.getInstance().getPublished(subscriber1, jsonObject, context);
    }

//    private void parseData(ResponseBody responseBody) throws IOException, JSONException {
//        JSONObject jsonObject = new JSONObject(responseBody.string());
//        switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
//            case 200:
//                final JSONArray jsonArray = jsonObject.getJSONObject(NS.MSG).getJSONArray("moments");
//                realm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        realm.createOrUpdateAllFromJson(Published.class, jsonArray);
//                    }
//                });
//                break;
//            default:
//                mView.showToast(jsonObject.getString(NS.MSG));
//                break;
//        }
//    }


    @Override
    public void LoadMore() {

    }
}
