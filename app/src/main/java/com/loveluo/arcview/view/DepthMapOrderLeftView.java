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
import android.util.Log;
import android.view.View;

import com.loveluo.arcview.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 买卖盘深度图
 *
 * @author 罗富清
 * @date 2018/7/14.
 */
public class DepthMapOrderLeftView extends View {

    private Context context;
    private Paint paint;
    private TextPaint textPaint;
    private float viewHeight;
    private float viewWidth;
    private float max;
    private static final String TAG = "DepthMapOrderLeftView";

    private List<Float> list = new ArrayList<>();
    private RectF rectF;
    private Rect rect;


    public DepthMapOrderLeftView(Context context) {
        this(context, null);
    }

    public DepthMapOrderLeftView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DepthMapOrderLeftView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initPaint();
        initTextPaint();
        initRect();
        initDate();
        initMaxValue();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure_Width:" + getMeasureWidth(widthMeasureSpec));
        Log.e(TAG, "onMeasure_Height" + getMeasureHeight(heightMeasureSpec));
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
     * 初始化矩形，画背景色的时候需要
     */
    private void initRect() {
        rectF = new RectF();
        rect = new Rect();
    }

    private void initDate() {
        for (int i = 1; i <= 20; i++) {
            list.add((float) (i + i * 8));
        }
        Log.e(TAG, "ListSize:" + list.size());
    }

    private void initTextPaint() {
        textPaint = new TextPaint();
        //设置文字大小
        textPaint.setTextSize(DisplayUtils.sp2px(context, 12));
        //开启防抖动
        textPaint.setDither(true);
        //开启防锯齿
        textPaint.setAntiAlias(true);

    }

