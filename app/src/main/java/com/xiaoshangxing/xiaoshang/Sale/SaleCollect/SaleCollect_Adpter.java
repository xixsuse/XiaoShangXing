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

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmoticonsEditText;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.Name;
import com.xiaoshangxing.wo.WoFrafment.NoScrollGridView;
import com.xiaoshangxing.wo.WoFrafment.check_photo.ImagePagerActivity;
import com.xiaoshangxing.xiaoshang.Sale.SaleActivity;
import com.xiaoshangxing.xiaoshang.Sale.SaleFrafment.SaleGridAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class SaleCollect_Adpter extends ArrayAdapter<Published> {
    private Context context;
    List<Published> publisheds;
    private SaleCollectFragment fragment;
    private SaleActivity activity;
    protected Handler handler;

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

        viewHolder.downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.showCollectDialog(99);
            }
        });

        final ViewHolder finalViewHolder = viewHolder;
//        viewHolder.input.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handler = new Handler();
//                fragment.showEdittext(true, finalViewHolder.input);
//                final int[] xy = new int[2];
//                v.getLocationOnScreen(xy);
//                final View mv = v;
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        int[] xy1 = new int[2];
//                        finalViewHolder.input.getLocationOnScreen(xy1);
//                        int destination = xy[1] + mv.getHeight() - xy1[1];
//                        fragment.moveListview(destination);
//                    }
//                }, 300);
//                finalViewHolder.input.setFocusable(true);
//                finalViewHolder.input.setFocusableInTouchMode(true);
//                finalViewHolder.input.requestFocus();
//                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        });

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

        final ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg");
        imageUrls.add("http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg");
        imageUrls.add("http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg");

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


//    static class ViewHolder {
//        @Bind(R.id.head_image)
//        CirecleImage headImage;
//        @Bind(R.id.name)
//        Name name;
//        @Bind(R.id.college)
//        TextView college;
//        @Bind(R.id.time)
//        TextView time;
//        @Bind(R.id.dorm)
//        TextView dorm;
//        @Bind(R.id.price)
//        TextView price;
//        @Bind(R.id.down_arrow)
//        ImageView downArrow;
//        @Bind(R.id.text)
//        EmotinText text;
//        @Bind(R.id.pictures)
//        NoScrollGridView pictures;
//        @Bind(R.id.input)
//        EditText input;
//        @Bind(R.id.chat)
//        Button chat;
//        @Bind(R.id.complete)
//        ImageView complete;
//
//        ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//    }
}
