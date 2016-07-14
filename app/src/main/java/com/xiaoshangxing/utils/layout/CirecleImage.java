package com.xiaoshangxing.utils.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 */
public class CirecleImage extends ImageView {
//    Path path;
//    public PaintFlagsDrawFilter mPaintFlagsDrawFilter;// 毛边过滤
//    Paint paint;
//
//    public CirecleImage(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        // TODO Auto-generated constructor stub
//        init();
//    }
//
//    public CirecleImage(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        // TODO Auto-generated constructor stub
//        init();
//    }
//
//    public CirecleImage(Context context) {
//        super(context);
//        // TODO Auto-generated constructor stub
//        init();
//    }
//    public void init(){
//        mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0,
//                Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
//        paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setFilterBitmap(true);
//        paint.setColor(Color.TRANSPARENT);
//
//    }
//
//    @Override
//    protected void onDraw(Canvas cns) {
//        // TODO Auto-generated method stub
//        float h = getMeasuredHeight()- 3.0f;
//        float w = getMeasuredWidth()- 3.0f;
//        if (path == null) {
//            path = new Path();
//            path.addCircle(
//                    w/2.0f
//                    , h/2.0f
//                    , (float) Math.min(w/2.0f, (h / 2.0))
//                    , Path.Direction.CCW);
//            path.close();
//        }
//        cns.drawCircle(w/2.0f, h/2.0f,  Math.min(w/2.0f, h / 2.0f) + 1.5f, paint);
//        int saveCount = cns.getSaveCount();
//        cns.save();
//        cns.setDrawFilter(mPaintFlagsDrawFilter);
//        cns.clipPath(path, Region.Op.REPLACE);
//        cns.setDrawFilter(mPaintFlagsDrawFilter);
//        cns.drawColor(Color.TRANSPARENT);
//        super.onDraw(cns);
//        cns.restoreToCount(saveCount);
//    }
    /**
     * 获取屏幕密度
     */
    private final float density = getContext().getResources().getDisplayMetrics().density;
    /**
     * �?
     */
    private float roundness;

    public CirecleImage(Context context) {
        super(context);

        init();
    }

    public CirecleImage(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CirecleImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    @Override
    public void draw(Canvas canvas) {
        final Bitmap composedBitmap;
        final Bitmap originalBitmap;
        final Canvas composedCanvas;
        final Canvas originalCanvas;
        final Paint paint;
        final int height;
        final int width;

        width = getWidth();

        height = getHeight();

        composedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        originalBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        composedCanvas = new Canvas(composedBitmap);
        originalCanvas = new Canvas(originalBitmap);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);

        super.draw(originalCanvas);

        composedCanvas.drawARGB(0, 0, 0, 0);

        composedCanvas.drawRoundRect(new RectF(0, 0, width, height), this.roundness, this.roundness, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        composedCanvas.drawBitmap(originalBitmap, 0, 0, paint);

        canvas.drawBitmap(composedBitmap, 0, 0, new Paint());
    }

    public float getRoundness() {
        return this.roundness / this.density;
    }

    public void setRoundness(float roundness) {
        this.roundness = roundness * this.density;
    }

    private void init() {
        // 括号中的数字是调整图片弧度的 调成100为圆形图�? 调成15为圆角图�?
        setRoundness(100);
    }
}
