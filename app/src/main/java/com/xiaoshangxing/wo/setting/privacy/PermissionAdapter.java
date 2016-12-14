package com.xiaoshangxing.wo.setting.privacy;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.xiaoshangxing.R;
import com.xiaoshangxing.publicActivity.SelectPerson.SelectPersonActivity;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.utils.customView.CirecleImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2016/10/25
 */

public class PermissionAdapter extends BaseAdapter {

    private List<String> accounts = new ArrayList<>();
    private Context context;
    private String add = "add";
    private String reduce = "reduce";
    private boolean isDelete;
    private Fragment fragment;

    public PermissionAdapter(List<String> accounts, Fragment fragment) {
        this.accounts = accounts;
        this.context = fragment.getContext();
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        if (accounts == null || accounts.size() == 0) {
            return 1;
        } else {
            return accounts.size() + 2;
        }
    }

    @Override
    public Object getItem(int position) {
        return accounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_privacy_adapter, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Log.d("count", "-" + getCount());
        viewHolder.privacyAdd.setImageResource(R.mipmap.privacy_add);
        if (getCount() == 1) {
            initAdd(viewHolder);
        } else {
            if (position == getCount() - 1) {
                initDelete(viewHolder);
                Log.d("count" + position, "-" + "initDelete");
            } else if (position == getCount() - 2) {
                initAdd(viewHolder);
                Log.d("count" + position, "-" + "initAdd");
            } else {
                initView(viewHolder, accounts.get(position));
                Log.d("count" + position, "-" + "initView");
            }
        }

        return convertView;
    }

    private void initAdd(ViewHolder viewHolder) {
        Glide.with(context).load(R.mipmap.privacy_add).into(viewHolder.privacyAdd);
        viewHolder.privacyAdd.setImageResource(R.mipmap.privacy_add);
        viewHolder.privacyReddelete.setVisibility(View.GONE);
        viewHolder.privacyAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDelete(false);
                notifyDataSetChanged();
                Intent intent = new Intent(context, SelectPersonActivity.class);
                intent.putExtra(SelectPersonActivity.LOCKED, (ArrayList) accounts);
                fragment.startActivityForResult(intent, SelectPersonActivity.SELECT_PERSON_CODE);
            }
        });
    }

    private void initDelete(ViewHolder viewHolder) {
        Glide.with(context).load(R.mipmap.privacy_remove).into(viewHolder.privacyAdd);
        viewHolder.privacyReddelete.setVisibility(View.GONE);
        viewHolder.privacyAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDelete(!isDelete());
                notifyDataSetChanged();
            }
        });
    }

    private void initView(ViewHolder viewHolder, final String id) {
        UserInfoCache.getInstance().getHeadIntoImage(id, viewHolder.privacyAdd);
        viewHolder.privacyReddelete.setVisibility(isDelete ? View.VISIBLE : View.GONE);
        viewHolder.privacyAdd.setIntent_type(CirecleImage.PERSON_INFO, id);
        viewHolder.privacyReddelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accounts.remove(id);
                notifyDataSetChanged();
            }
        });
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public List<String> getAccounts() {
        return accounts;
    }

    static class ViewHolder {
        @Bind(R.id.privacy_add)
        CirecleImage privacyAdd;
        @Bind(R.id.privacy_reddelete)
        CirecleImage privacyReddelete;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
