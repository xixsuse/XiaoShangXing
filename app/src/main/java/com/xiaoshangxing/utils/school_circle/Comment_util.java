package com.xiaoshangxing.utils.school_circle;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by FengChaoQun
 * on 2016/9/18
 */
public class Comment_util {

    public static void loadComment(String reply_id, final String reply_text, final TextView textView) {
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
                            String reply_person = user.getString("username");

                            SpannableString spannableString = new SpannableString(reply_person);
                            spannableString.setSpan(new ClickableSpan() {
                                @Override
                                public void onClick(View widget) {
//                                    Intent intent = new Intent(context, myStateActivity.class);
//                                    context.startActivity(intent);
//                                    Log.d("name", reply_person);
                                }

                                @Override
                                public void updateDrawState(TextPaint ds) {
                                    ds.setColor(textView.getResources().getColor(R.color.blue1));
                                    ds.setUnderlineText(false);
                                }
                            }, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            textView.append(spannableString);
                            textView.append(":" + reply_text);

                            break;
                        default:
                            textView.setText("加载错误");
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
        InfoNetwork.getInstance().GetUser(subscriber, jsonObject, textView.getContext());
    }

}
