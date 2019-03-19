package com.loveluo.arcview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.loveluo.arcview.R;
import com.loveluo.arcview.entity.HandicapEntity;
import com.loveluo.arcview.utils.BigDecimalUtils;
import com.loveluo.arcview.utils.DataRequestUtils;
import com.loveluo.arcview.utils.DisplayUtils;
import com.loveluo.arcview.utils.JsonUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



/**
 * 盘口显示
 *
 * @author 罗富清
 * @date 2018/8/13.
 */
public class DepthView extends View {
    private int viewWidth;
    private int viewHeight;

    private int priceDecimal = 2;
    private int numberDecimal = 2;

    private List<HandicapEntity.OrdersBean> buyList = new ArrayList<>();

    private List<HandicapEntity.OrdersBean> sellList = new ArrayList<>();

    private float allNumber = 5f;

    private Paint paint;

    private Context context;

    private TextPaint titlePaint;

    private String[] title = new String[3];

    private Rect rect;


    /**
     * 美元价格
     */
    private BigDecimal nowPrice = new BigDecimal("0");
    private BigDecimal allBuy = new BigDecimal("0");
    private BigDecimal allSell = new BigDecimal("0");
    private BigDecimal maxNum = new BigDecimal("1");

    private PriceChangeListener priceListener;

    public interface PriceChangeListener {
        /**
         * 买入价格更新
         * @param max 买入价格
         */
        void buyPrice(BigDecimal max);

        /**
         * 返回卖出价格
         * @param min 卖出价格
         */
        void sellPrice(BigDecimal min);
    }

    public void setPriceListener(PriceChangeListener priceListener) {
        this.priceListener = priceListener;
    }

    public DepthView(Context context) {
        this(context, null);
    }

    public DepthView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DepthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
        initTitleText();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String json = DataRequestUtils.getStringFromAssert(context, "depth_list.json");
        HandicapEntity entity = JsonUtils.deserialize(json, HandicapEntity.class);
        nowPrice = entity.getPrice();
        BigDecimal lastDecimal = new BigDecimal("0");
        //初始化买盘列表的数据
        for (int i = 0; i < allNumber; i++) {
            if (i < entity.getBuyOrders().size()) {
                HandicapEntity.OrdersBean bean = entity.getBuyOrders().get(i);
                bean.setDepth(bean.getAmount().add(lastDecimal));
                lastDecimal = bean.getDepth();
                allBuy = allBuy.add(bean.getAmount());
                buyList.add(bean);
            } else {
                HandicapEntity.OrdersBean bean = new HandicapEntity.OrdersBean(
                        new BigDecimal("0"),
                        new BigDecimal("0").add(lastDecimal),
                        new BigDecimal("0").add(lastDecimal)
                );
                buyList.add(
                        bean
                );
            }


        }

        lastDecimal = new BigDecimal("0");

