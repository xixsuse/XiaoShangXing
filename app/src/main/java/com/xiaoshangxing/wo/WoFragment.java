package com.xiaoshangxing.wo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.loadingview.DotsTextView;
import com.xiaoshangxing.utils.pull_refresh.PtrDefaultHandler;
import com.xiaoshangxing.utils.pull_refresh.PtrFrameLayout;
import com.xiaoshangxing.utils.pull_refresh.PtrHandler;
import com.xiaoshangxing.utils.pull_refresh.StoreHouseHeader;
import com.xiaoshangxing.wo.school_circle.Wo_listview_adpter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/7/3
 */
public class WoFragment extends BaseFragment {
    public static final String TAG = BaseFragment.TAG + "-WoFragment";
    private View mView,divider_line,headView,footerview;
    private ListView listView;
    private PtrFrameLayout ptrFrameLayout;
    private String[] str;
   private List<String> list=new ArrayList<String>();
    private int location=0;
    private ArrayAdapter<String> arrayAdapter;
    private Wo_listview_adpter adpter;

    public static WoFragment newInstance() {
        return new WoFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_wo, container, false);
        mView = view;


        initView();
        return view;
    }

    private void initView(){
        listView=(ListView)mView.findViewById(R.id.listview);
        ptrFrameLayout=(PtrFrameLayout)mView.findViewById(R.id.reflesh_layout);
        divider_line=mView.findViewById(R.id.line);

        //设置头
        headView=getActivity().getLayoutInflater().inflate(R.layout.util_wo_header,null);
        listView.addHeaderView(headView);

        footerview=getActivity().getLayoutInflater().inflate(R.layout.util_wo_footer,null);
        listView.addFooterView(footerview);




        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction()==MotionEvent.ACTION_UP){
                    divider_line.setVisibility(View.VISIBLE);
                    return false;
                }
                return false;
            }
        });



        for (int i=0;i<=14;i++){
            list.add("hhhh"+i);
        }
        arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list);
        adpter=new Wo_listview_adpter(getContext(),R.layout.item_wo_listview,list);
        listView.setAdapter(adpter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (totalItemCount-(firstVisibleItem+visibleItemCount-1)<=5){
//                    for (int i=15;i<=29;i++){
//                        list.add("yyyy"+i);
//                    }
//                    arrayAdapter.notifyDataSetChanged();
//                }
//                Log.d("total",""+(firstVisibleItem+visibleItemCount-1));
                if (firstVisibleItem+visibleItemCount==totalItemCount){
                    DotsTextView dotsTextView=(DotsTextView)footerview.findViewById(R.id.dot);
                    dotsTextView.start();
                }
            }
        });
        initFresh();

        footerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i=15;i<=29;i++){
                    list.add("yyyy"+i);
                }
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initFresh(){
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.setPadding(0,20,0,20);
        header.initWithString("SWALK");
        header.setTextColor(getResources().getColor(R.color.green1));
        header.setBackgroundColor(getResources().getColor(R.color.w0));

        header.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fade_in));

        ptrFrameLayout.setDurationToCloseHeader(2000);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
//        ptrFrameLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ptrFrameLayout.autoRefresh(false);
//            }
//        }, 100);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                divider_line.setVisibility(View.INVISIBLE);
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 1500);
            }
        });
    }
}
