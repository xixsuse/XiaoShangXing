package com.xiaoshangxing.yujian.personInfo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.baseClass.BaseActivity;
import com.xiaoshangxing.xiaoshang.Help.HelpActivity;
import com.xiaoshangxing.xiaoshang.Plan.PlanActivity;
import com.xiaoshangxing.xiaoshang.Reward.RewardActivity;
import com.xiaoshangxing.xiaoshang.Sale.SaleActivity;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *modified by FengChaoQun on 2016/12/24 19:22
 * description:优化代码
 */
public class MoreInfoActivity extends BaseActivity {
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.moreinfo_text)
    TextView moreinfoText;
    private TextView personalInfo;
    private String account;
    private NimUserInfo nimUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_moreinfo);
        ButterKnife.bind(this);
        personalInfo = (TextView) findViewById(R.id.moreinfo_text);
        title.setText("更多资料");
        more.setVisibility(View.GONE);

        account = getIntent().getStringExtra(IntentStatic.ACCOUNT);

        if (TextUtils.isEmpty(account)) {
            showToast("账号异常");
            return;
        }

        nimUserInfo = NimUserInfoCache.getInstance().getUserInfo(account);

        if (nimUserInfo != null) {
            personalInfo.setText("" + nimUserInfo.getSignature());
        }
        assert personalInfo != null;
        ViewTreeObserver vto = personalInfo.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int lineCount = personalInfo.getLineCount();
                if (lineCount == 1) personalInfo.setGravity(Gravity.RIGHT);
                else personalInfo.setGravity(Gravity.LEFT);
                return true;
            }
        });

    }

    public void JiHua(View view) {
        Intent intent = new Intent(this, PlanActivity.class);
        intent.putExtra(IntentStatic.TYPE, IntentStatic.OTHERS);
        intent.putExtra(IntentStatic.ACCOUNT, account);
        startActivity(intent);
    }

    public void HuBang(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        intent.putExtra(IntentStatic.TYPE, IntentStatic.OTHERS);
        intent.putExtra(IntentStatic.ACCOUNT, account);
        startActivity(intent);
    }

    public void XuanShang(View view) {
        Intent intent = new Intent(this, RewardActivity.class);
        intent.putExtra(IntentStatic.TYPE, IntentStatic.OTHERS);
        intent.putExtra(IntentStatic.ACCOUNT, account);
        startActivity(intent);
    }

    public void XianZhi(View view) {
        Intent intent = new Intent(this, SaleActivity.class);
        intent.putExtra(IntentStatic.TYPE, IntentStatic.OTHERS);
        intent.putExtra(IntentStatic.ACCOUNT, account);
        startActivity(intent);
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
