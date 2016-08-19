package com.xiaoshangxing.xiaoshang.schoolcalender.SchoolCalenderInfoFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Bean;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.normalUtils.DateUtils;
import com.xiaoshangxing.utils.normalUtils.ToastUtil;
import com.xiaoshangxing.xiaoshang.RecyclerViewUtil;
import com.xiaoshangxing.xiaoshang.schoolcalender.SchoolCalenderActivity;
import com.xiaoshangxing.xiaoshang.schoolcalender.SchoolCalenderInfoFragment.calender.MonthDateView;
import com.xiaoshangxing.xiaoshang.schoolcalender.addEntranceFragment.AddEntranceFragment;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by quchwe on 2016/7/22 0022.
 */
public class CalenderInfoFragment extends BaseFragment implements CalenderInfoContract.View,View.OnClickListener{

    public static final String TAG = BaseFragment.TAG ;
    private CalenderInfoContract.Presenter mPrenster;
    private CalenderInfoAdapter mAdapter;
    private TextView nowDate;
    private TextView nowWeek;
    LinearLayout calenderLayout;
    RelativeLayout calenderDate;
    //    @BindView(R.id.rv_calender_info)
    RecyclerView infoView;
    //    @BindView(R.id.textTitle)
    TextView title;
    //    @BindView(R.id.back)
    Button back;
    //    @BindView(R.id.ib_add)
    ImageButton add;
    //    @BindView(R.id.tv_currentMonth)
    TextView currentMonth;
    //    @BindView(R.id.tv_nextMonth)
    TextView nextMonth;
    //    @BindView(R.id.ib_arrow)
    ImageButton arrowUp;
    ImageButton arrowDown;
    //    @BindView(R.id.calender_day)
    MonthDateView dateView;
    private RelativeLayout myLL;
    List<ZiXunInfo>  myzixun = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frg_calender_info, container, false);
        setmPresenter(new CalenderInfoPresenter(new Bean(),this));
