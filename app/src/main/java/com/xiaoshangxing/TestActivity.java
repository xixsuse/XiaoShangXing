package com.xiaoshangxing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.xiaoshangxing.input_activity.EmotionEdittext.EmoticonsEditText;
import com.xiaoshangxing.input_activity.EmotionText.EmotTextUtil;
import com.xiaoshangxing.utils.layout.MoreTextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {


    @Bind(R.id.mtv_text)
    MoreTextView mtvText;
    @Bind(R.id.text)
    EmoticonsEditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        ButterKnife.bind(this);
        mtvText.setText(getString(R.string.longtext)+"[ebg]");
        text.setText("[ebt]");

        TextView textView=(TextView)findViewById(R.id.text2);
        textView.setText("[ebt]");
        EmotTextUtil.spannableEmoticonFilter(textView,"[ebt]");
    }
}
