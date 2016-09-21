package com.xiaoshangxing.utils.school_circle;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.wo.myState.myStateActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.ResponseBody;
import rx.Subscriber;

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
    private boolean isLoaded;

    public CommentTextview(Context context, String reply_id, String reply_text, String id) {
        super(context);
        this.context = context;
        this.reply_id = reply_id;
        this.reply_text = reply_text;
        this.id = id;
        init();
        if (isLoaded) {
            init_just_on_name();
        } else {
            setName1();
        }
    }

    private void setName1() {
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
                            reply_person = user.getString("username");
                            isLoaded = true;
                            init_just_on_name();
                            break;
                        default:
                            setText("加载出错");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.ID, reply_id);
        jsonObject.addProperty(NS.TIMESTAMP, System.currentTimeMillis());
        InfoNetwork.getInstance().GetUser(subscriber, jsonObject, context);
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
//        invalidate();
    }

    private void init() {
        setTextSize(13);
        setTextColor(context.getResources().getColor(R.color.b0));
    }

}
