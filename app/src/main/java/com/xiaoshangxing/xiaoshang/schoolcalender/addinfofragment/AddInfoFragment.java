package com.xiaoshangxing.xiaoshang.schoolcalender.addinfofragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.normalUtils.DateUtils;
import com.xiaoshangxing.utils.normalUtils.UpKeyBoard;
import com.xiaoshangxing.xiaoshang.schoolcalender.SchoolCalenderInfoFragment.calender.WeekDayView;
import com.xiaoshangxing.xiaoshang.schoolcalender.addEntranceFragment.AddEntranceFragment;

/**
 * Created by quchwe on 2016/7/22 0022.
 */
public class AddInfoFragment extends BaseFragment implements View.OnClickListener,AddInfoContract.View,TextWatcher {
    public static final String TAG = BaseFragment.TAG+"-AddInfpFragment";
    private Button back;
    private TextView textTitle;
    private Button ib_add;
    private RelativeLayout rl_mytitle;
    private ImageView calender_black;
    private TextView tv_nowDate;
    private TextView tv_date_week;
    private RelativeLayout rl_calenderDate;
    private TextView tv_currentMonth;
    private WeekDayView calender;
    private MonthdateView calender_day;
    private TextView tv_nextMonth;
    private LinearLayout ll_calender;
    private MyEditText editText;
    public static boolean IS_BACKED;


    private AddInfoContract.Presenter mPresenter;
    public static AddInfoFragment newInstance(){
        return new AddInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_add_calender_info,container,false);
        IS_BACKED = true;
        initView(root);
        setmPresenter(new AddInfoPresenter());
        calender_day.setDaysHasThingList(mPresenter.getDaysHaveThings());

        return root;

    }

    private void initView(View root) {

        back = (Button) root.findViewById(R.id.back);

        textTitle = (TextView) root.findViewById(R.id.textTitle);
        textTitle.setText("添加资讯");
        back.setText("返回");
        ib_add = (Button) root.findViewById(R.id.ib_add);
        editText = (MyEditText)root.findViewById(R.id.et_edit_task);
        rl_mytitle = (RelativeLayout) root.findViewById(R.id.rl_mytitle);
        calender_black = (ImageView) root.findViewById(R.id.calender_black);
        tv_nowDate = (TextView) root.findViewById(R.id.tv_nowDate);
        tv_date_week = (TextView) root.findViewById(R.id.tv_date_week);
        tv_nowDate.setText(DateUtils.getCurrentTimeCHN());
        tv_date_week.setText("周"+ DateUtils.getDay());
        rl_calenderDate = (RelativeLayout) root.findViewById(R.id.rl_calenderDate);
        tv_currentMonth = (TextView) root.findViewById(R.id.tv_currentMonth);
        calender = (WeekDayView) root.findViewById(R.id.calender);
        calender_day = (MonthdateView) root.findViewById(R.id.calender_day);
        tv_nextMonth = (TextView) root.findViewById(R.id.tv_nextMonth);
        ll_calender = (LinearLayout) root.findViewById(R.id.ll_calender);
        ib_add.setClickable(false);
        rl_calenderDate.setOnClickListener(this);
        ib_add.setAlpha(0.5f);
        back.setOnClickListener(this);
        ib_add.setOnClickListener(this);
        editText.addTextChangedListener(this);
        calender_day.setTextView(tv_currentMonth,tv_nextMonth,tv_nowDate,tv_date_week);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (ll_calender.getVisibility()==View.VISIBLE) {
                    if (hasFocus) {
                        ll_calender.setVisibility(View.GONE);
                    }
                }
            }
        });
        final Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.calender_down);
        editText.setLayout(ll_calender,a);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();



        UpKeyBoard.autoUpKeyBoard(editText);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                if (editText.getText().toString()!=null&&editText.getText().toString().length()>0){
                    showSureDialog();
                }else getActivity().getSupportFragmentManager().popBackStack(AddEntranceFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                getFragmentManager().popBackStack();
                break;
            case R.id.rl_calenderDate:
                if (ll_calender.getVisibility()==View.GONE){

                    Animation an = AnimationUtils.loadAnimation(getActivity(), R.anim.calender_up);
                    ll_calender.startAnimation(an);
                    ll_calender.setVisibility(View.VISIBLE);
                    UpKeyBoard.hideKeyBoard(editText);
                }else{
                    Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.calender_down);
                    ll_calender.startAnimation(a);
                    a.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            ll_calender.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    ll_calender.startAnimation(a);

                }
                break;
            default:break;
        }
    }

    @Override
    public void showAddTask() {

    }

    @Override
    public void showDateSelect() {

    }

    @Override
    public void setmPresenter(@Nullable AddInfoContract.Presenter presenter) {
        if(presenter==null){
            return;
        }
        this.mPresenter = presenter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }



    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length()>0) {
            ib_add.setAlpha(1f);
            ib_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                LayoutInflater inflater = getActivity().getLayoutInflater();
//                View layout = inflater.inflate(R.layout.add_calender_info_toast,null);
//                ImageView image = (ImageView) layout
//                        .findViewById(R.id.image);
////                image.setImageResource(R.drawable.icon);
//                TextView title = (TextView) layout.findViewById(R.id.text);
////                title.setText("Attention");
////                TextView text = (TextView) layout.findViewById(R.id.tvTextToast);
////                text.setText("n");
//              Toast  toast = new Toast(getActivity());
//                toast.setGravity(Gravity.CENTER,0,0);
//                toast.setDuration(Toast.LENGTH_LONG);
//                toast.setView(layout);
//                toast.show();
                    DialogUtils.Dialog_No_Button dialog_no_button =
                            new DialogUtils.Dialog_No_Button(getActivity(), "已发布");
                    final Dialog notice_dialog = dialog_no_button.create();
                    notice_dialog.show();
                    LocationUtil.setWidth(getActivity(), notice_dialog,
                            getResources().getDimensionPixelSize(R.dimen.x420));
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            notice_dialog.dismiss();
                        }
                    }, 500);
                }
            });
        }else {
            ib_add.setAlpha(0.5f);
            ib_add.setClickable(false);
        }
    }


    private void showDialog(){
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.add_calender_info_dialog);
        Button back = (Button) window.findViewById(R.id.back);
        Button continueEdit = (Button)window.findViewById(R.id.btn_continue);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpKeyBoard.hideKeyBoard(editText);

                getActivity().getSupportFragmentManager().popBackStack(AddEntranceFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                alertDialog.dismiss();
            }
        });
        continueEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //IS_BACKED = true;
        if (KeyEvent.ACTION_DOWN == event.getAction()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (editText.getText().toString() != null&&editText.getText().toString().length()>0) {
                    showSureDialog();
                } else
                    getActivity().getSupportFragmentManager().popBackStack(AddEntranceFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

        }
        return true;
    }

    public void showSureDialog() {
        final DialogUtils.Dialog_Center center = new DialogUtils.Dialog_Center(getActivity());
        center.Message("退出此次编辑?");
        center.Button("退出", "继续编辑");
        center.MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
            @Override
            public void onButton1() {
                getActivity().getSupportFragmentManager().popBackStack(AddEntranceFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                center.close();
            }

            @Override
            public void onButton2() {
                center.close();
            }
        });
        Dialog dialog = center.create();
        dialog.show();
        LocationUtil.setWidth(getActivity(), dialog,
                getResources().getDimensionPixelSize(R.dimen.x780));
    }
}

