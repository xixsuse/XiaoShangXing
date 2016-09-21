package com.xiaoshangxing.setting.shiming.sucess;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tianyang on 2016/9/20.
 */
public class VertifySucessActivity extends BaseActivity {
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.sex)
    TextView sex;
    @Bind(R.id.xuehao)
    TextView xuehao;
    @Bind(R.id.school)
    TextView school;
    @Bind(R.id.xueyuan)
    TextView xueyuan;
    @Bind(R.id.zhuanye)
    TextView zhuanye;
    @Bind(R.id.ruxuenianfen)
    TextView ruxuenianfen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_sucess);
        ButterKnife.bind(this);

    }

    public void Back(View view) {
        finish();
    }
}
