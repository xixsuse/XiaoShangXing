package com.xiaoshangxing.yujian.personInfo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.xiaoshang.Plan.PlanActivity;
import com.xiaoshangxing.xiaoshang.Sale.SaleActivity;
import com.xiaoshangxing.xiaoshang.Reward.RewardActivity;
import com.xiaoshangxing.xiaoshang.Help.HelpActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 15828 on 2016/7/25.
 */
public class MoreInfoActivity extends BaseActivity {
    private TextView personalInfo;
    private String account;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_moreinfo);
        personalInfo = (TextView) findViewById(R.id.moreinfo_text);

        account = getIntent().getStringExtra(IntentStatic.EXTRA_ACCOUNT);

        if (TextUtils.isEmpty(account)) {
            showToast("账号异常");
            return;
        }

        user = UserInfoCache.getInstance().getUser(Integer.valueOf(account));
        if (user != null) {
            personalInfo.setText("" + user.getSignature());
        } else {
            UserInfoCache.getInstance().reload(new UserInfoCache.ReloadCallback() {
                @Override
                public void callback(JSONObject jsonObject) throws JSONException {
                    personalInfo.setText(jsonObject.getString("signature"));
                }
            }, Integer.valueOf(account));
        }


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
        Intent intent = new Intent(this, PlanActivity.class);
        intent.putExtra(IntentStatic.TYPE, IntentStatic.OTHERS);
        intent.putExtra(IntentStatic.EXTRA_ACCOUNT, account);
        startActivity(intent);
    }

    public void HuBang(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        intent.putExtra(IntentStatic.TYPE, IntentStatic.OTHERS);
        intent.putExtra(IntentStatic.EXTRA_ACCOUNT, account);
        startActivity(intent);
    }

    public void XuanShang(View view) {
        Intent intent = new Intent(this, RewardActivity.class);
        intent.putExtra(IntentStatic.TYPE, IntentStatic.OTHERS);
        intent.putExtra(IntentStatic.EXTRA_ACCOUNT, account);
        startActivity(intent);
    }

    public void XianZhi(View view) {
        Intent intent = new Intent(this, SaleActivity.class);
        intent.putExtra(IntentStatic.TYPE, IntentStatic.OTHERS);
        intent.putExtra(IntentStatic.EXTRA_ACCOUNT, account);
        startActivity(intent);
    }
}
