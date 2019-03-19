package com.loveluo.arcview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.loveluo.arcview.R;
import com.loveluo.arcview.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 进度条
 * @author 罗富清
 * @date 2018/8/2.
 */
public class ProgressBarView extends View {

    private float width;

    private Context context;

    private int status;

    private Paint paint;

    private TextPaint textPaint;

    private Rect rect;

    private List<String> list = new ArrayList<>();

    public ProgressBarView(Context context) {
        this(context, null);
    }

    public ProgressBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initPaint();
        initRect();
        initData();
    }

    private void initData() {
        list.add("已提交");
        list.add("已完成");
        list.add("审核中");
        list.add("已汇出");
    }

    private void initRect() {
        rect = new Rect();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(20);
        //抗锯齿
        paint.setAntiAlias(true);
        //防抖动
        paint.setDither(true);
        paint.setStrokeWidth(DisplayUtils.dip2px(context, 1.7f));

        textPaint = new TextPaint();
        textPaint.setColor(ContextCompat.getColor(context, R.color.colorGray));
        textPaint.setTextSize(DisplayUtils.sp2px(context, 14f));
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
        drawCircle(canvas);
    }

    /**
     * 画圆的部分
     *
     * @param canvas canvas
     */
    private void drawCircle(Canvas canvas) {
        //统一圆心点的Y坐标
        float y = DisplayUtils.dip2px(context, 37.5f);
        //第一个圆 的横向坐标
        float startX = DisplayUtils.dip2px(context, 40f);

        //圆的半径
        float radius = DisplayUtils.dip2px(context, 5f);
        float bigRadius = DisplayUtils.dip2px(context, 7.5f);
        //线段长度
        float length = width - DisplayUtils.dip2px(context, 2 * 40f);
        //有四个点
        int max = 4;
        for (int i = 0; i < max; i++) {
            if (i == status) {
                paint.setColor(ContextCompat.getColor(context, R.color.colorBlue));
                canvas.drawCircle(startX + (float) i / 3 * length, y, radius, paint);
                paint.setColor(ContextCompat.getColor(context, R.color.colorBlueHalf));
                canvas.drawCircle(startX + (float) i / 3 * length, y, bigRadius, paint);
            } else {
                paint.setColor(ContextCompat.getColor(context, R.color.colorGray));
                canvas.drawCircle(startX + (float) i / 3 * length, y, radius, paint);
            }

            //画出文字
            textPaint.getTextBounds(list.get(i), 0, list.get(i).length(), rect);
            float h = DisplayUtils.dip2px(context, 55f) + rect.height();
            float w = startX + (float) i / 3 * length - rect.width() / 2;
            canvas.drawText(list.get(i), w, h, textPaint);
        }
        canvas.save();
    }

    /**
     * 画线段
     *
     * @param canvas canvas
     */
    private void drawLine(Canvas canvas) {
        float startX = DisplayUtils.dip2px(context, 40f);
        float startY = DisplayUtils.dip2px(context, 37.5f);

        float endX = width - startX;

        paint.setColor(ContextCompat.getColor(context, R.color.colorGray));
        canvas.drawLine(startX, startY, endX, startY, paint);
    }


    public void setStatus(int status) {
        this.status = status;
        //重新绘制
        invalidate();
    }


}
