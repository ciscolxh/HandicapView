package com.loveluo.arcview.entity;

/**
 *
 * Created by luo on 2017/6/10.
 */

public class DateEntity {
    private float scale;
    private int color;

    public DateEntity(float scale, int color) {
        this.scale = scale;
        this.color = color;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
