package com.xiaoshangxing.wo.WoFrafment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.input_activity.InputBoxLayout;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.MoreTextView;
import com.xiaoshangxing.utils.layout.Name;
import com.xiaoshangxing.utils.school_circle.Item_Comment;
import com.xiaoshangxing.utils.school_circle.PraisePeople;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImagePagerActivity;
import com.xiaoshangxing.wo.roll.rollActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class Wo_listview_adpter extends ArrayAdapter<String> {
    private Context context;
    private int resource;
    List<String> strings;
    WoFragment woFragment;
    private Activity activity;
    private Handler mHandler;

    public Wo_listview_adpter(Context context, int resource, List<String> objects, WoFragment woFragment, Activity activity) {
        super(context, resource, objects);
        this.context = context;
        this.strings = objects;
        this.resource = resource;
        this.woFragment = woFragment;
        this.activity = activity;
        mHandler = new Handler();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_wo_listview, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        initView(viewHolder);
        initOnclick(viewHolder);


        //测试图片
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
        for (int i = 1; i <= 5; i++) {
            imageUrls.add(urls2[i]);
        }

        viewHolder.photos1.setAdapter(new NoScrollGridAdapter(context, imageUrls));
        viewHolder.photos1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ImagePagerActivity.class);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrls);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                context.startActivity(intent);
            }
        });

        if (viewHolder.praisePeople.getChildCount() != 0) {
            viewHolder.praisePeople.removeAllViews();
        }

        viewHolder.justOne.setImageResource(R.mipmap.cirecleimage_default);
        Glide.with(context)
                .load(R.mipmap.test)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (resource.getHeight() > context.getResources().getDimensionPixelSize(R.dimen.y600)) {
                            viewHolder.justOne.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    context.getResources().getDimensionPixelSize(R.dimen.y600)));
                        }
                        viewHolder.justOne.setImageBitmap(resource);
                    }
                });
        viewHolder.justOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImagePagerActivity.class);
                ArrayList<String> justone = new ArrayList<String>();
                justone.add("http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg");
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, justone);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                context.startActivity(intent);
            }
        });
//        测试图片及转发显示
        Random random = new Random();
        int ran = random.nextInt(3);
        switch (ran) {
            case 0:
                viewHolder.photos1.setVisibility(View.VISIBLE);
                viewHolder.justOne.setVisibility(View.GONE);
                viewHolder.transmitContent.setVisibility(View.GONE);
                break;
            case 1:
                viewHolder.photos1.setVisibility(View.GONE);
                viewHolder.justOne.setVisibility(View.VISIBLE);
                viewHolder.transmitContent.setVisibility(View.GONE);
                break;
            case 2:
                viewHolder.photos1.setVisibility(View.GONE);
                viewHolder.justOne.setVisibility(View.GONE);
                viewHolder.transmitContent.setVisibility(View.VISIBLE);
                break;
        }
//        测试点赞的人
        PraisePeople praisePeople = new PraisePeople(context);
        praisePeople.addName("冯超群");
        praisePeople.addName("吴天阳");
        praisePeople.addName("王振华");
        praisePeople.addName("徐莹");
        praisePeople.addName("孙璐阳");
        praisePeople.addName("御天证道");
        praisePeople.addName("御天证道");
        praisePeople.addName("御天证道");
        viewHolder.praisePeople.addView(praisePeople.getTextView());

//        测试评论
        if (viewHolder.comments.getChildCount() != 0) {
            viewHolder.comments.removeAllViews();
        }

        Item_Comment comment = new Item_Comment(context, "御天证道", "哇哇哇哇哇哇哇哇哇哇哇哇" + "[可爱]");
        Item_Comment comment2 = new Item_Comment(context, "王振华", "孙璐阳", "和哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈和");
        viewHolder.comments.addView(comment.getTextView());
        viewHolder.comments.addView(comment2.getTextView());

