package com.xiaoshangxing.utils.school_circle;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import com.xiaoshangxing.Network.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.data.UserCache;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.wo.myState.myStateActivity;

import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by FengChaoQun
 * on 2016/9/11
 */
public class CommentTextview extends EmotinText {
    private Context context;
    private String reply_person, reply_text, replyed_person;
    private String reply_id, replyed_id;
    private SpannableString spannableString;
    private String id;

    public CommentTextview(Context context, String reply_id, String reply_text, String id) {
        super(context);
        this.context = context;
        this.reply_id = reply_id;
        this.reply_text = reply_text;
        this.id = id;
        init();
        setName1();
    }

    private void setName1() {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo(NS.ID, Integer.valueOf(reply_id)).findFirst();
        if (false) {
            reply_person = user.getUsername();
            init_just_on_name();
        } else {

            Observable<User> observable = Observable.create(new Observable.OnSubscribe<User>() {
                @Override
                public void call(Subscriber<? super User> subscriber) {
                    User user1 = UserCache.getUserByBlock(reply_id, context);
                    subscriber.onNext(user1);
                }
            });

            Subscriber<User> subscriber = new Subscriber<User>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    append(e.toString());
                }

                @Override
                public void onNext(User user) {
                    reply_person = user.getUsername();
                    init_just_on_name();
                }
            };

            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }


    }

    private void init_just_on_name() {
        spannableString = new SpannableString(reply_person);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(context, myStateActivity.class);
                context.startActivity(intent);
                Log.d("name", reply_person);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(context.getResources().getColor(R.color.blue1));
                ds.setUnderlineText(false);
            }
        }, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        append(spannableString);
        append(":" + reply_text);
        invalidate();
    }

    private void init() {
        setTextSize(13);
        setTextColor(context.getResources().getColor(R.color.b0));
    }

}
