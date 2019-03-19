package com.loveluo.arcview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.loveluo.arcview.R;
import com.loveluo.arcview.entity.HandicapEntity;
import com.loveluo.arcview.utils.BigDecimalUtils;
import com.loveluo.arcview.utils.JsonUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HandicapView extends View implements HandicapViewInterface {

    private final String TAG = "HandicapView";
    /**
     * 布局宽度
     */
    private int viewWidth;
    /**
     * 布局高度
     */
    private int viewHeight;
    /**
     * 上下文
     */
    private Context context;
    /**
     * 买入背景色
     */
    private int buyBgColor;
    /**
     * 卖出背景色
     */
    private int sellBgColor;
    /**
     * 买入文字颜色
     */
    private int buyTextColor;
    /**
     * 卖出文字颜色
     */
    private int sellTextColor;
    /**
     * 标题文字
     */
    private String[] titles;
    /**
     * 画背景的画笔
     */
    private Paint paint;
    /**
     * 画文字的画笔
     */
    private TextPaint titlePaint;
    /**
     * 背景的矩形
     */
    private Rect rect;

    private BigDecimal nowPrice = new BigDecimal(0);

    private List<HandicapEntity.OrdersBean> buyList = new ArrayList<>();

    private List<HandicapEntity.OrdersBean> sellList = new ArrayList<>();

    private BigDecimal max = new BigDecimal(0);

    /**
     * 人民币汇率自己初始化
     */
    private BigDecimal rate = new BigDecimal(7);

    /**
     * 中间部分的高度
     */
    private int middleHeight;

    /**
     * 盘口上半部分的高度
     */
    private int topViewHeight;

    /**
     * 标题栏的高度
     */
    private int titleViewHeight;
    private int itemHeight;
    private int titleColor;
    private int itemNumberColor;
    private int titleSize;
    private int itemSize;
    private int aboutPriceSize;
    private int nowPriceSize;
    private int nowPricePadding;
    private int nowPriceLineHeight;
    private int titlePadding;
    private int textPaddingStart;
    private int textPaddingEnd;

    private SelectItemListener selectItemListener;

    public HandicapView(Context context) {
        this(context, null);
    }

    public HandicapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HandicapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        if (initType(context, attrs)) {
            return;
        }
        initPaint();
        initTitle();
        initData();
    }

    public void setSelectItemListener(SelectItemListener selectItemListener) {
        this.selectItemListener = selectItemListener;
    }

    private boolean initType(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HandicapView);
        buyBgColor = typedArray.getResourceId(R.styleable.HandicapView_buy_bg_color, ContextCompat.getColor(context, R.color.colorBuy));
        sellBgColor = typedArray.getResourceId(R.styleable.HandicapView_sell_bg_color, ContextCompat.getColor(context, R.color.colorSell));
        buyTextColor = typedArray.getResourceId(R.styleable.HandicapView_buy_text_color, ContextCompat.getColor(context, R.color.colorTextBuy));
        sellTextColor = typedArray.getResourceId(R.styleable.HandicapView_sell_text_color, ContextCompat.getColor(context, R.color.colorTextSell));
        titleColor = typedArray.getResourceId(R.styleable.HandicapView_title_text_color, ContextCompat.getColor(context, R.color.colorTitleColor));
        itemNumberColor = typedArray.getResourceId(R.styleable.HandicapView_item_number_color, ContextCompat.getColor(context, R.color.colorTitleColor));
        titleSize = (int) typedArray.getDimension(R.styleable.HandicapView_title_text_size, getDimension(R.dimen.handicap_title_size));
        titlePadding = (int) typedArray.getDimension(R.styleable.HandicapView_title_text_padding, getDimension(R.dimen.handicap_title_text_padding));
        itemSize = (int) typedArray.getDimension(R.styleable.HandicapView_item_text_size, getDimension(R.dimen.handicap_item_size));
        aboutPriceSize = (int) typedArray.getDimension(R.styleable.HandicapView_now_price_size, getDimension(R.dimen.handicap_about_price_size));
        nowPriceSize = (int) typedArray.getDimension(R.styleable.HandicapView_now_price_size, getDimension(R.dimen.handicap_now_price_size));
        nowPricePadding = (int) typedArray.getDimension(R.styleable.HandicapView_now_price_padding, getDimension(R.dimen.handicap_now_price_padding));
        nowPriceLineHeight = (int) typedArray.getDimension(R.styleable.HandicapView_now_price_line_height, getDimension(R.dimen.handicap_now_price_height));
        textPaddingStart = (int) typedArray.getDimension(R.styleable.HandicapView_text_padding_start, getDimension(R.dimen.handicap_text_padding_start));
        textPaddingEnd = (int) typedArray.getDimension(R.styleable.HandicapView_text_padding_end, getDimension(R.dimen.handicap_text_padding_end));

        typedArray.recycle();
        return false;
    }

    private float getDimension(@DimenRes int resId) {
        return getResources().getDimension(resId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        drawTitle(canvas);
        drawNowPrice(canvas);
        drawHandicapTopView(canvas);
        drawHandicapDownView(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        Log.e(TAG, "new W :" + w);
        Log.e(TAG, "new H" + h);
    }

    @Override
    public void initPaint() {
        paint = new Paint();
        //开启抗锯齿
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        titlePaint = new TextPaint();
        //初始化标题的画笔
        titlePaint.setAntiAlias(true);
        titlePaint.setDither(true);
        titlePaint.setStrokeWidth(3);
        titlePaint.setTextSize(titleSize);
        titlePaint.setColor(titleColor);
        rect = new Rect();
    }

    @Override
    public void initData() {
        for (int i = 0; i < 5; i++) {
            buyList.add(new HandicapEntity.OrdersBean());
            sellList.add(new HandicapEntity.OrdersBean());
        }
    }

    @Override
    public void initTitle() {
        titles = new String[]{context.getString(R.string.price), context.getString(R.string.number)};
    }

    @Override
    public void drawTitle(Canvas canvas) {
        //获取文字所在的矩形
        titlePaint.getTextBounds(titles[0], 0, titles[0].length(), rect);
        // 文本的上边距配置
        int h = titlePadding + rect.height();
        canvas.drawText(titles[0], textPaddingStart, h, titlePaint);

        titlePaint.getTextBounds(titles[1], 0, titles[1].length(), rect);
        //起始位置 = view的宽度 - 文字的宽度 - 右边边距的宽度

        int w = viewWidth - textPaddingEnd - rect.width();
        canvas.drawText(titles[1], w, h, titlePaint);

        titleViewHeight = h + titlePadding;
    }

    @Override
    public void drawNowPrice(Canvas canvas) {
        measureTopViewHeight(nowPricePadding, nowPriceLineHeight);
        titlePaint.setTextSize(nowPriceSize);
        titlePaint.getTextBounds(nowPrice.toPlainString(), 0, nowPrice.toPlainString().length(), rect);
        int h = titleViewHeight + topViewHeight + nowPricePadding + rect.height();
        canvas.drawText(nowPrice.toPlainString(), textPaddingStart, h, titlePaint);
        titlePaint.setTextSize(aboutPriceSize);
        titlePaint.getTextBounds(nowPrice.toPlainString(), 0, nowPrice.toPlainString().length(), rect);
        h = h + nowPriceLineHeight + rect.height();
        //todo 自己将≈配置到string.xml
        canvas.drawText("≈ " + nowPrice.toPlainString(), textPaddingStart, h, titlePaint);
    }

    /**
     * 测量
     *
     * @param nowPriceHeight  文字的上下边距
     * @param nowPriceSpacing 文字的中间边距
     */
    private void measureTopViewHeight(int nowPriceHeight, int nowPriceSpacing) {
        // 计算当前价格的文字大小
        //当前价格的大小
        titlePaint.setTextSize(nowPriceSize);
        titlePaint.getTextBounds(nowPrice.toPlainString(), 0, nowPrice.toPlainString().length(), rect);
        int h1 = rect.height();
        //价格
        titlePaint.setTextSize(aboutPriceSize);
        titlePaint.getTextBounds(nowPrice.toPlainString(), 0, nowPrice.toPlainString().length(), rect);
        int h2 = rect.height();
        middleHeight = h1 + h2 + 2 * nowPriceHeight + nowPriceSpacing;

        topViewHeight = (viewHeight - titleViewHeight - middleHeight) / 2;
    }

    @Override
    public void drawHandicapTopView(Canvas canvas) {
        paint.setColor(sellBgColor);
        titlePaint.setTextSize(itemSize);
        itemHeight = topViewHeight / 5;
        int viewTopNoTitle = 0;
        int viewTop = titleViewHeight;
        for (float i = 0; i < buyList.size(); i++) {
            // 卖盘需要倒序绘制
            int index = 4 - (int) i;
            //这里取出价格
            String price = getPrice(index, sellList);

            //这里取出价格
            String number = getNumber(index, sellList);

            BigDecimal depth = sellList.get(index) == null ? null : sellList.get(index).getDepth();
            //draw BG 左上右下
            drawBg(canvas, i, depth, viewTopNoTitle);

            // draw price
            int textBottom = drawPrice(canvas, viewTop, (int) i, price, sellTextColor);

            // draw Number
            drawNumber(canvas, number, textBottom);
        }

    }


    @Override
    public void drawHandicapDownView(Canvas canvas) {
        paint.setColor(buyBgColor);

        int viewTopNoTitle = topViewHeight + middleHeight;
        //下部分盘口距离顶部的距离
        int viewTop = titleViewHeight + viewTopNoTitle;

        for (float i = 0; i < buyList.size(); i++) {
            int index = (int) i;

            //这里取出价格
            String price = getPrice(index, buyList);

            //这里取出价格
            String number = getNumber(index, buyList);

            BigDecimal depth = buyList.get(index) == null ? null : buyList.get(index).getDepth();

            //draw BG 左上右下
            drawBg(canvas, i, depth, viewTopNoTitle);

            // draw price
            int textBottom = drawPrice(canvas, viewTop, (int) i, price, buyTextColor);

            // draw Number
            drawNumber(canvas, number, textBottom);
        }
    }

    /**
     * 获取数量
     *
     * @param index index
     * @return 数量
     */
    private String getNumber(int index, List<HandicapEntity.OrdersBean> list) {
        String number;
        if (max.floatValue() == 0) {
            number = "--";
        } else if (list.get(index) != null && list.get(index).getAmount() != null) {
            number = list.get(index).getAmount().toPlainString();
        } else {
            number = "--";
        }
        return number;
    }

    /**
     * 获取价格
     *
     * @param index index
     * @return price
     */
    private String getPrice(int index, List<HandicapEntity.OrdersBean> list) {
        String price;
        if (max.floatValue() == 0) {
            price = "--";
        } else if (list.get(index) != null && list.get(index).getPrice() != null) {
            price = list.get(index).getPrice().toPlainString();
        } else {
            price = "--";
        }
        return price;
    }


    /**
     * 画出盘口背景
     *
     * @param canvas canvas
     * @param i      第几条
     * @param number number
     */
    private void drawBg(Canvas canvas, float i, BigDecimal number, int top) {
        //max == 0 说明是空数据。 number=null也是空数据
        if (number != null && max.floatValue() != 0) {
            //left  = (1 - num/max) * viewWight;

            int left = (int) ((new BigDecimal(1).subtract(number.divide(max, 4, BigDecimal.ROUND_DOWN))).floatValue() * viewWidth);
            Rect rectBg = new Rect(
//                (int) (paddingStart + ((5f - i) / 5) * itemHeight),
                    left,
                    titleViewHeight + itemHeight * (int) i + top,
                    viewWidth,
                    ((int) i + 1) * itemHeight + titleViewHeight + top);
            canvas.drawRect(rectBg, paint);
        }

    }

    /**
     * 画出盘口条目中的价格
     *
     * @param canvas  canvas
     * @param viewTop 盘口距离顶部的高度
     * @param i       第几个条目
     * @param price   价格
     * @param color   文字颜色
     * @return 返回当前条目文字的底部距离 给绘制数量使用
     */
    private int drawPrice(Canvas canvas, int viewTop, int i, String price, int color) {
        titlePaint.getTextBounds(price, 0, price.length(), rect);
        //当前item中文字底部的高度 = 条目的一半 + 文字的一半
        int textBottom = viewTop + itemHeight * i + (itemHeight / 2 + rect.height() / 2);
        //下面的条目的高度需要算出每个条目文字的底部距离
        titlePaint.setColor(color);
        canvas.drawText(price, textPaddingStart, textBottom, titlePaint);
        return textBottom;
    }


    /**
     * 画出盘口条目中的数量
     *
     * @param canvas     canvas
     * @param number     数量
     * @param textBottom 文字的绘制Y坐标
     */
    private void drawNumber(Canvas canvas, String number, int textBottom) {
        titlePaint.getTextBounds(number, 0, number.length(), rect);
        int w = viewWidth - textPaddingEnd - rect.width();
        titlePaint.setColor(itemNumberColor);
        canvas.drawText(number, w, textBottom, titlePaint);
    }


    /**
     * 重写触摸事件实现点击回调回来价格
     *
     * @param event event
     * @return 拦截触摸事件
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (selectItemListener != null) {
            Log.e(TAG, "类型：" + String.valueOf(event.getAction()));
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    int y = (int) event.getY();
                    int x = (int) event.getX();
                    //手势滑动到外面以后不处理
                    if (x > 0 && x < viewWidth && y > 0 && y < viewHeight) {
                        int h = titleViewHeight + topViewHeight + middleHeight;
                        // 判断如果点击标题或者中间显示价格部分 不处理
                        if (y < titleViewHeight || (y > titleViewHeight + topViewHeight && y < h)) {
                            return super.onTouchEvent(event);
                        } else if (y < h) {//盘口上半部分
                            int selectItem = (y - titleViewHeight) / itemHeight;
                            Log.e(TAG, "点击了买入第几条：" + selectItem);
                            if (buyList != null && buyList.get(selectItem) != null) {
                                selectItemListener.selectPrice(buyList.get(selectItem).getPrice().toPlainString());
                            }
                        } else if (y > h) {
                            int selectItem = (y - titleViewHeight - topViewHeight - middleHeight) / itemHeight;
                            Log.e(TAG, "点击了卖出第几条：" + selectItem);
                            if (sellList != null && sellList.get(selectItem) != null) {
                                selectItemListener.selectPrice(sellList.get(selectItem).getPrice().toPlainString());
                            }

                        }
                    }
                    break;
                default:
                    break;
            }
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }


    /**
     * 聚合数据 刷新页面
     *
     * @param json json
     */
    @Override
    public void updateData(String json) {
        HandicapEntity entity = JsonUtils.deserialize(json, HandicapEntity.class);
        nowPrice = entity.getPrice();
        BigDecimal lastDecimal = new BigDecimal("0");
        BigDecimal allBuy = new BigDecimal("0");
        if (entity.getBuyOrders() != null) {
            buyList.clear();
            allBuy = new BigDecimal("0");
            //初始化买盘列表的数据
            for (int i = 0; i < 5; i++) {
                if (i < entity.getBuyOrders().size()) {
                    HandicapEntity.OrdersBean bean = entity.getBuyOrders().get(i);
                    bean.setDepth(bean.getAmount().add(lastDecimal));
                    lastDecimal = bean.getDepth();
                    allBuy = allBuy.add(bean.getAmount());
                    buyList.add(bean);
                } else {

                    buyList.add(
                            null
                    );

                }
            }
        }

        BigDecimal allSell = new BigDecimal(0);
        if (entity.getSellOrders() != null) {
            sellList.clear();
            allSell = new BigDecimal("0");

            lastDecimal = new BigDecimal("0");
            //初始化卖盘列表的数据
            for (int i = 0; i < 5; i++) {
                if (i < entity.getSellOrders().size()) {
                    HandicapEntity.OrdersBean bean = entity.getSellOrders().get(i);
                    bean.setDepth(bean.getAmount().add(lastDecimal));
                    lastDecimal = bean.getDepth();
                    sellList.add(bean);
                    allSell = allSell.add(bean.getAmount());
                } else {

                    sellList.add(
                            null
                    );

                }
            }
        }
        BigDecimal max = BigDecimalUtils.greater(allBuy, allSell) ? allBuy : allSell;
        if (max.compareTo(new BigDecimal(0)) > 0) {
            this.max = max;
        }
        // 刷新页面
        invalidate();
    }

    public interface SelectItemListener {
        void selectPrice(String selectPrice);
    }
}
