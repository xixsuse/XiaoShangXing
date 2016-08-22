package com.xiaoshangxing.yujian.groupchatInfo.groupNotice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.layout.CirecleImage;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/8/13.
 */
public class GroupNoticeShowActivity extends BaseActivity {
    @Bind(R.id.QunzhuHeadImg)
    CirecleImage QunzhuHeadImg;  //群主头像
    @Bind(R.id.QunzhuName)
    TextView QunzhuName;       //群主姓名
    @Bind(R.id.Time)
    TextView Time;            //设置时间
    @Bind(R.id.NoticeContent)
    TextView NoticeContent;
    @Bind(R.id.groupNoticeShow_edit)
    TextView edit;

    private boolean isQUnzhu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_groupnotice_show);
        ButterKnife.bind(this);

        NoticeContent.setText("和融合为哦如何为哦让我耳机哦耳机哦维护请勿去挖掘和期望黑哦亲我喝哦气温就抛弃我ii");

        //如果不是群主 ，编辑不可见
        isQUnzhu = true;
        if (!isQUnzhu) edit.setVisibility(View.GONE);
    }


    @OnClick({R.id.groupNoticeShow_back, R.id.groupNoticeShow_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.groupNoticeShow_back:
                finish();
                break;
            case R.id.groupNoticeShow_edit:
                Intent intent = new Intent(this, GroupNoticeEditActivity.class);
                startActivity(intent);
                break;
        }
    }
}
