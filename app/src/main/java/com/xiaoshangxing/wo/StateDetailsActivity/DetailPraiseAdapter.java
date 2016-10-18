package com.xiaoshangxing.wo.StateDetailsActivity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xiaoshangxing.R;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.utils.layout.CirecleImage;

import java.util.ArrayList;

import io.realm.Realm;

public class DetailPraiseAdapter extends BaseAdapter {

    Realm realm;
    /**
     * 上下文
     */
    private Context ctx;
    /**
     * 图片Url集合
     */
    private ArrayList<String> imageUrls;

    public DetailPraiseAdapter(Context ctx, ArrayList<String> urls, Realm realm) {
        this.ctx = ctx;
        this.imageUrls = urls;
        this.realm = realm;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageUrls == null ? 0 : imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(ctx, R.layout.util_circle_image_96, null);
        CirecleImage imageView = (CirecleImage) view.findViewById(R.id.head_image);
        imageView.setIntent_type(CirecleImage.PERSON_STATE, imageUrls.get(position));
        UserInfoCache.getInstance().getHeadIntoImage(imageUrls.get(position), imageView);
//        UserInfoCache.getInstance().getHead(imageView, Integer.valueOf(imageUrls.get(position)), ctx);
        return view;
    }

}
