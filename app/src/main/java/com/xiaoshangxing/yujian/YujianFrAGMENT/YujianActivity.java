package com.xiaoshangxing.yujian.YujianFragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.EditText;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/8/10
 */
public class YujianActivity extends BaseActivity {
    @Bind(R.id.edittext)
    EditText edittext;
    private SpannableString spannableString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian);
        ButterKnife.bind(this);
//        edittext.setHint();
        spannableString = new SpannableString("image" + " 搜索");
        Drawable d = getResources().getDrawable(R.mipmap.blue_heart);
//        d.setBounds(0, 0, context.getResources().getDimensionPixelSize(R.dimen.image_11sp),
//                context.getResources().getDimensionPixelSize(R.dimen.image_10sp));
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        spannableString.setSpan(new ImageSpan(d, ImageSpan.ALIGN_BASELINE), 0, 5,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        edittext.setHint(spannableString);
    }
}
