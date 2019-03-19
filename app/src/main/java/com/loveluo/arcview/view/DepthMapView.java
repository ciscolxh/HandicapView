package com.loveluo.arcview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.loveluo.arcview.utils.DisplayUtils;


/**
 * 股票深度图
 *
 * @author 罗富清
 * @date 2018/7/12.
 */
public class DepthMapView extends View {
    private int viewWidth;
    private int viewHeight;
    private float[] data = {4.22f, 2.76f, 3.6723f, 4.23f, 5.34f, 5.34f, 4.4f, 3.032f, 1.3f, 3.21f};
    private Paint paint;
    private Context context;

    private TextPaint titlePaint;

    private String[] title = {"盘口", "价格", "数量"};

    private Rect rect;

    public DepthMapView(Context context) {
        this(context, null);
    }

    public DepthMapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DepthMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        paint = new Paint();
        //开启抗锯齿
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        titlePaint = new TextPaint();
        //初始化标题的画笔
        titlePaint.setAntiAlias(true);
        titlePaint.setDither(true);
        titlePaint.setStrokeWidth(3);
        titlePaint.setTextSize(DisplayUtils.sp2px(context, 12));
        titlePaint.setColor(Color.parseColor("#6D7E81"));
        rect = new Rect();


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println("onMeasure-----宽度-----" + widthMeasureSpec);
        System.out.println("onMeasure-----宽度高度" + heightMeasureSpec);

        System.out.println("onMeasure-----宽度-----" + getMeasureWidth(widthMeasureSpec));
        System.out.println("onMeasure-----宽度高度" + getMeasureHeight(heightMeasureSpec));
    }

    @Override
    public boolean willNotCacheDrawing() {
        return super.willNotCacheDrawing();
    }

    private int getMeasureHeight(int heightMeasureSpec) {
        int result = 0;
        //每次调用此方法，测量用到的size会发生变化
        int size = MeasureSpec.getSize(heightMeasureSpec);
        //根据定义的Layout_width,Layout_height，会对此值产生影响
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else if (mode == MeasureSpec.UNSPECIFIED) {
            result = (int) (paint.measureText("") + getPaddingLeft() + getPaddingRight());
        } else {
            result = Math.min(result, size);
        }
        return result;
    }

    private int getMeasureWidth(int widthMeasureSpec) {
        int result = 0;
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else if (mode == MeasureSpec.UNSPECIFIED) {
            result = (int) (paint.measureText("") + getPaddingTop() + getPaddingBottom());
        } else {
            result = Math.min(result, size);
        }
        return result;
    }

    /**
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        System.out.println("onSizeChanged-----宽度-----" + w);
        System.out.println("onSizeChanged-----宽度高度" + h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        drawTitle(canvas);
        drawDepth(canvas);

    }


    /**
     * 画出顶部的文字
     *
     * @param canvas canvas
     */
    private void drawTitle(Canvas canvas) {


        titlePaint.getTextBounds(title[0], 0, title[0].length(), rect);
        float h = DisplayUtils.dip2px(context, 15.5f) + rect.height();
        canvas.drawText(title[0], 3, h, titlePaint);

        titlePaint.getTextBounds(title[1], 0, title[1].length(), rect);
        float w2 = viewWidth * 4 / 7 - rect.width();
        canvas.drawText(title[1], w2, h, titlePaint);

        titlePaint.getTextBounds(title[2], 0, title[2].length(), rect);
        float w3 = viewWidth - DisplayUtils.dip2px(context, 17) - rect.width();
        canvas.drawText(title[2], w3, h, titlePaint);

    }


    /**
     * 画出深度图
     *
     * @param canvas canvas
     */
    private void drawDepth(Canvas canvas) {

        float max = 0;
        for (float a : data) {
            if (a > max) {
                max = a;
            }
        }
        //深度图距离顶部的距离
        float itemMarginTop = DisplayUtils.dip2px(context, 37.5f);

        //中间价格
        titlePaint.getTextBounds(String.valueOf("5.03"), 0, String.valueOf("5.03").length(), rect);
        float priceH = rect.height();
        titlePaint.getTextBounds(String.valueOf("≈1000"), 0, String.valueOf("1000").length(), rect);
        float priceSmallHeight = rect.height();
        //深度图中间当前价格的高度
        float priceViewHeight = priceH + priceSmallHeight + DisplayUtils.dip2px(context, 20 + 2);
        //深度图的颜色
        float itemHeight = (viewHeight - itemMarginTop - priceViewHeight) / data.length;

        titlePaint.setColor(Color.parseColor("#04B987"));
        titlePaint.setTextSize(DisplayUtils.sp2px(context, 16));
        float priceY = itemMarginTop + itemHeight * data.length / 2 + DisplayUtils.dip2px(context, 10) + priceH;
        canvas.drawText(String.valueOf("5.03"), 3, priceY, titlePaint);

        titlePaint.setColor(Color.parseColor("#6D7E81"));
        titlePaint.setTextSize(DisplayUtils.sp2px(context, 12));
        float priceSmallY = priceY + priceSmallHeight + DisplayUtils.dip2px(context, 2);
        canvas.drawText(String.valueOf("≈1000"), 3, priceSmallY, titlePaint);


        //画出深度图部分
        for (int i = 0; i < data.length; i++) {
            //深度图
            float left = viewWidth - (data[i] / max) * viewWidth;
            float top;
            float bottom;
            if (i > data.length / 2 || i == data.length / 2) {
                top = i * itemHeight + itemMarginTop + priceViewHeight;
                bottom = (i + 1) * itemHeight + itemMarginTop + priceViewHeight;
                paint.setColor(Color.parseColor("#E5132D3D"));
            } else {
                top = i * itemHeight + itemMarginTop;
                bottom = (i + 1) * itemHeight + itemMarginTop;
                paint.setColor(Color.parseColor("#E52D2338"));
            }
            float right = viewWidth;
            System.out.println("左上右下----" + left + "----" + top + "----" + right + "----" + bottom);
            RectF r = new RectF(left, top, right, bottom);
            canvas.drawRect(r, paint);

            //深度图上的文字
            titlePaint.setColor(Color.parseColor("#6D7E81"));
            titlePaint.getTextBounds(String.valueOf(data[i]), 0, String.valueOf(data[i]).length(), rect);
            float h = top + rect.height() + (itemHeight - rect.height()) / 2;
            canvas.drawText(String.valueOf(data[i]), 3, h, titlePaint);

            if (i > data.length / 2 || i == data.length / 2) {
                titlePaint.setColor(Color.parseColor("#04B987"));
            } else {
                titlePaint.setColor(Color.parseColor("#FF5E5D"));
            }
            //深度图上的文字
            titlePaint.getTextBounds(String.valueOf(data[i]), 0, String.valueOf(data[i]).length(), rect);
            float w2 = viewWidth * 4 / 7 - rect.width();
            canvas.drawText(String.valueOf(data[i]), w2, h, titlePaint);
            //深度图上的文字
            titlePaint.setColor(Color.parseColor("#E5EBED"));
            titlePaint.getTextBounds(String.valueOf("5"), 0, String.valueOf("5").length(), rect);
            float w3 = viewWidth - DisplayUtils.dip2px(context, 17) - rect.width();
            canvas.drawText(String.valueOf("5"), w3, h, titlePaint);
            paint.reset();
        }
    }

}
