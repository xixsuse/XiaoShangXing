package com.xiaoshangxing.Network.netUtil;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xiaoshangxing.data.Published;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by FengChaoQun
 * on 2016/9/16
 */
public class LoadByPager {
    private int pager;
    private int anchor;
    private RealmResults<Published> publisheds;
    private List<Published> pager_publisheds = new ArrayList<>();
    private int selection;//记录listview的位置
    private ListView listView;
    private ArrayAdapter adapter;

}
