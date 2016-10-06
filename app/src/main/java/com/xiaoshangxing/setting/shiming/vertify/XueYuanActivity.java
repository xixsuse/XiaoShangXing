package com.xiaoshangxing.setting.shiming.vertify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;

/**
 * Created by tianyang on 2016/10/5.
 */
public class XueYuanActivity extends BaseActivity {
    private ListView mListView;
    private String[] strings;
    private ArrayAdapter mAdapter;
    private TextView next;
    private static String xueyuan; //学院
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_xueyuan);

        mListView = (ListView) findViewById(R.id.list);
        next = (TextView) findViewById(R.id.next);
        editText = (EditText) findViewById(R.id.edittext);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        strings = getResources().getStringArray(R.array.Year);
        mAdapter = new ArrayAdapter<String>(this, R.layout.item_nodisturb, strings);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                next.setEnabled(true);
                next.setAlpha(1);
                editText.setText(strings[position]);
            }
        });

    }

    public void Back(View view) {
        finish();
    }

    public void Next(View view) {
        xueyuan = editText.getText().toString();
        startActivity(new Intent(this, ZhuanYeActivity.class));
        Log.d("qqq", "xueyuan   " + xueyuan);
    }


}