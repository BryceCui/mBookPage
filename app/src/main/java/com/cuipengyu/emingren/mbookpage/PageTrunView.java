package com.cuipengyu.emingren.mbookpage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by  cuipengyu 2017/11/30.
 */

public class PageTrunView extends View {
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
        Log.e("ss","ss");
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


        }
        return super.onTouchEvent(event);
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

    }

    private void defaultDisplay(Canvas canvas) {

    }
}
