package com.xiaoshangxing.input_activity.album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2016/8/8
 */
public class AlbumListFragment extends BaseFragment {
    public static final String TAG = BaseFragment.TAG + "-AlbumListFragment";
    @Bind(R.id.myState)
    TextView myState;
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.title)
    RelativeLayout title;
    @Bind(R.id.listview)
    ListView listview;
    private View view;
    private AlbumHelper helper;
    public List<ImageBucket> contentList;
    private AlbumListAdpter albumListAdpter;
    private ArrayList<ImageItem> dataList = new ArrayList<>();
    private ImageBucket totalImageBucket = new ImageBucket();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getContext(), R.layout.frag_album_list, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public static AlbumListFragment newInstance() {
        return new AlbumListFragment();
    }

    private void initView() {
        setRightSlide(new BaseActivity.RightSlide() {
            @Override
            public boolean rightSlide() {
                AlbumActivity albumActivity = (AlbumActivity) getActivity();
                albumActivity.finish();
                return true;
            }
        });
        helper = AlbumHelper.getHelper();
        helper.init(getContext());
        contentList = helper.getImagesBucketList(false);
        for (int i = 0; i < contentList.size(); i++) {
            SortImage.SortImages(contentList.get(i));
        }
//最近照片
        if (!contentList.contains(totalImageBucket)) {
            totalImageBucket=helper.getTotalImage(false);
            totalImageBucket.bucketName = "最近照片";
            SortImage.SortImages(totalImageBucket);
            if (totalImageBucket.imageList.size() > 100) {
                totalImageBucket.imageList =
                        totalImageBucket.imageList.subList(0, 100);
            }
            totalImageBucket.count = totalImageBucket.imageList.size();
            contentList.add(0, totalImageBucket);
        }

        albumListAdpter = new AlbumListAdpter(getContext(), 1, (ArrayList<ImageBucket>) contentList);
        listview.setAdapter(albumListAdpter);
        View view = new View(getContext());
        view.setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.y48));
        listview.addHeaderView(view);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }

                AlbumActivity activity = (AlbumActivity) getActivity();
                activity.setCurrent_imagebucket(contentList.get(position - 1));
                AlbumDetailFragment fragment = AlbumDetailFragment.newInstance();
                AlbumListFragment albumListFragment = activity.getAlbumListFragment();

                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                R.anim.slide_in_left, R.anim.slide_out_left)
                        .hide(albumListFragment)
                        .add(R.id.main_fragment, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.cancel)
    public void onClick() {
        AlbumActivity activity = (AlbumActivity) getActivity();
        List<String> list=new ArrayList<>();
        activity.setSelect_image_urls(list);
        getActivity().finish();
    }

}
