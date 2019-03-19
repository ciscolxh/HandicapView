package com.loveluo.arcview.view;

import android.graphics.Canvas;

public interface HandicapViewInterface {
    /**
     * 初始化画笔
     */
    void initPaint();

    /**
     * 初始化数据
     */
    void initData();

    /**
     * 初始化标题
     */
    void initTitle();

    /**
     * 绘制标题
     */
    void drawTitle(Canvas canvas);

    /**
     * 绘制盘口上半部分
     */
    void drawHandicapTopView(Canvas canvas);

    /**
     * 绘制当前价格
     */
    void drawNowPrice(Canvas canvas);

    /**
     * 绘制盘口下半部分
     */
    void drawHandicapDownView(Canvas canvas);

    /**
     * 设置数据(socket 抓到数据后更新盘口显示)
     * @param json json
     */
    void updateData(String json);

}
