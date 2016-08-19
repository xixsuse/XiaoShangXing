package com.xiaoshangxing.xiaoshang.idlesale.favoriteIdleFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.xiaoshang.idlesale.IdleBean;
import com.xiaoshangxing.xiaoshang.idlesale.IdleDetailFragment.IdleDetailFragment;
import com.xiaoshangxing.xiaoshang.idlesale.IdleSaleActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by quchwe on 2016/8/9 0009.
 */

public class FavoriteIdleFragMent extends BaseFragment implements FavoriteIdleContract.View,View.OnClickListener {

public static final String  TAG = BaseFragment.TAG+"FavoIdle";

    private FavoriteIdleContract.Presenter mPresenter;
    public static FavoriteIdleFragMent newInstance() {

        Bundle args = new Bundle();

        FavoriteIdleFragMent fragment = new FavoriteIdleFragMent();
        fragment.setArguments(args);
        return fragment;
    }

    List<IdleBean> idles = new ArrayList<>();
    @Bind(R.id.back)
    Button back;
    @Bind(R.id.rv_idle)
    RecyclerView idleRec;
    @Bind(R.id.ib_add)
    ImageView moreImage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_idel_sale,container,false);
        ButterKnife.bind(this,root);
        initList();
        moreImage.setVisibility(View.GONE);
        back.setText("返回");
        FavoriteIdleSaleAdapter adapter = new FavoriteIdleSaleAdapter(getActivity(),this,idles);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        idleRec.setLayoutManager(manager);
        idleRec.setAdapter(adapter);
        idleRec.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemClickListener(new  FavoriteIdleSaleAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                IdleSaleActivity activity = (IdleSaleActivity)getActivity();

                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                R.anim.slide_in_left, R.anim.slide_out_left)
                        .hide(activity.getIdleSaleView())
                        .show(activity.getIdleDetailView())
                        .addToBackStack(IdleDetailFragment.TAG)
                        .commit();
            }

            @Override
            public void onItemLongClickListener(View v, int position) {

            }
        });
        return root;
    }

    @Override
    public void showShare() {

    }

    @Override
    public void showFavoriteDialog() {

    }

    @Override
    public void setmPresenter(@Nullable FavoriteIdleContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
    private void initList(){
        IdleBean id= new IdleBean();
        id.setTime("20分钟前");
        id.setText("这是闲置的说明，最多可以显示三行这是闲置的说明，最多可以显示三行这是闲置的说明，最多可以显示三行这是闲置的说明，最多可以显示三行");
        id.setComplete(false);
        id.setDepartment("李园");
        id.setAcademy("设计学院");
        id.setPrice("1000");
        idles.add(id);
        id.setComplete(true);
        idles.add(id);


    }

    @OnClick(R.id.back)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
