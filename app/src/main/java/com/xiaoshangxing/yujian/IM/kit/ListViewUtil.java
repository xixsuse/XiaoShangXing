package com.xiaoshangxing.yujian.IM.kit;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.data.bean.Published;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ListViewUtil {

    public static boolean isLastMessageVisible(ListView messageListView) {
        if (messageListView == null || messageListView.getAdapter() == null) {
            return false;
        }

        if (messageListView.getLastVisiblePosition() >= messageListView.getAdapter().getCount() - 1 - messageListView.getFooterViewsCount()) {
            return true;
        } else {
            return false;
        }
    }

    //index是items的index，不包含header
    public static Object getViewHolderByIndex(ListView listView, int index) {
        int firstVisibleFeedPosition = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount();
        int lastVisibleFeedPosition = listView.getLastVisiblePosition() - listView.getHeaderViewsCount();

        //只有获取可见区域的
        if (index >= firstVisibleFeedPosition && index <= lastVisibleFeedPosition) {
            View view = listView.getChildAt(index - firstVisibleFeedPosition);
            Object tag = view.getTag();
            return tag;
        } else {
            return null;
        }
    }

    public static void scrollToBottom(ListView listView) {
        scrollToPosition(listView, listView.getAdapter().getCount() - 1, 0);
    }

    public static void scrollToBottom(ListView listView, ScrollToPositionListener listener) {
        scrollToPosition(listView, listView.getAdapter().getCount() - 1, 0, listener);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void scrollToPosition(ListView messageListView, int position, int y) {
        scrollToPosition(messageListView, position, y, null);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void scrollToPosition(final ListView messageListView, final int position, final int y, final ScrollToPositionListener listener) {
        messageListView.post(new Runnable() {

            @Override
            public void run() {
                messageListView.setSelectionFromTop(position, y);

                if (listener != null) {
                    listener.onScrollEnd();
                }
            }
        });
    }

    public static void LoadByPager(ListView listView, ArrayAdapter<Published> adapter, Realm realm, String categery) {
        int curent_anchor = 10;
        RealmResults<Published> publisheds = publisheds = realm.where(Published.class)
                .equalTo(NS.CATEGORY, Integer.valueOf(categery))
                .findAll().sort(NS.ID, Sort.DESCENDING);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount) {

                }
            }
        });
    }

    public interface ScrollToPositionListener {
        void onScrollEnd();
    }
}
