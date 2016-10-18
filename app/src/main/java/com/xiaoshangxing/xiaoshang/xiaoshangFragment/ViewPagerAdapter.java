package com.xiaoshangxing.xiaoshang.XiaoShangFragment;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiaoshangxing.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter implements ViewAdapter {

    private List<View> mViews;
    private List<String> mData;
    private List<Integer> images = new ArrayList<>();
    private XiaoShangFragment xiaoShangFragment;

    public ViewPagerAdapter(XiaoShangFragment xiaoShangFragment) {

        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        this.xiaoShangFragment = xiaoShangFragment;

        for (int i = 0; i < 5; i++) {
            mData.add("");
            mViews.add(null);
        }
        images.add(R.mipmap.xiaoshang_01);
        images.add(R.mipmap.xiaoshang_02);
        images.add(R.mipmap.xiaoshang_03);
        images.add(R.mipmap.xiaoshang_04);
        images.add(R.mipmap.xiaoshang_05);
    }


    @Override
    public View getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.xiaoshang_image, container, false);
        ImageView showImage = (ImageView) view.findViewById(R.id.show_image);
        showImage.setImageResource(images.get(position));
        showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xiaoShangFragment.gotoOther(position + 1);
            }
        });
        container.addView(view);
        mViews.set(position, view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

}