//        ButterKnife.bind(root);
        Log.d("oncreat","create");
        infoView = (RecyclerView)root.findViewById(R.id.rv_calender_info);

        arrowUp = (ImageButton)root.findViewById(R.id.ib_arrow_up);
        arrowDown = (ImageButton)root.findViewById(R.id.ib_arrow);
        add = (ImageButton)root.findViewById(R.id.ib_add);
        calenderLayout = (LinearLayout)root.findViewById(R.id.ll_calender);
        back = (Button)root.findViewById(R.id.back);
        back.setOnClickListener(this);
        nowDate = (TextView)root.findViewById(R.id.tv_nowDate);
        nowWeek = (TextView)root.findViewById(R.id.tv_date_week);
        currentMonth =(TextView)root.findViewById(R.id.tv_currentMonth);
        nextMonth = (TextView)root.findViewById(R.id.tv_nextMonth);
        dateView = (MonthDateView)root.findViewById(R.id.calender_day);
        dateView.setDaysHasThingList(mPrenster.getDayHasThingsList());
        calenderDate = (RelativeLayout)root.findViewById(R.id.rl_calenderDate);
        myLL = (RelativeLayout)root.findViewById(R.id.ll_myll);
        dateView.setTextView(currentMonth,nextMonth,nowDate,nowWeek);
        nowDate.setText(DateUtils.getCurrentTimeCHN());
        nowWeek.setText("周"+DateUtils.getDay());
        setRvHeight();

        arrowDown.setOnClickListener(this);

        arrowUp.setOnClickListener(this);
        add.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);



        final List<ZiXunInfo> ziXunInfos = new ArrayList<>();

        for (ZiXunInfo z:mPrenster.getTask())
        {
            if (z.numberOfLegs()<getTime()){
                z.setMipMap(getContext().getResources().getColor(R.color.g0));

            }else if (z.numberOfLegs()==getTime()){
                z.setMipMap(getContext().getResources().getColor(R.color.green1));
            }else {
                z.setMipMap(getContext().getResources().getColor(R.color.blue1));
            }
            ziXunInfos.add(z);
        }
        if (myzixun.size()>0){
            myzixun.clear();
        }
        for (ZiXunInfo z:ziXunInfos){
            if (z.numberOfLegs()==Integer.valueOf(DateUtils.getCurrentTimeInteger())){
                myzixun.add(z);
            }
        }
        mAdapter = new CalenderInfoAdapter(getContext(),myzixun);
        infoView.setLayoutManager(linearLayoutManager);
        infoView.setAdapter(mAdapter);
        showCalenderInfo();
        showInfo();
        dateView.setDateClick(new MonthDateView.DateClick() {

            @Override
            public void onClickOnDate() {
                if (mPrenster.getDayHasThingsList().contains(getTime())){
                    List<ZiXunInfo> zixu = new ArrayList<ZiXunInfo>();
                    myzixun.clear();

                    Log.d(""+ziXunInfos.size(),getTime()+"");
                    for (ZiXunInfo z:ziXunInfos){
                        if (z.numberOfLegs()==getTime()){
                            myzixun.add(z);
                            Log.d("添加成功","添加");
                        }
                    }
                    Log.d("uqyn",""+myzixun.size());
                    mAdapter.notifyDataSetChanged();
                }else showNoInfos();

            }
        });
        return root;
    }

    public static CalenderInfoFragment getInstance(){
        return new CalenderInfoFragment();
    }
    @Override
    public void showCurrentTask(List<ZiXunInfo> tasks) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showInfo() {
        mAdapter.setOnItemClickListener(new RecyclerViewUtil.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                String data = myzixun.get(position).name();
                Intent intent = new Intent(getActivity(), CalenderInfoFullScreen.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }

            @Override
            public void onItemLongClickListener(View view, int position) {

            }
        });


    }

    @Override
    public void showLoadingProgress() {

    }

    @Override
    public void showCompleteShow() {

    }

    @Override
    public void showCalenderInfo() {
        if (myzixun.size()==0){
            showNoInfos();
        }else mAdapter.notifyDataSetChanged();
    }



    @Override
    public void showNoInfos() {
        myzixun.clear();
        mAdapter.notifyDataSetChanged();
        ToastUtil.show(getActivity(),"这天没有校历资讯", Toast.LENGTH_SHORT);
    }


    @Override
    public void setmPresenter(@Nullable CalenderInfoContract.Presenter presenter) {
        mPrenster = presenter;
    }

    @Override
    public void onClick(View v) {
        AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
//        TranslateAnimation translateAnimation = new TranslateAnimation()
        appearAnimation.setDuration(800);

        AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
        disappearAnimation.setDuration(800);
        switch (v.getId()){
            case R.id.back:
                getActivity().finish();
                break;

            case R.id.ib_arrow_up:
                Animation an = AnimationUtils.loadAnimation(getActivity(), R.anim.calender_up);
                calenderLayout.startAnimation(an);
                calenderLayout.setVisibility(View.VISIBLE);
                calenderDate.setVisibility(View.GONE);
                setRvHeight();
                break;
            case R.id.ib_arrow:
                Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.calender_down);
                calenderDate.startAnimation(a);
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        calenderLayout.setVisibility(View.GONE);
                        setRvHeight();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                calenderLayout.startAnimation(a);
                calenderDate.setVisibility(View.VISIBLE);


                break;
            case R.id.ib_add:
                AddEntranceFragment frag = ((SchoolCalenderActivity)mActivity).getAddEntranceFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                R.anim.slide_in_left, R.anim.slide_out_left)
                        .replace(R.id.fl_school_calender, frag, AddEntranceFragment.TAG)
                        .addToBackStack(AddEntranceFragment.TAG)
                        .commit();

        }
    }
    private void setRvHeight(){
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (calenderLayout.getVisibility()==View.GONE){
            ViewGroup.LayoutParams layoutParams = infoView.getLayoutParams();
            int rvHeight = dm.heightPixels-366;
            layoutParams.height = rvHeight;
            infoView.setLayoutParams(layoutParams);
            infoView.invalidate();
        }else {
            ViewGroup.LayoutParams layoutParams = infoView.getLayoutParams();
            int rvHeight = dm.heightPixels-1250;
            layoutParams.height = rvHeight;
            infoView.setLayoutParams(layoutParams);
            infoView.invalidate();
        }
    }




    private int getTime(){
        int date;
        String dateString = dateView.getmSelYear()+"";
        if (dateView.getmSelMonth()<9){
            dateString += "0"+(dateView.getmSelMonth()+1);

        }else dateString+=""+(dateView.getmSelMonth()+1);

        if (dateView.getmSelDay()<10){
            dateString+="0"+dateView.getmSelDay();
        }else dateString+=dateView.getmSelDay();

        return Integer.valueOf(dateString);
    }

}
