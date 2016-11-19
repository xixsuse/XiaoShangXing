package com.xiaoshangxing.wo.WoFrafment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 自定义的“九宫格”——用在显示帖子详情的图片集合 
 * 解决的问题：GridView显示不全，只显示了一行的图片，比较奇怪，尝试重写GridView来解决
 * 
 * @author lichao
 * @since 2014-10-16 16:41
 * 
 */
public class NoScrollGridView extends GridView implements GestureDetector.OnGestureListener {
	private float downX;
	private float downY;
	private long downTime;
	private myOnLongClickListener onLongClickListener;
	GestureDetector gestureDetector = new GestureDetector(this);

	public NoScrollGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NoScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public NoScrollGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	//	自己实现长按回调
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		gestureDetector.onTouchEvent(ev);
		return super.onTouchEvent(ev);
	}

	public myOnLongClickListener getOnLongClickListener() {
		return onLongClickListener;
	}

	public void setOnLongClickListener(myOnLongClickListener onLongClickListener) {
		this.onLongClickListener = onLongClickListener;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		if (onLongClickListener != null) {
			onLongClickListener.onLongClick();
		}
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

	public interface myOnLongClickListener {
		void onLongClick();
	}
}
