package com.xiaoshangxing.xiaoshang.idlesale.IdleDetailFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.xiaoshang.idlesale.IdleImageAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by quchwe on 2016/8/5 0005.
 */
public class IdleDetailFragment extends BaseFragment implements IdleDetailContract.View,View.OnClickListener{

    public static final String TAG = BaseFragment.TAG+"-IdleDetail";

    private IdleDetailContract.Presenter mPresenter;


    public static IdleDetailFragment newInstance() {

        Bundle args = new Bundle();

        IdleDetailFragment fragment = new IdleDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    Button back;
    @Bind(R.id.iv_headImage)
    CirecleImage headImage;
    @Bind(R.id.tv_name)
    TextView name;
    @Bind(R.id.tv_time)
    TextView time;
    @Bind(R.id.tv_academy)
    TextView academy;
    @Bind(R.id.iv_favorite)
    ImageView favorite;
    @Bind(R.id.tv_idle_info)
    TextView idleInfo;
    @Bind(R.id.ll_dormitory)
    LinearLayout dornitory;
    @Bind(R.id.iv_idle_showComplete)
    ImageView showComplete;
    @Bind(R.id.btn_whispher)
    Button whispher;
    @Bind(R.id.rv_image)
    RecyclerView image;
    @Bind(R.id.et_edit)
    EditText talk;
    @Bind(R.id.ib_add)
    ImageView more;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_idle_detail,container,false);
        ButterKnife.bind(this,root);
//        back = (Button) root.findViewById(R.id.back);
//        headImage = (CirecleImage) root.findViewById(R.id.iv_headImage);
//        name = (TextView) root.findViewById(R.id.tv_name);
//        time = (TextView)root.findViewById(R.id.tv_time);
//        academy = (TextView)root.findViewById(R.id.tv_academy);
//        favorite = (ImageView)root.findViewById(R.id.iv_favorite);
//        idleInfo = (TextView)root.findViewById(R.id.tv_idle_info);
//        more = (ImageView)root.findViewById(R.id.ib_add);
//        showComplete = (ImageView) root.findViewById(R.id.iv_idle_showComplete);
//        talk = (EditText)root.findViewById(R.id.et_edit);
//        dornitory = (LinearLayout)root.findViewById(R.id.ll_dormitory);
//        whispher = (Button) root.findViewById(R.id.btn_whispher);
//        image = (ImageView)root.findViewById(R.id.iv_image);

        String[] urls2 = {"http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg",
                "http://img.my.csdn.net/uploads/201410/19/1413698867_8323.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383264_3954.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383264_4787.jpg"
        };
        final ArrayList<String> imageUrls = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            imageUrls.add(urls2[i]);
        }
        IdleImageAdapter adapter = new IdleImageAdapter(getContext(),imageUrls,R.layout.rv_idle_detail_image);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        image.setLayoutManager(manager);
        image.setAdapter(adapter);
        image.setItemAnimator(new DefaultItemAnimator());


//        back.setOnClickListener(this);
//        more.setOnClickListener(this);

        talk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>0){
                    whispher.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_xianzi_shiliao_green));
                    whispher.setClickable(true);
                }else{
                    whispher.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_xianzi_shiliao));
                    whispher.setClickable(false);
                }

            }
        });
        return root;
    }

    @OnClick({R.id.back,R.id.ib_add})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.ib_add:
                showShareDialog();
        }
    }


    @Override
    public void showPop() {

    }

    @Override
    public void showShareDialog() {
       final DialogUtils.DialogShowShareOtherSdk showShareOtherSdk = new DialogUtils.DialogShowShareOtherSdk(getContext(), true, new DialogUtils.DialogShowShareOtherSdk.OnShareListener() {
            @Override
            public void onShareFriends() {

            }

            @Override
            public void onShareXyq() {
                Intent intent = new Intent(getContext(), InputActivity.class);
                intent.putExtra(InputActivity.EDIT_STATE, InputActivity.PUBLISH_STATE);
                startActivity(intent);
            }

            @Override
            public void onShareQQ() {

            }

            @Override
            public void onShareWeiChat() {

            }

            @Override
            public void onShareWeibo() {

            }
        });
        LocationUtil.bottom_FillWidth(getActivity(),showShareOtherSdk);
    }

    @Override
    public void showShareXYQDialog() {

    }


    @Override
    public void setmPresenter(@NonNull IdleDetailContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
