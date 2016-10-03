package com.xiaoshangxing.xiaoshang.Sale.SaleFrafment;

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
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.Network.netUtil.OperateUtils;
import com.xiaoshangxing.Network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmoticonsEditText;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.Name;
import com.xiaoshangxing.wo.WoFrafment.NoScrollGridView;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImagePagerActivity;
import com.xiaoshangxing.xiaoshang.Sale.SaleActivity;
import com.xiaoshangxing.xiaoshang.Sale.SaleDetail.SaleDetailsActivity;
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
public class Sale_Adpter extends ArrayAdapter<Published> {
    private Context context;
    List<Published> publisheds;
    private SaleFragment fragment;
    private SaleActivity activity;
    protected Handler handler;

    public Sale_Adpter(Context context, int resource, List<Published> objects,
                       SaleFragment fragment, SaleActivity activity) {
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

        UserInfoCache.getInstance().getHead(viewHolder.headImage, published.getUserId(), context);
        UserInfoCache.getInstance().getName(viewHolder.name, published.getUserId());
        UserInfoCache.getInstance().getCollege(viewHolder.college, published.getUserId());
        viewHolder.time.setText(TimeUtil.getTimeShowString(published.getCreateTime(), false));
        viewHolder.text.setText(published.getText());
        viewHolder.price.setText(NS.RMB + published.getPrice());
        viewHolder.dorm.setText(published.getDorm());
        viewHolder.complete.setVisibility(published.isAlive() ? View.GONE : View.VISIBLE);

        viewHolder.downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.showCollectDialog(published.getId(), published.isCollected());
            }
        });

        viewHolder.input.setFocusable(true);
        viewHolder.input.requestFocus();

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
                    finalViewHolder.chat.setClickable(false);
                } else {
                    finalViewHolder.chat.setBackground(context.getResources().getDrawable(R.drawable.circular_4_green));
                    finalViewHolder.chat.setClickable(true);
                }
            }
        });

        final ViewHolder finalViewHolder1 = viewHolder;

        viewHolder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OperateUtils.Tranmit(published.getId(), NS.CATEGORY_SALE, "4"/*String.valueOf(published.getUserId())*/, fragment,
                        finalViewHolder1.input.getText().toString(), new SimpleCallBack() {
                            @Override
                            public void onSuccess() {
                                ChatActivity.start(context, "4", null, SessionTypeEnum.P2P);
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
