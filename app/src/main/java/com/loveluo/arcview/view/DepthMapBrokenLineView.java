package com.loveluo.arcview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.loveluo.arcview.entity.CoordinateEntity;
import com.loveluo.arcview.entity.PriceEntity;
import com.loveluo.arcview.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 深度图折线图
 * @author 罗富清
 * @date 2018/7/12.
 */
public class DepthMapBrokenLineView extends View {
    private final static String TAG = "DepthMapLineView";
    private float viewHeight;
    private float viewWidth;

    private String buy = "买";
    private String sell = "卖";
    /**
     * 最大的刻度
     */
    private float maxAmount = 0;
    /**
     * 最小的刻度
     */
    private float mixAmount = Float.MAX_VALUE;

    private float marginTop;
    private float marginLeft;

    private float viewMarginTop;
    private float bottomTextHeight;

    private Context context;

    List<PriceEntity> list = new ArrayList<>();
    private Paint linePaint;
    private Paint paintDrop;
    private TextPaint paintText;
    private Rect rect;
    private Path path = new Path();
    private Path pathR = new Path();
    private float depthHeight;

    public DepthMapBrokenLineView(Context context) {
        this(context, null);
    }

    public DepthMapBrokenLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DepthMapBrokenLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void initMeasure() {
        marginTop = DisplayUtils.dip2px(context, 22.5f);
        marginLeft = viewWidth * 1 / 3;

    }

    private void init() {
        initData();
        rect = new Rect();
        maxNum();

        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#5004B987"));
        linePaint.setStrokeWidth(3);
        //抗锯齿
        linePaint.setAntiAlias(true);
        //防抖动
        linePaint.setDither(true);

        //画点的画笔
        paintDrop = new Paint();
        paintDrop.setColor(Color.RED);
        //抗锯齿
        paintDrop.setAntiAlias(true);
        //防抖动
        paintDrop.setDither(true);
        paintDrop.setStrokeCap(Paint.Cap.SQUARE);

        paintText = new TextPaint();
        //初始化标题的画笔
        paintText.setAntiAlias(true);
        paintText.setDither(true);
        paintText.setStrokeWidth(3);
        paintText.setTextSize(DisplayUtils.sp2px(context, 12));
        paintText.setColor(Color.parseColor("#6D7E81"));


    }

    /**
     * 初始化数据
     */
    private void initData() {
        //模拟初始化数据
        list.add(new PriceEntity(1, 345));
        list.add(new PriceEntity(2, 340));
        list.add(new PriceEntity(3, 332));
        list.add(new PriceEntity(4, 220));
        list.add(new PriceEntity(5, 205));
        list.add(new PriceEntity(6, 182));
        list.add(new PriceEntity(7, 169));
        list.add(new PriceEntity(8, 150));
        list.add(new PriceEntity(9, 131));
        list.add(new PriceEntity(10, 125));
        list.add(new PriceEntity(12, 120));
        list.add(new PriceEntity(13, 110));
        list.add(new PriceEntity(14, 100));
        list.add(new PriceEntity(15, 96));
        list.add(new PriceEntity(16, 92));
        list.add(new PriceEntity(17, 88));
        list.add(new PriceEntity(18, 87));
        list.add(new PriceEntity(19, 78));

        list.add(new PriceEntity(20, 75));

        list.add(new PriceEntity(21, 80));
        list.add(new PriceEntity(22, 92));
        list.add(new PriceEntity(23, 102));
        list.add(new PriceEntity(24, 113));
        list.add(new PriceEntity(25, 130));
        list.add(new PriceEntity(26, 149));
        list.add(new PriceEntity(27, 158));
        list.add(new PriceEntity(28, 179));
        list.add(new PriceEntity(29, 182));
        list.add(new PriceEntity(30, 201));
        list.add(new PriceEntity(31, 221));
        list.add(new PriceEntity(32, 234));
        list.add(new PriceEntity(33, 256));
        list.add(new PriceEntity(34, 278));
        list.add(new PriceEntity(35, 290));
        list.add(new PriceEntity(36, 312));
        list.add(new PriceEntity(37, 315));
        list.add(new PriceEntity(38, 323));
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = h;
        viewWidth = w;
        initMeasure();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDot(canvas);
        drawXScale(canvas);
        drawView(canvas);
        drawYScale(canvas);

    }

