package com.loveluo.arcview.entity;

/**
 * @author 罗富清
 * @date 2018/7/13.
 */
public class PriceEntity {
    private float price ;

    private float amount;

    public PriceEntity(float price, float amount) {
        this.price = price;
        this.amount = amount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
