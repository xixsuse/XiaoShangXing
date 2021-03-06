package com.xiaoshangxing.xiaoshang.Sale.SaleCollect;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.network.netUtil.OperateUtils;
import com.xiaoshangxing.network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.data.bean.Published;
import com.xiaoshangxing.utils.AppContracts;
import com.xiaoshangxing.utils.customView.EmotionEdittext.EmoticonsEditText;
import com.xiaoshangxing.utils.customView.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.customView.CirecleImage;
import com.xiaoshangxing.utils.customView.Name;
import com.xiaoshangxing.wo.WoFrafment.NoScrollGridView;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImagePagerActivity;
import com.xiaoshangxing.xiaoshang.Sale.SaleActivity;
import com.xiaoshangxing.xiaoshang.Sale.SaleDetail.SaleDetailsActivity;
import com.xiaoshangxing.xiaoshang.Sale.SaleFrafment.SaleGridAdapter;
import com.xiaoshangxing.yujian.ChatActivity.ChatActivity;
import com.xiaoshangxing.yujian.IM.kit.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class SaleCollect_Adpter extends ArrayAdapter<Published> {
    protected Handler handler;
    List<Published> publisheds;
    private Context context;
    private SaleCollectFragment fragment;
    private SaleActivity activity;

    public SaleCollect_Adpter(Context context, int resource, List<Published> objects,
                              SaleCollectFragment fragment, SaleActivity activity) {
        super(context, resource, objects);
        this.context = context;
        this.publisheds = objects;
        this.fragment = fragment;
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_sale_adpter, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Published published = publisheds.get(position);

        String userId = String.valueOf(published.getUserId());
        UserInfoCache.getInstance().getHeadIntoImage(userId, viewHolder.headImage);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.USER_NAME, viewHolder.name);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.COLLEGE, viewHolder.college);
        viewHolder.time.setText(TimeUtil.getSimplePublishedTime(published.getCreateTime()));
        viewHolder.text.setText(published.getText());
        viewHolder.price.setText(AppContracts.RMB + published.getPrice());
        viewHolder.dorm.setText(published.getDorm());
        viewHolder.complete.setVisibility(published.isAlive() ? View.GONE : View.VISIBLE);
        viewHolder.price.setTextColor(published.isAlive() ? context.getResources().getColor(R.color.red1) :
                context.getResources().getColor(R.color.g0));
        viewHolder.headImage.setIntent_type(CirecleImage.PERSON_INFO, userId);

        viewHolder.downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.showCollectDialog(published.getId());
            }
        });

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.showEdittext(true, finalViewHolder.input);
            }
        });
        viewHolder.input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(finalViewHolder.input.getText().toString())) {
                    finalViewHolder.chat.setBackground(context.getResources().getDrawable(R.drawable.circular_4_g2));
                    finalViewHolder.chat.setEnabled(false);
                } else {
                    finalViewHolder.chat.setBackground(context.getResources().getDrawable(R.drawable.circular_4_green));
                    finalViewHolder.chat.setEnabled(true);
                }
            }
        });


        viewHolder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.add(String.valueOf(published.getUserId()));
                OperateUtils.Tranmit(published.getId(), NS.CATEGORY_SALE, arrayList, fragment,
                        finalViewHolder.input.getText().toString(), new SimpleCallBack() {
                            @Override
                            public void onSuccess() {
                                ChatActivity.start(context, String.valueOf(published.getUserId()), null, SessionTypeEnum.P2P);
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onBackData(Object o) {

                            }
                        });
            }
        });

        final ArrayList<String> imageUrls = new ArrayList<>();
        if (!TextUtils.isEmpty(published.getImage())) {
            for (String i : published.getImage().split(NS.SPLIT)) {
                imageUrls.add(i);
            }
        }

        viewHolder.pictures.setVisibility(View.VISIBLE);
        viewHolder.pictures.setAdapter(new SaleGridAdapter(context, imageUrls));
        viewHolder.pictures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ImagePagerActivity.class);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrls);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                context.startActivity(intent);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SaleDetailsActivity.class);
                intent.putExtra(IntentStatic.DATA, published.getId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.head_image)
        CirecleImage headImage;
        @Bind(R.id.name)
        Name name;
        @Bind(R.id.college)
        TextView college;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.dorm)
        TextView dorm;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.down_arrow)
        ImageView downArrow;
        @Bind(R.id.text)
        EmotinText text;
        @Bind(R.id.pictures)
        NoScrollGridView pictures;
        @Bind(R.id.input)
        EmoticonsEditText input;
        @Bind(R.id.chat)
        Button chat;
        @Bind(R.id.complete)
        ImageView complete;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
