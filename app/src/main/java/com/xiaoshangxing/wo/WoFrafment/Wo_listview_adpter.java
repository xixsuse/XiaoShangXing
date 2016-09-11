package com.xiaoshangxing.wo.WoFrafment;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.xiaoshangxing.Network.NS;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.wo.WoFrafment.WoViewHolder.WoBaseHolder;
import com.xiaoshangxing.wo.WoFrafment.WoViewHolder.WoJustOneImage;
import com.xiaoshangxing.wo.WoFrafment.WoViewHolder.WoMoreImage;
import com.xiaoshangxing.wo.WoFrafment.WoViewHolder.WoNoImages;
import com.xiaoshangxing.wo.WoFrafment.WoViewHolder.WoTrasnsmit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class Wo_listview_adpter extends ArrayAdapter<Published> {
    private Context context;
    private int resource;
    List<Published> publisheds;
    WoFragment woFragment;
    private BaseActivity activity;
    private Handler mHandler;
    Realm realm;
    private final Map<Class<?>, Integer> viewTypes;

    public Wo_listview_adpter(Context context, int resource, List<Published> objects,
                              WoFragment woFragment, BaseActivity activity, Realm realm) {
        super(context, resource, objects);
        this.context = context;
        this.publisheds = objects;
        this.resource = resource;
        this.woFragment = woFragment;
        this.activity = activity;
        this.realm = realm;
        mHandler = new Handler();
        this.viewTypes = new HashMap<Class<?>, Integer>(getViewTypeCount());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        WoBaseHolder woBaseHolder;
        if (convertView == null) {
            convertView = viewAtPosition(position);
        }
        woBaseHolder = (WoBaseHolder) convertView.getTag();
        woBaseHolder.refresh(publisheds.get(position));
        return convertView;
    }


    @Override
    public int getItemViewType(int position) {

        if (getViewTypeCount() == 1) {
            return 0;
        }

        Class<?> clazz = viewHolderAtPosition(position);
        if (viewTypes.containsKey(clazz)) {
            return viewTypes.get(clazz);
        } else {
            int type = viewTypes.size();
            if (type < getViewTypeCount()) {
                viewTypes.put(clazz, type);
                return type;
            }
            return 0;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }


    public View viewAtPosition(int position) {
        WoBaseHolder holder = null;
        View view = null;
        try {
            Class<?> viewHolder = viewHolderAtPosition(position);
            holder = (WoBaseHolder) viewHolder.newInstance();
            holder.setActivity(activity);
            holder.setWoFragment(woFragment);
            holder.setRealm(realm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        view = holder.getView(LayoutInflater.from(context));
        view.setTag(holder);
        holder.setContext(view.getContext());
        Log.d("new view", "----ok");
        return view;
    }

    private Class<? extends WoBaseHolder> viewHolderAtPosition(int position) {
        if (publisheds.get(position).getIsTransimit() == 1) {
            return WoTrasnsmit.class;
        } else if (TextUtils.isEmpty(publisheds.get(position).getImage())) {
            return WoNoImages.class;
        } else {
            String[] images = publisheds.get(position).getImage().split(NS.SPLIT);
            if (images.length > 1) {
                return WoMoreImage.class;
            } else {
                return WoJustOneImage.class;
            }
        }
    }

//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        final ViewHolder viewHolder;
//        if (convertView == null) {
//            convertView = View.inflate(context, R.layout.item_wo_listview, null);
//            viewHolder = new ViewHolder(convertView);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//        initView(viewHolder, position);
//        initOnclick(viewHolder);
//
//        return convertView;
//    }
//
//    private void initOnclick(final ViewHolder viewHolder) {
//        viewHolder.commentLay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewHolder.comment.performClick();
//            }
//        });
//        viewHolder.praiseLay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewHolder.praise.performClick();
//            }
//        });
//
//        //头像监听
//        viewHolder.headImage.setIntent_type(CirecleImage.PERSON_STATE);
////        删除
//        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final DialogUtils.Dialog_Center center = new DialogUtils.Dialog_Center(context);
//                center.Message("确定删除吗?");
//                center.Button("删除", "取消");
//                center.MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
//                    @Override
//                    public void onButton1() {
//                        Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
//                        center.close();
//                    }
//
//                    @Override
//                    public void onButton2() {
//                        Toast.makeText(context, "cancle", Toast.LENGTH_SHORT).show();
//                        center.close();
//                    }
//                });
//                Dialog dialog = center.create();
//                dialog.show();
//                LocationUtil.setWidth(activity, dialog,
//                        context.getResources().getDimensionPixelSize(R.dimen.x780));
//            }
//        });
//
//        viewHolder.permission.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, rollActivity.class);
//                intent.putExtra(rollActivity.TYPE, rollActivity.NOTICE);
//                context.startActivity(intent);
//            }
//        });
//    }
//
//    private void initView(ViewHolder viewHolder, int position) {
//        Published published = publisheds.get(position);
//
////      头像  姓名 学院
//        UserCache userCache = new UserCache(context, String.valueOf(published.getUserId()), realm);
//        userCache.getHead(viewHolder.headImage);
//        userCache.getName(viewHolder.name);
//
////        是否头条
//        viewHolder.headline.setVisibility(published.getIsHeadline() == 1 ? View.VISIBLE : View.INVISIBLE);
//
////      文字内容
//        viewHolder.text.setText(published.getText());
//
////        图片
//        parseImages(viewHolder, published.getImage());
//
////      地点
//        viewHolder.location.setText(published.getLocation());
//
////        时间
//        viewHolder.time.setText(TimeUtil.getTimeShowString(published.getCreateTime(), false));
//
////        显示权限列表
//        if (TempUser.isMine(String.valueOf(published.getUserId()))) {
//            viewHolder.permission.setVisibility(View.VISIBLE);
//            viewHolder.delete.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.permission.setVisibility(View.GONE);
//            viewHolder.delete.setVisibility(View.GONE);
//        }
//
////        转发
//        parseTransmit(viewHolder, published);
//
////        点赞
//        if (viewHolder.praisePeople.getChildCount() != 0) {
//            viewHolder.praisePeople.removeAllViews();
//        }
//        if (!TextUtils.isEmpty(published.getPraiseUserIds())) {
//            Woadapter_Help.buildPrasiPeople(published.getPraiseUserIds().split(NS.SPLIT),
//                    context, viewHolder.praiseLay);
//        }
//
//        parseComment(viewHolder, published);
//
//    }
//
//    private void parseImages(final ViewHolder viewHolder, String images) {
//        if (TextUtils.isEmpty(images)) {
//            viewHolder.justOne.setVisibility(View.GONE);
//            viewHolder.photos1.setVisibility(View.GONE);
//        } else {
//            final ArrayList<String> imageUrls = new ArrayList<>();
//            String[] splits = images.split(NS.SPLIT);
//            for (int i = 0; i < splits.length; i++) {
//                imageUrls.add(splits[i]);
//            }
//            if (imageUrls.size() > 1) {
//                viewHolder.justOne.setVisibility(View.GONE);
//                viewHolder.photos1.setVisibility(View.VISIBLE);
//                viewHolder.photos1.setAdapter(new NoScrollGridAdapter(context, imageUrls));
//                viewHolder.photos1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Intent intent = new Intent(context, ImagePagerActivity.class);
//                        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrls);
//                        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
//                        context.startActivity(intent);
//                    }
//                });
//            } else if (imageUrls.size() == 1) {
//                viewHolder.justOne.setVisibility(View.VISIBLE);
//                viewHolder.photos1.setVisibility(View.GONE);
//                Glide.with(context)
//                        .load(imageUrls.get(0))
//                        .asBitmap()
//                        .placeholder(R.mipmap.greyblock)
//                        .error(R.mipmap.nim_image_download_failed)
//                        .into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                                if (resource.getHeight() > context.getResources().getDimensionPixelSize(R.dimen.y600)) {
//                                    viewHolder.justOne.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                                            context.getResources().getDimensionPixelSize(R.dimen.y600)));
//                                }
//                                viewHolder.justOne.setImageBitmap(resource);
//                            }
//                        });
//                viewHolder.justOne.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(context, ImagePagerActivity.class);
//                        ArrayList<String> justone = new ArrayList<String>();
//                        justone.add(imageUrls.get(0));
//                        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, justone);
//                        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
//                        context.startActivity(intent);
//                    }
//                });
//            }
//        }
//    }
//
//    private void parseTransmit(ViewHolder viewHolder, Published published) {
//        if (published.getIsTransimit() == 1) {
//            viewHolder.transmitContent.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.transmitContent.setVisibility(View.GONE);
//        }
//    }
//
//    private void parseComment(final ViewHolder viewHolder, Published published) {
////        //        测试评论
////        if (viewHolder.comments.getChildCount() != 0) {
////            viewHolder.comments.removeAllViews();
////        }
////
////        Item_Comment comment = new Item_Comment(context, "御天证道", "哇哇哇哇哇哇哇哇哇哇哇哇" + "[可爱]");
////        Item_Comment comment2 = new Item_Comment(context, "王振华", "孙璐阳", "和哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈和");
////        viewHolder.comments.addView(comment.getTextView());
////        viewHolder.comments.addView(comment2.getTextView());
////
//////       评论
////        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                woFragment.showEdittext(context);
////                final int[] xy = new int[2];
////                v.getLocationOnScreen(xy);
////                final int layoutHeight = viewHolder.commentLayout.getHeight();
////                final View mv = v;
////                mHandler.postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////                        int editextLocation = woFragment.get_Editext_Height();
////                        int destination = xy[1] + mv.getHeight() + layoutHeight - editextLocation;
////                        woFragment.moveListview(destination);
////                    }
////                }, 300);
////            }
////        });
////        setCommentListner(comment.getTextView());
////        setCommentListner(comment2.getTextView());
//    }
//
//    private void setCommentListner(View view) {
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showEdittext(v);
//                woFragment.setEditCallback(new InputBoxLayout.CallBack() {
//                    @Override
//                    public void callback(String text) {
//                        Log.d("callback", "pp");
//                    }
//                });
//            }
//        });
//    }
//
//    private void showEdittext(View v) {
//        woFragment.showEdittext(context);
//        final int[] xy = new int[2];
//        v.getLocationOnScreen(xy);
//        final View mv = v;
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                int editextLocation = woFragment.get_Editext_Height();
//                int destination = xy[1] + mv.getHeight() - editextLocation;
//                woFragment.moveListview(destination);
//            }
//        }, 300);
//        woFragment.setEditCallback(new InputBoxLayout.CallBack() {
//            @Override
//            public void callback(String text) {
//
//            }
//        });
//    }
//
//    static class ViewHolder {
//        @Bind(R.id.head_image)
//        CirecleImage headImage;
//        @Bind(R.id.name)
//        Name name;
//        @Bind(R.id.college)
//        TextView college;
//        @Bind(R.id.text)
//        MoreTextView text;
//        @Bind(R.id.photos1)
//        NoScrollGridView photos1;
//        @Bind(R.id.just_one)
//        ImageView justOne;
//        @Bind(R.id.transmit_image)
//        CirecleImage transmitImage;
//        @Bind(R.id.transmit_text)
//        EmotinText transmitText;
//        @Bind(R.id.transmit_content)
//        LinearLayout transmitContent;
//        @Bind(R.id.location)
//        TextView location;
//        @Bind(R.id.time)
//        TextView time;
//        @Bind(R.id.permission)
//        ImageView permission;
//        @Bind(R.id.delete)
//        TextView delete;
//        @Bind(R.id.praise)
//        CheckBox praise;
//        @Bind(R.id.praise_lay)
//        LinearLayout praiseLay;
//        @Bind(R.id.comment)
//        ImageView comment;
//        @Bind(R.id.comment_lay)
//        LinearLayout commentLay;
//        @Bind(R.id.praise_people)
//        LinearLayout praisePeople;
//        @Bind(R.id.comments)
//        LinearLayout comments;
//        @Bind(R.id.comment_layout)
//        LinearLayout commentLayout;
//        @Bind(R.id.headline)
//        ImageView headline;
//
//        ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//    }
}