//       评论
        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                woFragment.showEdittext(context);
                final int[] xy = new int[2];
                v.getLocationOnScreen(xy);
                final int layoutHeight = viewHolder.commentLayout.getHeight();
                final View mv = v;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int editextLocation = woFragment.get_Editext_Height();
                        int destination = xy[1] + mv.getHeight() + layoutHeight - editextLocation;
                        woFragment.moveListview(destination);
                    }
                }, 300);
            }
        });
//        comment.getTextView().setOnClickListener(showEdittext);
//        comment2.getTextView().setOnClickListener(showEdittext);
        setCommentListner(comment.getTextView());
        setCommentListner(comment2.getTextView());

        //头像监听
        viewHolder.headImage.setIntent_type(CirecleImage.PERSON_STATE);
//        删除
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogUtils.Dialog_Center center = new DialogUtils.Dialog_Center(context);
                center.Message("确定删除吗?");
                center.Button("删除", "取消");
                center.MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
                    @Override
                    public void onButton1() {
                        Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                        center.close();
                    }

                    @Override
                    public void onButton2() {
                        Toast.makeText(context, "cancle", Toast.LENGTH_SHORT).show();
                        center.close();
                    }
                });
                Dialog dialog = center.create();
                dialog.show();
                LocationUtil.setWidth(activity, dialog,
                        context.getResources().getDimensionPixelSize(R.dimen.x780));
            }
        });

        viewHolder.permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, rollActivity.class);
                intent.putExtra(rollActivity.TYPE, rollActivity.NOTICE);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private void initOnclick(final ViewHolder viewHolder) {
        viewHolder.commentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.comment.performClick();
            }
        });
        viewHolder.praiseLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.praise.performClick();
            }
        });
    }

    private void initView(ViewHolder viewHolder) {
        viewHolder.text.setText(context.getString(R.string.longtext) + context.getString(R.string.longtext));

    }

    private void setCommentListner(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEdittext(v);
                woFragment.setEditCallback(new InputBoxLayout.CallBack() {
                    @Override
                    public void callback(String text) {
                        Log.d("callback", "pp");
                    }
                });
            }
        });
    }

    private void showEdittext(View v) {
        woFragment.showEdittext(context);
        final int[] xy = new int[2];
        v.getLocationOnScreen(xy);
        final View mv = v;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int editextLocation = woFragment.get_Editext_Height();
                int destination = xy[1] + mv.getHeight() - editextLocation;
                woFragment.moveListview(destination);
            }
        }, 300);
        woFragment.setEditCallback(new InputBoxLayout.CallBack() {
            @Override
            public void callback(String text) {

            }
        });
    }

    static class ViewHolder {
        @Bind(R.id.head_image)
        CirecleImage headImage;
        @Bind(R.id.name)
        Name name;
        @Bind(R.id.college)
        TextView college;
        @Bind(R.id.text)
        MoreTextView text;
        @Bind(R.id.photos1)
        NoScrollGridView photos1;
        @Bind(R.id.just_one)
        ImageView justOne;
        @Bind(R.id.transmit_image)
        CirecleImage transmitImage;
        @Bind(R.id.transmit_text)
        EmotinText transmitText;
        @Bind(R.id.transmit_content)
        LinearLayout transmitContent;
        @Bind(R.id.location)
        TextView location;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.permission)
        ImageView permission;
        @Bind(R.id.delete)
        TextView delete;
        @Bind(R.id.praise)
        CheckBox praise;
        @Bind(R.id.praise_lay)
        LinearLayout praiseLay;
        @Bind(R.id.comment)
        ImageView comment;
        @Bind(R.id.comment_lay)
        LinearLayout commentLay;
        @Bind(R.id.praise_people)
        LinearLayout praisePeople;
        @Bind(R.id.comments)
        LinearLayout comments;
        @Bind(R.id.comment_layout)
        LinearLayout commentLayout;
        @Bind(R.id.headline)
        ImageView headline;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
