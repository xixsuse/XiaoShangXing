package com.xiaoshangxing.yujian.groupchatInfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.netease.nimlib.sdk.team.constant.TeamMemberType;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.R;
import com.xiaoshangxing.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.layout.CirecleImage;
import com.xiaoshangxing.yujian.IM.NimUIKit;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;
import com.xiaoshangxing.yujian.IM.cache.SimpleCallback;
import com.xiaoshangxing.yujian.IM.cache.TeamDataCache;
import com.xiaoshangxing.yujian.groupchatInfo.deleteMember.DeleteMemberActivity;
import com.xiaoshangxing.yujian.personInfo.PersonInfoActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 15828 on 2016/8/12.
 */
public class Adapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    List<TeamMember> data;
    private ChatInfoActivity activity;
    private boolean isMyteam = false;

    public Adapter(Context context, List<TeamMember> data, ChatInfoActivity activity) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.activity = activity;
        TeamDataCache.getInstance().fetchTeamMember(activity.getAccount(), NimUIKit.getAccount(), new SimpleCallback<TeamMember>() {
            @Override
            public void onResult(boolean success, TeamMember result) {
                if (result.getType() == TeamMemberType.Owner) {
                    isMyteam = true;
                }
            }
        });
    }

    public List<TeamMember> getData() {
        return data;
    }

    public void setData(List<TeamMember> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 2;
        } else {
            return data.size() + 2;
        }

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview_chatinfo, parent, false);
            holder = new ViewHolder();
            holder.img = (CirecleImage) convertView.findViewById(R.id.gridview_chatinfo_img);
            holder.name = (TextView) convertView.findViewById(R.id.gridview_chatinfo_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == getCount() - 2) {
            Glide.with(context).load(R.mipmap.privacy_add).into(holder.img);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SelectPersonActivity.class);
                    ArrayList<String> arrayList = new ArrayList<String>();
                    for (int i = 0; i < data.size(); i++) {
                        arrayList.add(data.get(i).getAccount());
                    }
                    intent.putExtra(SelectPersonActivity.LOCKED, arrayList);
                    activity.startActivityForResult(intent, SelectPersonActivity.SELECT_PERSON_CODE);
                }
            });
            holder.name.setText("");
        } else if (position == getCount() - 1) {
            Glide.with(context).load(R.mipmap.privacy_remove).into(holder.img);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isMyteam) {
                        Intent intent = new Intent(context, DeleteMemberActivity.class);
                        intent.putExtra(IntentStatic.DATA,activity.getAccount());
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "只有群主可以踢人", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            holder.name.setText("");

        } else if (data.size() != 0 ) {
            final NimUserInfo userInfo = NimUserInfoCache.getInstance().getUserInfo(data.get(position).getAccount());
            Glide.with(context)
                    .load(userInfo.getAvatar())
                    .placeholder(R.mipmap.greyblock)
                    .error(R.mipmap.cirecleimage_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img);
            holder.name.setText(NimUserInfoCache.getInstance().getUserDisplayName(data.get(position).getAccount()));
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PersonInfoActivity.class);
                    intent.putExtra(IntentStatic.EXTRA_ACCOUNT, userInfo.getAccount());
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }


    class ViewHolder {
        public CirecleImage img;
        public TextView name;
    }

}
