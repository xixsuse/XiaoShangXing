package com.xiaoshangxing.wo.inform.report.reportEvidenceFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.setting.utils.photo_choosing.Bimp;
import com.xiaoshangxing.setting.utils.photo_choosing.PublicWay;
import com.xiaoshangxing.setting.utils.photo_choosing.Res;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.wo.inform.report.ReportActivity;
import com.xiaoshangxing.wo.inform.report.photoChoosing.AlbumFragment;
import com.xiaoshangxing.wo.inform.report.reportCommitFragment.ReportCommitFragment;
import com.xiaoshangxing.wo.inform.report.reportNoticeFragment.ReportNoticeFragment;

/**
 * Created by tianyang on 2016/7/2.
 */
public class ReportEvidenceFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = BaseFragment.TAG + "-ReportEvidenceFragment";
    private View mView;
    private TextView back, submit, reportNotice;
    private EditText reportText;
    public static Bitmap bimap;
    private GridView noScrollgridview;
    private GridAdapter adapter;
    private ReportActivity reportActivity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_report_evidence, container, false);
        Log.d("qqq", "onCreate...");
        reportActivity = (ReportActivity) getActivity();

        if (reportActivity.isCanceled()) {
            Log.d("qqq","   "+ reportActivity.isCanceled());
            Bimp.tempSelectBitmap.clear();
            Bimp.max = 0;
            reportActivity.setCanceled(false);
        }
        back = (TextView) mView.findViewById(R.id.toolbar_reportevidence_back);
        submit = (TextView) mView.findViewById(R.id.toolbar_reportevidence_submit);
        reportNotice = (TextView) mView.findViewById(R.id.report_evidence_notice);
        reportText = (EditText) mView.findViewById(R.id.report_evidence_edittext);
        reportText.setText(reportActivity.getReportText());
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
        reportNotice.setOnClickListener(this);

        Res.init(getActivity());
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.mipmap.icon_addpic_unfocused);
        PublicWay.activityList.add(getActivity());
        Init();
        return mView;
    }

    private void Init() {
        noScrollgridview = (GridView) mView.findViewById(R.id.report_evidence_gridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(getActivity());
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {//查看某个照片

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //    if (arg2 == Bimp.tempSelectBitmap.size()) {
               /* Intent intent = new Intent(getActivity(),
                        AlbumFragment.class);
                startActivity(intent);
                getActivity().getSupportFragmentManager().popBackStack();*/
                //     Toast.makeText(getActivity(), Bimp.tempSelectBitmap.get(arg2).imagePath, Toast.LENGTH_LONG).show();

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                R.anim.slide_in_left, R.anim.slide_out_left)
                        .addToBackStack(null)
                        .replace(R.id.reportContent, new AlbumFragment())
                        .commit();
            }
        });
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
            Log.d("qqq", "gridAdapter");
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if (Bimp.tempSelectBitmap.size() == 9) {
                return 9;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
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

            if (position == Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.icon_addpic_unfocused));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }
            return convertView;
        }


        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.update();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_reportevidence_back:
                //   Toast.makeText(getActivity(),"back",Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.toolbar_reportevidence_submit:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                R.anim.slide_in_left, R.anim.slide_out_left)
                        .addToBackStack(null)
                        .replace(R.id.reportContent, new ReportCommitFragment(), ReportCommitFragment.TAG)
                        .commit();
                for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
                    Log.d("qqq", Bimp.tempSelectBitmap.get(i).imagePath);
                }
                break;
            case R.id.report_evidence_notice:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                R.anim.slide_in_left, R.anim.slide_out_left)
                        .addToBackStack(null)
                        .replace(R.id.reportContent, new ReportNoticeFragment(), ReportNoticeFragment.TAG)
                        .commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {

        reportActivity.setReportText(reportText.getText().toString());
        super.onDestroyView();
    }
}