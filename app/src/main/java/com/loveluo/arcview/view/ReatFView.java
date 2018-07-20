package com.loveluo.arcview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.loveluo.arcview.entity.DateEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 条形统计图
 *
 * @author luo
 * @date 2017/6/11
 */

public class ReatFView extends View{
    private float w;
    private float h;
    private List<DateEntity> list = new ArrayList<>();
    private Paint paintLine;
    private Paint paint;

    public ReatFView(Context context) {
        this(context,null);
    }

    public ReatFView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ReatFView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paintLine = new Paint();
        paintLine.setColor(Color.GRAY);
        paintLine.setAntiAlias(true);
        paintLine.setStrokeWidth(3);



    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(0, h);
        float gauge = w * 0.02f;
        //横坐标
        canvas.drawLine(0, -gauge, w, -gauge, paintLine);
        //纵坐标
        canvas.drawLine(gauge, 0, gauge, -h, paintLine);
        canvas.drawLine(w - gauge, 0, w - gauge, -h, paintLine);
        for (int j = 1; j <= 10; j++) {
            //画出左边的线段
            canvas.drawLine(0, -((h - gauge * 2) / 10) * j, gauge, -((h - gauge * 2) / 10) * j, paintLine);
        }
        float distance = (w - gauge * 2) / list.size();
        float rectFw = distance / 2;
        float allData = getAllData();
        for (int i = 1; i <= list.size(); i++) {
            //画出横坐标上的线段
            canvas.drawLine(distance * i+gauge, 0, distance * i+gauge, -gauge, paintLine);
            RectF rectF = new RectF(gauge+(i-1)*distance + distance * 1 / 4, -list.get(i-1).getScale() /10*(h-2*gauge)-1, gauge+(i-1)*distance +distance * 3/ 4,-gauge-1 );
            paint.setColor(list.get(i-1).getColor());

            //开启抗锯齿
            paint.setAntiAlias(true);
            paint.setStrokeWidth(3);
            canvas.drawRect(rectF,paint);
            paint.reset();
        }



    }

    public void setList(List<DateEntity> list) {
        this.list = list;
    }


    public float getAllData() {
        float allData = 0;
        for (int i=0;i<list.size();i++){
            allData = allData +list.get(i).getScale();
        }
        return allData;
    }
}
