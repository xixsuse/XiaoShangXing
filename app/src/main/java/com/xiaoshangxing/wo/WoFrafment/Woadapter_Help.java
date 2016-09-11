package com.xiaoshangxing.wo.WoFrafment;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.NS;
import com.xiaoshangxing.data.DataCopy;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.data.UserCache;
import com.xiaoshangxing.utils.school_circle.PraisePeople;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by FengChaoQun
 * on 2016/9/10
 */
public class Woadapter_Help {

    public static void buildPrasiPeople(final String[] ids, final Context context,
                                        final LinearLayout linearLayout) {


        linearLayout.removeAllViews();


        Observable<List<User>> observable = Observable.create(new Observable.OnSubscribe<List<User>>() {
            @Override
            public void call(Subscriber<? super List<User>> subscriber) {
                Realm realm = Realm.getDefaultInstance();
                ArrayList<User> users = new ArrayList<User>();
                for (String id : ids) {
                    User user = realm.where(User.class).equalTo(NS.ID, Integer.valueOf(id)).findFirst();
                    if (user == null) {
                        user = UserCache.getUserByBlock(id, context);
                    }
                    users.add(DataCopy.copyUser(user));
                }
                realm.close();
                subscriber.onNext(users);
            }
        });

        Subscriber<List<User>> subscriber = new Subscriber<List<User>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                TextView textView = new TextView(context);
                textView.setText(e.toString());
                linearLayout.removeAllViews();
                linearLayout.addView(textView);
                e.printStackTrace();

            }

            @Override
            public void onNext(List<User> users) {
                PraisePeople praisePeople = new PraisePeople(context);
                for (User user : users) {
                    praisePeople.addName(user.getUsername(), String.valueOf(user.getId()));
                }
                linearLayout.removeAllViews();
                linearLayout.addView(praisePeople.getTextView());
            }
        };

        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
