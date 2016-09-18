package com.xiaoshangxing.xiaoshang.ShoolfellowHelp.MyShoolfellowHelp;

import android.content.Context;

import com.xiaoshangxing.Network.netUtil.LoadUtils;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;

import io.realm.Realm;

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
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty(NS.USER_ID, TempUser.getID(context));
//        jsonObject.addProperty(NS.CATEGORY, NS.CATEGORY_HELP);
//        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());
//
//        Subscriber<ResponseBody> subscriber1 = new Subscriber<ResponseBody>() {
//            @Override
//            public void onCompleted() {
//                frame.refreshComplete();
//                mView.showToast("更新信息成功");
//                LoadUtils.refreshTime(LoadUtils.TIME_LOAD_SELFHELP);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                frame.refreshComplete();
//                mView.showToast("更新信息失败");
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onNext(ResponseBody responseBody) {
//                try {
//                    LoadUtils.parseData(responseBody, realm, mView);
//                    RealmResults<Published> publisheds = realm.where(Published.class).findAll();
//                    Log.d("saved_published", "--" + publisheds);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        PublishNetwork.getInstance().getPublished(subscriber1, jsonObject, context);
        LoadUtils.getSelfState(frame, mView, realm, NS.CATEGORY_HELP, null);
    }

    @Override
    public void LoadMore() {

    }
}
