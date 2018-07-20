package com.loveluo.arcview.entity;

/**
 * @author 罗富清
 * @date 2018/7/13.
 */
public class CoordinateEntity {
    private float x;
    private float y;

    public CoordinateEntity(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
        
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
