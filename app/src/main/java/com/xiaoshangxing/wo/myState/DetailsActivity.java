package com.xiaoshangxing.wo.myState;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.wo.myState.check_photo.myStateNoScrollGridAdapter;
import com.xiaoshangxing.wo.roll.rollActivity;
import com.xiaoshangxing.wo.school_circle.NoScrollGridView;
import com.xiaoshangxing.wo.school_circle.check_photo.ImagePagerActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/7/9
 */
public class DetailsActivity extends BaseActivity {

    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.head_image)
    CirecleImage headImage;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.college)
    TextView college;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.photos1)
    NoScrollGridView photos1;
    @Bind(R.id.just_one)
    ImageView justOne;
    @Bind(R.id.location)
    TextView location;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.delete)
    TextView delete;
    @Bind(R.id.praise)
    CheckBox praise;
    @Bind(R.id.comment)
    ImageView comment;
    @Bind(R.id.praise_people)
    GridLayout praisePeople;
    @Bind(R.id.comments)
    LinearLayout comments;
    @Bind(R.id.comment_layout)
    LinearLayout commentLayout;
    @Bind(R.id.comment_input)
    EditText commentInput;
    @Bind(R.id.emotion)
    ImageView emotion;
    @Bind(R.id.input_layout)
    RelativeLayout inputLayout;
    @Bind(R.id.send)
    TextView send;
    @Bind(R.id.permission)
    ImageView permission;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    private Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
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
        for (int i = 1; i <= 8; i++) {
            imageUrls.add(urls2[i]);
        }


        photos1.setAdapter(new myStateNoScrollGridAdapter(this, imageUrls));
        photos1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DetailsActivity.this, ImagePagerActivity.class);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrls);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                startActivity(intent);
            }
        });

        for (int i = 0; i <= 20; i++) {
            CirecleImage cirecleImage = new CirecleImage(this);
            cirecleImage.setImageResource(R.mipmap.cirecleiamge_default);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.x96),
                    getResources().getDimensionPixelSize(R.dimen.y96));
            cirecleImage.setLayoutParams(params);
            cirecleImage.setPadding(getResources().getDimensionPixelSize(R.dimen.x20), 0, 0, 0);

            cirecleImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(DetailsActivity.this,myStateActivity.class);
                    startActivity(intent);
                }
            });

            praisePeople.addView(cirecleImage);

            final Comment_layout comment_layout = new Comment_layout(this);
            comments.addView(comment_layout.getView());
            comment_layout.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    commentInput.setFocusable(true);
                    commentInput.setFocusableInTouchMode(true);
                    commentInput.requestFocus();
                    InputMethodManager imm = (InputMethodManager) DetailsActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                    final  View mv=v;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int[] xy = new int[2];
                            mv.getLocationOnScreen(xy);
                            int[] xy2 = new int[2];
                            commentInput.getLocationOnScreen(xy2);
                            int destination = xy[1] + mv.getHeight()  - xy2[1];
                            scrollView.smoothScrollBy(0,destination+10);
                        }
                    },300);

                }
            });

                View cirecleImage1=comment_layout.getView().findViewById(R.id.head_image);
                cirecleImage1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(DetailsActivity.this,myStateActivity.class);
                        startActivity(intent);
                    }
                });
        }
        commentInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (commentInput.getText().length() > 0) {
                    send.setBackground(getResources().getDrawable(R.drawable.btn_circular_green1));
                    send.setEnabled(true);
                } else {
                    send.setBackground(getResources().getDrawable(R.drawable.btn_circular_g1));
                    send.setEnabled(false);
                }
            }
        });

    }

    @OnClick({R.id.back, R.id.head_image, R.id.delete, R.id.praise, R.id.comment, R.id.emotion, R.id.send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.head_image:
                break;
            case R.id.delete:
                final DialogUtils.Dialog_Center center = new DialogUtils.Dialog_Center(this);
                center.Message("确定删除吗?");
                center.Button("删除", "取消");
                center.MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
                    @Override
                    public void onButton1() {
                        Toast.makeText(DetailsActivity.this, "delete", Toast.LENGTH_SHORT).show();
                        center.close();
                    }

                    @Override
                    public void onButton2() {
                        Toast.makeText(DetailsActivity.this, "cancle", Toast.LENGTH_SHORT).show();
                        center.close();
                    }
                });
                Dialog dialog = center.create();
                dialog.show();
                LocationUtil.setWidth(this, dialog,
                        getResources().getDimensionPixelSize(R.dimen.x780));
                break;
            case R.id.praise:
                break;
            case R.id.comment:
                commentInput.setFocusable(true);
                commentInput.setFocusableInTouchMode(true);
                commentInput.requestFocus();
                InputMethodManager imm = (InputMethodManager) DetailsActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.emotion:
                break;
            case R.id.send:
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(commentInput.getWindowToken(), 0);
                commentInput.setText("");
                break;
        }
    }

    @OnClick(R.id.permission)
    public void onClick() {
        Intent intent = new Intent(this, rollActivity.class);
        intent.putExtra("type", rollActivity.NOTICE);
        startActivity(intent);
    }
}
