package com.xiaoshangxing.xiaoshang.Help.HelpFragment;

import android.content.Context;

import com.xiaoshangxing.utils.customView.pull_refresh.PtrFrameLayout;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/8/6
 */
public class HelpPresenter implements HelpContract.Presenter {
    private HelpContract.View mView;
    private Context context;

    public HelpPresenter(HelpContract.View mView, Context context) {
        this.mView = mView;
        this.context = context;
    }

    @Override
    public boolean isNeedRefresh() {
        return true;
    }

    @Override
    public void RefreshData(final PtrFrameLayout frame, final Realm realm) {
//        mView.setLoadState(true);
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty(NS.USER_ID, TempUser.id);
//        jsonObject.addProperty(NS.CATEGORY, NS.CATEGORY_HELP);
//        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());
//
//        Subscriber<ResponseBody> subscriber1 = new Subscriber<ResponseBody>() {
//            @Override
//            public void onCompleted() {
//                frame.refreshComplete();
//                mView.setLoadState(false);
//                mView.showToast("更新信息成功");
//                LoadUtils.refreshTime(LoadUtils.TIME_LOAD_SELFHELP);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                frame.refreshComplete();
//                mView.setLoadState(false);
//                mView.showToast("更新信息失败");
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onNext(ResponseBody responseBody) {
//                try {
//                    LoadUtils.parseData(responseBody, realm, context,null);
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
//        PublishNetwork.getInstance().getPersonalPublished(subscriber1, jsonObject, context);
//        mView.refreshPager();
    }

    @Override
    public void LoadMore() {

    }
}
