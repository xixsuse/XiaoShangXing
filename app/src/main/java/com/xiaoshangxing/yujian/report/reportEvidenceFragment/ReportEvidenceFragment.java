package com.xiaoshangxing.yujian.report.reportEvidenceFragment;

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
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.xiaoshangxing.network.Formmat;
import com.xiaoshangxing.network.ShowMsgHandler;
import com.xiaoshangxing.network.netUtil.BaseUrl;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.network.netUtil.SimpleCallBack;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.publicActivity.album.AlbumActivity;
import com.xiaoshangxing.utils.baseClass.BaseFragment;
import com.xiaoshangxing.utils.baseClass.IBaseView;
import com.xiaoshangxing.utils.imageUtils.MyGlide;
import com.xiaoshangxing.yujian.report.ReportActivity;
import com.xiaoshangxing.yujian.report.reportCommitFragment.ReportCommitFragment;
import com.xiaoshangxing.yujian.report.reportNoticeFragment.ReportNoticeFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *modified by FengChaoQun on 2016/12/25 13:49
 * description:优化代码
 */
public class ReportEvidenceFragment extends BaseFragment implements IBaseView {
    public static final String TAG = BaseFragment.TAG + "-ReportEvidenceFragment";
    public static Bitmap bimap;
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
    private GridView noScrollgridview;
    private GridAdapter adapter;
    private ReportActivity reportActivity;
    private List<String> select_image_urls = new ArrayList<>();
    private Handler handler;
    private ShowMsgHandler showMsgHandler;

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
        showMsgHandler = new ShowMsgHandler(getContext());
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.mipmap.icon_addpic_unfocused);
        Init();

        return mView;
    }

    private void Init() {
        noScrollgridview = (GridView) mView.findViewById(R.id.report_evidence_gridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(getActivity());
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
                upload();
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

    private void upload() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Formmat formmat = null;
                try {
                    formmat = new Formmat(ReportEvidenceFragment.this, getActivity(), BaseUrl.BASE_URL + BaseUrl.REPORT);
                } catch (IOException e) {
                    e.printStackTrace();
                    showMsgHandler.showToast("初始化上传组件失败,请检查网络状态");
                    return;
                }
                formmat.setSuccessToast("提交成功");
                formmat.setSimpleCallBack(new SimpleCallBack() {
                    @Override
                    public void onSuccess() {
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                        R.anim.slide_in_left, R.anim.slide_out_left)
                                .addToBackStack(null)
                                .replace(R.id.main_fragment, new ReportCommitFragment(), ReportCommitFragment.TAG)
                                .commit();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onBackData(Object o) {

                    }
                });
                final Map<String, String> map = new HashMap<>();
                map.put(NS.USER_ID, TempUser.getId());
                map.put("reportedUser", reportActivity.getAccount());
                map.put(NS.TYPE, reportActivity.getReportType());
                map.put("reason", reportText.getText().toString());
                try {
                    formmat.addFormField(map)
                            .addFilePart(select_image_urls, getContext())
                            .doUpload();
                } catch (IOException e) {
                    e.printStackTrace();
                    showToast("图片出错");
                }
            }
        });
        thread.start();
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

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

    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public int getCount() {
            int count = select_image_urls.size() + 1;
            return count > 9 ? 9 : count;
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
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
                Glide.with(getContext()).load(R.mipmap.icon_addpic_unfocused).into(holder.image);
            }
            return convertView;
        }


        public class ViewHolder {
            public ImageView image;
        }


    }
}
