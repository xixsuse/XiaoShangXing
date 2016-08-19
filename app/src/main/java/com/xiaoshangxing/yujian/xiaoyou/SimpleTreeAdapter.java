package com.xiaoshangxing.yujian.xiaoyou;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.xiaoshangxing.R;
import com.xiaoshangxing.yujian.xiaoyou.tree.Node;
import com.xiaoshangxing.yujian.xiaoyou.tree.TreeHelper;
import com.xiaoshangxing.yujian.xiaoyou.tree.TreeListViewAdapter;

import java.util.List;

public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T> {

    public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas,
                             int defaultExpandLevel) throws IllegalArgumentException,
            IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
    }

    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {

        int level = node.getLevel();
//        Log.d("qqq", "getlevel...");
//        Log.d("qqq", "level   " + node.getLevel() + "    position   " + position + "  name   " + node.getName1());

        ViewHolder viewHolder = null;

        if (level == 0) {
            convertView = mInflater.inflate(R.layout.item_xiaoyou0, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imge = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.text1 = (TextView) convertView.findViewById(R.id.text1);
            viewHolder.text2 = (TextView) convertView.findViewById(R.id.text2);
            viewHolder.divider = convertView.findViewById(R.id.divider);
            viewHolder.text1.setText(node.getName1());
            viewHolder.text2.setText(node.getName2());
            viewHolder.imge.setImageBitmap(node.getBitmap());
            if (node.isExpand()) {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.w3));
                viewHolder.divider.setVisibility(View.GONE);
            } else {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.w0));
                viewHolder.divider.setVisibility(View.VISIBLE);
            }
        } else if (level == 1) {
            convertView = mInflater.inflate(R.layout.item_xiaoyou1, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.text1 = (TextView) convertView.findViewById(R.id.text);
            viewHolder.divider = convertView.findViewById(R.id.divider);
            viewHolder.text1.setText(node.getName1());
            if (node.isExpand()) {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.w3));
                viewHolder.divider.setVisibility(View.GONE);
            } else {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.w0));
                viewHolder.divider.setVisibility(View.VISIBLE);
            }

        } else if (level == 2) {
            convertView = mInflater.inflate(R.layout.item_xiaoyou2, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.text1 = (TextView) convertView.findViewById(R.id.text);
            viewHolder.divider = convertView.findViewById(R.id.divider);
            viewHolder.text1.setText(node.getName1());
            if (node.isExpand()) {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.w3));
                viewHolder.divider.setVisibility(View.GONE);
            } else {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.w0));
                viewHolder.divider.setVisibility(View.VISIBLE);
            }

        } else if (level == 3) {
            convertView = mInflater.inflate(R.layout.item_xiaoyou3, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imge = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.text1 = (TextView) convertView.findViewById(R.id.text1);
            viewHolder.text2 = (TextView) convertView.findViewById(R.id.text2);
            viewHolder.text1.setText(node.getName1());
            viewHolder.text2.setText(node.getName2());
            viewHolder.imge.setImageBitmap(node.getBitmap());
        }

        return convertView;
    }

    private final class ViewHolder {
        ImageView imge;
        TextView text1;
        TextView text2;
        View divider;
    }


    public void addExtraNode(int position, String string) {
        Node node = mNodes.get(position);
        int indexOf = mAllNodes.indexOf(node);
        // Node
        Node extraNode = new Node(-1, node.getId(), string);
        extraNode.setParent(node);
        node.getChildren().add(extraNode);
        mAllNodes.add(indexOf + 1, extraNode);
        mNodes = TreeHelper.filterVisibleNode(mAllNodes);
        notifyDataSetChanged();
    }


    public void addExtraNodes(int position, List<XiaoyouBean> nodes) {
        Node node = mNodes.get(position);
        int indexOf = mAllNodes.indexOf(node);
        for (int i = 0; i < nodes.size(); i++) {
            Node extraNode = new Node(-1, node.getId(), nodes.get(i).getName1(), nodes.get(i).getName2(), nodes.get(i).getBitmap());
            extraNode.setParent(node);
            node.getChildren().add(extraNode);
            mAllNodes.add(indexOf + i + 1, extraNode);
        }
        mNodes = TreeHelper.filterVisibleNode(mAllNodes);
        notifyDataSetChanged();
    }

    public void removeChildrens(int position) {
        Node node = mNodes.get(position);
        for (int i = 0; i < node.getChildren().size(); i++) {
            mAllNodes.remove(node.getChildren().get(i));
        }
        mNodes = TreeHelper.filterVisibleNode(mAllNodes);
        notifyDataSetChanged();
    }

}
