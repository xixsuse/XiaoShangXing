package com.xiaoshangxing.yujian.personChatInfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.model.Team;
import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.setting.currency.chatBackground.ChatBackgroundActivity;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.yujian.IM.TeamCreateHelper;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;
import com.xiaoshangxing.yujian.groupchatInfo.Member;
import com.xiaoshangxing.yujian.groupchatInfo.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tianyang on 2016/8/22.
 */
public class PersonChatInfoActivity extends BaseActivity implements IBaseView {

    @Bind(R.id.person_chatinfo_gridview)
    MyGridView mGridview;

    private PersonalAdapter adapter;
    private List<Member> data = new ArrayList<>();
    private String account;

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_personchatinfo);
        ButterKnife.bind(this);

        account = getIntent().getStringExtra(IntentStatic.EXTRA_ACCOUNT);
        final Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.cirecleimage_default);
        String name = NimUserInfoCache.getInstance().getUserDisplayName(account);
        for (int i = 0; i < 1; i++) {
            Member member = new Member();
            member.setBitmap(bitmap1);
            member.setName(name);
            data.add(member);
        }
        adapter = new PersonalAdapter(this, data, this, account);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SelectPersonActivity.SELECT_PERSON_CODE) {
            if (data==null){
                Toast.makeText(PersonChatInfoActivity.this, "没有选择联系人", Toast.LENGTH_SHORT).show();
                return;
            }
            ArrayList<String> arrayList = data.getStringArrayListExtra(SelectPersonActivity.SELECT_PERSON);
            if (arrayList == null || arrayList.size() == 0) {
                Toast.makeText(PersonChatInfoActivity.this, "没有选择联系人", Toast.LENGTH_SHORT).show();
            } else {
                arrayList.add(account);
                Log.d("select account", arrayList.toString());
                TeamCreateHelper.createAdvancedTeam(PersonChatInfoActivity.this,
                        arrayList, this, new RequestCallback<Team>() {
                            @Override
                            public void onSuccess(Team team) {

                            }

                            @Override
                            public void onFailed(int i) {

                            }

                            @Override
                            public void onException(Throwable throwable) {

                            }
                        });
            }
        }
    }
}
