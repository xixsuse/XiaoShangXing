package com.xiaoshangxing.utils.school_circle;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.data.UserCache;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.wo.myState.myStateActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by FengChaoQun
 * on 2016/7/6
 */
public class Comment_net {
    private Context context;
    private EmotinText textView;
    private String reply_person, reply_text, replyed_person;
    private String reply_id, replyed_id;
    private SpannableString spannableString;
    private String id;


    public Comment_net(Context context, String reply_id, String replyed_id, String id) {
        this.context = context;
        this.reply_id = reply_id;
        this.replyed_id = replyed_id;
        this.id = id;

        init();
        setName1();
    }

    public Comment_net(Context context, String reply_person, String replyed_person, String reply_text, String id) {
        this.context = context;
        this.reply_person = reply_person;
        this.reply_text = reply_text;
        this.replyed_person = replyed_person;
        this.id = id;

        init();
        init_two_name();
    }

    private void setName1() {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo(NS.ID, Integer.valueOf(reply_id)).findFirst();
        if (user != null) {
            reply_person = user.getUsername();
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

    private void setName2() {
        Observable<List<User>> observable = Observable.create(new Observable.OnSubscribe<List<User>>() {
            @Override
            public void call(Subscriber<? super List<User>> subscriber) {
                List<User> list = new ArrayList<User>();
                list.add(UserCache.getUserByBlock(reply_id, context));
                list.add(UserCache.getUserByBlock(replyed_id, context));
                subscriber.onNext(list);
            }
        });

        Subscriber<List<User>> subscriber = new Subscriber<List<User>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<User> users) {
                reply_person = users.get(0).getUsername();
                replyed_person = users.get(1).getUsername();
                init_two_name();
            }
        };
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    private void init() {
        this.textView = new EmotinText(context);
        textView.setTextSize(13);
        textView.setTextColor(context.getResources().getColor(R.color.b0));
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
        textView.append(spannableString);
        textView.append(":" + reply_text);
    }

    private void init_two_name() {

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
        textView.append(spannableString);

        textView.append("回复");

        spannableString = new SpannableString(replyed_person);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(context, myStateActivity.class);
                context.startActivity(intent);
                Log.d("name", replyed_person);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(context.getResources().getColor(R.color.blue1));
                ds.setUnderlineText(false);
            }
        }, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.append(spannableString);
        textView.append(":" + reply_text);
    }

    public TextView getTextView() {
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        return this.textView;
    }
}