        //初始化卖盘列表的数据
        for (int i = 0; i < allNumber; i++) {
            if (i < entity.getSellOrders().size()) {
                HandicapEntity.OrdersBean bean = entity.getSellOrders().get(i);
                bean.setDepth(bean.getAmount().add(lastDecimal));
                lastDecimal = bean.getDepth();
                sellList.add(bean);
                allSell = allSell.add(bean.getAmount());
            } else {
                sellList.add(
                        new HandicapEntity.OrdersBean(
                                new BigDecimal("0"),
                                new BigDecimal("0"),
                                new BigDecimal("0").add(lastDecimal)
                        )
                );
            }
        }
        BigDecimal max = BigDecimalUtils.greater(allBuy, allSell) ? allBuy : allSell;
        if (max.compareTo(new BigDecimal(0))>0){
            maxNum = max;
        }
    }

    /**
     * 初始化标题文字
     */
    private void initTitleText() {
        title[0] = context.getString(R.string.handicap);
        title[1] = context.getString(R.string.price);
        title[2] = context.getString(R.string.number);

    }

    /**
     * 初始化
     */
    private void init(Context context) {
        paint = new Paint();
        //开启抗锯齿
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        titlePaint = new TextPaint();
        //初始化标题的画笔
        titlePaint.setAntiAlias(true);
        titlePaint.setDither(true);
        titlePaint.setStrokeWidth(3);
        titlePaint.setTextSize(DisplayUtils.sp2px(context, 12));
        titlePaint.setColor(Color.parseColor("#A8AFB7"));
        rect = new Rect();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean willNotCacheDrawing() {
        return super.willNotCacheDrawing();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        drawTitle(canvas);
        drawDepth(canvas);
    }

    /**
     * 画出顶部的文字
     *
     * @param canvas canvas
     */
    private void drawTitle(Canvas canvas) {
        //获取文字的具体位置
        titlePaint.getTextBounds(title[0], 0, title[0].length(), rect);
        float h = DisplayUtils.dip2px(context, 15.5f) + rect.height();
        canvas.drawText(title[0], 3, h, titlePaint);

        titlePaint.getTextBounds(title[1], 0, title[1].length(), rect);
        float w2 = viewWidth * 4 / 7 - rect.width();
        canvas.drawText(title[1], w2, h, titlePaint);

        titlePaint.getTextBounds(title[2], 0, title[2].length(), rect);
        float w3 = viewWidth - DisplayUtils.dip2px(context, 17) - rect.width();
        canvas.drawText(title[2], w3, h, titlePaint);
    }


    /**
     * 画出深度图
     *
     * @param canvas canvas
     */
    private void drawDepth(Canvas canvas) {
        //深度图距离顶部的距离
        float itemMarginTop = DisplayUtils.dip2px(context, 37.5f);
        //中间价格
        titlePaint.setTextSize(DisplayUtils.sp2px(context, 16));
        //当前币价
        String showPrice = nowPrice.setScale(priceDecimal, BigDecimal.ROUND_DOWN).toPlainString();
        // 计算价格
//        String fexSymbol = (String) SPUtils.get(context, SPUtils.FEX, FexUtils.getFex(context));
        String coinPrice = "≈ 0.5";
//               nowPrice.multiply(MyApplication.getFex(fexSymbol)).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
        //加一个标识符 例如：￥5
        //折合币价
//        String amountToPrice = CoinPriceHelper.getCoinPrice(context, coinPrice, fexSymbol);
        String amountToPrice = "￥5";

        BigDecimal bugOne = buyList.get(0).getPrice().setScale(priceDecimal, BigDecimal.ROUND_DOWN);

        // 当前价格 如果大于 买一，是蓝色 否则是红色
        if (BigDecimalUtils.greater(nowPrice,bugOne)){
            titlePaint.setColor(Color.parseColor("#4386F9"));
        }else {
            titlePaint.setColor(Color.parseColor("#EC2828"));
        }

        titlePaint.getTextBounds(showPrice, 0, showPrice.length(), rect);
        //实时价格绿色的显示高度
        float priceH = rect.height();
        titlePaint.setTextSize(DisplayUtils.sp2px(context, 12));
        titlePaint.getTextBounds(amountToPrice, 0, amountToPrice.length(), rect);
        float priceSmallHeight = rect.height();
        //深度图中间当前价格的高度
        float priceViewHeight = priceH + priceSmallHeight + DisplayUtils.dip2px(context, 20 + 5);
        //每个深度图的高度
        float itemHeight = (viewHeight - itemMarginTop - priceViewHeight) / (2 * allNumber);
        titlePaint.setTextSize(DisplayUtils.sp2px(context, 16));
        float priceY = itemMarginTop + itemHeight * allNumber + DisplayUtils.dip2px(context, 10) + priceH;
        canvas.drawText(showPrice, 3, priceY, titlePaint);
        titlePaint.setColor(Color.parseColor("#A8AFB7"));
        titlePaint.setTextSize(DisplayUtils.sp2px(context, 12));
        float priceSmallY = priceY + priceSmallHeight + DisplayUtils.dip2px(context, 5);
        canvas.drawText("≈" + amountToPrice, 3, priceSmallY, titlePaint);

        //画出深度图上部分
        for (int i = 0; i < allNumber; i++) {
            //卖盘部分
            float left = viewWidth - (sellList.get(4 - i).getDepth().floatValue() / maxNum.floatValue()) * viewWidth;
            float top;
            float bottom;
            top = (i) * itemHeight + itemMarginTop;
            bottom = (i + 1) * itemHeight + itemMarginTop;
            if (BigDecimalUtils.greater(sellList.get(4 - i).getAmount(), new BigDecimal("0"))) {
                paint.setColor(Color.parseColor("#1A4386F9"));
                float right = viewWidth;
                RectF r = new RectF(left, top, right, bottom);
                canvas.drawRect(r, paint);
            }

            //深度图上的文字
            titlePaint.setColor(Color.parseColor("#A8AFB7"));
            titlePaint.getTextBounds(String.valueOf(4 - i + 1), 0, String.valueOf(4 - i + 1).length(), rect);
            float h = top + rect.height() + (itemHeight - rect.height()) / 2;
            canvas.drawText(String.valueOf(4 - i + 1), 3, h, titlePaint);
            titlePaint.setColor(Color.parseColor("#4386F9"));
            //深度图上的文字
            String price;
            if (BigDecimalUtils.equal(sellList.get(4 - i).getPrice(), new BigDecimal("0"))) {
                price = "——";
            } else {
                price = sellList.get(4 - i).getPrice().setScale(priceDecimal, BigDecimal.ROUND_DOWN).toPlainString();
            }
            titlePaint.getTextBounds(price, 0, price.length(), rect);
            float w2 = viewWidth * 4 / 7 - rect.width();
            canvas.drawText(price, w2, h, titlePaint);
            //深度图上的文字
            titlePaint.setColor(Color.parseColor("#A8AFB7"));
            String number;
            if (BigDecimalUtils.equal(sellList.get(4 - i).getAmount(), new BigDecimal("0"))) {
                number = "——";
            } else {
                number = BigDecimalUtils.fmort(sellList.get(4 - i).getAmount().setScale(numberDecimal, BigDecimal.ROUND_DOWN));
            }
            titlePaint.getTextBounds(number, 0, number.length(), rect);
            float w3 = viewWidth - DisplayUtils.dip2px(context, 17) - rect.width();
            canvas.drawText(number, w3, h, titlePaint);
            paint.reset();
        }

        //画出深度图下半部分
        for (int i = 0; i < allNumber; i++) {
            //卖盘部分
            float left = viewWidth - (buyList.get(i).getDepth().floatValue() / maxNum.floatValue()) * viewWidth;
            float top;
            float bottom;

            top = (i + 5) * itemHeight + itemMarginTop + priceViewHeight;
            bottom = (i + 5 + 1) * itemHeight + itemMarginTop + priceViewHeight;
            if (BigDecimalUtils.greater(buyList.get(i).getAmount(), new BigDecimal("0"))) {
                paint.setColor(Color.parseColor("#1AEC2828"));
                float right = viewWidth;
                RectF r = new RectF(left, top, right, bottom);
                canvas.drawRect(r, paint);
            }
            //深度图上的文字
            titlePaint.setColor(Color.parseColor("#A8AFB7"));
            titlePaint.getTextBounds(String.valueOf(i + i), 0, String.valueOf(i + 1).length(), rect);
            float h = top + rect.height() + (itemHeight - rect.height()) / 2;
            canvas.drawText(String.valueOf(i + 1), 3, h, titlePaint);
            titlePaint.setColor(Color.parseColor("#EC2828"));
            //深度图上的文字
            String price;
            if (BigDecimalUtils.equal(buyList.get(i).getPrice(), new BigDecimal("0"))) {
                price = "——";
            } else {
                price = buyList.get(i).getPrice().setScale(priceDecimal, BigDecimal.ROUND_DOWN).toPlainString();
            }
            titlePaint.getTextBounds(price, 0, price.length(), rect);
            float w2 = viewWidth * 4 / 7 - rect.width();
            canvas.drawText(price, w2, h, titlePaint);
            //深度图上的文字
            titlePaint.setColor(Color.parseColor("#A8AFB7"));
            String number;
            if (BigDecimalUtils.equal(buyList.get(i).getAmount(), new BigDecimal("0"))) {
                number = "——";
            } else {
                number = BigDecimalUtils.fmort(buyList.get(i).getAmount().setScale(numberDecimal, BigDecimal.ROUND_DOWN));
            }
            titlePaint.getTextBounds(number, 0, number.length(), rect);
            float w3 = viewWidth - DisplayUtils.dip2px(context, 17) - rect.width();
            canvas.drawText(number, w3, h, titlePaint);
            paint.reset();
        }
    }



    public void setPriceDecimal(int priceDecimal) {
        this.priceDecimal = priceDecimal;
    }

    public void setNumberDecimal(int numberDecimal) {
        this.numberDecimal = numberDecimal;
    }

    /**
     * 初始化数据
     */
    public void setDate(String json) {
        HandicapEntity entity = JsonUtils.deserialize(json, HandicapEntity.class);
        nowPrice = entity.getPrice();
        BigDecimal lastDecimal = new BigDecimal("0");
        if (entity.getBuyOrders() != null) {
            buyList.clear();
            allBuy = new BigDecimal("0");
            //初始化买盘列表的数据
            for (int i = 0; i < allNumber; i++) {
                if (i < entity.getBuyOrders().size()) {
                    HandicapEntity.OrdersBean bean = entity.getBuyOrders().get(i);
                    bean.setDepth(bean.getAmount().add(lastDecimal));
                    lastDecimal = bean.getDepth();
                    allBuy = allBuy.add(bean.getAmount());
                    buyList.add(bean);
                } else {
                    HandicapEntity.OrdersBean bean = new HandicapEntity.OrdersBean(
                            new BigDecimal("0"),
                            new BigDecimal("0"),
                            new BigDecimal("0").add(lastDecimal)
                    );
                    buyList.add(
                            bean
                    );
                    allBuy = allBuy.add(new BigDecimal("0"));
                }
            }
        }

        if (entity.getSellOrders() != null) {
            sellList.clear();
            allSell = new BigDecimal("0");

            lastDecimal = new BigDecimal("0");
            //初始化卖盘列表的数据
            for (int i = 0; i < allNumber; i++) {
                if (i < entity.getSellOrders().size()) {
                    HandicapEntity.OrdersBean bean = entity.getSellOrders().get(i);
                    bean.setDepth(bean.getAmount().add(lastDecimal));
                    lastDecimal = bean.getDepth();
                    sellList.add(bean);
                    allSell = allSell.add(bean.getAmount());
                } else {
                    sellList.add(
                            new HandicapEntity.OrdersBean(
                                    new BigDecimal("0"),
                                    new BigDecimal("0"),
                                    new BigDecimal("0").add(lastDecimal)
                            )
                    );
                    allSell = allSell.add(new BigDecimal("0"));
                }
            }
        }
        BigDecimal max = BigDecimalUtils.greater(allBuy, allSell) ? allBuy : allSell;
        if (max.compareTo(new BigDecimal(0))>0){
            maxNum = max;
        }

        priceListener.buyPrice(buyList.get(0).getPrice().setScale(priceDecimal, BigDecimal.ROUND_DOWN));
        priceListener.sellPrice(sellList.get(0).getPrice().setScale(priceDecimal, BigDecimal.ROUND_DOWN));
        invalidate();
    }

}
