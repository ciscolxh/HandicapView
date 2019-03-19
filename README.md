
[TOC]
首先我们分析一下盘口图的构成部分，

![image](https://note.youdao.com/yws/api/personal/file/WEBaefc7fe8762a75b65543a505af4c27e7?method=download&shareKey=8852c7eaaa71cc3bc5a9ae6d2e4c9aa6)
# 分析
我们整体分为四部分
1. 盘口标题
2. 卖盘
3. 当前价
4. 买盘

## 标题
    标题部分就是一行文字，分为了左右两部分内容，
    并且只是显示的文字。
    
## 卖盘
    卖盘分为5条数据，从上到下依次是，卖5，卖4，卖3，卖2，卖1。
    并且我们还能看到在卖盘上面还有类似于条形统计图的阴影。
    
    阴影规则为：
    阴影宽度 = (卖1~当前卖盘数)/(卖1~卖5)*控件宽度。
    阴影高度 = (控件高度 - 标题高度 - 价格高度)/10。
    
## 当前价 
    当前价分为上下两部分内容
    上下都是文字，和标题栏差不多，只是颜色不一样而已
    
## 买盘
    买盘和卖盘差不多也是分为5条数据，从上到下依次是买1，买2，买3，买4，买5。
    并且我们还能看到在买盘也有阴影部分。
    
    阴影规则为：
    阴影宽度 = (买1~当前买盘数)/(买1~买5)*控件宽度。
    阴影高度 = 卖盘阴影高度。
    
# 实现
    模块分清楚我们就看看改怎么实现，我们来分步实现
    
## 标题
```
    @Override
    public void drawTitle(Canvas canvas) {
        //获取文字的宽高
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
```
    这里我首先获取到文字的宽高，然后通过文字的宽高（rect的宽高），
    以及文字的
    左（textPaddingStart）,
    右（textPaddingEnd）,
    上下边距（titlePadding），
    定位文字的具体位置然后绘制。
    
## 当前价格

```
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
```
```
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
```

    当前价在布局的中间部分，而买卖盘的高度确定，由此可以通过measureTopViewHeight()定位当前价的位置。
    测量出具体的位置然后进行绘制文字
    
## 卖盘

```
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
```
    卖盘从当前价格向上显示，所以绘制的时候需要倒序绘制（int index = 4 - i），
    然后绘制背景，绘制价格，绘制数量。
    
## 买盘

```
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

```

    买盘和卖盘绘制方式一致，唯一不同的是，买盘是正序绘制
    
### 买卖盘内部组件的绘制

```
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
```

## 点击事件
    点击买盘盘的条目能回调回来选中的价格

```
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

```
    如果没有设置触摸事件监听，不进行出来触摸事件。
    否则监听当前选中的条目进行回调。空条目不进行处理
    
## 数据聚合

```
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
```

    当收到价格变化的消息，通过updateDate()对数据进行适配处理。
    然后通过invalidate()刷新页面。



    


