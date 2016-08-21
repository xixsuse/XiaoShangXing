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

import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.wo.myState.myStateActivity;

/**
 * Created by FengChaoQun
 * on 2016/7/6
 */
public class Item_Comment {
    private Context context;
    private EmotinText textView;
    private String reply_person,reply_text,replyed_person;
    private SpannableString spannableString;


    public Item_Comment(Context context, String reply_person, String reply_text) {
        this.context = context;
        this.reply_person = reply_person;
        this.reply_text = reply_text;

        init();
        init_just_on_name();
    }

    public Item_Comment(Context context, String reply_person,String replyed_person,String reply_text) {
        this.context = context;
        this.reply_person = reply_person;
        this.reply_text = reply_text;
        this.replyed_person = replyed_person;

        init();
        init_two_name();
    }

    private void  init(){
        this.textView=new EmotinText(context);
        textView.setTextSize(13);
        textView.setTextColor(context.getResources().getColor(R.color.b0));
    }

    private void init_just_on_name(){
        spannableString=new SpannableString(reply_person);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent=new Intent(context,myStateActivity.class);
                context.startActivity(intent);
                Log.d("name",reply_person);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(context.getResources().getColor(R.color.blue1));
                ds.setUnderlineText(false);
            }
        },0,spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.append(spannableString);
        textView.append(":"+reply_text);
    }

    private void init_two_name(){

        spannableString=new SpannableString(reply_person);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent=new Intent(context,myStateActivity.class);
                context.startActivity(intent);
                Log.d("name",reply_person);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(context.getResources().getColor(R.color.blue1));
                ds.setUnderlineText(false);
            }
        },0,spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.append(spannableString);

        textView.append("回复");

        spannableString=new SpannableString(replyed_person);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent=new Intent(context,myStateActivity.class);
                context.startActivity(intent);
                Log.d("name",replyed_person);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(context.getResources().getColor(R.color.blue1));
                ds.setUnderlineText(false);
            }
        },0,spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.append(spannableString);
        textView.append(":"+reply_text);
    }

    public TextView getTextView(){
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        return this.textView;
    }
}
