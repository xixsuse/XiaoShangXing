package com.xiaoshangxing.Network.netUtil;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.PublishNetwork;
import com.xiaoshangxing.data.PublishCache;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.yujian.IM.CustomMessage.ApplyPlanMessage;
import com.xiaoshangxing.yujian.IM.CustomMessage.TransmitMessage_NoImage;
import com.xiaoshangxing.yujian.IM.CustomMessage.TransmitMessage_WithImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by FengChaoQun
 * on 2016/9/19
 * 集中处理动态的有关操作
 */
public class OperateUtils {

    /**
     * description:动态操作处理方法  类型：赞  加入 举报 收藏
     *
     * @param publishedId     动态id
     * @param needRefreshData 是否刷新本地数据库
     * @param operate         操作类型
     * @param callback        回调
     * @return
     */

    public static void operate(final int publishedId, final Context context,
                               final boolean needRefreshData, String operate, final SimpleCallBack callback) {

        Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (callback != null) {
                    callback.onError(e);
                }
                e.printStackTrace();
                Toast.makeText(context, "操作失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(responseBody.string());

                    if (jsonObject.getString(NS.CODE).equals("30000004")) {

                        if (needRefreshData) {
                            PublishCache.reload(String.valueOf(publishedId), new PublishCache.publishedCallback() {
                                @Override
                                public void callback(Published published) {
                                    if (callback != null) {
                                        callback.onBackData(published);
                                    }
                                }
                            });
                        }

                        if (callback != null) {
                            callback.onSuccess();
                        }
                    } else {
                        Toast.makeText(context, jsonObject.getString(NS.MSG), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        JsonObject j = new JsonObject();
        j.addProperty(NS.USER_ID, TempUser.id);
        j.addProperty(NS.MOMENTID, publishedId);
        j.addProperty(NS.CATEGORY, operate);
        j.addProperty(NS.TIMESTAMP, NS.currentTime());
        PublishNetwork.getInstance().operate(subscriber, j, context);
    }

    /**
     * description:删除一条动态
     *
     * @param publishedId 动态id
     * @return
     */

    public static void deleteOnePublished(final int publishedId, final Context context, final IBaseView iBaseView,
                                          final SimpleCallBack callback) {
        ProgressSubscriberOnNext<ResponseBody> next = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                        case 8001:
                            Realm realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.where(Published.class).equalTo(NS.ID, publishedId).findFirst().deleteFromRealm();
                                }
                            });
                            realm.close();
                            iBaseView.showToast("删除成功");
                            if (callback != null) {
                                callback.onSuccess();
                            }
                            break;
                        default:
                            iBaseView.showToast(jsonObject.getString(NS.MSG));
                            if (callback != null) {
                                callback.onError(null);
                            }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };

