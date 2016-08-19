package com.xiaoshangxing.input_activity.EmotionEdittext;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.xiaoshangxing.input_activity.EmotionEdittext.EmotFilter.MoonUtil;

/**
 * Created by FengChaoQun
 * on 2016/8/5
 */
public class EmotinText extends TextView {
    public EmotinText(Context context) {
        super(context);
    }

    public EmotinText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmotinText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
//        EmotTextUtil.spannableEmoticonFilter(this,text.toString());
//    }

    @Override
    public void setText(CharSequence text, BufferType type) {
//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
//        Spannable spannable = QqFilter.spannableFilter(this.getContext(),
//                spannableStringBuilder,
//                text,
//                EmotionFilter.getFontHeight(this));
//
//        super.setText(spannable, type);
        SpannableString spannableString = MoonUtil.makeSpannableStringTags(getContext(), text.toString(), 0.6f, ImageSpan.ALIGN_BOTTOM, MoonUtil.getFontHeight(this));
        super.setText(spannableString, type);
//        MoonUtil.identifyFaceExpression(getContext(), this, getText().toString(), ImageSpan.ALIGN_BOTTOM);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
//        Spannable spannable = QqFilter.spannableFilter(this.getContext(),
//                spannableStringBuilder,
//                text,
//                EmotionFilter.getFontHeight(this));
        SpannableString spannableString = MoonUtil.makeSpannableStringTags(getContext(), text.toString(), 0.6f, ImageSpan.ALIGN_BOTTOM, MoonUtil.getFontHeight(this));
        super.onTextChanged(spannableString, start, lengthBefore, lengthAfter);
    }

    @Override
    public void append(CharSequence text, int start, int end) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        Spannable spannable1 = MoonUtil.getSpannable(
                spannableStringBuilder, getContext(), text.toString(), MoonUtil.getFontHeight(this));
        super.append(spannable1, start, end);
    }
}
