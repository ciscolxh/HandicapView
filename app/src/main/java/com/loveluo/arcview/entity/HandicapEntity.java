package com.loveluo.arcview.entity;

import java.math.BigDecimal;

import java.util.List;

/**
 * 盘口实体类
 * @author 罗富清
 * @date 2018/12/18.
 */
public class HandicapEntity {
    /**
     * symbol : BTC_USDT
     * sequenceId : 60601
     * timestamp : 1545032768245
     * price : 3285.5385
     * buyOrders : [{"price":3256.2096,"amount":0.1728},{"price":3256.1452,"amount":0.1315}]
     * sellOrders : [{"price":3287.9187,"amount":0.2302},{"price":3288.1658,"amount":0.2452}]
     */

    private String symbol;
    private int sequenceId;
    private BigDecimal timestamp;
    private BigDecimal price;
    private List<OrdersBean> buyOrders;
    private List<OrdersBean> sellOrders;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public BigDecimal getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(BigDecimal timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getPrice() {
        if (price==null){
            return new BigDecimal(0);
        }
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<OrdersBean> getBuyOrders() {
        return buyOrders;
    }

    public void setBuyOrders(List<OrdersBean> buyOrders) {
        this.buyOrders = buyOrders;
    }

    public List<OrdersBean> getSellOrders() {
        return sellOrders;
    }

    public void setSellOrders(List<OrdersBean> sellOrders) {
        this.sellOrders = sellOrders;
    }

    public static class OrdersBean {
        /**
         * price : 3256.2096
         * amount : 0.1728
         */

        private BigDecimal price;
        private BigDecimal amount;
        private BigDecimal depth;

        public OrdersBean() {

        }

        public OrdersBean(BigDecimal price, BigDecimal amount, BigDecimal depth) {
            this.price = price;
            this.amount = amount;
            this.depth = depth;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getDepth() {
            if (depth==null){
                return new BigDecimal(0);
            }
            return depth;
        }

        public void setDepth(BigDecimal depth) {
            this.depth = depth;
        }
    }


}
