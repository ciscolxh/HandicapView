package com.loveluo.arcview.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author 罗富清
 * @date 2018/8/10.
 */
public class BigDecimalUtils {

    /**
     * 大于比较
     *
     * @param a a
     * @param b b
     * @return a>b
     */
    public static boolean greater(BigDecimal a, BigDecimal b) {
        int type = a.compareTo(b);
        return type > 0;
    }

    /**
     * 等于比较
     *
     * @param a a
     * @param b b
     * @return a==b
     */
    public static boolean equal(BigDecimal a, BigDecimal b) {
        int type = a.compareTo(b);
        return type == 0;
    }

    /**
     * 小于比较
     *
     * @param a a
     * @param b b
     * @return a<b
                    */
    public static boolean less(BigDecimal a, BigDecimal b) {
        int type = a.compareTo(b);
        return type < 0;
    }


    /**
     *
     * @param a a
     * @param b b
     * @return type 1:大于 、-1:小于、 0:等于
     */
    public static int compare (BigDecimal a, BigDecimal b){
        return a.compareTo(b);
    }


    /**
     * 计算涨幅
     * @param open 开
     * @param close 收
     * @return 返回涨幅
     */
    public static BigDecimal growth(BigDecimal open, BigDecimal close){

        if (open.compareTo(new BigDecimal(0))==0){
            return new BigDecimal(0);
        }
        return (close.subtract(open)).divide(open,4, BigDecimal.ROUND_HALF_DOWN);
    }


    /**
     *
     * @return 转换为百分比
     */
    public static String decimalToScale(BigDecimal scale){
        DecimalFormat df = new DecimalFormat("0.00%");
        if (scale.floatValue()>0){
            return "+" + df.format(scale);
        }
        return df.format(scale);
    }


    public static String fmort(BigDecimal num) {
        /*
         * 1000<=x < 100000 转换为k
         * x>100000 转换为m
         */
        int kilo = 1000;
        int thousand = 10000;
        int ten = 100000;
        if (num.floatValue() >= kilo && num.floatValue() < ten) {
            return num.divide(new BigDecimal(kilo), 2, BigDecimal.ROUND_DOWN).toPlainString() + "k";
        } else if (num.floatValue() >= ten) {
            return num.divide(new BigDecimal(thousand), 2, BigDecimal.ROUND_DOWN).toPlainString() + "m";
        } else {
            return num.toPlainString();
        }
    }


}
