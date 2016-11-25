package com.xiaoshangxing.xiaoshang.Help.HelpDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xiaoshangxing.Network.PublishNetwork;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.Published;
import com.xiaoshangxing.data.TransmitInfo;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotinText;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.utils.layout.Name;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by FengChaoQun
 * on 2016/7/20
 */
public class TransmitListFrafment extends BaseFragment implements IBaseView {
    private RecyclerView recyclerView;
    private TextView emptyText;
    private Published published;
    private List<TransmitInfo> transmitInfos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_comment_list,null);

        GetDataFromActivity activity = (GetDataFromActivity) getActivity();
        published = (Published) (activity.getData());
        if (published==null){
            return view;
        }

        recyclerView=(RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        emptyText=(TextView)view.findViewById(R.id.empty_text);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getTransmitInfo();
    }

    private void getTransmitInfo() {
        Subscriber<ResponseBody> subscriber1 = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    switch (Integer.valueOf(jsonObject.getString(NS.CODE))) {
                        case 50000004:
                            Gson gson = new Gson();
                            transmitInfos = gson.fromJson(jsonObject.getJSONArray(NS.MSG).toString(),
                                    new TypeToken<List<TransmitInfo>>() {
                                    }.getType());
                            recyclerView.setAdapter(new HomeAdapter());
                            if (transmitInfos.size() < 1) {
                                recyclerView.setVisibility(View.GONE);
                                emptyText.setVisibility(View.VISIBLE);
                                emptyText.setText("赶紧转发一下");
                            }

                            break;
                        default:
                            recyclerView.setVisibility(View.GONE);
                            emptyText.setVisibility(View.VISIBLE);
                            emptyText.setText("赶紧转发一下");
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.MOMENTID, published.getId());
        PublishNetwork.getInstance().getTransmitInfo(subscriber1, jsonObject, getContext());

    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view=LayoutInflater.from(
                    getContext()).inflate(R.layout.item_transmit_list_recycleview, parent,
                    false);
            MyViewHolder holder = new MyViewHolder(view);
            holder.name=(Name) view.findViewById(R.id.name);
            holder.college=(TextView)view.findViewById(R.id.college);
            holder.time=(TextView)view.findViewById(R.id.time);
            holder.transmitLoction=(TextView)view.findViewById(R.id.transmit_loaction);
            holder.text=(EmotinText) view.findViewById(R.id.text);
            holder.headImage=(CirecleImage)view.findViewById(R.id.head_image);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            TransmitInfo transmitInfo = transmitInfos.get(position);
            String userId = transmitInfo.getUserId();
            UserInfoCache.getInstance().getHeadIntoImage(userId, holder.headImage);
            UserInfoCache.getInstance().getExIntoTextview(userId, NS.USER_NAME, holder.name);
            UserInfoCache.getInstance().getExIntoTextview(userId, NS.COLLEGE, holder.college);
            holder.text.setText(TextUtils.isEmpty(transmitInfo.getComment()) ? "" : transmitInfo.getComment());
            holder.headImage.setIntent_type(CirecleImage.PERSON_INFO, String.valueOf(userId));
        }

        @Override
        public int getItemCount()
        {
            return transmitInfos.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            TextView college, time, transmitLoction;
            Name name;
            EmotinText text;
            CirecleImage headImage;
            public MyViewHolder(View view)
            {
                super(view);
            }
        }
    }
}
