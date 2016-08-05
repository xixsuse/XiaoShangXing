package com.xiaoshangxing.utils.layout;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.wo.myState.myStateActivity;

/**
 * Created by FengChaoQun
 * on 2016/8/4
 */
public class Name extends TextView {
    private Context context;
    private boolean is_clickable;
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

    private void init(Context context){
        this.context=context;
        setColor(getResources().getColor(R.color.blue1));
        if (is_clickable){
            setOnClickListener(clickListener);
        }
    }

    private void setColor(int color){
        setTextColor(color);
    }

    @Override
    public void setOnClickListener(OnClickListener clickListener) {
        super.setOnClickListener(clickListener);
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
        invalidate();
    }

    private OnClickListener clickListener=new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(context, myStateActivity.class);
            context.startActivity(intent);
        }
    };

    public void setIs_clickable(boolean is_clickable) {
        this.is_clickable = is_clickable;
        invalidate();
    }
}
