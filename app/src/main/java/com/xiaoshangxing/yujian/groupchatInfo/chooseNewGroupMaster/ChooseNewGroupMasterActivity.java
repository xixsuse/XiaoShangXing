package com.xiaoshangxing.yujian.groupchatInfo.chooseNewGroupMaster;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.CharacterParser;
import com.xiaoshangxing.SelectPerson.SideBar;

import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.yujian.groupchatInfo.Member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 15828 on 2016/8/13.
 */
public class ChooseNewGroupMasterActivity extends BaseActivity {
    private ListView sortListView;
    private SideBar sideBar;
    private ChooseNewMasterAdapter adapter;
    private TextView dialog;
    private CharacterParser characterParser;
    private List<Member> dataList = new ArrayList<>();  //传过来的数据
    private List<SortBean> SourceDateList = new ArrayList<>();
    private NewMasterPinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yujian_newgroupmaster);
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new NewMasterPinyinComparator();
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                if (s.contains("☆")) {
                    sortListView.setSelection(0);
                    return;
                }
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });
        sortListView = (ListView) findViewById(R.id.listview);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                Toast.makeText(getApplication(), ((SortBean) adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
                String a = String.format("确定选择%s为新群主，你将自\n动放弃群主身份", ((SortBean) adapter.getItem(position)).getName());
                final DialogUtils.Dialog_Center2 dialogUtils = new DialogUtils.Dialog_Center2(ChooseNewGroupMasterActivity.this);
                final Dialog alertDialog = dialogUtils.Message(a)
                        .Button("取消", "确定").MbuttonOnClick(new DialogUtils.Dialog_Center2.buttonOnClick() {
                            @Override
                            public void onButton1() {
                                dialogUtils.close();
                            }

                            @Override
                            public void onButton2() {
                                finish();
                            }
                        }).create();
                alertDialog.show();
//                LocationUtil.setWidth(ChooseNewGroupMasterActivity.this, alertDialog,
//                        getResources().getDimensionPixelSize(R.dimen.x780));
            }
        });


        //模拟数据
        String[] strings = getResources().getStringArray(R.array.date);
        for (int i = 0; i < strings.length; i++) {
            Member member = new Member();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.cirecleimage_default);
            member.setBitmap(bitmap);
            member.setName(strings[i]);
            dataList.add(member);
        }

        SourceDateList = filledData(dataList);
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new ChooseNewMasterAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);

    }

    private List<SortBean> filledData(List<Member> date) {
        List<SortBean> mSortList = new ArrayList<SortBean>();

        for (int i = 0; i < date.size(); i++) {
            SortBean sortModel = new SortBean();
            sortModel.setName(date.get(i).getName());
            sortModel.setBitmap(date.get(i).getBitmap());
            String pinyin = characterParser.getSelling(date.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;

    }


}
