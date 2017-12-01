package com.cuipengyu.emingren.mbookpage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  cuipengyu 2017/11/30.
 */

public class PageTrunView extends View {
    private static final float TEXT_SIZE_MORMAL = 1 / 40F, TEXT_SIZE_LARGER = 1 / 20F;//标准文字尺寸和大号文字尺寸的占比
    private static final float AUTO_AREA_LEFT = 1 / 5F, AUTO_AREA_RIGHT = 4 / 5F;//控件左右侧自动滑动区域占比
    private static final float MOVE_VALID = 1 / 100F;//移动事件的有效距离占比

    private List<Bitmap> mBitmaps;//位图数据列表
    private TextPaint mTextPaint;//文本画笔
    private int pageIndex;//当前图片位置下标
    private int mViewWidth, mViewHight;//当前控件的宽高
    private float mTextSizeNormal, mTextSizeLarger;//标准文字尺寸，大号文字尺寸
    private float mClipX;//剪裁右端点坐标
    private float mAutoAreaLeft, mAutoAreaRight;//控件左侧和右侧自动吸附的区域
    private float mCurPointX;//触摸屏幕的X点坐标
    private float mMoveValid;//移动的有效距离
    private boolean isNextPage, isLastPage;//是否显示下一页和是否是最后一页
    private Context mContext;

    public PageTrunView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        Log.e("ss", "ss");
        /**
         *   Paint.FILTER_BITMAP_FLAG是使位图过滤的位掩码标志
         *   Paint.ANTI_ALIAS_FLAG是使位图抗锯齿的标志
         *   Paint.DITHER_FLAG是使位图进行有利的抖动的位掩码标志
         *   setTextAlign(Paint.Align align);设置绘制文字的对齐方向
         */

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    //触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //每次触摸是重置为true
        isNextPage = true;
        //判断当前事件类型
        // 可以处理处理多点触摸
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mCurPointX = event.getX();
                Log.e("mCurPointX", mCurPointX + "按下时");
                //如果事件点击位于回滚区域
                if (mCurPointX < mAutoAreaLeft) {
                    //上一页
                    isNextPage = false;
                    pageIndex--;
                    //剪裁右端点坐标X等于触摸点X
                    mClipX = mCurPointX;
                    //重新绘制
                    invalidate();
                }
                break;
            //滑动时
            case MotionEvent.ACTION_MOVE:
                Log.e("mCurPointX", mCurPointX + "移动时");
                //触摸点坐标减去移动点坐标
                float SlideDis = mCurPointX - event.getX();
                Log.e("SlideDis", SlideDis + "移动时");

                //Math.abs计算绝对值
                if (Math.abs(SlideDis) > mMoveValid) {
                    mClipX = event.getX();
                    invalidate();
                }
                break;
            //当触点抬起时
            case MotionEvent.ACTION_UP:
                //判断是否需要自动滑动
                judgeSlideAuto();
                //如果当前页不是最后一页，需要翻到下一页，并且上一页已被clip掉
                if (!isLastPage && isNextPage && mClipX <= 0) {
                    pageIndex++;
                    mClipX = mViewWidth;
                    invalidate();
                }
                break;
        }
        return true;
    }

    //判断是否需要自动滑动，根据当前值判断自动滑动
    private void judgeSlideAuto() {
        //如果裁剪的右端点坐标在控件左端十分之一的区域内，那么就让其自动滑动到控件左端
        if (mClipX < mAutoAreaLeft) {
            while (mClipX > 0) {
                mClipX--;
                invalidate();
            }
        }
        //如果裁剪的右端点坐标在控件右端十分之一的区域内，那么我们直接让其自动滑动到控件右端
        if (mClipX > mAutoAreaRight) {
            while (mClipX < mViewWidth) {
                mClipX++;
                invalidate();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //获取控件宽高
        mViewWidth = w;
        mViewHight = h;
        //初始化图数据
        initBitmaps();
        //计算文字尺寸
        mTextSizeNormal = TEXT_SIZE_MORMAL * mViewHight;
        mTextSizeLarger = TEXT_SIZE_LARGER * mViewHight;
        //初始化裁剪右端点坐标
        mClipX = mViewWidth;
        //计算控件左侧和右侧自动吸附的区域
        mAutoAreaLeft = mViewWidth * AUTO_AREA_LEFT;
        mAutoAreaRight = mViewWidth * AUTO_AREA_RIGHT;
        //计算移动的有效距离
        mMoveValid = mViewWidth * MOVE_VALID;
    }

    private void initBitmaps() {
        List<Bitmap> temp = new ArrayList<>();
        for (int i = mBitmaps.size() - 1; i >= 0; i--) {
            Bitmap bitmap = Bitmap.createScaledBitmap(mBitmaps.get(i), mViewWidth, mViewHight, true);
            temp.add(bitmap);
        }
        mBitmaps = temp;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null == mBitmaps || mBitmaps.size() == 0) {
            defaultDisplay(canvas);
            return;
        }
        drawBitmaps(canvas);
    }

    //绘制位图
    private void drawBitmaps(Canvas canvas) {
        //绘制前重置
        isLastPage = false;
        //限制pageIndex范围
        pageIndex = pageIndex < 0 ? 0 : pageIndex;
        pageIndex = pageIndex > mBitmaps.size() ? mBitmaps.size() : pageIndex;
        //计算数据起始位置
        int start = mBitmaps.size() - 2 - pageIndex;
        int end = mBitmaps.size() - pageIndex;
        //如果数据起始位置小于0则表示当前已经是最后一张图片
        if (start < 0) {
            //此时设置最后一页为true
            isLastPage = true;
            Toast.makeText(mContext, "没有更多了兄弟", Toast.LENGTH_LONG).show();

            //强制重置起始位置
            start = 0;
            end = 1;
        }
        for (int i = start; i < end; i++) {
            canvas.save();
            //仅裁剪位于最顶层的画布区域，如果到了末页则不再执行裁剪
            if (!isLastPage && i == end - 1) {
                canvas.clipRect(0, 0, mClipX, mViewHight);
            }
            canvas.drawBitmap(mBitmaps.get(i), 0, 0, null);
            canvas.restore();

        }

    }

    public synchronized void setBitmaps(List<Bitmap> bitmaps) {
        //进行数据判断
        if (null == bitmaps || bitmaps.size() == 0) {
            throw new IllegalArgumentException("没有数据");
        }
        if (bitmaps.size() < 2) {
            throw new IllegalArgumentException("数据长度小于②");
        }
        this.mBitmaps = bitmaps;
        invalidate();

    }

    private void defaultDisplay(Canvas canvas) {
         canvas.drawColor(Color.WHITE);

         mTextPaint.setTextSize(mTextSizeLarger);
         mTextPaint.setColor(Color.RED);
         canvas.drawText("绘制文本",mViewWidth/2,mViewHight/2,mTextPaint);

        mTextPaint.setTextSize(mTextSizeLarger);
        mTextPaint.setColor(Color.BLACK);
        canvas.drawText("没有数据",mViewWidth/2,mViewHight/3,mTextPaint);
    }
}
