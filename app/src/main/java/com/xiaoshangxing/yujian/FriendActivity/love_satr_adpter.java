package com.xiaoshangxing.yujian.FriendActivity;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.xiaoshangxing.Network.IMNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.yujian.ChatActivity.ChatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class love_satr_adpter extends ArrayAdapter<User> {
    private LoveOrStartActivity context;
    List<User> users;
    private int type;

    public love_satr_adpter(LoveOrStartActivity context, int resource, List<User> objects, int type) {
        super(context, resource, objects);
        this.context = context;
        this.users = objects;
        this.type=type;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_love_star_listview, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        final User user = users.get(position);
        final String userId = String.valueOf(user.getId());
        UserInfoCache.getInstance().getHeadIntoImage(userId, viewHolder.headImage);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.USER_NAME, viewHolder.name);
        UserInfoCache.getInstance().getExIntoTextview(userId, NS.COLLEGE, viewHolder.college);
        if (user.getSex().equals(2)) {
            viewHolder.sex.setImageResource(R.mipmap.sex_female);
        } else if (user.getSex().equals(1)) {
            viewHolder.sex.setImageResource(R.mipmap.sex_male);
        } else {
            viewHolder.sex.setVisibility(View.GONE);
        }

        if (type == LoveOrStartActivity.LOVE) {
            viewHolder.loveLay.setVisibility(View.VISIBLE);
            viewHolder.starLay.setVisibility(View.GONE);
            viewHolder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatActivity.start(context, String.valueOf(user.getId()), null, SessionTypeEnum.P2P);
                }
            });
        }else {
            viewHolder.loveLay.setVisibility(View.GONE);
            viewHolder.starLay.setVisibility(View.VISIBLE);
            viewHolder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favor(Integer.valueOf(userId));
                }
            });
        }

        viewHolder.headImage.setIntent_type(CirecleImage.PERSON_INFO, String.valueOf(userId));

        return convertView;
    }

    private void favor(int id) {

        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(e.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case 200:
                            Toast.makeText(context, "你们成为了好友啦", Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    context.getData();
                                }
                            }, 250);
                            break;
                        default:
                            Toast.makeText(context, jsonObject.getString(NS.MSG), Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        };

        ProgressSubsciber<ResponseBody> subscriber = new ProgressSubsciber<>(onNext, context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.USER_ID, TempUser.id);
        jsonObject.addProperty("oppositeUserId", id);
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());

        IMNetwork.getInstance().Favor(subscriber, jsonObject, context);
    }

    static class ViewHolder {
        @Bind(R.id.head_image)
        CirecleImage headImage;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.sex)
        ImageView sex;
        @Bind(R.id.college)
        TextView college;
        @Bind(R.id.love_lay)
        LinearLayout loveLay;
        @Bind(R.id.star_lay)
        LinearLayout starLay;
        @Bind(R.id.more)
        FrameLayout more;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
