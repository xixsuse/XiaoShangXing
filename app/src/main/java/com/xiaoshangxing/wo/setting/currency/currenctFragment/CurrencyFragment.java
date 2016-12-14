package com.xiaoshangxing.wo.setting.currency.currenctFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.xiaoshangxing.R;
import com.xiaoshangxing.wo.setting.DataSetting;
import com.xiaoshangxing.utils.baseClass.BaseFragment;
import com.xiaoshangxing.utils.customView.SwitchView;
import com.xiaoshangxing.utils.customView.dialog.DialogLocationAndSize;
import com.xiaoshangxing.utils.customView.dialog.DialogUtils;
import com.xiaoshangxing.utils.normalUtils.FileUtils;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15828 on 2016/7/14.
 */
public class CurrencyFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.cache_size)
    TextView cacheSize;
    @Bind(R.id.clean_cache)
    RelativeLayout cleanCache;
    @Bind(R.id.clean_chat_data)
    RelativeLayout cleanChatData;
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.currency_receivemode)
    SwitchView currencyReceivemode;
    @Bind(R.id.clearcache_rightarrow)
    ImageView clearcacheRightarrow;
    private View mView;
    private SwitchView receivemode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_currency, container, false);
        ButterKnife.bind(this, mView);
        title.setText("通用");
        more.setVisibility(View.GONE);

        receivemode = (SwitchView) mView.findViewById(R.id.currency_receivemode);
        receivemode.setState(DataSetting.isEarPhone(getActivity()));
        receivemode.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                SPUtils.put(getActivity(), SPUtils.EarPhone, true);
                receivemode.setState(true);
            }

            @Override
            public void toggleToOff() {
                SPUtils.put(getActivity(), SPUtils.EarPhone, false);
                receivemode.setState(false);
            }
        });

        initView();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setCloseActivity();
    }

    private void initView() {
        File glide_cache = new File(FileUtils.getGlideCache());
        File IM_cache = new File(FileUtils.getImCache());
        File Camera_cache = new File(FileUtils.getXSX_CameraPhotoPath());
        String size = FileUtils.getFormatSize(FileUtils.getFolderSize(glide_cache)
                + FileUtils.getFolderSize(IM_cache) + FileUtils.getFolderSize(Camera_cache));
        cacheSize.setText(size);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick({R.id.clean_cache, R.id.clean_chat_data, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clean_cache:
                final DialogUtils.Dialog_Center dialog_center = new DialogUtils.Dialog_Center(getContext());
                dialog_center.Message("确定清除缓存?").Button("确定", "取消")
                        .MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
                            @Override
                            public void onButton1() {
                                dialog_center.close();
                                showLoadingDialog("正在清除");
                                FileUtils.deleteFolderFile(FileUtils.getGlideCache(), false);
                                FileUtils.deleteFolderFile(FileUtils.getImCache(), false);
                                FileUtils.deleteFolderFile(FileUtils.getXSX_CameraPhotoPath(), false);
                                hideLoadingDialog();
                                initView();
                            }

                            @Override
                            public void onButton2() {
                                dialog_center.close();
                            }
                        });
                Dialog dialog = dialog_center.create();
                dialog.show();
                DialogLocationAndSize.setWidth(dialog, R.dimen.x780);
                break;
            case R.id.clean_chat_data:
                final DialogUtils.Dialog_Center dialog_center1 = new DialogUtils.Dialog_Center(getContext());
                dialog_center1.Message("确定清空所有聊天记录?").Button("确定", "取消")
                        .MbuttonOnClick(new DialogUtils.Dialog_Center.buttonOnClick() {
                            @Override
                            public void onButton1() {
                                dialog_center1.close();
                                NIMClient.getService(MsgService.class).clearMsgDatabase(true);
                                showToast("清除成功");
                            }

                            @Override
                            public void onButton2() {
                                dialog_center1.close();
                            }
                        });
                Dialog dialog1 = dialog_center1.create();
                dialog1.show();
                DialogLocationAndSize.setWidth(dialog1, R.dimen.x780);
                break;
            case R.id.back:
                getActivity().finish();
                break;
        }
    }
}
