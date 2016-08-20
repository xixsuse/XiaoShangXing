package com.xiaoshangxing.yujian.chatInfo;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.currency.chatBackground.ChatBackgroundActivity;
import com.xiaoshangxing.setting.utils.ActionSheet;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.SwitchView;
import com.xiaoshangxing.utils.XSXApplication;
import com.xiaoshangxing.utils.normalUtils.SPUtils;
import com.xiaoshangxing.yujian.chatInfo.chooseNewGroupMaster.ChooseNewGroupMasterActivity;
import com.xiaoshangxing.yujian.chatInfo.groupCode.GroupCodeActivity;
import com.xiaoshangxing.yujian.chatInfo.groupMembers.GroupMembersActivity;
import com.xiaoshangxing.yujian.chatInfo.groupName.GroupNameActivity;
import com.xiaoshangxing.yujian.chatInfo.groupNotice.GroupNoticeEditActivity;
import com.xiaoshangxing.yujian.chatInfo.groupNotice.GroupNoticeShowActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/8/12.
 */
public class ChatInfoActivity extends BaseActivity {
    @Bind(R.id.GroupChatName)
    TextView GroupChatName;

    @Bind(R.id.chatinfo_gridview)
    MyGridView mGridview;
    @Bind(R.id.GroupNoticeView1)
    RelativeLayout GroupNoticeView1;
    @Bind(R.id.GroupNotice_content)
    TextView GroupNoticeContent;
    @Bind(R.id.GroupNoticeView2)
    RelativeLayout GroupNoticeView2;
    @Bind(R.id.topChat)
    SwitchView topChat;
    @Bind(R.id.noDisturb)
    SwitchView noDisturb;
    @Bind(R.id.saveToFriend_GroupChat)
    SwitchView saveToFriendGroupChat;

    private Adapter adapter;
    private List<Member> data = new ArrayList<>();
    private String groupChatName;
    private String groupNoticeContent;
    private ActionSheet mActionSheet1;
    private ActionSheet mActionSheet2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_chatinfo);
        ButterKnife.bind(this);

        final Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.cirecleimage_default);
        String name = "姓名";
        for (int i = 0; i < 5; i++) {
            Member member = new Member();
            member.setBitmap(bitmap1);
            member.setName(name);
            data.add(member);
        }
        adapter = new Adapter(this, data);
        mGridview.setAdapter(adapter);

        setGroupChatName();
        setGroupNoticeContent();


    }


    private void setGroupNoticeContent() {
        groupNoticeContent = "最多显示两行最多显示两行最多显示两行最多显示两行最多显示两行最多显示两行最多显示两行最多显示两行最多显示两行最多显示两行最多显示两行最多显示两行";
        if (groupNoticeContent != null) {
            GroupNoticeContent.setText(groupNoticeContent);
            GroupNoticeView1.setVisibility(View.GONE);
            GroupNoticeView2.setVisibility(View.VISIBLE);
        } else {
            GroupNoticeView1.setVisibility(View.VISIBLE);
            GroupNoticeView2.setVisibility(View.GONE);
        }
    }


    private void setGroupChatName() {
        //groupChatNamec = ~~~~
        if (groupChatName == null) {
            GroupChatName.setText("未命名");
        } else {
            GroupChatName.setText(groupChatName);
        }

    }

    @OnClick(R.id.chatinfo_back)
    public void onClick() {
        finish();
    }


    public void AllGroupMember(View view) {
        Intent intent = new Intent(this, GroupMembersActivity.class);
        startActivity(intent);
    }

    public void GroupChatName(View view) {
        Intent intent = new Intent(this, GroupNameActivity.class);
        startActivity(intent);
    }

    public void GroupCode(View view) {
        Intent intent = new Intent(this, GroupCodeActivity.class);
        startActivity(intent);
    }

    public void GroupNotice(View view) {
        boolean isQunzhu = true, isEdited = false;

        if(isQunzhu && !isEdited){
            //如果是群主并且没有公告
            Intent intent = new Intent(this, GroupNoticeEditActivity.class);
            startActivity(intent);
        }else if(!isQunzhu && !isEdited){
            //如果不是群主并且没有公告
            final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(this);
            final Dialog alertDialog = dialogUtils.Message("只有群主王振华才能修改群公告")
                    .Button("我知道了").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
                        @Override
                        public void onButton1() {
                            dialogUtils.close();
                        }

                        @Override
                        public void onButton2() {

                        }

                    }).create();
            alertDialog.show();
            LocationUtil.setWidth(this, alertDialog,
                    getResources().getDimensionPixelSize(R.dimen.x780));
        }else{
            //有公告
            Intent intent = new Intent(this, GroupNoticeShowActivity.class);
            startActivity(intent);
        }

//        Intent intent = new Intent(this, GroupNoticeShowActivity.class);
//        startActivity(intent);

    }

    public void SetChatBackGround(View view) {
        Intent intent = new Intent(this, ChatBackgroundActivity.class);
        startActivity(intent);
    }

    public void TransferMainRight(View view) {
        Intent intent = new Intent(this, ChooseNewGroupMasterActivity.class);
        startActivity(intent);
    }

    public void CleanChatRecord(View view) {
        if (mActionSheet1 == null) {
            mActionSheet1 = new ActionSheet(this);
            mActionSheet1.addMenuBottomItem("清空聊天记录");
        }
        mActionSheet1.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mActionSheet1.getWindow().getAttributes();
        lp.width = (display.getWidth()); //设置宽度
        mActionSheet1.getWindow().setAttributes(lp);
        mActionSheet1.setMenuBottomListener(new ActionSheet.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                Toast.makeText(ChatInfoActivity.this, item, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {

            }
        });
    }

    public void DeleteAndQuit(View view) {
        if (mActionSheet2 == null) {
            mActionSheet2 = new ActionSheet(this);
            mActionSheet2.addMenuTopItem("退出后不会通知群中其他成员，且不会再接收此群聊消息")
                    .addMenuBottomItem("确定");
        }
        mActionSheet2.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mActionSheet2.getWindow().getAttributes();
        lp.width = (display.getWidth()); //设置宽度
        mActionSheet2.getWindow().setAttributes(lp);
        mActionSheet2.setMenuBottomListener(new ActionSheet.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {


            }

            @Override
            public void onCancel() {

            }
        });
    }
}
