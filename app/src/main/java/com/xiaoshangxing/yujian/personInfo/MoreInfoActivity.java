package com.xiaoshangxing.yujian.personInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.xiaoshang.ShoolReward.ShoolRewardActivity;
import com.xiaoshangxing.xiaoshang.ShoolfellowHelp.ShoolfellowHelpActivity;

/**
 * Created by 15828 on 2016/7/25.
 */
public class MoreInfoActivity extends BaseActivity {
    private TextView personalInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_moreinfo);
        personalInfo = (TextView) findViewById(R.id.moreinfo_text);

        assert personalInfo != null;
        ViewTreeObserver vto = personalInfo.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int lineCount = personalInfo.getLineCount();
                if (lineCount == 1) personalInfo.setGravity(Gravity.RIGHT);
                else personalInfo.setGravity(Gravity.LEFT);
                // System.out.println(lineCount);
                return true;
            }
        });


    }

    public void Back(View view) {
        finish();
    }

    public void JiHua(View view) {
    }

    public void HuBang(View view) {
        Intent intent=new Intent(this, ShoolfellowHelpActivity.class);
        intent.putExtra(IntentStatic.TYPE,ShoolfellowHelpActivity.OTHERS);
        startActivity(intent);
    }

    public void XuanShang(View view) {
        Intent intent=new Intent(this, ShoolRewardActivity.class);
        intent.putExtra(IntentStatic.TYPE,ShoolRewardActivity.OTHERS);
        startActivity(intent);
    }

    public void XianZhi(View view) {
    }
}
