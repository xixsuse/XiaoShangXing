package com.xiaoshangxing.setting.shiming.chooseschool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.CharacterParser;
import com.xiaoshangxing.SelectPerson.PinyinComparator;
import com.xiaoshangxing.SelectPerson.SideBar;
import com.xiaoshangxing.SelectPerson.SortModel;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tianyang on 2016/9/19.
 */
public class DiQUActivity extends BaseActivity {
    @Bind(R.id.view1)
    CardView view1;
    @Bind(R.id.view2)
    LinearLayout view2;
    @Bind(R.id.editText)
    EditText editText;

    private ListView sortListView;
    private SideBar sideBar;
    private SortAdapter adapter;
    private TextView dialog;
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    private List<String> citys = new ArrayList<>();
    private List<SortModel> SourceDateList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_diqu);
        ButterKnife.bind(this);

        citys = Arrays.asList(getResources().getStringArray(R.array.date));

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
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

        SourceDateList = filledData(citys);
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);

        //根据输入框输入值的改变来过滤搜索
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplication(), adapter.getItem(position).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DiQUActivity.this,SchoolActivity.class);
                intent.putExtra("city",adapter.getItem(position).getName());
                startActivity(intent);
                finish();
            }
        });


    }


    private List<SortModel> filledData(List<String> date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();
        for (int i = 0; i < date.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(NimUserInfoCache.getInstance().getUserDisplayName(date.get(i)));
            sortModel.setAccount(date.get(i));
            String pinyin = characterParser.getSelling(sortModel.getName());
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

    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.toUpperCase().indexOf(
                        filterStr.toString().toUpperCase()) != -1
                        || characterParser.getSelling(name).toUpperCase()
                        .startsWith(filterStr.toString().toUpperCase())) {
                    filterDateList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);

    }


    public void Back(View view) {
        finish();
    }

    public void SearchView(View view) {
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.VISIBLE);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.findFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void Cancel(View view) {
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        adapter.updateListView(SourceDateList);
        editText.setText("");
    }
}
