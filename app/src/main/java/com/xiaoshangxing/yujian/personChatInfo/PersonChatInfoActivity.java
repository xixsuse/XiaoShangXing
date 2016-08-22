package com.xiaoshangxing.yujian.personChatInfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.currency.chatBackground.ChatBackgroundActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.yujian.groupchatInfo.Member;
import com.xiaoshangxing.yujian.groupchatInfo.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tianyang on 2016/8/22.
 */
public class PersonChatInfoActivity extends BaseActivity {

    @Bind(R.id.person_chatinfo_gridview)
    MyGridView mGridview;

    private PersonalAdapter adapter;
    private List<Member> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_personchatinfo);
        ButterKnife.bind(this);

        final Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.cirecleimage_default);
        String name = "姓名";
        for (int i = 0; i < 1; i++) {
            Member member = new Member();
            member.setBitmap(bitmap1);
            member.setName(name);
            data.add(member);
        }
        adapter = new PersonalAdapter(this, data);
        mGridview.setAdapter(adapter);

    }

    public void Back(View view) {
        finish();
    }

    public void SetChatBackGround(View view) {
        Intent intent = new Intent(this, ChatBackgroundActivity.class);
        startActivity(intent);
    }

    public void CleanChatRecord(View view) {

    }
}
