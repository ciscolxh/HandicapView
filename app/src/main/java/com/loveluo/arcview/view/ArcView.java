package com.loveluo.arcview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.loveluo.arcview.entity.DateEntity;

import java.util.ArrayList;
import java.util.List;

/**
 *自定义一个扇形。
 *
 * @author luo
 * @date 2017/6/10
 */

public class ArcView extends View{
    private List<DateEntity> list = new ArrayList<>();
    /**宽度**/
    private int w;
    /**高度**/
    private int h;
    private float radius;
    /**
     * 圆所在的内接矩形
     */
    private RectF rectF;
    /**
     * 画笔
     */
    private Paint paint;
    private Paint paintLine;
    /**
     * 变量
     */
    final float P = 3.14f;


    public ArcView(Context context) {
        this(context,null);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paintLine = new Paint();
        paintLine.setColor(Color.BLACK);
        paintLine.setStyle(Paint.Style.FILL);
        paintLine.setTextSize(20);
        //抗锯齿
        paintLine.setAntiAlias(true);
        //防抖动
        paintLine.setDither(true);
        paintLine.setStrokeWidth(3);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
    }

    /**
     * 只要重新绘制，必须要重写OnDraw方法。
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startAngle = 0;
        initData();
        //算出来总共分了多少份
        float scale = allPoint();
        canvas.save();
        canvas.translate(w/2,h/2);
        for (int i=0;i<list.size();i++){
            setPaint(i);
            //终点角度
            float sweepAngle = list.get(i).getScale()/scale*360;
            Log.e("终止角度",String.valueOf(sweepAngle-1));
            //本次起始角度，加上本次终点的角度的一半
            //画出扇形
            canvas.drawArc(rectF,startAngle,sweepAngle,true,paint);
            //这个是扇形中点的角度。
            float central = startAngle+((sweepAngle)/2);
            Log.e("圆心角",String.valueOf(central));
            //每个扇形的角度
            startAngle = startAngle + list.get(i).getScale()/scale*360;
            Log.e("每个扇形的角度",String.valueOf(startAngle));
            float startX = radius*(float) Math.cos(central*P/180);
            float startY = radius*(float)Math.sin(central*P/180);
            float endX = (radius*1.1f)*(float) Math.cos(central*P/180);
            float endY = (radius*1.1f)*(float)Math.sin(central*P/180);
            float textX = (radius*1.2f)*(float) Math.cos(central*P/180);
            float textY = (radius*1.2f)*(float)Math.sin(central*P/180);
            //画出线段
            canvas.drawLine(startX,startY,endX,endY,paintLine);
            if (central>=90&&central<135){
                textY = textY+radius*0.1f;
                textX = textX-radius*0.2f;
            } else if (central>=135&&central<225) {
                textX = textX-radius*0.2f;
            }else if (central>=225&&central<=270){
                textX = textX-radius*0.1f;
            }
            //画出百分比
            canvas.drawText((float)(Math.round(sweepAngle/360*100*100))/100+"%",textX,textY,paintLine);
            //初始化画笔
            paint.reset();

        }

    }

    /**
     * 设置数据
     * @param i
     */
    private void setPaint(int i) {
        paint.setStyle(Paint.Style.FILL);
        //抗锯齿
        paint.setAntiAlias(true);
        //防抖动
        paint.setDither(true);
        paint.setStrokeWidth(5);
        paint.setColor(list.get(i).getColor());
    }

    /**
     * 初始化数据
     */
    private void initData() {
        radius = Math.min(w,h)/2*0.7f;
        rectF = new RectF(-radius,-radius,radius,radius);
    }

    public void setList(List<DateEntity> list) {
        this.list = list;
    }

    /**
     * @return 计算出总共的份数
     */
    private  float allPoint(){
        float point= 0;
        for (int i=0;i<list.size();i++){
            point = point+list.get(i).getScale();
        }
        return point;
    }

}
