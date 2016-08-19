package com.xiaoshangxing.input_activity.EmotAndPicture;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiaoshangxing.R;
import com.xiaoshangxing.input_activity.EmotionEdittext.DefQqEmoticons;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmojiBean;
import com.xiaoshangxing.input_activity.EmotionEdittext.EmotFilter.EmojiManager;
import com.xiaoshangxing.input_activity.InputActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EmotionGrideViewAdapter extends BaseAdapter {

	/** 上下文 */
	private Context ctx;

//	private InputActivity activity;
	private HashMap<String, Integer> hashMap = new HashMap<>();
	private List<EmojiBean> list=new ArrayList<EmojiBean>();
	private callBack mcallBack;
	public EmotionGrideViewAdapter(Context ctx/*, InputActivity activity*/) {
		this.ctx = ctx;
//		this.activity=activity;
		hashMap= DefQqEmoticons.sQqEmoticonHashMap;
		Iterator iterator=hashMap.entrySet().iterator();
		while (iterator.hasNext()){
			Map.Entry entry=(Map.Entry)iterator.next();
			EmojiBean bean=new EmojiBean((int)entry.getValue(),(String)entry.getKey());
			list.add(bean);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
//		return list.size();
		return EmojiManager.getDisplayCount();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = View.inflate(ctx, R.layout.item_imageview_84, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
		Glide.with(ctx).
//				load(list.get(position).icon)
		load("file:///android_asset/"+EmojiManager.getDisplayPath(ctx,position))
				.animate(R.anim.fade_in)
				.into(imageView);
//		imageView.setBackground(EmojiManager.getDisplayDrawable(ctx,position));
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mcallBack!=null){
					mcallBack.callback(EmojiManager.getDisplayText(position));
				}
//				activity.inputEmot(iconName);
			}
		});
		return view;
	}

	public List<EmojiBean> getList() {
		return list;
	}

	public void setMcallBack(callBack mcallBack) {
		this.mcallBack = mcallBack;
	}

	public void setList(List<EmojiBean> list) {
		this.list = list;
	}
	public static interface callBack{
		void callback(String emot);
	}
}