    /**
     * 绘制背景阴影部分
     *
     * @param canvas canvas
     */
    private void drawView(Canvas canvas) {
        path.moveTo(0, viewHeight - bottomTextHeight);
        List<CoordinateEntity> ls = new ArrayList<>();
        for (int i = 0; i <= list.size()/2; i++) {
            path.lineTo(
                    (float) i / (list.size() - 1) * viewWidth,
                    viewMarginTop + depthHeight - (list.get(i).getAmount() - mixAmount) / (maxAmount - mixAmount) * depthHeight
            );

            ls.add(new CoordinateEntity(
                            (float) i / (list.size() - 1) * viewWidth,
                            viewMarginTop + depthHeight - (list.get(i).getAmount() - mixAmount) / (maxAmount - mixAmount) * depthHeight
                    )
            );

        }
        path.close();
        //绘制左边的深度图
        linePaint.setColor(Color.parseColor("#0E04B987"));
        canvas.drawPath(path, linePaint);

        //绘制左边线条
        linePaint.setColor(Color.parseColor("#04B987"));
        for (int i=1;i<ls.size();i++){
            canvas.drawLine(ls.get(i-1).getX(),ls.get(i-1).getY(),ls.get(i).getX(),ls.get(i).getY(),linePaint);
        }

        ls.clear();
        pathR.moveTo(viewWidth / 2, viewHeight - bottomTextHeight);
        for (int i = list.size()/2; i < list.size(); i++) {
            pathR.lineTo(
                    (float) i / (list.size() - 1) * viewWidth,
                    viewMarginTop + depthHeight - (list.get(i).getAmount() - mixAmount) / (maxAmount - mixAmount) * depthHeight
            );
            ls.add(new CoordinateEntity(
                            (float) i / (list.size() - 1) * viewWidth,
                            viewMarginTop + depthHeight - (list.get(i).getAmount() - mixAmount) / (maxAmount - mixAmount) * depthHeight
                    )
            );
        }


        pathR.lineTo(viewWidth, viewHeight - bottomTextHeight);
        pathR.close();
        //绘制右边深度图
        linePaint.setColor(Color.parseColor("#0EFF5E5D"));
        canvas.drawPath(pathR, linePaint);
        //绘制右边线条
        linePaint.setColor(Color.parseColor("#FF5E5D"));
        for (int i=1;i<ls.size();i++){
            canvas.drawLine(ls.get(i-1).getX(),ls.get(i-1).getY(),ls.get(i).getX(),ls.get(i).getY(),linePaint);
        }
        ls.clear();


    }

    /**
     * 绘制横坐标
     *
     * @param canvas canvas
     */
    private void drawXScale(Canvas canvas) {
        String value;
        value = String.valueOf(list.get(0).getPrice());
        paintText.getTextBounds(value, 0, value.length(), rect);
        canvas.drawText(value, 2, viewHeight, paintText);

        value = String.valueOf(list.get((list.size() - 1) / 2).getPrice());
        paintText.getTextBounds(value, 0, value.length(), rect);
        canvas.drawText(value, viewWidth / 2 - rect.width() / 2, viewHeight, paintText);

        value = String.valueOf(list.get(list.size() - 1).getPrice());
        paintText.getTextBounds(value, 0, value.length(), rect);
        canvas.drawText(
                value,
                viewWidth - rect.width() -DisplayUtils.dip2px(context,5),
                viewHeight,
                paintText
        );

        bottomTextHeight = rect.height() + DisplayUtils.dip2px(context, 5);
        depthHeight = viewHeight - viewMarginTop - bottomTextHeight;
    }

    /**
     * 绘制纵坐标的刻度
     *
     * @param canvas canvas
     */
    private void drawYScale(Canvas canvas) {
        Log.e(TAG, "value:" + depthHeight);
        for (int i = 5; i > 0; i--) {
            String value = String.valueOf((maxAmount - mixAmount) / 5 * i + mixAmount);
            Log.e(TAG, "value:" + value);
            paintText.getTextBounds(value, 0, value.length(), rect);
            canvas.drawText(value, 2, (1 - (float) i / 5) * depthHeight + viewMarginTop + rect.height() / 2, paintText);
        }


    }

    /**
     * 绘制顶部的点 以及文字
     *
     * @param canvas canvas
     */
    private void drawDot(Canvas canvas) {
        paintText.getTextBounds(buy, 0, buy.length(), rect);
        paintDrop.setStrokeWidth(rect.height());
        paintDrop.setColor(Color.parseColor("#04B987"));
        paintText.setColor(Color.parseColor("#04B987"));
        viewMarginTop = marginTop + rect.height() + DisplayUtils.dip2px(context, 5);

        canvas.drawPoint(
                marginLeft + rect.height() / 2,
                marginTop + rect.height() / 2,
                paintDrop
        );

        canvas.drawText(
                buy,
                marginLeft + rect.height() + DisplayUtils.dip2px(context, 5),
                marginTop + rect.height() - DisplayUtils.dip2px(context, 1),
                paintText
        );

        paintText.getTextBounds(sell, 0, sell.length(), rect);
        paintDrop.setStrokeWidth(rect.height());

        paintDrop.setColor(Color.parseColor("#FF5E5D"));
        paintText.setColor(Color.parseColor("#FF5E5D"));
        canvas.drawPoint(
                viewWidth - marginLeft - rect.height() / 2,
                marginTop + rect.height() / 2,
                paintDrop
        );

        canvas.drawText(
                sell,
                viewWidth - marginLeft - rect.height() - DisplayUtils.dip2px(context, 5) - rect.width(),
                marginTop + rect.height() - DisplayUtils.dip2px(context, 1),
                paintText
        );

        paintText.setColor(Color.parseColor("#6D7E81"));
    }


    private void maxNum() {
        for (PriceEntity entity : list) {
            if (entity.getAmount() > maxAmount) {
                maxAmount = entity.getAmount();
            }
            if (entity.getAmount() < mixAmount) {
                mixAmount = entity.getAmount();
            }
        }
        Log.e(TAG, "max:" + maxAmount);
        Log.e(TAG, "min:" + mixAmount);
    }
}
