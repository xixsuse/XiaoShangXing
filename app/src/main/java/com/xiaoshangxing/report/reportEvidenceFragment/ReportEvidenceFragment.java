package com.xiaoshangxing.report.reportEvidenceFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.album.AlbumActivity;
import com.xiaoshangxing.report.ReportActivity;
import com.xiaoshangxing.report.reportCommitFragment.ReportCommitFragment;
import com.xiaoshangxing.report.reportNoticeFragment.ReportNoticeFragment;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.image.MyGlide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tianyang on 2016/7/2.
 */
public class ReportEvidenceFragment extends BaseFragment {
    public static final String TAG = BaseFragment.TAG + "-ReportEvidenceFragment";
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_text)
    TextView rightText;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.report_evidence_content1)
    RelativeLayout reportEvidenceContent1;
    @Bind(R.id.report_evidence_edittext)
    EditText reportEvidenceEdittext;
    @Bind(R.id.report_evidence_content2)
    RelativeLayout reportEvidenceContent2;
    @Bind(R.id.report_evidence_picture)
    LinearLayout reportEvidencePicture;
    @Bind(R.id.report_evidence_gridview)
    GridView reportEvidenceGridview;
    @Bind(R.id.report_evidence_notice)
    TextView reportEvidenceNotice;
    private View mView;
    private TextView reportNotice;
    private EditText reportText;
    public static Bitmap bimap;
    private GridView noScrollgridview;
    private GridAdapter adapter;
    private ReportActivity reportActivity;
    private List<String> select_image_urls = new ArrayList<>();
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_report_evidence, container, false);
        ButterKnife.bind(this, mView);
        title.setText("投诉");
        rightText.setText("提交");
        rightText.setTextColor(getResources().getColor(R.color.green1));

        reportActivity = (ReportActivity) getActivity();
        handler = new Handler();
        reportNotice = (TextView) mView.findViewById(R.id.report_evidence_notice);
        reportText = (EditText) mView.findViewById(R.id.report_evidence_edittext);
        reportText.setText(reportActivity.getReportText());

//        Res.init(getActivity());
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.mipmap.icon_addpic_unfocused);
//        PublicWay.activityList.add(getActivity());
        Init();

        return mView;
    }

    private void Init() {
        noScrollgridview = (GridView) mView.findViewById(R.id.report_evidence_gridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(getActivity());
        //   adapter.update();
        adapter.notifyDataSetChanged();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {//查看某个照片

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mView, InputMethodManager.SHOW_FORCED);
                imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
                Intent album_intent = new Intent(getContext(), AlbumActivity.class);
                album_intent.putExtra(AlbumActivity.LIMIT, 9);
                album_intent.putStringArrayListExtra(AlbumActivity.SELECTED, (ArrayList<String>) select_image_urls);
                getActivity().startActivityForResult(album_intent, 20000);
            }
        });
    }

    @OnClick({R.id.back, R.id.right_text, R.id.report_evidence_notice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mView, InputMethodManager.SHOW_FORCED);
                imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.right_text:
                InputMethodManager imm2 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.showSoftInput(mView, InputMethodManager.SHOW_FORCED);
                imm2.hideSoftInputFromWindow(mView.getWindowToken(), 0);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                R.anim.slide_in_left, R.anim.slide_out_left)
                        .addToBackStack(null)
                        .replace(R.id.main_fragment, new ReportCommitFragment(), ReportCommitFragment.TAG)
                        .commit();
                break;
            case R.id.report_evidence_notice:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                R.anim.slide_in_left, R.anim.slide_out_left)
                        .addToBackStack(null)
                        .replace(R.id.main_fragment, new ReportNoticeFragment(), ReportNoticeFragment.TAG)
                        .commit();
                break;

        }
    }

    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

//        public void update() {
//            loading();
//        }

        public int getCount() {
//            if (Bimp.tempSelectBitmap.size() == 9) {
//                return 9;
//            }
//            return (Bimp.tempSelectBitmap.size() + 1);
            int count = select_image_urls.size() + 1;
            return count > 9 ? 9 : count;
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (select_image_urls.size() > position) {
                MyGlide.with(getContext(), select_image_urls.get(position), holder.image);
            } else {
                holder.image.setImageResource(R.mipmap.icon_addpic_unfocused);
            }
            return convertView;
        }


        public class ViewHolder {
            public ImageView image;
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        // adapter.update();
    }


    public void setSelect_image_urls(List<String> select_image_urls) {
        this.select_image_urls = select_image_urls;
        adapter.notifyDataSetChanged();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        }, 300);

    }

    @Override
    public void onDestroyView() {
        reportActivity.setReportText(reportText.getText().toString());
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
