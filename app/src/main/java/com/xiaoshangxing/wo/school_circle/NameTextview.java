package com.xiaoshangxing.wo.school_circle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.R;

/**
 * Created by FengChaoQun
 * on 2016/7/4
 */
public class NameTextview extends TextView {
    private String name="冯超群";
    private int id=100000;
    private boolean isend=false;
    private Context context;
    public NameTextview(Context context,int id,boolean isend) {
        super(context);
        this.isend=isend;
        this.id=id;
        init(context);


    }

    public NameTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NameTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }



    public NameTextview(Context context) {
        super(context);
        init(context);
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(context.getResources().getColor(R.color.blue1));
    }



    private void init(Context mcontext){
        this.context=mcontext;
        if (isend){
            setText(getName());
        }else {
            setText(getName()+",");
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+id, Toast.LENGTH_SHORT).show();
            }
        });
        setTextColor(context.getResources().getColor(R.color.blue1));
        setTextSize(13);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setText(name);
    }
}
