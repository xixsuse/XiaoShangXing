package com.xiaoshangxing.wo.WoFrafment;

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

import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.wo.PersonalState.PersonalStateActivity;

/**
 * Created by FengChaoQun
 * on 2016/7/6
 */
public class Item_Comment {
    private Context context;
    private EmotinText textView;
    private String reply_person, reply_text, replyed_person;
    private SpannableString spannableString;
    private String user_id, objectId;


    public Item_Comment(Context context, String reply_person, String reply_text, String user_id) {
        this.context = context;
        this.reply_person = reply_person;
        this.reply_text = reply_text;
        this.user_id = user_id;

        init();
        init_just_on_name();
    }

    public Item_Comment(Context context, String reply_person, String replyed_person, String reply_text,
                        String user_id, String objectId) {
        this.context = context;
        this.reply_person = reply_person;
        this.reply_text = reply_text;
        this.replyed_person = replyed_person;
        this.user_id = user_id;
        this.objectId = objectId;

        init();
        init_two_name();
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
                Intent intent = new Intent(context, PersonalStateActivity.class);
                intent.putExtra(IntentStatic.EXTRA_ACCOUNT, user_id);
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
                Intent intent = new Intent(context, PersonalStateActivity.class);
                context.startActivity(intent);
                intent.putExtra(IntentStatic.EXTRA_ACCOUNT, user_id);
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
                Intent intent = new Intent(context, PersonalStateActivity.class);
                intent.putExtra(IntentStatic.EXTRA_ACCOUNT, objectId);
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

    /**
     * description:获取如【某某：巴拉巴拉】格式的textview  用在校上新消息里
     *
     * @param id       评论人id
     * @param name     评论人name
     * @param text     评论的内容
     * @param textView 要填充内容的textView
     */

    public static void getComment(final String id, String name, String text, final Context context, TextView textView) {
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(context, PersonalStateActivity.class);
                intent.putExtra(IntentStatic.EXTRA_ACCOUNT, id);
                context.startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(context.getResources().getColor(R.color.blue1));
                ds.setUnderlineText(false);
            }
        }, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.append(spannableString);
        textView.append(":" + text);
    }
}
