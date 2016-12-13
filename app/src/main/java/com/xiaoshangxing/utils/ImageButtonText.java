package com.xiaoshangxing.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.DataSetting;

/**
 * Created by 15828 on 2016/7/25.
 */
public class ImageButtonText extends LinearLayout implements View.OnClickListener {

    private ImageView imgView;
    private TextView textView;
    private boolean checked = false;
    private OnImageButtonTextClickListener mOnImageButtonTextClickListener;

    public ImageButtonText(Context context) {
        super(context, null);
    }

    public ImageButtonText(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.button_img_text, this, true);

        this.imgView = (ImageView) findViewById(R.id.imgview);
        this.textView = (TextView) findViewById(R.id.textview);

        this.setClickable(true);
        this.setFocusable(true);
        if (!checked) {
            imgView.setImageResource(R.mipmap.icon_liuxin);
        } else {
            imgView.setImageResource(R.mipmap.icon_liuxin_select);
        }
        this.setOnClickListener(this);

    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setImgView(ImageView imgView){
        this.imgView = imgView;
    }

    public ImageView getImgView() {
        return imgView;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    @Override
    public void onClick(View v) {
        mOnImageButtonTextClickListener.OnImageButtonTextClick();
    }


    public interface OnImageButtonTextClickListener {
        void OnImageButtonTextClick();
    }

    public void setmOnImageButtonTextClickListener(OnImageButtonTextClickListener mOnImageButtonTextClickListener) {
        this.mOnImageButtonTextClickListener = mOnImageButtonTextClickListener;
    }
}
