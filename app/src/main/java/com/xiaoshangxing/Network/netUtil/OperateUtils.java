package com.xiaoshangxing.Network.netUtil;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.xiaoshangxing.Network.IMNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.PublishNetwork;
import com.xiaoshangxing.data.PublishCache;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.input_activity.InputActivity;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.yujian.IM.CustomMessage.ApplyPlanMessage;
import com.xiaoshangxing.yujian.IM.CustomMessage.CustomAttachmentType;
import com.xiaoshangxing.yujian.IM.CustomMessage.TransmitMessage_NoImage;
import com.xiaoshangxing.yujian.IM.CustomMessage.TransmitMessage_WithImage;
import com.xiaoshangxing.yujian.IM.cache.FriendDataCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param isCancle        是否是取消操作
     * @param callback        回调
     */

    public static void operate(final int publishedId, final Context context, final boolean needRefreshData,
                               String operate, boolean isCancle, final SimpleCallBack callback) {

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
                            PublishCache.reload(String.valueOf(publishedId), new SimpleCallBack() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onBackData(Object o) {
                                    if (callback != null) {
                                        callback.onBackData(o);
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
        if (isCancle) {
            PublishNetwork.getInstance().cancleOperate(subscriber, j, context);
        } else {
            PublishNetwork.getInstance().operate(subscriber, j, context);
        }

    }

    /**
     * description:删除一条动态
     *
     * @param publishedId 动态id
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
     *description:删除多条动态
     *@param publishedIds 动态id组
     */

    public static void deletePublisheds(final List<String> publishedIds, final Context context, final IBaseView iBaseView,
                                        final SimpleCallBack callback) {

        if (publishedIds.size() == 0) {
            return;
        }

        final List<String> arrayList = /*new ArrayList<>();*/
                Collections.synchronizedList(new ArrayList<String>());

        iBaseView.showLoadingDialog("删除中");
        for (final String id : publishedIds) {
            Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    if (callback != null) {
                        callback.onError(e);
                        iBaseView.hideLoadingDialog();
                    }
                }

                @Override
                public void onNext(ResponseBody responseBody) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody.string());
                        switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                            case 8001:
                                Realm realm = Realm.getDefaultInstance();
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.where(Published.class).equalTo(NS.ID, Integer.valueOf(id)).findFirst().deleteFromRealm();
                                    }
                                });
                                realm.close();
                                arrayList.add(id);
                                if (arrayList.size() == publishedIds.size() && callback != null) {
                                    callback.onSuccess();
                                    iBaseView.hideLoadingDialog();
                                }
                                break;
                            default:
                                iBaseView.showToast(jsonObject.getString(NS.MSG));
                                if (callback != null) {
                                    callback.onError(null);
                                    iBaseView.hideLoadingDialog();
                                }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (callback != null) {
                            callback.onError(null);
                            iBaseView.hideLoadingDialog();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        if (callback != null) {
                            callback.onError(null);
                            iBaseView.hideLoadingDialog();
                        }
                    }
                }
            };

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(NS.USER_ID, TempUser.id);
            jsonObject.addProperty(NS.MOMENTID, id);
            jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());
            PublishNetwork.getInstance().deletePublished(subscriber, jsonObject, context);
        }


    }


    /**
     * description:转发一条动态给一个或多个好友
     *
     * @param publishedId 动态id
     * @param categry     动态类型
     * @param personIds   转发对象id组
     * @param text1       转发时说的话  可为空
     */
    public static void Tranmit(final int publishedId, String categry, final List<String> personIds,
                               final IBaseView iBaseView, final String text1, final SimpleCallBack callback) {
        List<IMMessage> imMessages = new ArrayList<>();
        TransmitMessage_NoImage noImage = null;
        TransmitMessage_WithImage transmitMessage_withImage = null;
        int type;
        switch (categry) {
            case NS.CATEGORY_REWARD:
                noImage = new TransmitMessage_NoImage(CustomAttachmentType.Reward);
                noImage.setState_id(publishedId);
                for (String personId : personIds) {
                    IMMessage imMessage = MessageBuilder.createCustomMessage(personId, SessionTypeEnum.P2P, noImage);
                    imMessages.add(imMessage);
                }
                type = CustomAttachmentType.Reward;
                break;
            case NS.CATEGORY_HELP:
                noImage = new TransmitMessage_NoImage(CustomAttachmentType.Help);
                noImage.setState_id(publishedId);
                for (String personId : personIds) {
                    IMMessage imMessage = MessageBuilder.createCustomMessage(personId, SessionTypeEnum.P2P, noImage);
                    imMessages.add(imMessage);
                }
                type = CustomAttachmentType.Help;
                break;
            case NS.CATEGORY_PLAN:
                noImage = new TransmitMessage_NoImage(CustomAttachmentType.Plan);
                noImage.setState_id(publishedId);
                for (String personId : personIds) {
                    IMMessage imMessage = MessageBuilder.createCustomMessage(personId, SessionTypeEnum.P2P, noImage);
                    imMessages.add(imMessage);
                }
                type = CustomAttachmentType.Plan;
                break;
            case NS.CATEGORY_SALE:
                transmitMessage_withImage = new TransmitMessage_WithImage(CustomAttachmentType.Sale);
                transmitMessage_withImage.setState_id(publishedId);
                for (String personId : personIds) {
                    IMMessage imMessage = MessageBuilder.createCustomMessage(personId, SessionTypeEnum.P2P, transmitMessage_withImage);
                    imMessages.add(imMessage);
                }
                type = CustomAttachmentType.Sale;
                break;
            case NS.APPLY_PLAN:
                ApplyPlanMessage applyPlanMessage = new ApplyPlanMessage();
                applyPlanMessage.setState_id(publishedId);
                for (String personId : personIds) {
                    IMMessage imMessage = MessageBuilder.createCustomMessage(personId, SessionTypeEnum.P2P, applyPlanMessage);
                    imMessages.add(imMessage);
                }
                type = CustomAttachmentType.ApplyPlan;
                break;
            default:
                return;
        }

        if (!TextUtils.isEmpty(text1)) {
            List<IMMessage> texts = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put(NS.TYPE, type);
            for (String personId : personIds) {
                IMMessage imMessage = MessageBuilder.createTextMessage(personId, SessionTypeEnum.P2P, text1);
                imMessage.setRemoteExtension(map);
                texts.add(imMessage);
            }
            sendTransmitMessage(imMessages, texts, callback, iBaseView);
        } else {
            sendTransmitMessage(imMessages, null, callback, iBaseView);
        }
    }

    /**
     * description:发送一条动态给多个好友
     *
     * @param imMessages 第一条消息组
     * @param texts      第二条消息组
     * @param callBack   回调
     */
    public static void sendTransmitMessage(final List<IMMessage> imMessages, final List<IMMessage> texts,
                                           final SimpleCallBack callBack, final IBaseView iBaseView) {

        final List<String> count = new ArrayList<>();

        for (int i = 0; i < imMessages.size(); i++) {
            IMMessage imMessage = imMessages.get(i);
            IMMessage text = null;
            if (texts != null && texts.size() > i) {
                text = texts.get(i);
            }

            final IMMessage finalText = text;
            NIMClient.getService(MsgService.class).sendMessage(imMessage, false).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    if (finalText != null) {
                        NIMClient.getService(MsgService.class).sendMessage(finalText, false);
                    }
                    count.add("1");
                    if (count.size() == imMessages.size() && callBack != null) {
                        callBack.onSuccess();
                    }
                }

                @Override
                public void onFailed(int i) {
                    iBaseView.showToast("操作失败:" + i);
                }

                @Override
                public void onException(Throwable throwable) {
                    iBaseView.showToast("操作失败:异常");
                    throwable.printStackTrace();
                    if (callBack != null) {
                        callBack.onError(throwable);
                    }
                }
            });
        }
    }

    /**
     * description:转发多条动态给一个好友
     *
     * @param publishedIds 动态id组
     * @param categry      动态类型
     * @param personId     转发对象id组
     * @param text1        转发时说的话  可为空
     */
    public static void TranmitMoreToOne(final List<String> publishedIds, String categry, final String personId,
                                        final IBaseView iBaseView, final String text1, final SimpleCallBack callback) {
        List<IMMessage> imMessages = new ArrayList<>();
        int type;
        switch (categry) {
            case NS.CATEGORY_REWARD:
                for (String publishedId : publishedIds) {
                    TransmitMessage_NoImage noImage = new TransmitMessage_NoImage(CustomAttachmentType.Reward);
                    noImage.setState_id(Integer.parseInt(publishedId));
                    IMMessage imMessage = MessageBuilder.createCustomMessage(personId, SessionTypeEnum.P2P, noImage);
                    imMessages.add(imMessage);
                }
                type = CustomAttachmentType.Reward;
                break;
            case NS.CATEGORY_HELP:
                for (String publishedId : publishedIds) {
                    TransmitMessage_NoImage noImage = new TransmitMessage_NoImage(CustomAttachmentType.Help);
                    noImage.setState_id(Integer.parseInt(publishedId));
                    IMMessage imMessage = MessageBuilder.createCustomMessage(personId, SessionTypeEnum.P2P, noImage);
                    imMessages.add(imMessage);
                }
                type = CustomAttachmentType.Help;
                break;
            case NS.CATEGORY_PLAN:
                for (String publishedId : publishedIds) {
                    TransmitMessage_NoImage noImage = new TransmitMessage_NoImage(CustomAttachmentType.Plan);
                    noImage.setState_id(Integer.parseInt(publishedId));
                    IMMessage imMessage = MessageBuilder.createCustomMessage(personId, SessionTypeEnum.P2P, noImage);
                    imMessages.add(imMessage);
                }
                type = CustomAttachmentType.Plan;
                break;
            case NS.CATEGORY_SALE:
                for (String publishedId : publishedIds) {
                    TransmitMessage_WithImage transmitMessage_withImage = new TransmitMessage_WithImage(CustomAttachmentType.Sale);
                    transmitMessage_withImage.setState_id(Integer.parseInt(publishedId));
                    IMMessage imMessage = MessageBuilder.createCustomMessage(personId, SessionTypeEnum.P2P, transmitMessage_withImage);
                    imMessages.add(imMessage);
                }
                type = CustomAttachmentType.Sale;
                break;
            case NS.APPLY_PLAN:
                for (String publishedId : publishedIds) {
                    ApplyPlanMessage applyPlanMessage = new ApplyPlanMessage();
                    applyPlanMessage.setState_id(Integer.parseInt(publishedId));
                    IMMessage imMessage = MessageBuilder.createCustomMessage(personId, SessionTypeEnum.P2P, applyPlanMessage);
                    imMessages.add(imMessage);
                }
                type = CustomAttachmentType.ApplyPlan;
                break;
            default:
                return;
        }

        if (!TextUtils.isEmpty(text1)) {
            Map<String, Object> map = new HashMap<>();
            map.put(NS.TYPE, type);
            IMMessage imMessage = MessageBuilder.createTextMessage(personId, SessionTypeEnum.P2P, text1);
            imMessage.setRemoteExtension(map);
            sendTransmitMessagesToOne(imMessages, imMessage, callback, iBaseView);
        } else {
            sendTransmitMessagesToOne(imMessages, null, callback, iBaseView);
        }
    }

    /**
     * description:发送多条校上动态给一个好友
     *
     * @param imMessages 第一条消息组
     * @param text       第二条消息
     * @param callBack   回调
     */
    public static void sendTransmitMessagesToOne(final List<IMMessage> imMessages, final IMMessage text,
                                                 final SimpleCallBack callBack, final IBaseView iBaseView) {

        final List<String> count = new ArrayList<>();

        for (IMMessage imMessage : imMessages) {
            NIMClient.getService(MsgService.class).sendMessage(imMessage, false).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    count.add("1");
                    if (count.size() == imMessages.size()) {
                        if (callBack != null) {
                            callBack.onSuccess();
                        }
                        if (text != null) {
                            NIMClient.getService(MsgService.class).sendMessage(text, false);
                        }
                    }
                }

                @Override
                public void onFailed(int i) {
                    iBaseView.showToast("操作失败:" + i);
                    if (callBack != null) {
                        callBack.onError(null);
                    }
                }

                @Override
                public void onException(Throwable throwable) {
                    iBaseView.showToast("操作失败:异常");
                    throwable.printStackTrace();
                    if (callBack != null) {
                        callBack.onError(throwable);
                    }
                }
            });
        }

    }


    /**
     * description: 分享动态到校友圈
     *
     * @param type        动态类型
     * @param publishedId 动态id
     */

    public static void Share(Context context, int type, int publishedId) {
        Intent intent = new Intent(context, InputActivity.class);
        intent.putExtra(InputActivity.EDIT_STATE, InputActivity.TRANSMIT);
        intent.putExtra(InputActivity.TRANSMIT_TYPE, type);
        intent.putExtra(InputActivity.MOMENTID, publishedId);
        context.startActivity(intent);
    }


    /**
     * description:评论
     *
     * @param publishId       评论的动态id
     * @param commentId       评论的评论id
     * @param text            评论的内容
     * @param needRefreshData 评论后是否需要刷新数据库
     * @param callBack        回调
     */

    public static void Comment(final int publishId, int commentId, String text, final Context context,
                               final boolean needRefreshData, final SimpleCallBack callBack) {
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

            }

            @Override
            public void onError(Throwable e) {
                callBack.onError(e);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject1 = new JSONObject(responseBody.string());
                    switch (jsonObject1.getInt(NS.CODE)) {
                        case NS.CODE_200:
                            Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT).show();

                            if (needRefreshData) {
                                PublishCache.reload(String.valueOf(publishId), new SimpleCallBack() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onBackData(Object o) {
                                        callBack.onBackData(o);
                                    }
                                });
                            }
                            callBack.onSuccess();
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

    /**
     * description:改变动态的状态信息  结束或未结束
     *
     * @param publishId       动态id
     * @param statu           动态当前状态 0表示结束 1表示未结束
     * @param needRefreshData 是否需要刷新
     * @param callBack        回调
     */

    public static void ChangeStatu(final int publishId, int statu, final Context context, final boolean needRefreshData,
                                   final SimpleCallBack callBack) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, TempUser.id);
        jsonObject.addProperty(NS.MOMENTID, publishId);
        jsonObject.addProperty(NS.STATU, statu);

        Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
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
                        case 50000014:
                            callBack.onSuccess();
                            if (needRefreshData) {
                                PublishCache.reload(String.valueOf(publishId), new SimpleCallBack() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onBackData(Object o) {
                                        callBack.onBackData(o);
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

        PublishNetwork.getInstance().changePublishStatu(subscriber, jsonObject, context);
    }

    /**
     * description:留心某人
     *
     * @param account        留心对象的id
     * @param simpleCallBack 回调
     */

    public static void Favor(String account, final Context context, final IBaseView iBaseView, final SimpleCallBack simpleCallBack) {
        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(e.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case 200:
                            simpleCallBack.onSuccess();
                            break;
                        default:
                            iBaseView.showToast(jsonObject.getString(NS.MSG));
                            break;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        };

        ProgressSubsciber<ResponseBody> subscriber = new ProgressSubsciber<>(onNext, iBaseView);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, TempUser.id);
        jsonObject.addProperty("oppositeUserId", account);
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());

        IMNetwork.getInstance().Favor(subscriber, jsonObject, context);
    }

    /**
     * description:取消留心某人  如果是好友 则删除好友  (该方法也是校上行APP上删除好友的方法)
     *
     * @param account        对象id
     * @param simpleCallBack 回调
     */

    public static void CancelFavor(final String account, final Context context, final IBaseView iBaseView, final SimpleCallBack simpleCallBack) {
        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(e.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case 200:
                            if (FriendDataCache.getInstance().isMyFriend(account)) {
                                deleteFriend(account, context, simpleCallBack);
                            } else {
                                simpleCallBack.onSuccess();
                            }
                            break;
                        default:
                            if (FriendDataCache.getInstance().isMyFriend(account)) {
                                deleteFriend(account, context, simpleCallBack);
                            } else {
                                iBaseView.showToast(jsonObject.getString(NS.MSG));
                            }
                            break;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        };

        ProgressSubsciber<ResponseBody> subscriber = new ProgressSubsciber<>(onNext, iBaseView);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, TempUser.id);
        jsonObject.addProperty("oppositeUserId", account);

        IMNetwork.getInstance().CancelFavor(subscriber, jsonObject, context);
    }

    /**
     * description:删除好友
     *
     * @param account        对方id
     * @param simpleCallBack 回调
     * @return
     */

    public static void deleteFriend(String account, final Context context, final SimpleCallBack simpleCallBack) {
        NIMClient.getService(FriendService.class).deleteFriend(account).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                simpleCallBack.onSuccess();
            }

            @Override
            public void onFailed(int i) {
                Toast.makeText(context, "删除失败:" + i, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable throwable) {
                Toast.makeText(context, "删除失败:异常", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }
        });
    }
}