        ProgressSubsciber<ResponseBody> subsciber = new ProgressSubsciber<>(next, iBaseView);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, TempUser.id);
        jsonObject.addProperty(NS.MOMENTID, publishedId);
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());
        PublishNetwork.getInstance().deletePublished(subsciber, jsonObject, context);
    }


    /**
     * description:转发一条动态给好友  目前仅限一次一人
     *
     * @param publishedId 动态id
     * @param categry     动态类型
     * @param personId    转发对象id
     * @param text1       转发时说的话  可为空
     * @return
     */

    public static void Tranmit(final int publishedId, String categry, final String personId,
                               final IBaseView iBaseView, final String text1, final SimpleCallBack callback) {
        IMMessage imMessage = null;

        if (categry.equals(NS.CATEGORY_REWARD) || categry.equals(NS.CATEGORY_HELP)) {
            TransmitMessage_NoImage noImage = new TransmitMessage_NoImage();
            noImage.setState_id(publishedId);
            imMessage = MessageBuilder.createCustomMessage(personId, SessionTypeEnum.P2P, noImage);
        } else if (categry.equals(NS.CATEGORY_SALE)) {
            TransmitMessage_WithImage transmitMessage_withImage = new TransmitMessage_WithImage();
            transmitMessage_withImage.setState_id(publishedId);
            imMessage = MessageBuilder.createCustomMessage(personId, SessionTypeEnum.P2P, transmitMessage_withImage);
        } else if (categry.equals(NS.CATEGORY_PLAN)) {
            ApplyPlanMessage applyPlanMessage = new ApplyPlanMessage();
            applyPlanMessage.setState_id(publishedId);
            imMessage = MessageBuilder.createCustomMessage(personId, SessionTypeEnum.P2P, applyPlanMessage);
        } else {
            return;
        }

        if (!TextUtils.isEmpty(text1)) {
            IMMessage text = MessageBuilder.createTextMessage(personId, SessionTypeEnum.P2P,
                    text1);
            sendTransmitMessage(imMessage, text, callback, iBaseView);
        } else {
            sendTransmitMessage(imMessage, null, callback, iBaseView);
        }
    }

    /**
     * description:发送消息
     *
     * @param imMessage 第一条消息
     * @param text      第二条消息
     * @param callBack  回调
     * @return
     */

    public static void sendTransmitMessage(IMMessage imMessage, final IMMessage text,
                                           final SimpleCallBack callBack, final IBaseView iBaseView) {

        NIMClient.getService(MsgService.class).sendMessage(imMessage, false).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (text != null) {
                    NIMClient.getService(MsgService.class).sendMessage(text, false);
                }
                if (callBack != null) {
                    callBack.onSuccess();
                }
            }

            @Override
            public void onFailed(int i) {
                iBaseView.showToast("分享失败:" + i);
            }

            @Override
            public void onException(Throwable throwable) {
                iBaseView.showToast("分享失败:异常");
                throwable.printStackTrace();
                if (callBack != null) {
                    callBack.onError(throwable);
                }
            }
        });
    }

    /**
     * description: 分享动态到校友圈
     *
     * @param type        动态类型
     * @param publishedId 动态id
     * @return
     */

    public static void Share(Context context, int type, int publishedId) {
        Intent intent = new Intent(context, InputActivity.class);
        intent.putExtra(InputActivity.EDIT_STATE, InputActivity.TRANSMIT);
        intent.putExtra(InputActivity.TRANSMIT_TYPE, type);
        intent.putExtra(InputActivity.MOMENTID, publishedId);
        context.startActivity(intent);
    }

    /**
     * description:判断是否已经赞过
     *
     * @param ids 动态点赞人的id串
     * @return
     */

    public static boolean isPraised(String ids) {
        if (TextUtils.isEmpty(ids)) {
            return false;
        }
        for (String i : ids.split(NS.SPLIT)) {
            if (TempUser.isMine(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * description:评论
     *
     * @param publishId       评论的动态id
     * @param commentId       评论的评论id
     * @param text            评论的内容
     * @param needRefreshData 评论后是否需要刷新数据库
     * @param callBack        回调
     * @return
     */

    public static void Comment(final int publishId, int commentId, String text, final Context context, final boolean needRefreshData,
                               final SimpleCallBack callBack) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, TempUser.id);
        jsonObject.addProperty(NS.MOMENTID, publishId);
        jsonObject.addProperty(NS.CONTENT, text);
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());
        if (commentId > 0) {
            jsonObject.addProperty(NS.COMMENTID, commentId);
        }

        Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                callBack.onSuccess();
            }

            @Override
            public void onError(Throwable e) {
                callBack.onError(e);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject1 = new JSONObject(responseBody.string());
                    switch (Integer.valueOf(jsonObject1.getString(NS.CODE))) {
                        case NS.CODE_200:
                            Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT).show();

                            if (needRefreshData) {
                                PublishCache.reload(String.valueOf(publishId), new PublishCache.publishedCallback() {
                                    @Override
                                    public void callback(Published published) {
                                        callBack.onBackData(published);
                                    }
                                });
                            }

                            break;
                        default:
                            Toast.makeText(context, jsonObject1.getString(NS.MSG), Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        PublishNetwork.getInstance().comment(subscriber, jsonObject, context);
    }
}
