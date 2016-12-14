package com.xiaoshangxing.utils.customView;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.wo.PersonalState.PersonalStateActivity;
import com.xiaoshangxing.yujian.personInfo.PersonInfoActivity;

/**
 * Created by FengChaoQun
 * on 2016/8/4
 * 姓名textview
 */
public class Name extends TextView {
    public static final int PERSON_INFO = 1000;
    public static final int PERSON_STATE = 2000;
    private Context context;
    private int intent_type;
    private String account;

    public Name(Context context) {
        super(context);
        init(context);
    }

    public Name(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Name(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    public void setIntent_type(int intent_type, final String account) {
        this.intent_type = intent_type;
        this.account = account;
        switch (intent_type) {
            case PERSON_INFO:
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent state_intent = new Intent(context, PersonInfoActivity.class);
                        state_intent.putExtra(IntentStatic.EXTRA_ACCOUNT, account);
                        context.startActivity(state_intent);
                    }
                });
                break;
            case PERSON_STATE:
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent state_intent = new Intent(context, PersonalStateActivity.class);
                        state_intent.putExtra(IntentStatic.EXTRA_ACCOUNT, account);
                        context.startActivity(state_intent);
                    }
                });
                break;
        }

    }

    public int getIntent_type() {
        return intent_type;
    }

}
