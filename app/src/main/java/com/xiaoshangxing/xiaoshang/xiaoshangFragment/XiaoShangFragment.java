package com.xiaoshangxing.xiaoshang.XiaoShangFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.xiaoshang.Calendar.CalendarActivity;
import com.xiaoshangxing.xiaoshang.Help.HelpActivity;
import com.xiaoshangxing.xiaoshang.MessageNotice.MessageNoticeActivity;
import com.xiaoshangxing.xiaoshang.Plan.PlanActivity;
import com.xiaoshangxing.xiaoshang.Reward.RewardActivity;
import com.xiaoshangxing.xiaoshang.Sale.SaleActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/7/18
 */
public class XiaoShangFragment extends BaseFragment {
    public static final String TAG = BaseFragment.TAG + "-XiaoShangFragment";

    @Bind(R.id.tuch)
    ImageView tuch;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;

    private float current;
    private int currentImage = 1;

    private int image_width, divider, padding_start, total, tuchlength;
    private ViewPagerAdapter viewAdapter;
    private Transformer transformer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_xiaoshang, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setImagePosition(currentImage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void init() {
        setDefaultPopFragment(false);
        leftText.setVisibility(View.GONE);
        leftImage.setImageResource(R.mipmap.xiaoshang_notice);
        titleBottomLine.setVisibility(View.GONE);
        more.setVisibility(View.GONE);

        setImagePosition(currentImage);
        image_width = getResources().getDimensionPixelSize(R.dimen.x808);
        divider = getResources().getDimensionPixelSize(R.dimen.x48);
        padding_start = getResources().getDimensionPixelSize(R.dimen.x136);
        total = image_width * 5 + divider * 4 + padding_start * 2;

        tuch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tuchlength = tuch.getWidth();
                final int item = tuchlength / 5;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        current = event.getX();
                        int des1 = (int) current;
                        if (des1 <= item) {
                            viewPager.setCurrentItem(0);
                        } else if (des1 <= item * 2) {
                            viewPager.setCurrentItem(1);
                        } else if (des1 <= item * 3) {
                            viewPager.setCurrentItem(2);
                        } else if (des1 <= item * 4) {
                            viewPager.setCurrentItem(3);
                        } else if (des1 <= item * 5) {
                            viewPager.setCurrentItem(4);
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        instantSetImage((int) (event.getX()));
                        break;
                    case MotionEvent.ACTION_UP:
                        final int des = (int) event.getX();
                        if (des <= item) {
                            viewPager.setCurrentItem(0);
                        } else if (des <= item * 2) {
                            viewPager.setCurrentItem(1);
                        } else if (des <= item * 3) {
                            viewPager.setCurrentItem(2);
                        } else if (des <= item * 4) {
                            viewPager.setCurrentItem(3);
                        } else if (des <= item * 5) {
                            viewPager.setCurrentItem(4);
                        }
                }
                return false;
            }
        });
        initViewPager();
        leftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showToast(NS.ON_DEVELOPING);
                Intent notice_intent = new Intent(getContext(), MessageNoticeActivity.class);
                startActivity(notice_intent);
            }
        });
    }

    private void initViewPager() {
        viewAdapter = new ViewPagerAdapter(this);
        transformer = new Transformer(viewPager, viewAdapter);
        viewPager.setAdapter(viewAdapter);
        viewPager.setPageTransformer(false, transformer);
        viewPager.setOffscreenPageLimit(3);
        transformer.enableScaling(true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setImagePosition(position + 1);
                currentImage = position + 1;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static XiaoShangFragment newInstance() {
        return new XiaoShangFragment();
    }

    private void instantSetImage(int loaction) {
        tuchlength = tuch.getWidth();
        final int item = tuchlength / 5;
        if (loaction <= item) {
            viewPager.setCurrentItem(0);
        } else if (loaction <= item * 2) {
            viewPager.setCurrentItem(1);
        } else if (loaction <= item * 3) {
            viewPager.setCurrentItem(2);
        } else if (loaction <= item * 4) {
            viewPager.setCurrentItem(3);
        } else if (loaction <= item * 5) {
            viewPager.setCurrentItem(4);
        }
    }

    private void setImagePosition(int position) {
        switch (position) {
            case 1:
                tuch.setImageResource(R.mipmap.xiaoshang_select1);
                title.setText("校历资讯");
                break;
            case 2:
                tuch.setImageResource(R.mipmap.xiaoshang_select2);
                title.setText("校内悬赏");
                break;
            case 3:
                tuch.setImageResource(R.mipmap.xiaoshang_select3);
                title.setText("校友互帮");
                break;
            case 4:
                tuch.setImageResource(R.mipmap.xiaoshang_select4);
                title.setText("计划发起");
                break;
            case 5:
                tuch.setImageResource(R.mipmap.xiaoshang_select5);
                title.setText("闲置出售");
                break;
        }

    }

    public void gotoOther(int position) {
        switch (position) {
            case 1:
                Intent schoolCalender = new Intent(getContext(), CalendarActivity.class);
                getContext().startActivity(schoolCalender);
                break;
            case 2:
                Intent rewaed_intent = new Intent(getContext(), RewardActivity.class);
                getContext().startActivity(rewaed_intent);
                break;
            case 3:
                Intent help_intent = new Intent(getContext(), HelpActivity.class);
                getContext().startActivity(help_intent);
                break;
            case 4:
                Intent plan = new Intent(getContext(), PlanActivity.class);
                getContext().startActivity(plan);
                break;
            case 5:
                Intent idleSale = new Intent(getContext(), SaleActivity.class);
                getActivity().startActivity(idleSale);
                break;

        }
    }

}
