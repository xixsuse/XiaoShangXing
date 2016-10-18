package com.xiaoshangxing.wo.WoFrafment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.JsonObject;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.PublishNetwork;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.input_activity.InputBoxLayout;
import com.xiaoshangxing.utils.DialogUtils;
import com.xiaoshangxing.utils.LocationUtil;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.MoreTextView;
import com.xiaoshangxing.utils.layout.Name;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImagePagerActivity;
import com.xiaoshangxing.wo.Roll.RollActivity;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import okhttp3.ResponseBody;

/**
 * Created by FengChaoQun
 * on 2016/9/10
 */
public class WoAdapter extends RealmBaseAdapter<Published> {
    private Context context;
    List<Published> publisheds;
    WoFragment woFragment;
    private Activity activity;
    private Handler mHandler;
    Realm realm;

    public WoAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Published> data,
                     WoFragment woFragment, Activity activity, Realm realm) {
        super(context, data);
        this.publisheds = data;
        this.woFragment = woFragment;
        this.activity = activity;
        this.realm = realm;
        mHandler = new Handler();
        this.context = context;
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

        initView(viewHolder, position);
        initOnclick(viewHolder, position);

        return convertView;
    }

    private void initOnclick(final ViewHolder viewHolder, final int position) {
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

        //头像监听
        viewHolder.headImage.setIntent_type(CirecleImage.PERSON_STATE,
                String.valueOf(publisheds.get(position).getUserId()));
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
                        delete(position);
                        center.close();
                    }

                    @Override
                    public void onButton2() {
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
                Intent intent = new Intent(context, RollActivity.class);
                intent.putExtra(RollActivity.TYPE, RollActivity.NOTICE);
                context.startActivity(intent);
            }
        });
    }

    private void initView(ViewHolder viewHolder, int position) {
        Published published = publisheds.get(position);

//      头像  姓名 学院
        String id = String.valueOf(published.getUserId());
        UserInfoCache.getInstance().getHeadIntoImage(id, viewHolder.headImage);
        UserInfoCache.getInstance().getExIntoTextview(id, NS.USER_NAME, viewHolder.name);
        UserInfoCache.getInstance().getExIntoTextview(id, NS.COLLEGE, viewHolder.college);
//        UserCache userCache = new UserCache(context, String.valueOf(published.getUserId()), realm);
//        userCache.getHead(viewHolder.headImage);
//        userCache.getName(viewHolder.name);

//        是否头条
        viewHolder.headline.setVisibility(published.getIsHeadline() == 1 ? View.VISIBLE : View.INVISIBLE);

//      文字内容
        viewHolder.text.setText(published.getText());

//        图片
        parseImages(viewHolder, published.getImage());

//      地点
        viewHolder.location.setText(published.getLocation());

//        时间
        viewHolder.time.setText(TimeUtil.getTimeShowString(published.getCreateTime(), false));

//        显示权限列表
        if (TempUser.isMine(String.valueOf(published.getUserId()))) {
            viewHolder.permission.setVisibility(View.VISIBLE);
            viewHolder.delete.setVisibility(View.VISIBLE);
        } else {
            viewHolder.permission.setVisibility(View.GONE);
            viewHolder.delete.setVisibility(View.GONE);
        }

//        转发
        parseTransmit(viewHolder, published);

//        点赞

        Published_Help.buildPrasiPeople(published, viewHolder.praiseLay, context);



        parseComment(viewHolder, published);

    }

    private void parseImages(final ViewHolder viewHolder, String images) {
        if (TextUtils.isEmpty(images)) {
            viewHolder.justOne.setVisibility(View.GONE);
            viewHolder.photos1.setVisibility(View.GONE);
        } else {
            final ArrayList<String> imageUrls = new ArrayList<>();
            String[] splits = images.split(NS.SPLIT);
            for (int i = 0; i < splits.length; i++) {
                imageUrls.add(splits[i]);
            }
            if (imageUrls.size() > 1) {
                viewHolder.justOne.setVisibility(View.GONE);
                viewHolder.photos1.setVisibility(View.VISIBLE);
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
            } else if (imageUrls.size() == 1) {
                viewHolder.justOne.setVisibility(View.VISIBLE);
                viewHolder.photos1.setVisibility(View.GONE);
                Glide.with(context)
                        .load(imageUrls.get(0))
                        .asBitmap()
                        .placeholder(R.mipmap.greyblock)
                        .error(R.mipmap.nim_image_download_failed)
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
                        justone.add(imageUrls.get(0));
                        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, justone);
                        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    private void parseTransmit(ViewHolder viewHolder, Published published) {
        if (published.getIsTransimit() == 1) {
            viewHolder.transmitContent.setVisibility(View.VISIBLE);
        } else {
            viewHolder.transmitContent.setVisibility(View.GONE);
        }
    }

    private void parseComment(final ViewHolder viewHolder, Published published) {
        //        测试评论
//        if (viewHolder.comments.getChildCount() != 0) {
//            viewHolder.comments.removeAllViews();
//        }
//
//        Item_Comment comment = new Item_Comment(context, "御天证道", "哇哇哇哇哇哇哇哇哇哇哇哇" + "[可爱]");
//        Item_Comment comment2 = new Item_Comment(context, "王振华", "孙璐阳", "和哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈和");
//        viewHolder.comments.addView(comment.getTextView());
//        viewHolder.comments.addView(comment2.getTextView());
//
////       评论
//        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                woFragment.showEdittext(context);
//                final int[] xy = new int[2];
//                v.getLocationOnScreen(xy);
//                final int layoutHeight = viewHolder.commentLayout.getHeight();
//                final View mv = v;
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        int editextLocation = woFragment.get_Editext_Height();
//                        int destination = xy[1] + mv.getHeight() + layoutHeight - editextLocation;
//                        woFragment.moveListview(destination);
//                    }
//                }, 300);
//            }
//        });
//        setCommentListner(comment.getTextView());
//        setCommentListner(comment2.getTextView());
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

    private void delete(final int position) {
        ProgressSubscriberOnNext<ResponseBody> next = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                        case 8001:
                            woFragment.showToast("删除成功");
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    publisheds.get(position).deleteFromRealm();
                                    notifyDataSetChanged();
                                }
                            });
                            break;
                        default:
                            woFragment.showToast(jsonObject.getString(NS.MSG));
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };

        ProgressSubsciber<ResponseBody> subsciber = new ProgressSubsciber<>(next, woFragment);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, TempUser.id);
        jsonObject.addProperty(NS.MOMENTID, publisheds.get(position).getId());
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());
        PublishNetwork.getInstance().deletePublished(subsciber, jsonObject, context);
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
