package com.xiaoshangxing.setting.privacy.blackListFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.image.MyGlide;
import com.xiaoshangxing.utils.layout.CustomSwipeListView;
import com.xiaoshangxing.utils.layout.SwipeItemView;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;

import java.util.List;

/**
 * Created by 15828 on 2016/7/14.
 */
public class BlackListFragment extends BaseFragment implements View.OnClickListener {
    private View mView;
    private TextView back;
    private CustomSwipeListView blackList;
    private BaseAdapter baseAdapter;
    private List<String> accounts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_blacklist, container, false);
        back = (TextView) mView.findViewById(R.id.blacklist_back);
        back.setOnClickListener(this);
        blackList = (CustomSwipeListView) mView.findViewById(R.id.blacklist_listView);
        init();
        return mView;
    }

    private void init() {
        accounts = NIMClient.getService(FriendService.class).getBlackList();

        baseAdapter = new BaseAdapter() {
            SwipeItemView mLastSlideViewWithStatusOn;

            @Override
            public int getCount() {
                return accounts.size();
            }

            @Override
            public String getItem(int position) {
                return accounts.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                SwipeItemView slideView = (SwipeItemView) convertView;
                if (slideView == null) {
                    View itemView = getActivity().getLayoutInflater().inflate(R.layout.item_setting_blacklist, null);

                    slideView = new SwipeItemView(getActivity());
                    slideView.setContentView(itemView);

                    holder = new ViewHolder(slideView);
                    slideView.setOnSlideListener(new SwipeItemView.OnSlideListener() {

                        @Override
                        public void onSlide(View view, int status) {
                            // TODO Auto-generated method stub
                            if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
                                mLastSlideViewWithStatusOn.shrink();
                            }

                            if (status == SLIDE_STATUS_ON) {
                                mLastSlideViewWithStatusOn = (SwipeItemView) view;
                            }
                        }
                    });
                    slideView.setTag(holder);
                } else {
                    holder = (ViewHolder) slideView.getTag();
                }

                MyGlide.with(getContext(), NimUserInfoCache.getInstance().getHeadImage(accounts.get(position)), holder.image);
                holder.name.setText(NimUserInfoCache.getInstance().getUserDisplayName(accounts.get(position)));
                holder.deleteHolder.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
//                        Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
//                        notifyDataSetChanged();
                        NIMClient.getService(FriendService.class).removeFromBlackList(accounts.get(position))
                                .setCallback(new RequestCallback<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        init();
                                    }

                                    @Override
                                    public void onFailed(int i) {
                                        showToast("删除失败:" + i);
                                    }

                                    @Override
                                    public void onException(Throwable throwable) {
                                        showToast("删除失败:异常");
                                        throwable.printStackTrace();
                                    }
                                });
                    }
                });
                return slideView;
            }
        };

        blackList.setAdapter(baseAdapter);

    }

    private static class ViewHolder {
        public ImageView image;
        public TextView name;
        public ViewGroup deleteHolder;

        ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.blacklist_img);
            name = (TextView) view.findViewById(R.id.blacklist_text);
            deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
        }
    }

    @Override
    public void onClick(View v) {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
