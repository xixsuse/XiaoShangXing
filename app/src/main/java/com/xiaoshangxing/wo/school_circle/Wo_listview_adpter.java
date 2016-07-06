package com.xiaoshangxing.wo.school_circle;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoshangxing.R;
import com.xiaoshangxing.utils.CirecleImage;
import com.xiaoshangxing.utils.school_circle.Item_Comment;
import com.xiaoshangxing.utils.school_circle.PraisePeople;
import com.xiaoshangxing.wo.school_circle.check_photo.ImagePagerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class Wo_listview_adpter extends ArrayAdapter<String> {
    private Context context;
    private int resource;
    List<String> strings;

    public Wo_listview_adpter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.strings = objects;
        this.resource=resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        wo_viewholder viewholder;
        if (convertView==null){
            view=View.inflate(context,R.layout.item_wo_listview,null);
            viewholder=new wo_viewholder();
            viewholder.head=(CirecleImage)view.findViewById(R.id.head_image);
            viewholder.name=(TextView)view.findViewById(R.id.name);
            viewholder.college=(TextView)view.findViewById(R.id.college);
            viewholder.text=(TextView)view.findViewById(R.id.text);
            viewholder.photos1 =(NoScrollGridView)view.findViewById(R.id.photos1);
            viewholder.just_one=(ImageView)view.findViewById(R.id.just_one);
            viewholder.time=(TextView)view.findViewById(R.id.time);
            viewholder.praise=(ImageView)view.findViewById(R.id.praise);
            viewholder.comment=(ImageView)view.findViewById(R.id.comment);
            viewholder.praise_people=(LinearLayout)view.findViewById(R.id.praise_people);
            viewholder.comments=(LinearLayout)view.findViewById(R.id.comments);
            view.setTag(viewholder);
        }
        else {
            view=convertView;
            viewholder=(wo_viewholder) view.getTag();
        }


//测试图片
        String[] urls2={"http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg",
                "http://img.my.csdn.net/uploads/201410/19/1413698867_8323.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383264_3954.jpg",
                "http://img.my.csdn.net/uploads/201407/26/1406383264_4787.jpg"
        };

        final ArrayList<String> imageUrls = new ArrayList<>();
        for (int i=1;i<=5;i++){
            imageUrls.add(urls2[i]);
        }


        viewholder.photos1.setAdapter(new NoScrollGridAdapter(context,imageUrls));
        viewholder.photos1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(context, ImagePagerActivity.class);
                // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrls);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                context.startActivity(intent);
            }
        });

        if(viewholder.praise_people.getChildCount()!=0){
                viewholder.praise_people.removeAllViews();
        }
//        测试点赞的人
        PraisePeople praisePeople=new PraisePeople(context);
        praisePeople.addName("冯超群");
        praisePeople.addName("吴天阳");
        praisePeople.addName("王振华");
        praisePeople.addName("徐莹");
        praisePeople.addName("孙璐阳");
        praisePeople.addName("御天证道");
        viewholder.praise_people.addView(praisePeople.getTextView());

//        测试评论
        if (viewholder.comments.getChildCount()!=0){
            viewholder.comments.removeAllViews();
        }

        Item_Comment comment=new Item_Comment(context,"御天证道","哇哇哇哇哇哇哇哇哇哇哇哇");
        Item_Comment comment2=new Item_Comment(context,"王振华","孙璐阳","和哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈和");
        viewholder.comments.addView(comment.getTextView());
        viewholder.comments.addView(comment2.getTextView());
        return view;
    }

    private static class wo_viewholder{
        private CirecleImage head;
        private TextView name;
        private TextView college;
        private TextView text;
        private NoScrollGridView photos1;
        private ImageView just_one;
        private TextView time;
        private ImageView praise;
        private ImageView comment;
        private LinearLayout praise_people;
        private LinearLayout comments;
    }
}
