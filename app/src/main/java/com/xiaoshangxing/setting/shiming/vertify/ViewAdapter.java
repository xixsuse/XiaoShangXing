package com.xiaoshangxing.setting.shiming.vertify;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

/**
 * Created by tianyang on 2016/9/19.
 */
public class ViewAdapter extends PagerAdapter {
    private List<View> viewList;

    public ViewAdapter(List<View> viewList) {
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }


    //对应页卡添加上数据
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));//千万别忘记添加到container
        return viewList.get(position);
    }

}