    private void initPaint() {
        paint = new Paint();
        //开启抗锯齿
        paint.setAntiAlias(true);
        //开启防抖动
        paint.setDither(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = h;
        Log.e(TAG, "viewHeight:" + viewHeight);

        viewWidth = w;
        Log.e(TAG, "viewWidth:" + viewWidth);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float itemHeight = viewHeight / list.size();
        Log.e(TAG, "itemHeight:" + itemHeight);
        drawLeftView(canvas, itemHeight);
        drawRightView(canvas, itemHeight);
    }

    /**
     * 画出右边的布局
     *
     * @param canvas     canvas
     * @param itemHeight 条目高度
     */
    private void drawRightView(Canvas canvas, float itemHeight) {
        for (int i = 0; i < list.size(); i++) {
            float left = viewWidth / 2;
            float top = i * itemHeight;
            float right = viewWidth / 2 + list.get(i) / max * viewWidth / 2;
            float bottom = (i + 1) * itemHeight;

            rectF.set(left, top, right, bottom);
            paint.setColor(Color.parseColor("#E52D2338"));
            canvas.drawRect(rectF, paint);

            drawRightPrice(canvas, itemHeight, i);

            drawRightNum(canvas, itemHeight, i);

            drawRightNo(canvas, itemHeight, i);

        }
    }

    private void drawRightNo(Canvas canvas, float itemHeight, int i) {
        String value = String.valueOf(i + 1);
        textPaint.getTextBounds(value, 0, value.length(), rect);
        float textMarinTop = (itemHeight - rect.height()) / 2;
        float textHeight = (i * itemHeight + itemHeight) - textMarinTop;
        float textWidth = viewWidth - rect.width() - DisplayUtils.dip2px(context, 3);
        textPaint.setColor(Color.parseColor("#6D7E81"));
        canvas.drawText(value, textWidth, textHeight, textPaint);
    }

    /**
     * 画出右边的数量
     *
     * @param canvas     画布
     * @param itemHeight 条目高度
     * @param i          第几条
     */
    private void drawRightNum(Canvas canvas, float itemHeight, int i) {
        String num = String.valueOf(list.get(i));
        textPaint.getTextBounds(num, 0, num.length(), rect);
        float textMarinTop = (itemHeight - rect.height()) / 2;
        float textHeight = (i * itemHeight + itemHeight) - textMarinTop;
        float textWidth = viewWidth - DisplayUtils.dip2px(context, 30) - rect.width();
        textPaint.setColor(Color.parseColor("#E5EBED"));
        canvas.drawText(num, textWidth, textHeight, textPaint);
    }

    /**
     * 画出右边的价格
     *
     * @param canvas     canvas
     * @param itemHeight 条目高度
     * @param i          i
     */
    private void drawRightPrice(Canvas canvas, float itemHeight, int i) {
        float textMarinTop;
        float textHeight;
        String price = String.valueOf(list.get(i));
        textPaint.getTextBounds(price, 0, price.length(), rect);
        textMarinTop = (itemHeight - rect.height()) / 2;
        textHeight = (i * itemHeight + itemHeight) - textMarinTop;
        textPaint.setColor(Color.parseColor("#FF5E5D"));
        float textWidth = viewWidth / 2 + DisplayUtils.dip2px(context, 10);
        canvas.drawText(price, textWidth, textHeight, textPaint);
    }

    /**
     * 画出左边的布局
     *
     * @param canvas     canvas
     * @param itemHeight 条目高度
     */
    private void drawLeftView(Canvas canvas, float itemHeight) {
        for (int i = 0; i < list.size(); i++) {
            float left = (viewWidth / 2) - (list.get(i) / max * (viewWidth / 2));
            float top = i * itemHeight;
            float right = viewWidth / 2;
            float bottom = i * itemHeight + itemHeight;
            Log.e(TAG, "item" + i + "\nLeft:" + left + "Top:" + top + "Right:" + right + "Bottom:" + bottom);
            rectF.set(left, top, right, bottom);
            paint.setColor(Color.parseColor("#E5132D3D"));
            canvas.drawRect(rectF, paint);

            //画出序号
            drawLeftNo(canvas, itemHeight, i);

            //画出数量
            drawLeftNum(canvas, itemHeight, i);

            //画出价格
            drawLeftPrice(canvas, itemHeight, i);


        }
    }

    /**
     * 画出左边的价格文字
     *
     * @param canvas     c
     * @param itemHeight 条目高度
     * @param i          第几条
     */
    private void drawLeftPrice(Canvas canvas, float itemHeight, int i) {
        float textMarinTop;
        float textHeight;
        String price = String.valueOf(list.get(i));
        textPaint.getTextBounds(price, 0, price.length(), rect);
        textMarinTop = (itemHeight - rect.height()) / 2;
        textHeight = (i * itemHeight + itemHeight) - textMarinTop;
        textPaint.setColor(Color.parseColor("#04B987"));
        float textWidth = viewWidth / 2 - DisplayUtils.dip2px(context, 10) - rect.width();
        canvas.drawText(price, textWidth, textHeight, textPaint);
    }

    /**
     * 画出左边的数量文字
     *
     * @param canvas     canvas
     * @param itemHeight 条目高度
     * @param i          第几条
     */
    private void drawLeftNum(Canvas canvas, float itemHeight, int i) {
        float textMarinTop;
        float textHeight;
        String num = String.valueOf(list.get(i));
        textPaint.getTextBounds(num, 0, num.length(), rect);
        textMarinTop = (itemHeight - rect.height()) / 2;
        textHeight = (i * itemHeight + itemHeight) - textMarinTop;
        textPaint.setColor(Color.parseColor("#E5EBED"));
        canvas.drawText(num, DisplayUtils.dip2px(context, 30), textHeight, textPaint);
    }

    /**
     * 画出左边的序号文字
     *
     * @param canvas     画布
     * @param itemHeight 条目高度
     * @param i          第几条
     */
    private void drawLeftNo(Canvas canvas, float itemHeight, int i) {
        float textMarinTop;
        float textHeight;
        String value = String.valueOf(i + 1);
        textPaint.getTextBounds(value, 0, value.length(), rect);
        textMarinTop = (itemHeight - rect.height()) / 2;
        textHeight = (i * itemHeight + itemHeight) - textMarinTop;
        textPaint.setColor(Color.parseColor("#6D7E81"));
        canvas.drawText(value, 0, textHeight, textPaint);
    }

    /**
     * 找出最大值
     */
    private void initMaxValue() {
        for (float f : list) {
            if (f > max) {
                max = f;
                Log.e(TAG, "Max：" + max);
            }
        }
    }

}
