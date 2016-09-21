package com.xiaoshangxing.xiaoshang.ShoolfellowHelp.HelpDetail;

import android.content.Context;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by FengChaoQun
 * on 2016/8/6
 */
public class HelpDetailPresenter implements HelpDetailContract.Presenter {
    private HelpDetailContract.View mView;
    private Context context;

    public HelpDetailPresenter(HelpDetailContract.View mView, Context context) {
        this.mView = mView;
        this.context = context;
    }

    @Override
    public void transmit(IMMessage imMessage, final IMMessage text) {
        NIMClient.getService(MsgService.class).sendMessage(imMessage, false).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (text != null) {
                    NIMClient.getService(MsgService.class).sendMessage(text, false);
                }
                mView.showTransmitSuccess();
            }

            @Override
            public void onFailed(int i) {
                mView.showToast("分享失败:" + i);
            }

            @Override
            public void onException(Throwable throwable) {
                mView.showToast("分享失败:异常");
                throwable.printStackTrace();
            }
        });

    }

    @Override
    public void praise() {
        mView.setPraise();
    }

    @Override
    public void share() {

    }
}
