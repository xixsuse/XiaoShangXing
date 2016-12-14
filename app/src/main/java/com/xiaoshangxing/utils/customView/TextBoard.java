package com.xiaoshangxing.utils.customView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/8/23
 */
public class TextBoard extends BaseActivity {
    public static final int REWARD = 10001;
    public static final int HELP = 10002;
    public static final int LAUNCH_PLAN = 10003;
    public static final int IDLE_SALE = 10004;
    public static final int CALENDER = 10005;
    public static final int PROTOCOL = 10006;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.content)
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_board);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        title.setText(getString(R.string.rules));
        switch (getIntent().getIntExtra(IntentStatic.TYPE, 0)) {
            case REWARD:
                content.setText(getString(R.string.reward_rules));
                break;
            case HELP:
                content.setText(getString(R.string.help_rules));
                break;
            case LAUNCH_PLAN:
                content.setText(getString(R.string.launch_plan));
                break;
            case IDLE_SALE:
                content.setText(getString(R.string.idle_sale));
                break;
            case CALENDER:
                content.setText(getString(R.string.calender));
                break;
            case PROTOCOL:
                title.setText("校上行软件许可及服务协议");

                try {
                    StringBuffer sb = new StringBuffer();
                    BufferedReader br = null;
                    InputStream inputStream = getResources().getAssets().open("protocol.txt");
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "gbk");
                    br = new BufferedReader(inputStreamReader);
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    content.setText(sb);
                    br.close();
                } catch (IOException e) {
                    Log.d("assets", "error");
                    e.printStackTrace();
                }

        }
    }
}
