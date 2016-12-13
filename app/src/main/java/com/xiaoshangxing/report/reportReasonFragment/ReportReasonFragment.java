package com.xiaoshangxing.report.reportReasonFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.report.ReportActivity;
import com.xiaoshangxing.report.reportEvidenceFragment.ReportEvidenceFragment;
import com.xiaoshangxing.utils.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tianyang on 2016/7/1.
 */
public class ReportReasonFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    public static final String TAG = BaseFragment.TAG + "-ReportReasonFragment";
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_text)
    TextView next;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.list_reportReason)
    ListView listReportReason;

    private View mView;
    private ArrayAdapter mAdapter;
    private ListView listView;
    private String[] mReasons;
    private ReportActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_report_reason, container, false);
        ButterKnife.bind(this, mView);
        leftImage.setVisibility(View.GONE);
        leftText.setText("取消");
        title.setText("投诉");
        next.setText("下一步");
        next.setTextColor(getResources().getColor(R.color.green1));
        next.setEnabled(false);
        next.setAlpha(0.5f);

        setCloseActivity();

        activity = (ReportActivity) getActivity();

        mReasons = getResources().getStringArray(R.array.report_reasons);
        mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_report, mReasons);
        listView = (ListView) mView.findViewById(R.id.list_reportReason);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        return mView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        activity.setReportType(mReasons[position]);
        next.setAlpha(1.0f);
        next.setEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.back, R.id.right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.right_text:
                int position = listView.getCheckedItemPosition();
                if (position == -1) break;
                else {
                    ReportEvidenceFragment reportEvidenceFragment = activity.getReportEvidenceFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                    R.anim.slide_in_left, R.anim.slide_out_left)
                            .addToBackStack(ReportEvidenceFragment.TAG)
                            .replace(R.id.main_fragment, reportEvidenceFragment, ReportEvidenceFragment.TAG)
                            .commit();
                    listView.setItemChecked(position, false);
                }
                break;
        }
    }
}
